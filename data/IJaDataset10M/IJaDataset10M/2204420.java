package com.kenai.jbosh;

/**
 * Data type representing the getValue of the {@code wait} attribute of the
 * {@code bosh} element.
 */
final class AttrWait extends AbstractIntegerAttr {

    /**
     * Creates a new attribute object.
     * 
     * @param val attribute getValue
     * @throws BOSHException on parse or validation failure
     */
    private AttrWait(final String val) throws BOSHException {
        super(val);
        checkMinValue(1);
    }

    /**
     * Creates a new attribute instance from the provided String.
     * 
     * @param str string representation of the attribute
     * @return attribute instance or {@code null} if provided string is
     *  {@code null}
     * @throws BOSHException on parse or validation failure
     */
    static AttrWait createFromString(final String str) throws BOSHException {
        if (str == null) {
            return null;
        } else {
            return new AttrWait(str);
        }
    }
}
