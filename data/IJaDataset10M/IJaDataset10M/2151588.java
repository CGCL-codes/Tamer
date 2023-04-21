package org.ascape.query.parser;

import org.ascape.query.Validated;

public class QTValue extends QTCriteriaSubNode implements Validated {

    private String stringValue;

    private Object coercedValue;

    private Comparor comparor;

    public QTValue(int i) {
        super(i);
    }

    public QTValue(BoolExprTree p, int i) {
        super(p, i);
    }

    interface Comparor {

        public int compareTo(Object value);
    }

    public Comparor createComparor() {
        if (coercedValue instanceof Integer) {
            return new Comparor() {

                public int compareTo(Object value) {
                    return ((Integer) coercedValue).compareTo((Integer) value);
                }
            };
        } else if (coercedValue instanceof Double) {
            return new Comparor() {

                public int compareTo(Object value) {
                    return ((Double) coercedValue).compareTo((Double) value);
                }
            };
        } else if (coercedValue instanceof Float) {
            return new Comparor() {

                public int compareTo(Object value) {
                    return ((Float) coercedValue).compareTo((Float) value);
                }
            };
        } else if (coercedValue instanceof Long) {
            return new Comparor() {

                public int compareTo(Object value) {
                    return ((Long) coercedValue).compareTo((Long) value);
                }
            };
        } else if (coercedValue instanceof String) {
            return new Comparor() {

                public int compareTo(Object value) {
                    return ((String) coercedValue).compareTo((String) value);
                }
            };
        } else if (coercedValue instanceof Boolean) {
            return new Comparor() {

                public int compareTo(Object value) {
                    return ((Boolean) coercedValue).equals(value) ? 0 : -1;
                }
            };
        } else {
            throw new InternalError("Unexpected coerced value type for compareTo op: " + coercedValue.getClass());
        }
    }

    public void validate(Object object) throws ParseException {
        Class type = getCriteria().getProperty().getType();
        try {
            if ((type == Integer.TYPE) || (type == Integer.class)) {
                coercedValue = Integer.valueOf(stringValue);
            } else if ((type == Double.TYPE) || (type == Double.class)) {
                coercedValue = Double.valueOf(stringValue);
            } else if ((type == Float.TYPE) || (type == Float.class)) {
                coercedValue = Float.valueOf(stringValue);
            } else if ((type == Long.TYPE) || (type == Long.class)) {
                coercedValue = Long.valueOf(stringValue);
            } else if ((type == Boolean.TYPE) || (type == Boolean.class)) {
                if ((stringValue.equals("true")) || (stringValue.equals("false"))) {
                    coercedValue = Boolean.valueOf(stringValue);
                } else {
                    throw new ParseException("Unrecognized format for field: " + getCriteria().getProperty().getName() + ". Can't convert " + stringValue + " to boolean. (Use \"true\" or \"false\".)");
                }
            } else {
                coercedValue = stringValue;
            }
        } catch (NumberFormatException e) {
            throw new ParseException("Unrecognized number format for field: " + getCriteria().getProperty().getName() + ". Can't convert " + stringValue + " to " + type + ".");
        }
        comparor = createComparor();
    }

    public String getValue() {
        return stringValue;
    }

    public void setValue(String value) {
        this.stringValue = value;
    }

    public Object getCoercedValue() {
        return coercedValue;
    }

    public String toString() {
        return stringValue;
    }

    public Comparor getComparor() {
        return comparor;
    }
}
