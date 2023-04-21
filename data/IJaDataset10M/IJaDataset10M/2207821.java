package CurveEditor.Exceptions;

public class DivisionByZeroException extends Exception {

    private static final long serialVersionUID = 1L;

    public DivisionByZeroException() {
    }

    public DivisionByZeroException(String arg0) {
        super(arg0);
    }

    public DivisionByZeroException(Throwable arg0) {
        super(arg0);
    }

    public DivisionByZeroException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }
}
