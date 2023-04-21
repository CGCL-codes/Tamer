package jpl;

import java.util.Map;
import jpl.fli.Prolog;
import jpl.fli.term_t;

/**
 * A jpl.JVoid is a specialised Term.  Instances of this class
 * denote JPL 'jvoid' values in Prolog, i.e. @(void):
 * <pre>
 * JVoid b = new JVoid();
 * </pre>
 * A JVoid can be used (and re-used) in Compound Terms.
 * 
 * <hr><i>
 * Copyright (C) 2004  Paul Singleton<p>
 * Copyright (C) 1998  Fred Dushin<p>
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.<p>
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Library Public License for more details.<p>
 * </i><hr>
 * @author  Fred Dushin <fadushin@syr.edu>
 * @version $Revision: 1.1 $
 * @see jpl.Term
 * @see jpl.Compound
 */
public class JVoid extends Term {

    /**
	 * This constructor creates a JVoid.
	 * 
	 */
    public JVoid() {
    }

    /**
	 * Returns a Prolog source text representation of this JVoid
	 * 
	 * @return  a Prolog source text representation of this JVoid
	 */
    public String toString() {
        return "@(void)";
    }

    /**
	 * Two JVoids are equal
	 * 
	 * @param   obj  The Object to compare (not necessarily another JVoid)
	 * @return  true if the Object satisfies the above condition
	 */
    public final boolean equals(Object obj) {
        return this == obj || (obj instanceof JVoid);
    }

    public final int type() {
        return Prolog.JVOID;
    }

    public String typeName() {
        return "JVoid";
    }

    /**
	 * The (nonexistent) args of this JVoid
	 * 
	 * @return the (nonexistent) args of this JVoid
	 * @deprecated
	 */
    public Term[] args() {
        return new Term[] {};
    }

    /**
	 * Returns a debug-friendly representation of this JVoid
	 * 
	 * @return  a debug-friendly representation of this JVoid
	 * @deprecated
	 */
    public String debugString() {
        return "(JVoid)";
    }

    /**
	 * To convert a JVoid to a term, we unify the (freshly created, hence unbound)
	 * term_t with @(void).
	 * 
	 * @param   varnames_to_vars  A Map from variable names to Prolog variables.
	 * @param   term              A (previously created and unbound) term_t which is to be
	 *                            assigned a Prolog @(void) structure
	 */
    protected final void put(Map varnames_to_vars, term_t term) {
        Prolog.put_jvoid(term);
    }

    /**
	 * Converts a term_t to a JVoid.  Assuming the Prolog term to be
	 * @(void), we just create a new JVoid instance.
	 * NB This conversion is only invoked if "JPL-aware" term import is specified.
	 *
	 * @param   vars_to_Vars  A Map from Prolog variables to JPL Variables.
	 * @param   term          The term_t to convert
	 * @return                A new JVoid instance
	 */
    protected static Term getTerm(Map vars_to_Vars, term_t term) {
        return new jpl.JVoid();
    }

    /**
	 * Nothing needs to be done if the Term denotes an Atom, Integer, Float, JRef, JBoolean or JVoid
	 * 
	 * @param   varnames_to_Terms  A Map from variable names to Terms.
	 * @param   vars_to_Vars       A Map from Prolog variables to JPL Variables.
	 *                 Variables.
	 */
    protected final void getSubst(Map varnames_to_Terms, Map vars_to_Vars) {
    }
}
