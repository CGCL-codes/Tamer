package flattree.flat.write;

import flattree.flat.WriteLine;

/**
 * A line wrapping a {@link String}.
 */
public class StringWriteLine implements WriteLine {

    private StringBuffer buffer = new StringBuffer();

    private int index = 0;

    private int columnIndex = 0;

    private int rowIndex = 0;

    public int getRowIndex() {
        return rowIndex;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public int getIndex() {
        return index;
    }

    public void write(char character) {
        buffer.append(character);
        index++;
        columnIndex++;
    }

    public void flush() {
    }

    /**
	 * Get the written string.
	 */
    public String toString() {
        return buffer.toString();
    }
}
