package edu.ucla.sspace.matrix;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOError;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An iterator for sequentially accessing the data of a {@link
 * MatrixIO.Format.SVDLIBC_DENSE_BINARY} formatted file.
 *
 * @author Keith Stevens
 */
class SvdlibcDenseTextFileIterator implements Iterator<MatrixEntry> {

    /**
     * The {@link DataInputStream} holding the matrix data.
     */
    private final BufferedReader reader;

    /**
     * The next {@link MatrixEntry} to be returned.
     */
    private MatrixEntry next;

    /**
     * The number of rows in the matrix.
     */
    private int rows;

    /**
     * The number of columns in the matrix.
     */
    private int cols;

    /**
     * The column number of the next value to return
     */
    private int curCol;

    /**
     * The row number of the next value to return
     */
    private int curRow;

    /**
     * The current line to be processed.
     */
    private String[] curLine;

    /**
     * Creates a new {@link SvdlibcDenseTextFileIterator} for {@code
     * matrixFile}.
     */
    public SvdlibcDenseTextFileIterator(File matrixFile) throws IOException {
        reader = new BufferedReader(new FileReader(matrixFile));
        String[] numRowCol = reader.readLine().split("\\s");
        rows = Integer.parseInt(numRowCol[0]);
        cols = Integer.parseInt(numRowCol[1]);
        curLine = reader.readLine().split("\\s+");
        curCol = 0;
        curRow = 0;
        next = advance();
    }

    private MatrixEntry advance() throws IOException {
        if (curRow >= rows) return null;
        if (curCol >= cols) {
            curCol = 0;
            curRow++;
            if (curRow >= rows) {
                reader.close();
                return null;
            }
            curLine = reader.readLine().split("\\s+");
            ;
        }
        return new SimpleEntry(curRow, curCol, Double.parseDouble(curLine[curCol++]));
    }

    public boolean hasNext() {
        return next != null;
    }

    public MatrixEntry next() {
        if (next == null) throw new NoSuchElementException("No futher entries");
        MatrixEntry me = next;
        try {
            next = advance();
        } catch (IOException ioe) {
            throw new IOError(ioe);
        }
        return me;
    }

    /**
     * Throws an {@link UnsupportedOperationException} if called.
     */
    public void remove() {
        throw new UnsupportedOperationException("Cannot remove from file");
    }
}
