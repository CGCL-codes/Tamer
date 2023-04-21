package net.sf.dpdesktop.service.exception;

/**
 *
 * @author Heiner Reinhardt
 */
public class DataServiceException extends java.lang.Exception {

    private Object resource;

    public DataServiceException(String message) {
        super(message);
    }

    public void setResourceObject(Object resource) {
        this.resource = resource;
    }

    public Object getResourceObject() {
        return this.resource;
    }
}
