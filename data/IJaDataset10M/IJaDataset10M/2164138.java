package jsomap.data;

/** A collection of double patterns arranged on a hexagonal integer lattice.
 *
 * @author Zach Cox <zcox@iastate.edu>
 * @version $Revision: 1.1 $
 * @since JDK1.3
 */
public class HexDoubleData extends Object implements DoubleData {

    private DoubleData _data = null;

    /** Creates a new <CODE>HexDoubleData</CODE> with the specified number of rows and columns of patterns
	 * in the lattice.
	 * @param rows the number of rows.
	 * @param cols the number of columns.
	 * @throws IllegalArgumentException if <CODE>rows < 1</CODE>
	 * @throws IllegalArgumentException if <CODE>cols < 1</CODE>
	 */
    public HexDoubleData(int rows, int cols) {
        if (rows < 1) {
            throw new IllegalArgumentException("rows must be non-zero and positive: " + rows);
        }
        if (cols < 1) {
            throw new IllegalArgumentException("cols must be non-zero and positive: " + cols);
        }
        double[][] data = new double[rows * cols - (int) Math.floor((double) rows / 2.0d)][2];
        double x = 0.0d, y = 0.0d;
        int row = 0, count = 0;
        for (int i = 0; i < data.length; i++) {
            data[i][0] = x;
            data[i][1] = y;
            if (row % 2 == 0) {
                if (count == cols - 1) {
                    x = 0.5d;
                    y++;
                    count = 0;
                    row++;
                } else {
                    x++;
                    count++;
                }
            } else {
                if (count == cols - 2) {
                    x = 0.0d;
                    y++;
                    count = 0;
                    row++;
                } else {
                    x++;
                    count++;
                }
            }
        }
        _data = new BasicDoubleData(data);
    }

    /** Returns the number of patterns being stored in this data object.
	 *
	 * @return the number of patterns in this data object.
	 */
    public int count() {
        return _data.count();
    }

    /** Returns the pattern at the specified position.
	 *
	 * @param index the index of the desired pattern.
	 * @return the pattern indexed by <CODE>index</CODE>.
	 * @throws IndexOutOfBoundsException if <CODE>index</CODE> is out of range.
	 */
    public Pattern get(int index) {
        return _data.get(index);
    }

    /** Returns the double pattern at the specified position.
	 *
	 * @param index the index of the pattern.
	 * @return the pattern at the specified position.
	 * @throws IndexOutOfBoundsException if <CODE>index</CODE> is out of range.
	 */
    public DoublePattern getDouble(int index) {
        return _data.getDouble(index);
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
        _data.set(index, pattern);
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
        _data.setDouble(index, pattern);
    }
}
