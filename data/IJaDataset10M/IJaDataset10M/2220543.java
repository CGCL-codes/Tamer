package cn.goldgrid.ex;

public class ActionException extends RuntimeException {

    /**
	 * 
	 */
    private static final long serialVersionUID = -779234730122088506L;

    public ActionException(String message) {
        super(message);
    }

    public ActionException(Throwable cause) {
        super(cause);
    }
}
