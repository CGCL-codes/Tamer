package emil.poker;

public class HandNotCreatableException extends RuntimeException {

    public HandNotCreatableException() {
        super();
    }

    public HandNotCreatableException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public HandNotCreatableException(String arg0) {
        super(arg0);
    }

    public HandNotCreatableException(Throwable arg0) {
        super(arg0);
    }
}
