package de.denkselbst.prologinterface.prolog;

public class PrologException extends Exception {

    private static final long serialVersionUID = -3075168715664178088L;

    public PrologException() {
        super();
    }

    public PrologException(String arg0) {
        super(arg0);
    }

    public PrologException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public PrologException(Throwable arg0) {
        super(arg0);
    }
}
