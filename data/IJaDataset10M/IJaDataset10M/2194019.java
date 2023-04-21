package org.cesar.flip.flipex.validators;

import org.cesar.flip.flipex.RefactoringInfo;

/**
 * Defines the methods that will be implemented by validators in the FLIPex
 * platform.
 * 
 * @author Davi Pires (davi.pires@cesar.org.br)
 * 
 */
public interface IValidator {

    /**
	 * Validates the requested extraction at the origin side, returning a status
	 * object that tells whether the validation was successful or not.
	 * 
	 * @param refactoringInfo
	 *            the description of the requested extraction.
	 * @return a <code>ValidatorStatus</code> object describing the problems
	 *         encountered.
	 * 
	 * @see RefactoringInfo
	 * @see ValidatorStatus
	 */
    public ValidatorStatus validateOrigin(RefactoringInfo refactoringInfo);

    /**
	 * Validates the requested extraction at the destination side, returning a
	 * status object that tells whether the validation was successful or not.
	 * 
	 * @param refactoringInfo
	 *            the description of the requested extraction.
	 * @return a <code>ValidatorStatus</code> object describing the problems
	 *         encountered.
	 * 
	 * @see RefactoringInfo
	 * @see ValidatorStatus
	 */
    public ValidatorStatus validateDestination(RefactoringInfo refactoringInfo);
}
