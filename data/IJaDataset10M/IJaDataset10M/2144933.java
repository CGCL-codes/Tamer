package de.folt.util;

import de.folt.constants.OpenTMSConstants;

/**
 * This class implements the main Exception of OpenTMS: The OpenTMSException class.<br>
 * The class contains for main components (variables)<br>
 * String id - an unique identifier for the cause<br>
 * String[] args arguments supplied by the caller<br>
 * int type the type of the Exception<br>
 * Object cause the object causing the exception<br>
 * @author klemens
 *
 */
public class OpenTMSException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -7677349601910781957L;

    protected String[] args = null;

    protected Object cause;

    protected Exception exception;

    protected String id = "";

    protected int type;

    /**
     * 
     */
    public OpenTMSException() {
        super();
    }

    /**
     * @param id
     */
    public OpenTMSException(String id) {
        this(id, (String[]) null, OpenTMSConstants.OpenTMS_EXCEPTION_ERROR, null);
    }

    /**
     * @param id
     * @param type
     */
    public OpenTMSException(String id, int type) {
        this(id, (String[]) null, type, null);
    }

    /**
     * @param id
     * @param cause
     */
    public OpenTMSException(String id, Object cause) {
        this(id, (String[]) null, OpenTMSConstants.OpenTMS_EXCEPTION_ERROR, cause);
    }

    /**
     * @param id
     * @param arg
     */
    public OpenTMSException(String id, String arg) {
        this(id, new String[] { arg }, OpenTMSConstants.OpenTMS_EXCEPTION_ERROR, null);
    }

    /**
     * @param id
     * @param arg
     * @param type
     */
    public OpenTMSException(String id, String arg, int type) {
        this(id, new String[] { arg }, type, null);
    }

    /**
     * @param id
     * @param arg
     * @param type
     * @param cause
     */
    public OpenTMSException(String id, String arg, int type, Object cause) {
        this(id, new String[] { arg }, type, cause);
    }

    /**
     * @param id
     * @param arg
     * @param type
     * @param cause
     * @param ex
     */
    public OpenTMSException(String id, String arg, int type, Object cause, Exception ex) {
        this(id, new String[] { arg }, type, cause, ex);
    }

    /**
     * @param id
     * @param arg
     * @param cause
     */
    public OpenTMSException(String id, String arg, Object cause) {
        this(id, new String[] { arg }, OpenTMSConstants.OpenTMS_EXCEPTION_ERROR, cause);
    }

    public OpenTMSException(String id, String[] args) {
        this(id, args, OpenTMSConstants.OpenTMS_EXCEPTION_ERROR, null);
    }

    /**
     * @param id
     * @param args
     * @param type
     */
    public OpenTMSException(String id, String[] args, int type) {
        this(id, args, type, null);
    }

    /**
     * @param id
     * @param args
     * @param type
     * @param cause
     */
    public OpenTMSException(String id, String[] args, int type, Object cause) {
        super(id);
        this.id = id;
        this.args = args;
        this.type = type;
        this.cause = cause;
    }

    /**
     * @param id
     * @param args
     * @param type
     * @param cause
     * @param ex
     */
    public OpenTMSException(String id, String[] args, int type, Object cause, Exception ex) {
        super(id);
        ex.printStackTrace();
        this.id = id;
        this.args = args;
        this.type = type;
        this.cause = cause;
        this.exception = ex;
    }

    /**
     * @param id
     * @param args
     * @param cause
     */
    public OpenTMSException(String id, String[] args, Object cause) {
        this(id, args, OpenTMSConstants.OpenTMS_EXCEPTION_ERROR, cause);
    }

    /**
     * @param arg0
     * @param arg1
     */
    public OpenTMSException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    /**
     * @param arg0
     */
    public OpenTMSException(Throwable arg0) {
        super(arg0);
    }

    /**
     * getArgs 
     * @return the arguments (Strings) supplied for an exception
     */
    public String[] getArgs() {
        return args;
    }

    /**
     * getEmxCause 
     * @return the cause of an exception
     */
    public Object getEmxCause() {
        return cause;
    }

    /**
     * getErrorCode 
     * @return the error code of an exception
     */
    public int getErrorCode() {
        return Integer.parseInt(id, 0);
    }

    /**
     * @return the exception
     */
    public Exception getException() {
        return exception;
    }

    /**
     * getKey 
     * @return the message associated with the exception
     */
    public String getKey() {
        return getMessage();
    }

    /**
     * getType 
     * @return the type of an exception
     */
    public int getType() {
        return type;
    }
}
