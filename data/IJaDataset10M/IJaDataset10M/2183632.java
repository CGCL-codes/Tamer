package cn.sduo.app.exception;

public class IllegalRequestException extends RuntimeException {

    public IllegalRequestException(String msg) {
        super(msg);
    }

    public IllegalRequestException(Exception e) {
        super(e);
    }

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
}
