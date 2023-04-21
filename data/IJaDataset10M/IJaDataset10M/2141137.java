package org.op4j.operators.qualities;

import org.op4j.operators.intf.generic.ILevel0GenericUniqOperator;

/**
 * <p>
 * This interface contains methods for converting specialized structure operators
 * (list, set, array...) into generic operators (as if an "Op.on" was executed on
 * them instead of "Op.onList", "Op.onArray", etc.)
 * </p>
 * 
 * @since 1.0
 * 
 * @author Daniel Fern&aacute;ndez
 *
 */
public interface GenerizableOperator<I, T> {

    /**
     * <p>
     * Converts the specialized structure operator into generic operators
     * (with no specific methods for structures - e.g. forEach())
     * </p>
     * 
     * @return a generic operator on the same target object
     */
    public ILevel0GenericUniqOperator<I, T> generic();
}
