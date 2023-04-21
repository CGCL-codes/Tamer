package org.deri.iris.api.terms;

import java.io.Serializable;

/**
 * <p>
 * An interface which defines a term. A term is a name for an object
 * in the universe of discourse. There are three types of terms:
 * <ul>
 * <li> variables</li>
 * <li> constants</li>
 * <li> constructed terms (functional symbols)</li>
 * </ul>
 * </p>
 * <p>
 * By convention <code>null</code> is the smalles possible term of all types.
 * So if you compare a term using the compare method you will always recieve
 * a positive number.
 * </p>
 * <p>
 * $Id: ITerm.java,v 1.15 2007-10-15 15:20:38 bazbishop237 Exp $
 * </p>
 * @author Darko Anicic, DERI Innsbruck
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 */
public interface ITerm extends Comparable<ITerm>, Serializable {

    /**
	 * Checks whether the term is ground (a term with no variables).
	 * 
	 * @return	<code>true</code> if the term is ground, 
	 * 			otherwise <code>false</code>.
	 */
    public boolean isGround();

    /**
	 * Returns a vale of the term.
	 * 
	 * @return	The term value.
	 */
    public Object getValue();
}
