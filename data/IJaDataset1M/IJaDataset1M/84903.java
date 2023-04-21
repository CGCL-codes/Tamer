package org.deri.iris.api.terms.concrete;

import org.deri.iris.api.terms.IConcreteTerm;

/**
 * 
 * gMonth is a gregorian month that recurs every year.
 *  <p>
 * Remark: IRIS supports datatypes according to the standard 
 * specification for primitive XML Schema datatypes.
 * </p>
 * <pre>
 *   Created on 11.04.2006
 *   Committed by $Author: bazbishop237 $
 *   $Source: /tmp/iris-cvsbackup/iris/api/org/deri/iris/api/terms/concrete/IGMonth.java,v $,
 * </pre>
 * 
 * @author Richard Pöttler
 * 
 * @version $Revision: 1.5 $ $Date: 2007-10-09 20:21:21 $
 */
public interface IGMonth extends IConcreteTerm {

    /**
	 * Return the wrapped type.
	 */
    public Integer getValue();

    /**
	 * Returns the month.
	 * 
	 * @return the month
	 */
    public int getMonth();
}
