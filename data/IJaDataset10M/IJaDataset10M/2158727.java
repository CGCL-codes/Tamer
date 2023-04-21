package deltree.calc;

public class SyntaxError extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SyntaxError(String msg) {
        super(msg);
    }
}
