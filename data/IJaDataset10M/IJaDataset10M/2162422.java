package com.google.gwt.user.client.rpc;

/**
 * Superclass for exceptions thrown from RPC methods (those appearing in
 * interfaces derived from {@link RemoteService}).
 */
public class SerializableException extends Exception implements IsSerializable {

    private String msg;

    /**
   * The default constructor. This constructor is used implicitly during
   * serialization or when constructing subclasses.
   */
    public SerializableException() {
    }

    /**
   * Constructs a serializable exception with the specified message. This
   * constructor is most often called by subclass constructors.
   */
    public SerializableException(String msg) {
        this.msg = msg;
    }

    /**
   * Exception chaining is not currently supported for serialized exceptions.
   * 
   * @return always <code>null</code>
   */
    public Throwable getCause() {
        return null;
    }

    public String getMessage() {
        return msg;
    }

    /**
   * No effect; exception chaining is not currently supported for serialized
   * exceptions.
   */
    public Throwable initCause(Throwable cause) {
        return null;
    }
}
