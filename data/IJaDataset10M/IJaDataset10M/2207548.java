package whf.framework.exception;

/**
 * 创建对象失败时抛出异常
 * @author wanghaifeng
 *
 */
public class CreateException extends DAOException {

    private static final long serialVersionUID = 0;

    public CreateException() {
        super();
    }

    public CreateException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreateException(String message) {
        super(message);
    }

    public CreateException(Throwable cause) {
        super(cause);
    }
}
