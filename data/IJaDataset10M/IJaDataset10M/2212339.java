package r2q2.rendering.xhtml;

/**
 * TODO javadoc
 * 
 * @author Gavin Willingham
 *
 */
public class UnsupportedTagException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public UnsupportedTagException() {
        super();
    }

    public UnsupportedTagException(String _s) {
        super(_s);
    }

    public UnsupportedTagException(Throwable _t) {
        super(_t);
    }

    public UnsupportedTagException(String _s, Throwable _t) {
        super(_s, _t);
    }
}
