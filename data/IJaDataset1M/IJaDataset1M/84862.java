package cogitant.base;

/**	Exception thrown when an error is found while handling observers. */
public class ExceptionObserver extends Exception {

    public static final int ALREADYATTACHED = 10;

    public static final int ALREADYDETACHED = 11;

    public ExceptionObserver(int code, String msg) {
        super(code, msg);
    }

    public String toString() {
        String result = super.toString();
        int c = code();
        String tmp = "";
        if (c == ALREADYATTACHED) tmp = "Observer is already attached to an ObservableObject."; else if (c == ALREADYDETACHED) tmp = "Observer is already detached.";
        return result + tmp;
    }
}
