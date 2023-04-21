package com.softwoehr.pigiron.bizobj.constraint;

import com.softwoehr.pigiron.access.VSMInt;
import com.softwoehr.pigiron.access.VSMString;

/**
 * Constraint interface for representing constraints on
 * VSMAPI parameters and throwing exceptions on violation.
 * @author jax
 */
public interface VSMParamConstraint {

    /**
     * Exercise a constraint by throwing if constraint is violated.
     * @param vsmString target of constraint
     * @throws VSMConstraintException if constraint is violated
     */
    public void constrain(VSMString vsmString) throws VSMConstraintException;

    /**
     * Exercise a constraint by throwing if constraint is violated.
     * @param vsmInt target of constraint
     * @throws VSMConstraintException if constraint is violated
     */
    public void constrain(VSMInt vsmInt) throws VSMConstraintException;
}
