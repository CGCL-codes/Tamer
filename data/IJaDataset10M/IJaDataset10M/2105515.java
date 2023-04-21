package com.jcorporate.expresso.core.dbobj;

import com.jcorporate.expresso.core.cache.Cacheable;
import com.jcorporate.expresso.core.misc.StringUtil;

/**
 * A valid value is a enumerated collection very similar to a pure
 * Struts label and value bean. It typically represent an item of data
 * that is displayed in a drop-down list box or a menu selection.
 * A valid value has a real value as known as a key and description.
 *
 *
 * <p>
 *
 * To support internationalisation (i18n) the look at the subclass
 * <code>ISOValidValue</code>.
 *
 * </p>
 *
 *
 * <p>
 *
 * This class also contains two very useful static inner classes
 * <code>ValueComparator</code> and
 * <code>DescriptionComparator</code>, which are useful for supporting
 * Java collection objects that contain <code>ValidValue</code> types.
 *
 * </p>
 *
 *
 * @author Peter A. Pilgrim, Fri Dec 27 22:33:27 GMT 2002
 * @version $Id: ValidValue.java 3 2006-03-01 11:17:08Z gpolancic $
 *
 * @see ISOValidValue
 */
public class ValidValue implements Cacheable {

    protected String value = "";

    protected String description = "";

    /**
     * Default constructor for creating a valid value.
     * Please note: no canonization takes place within this method.
     */
    public ValidValue() {
    }

    /**
     * Original constructor for creating a valid value.
     * Please note: no canonization takes place within this method.
     *
     * @param   newValue the real value of the enumeration
     * @param   newDescrip the description of the enumeration
     */
    public ValidValue(String newValue, String newDescrip) {
        setValue(newValue);
        setDescription(newDescrip);
    }

    /**
     * Gets the real value of the valid value
     *
     * @return the value string
     *
     * @see #setValue
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the real value of the valid value
     *
     * @param value the new value string
     *
     * @see #getValue
     */
    public void setValue(String newValue) {
        this.value = StringUtil.notNull(newValue);
    }

    /**
     * Gets the real value of the valid value as a cache key
     *
     * @return the value string as a cache key
     *
     * @see #getValue
     */
    public String getKey() {
        return getValue();
    }

    /**
     * Hashcode since we store by key
     * @return integer
     */
    public int hashCode() {
        return this.value.hashCode();
    }

    /**
     * Gets the description of the valid value
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the real description of the valid value
     *
     * @param description the new description string
     *
     * @see #getDescription
     */
    public void setDescription(String newDescription) {
        this.description = StringUtil.notNull(newDescription);
    }

    /**
     * Human readable string for debugging
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("ValidValue@" + Integer.toHexString(this.hashCode()) + "{");
        buf.append("value:`" + value + "' (`" + description + "')");
        buf.append("}");
        return buf.toString();
    }

    /**
     * A simple comparator for comparing the <code>value</code>
     * attribute of <code>ValidValue</code> records.
     */
    public static class ValueComparator implements java.util.Comparator {

        boolean reverse;

        /**
         * Creates an ascending comparator
         */
        public ValueComparator() {
            this(false);
        }

        /**
         * Creates comparator with specified direction
         */
        public ValueComparator(boolean reverse) {
            this.reverse = reverse;
        }

