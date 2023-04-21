package runes.kernel.exceptions;

/**
 * @author Luca Mottola <a
 *         href="mailto:mottola@elet.polimi.it">mottola@elet.polimi.it</a>
 * 
 */
public class LoaderException extends CapsuleException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public static final byte MALFORMED_PATTERN = 1;

    private final byte kind;

    public LoaderException(byte kind) {
        this.kind = kind;
    }

    public byte getKind() {
        return kind;
    }
}
