package net.sf.sail.webapp.dao;

import java.io.Serializable;

/**
 * @author Laurel Williams
 *
 * @version $Id: ObjectNotFoundException.java 941 2007-08-16 14:03:11Z laurel $
 */
public class ObjectNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    private String message;

    @SuppressWarnings("unchecked")
    public ObjectNotFoundException(Serializable id, Class objectClass) {
        this.message = "Object with id " + id.toString() + "of type " + objectClass + " not found.";
    }

    public String getMessage() {
        return this.message;
    }
}
