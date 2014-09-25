package io.prismic;

import org.joda.time.DateTime;

import java.util.List;

class SimplePredicate implements Predicate {

  private final String name;
  private final String fragment;
  private final Object value1;
  private final Object value2;
  private final Object value3;

  SimplePredicate(String name, String fragment, Object value1) {
    this(name, fragment, value1, null, null);
  }

  SimplePredicate(String name, String fragment, Object value1, Object value2) {
    this(name, fragment, value1, value2, null);
  }

  SimplePredicate(String name, String fragment, Object value1, Object value2, Object value3) {
    this.name = name;
    this.fragment = fragment;
    this.value1 = value1;
    this.value2 = value2;
    this.value3 = value3;
  }

  @Override
  public String q() {
    String result = "[:d = " + this.name + "(";
    if ("similar".equals(this.name)) {
      result += ("\"" + this.fragment + "\"");
    } else {
      result += this.fragment;
    }
    result += ", " + serializeField(value1);
    if (value2 != null) {
      result += ", " + serializeField(value2);
    }
    if (value3 != null) {
      result += ", " + serializeField(value3);
    }
    result += ")]";
    return result;
  }

  private static String serializeField(Object value) {
    if (value instanceof List) {
      return "[" + join((List)value, ",") + "]";
    } else if (value instanceof Predicates.Month) {
      return ("\"" + capitalize(((Predicates.Month) value).name()) + "\"");
    } else if (value instanceof Predicates.DayOfWeek) {
      return ("\"" + capitalize(((Predicates.DayOfWeek) value).name()) + "\"");
    } else if (value instanceof String) {
      return ("\"" + value + "\"");
    } else if (value instanceof DateTime) {
      return Long.toString(((DateTime) value).getMillis());
    } else {
      return value.toString();
    }
  }

  private static <T> String join(List<T> l, String sep) {
    if (l.size() == 0) return "";
    String result = "";
    boolean first = true;
    for (T elt: l) {
      if (first) {
        first = false;
      } else {
        result += sep;
      }
      result += "\"" + elt + "\"";
    }
    return result;
  }

  private static String capitalize(String line) {
    if (line == null || "".equals(line)) return "";
    return Character.toUpperCase(line.charAt(0)) + line.substring(1).toLowerCase();
  }

}