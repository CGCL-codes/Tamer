package cn.vlabs.duckling.vwb.services.convert;

/**
 * Introduction Here.
 * @date Apr 13, 2010
 * @author xiejj@cnic.cn
 */
public class NoSavedPageNameException extends Exception {

    private static final long serialVersionUID = 1L;

    public NoSavedPageNameException() {
        super("convert data has not been found.");
    }
}
