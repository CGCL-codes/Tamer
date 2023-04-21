package net.sourceforge.cruisecontrol;

public class CruiseControlException extends Exception {

    public CruiseControlException() {
        super();
    }

    public CruiseControlException(String message) {
        super(message);
    }

    public CruiseControlException(Throwable cause) {
        super(cause);
    }

    public CruiseControlException(String message, Throwable cause) {
        super(message, cause);
    }

    public static void throwIf(boolean truthTest, String message) throws CruiseControlException {
        if (truthTest) {
            throw new CruiseControlException(message);
        }
    }

    public void throwIf(boolean truthTest) throws CruiseControlException {
        if (truthTest) {
            throw this;
        }
    }
}
