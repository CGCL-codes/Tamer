package cn.chengdu.in.error;

/**
 * @author Declan.Z(declan.zhang@gmail.com)
 * @date 2011-2-21
 */
public class IcdException extends Exception {

    private static final long serialVersionUID = 1L;

    private String extra;

    public IcdException(String message) {
        super(message);
    }

    public IcdException(String message, String extra) {
        super(message);
        this.extra = extra;
    }

    public String getExtra() {
        return extra;
    }
}
