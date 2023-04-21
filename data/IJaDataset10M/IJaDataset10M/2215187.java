package uk.co.ordnancesurvey.rabbitparser.parsedsentencepart;

import uk.co.ordnancesurvey.rabbitparser.exception.RabbitException;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.validation.ParsedEntityInOntologyVR;

/**
 * Base class for all validated parsed entities.
 * 
 * @author rdenaux
 * 
 */
public abstract class BaseValidatedParsedEntity extends BaseParsedPart implements IParsedEntity {

    ParsedEntityInOntologyVR entityInOntologyVR;

    /**
	 * Validates this parsedPart
	 */
    public final void validateEntity() {
        clearErrors();
        entityInOntologyVR = new ParsedEntityInOntologyVR();
        entityInOntologyVR.setEntityToValidate(this);
        entityInOntologyVR.validate();
        doValidateEntity();
    }

    /**
	 * Perform actions linked to this BaseValidatedParsedEntity
	 * 
	 * @throws RabbitException
	 */
    public abstract void performActions() throws RabbitException;

    /**
	 * Override this method to perform extra validations for parsed entities
	 */
    protected abstract void doValidateEntity();
}
