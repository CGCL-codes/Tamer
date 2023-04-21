package openr66.protocol.exception;

/**
 * Protocol exception due to no Data exception
 * @author Frederic Bregier
 *
 */
public class OpenR66ProtocolNoDataException extends OpenR66Exception {

    /**
     *
     */
    private static final long serialVersionUID = 3797740371759917728L;

    /**
     *
     */
    public OpenR66ProtocolNoDataException() {
    }

    /**
     * @param arg0
     * @param arg1
     */
    public OpenR66ProtocolNoDataException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    /**
     * @param arg0
     */
    public OpenR66ProtocolNoDataException(String arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     */
    public OpenR66ProtocolNoDataException(Throwable arg0) {
        super(arg0);
    }
}
