package org.isurf.spmiddleware.validation;

import org.isurf.spmiddleware.SPClient;
import org.isurf.spmiddleware.model.ale.ECSpec;

/**
 * Validates {@link SPClient}.define method.
 */
public interface DefineValidator {

    /**
	 * Validates that an {@link ECSpec} can be defined.
	 *
	 * @param eventCycleName The name.
	 * @param ecSpec The ecSpec.
	 * @throws DuplicateNameException If another {@link ECSpec} exists with this name.
	 * @throws ECSpecValidationException If the {@link ECSpec} is invalid.
	 */
    public void validate(String eventCycleName, ECSpec ecSpec) throws DuplicateNameException, ECSpecValidationException;
}
