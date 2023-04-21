package ch.arpage.collaboweb.services.validation;

import ch.arpage.collaboweb.model.Resource;
import ch.arpage.collaboweb.model.User;

/**
 * Validator interface used to validate an object
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public interface ResourceValidator {

    /**
	 * Validates the given resource.
	 * @param resource	The resource to be validated
	 * @param user		The current user
	 * @return			<code>true</code> if the resource is valid,
	 * 					<code>false</code> otherwise.
	 */
    boolean isValid(Resource resource, User user);
}
