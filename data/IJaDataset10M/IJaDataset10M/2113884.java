package org.deri.iris.api.terms.concrete;

import org.deri.iris.api.terms.INumericTerm;

/**
 * <p>
 * An interface for representing the float datatype.
 * </p>
 * <p>
 * Remark: IRIS supports datatypes according to the standard specification for
 * primitive XML Schema datatypes.
 * </p>
 */
public interface IFloatTerm extends INumericTerm {

    public static final String DATATYPE_URI = "http://www.w3.org/2001/XMLSchema#float";
}
