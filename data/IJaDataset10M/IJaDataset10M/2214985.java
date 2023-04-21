package org.dasein.attributes.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import org.dasein.attributes.DataType;
import org.dasein.attributes.DataTypeFactory;
import org.dasein.attributes.InvalidAttributeException;
import org.dasein.util.Translator;

/**
 * <p>
 *   Represents a true/false value.
 * </p>
 * <p>
 *   Last modified: $Date: 2009/01/30 23:01:49 $
 * </p>
 * @version $Revision: 1.4 $
 * @author George Reese
 */
public class BooleanFactory extends DataTypeFactory<Boolean> {

    /**
     * <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 4049636802178726201L;

    /**
     * The name of this type is 'boolean'.
     */
    public static final String TYPE_NAME = "boolean";

    /**
     * Constructs a new factory instance for boolean attributes.
     */
    public BooleanFactory() {
        super();
    }

    /**
     * @return a display name for this data type
     */
    public Translator<String> getDisplayName() {
        return new Translator<String>(Locale.US, TYPE_NAME);
    }

    /**
     * @return the type name, 'boolean'
     */
    public String getTypeName() {
        return TYPE_NAME;
    }

    /**
     * Technically, you can have a multi-lingual or multi-valued boolean, but why would you?
     * @param ml true if the boolean is multi-lingual
     * @param mv true if the boolean can support multiple values
     * @param req true if the boolean is required
     * @param params unused
     * @return a boolean instance
     */
    public DataType<Boolean> getType(boolean ml, boolean mv, boolean req, String... params) {
        return getType(null, null, ml, mv, req, params);
    }

    /**
     * Technically, you can have a multi-lingual or multi-valued boolean, but why would you?
     * @param ml true if the boolean is multi-lingual
     * @param mv true if the boolean can support multiple values
     * @param req true if the boolean is required
     * @param params unused
     * @return a boolean instance
     */
    public DataType<Boolean> getType(String grp, Number idx, boolean ml, boolean mv, boolean req, String... params) {
        return new BooleanAttribute(grp, idx, ml, mv, req);
    }

    /**
     * <p>
     *   Implements the rules around a boolean value.
     * </p>
     * <p>
     *   Last modified: $Date: 2009/01/30 23:01:49 $
     * </p>
     * @version $Revision: 1.4 $
     * @author George Reese
     */
    public static class BooleanAttribute extends DataType<Boolean> {

        /**
         * <code>serialVersionUID</code>
         */
        private static final long serialVersionUID = 3257848766349193780L;

        /**
         * A collection with the values <code>true</code> and <code>false</code>.
         */
        private static final Collection<Boolean> choices = new ArrayList<Boolean>();

        static {
            choices.add(true);
            choices.add(false);
        }

        /**
         * Constructs a boolean attribute instance.
         * @param grp the group of the data type.
         * @param idx the index of the data type.
         * @param ml true if the boolean is multi-lingual
         * @param mv true if the boolean supports multiple values
         * @param req true if a value is required
         */
        public BooleanAttribute(String grp, Number idx, boolean ml, boolean mv, boolean req) {
            super(TYPE_NAME, grp, idx, ml, mv, req, (String[]) null);
        }

        /**
         * @return <code>true</code> and <code>false</code>
         */
        public Collection<Boolean> getChoices() {
            return choices;
        }

        /**
         * @return the factory that constructed this boolean attribute
         */
        @SuppressWarnings("unchecked")
        public DataTypeFactory<Boolean> getFactory() {
            return (DataTypeFactory<Boolean>) DataTypeFactory.getInstance(TYPE_NAME);
        }

        /**
         * @return a {@link org.dasein.attributes.DataType.InputType#SELECT} instance
         */
        public InputType getInputType() {
            return (InputType) InputType.SELECT;
        }

        /**
         * Given a raw value from some source, this method will provide a Java
         * {@link java.lang.Boolean} instance. This raw value can be a string
         * ('true' or 'false'), a boolean, or a number (non-zero is true, zero is false).
         * @param val the raw value
         * @return the corresponding boolean value
         */
        public Boolean getValue(Object val) {
            if (val == null) {
                return null;
            } else if (val instanceof Boolean) {
                return (Boolean) val;
            } else if (val instanceof String) {
                String str = (String) val;
                return new Boolean(str.equalsIgnoreCase("true"));
            } else if (val instanceof Number) {
                Number num = (Number) val;
                return new Boolean(num.intValue() != 0);
            } else {
                throw new InvalidAttributeException("Not boolean: " + val);
            }
        }

        /**
         * Verifies that the specified value is valid according to the rules of this 
         * data type. In practice, the only check it performs is whether or not a
         * value is required when the parameter is null.
         * @param b the value being validated
         * @return true if the specified value is valid for this type's rules
         */
        public boolean isValidChoice(Boolean b) {
            return b != null || !isRequired();
        }

        public String toString() {
            return "Boolean (true/false)";
        }
    }
}
