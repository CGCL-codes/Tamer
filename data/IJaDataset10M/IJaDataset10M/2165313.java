package tudresden.ocl20.injection;

/**
 * Is thrown for invalid command line parameters
 */
public class IllegalParameterException extends Exception {

    public IllegalParameterException(String msg) {
        super(msg);
    }
}
