package net.sf.mxlosgi.mxlosgimainbundle;

import net.sf.mxlosgi.mxlosgixmppbundle.XMPPError;

/**
 * @author noah
 *
 */
public class TLSRequiredExcetpion extends XMPPException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5660907305466125734L;

    /**
	 * 
	 */
    public TLSRequiredExcetpion() {
        super();
    }

    /**
	 * @param message
	 * @param cause
	 */
    public TLSRequiredExcetpion(String message, Throwable cause) {
        super(message, cause);
    }

    /**
	 * @param message
	 */
    public TLSRequiredExcetpion(String message) {
        super(message);
    }

    /**
	 * @param cause
	 */
    public TLSRequiredExcetpion(Throwable cause) {
        super(cause);
    }

    /**
	 * @param error
	 */
    public TLSRequiredExcetpion(XMPPError error) {
        super(error);
    }
}
