package be.jabapage.snooker.exception;

public class ProcessingException extends Exception {

    private static final long serialVersionUID = -3752981603648347131L;

    public ProcessingException() {
        super();
    }

    public ProcessingException(final String arg0, final Throwable arg1) {
        super(arg0, arg1);
    }

    public ProcessingException(final String arg0) {
        super(arg0);
    }

    public ProcessingException(final Throwable arg0) {
        super(arg0);
    }
}
