package mfinder;

/**
 * mfinder用此异常或其子异常来标识mfinder特定的异常信息。
 */
public class MFinderException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 构造一个包含指定详细消息的MFinderException。
     *
     * @param message 详细消息。
     */
    public MFinderException(String message) {
        super(message);
    }

    /**
     * 构造一个包含指定原因的的MFinderException。
     *
     * @param cause 异常原因。
     */
    public MFinderException(Throwable cause) {
        super(cause);
    }

    /**
     * 构造一个包含指定详细消息和原因的MFinderException。
     *
     * @param message 详细消息。
     * @param cause 异常原因。
     */
    public MFinderException(String message, Throwable cause) {
        super(message, cause);
    }
}
