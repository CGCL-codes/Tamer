package edu.mit.wi.omnigene.service.security.rbac.profile;

/**
 *
 * @author  rajesh
 * @version 
 */
public class NullProfileFieldTypeException extends Exception {

    String message = "";

    /** Creates new NullProfileFieldTypeException */
    public NullProfileFieldTypeException() {
    }

    public NullProfileFieldTypeException(String m) {
        message = m;
    }

    public String toString() {
        return "edu.mit.wi.omnigene.service.security.rbac.profile.NullProfileFieldTypeException: " + message;
    }
}
