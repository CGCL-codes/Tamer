package org.deri.iris.api.terms.concrete;

import org.deri.iris.api.terms.IConcreteTerm;

/**
 * <p>
 * An interface for representing the boolean datatype. 
 * </p>
 * <p>
 * Remark: IRIS supports datatypes according to the standard 
 * specification for primitive XML Schema datatypes.
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date   11.01.2006 15:25:47
 */
public interface IBooleanTerm extends IConcreteTerm {

    /**
	 * Return the wrapped type.
	 */
    public Boolean getValue();
}
