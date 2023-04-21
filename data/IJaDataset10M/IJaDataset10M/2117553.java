package ge.exceptions;

public class FunctionNonSuported extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = -7161559033604704833L;

    public FunctionNonSuported(String engine, String function) {
        super(engine + " doesn't support " + function);
    }
}
