package com.loribel.tools.filter.bo.generated;

import com.loribel.commons.business.GB_BusinessObjectDefault;
import com.loribel.commons.business.abstraction.GB_BOPrototype;
import com.loribel.commons.business.abstraction.GB_SimpleBusinessObject;
import com.loribel.tools.filter.bo.GB_StringFilterConfigBO;

/**
 * Generated class for StringFilterConfig.
 */
public abstract class GB_StringFilterConfigBOGen extends GB_BusinessObjectDefault {

    public static final String BO_NAME = "StringFilterConfig";

    protected GB_StringFilterConfigBOGen() {
        super(BO_NAME);
    }

    public static final GB_BOPrototype newBOPrototype() {
        GB_BOPrototype retour = new GB_BOPrototype() {

            public String getName() {
                return GB_StringFilterConfigBO.BO_NAME;
            }

            public GB_SimpleBusinessObject newBusinessObject() {
                return new GB_StringFilterConfigBO();
            }
        };
        return retour;
    }

    /**
     * Getter for type.
     */
    public final int getType() {
        Integer retour = (Integer) getPropertyValue(BO_PROPERTY.TYPE);
        if (retour == null) {
            return 0;
        } else {
            return retour.intValue();
        }
    }

    /**
     * Setter for type.
     */
    public final void setType(int a_type) {
        Object l_value = new Integer(a_type);
        setPropertyValue(BO_PROPERTY.TYPE, l_value);
    }

    /**
     * Getter for expression.
     */
    public final String getValue() {
        String retour = (String) getPropertyValue(BO_PROPERTY.EXPRESSION);
        return retour;
    }

    /**
     * Setter for expression.
     */
    public final void setValue(String a_expression) {
        setPropertyValue(BO_PROPERTY.EXPRESSION, a_expression);
    }

    /**
     * Getter for ignoreCase.
     */
    public final boolean isIgnoreCase() {
        Boolean retour = (Boolean) getPropertyValue(BO_PROPERTY.IGNORE_CASE);
        if (retour == null) {
            return false;
        } else {
            return retour.booleanValue();
        }
    }

    /**
     * Setter for ignoreCase.
     */
    public final void setIgnoreCase(boolean a_ignoreCase) {
        Object l_value = Boolean.valueOf(a_ignoreCase);
        setPropertyValue(BO_PROPERTY.IGNORE_CASE, l_value);
    }

    /**
     * Property names of this Business Object.
     */
    public static final class BO_PROPERTY {

        public static final String TYPE = "type";

        public static final String EXPRESSION = "expression";

        public static final String IGNORE_CASE = "ignoreCase";
    }

    /**
     * Values for property type.
     */
    public static final class TYPE {

        public static final int CONTAINS = 1;

        public static final int EQUALS = 2;

        public static final int STARTS = 3;

        public static final int ENDS = 4;

        public static final int REGEX = 5;

        public static final int EMPTY = 6;
    }
}
