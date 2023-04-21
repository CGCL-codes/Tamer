package net.code4j.dolon.service;

/**
 * @author xandro
 *
 */
public class DolonSystemException extends RuntimeException {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8922980349269374321L;

    public DolonSystemException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public DolonSystemException(String arg0) {
        super(arg0);
    }

    public DolonSystemException(Throwable arg0) {
        super(arg0);
    }
}
