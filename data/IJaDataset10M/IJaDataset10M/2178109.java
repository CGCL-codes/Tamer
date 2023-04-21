package flattree.flat.read;

import flattree.flat.ReadLine;

/**
 * A line which doesn't {@link #drain()} any characters.
 */
public class LazyReadLine implements ReadLine {

    private final ReadLine line;

    public LazyReadLine(ReadLine line) {
        this.line = line;
    }

    public int getIndex() {
        return line.getIndex();
    }

    public int getRowIndex() {
        return line.getRowIndex();
    }

    public int getColumnIndex() {
        return line.getColumnIndex();
    }

    public char read() {
        return line.read();
    }

    public void drain() {
    }
}
