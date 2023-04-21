package rpi.goldsd.container;

/**
 * The <tt>Sequence</tt> interface defines two <tt>sequence()</tt> methods
 * to be used to generate enumerations of sequential values.  For example,
 * an <tt>IntSequence</tt> class may implement this interface and provide
 * a sequence of integers suitable for establishing a unique set of keys
 * for a hashtable or other associative container of this package.
 * As another example, consider an <tt>AlphabetSequence</tt> class that
 * implements this interface that provides a sequence of the letters of the
 * alphabet.
 *
 * @see IntSequence
 * @see RandomIntSequence
 * @version 1.1, 4/16/98
 * @author David Goldschmidt
 */
public interface Sequence {

    /**
   * Provides an enumeration of the elements making up a sequence
   * of values starting at a default start value.
   * @return an enumeration of the elements making up a sequence.
   */
    public java.util.Enumeration sequence();

    /**
   * Provides an enumeration of the elements making up a sequence
   * of values starting at the given start value.
   * @param startValue the starting value of the sequence.
   * @return an enumeration of the elements making up a sequence.
   */
    public java.util.Enumeration sequence(Object startValue);
}
