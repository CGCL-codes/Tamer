package com.loribel.commons.swing.text;

/**
 * An implementation of the document interface to integer content (Integer, Long,
 * Short).
 *
 * @author Gregory Borelli
 */
public class GB_IntegerDocument extends GB_NumberDocument {

    /**
     * Default constructor.
     * By default accept negatives values, and group separator character but
     * not decimal separator.
     */
    public GB_IntegerDocument() {
        this(Integer.class, true, true);
    }

    /**
     * Constructor of GB_IntegerDocument with parameter(s).
     *
     * @param a_typeOfNumber Class - ype of number (Integer.class, Double.class, ...)
     */
    public GB_IntegerDocument(Class a_typeOfNumber) {
        this(a_typeOfNumber, true, true);
    }

    /**
     * Constructor of GB_IntegerDocument with parameter(s).
     *
     * @param a_typeOfNumber Class - ype of number (Integer.class, Double.class, ...)
     * @param a_flagAcceptNegative boolean - true if this document accept negative values
     * @param a_flagAcceptGroupSeparator boolean - true if this document accept group separator
     */
    public GB_IntegerDocument(Class a_typeOfNumber, boolean a_flagAcceptNegative, boolean a_flagAcceptGroupSeparator) {
        super(a_typeOfNumber, false, a_flagAcceptNegative, a_flagAcceptGroupSeparator);
    }
}
