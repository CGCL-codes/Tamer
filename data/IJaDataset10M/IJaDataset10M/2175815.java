package de.offis.semanticmm4u.failures.generators;

import de.offis.semanticmm4u.failures.MM4UGeneratorException;

/** This exception is thrown, if an unknown operator is found in the abstract document. */
public class MM4URootLayoutNotFoundException extends MM4UGeneratorException {

    protected MM4URootLayoutNotFoundException() {
    }

    public MM4URootLayoutNotFoundException(Object incorrectObject, String incorrectMethod, String comment) {
        super(incorrectObject, incorrectMethod, comment);
    }
}
