package hw3.exception;

public class StdErrReporterException extends Exception {

    private static final long serialVersionUID = 1L;

    public StdErrReporterException(String origClassName, String errorMessage) {
        reportError(origClassName, errorMessage);
    }

    public static void reportError(String origClassName, String errorMessage) {
        System.err.println(origClassName + ": " + errorMessage);
    }

    public static void reportErrorFromException(String origClassName, String errorMessage, Exception e) {
        System.err.println(origClassName + ": " + errorMessage + " Reason: " + e.getMessage());
    }

    public static final String INIT_FAILED_NO_OP_ALLOWED = "Initalization failed, no operation allowed";

    public static final String TOKEN_STRINGS_ERROR = "Error in tokens, token replacement will not be made";

    public static final String UNABLE_TO_LOAD_PROP_FILE = "Unable to load properties file";

    public static final String INIT_FAILED = "Initalization failed.";

    public static final String HTTP_ERROR = "HTTP related error occured.";

    public static final String NETWORKING_ERROR = "Networking error occured.";

    public static final String BAD_ARGUMENTS = "Bad arguments received.";
}
