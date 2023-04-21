package org.nextframework.controller.resource;

/**
 * @author rogelgarcia
 * @since 02/02/2006
 * @version 1.1
 */
public class ResourceGenerationException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String action;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public ResourceGenerationException(String action, Exception exception) {
        super(exception);
        this.action = action;
    }
}
