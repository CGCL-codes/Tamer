package com.reserveamerica.jirarmi.exceptions;

/**
 * @author BStasyszyn
 */
public class ComponentNotFoundException extends EntityNotFoundException {

    private static final long serialVersionUID = 8374017687372287746L;

    public ComponentNotFoundException(String componentName) {
        super("Component [" + componentName + "] not found.");
    }

    public ComponentNotFoundException(Long componentId) {
        super("Component [" + componentId + "] not found.");
    }
}
