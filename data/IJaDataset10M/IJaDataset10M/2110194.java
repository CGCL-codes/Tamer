package projectatlast.query;

/**
 * Determines an output value based on an input value.
 * 
 * @author Kevin Bourrillion
 * @see <a href="http://code.google.com/p/guava-libraries">guava-libraries on
 *      Google Code</a>
 */
public interface Function<F, T> {

    /**
	 * Returns the result of applying this function to {@code input}.
	 */
    T apply(F input);
}
