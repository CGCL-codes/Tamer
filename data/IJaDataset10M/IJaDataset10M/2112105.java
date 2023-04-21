package com.loribel.commons.bo.generated;

import com.loribel.commons.bo.GB_IntegerCaseBO;
import com.loribel.commons.business.GB_BusinessObjectDefault;
import com.loribel.commons.business.abstraction.GB_BOPrototype;
import com.loribel.commons.business.abstraction.GB_SimpleBusinessObject;

/**
 * Generated class for IntegerCase.
 */
public abstract class GB_IntegerCaseBOGen extends GB_BusinessObjectDefault {

    public static final String BO_NAME = "IntegerCase";

    protected GB_IntegerCaseBOGen() {
        super(BO_NAME);
    }

    public static final GB_BOPrototype newBOPrototype() {
        GB_BOPrototype retour = new GB_BOPrototype() {

            public String getName() {
                return GB_IntegerCaseBO.BO_NAME;
            }

            public GB_SimpleBusinessObject newBusinessObject() {
                return new GB_IntegerCaseBO();
            }
        };
        return retour;
    }

    /**
     * Getter for simple.
     */
    public final int getSimple() {
        Integer retour = (Integer) getPropertyValue(BO_PROPERTY.SIMPLE);
        if (retour == null) {
            return 0;
        } else {
            return retour.intValue();
        }
    }

    /**
     * Setter for simple.
     */
    public final void setSimple(int a_simple) {
        Object l_value = new Integer(a_simple);
        setPropertyValue(BO_PROPERTY.SIMPLE, l_value);
    }

    /**
     * Getter for default.
     */
    public final int getDefault() {
        Integer retour = (Integer) getPropertyValue(BO_PROPERTY.DEFAULT);
        if (retour == null) {
            return 0;
        } else {
            return retour.intValue();
        }
    }

    /**
     * Setter for default.
     */
    public final void setDefault(int a_default) {
        Object l_value = new Integer(a_default);
        setPropertyValue(BO_PROPERTY.DEFAULT, l_value);
    }

    /**
     * Getter for positive.
     */
    public final int getPositive() {
        Integer retour = (Integer) getPropertyValue(BO_PROPERTY.POSITIVE);
        if (retour == null) {
            return 0;
        } else {
            return retour.intValue();
        }
    }

    /**
     * Setter for positive.
     */
    public final void setPositive(int a_positive) {
        Object l_value = new Integer(a_positive);
        setPropertyValue(BO_PROPERTY.POSITIVE, l_value);
    }

    /**
     * Getter for withMaxValue.
     */
    public final int getWithMaxValue() {
        Integer retour = (Integer) getPropertyValue(BO_PROPERTY.WITH_MAX_VALUE);
        if (retour == null) {
            return 0;
        } else {
            return retour.intValue();
        }
    }

    /**
     * Setter for withMaxValue.
     */
    public final void setWithMaxValue(int a_withMaxValue) {
        Object l_value = new Integer(a_withMaxValue);
        setPropertyValue(BO_PROPERTY.WITH_MAX_VALUE, l_value);
    }

    /**
     * Property names of this Business Object.
     */
    public static final class BO_PROPERTY {

        public static final String SIMPLE = "simple";

        public static final String DEFAULT = "default";

        public static final String POSITIVE = "positive";

        public static final String WITH_MAX_VALUE = "withMaxValue";
    }
}
