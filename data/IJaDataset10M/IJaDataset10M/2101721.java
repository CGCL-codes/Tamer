package ghm.follow.io;

/**
 * Interface used by a {@link FileFollower} to print the contents of a followed
 * file.
 * 
 * @see FileFollower
 * @author <a href="mailto:greghmerrill@yahoo.com">Greg Merrill</a>
 */
public interface OutputDestination {

    /**
	 * Print the supplied String.
	 * 
	 * @param s
	 *            String to be printed
	 */
    public void print(String s);

    /**
	 * Clear all previous text.
	 */
    public void clear();
}
