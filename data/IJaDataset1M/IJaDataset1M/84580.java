package org.cesar.flip.flipex.ajdt.validators;

import org.eclipse.osgi.util.NLS;

/**
 * The Messages for the before execution validator.
 * 
 * @author Vilmar Nepomuceno (vilmar.nepomuceno@cesar.org.br)
 * 
 */
public class BeforeExecutionValidatorMessages extends NLS {

    private static final String BUNDLE_NAME = "org.cesar.flip.flipex.ajdt.validators.beforeexecutionvalidatormessages";

    public static String BeforeExecutionValidator_CODE_NOT_IN_BEGINNING_OF_METHOD;

    static {
        NLS.initializeMessages(BUNDLE_NAME, BeforeExecutionValidatorMessages.class);
    }

    private BeforeExecutionValidatorMessages() {
    }
}
