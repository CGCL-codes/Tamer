package de.iritgo.aktera.model;

import de.iritgo.aktera.model.Command;
import de.iritgo.aktera.model.Model;
import de.iritgo.aktera.model.ModelException;
import de.iritgo.aktera.model.ModelRequest;
import org.apache.avalon.framework.configuration.ConfigurationException;

/**
 * A component that validates a ModelRequest before the specified
 * Model actually executes.
 *
 * @author Michael Nash
 */
public interface ModelValidator {

    /**
	 * Role string used to look up this component
	 */
    public static final String ROLE = "de.iritgo.aktera.model.ModelValidator";

    /**
	 * Validate the request parameters about to be passed to the specified
	 * model. This method is called *before* the model is executed.
	 * @param req The ModelRequest with the parameters to be validated
	 * @param theModel The model about to be executed
	 * @return Null if all validations are acceptable, or a Command
	 * indicating a re-direct to some other Model. This method can
	 * also return null if some validations fail, but if a redirect is
	 * not requested the errors are simply accumulated with the ModelRequest.
	 * @throws ConfigurationException In the event the configuration for this validator is incorrect
	 * @throws ModelException If there is something seriously wrong with the ModelRequest or the Model itself
	 */
    public Command validate(ModelRequest req, Model theModel) throws ConfigurationException, ModelException;
}
