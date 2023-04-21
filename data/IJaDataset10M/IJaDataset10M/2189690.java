package org.keel.services.model;

/**
 * A 'Model' is the basic unit of application logic in Keel. Applications consist
 * of a number of models and their associated support objects.
 * A model is a very simple interface - only one method is required, and the
 * implementation of that method is left entirely up to the model object itself. Models
 * work closely in conjunction with ModelRequests, so see the detailed information
 * in the ModelRequest class as well.
 */
public interface Model {

    public String ROLE = "org.keel.services.model.Model";

    public ModelResponse execute(ModelRequest request) throws ModelException;

    public Object getConfiguration() throws ModelException;
}
