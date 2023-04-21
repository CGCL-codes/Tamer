package jhomenet.commons;

/**
 * TODO: Class description.
 * <p>
 * Id: $Id: $
 *
 * @author Dave Irwin (david.irwin@jhu.edu)
 */
public class DataFormatException extends IllegalArgumentException {

    /**
	 * @param desc
	 */
    public DataFormatException(String desc) {
        super(desc);
    }

    /**
	 * @param t
	 */
    public DataFormatException(Throwable t) {
        super(t);
    }

    /**
	 * @param desc
	 * @param t
	 */
    public DataFormatException(String desc, Throwable t) {
        super(desc, t);
    }
}
