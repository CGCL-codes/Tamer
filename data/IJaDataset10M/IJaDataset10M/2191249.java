package at.langegger.xlwrap.map.range;

import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import at.langegger.xlwrap.common.XLWrapException;
import at.langegger.xlwrap.exec.ExecutionContext;
import at.langegger.xlwrap.map.MapTemplate;
import at.langegger.xlwrap.spreadsheet.Cell;
import at.langegger.xlwrap.spreadsheet.Sheet;
import at.langegger.xlwrap.spreadsheet.XLWrapEOFException;

/**
 * @author dorgon
 *
 */
public class FullSheetRange extends Range {

    protected String fileName = null;

    protected String sheetName = null;

    protected Integer sheetNum = null;

    public FullSheetRange(String fileName, String sheetName) {
        this.fileName = fileName;
        this.sheetName = sheetName;
    }

    public FullSheetRange(String fileName, Integer sheetNum) {
        this.fileName = fileName;
        this.sheetNum = sheetNum;
    }

    /**
	 * default constructor
	 */
    public FullSheetRange() {
    }

    public String getFileName() {
        return fileName;
    }

    public Integer getSheetNumber() {
        return sheetNum;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setSheetName(String sheetName) {
        this.sheetNum = null;
        this.sheetName = sheetName;
    }

    public void setSheetNumber(Integer sheetNum) {
        this.sheetName = null;
        this.sheetNum = sheetNum;
    }

    @Override
    public Range shiftCols(int n, Range restrict, ExecutionContext context) throws IndexOutOfBoundsException, XLWrapException {
        return this;
    }

    @Override
    public Range shiftRows(int n, Range restrict, ExecutionContext context) throws IndexOutOfBoundsException, XLWrapException {
        return this;
    }

    @Override
    public Range shiftSheets(int n, Range restrict, ExecutionContext context) throws XLWrapException {
        if (restrict.subsumes(this, context)) {
            if (sheetNum != null) sheetNum += n; else sheetNum = ((FullSheetRange) getAbsoluteRange(context)).sheetNum + n;
        }
        return this;
    }

    @Override
    public Range changeFileName(String fileName, Range restrict, ExecutionContext context) throws XLWrapException {
        if (restrict.subsumes(this, context)) this.fileName = fileName;
        return this;
    }

    @Override
    public Range changeSheetName(String sheetName, Range restrict, ExecutionContext context) throws XLWrapException {
        if (restrict.subsumes(this, context)) setSheetName(sheetName);
        return this;
    }

    @Override
    public Range changeSheetNumber(int n, Range restrict, ExecutionContext context) throws XLWrapException {
        if (restrict.subsumes(this, context)) setSheetNumber(n);
        return this;
    }

    @Override
    public CellIterator getCellIterator(ExecutionContext context) throws XLWrapException {
        return new CellIterator(getAbsoluteRange(context), context) {

            private FullSheetRange range;

            private int colPointer;

            private int rowPointer;

            private int colMax;

            private int rowMax;

            @Override
            public void init(Range r) throws XLWrapException {
                this.range = (FullSheetRange) r;
                colPointer = 0;
                rowPointer = 0;
                Sheet s = context.getSheet(range.fileName, range.sheetNum);
                colMax = s.getColumns() - 1;
                rowMax = s.getRows() - 1;
            }

            @Override
            public boolean hasNext() {
                return colPointer <= colMax && rowPointer <= rowMax;
            }

            @Override
            public Cell next() throws XLWrapException, XLWrapEOFException {
                Cell cell = context.getCell(range.fileName, range.sheetNum, colPointer, rowPointer);
                if (colPointer < colMax) colPointer++; else {
                    colPointer = 0;
                    rowPointer++;
                }
                return cell;
            }
        };
    }

    @Override
    public Range getAbsoluteRange(ExecutionContext context) throws XLWrapException {
        MapTemplate tmpl = context.getActiveTemplate();
        FullSheetRange r = new FullSheetRange();
        if (fileName != null) r.fileName = fileName; else r.fileName = tmpl.getFileName();
        if (sheetNum != null || sheetName != null) {
            if (sheetNum != null) {
                r.sheetNum = sheetNum;
                r.sheetName = context.getSheet(r.fileName, sheetName).getName();
            } else {
                r.sheetName = sheetName;
                r.sheetNum = context.getSheetNumber(r.fileName, sheetName);
            }
        } else {
            r.sheetNum = tmpl.getSheetNum();
            r.sheetName = tmpl.getSheetName();
        }
        return r;
    }

    @Override
    public Range copy() {
        if (sheetNum != null) return new FullSheetRange(fileName, sheetNum); else return new FullSheetRange(fileName, sheetName);
    }

    @Override
    public boolean subsumes(Range other, ExecutionContext context) throws XLWrapException {
        if (other == NullRange.INSTANCE) return true; else if (other instanceof CellRange) {
            FullSheetRange absThis = (FullSheetRange) getAbsoluteRange(context);
            CellRange absOther = (CellRange) other.getAbsoluteRange(context);
            return absThis.fileName.equals(absOther.fileName) && absThis.sheetNum.equals(absOther.sheetNum);
        } else if (other instanceof BoxRange) {
            FullSheetRange absThis = (FullSheetRange) getAbsoluteRange(context);
            BoxRange absOther = (BoxRange) other.getAbsoluteRange(context);
            return absThis.fileName.equals(absOther.fileName) && absOther.sheetNum1 >= absThis.sheetNum && absOther.sheetNum2 <= absThis.sheetNum;
        } else if (other instanceof FullSheetRange) {
            FullSheetRange absThis = (FullSheetRange) getAbsoluteRange(context);
            FullSheetRange absOther = (FullSheetRange) other.getAbsoluteRange(context);
            return absThis.fileName.equals(absOther.fileName) && absThis.sheetNum == absOther.sheetNum;
        } else if (other instanceof MultiRange) {
            Iterator<Range> it = ((MultiRange) other).getRangeIterator();
            while (it.hasNext()) {
                if (!subsumes(it.next(), context)) return false;
            }
            return true;
        } else return false;
    }

    @Override
    public boolean withinSheetBounds(ExecutionContext context) {
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        FullSheetRange other = (FullSheetRange) obj;
        return sheetName != null && other.sheetName != null && sheetName.equals(other.sheetName) || sheetNum != null && other.sheetNum != null && sheetNum.equals(other.sheetNum);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (fileName != null) sb.append("'").append(fileName).append("'#$");
        if (sheetNum != null) sb.append("#").append(sheetNum + 1).append("."); else if (sheetName != null) sb.append("'").append(sheetName).append("'.");
        sb.append("*");
        return sb.toString();
    }
}
