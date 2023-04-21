package cx.ath.contribs.internal.xerces.impl.dv.dtd;

import cx.ath.contribs.internal.xerces.impl.dv.DatatypeValidator;
import cx.ath.contribs.internal.xerces.impl.dv.InvalidDatatypeValueException;
import cx.ath.contribs.internal.xerces.impl.dv.ValidationContext;

/**
 * NOTATIONValidator defines the interface that data type validators must obey.
 * These validators can be supplied by the application writer and may be useful as
 * standalone code as well as plugins to the validator architecture.
 *
 * @xerces.internal  
 * 
 * @author Jeffrey Rodriguez, IBM
 * @author Sandy Gao, IBM
 * 
 * @version $Id: NOTATIONDatatypeValidator.java,v 1.2 2007/07/13 07:23:28 paul Exp $
 */
public class NOTATIONDatatypeValidator implements DatatypeValidator {

    public NOTATIONDatatypeValidator() {
    }

    /**
     * Checks that "content" string is valid NOTATION value.
     * If invalid a Datatype validation exception is thrown.
     * 
     * @param content       the string value that needs to be validated
     * @param context       the validation context
     * @throws InvalidDatatypeException if the content is
     *         invalid according to the rules for the validators
     * @see InvalidDatatypeValueException
     */
    public void validate(String content, ValidationContext context) throws InvalidDatatypeValueException {
    }
}
