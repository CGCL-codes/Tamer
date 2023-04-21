package bigraphspace.api;

/** BigraphFinder error.
 * 
 * @author cmg
 *
 */
public class BigraphNotFoundException extends Exception {

    /**
	 * 
	 */
    public BigraphNotFoundException() {
    }

    /**
	 * @param arg0
	 */
    public BigraphNotFoundException(String arg0) {
        super(arg0);
    }

    /**
	 * @param arg0
	 */
    public BigraphNotFoundException(Throwable arg0) {
        super(arg0);
    }

    /**
	 * @param arg0
	 * @param arg1
	 */
    public BigraphNotFoundException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }
}
