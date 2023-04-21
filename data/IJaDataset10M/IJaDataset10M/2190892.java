package jsomap.data;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/** A basic storage class for double patterns.  Clients can use this class to build a set of data one
 * pattern at a time from some outside source, such as a file or database.
 *
 * @author Zach Cox <zcox@iastate.edu>
 * @version $Revision: 1.3 $
 * @since JDK1.3
 */
public class BasicDoubleData extends Object implements DoubleData {

    private List _patterns = null;

    /** Creates a new empty <CODE>BasicDoubleData</CODE>.
	 *
	 */
    public BasicDoubleData() {
        _patterns = new ArrayList();
    }

    /** Creates a new <CODE>BasicDoubleData</CODE> using the specified double matrix.
	 *
	 * @param patterns the double matrix.
	 * @throws NullPointerException if <CODE>patterns</CODE> is <CODE>null</CODE>.
	 */
    public BasicDoubleData(double[][] patterns) {
        if (patterns == null) {
            throw new NullPointerException("patterns was null");
        }
        _patterns = new ArrayList();
        int cols = patterns[0].length;
        for (int i = 0, rows = patterns.length; i < rows; i++) {
            double[] pattern = new double[cols];
            for (int j = 0; j < cols; j++) {
                pattern[j] = patterns[i][j];
            }
            add(pattern);
        }
    }

    /** Creates a new <CODE>BasicDoubleData</CODE> using the specified collection of patterns.  The patterns
	 * are added in the ordered they are returned by the collection's iterator.
	 *
	 * @param patterns the collection of patterns.
	 * @throws NullPointerException if <CODE>patterns</CODE> is <CODE>null</CODE>.
	 */
    public BasicDoubleData(Collection patterns) {
        if (patterns == null) {
            throw new NullPointerException("patterns was null");
        }
        for (Iterator iterator = patterns.iterator(); iterator.hasNext(); ) {
            add((DoublePattern) iterator.next());
        }
    }

    /** Adds a copy of the specified pattern to this data object.
	 *
	 * @param pattern the pattern to add.
	 * @throws NullPointerException if <CODE>pattern</CODE> is <CODE>null</CODE>.
	 */
    public void add(double[] pattern) {
        if (pattern == null) {
            throw new NullPointerException("pattern was null");
        }
        _patterns.add(new DoublePattern(pattern));
    }

    /** Adds a copy of the specified pattern to this data object.
	 *
	 * @param pattern the pattern to add.
	 * @throws NullPointerException if <CODE>pattern</CODE> is <CODE>null</CODE>.
	 */
    public void add(DoublePattern pattern) {
        if (pattern == null) {
            throw new NullPointerException("pattern was null");
        }
        _patterns.add(new DoublePattern(pattern));
    }

    /** Returns the number of patterns being stored in this data object.
	 *
	 * @return the number of patterns in this data object.
	 */
    public int count() {
        return _patterns.size();
    }

    /** Returns the pattern at the specified position.
	 *
	 * @param index the index of the desired pattern.
	 * @return the pattern indexed by <CODE>index</CODE>.
	 * @throws IndexOutOfBoundsException if <CODE>index</CODE> is out of range.
	 */
    public Pattern get(int index) {
        if (!isValidIndex(index)) {
            throw new IndexOutOfBoundsException("index out of bounds: " + index);
        }
        return (Pattern) _patterns.get(index);
    }

    /** Returns the double pattern at the specified position.
	 *
	 * @param index the index of the pattern.
	 * @return the pattern at the specified position.
	 * @throws IndexOutOfBoundsException if <CODE>index</CODE> is out of range.
	 */
    public DoublePattern getDouble(int index) {
        return (DoublePattern) get(index);
    }

    /** Returns <CODE>true</CODE> if the specified index is valid.
	 *
	 * @param index the index to test for validity.
	 * @return <CODE>true</CODE> if <CODE>index >= 0</CODE> and <CODE>index < count()</CODE>.
	 */
    public boolean isValidIndex(int index) {
        return index >= 0 && index < count();
    }

    /** Sets the pattern at the specified index to the specified pattern (optional
	 * operation).
	 *
	 * @param index the index of the pattern.
	 * @param point the new pattern.
	 * @throws NullPointerException if <CODE>pattern</CODE> is null.
	 * @throws IndexOutOfBoundsException if <CODE>index</CODE> is out of range.
	 * @throws UnsupportedOperationException if this <CODE>set</CODE> method is not supported by this data object.
	 * @throws IllegalArgumentException if the specified pattern cannot be added to this data object.
	 */
    public void set(int index, Pattern pattern) {
        if (!(pattern instanceof DoublePattern)) {
            throw new IllegalArgumentException("pattern was not a DoublePattern");
        }
        setDouble(index, (DoublePattern) pattern);
    }

    /** Sets the pattern at the specified position to the specified double pattern (optional operation).
	 *
	 * @param index the index of the pattern.
	 * @param pattern the new double pattern.
	 * @throws NullPointerException if <CODE>pattern</CODE> is <CODE>null</CODE>.
	 * @throws IndexOutOfBoundsException if <CODE>index</CODE> is out of range.
	 * @throws UnsupportedOperationException if this <CODE>set</CODE> method is not supported by this data object.
	 * @throws IllegalArgumentException if <CODE>pattern</CODE> is a different length than the other double patterns in this data object.
	 */
    public void setDouble(int index, DoublePattern pattern) {
        if (pattern == null) {
            throw new NullPointerException("pattern was null");
        }
        if (!isValidIndex(index)) {
            throw new IndexOutOfBoundsException("index out of range: " + index);
        }
        if (pattern.get().length != ((DoublePattern) _patterns.get(0)).get().length) {
            throw new IllegalArgumentException("pattern must have same length as other patterns");
        }
        _patterns.set(index, pattern);
    }
}
