package net.sourceforge.jcoupling2.dao.obsolete;

public class PropertyException extends Exception {

    public static String UNKNOWN_PROPERTY = "UNKNOWN property";

    private String cause = null;

    public PropertyException(String cause) {
        this.cause = cause;
    }

    public String getMessage() {
        return this.cause;
    }
}
