package util.text;

public abstract class ExceptionMessageBuilder {

    public static String getMessage(Throwable t) {
        StringBuffer s = new StringBuffer(t.getClass().getName() + ": " + t.getMessage());
        Throwable cause = t.getCause();
        while (cause != null) {
            s.append("\n" + cause.getMessage());
            cause = cause.getCause();
        }
        return s.toString();
    }
}
