package uk.org.ogsadai.relational;

import java.math.BigDecimal;
import java.util.Comparator;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.tuple.TupleTypes;
import uk.org.ogsadai.tuple.TypeMismatchException;

/**
 * Utility methods used by some relational and DQP activities.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class RelationalUtils {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2008";

    /** Logger. */
    private static final DAILogger LOG = DAILogger.getLogger(RelationalUtils.class);

    /**
     * Private constructor. This class has only static methods.
     */
    private RelationalUtils() {
    }

    /**
     * Returns a comparator which compares objects of given tuple types.
     * 
     * @param typeA
     *            tuple type of the first comparison object
     * @param typeB
     *            tuple type of the second comparison object
     * @return compares objects of the given tuple types
     * @throws TypeMismatchException
     *      when comparison is not possible
     */
    public static Comparator<Object> getComparator(int typeA, int typeB) throws TypeMismatchException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("in getComparator");
            LOG.debug("Tuple Type 1: " + typeA);
            LOG.debug("Tuple Type 2 " + typeB);
        }
        if (invalidType(typeA) || invalidType(typeB)) {
            throw new TypeMismatchException(typeA, typeB);
        } else if (equalTypes(typeA, typeB)) {
            return equalTypeComparator(typeA, typeB);
        } else if (isIntegerType(typeA)) {
            return getCompareIntegerTo(typeA, typeB);
        } else if (isFloatingPointType(typeA)) {
            return getCompareFloatTo(typeA, typeB);
        } else if (typeA == TupleTypes._BIGDECIMAL) {
            return getCompareBigDecimalTo(typeA, typeB);
        } else if (typeA == TupleTypes._STRING) {
            return getCompareStringTo(typeA, typeB);
        } else if (typeA == TupleTypes._CHAR) {
            return getCompareCharsTo(typeA, typeB);
        } else {
            throw new TypeMismatchException(typeA, typeB);
        }
    }

    /**
     * Gets comparator for two equal types.
     * 
     * @param typeA
     *            first type
     * @param typeB
     *            second type
     * @return comparator
     */
    private static Comparator<Object> equalTypeComparator(int typeA, int typeB) {
        if (typeA == TupleTypes._CHAR) {
            return new Comparator<Object>() {

                public int compare(Object obj1, Object obj2) {
                    return obj1.toString().compareTo(obj2.toString());
                }
            };
        } else {
            return new Comparator<Object>() {

                @SuppressWarnings("unchecked")
                public int compare(Object obj1, Object obj2) {
                    return ((Comparable) obj1).compareTo(obj2);
                }
            };
        }
    }

    /**
     * Checks if two types are equal.
     * 
     * @param typeA
     *            first type
     * @param typeB
     *            second type
     * @return <code>true</code> if two types are equal
     */
    private static boolean equalTypes(int typeA, int typeB) {
        return (typeA == typeB);
    }

    /**
     * Checks if type is invalid.
     * 
     * @param type
     *            type to check
     * @return <code>true</code> if invalid type is used
     */
    private static boolean invalidType(int type) {
        return ((type == TupleTypes._FILE) || (type == TupleTypes._ODBLOB) || (type == TupleTypes._ODCLOB));
    }

    /**
     * Returns <code>true</code> if the type is one of SHORT, INT, or LONG.
     * 
     * @param type
     *            tuple type
     * @return <code>true</code> if the type represents an integer type
     */
    private static boolean isIntegerType(int type) {
        return (type == TupleTypes._SHORT || type == TupleTypes._INT || type == TupleTypes._LONG);
    }

    /**
     * Returns <code>true</code> if the given type is a float or a double.
     * 
     * @param type
     *            tuple type
     * @return <code>true</code> if the type represents a floating point type
     */
    private static boolean isFloatingPointType(int type) {
        return (type == TupleTypes._FLOAT || type == TupleTypes._DOUBLE);
    }

    private static boolean isString(int type) {
        return (type == TupleTypes._STRING);
    }

    private static Comparator<Object> getCompareIntegerTo(int typeA, int typeB) throws TypeMismatchException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Tuple Type 1 is an integer type");
        }
        if (isIntegerType(typeB)) {
            return new Comparator<Object>() {

                public int compare(Object num1, Object num2) {
                    return new Long(((Number) num1).longValue()).compareTo(((Number) num2).longValue());
                }
            };
        } else if (isFloatingPointType(typeB)) {
            return new Comparator<Object>() {

                public int compare(Object num1, Object num2) {
                    return new Double(((Number) num1).doubleValue()).compareTo(((Number) num2).doubleValue());
                }
            };
        } else if (typeB == TupleTypes._BIGDECIMAL) {
            return new Comparator<Object>() {

                public int compare(Object num1, Object num2) {
                    BigDecimal b1 = new BigDecimal(((Number) num1).longValue());
                    BigDecimal b2 = (BigDecimal) num2;
                    return b1.compareTo(b2);
                }
            };
        } else if (typeB == TupleTypes._STRING) {
            return new Comparator<Object>() {

                public int compare(Object num1, Object num2) {
                    return new Long(((Number) num1).longValue()).compareTo(Long.parseLong((String) num2));
                }
            };
        } else if (typeB == TupleTypes._CHAR) {
            return new Comparator<Object>() {

                public int compare(Object num1, Object num2) {
                    return new Long(((Number) num1).longValue()).compareTo(Long.parseLong(new String((char[]) num2)));
                }
            };
        }
        throw new TypeMismatchException(typeA, typeB);
    }

    private static Comparator<Object> getCompareFloatTo(int typeA, int typeB) throws TypeMismatchException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Tuple Type 1 is an floating type");
        }
        if (isIntegerType(typeB) || isFloatingPointType(typeB)) {
            return new Comparator<Object>() {

                public int compare(Object num1, Object num2) {
                    return new Double(((Number) num1).doubleValue()).compareTo(((Number) num2).doubleValue());
                }
            };
        } else if (typeB == TupleTypes._BIGDECIMAL) {
            return new Comparator<Object>() {

                public int compare(Object num1, Object num2) {
                    BigDecimal b1 = BigDecimal.valueOf(((Number) num1).doubleValue());
                    BigDecimal b2 = (BigDecimal) num2;
                    return b1.compareTo(b2);
                }
            };
        } else if (typeB == TupleTypes._STRING) {
            return new Comparator<Object>() {

                public int compare(Object num1, Object num2) {
                    return new Double(((Number) num1).doubleValue()).compareTo(Double.parseDouble((String) num2));
                }
            };
        } else if (typeB == TupleTypes._CHAR) {
            return new Comparator<Object>() {

                public int compare(Object num1, Object num2) {
                    return new Double(((Number) num1).doubleValue()).compareTo(Double.parseDouble(new String((char[]) num2)));
                }
            };
        }
        throw new TypeMismatchException(typeA, typeB);
    }

    private static Comparator<Object> getCompareBigDecimalTo(int typeA, int typeB) throws TypeMismatchException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Tuple Type 1: " + typeA + " is an bigdecimal type");
        }
        if (isIntegerType(typeB)) {
            return new Comparator<Object>() {

                public int compare(Object num1, Object num2) {
                    BigDecimal b1 = (BigDecimal) num1;
                    BigDecimal b2 = new BigDecimal(((Number) num2).longValue());
                    return b1.compareTo(b2);
                }
            };
        } else if (isFloatingPointType(typeB)) {
            return new Comparator<Object>() {

                public int compare(Object num1, Object num2) {
                    BigDecimal b1 = (BigDecimal) num1;
                    BigDecimal b2 = BigDecimal.valueOf(((Number) num2).doubleValue());
                    return b1.compareTo(b2);
                }
            };
        } else if (isString(typeB)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Tuple Type 1: " + typeA + " is an bigdeimcal type typeB is string");
            }
            return new Comparator<Object>() {

                public int compare(Object num1, Object num2) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("in compare operation of bigdecimal to string comparison");
                        LOG.debug("Num1 is type: " + num1.getClass().getName());
                        LOG.debug("Num2 is type: " + num2.getClass().getName());
                        LOG.debug("Num1 is " + num1);
                        LOG.debug("Num2 is " + num2);
                    }
                    try {
                        BigDecimal b1;
                        if (!(num1 instanceof BigDecimal)) {
                            b1 = new BigDecimal((String) num1);
                        } else {
                            b1 = new BigDecimal(((Number) num1).doubleValue());
                        }
                        BigDecimal b2;
                        if (!(num2 instanceof BigDecimal)) {
                            b2 = new BigDecimal((String) num2);
                        } else {
                            b2 = new BigDecimal(((Number) num2).doubleValue());
                        }
                        return b1.compareTo(b2);
                    } catch (NumberFormatException nfe) {
                        LOG.debug(nfe.getMessage());
                        throw new ClassCastException();
                    }
                }
            };
        } else if (typeB == TupleTypes._CHAR) {
            return new Comparator<Object>() {

                public int compare(Object num1, Object num2) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("in compare operation of bigdecimal to string comparison");
                        LOG.debug("Num1 is type: " + num1.getClass().getName());
                        LOG.debug("Num2 is type: " + num2.getClass().getName());
                        LOG.debug("Num1 is " + num1);
                        LOG.debug("Num2 is " + num2);
                    }
                    try {
                        BigDecimal b1;
                        BigDecimal b2;
                        b1 = new BigDecimal(((Number) num1).doubleValue());
                        b2 = new BigDecimal(new String((char[]) num2));
                        return b1.compareTo(b2);
                    } catch (NumberFormatException nfe) {
                        LOG.debug(nfe.getMessage());
                        throw new ClassCastException();
                    }
                }
            };
        }
        throw new TypeMismatchException(typeA, typeB);
    }

    private static Comparator<Object> getCompareStringTo(int typeA, int typeB) throws TypeMismatchException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("String or string statment");
        }
        if (typeB == TupleTypes._CHAR) {
            return new Comparator<Object>() {

                public int compare(Object num1, Object num2) {
                    return ((String) num1).compareTo(new String((char[]) num2));
                }
            };
        } else if (isIntegerType(typeB)) {
            return new Comparator<Object>() {

                public int compare(Object num1, Object num2) {
                    return new Long(Long.parseLong((String) num1)).compareTo(((Number) num2).longValue());
                }
            };
        } else if (isFloatingPointType(typeB)) {
            return new Comparator<Object>() {

                public int compare(Object num1, Object num2) {
                    return new Double(Double.parseDouble((String) num1)).compareTo(((Number) num2).doubleValue());
                }
            };
        } else if (typeB == TupleTypes._BIGDECIMAL) {
            return new Comparator<Object>() {

                public int compare(Object num1, Object num2) {
                    BigDecimal b1 = new BigDecimal((String) num1);
                    BigDecimal b2 = (BigDecimal) num2;
                    return b1.compareTo(b2);
                }
            };
        }
        throw new TypeMismatchException(typeA, typeB);
    }

    private static Comparator<Object> getCompareCharsTo(int typeA, int typeB) throws TypeMismatchException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("char or char");
        }
        if (typeB == TupleTypes._STRING) {
            return new Comparator<Object>() {

                public int compare(Object op1, Object op2) {
                    return new String((char[]) op1).compareTo((String) op2);
                }
            };
        } else if (isIntegerType(typeB)) {
            return new Comparator<Object>() {

                public int compare(Object num1, Object num2) {
                    return new Long(Long.parseLong(new String((char[]) num1))).compareTo(((Number) num2).longValue());
                }
            };
        } else if (isFloatingPointType(typeB)) {
            return new Comparator<Object>() {

                public int compare(Object num1, Object num2) {
                    return new Double(Double.parseDouble(new String((char[]) num1))).compareTo(((Number) num2).doubleValue());
                }
            };
        } else if (typeB == TupleTypes._BIGDECIMAL) {
            return new Comparator<Object>() {

                public int compare(Object num1, Object num2) {
                    BigDecimal b1 = new BigDecimal(new String((char[]) num1));
                    BigDecimal b2 = (BigDecimal) num2;
                    return b1.compareTo(b2);
                }
            };
        }
        throw new TypeMismatchException(typeA, typeB);
    }
}
