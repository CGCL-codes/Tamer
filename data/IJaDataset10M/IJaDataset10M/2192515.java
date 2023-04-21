package org.modelibra.modeler.model.action;

/**
 * 
 * @author Dzenan Ridjanovic
 * @version 2001-05-07
 */
public class CommandException extends RuntimeException {

    static final long serialVersionUID = 7168319479760000120L;

    public CommandException(String errorMsg) {
        super(errorMsg);
    }
}
