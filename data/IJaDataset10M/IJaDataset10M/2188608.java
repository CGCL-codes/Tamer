package gg.arkehion.network.exception;

/**
 * Protocol exception on Business part telling that remote host did not find the file.
 *
 * @author frederic bregier
 */
public class ArkProtocolBusinessRemoteFileNotFoundException extends ArkProtocolBusinessException {

    /**
     *
     */
    private static final long serialVersionUID = -1515420982161281552L;

    /**
	 *
	 */
    public ArkProtocolBusinessRemoteFileNotFoundException() {
        super();
    }

    /**
     * @param arg0
     * @param arg1
     */
    public ArkProtocolBusinessRemoteFileNotFoundException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    /**
     * @param arg0
     */
    public ArkProtocolBusinessRemoteFileNotFoundException(String arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     */
    public ArkProtocolBusinessRemoteFileNotFoundException(Throwable arg0) {
        super(arg0);
    }
}
