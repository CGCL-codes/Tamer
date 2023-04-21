package org.torweg.pulse.util.xml.transform;

/**
 * a {@code RuntimeException} thrown during XSL stylesheet compilation,
 * which provides a detailed error message explaining why the compilation
 * failed.
 * 
 * @author Thomas Weber
 * @version $Revision: 1427 $
 */
public final class XSLCompileException extends RuntimeException {

    /**
	 * serialVersionUID.
	 */
    private static final long serialVersionUID = -6859824918728135669L;

    /**
	 * creates a new {@code XSLCompileException} with the given detail
	 * message.
	 * 
	 * @param msg
	 *            the message
	 */
    public XSLCompileException(final String msg) {
        super(msg);
    }

    /**
	 * creates a new {@code XSLCompileException} with a detailed message
	 * and the underlying {@code TransformerException} as the root cause.
	 * 
	 * @param msg
	 *            the detailed error message of the Transformer
	 * @param ex
	 *            the root cause
	 */
    public XSLCompileException(final String msg, final Exception ex) {
        super(System.getProperty("line.separator") + msg, ex);
    }
}
