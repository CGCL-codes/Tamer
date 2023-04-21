package tudresden.ocl.interp.core.intern;

/**
 * The AssertionException is thrown if an assertion fails.
 * 
 * @see Assert for detailed description
 */
class AssertionException extends RuntimeException {

    public AssertionException() {
        super();
    }

    public AssertionException(String message) {
        super(message);
    }
}
