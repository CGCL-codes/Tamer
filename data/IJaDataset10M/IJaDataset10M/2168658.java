package at.langegger.xlwrap.map.range;

import at.langegger.xlwrap.common.XLWrapException;
import at.langegger.xlwrap.exec.ExecutionContext;
import at.langegger.xlwrap.spreadsheet.Cell;

/**
 * @author dorgon
 *
 */
public class NullRange extends Range {

    public static final NullRange INSTANCE = new NullRange();

    /**
	 * private constructor (singleton)
	 */
    private NullRange() {
    }

    @Override
    public Range shiftCols(int n, Range restrict, ExecutionContext context) throws IndexOutOfBoundsException {
        return this;
    }

    @Override
    public Range shiftRows(int n, Range restrict, ExecutionContext context) throws IndexOutOfBoundsException {
        return this;
    }

    @Override
    public Range shiftSheets(int n, Range restrict, ExecutionContext context) throws XLWrapException {
        return this;
    }

    @Override
    public Range changeFileName(String fileName, Range restrict, ExecutionContext context) {
        return this;
    }

    @Override
    public Range changeSheetName(String sheetName, Range restrict, ExecutionContext context) {
        return this;
    }

    @Override
    public Range changeSheetNumber(int n, Range restrict, ExecutionContext context) {
        return this;
    }

    @Override
    public Range getAbsoluteRange(ExecutionContext context) {
        return INSTANCE;
    }

    @Override
    public CellIterator getCellIterator(ExecutionContext context) throws XLWrapException {
        return new CellIterator(this, context) {

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Cell next() throws XLWrapException {
                return null;
            }
        };
    }

    @Override
    public boolean withinSheetBounds(ExecutionContext context) {
        return true;
    }

    @Override
    public Range copy() {
        return INSTANCE;
    }

    @Override
    public boolean subsumes(Range other, ExecutionContext context) {
        return equals(other);
    }

    @Override
    public String toString() {
        return "{}";
    }
}
