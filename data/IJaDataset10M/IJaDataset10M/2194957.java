package com.ail.core.validator;

import com.ail.core.BaseException;

/**
 * This interface defines the contract between the Core class and the validator
 * sub-system. The Core is expected to expose the methods defined here, and
 * this package will expose the required entry points.<p>
 * @version $Revision: 1.2 $
 * @state $State: Exp $
 * @date $Date: 2005/07/16 10:23:26 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/validator/Validator.java,v $
 **/
public interface Validator {

    /**
	 * Validate a value
	 * @param key Key used to identify validation required
	 * @param value Value to validate
	 * @return Validation results
	 */
    public ValidatorResult validate(String key, Object value) throws BaseException;
}
