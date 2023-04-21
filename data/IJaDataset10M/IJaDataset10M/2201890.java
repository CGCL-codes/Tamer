package de.tud.fujaba.futemplerator;

public class ParamInfoException extends Exception {

    static final long serialVersionUID = 3890457203957L;

    public ParamInfoException() {
    }

    public ParamInfoException(String message) {
        super(message);
    }

    public ParamInfoException(Throwable cause) {
        super(cause);
    }

    public ParamInfoException(String message, Throwable cause) {
        super(message, cause);
    }
}
