package com.interworldtransport.clados;

/**
 * This class is designed to be the handler of rotation exceptions.  Mismatches
 * between the operator and the operand can lead to ill-defined rotations.  Mismatches
 * may include reference frames, foot names, and grade problems.  Any of these problems
 * may throw a RotationDefinitionException to signal an inability to perform a
 * rotatation on a monad.
 * @version 0.80, $Date: 2005/09/29 08:36:20 $
 * @author Dr Alfred W Differ
 */
public class RotationDefinitionException extends CladosBinaryException {

    /**
 * Construct this exception.  This exception must have the source monad, the monad to act
 * as the rotation operator, and a message complaining about a mismatch to prevents
 * rotation.
 */
    public RotationDefinitionException(CladosObject pSource, String pMessage, CladosObject pOperator) {
        super(pSource, pMessage, pOperator);
    }
}