        /**
         * Comparison method
         * Compares its two arguments for order. Returns a negative
         * integer, zero, or a positive integer as the first argument
         * is less than, equal to, or greater than the second.
         *
         * The implementor must ensure that sgn(compare(x, y)) ==
         * -sgn(compare(y, x)) for all x and y. (This implies that
         * compare(x, y) must throw an exception if and only if
         * compare(y, x) throws an exception.)
         *
         * The implementor must also ensure that the relation is
         * transitive: ((compare(x, y)>0) && (compare(y, z)>0))
         * implies compare(x, z)>0.
         *
         * Finally, the implementer must ensure that compare(x, y)==0
         * implies that sgn(compare(x, z))==sgn(compare(y, z)) for all
         * z.
         *
         * It is generally the case, but not strictly required that
         * (compare(x, y)==0) == (x.equals(y)). Generally speaking,
         * any comparator that violates this condition should clearly
         * indicate this fact. The recommended language is "Note: this
         * comparator imposes orderings that inconsistent with equals."
         *
         * @param o1 the first <code>ValidValue</code> object
         * @param o2 the second <code>ValidValue</code> object
         * @return a negative integer, zero, or a positive integer as
         * the first argument is less than, equal to, or greater than
         * the second.
         *
         * @throws ClassCastException if the arguments' types prevent
         * them from being compared by this Comparator.
         */
        public int compare(Object o1, Object o2) {
            ValidValue ref1 = (ValidValue) o1;
            ValidValue ref2 = (ValidValue) o2;
            int result = ref2.getValue().compareTo(ref1.getValue());
            return result * (reverse ? 1 : -1);
        }

        /** Equivalence method */
        public boolean equals(Object o) {
            if (!(o instanceof ValueComparator)) return false;
            ValueComparator ref = (ValueComparator) o;
            return reverse == ref.reverse;
        }

        /**
         * Human readable string
         */
        public String toString() {
            StringBuffer buf = new StringBuffer();
            buf.append("{ ValueComparator ");
            buf.append(reverse ? "ascending" : "descending");
            buf.append(" sort }");
            return buf.toString();
        }
    }

    /**
     * A simple comparator for comparing the <code>description</code>
     * attribute of <code>ValidValue</code> records.
     */
    public static class DescriptionComparator implements java.util.Comparator {

        boolean reverse;

        /**
         * Creates an ascending comparator
         */
        public DescriptionComparator() {
            this(false);
        }

        /**
         * Creates comparator with specified direction
         */
        public DescriptionComparator(boolean reverse) {
            this.reverse = reverse;
        }

        /**
         * Comparison method
         * Compares its two arguments for order. Returns a negative
         * integer, zero, or a positive integer as the first argument
         * is less than, equal to, or greater than the second.
         *
         * The implementor must ensure that sgn(compare(x, y)) ==
         * -sgn(compare(y, x)) for all x and y. (This implies that
         * compare(x, y) must throw an exception if and only if
         * compare(y, x) throws an exception.)
         *
         * The implementor must also ensure that the relation is
         * transitive: ((compare(x, y)>0) && (compare(y, z)>0))
         * implies compare(x, z)>0.
         *
         * Finally, the implementer must ensure that compare(x, y)==0
         * implies that sgn(compare(x, z))==sgn(compare(y, z)) for all
         * z.
         *
         * It is generally the case, but not strictly required that
         * (compare(x, y)==0) == (x.equals(y)). Generally speaking,
         * any comparator that violates this condition should clearly
         * indicate this fact. The recommended language is "Note: this
         * comparator imposes orderings that inconsistent with equals."
         *
         * @param o1 the first <code>ValidValue</code> object
         * @param o2 the second <code>ValidValue</code> object
         * @return a negative integer, zero, or a positive integer as
         * the first argument is less than, equal to, or greater than
         * the second.
         *
         * @throws ClassCastException if the arguments' types prevent
         * them from being compared by this Comparator.
         */
        public int compare(Object o1, Object o2) {
            ValidValue ref1 = (ValidValue) o1;
            ValidValue ref2 = (ValidValue) o2;
            int result = ref2.getDescription().compareTo(ref1.getDescription());
            return result * (reverse ? 1 : -1);
        }

        /** Equivalence method */
        public boolean equals(Object o) {
            if (!(o instanceof DescriptionComparator)) return false;
            DescriptionComparator ref = (DescriptionComparator) o;
            return reverse == ref.reverse;
        }

        /**
         * Human readable string
         */
        public String toString() {
            StringBuffer buf = new StringBuffer();
            buf.append("{ DescriptionComparator ");
            buf.append(reverse ? "ascending" : "descending");
            buf.append(" sort }");
            return buf.toString();
        }
    }
}
