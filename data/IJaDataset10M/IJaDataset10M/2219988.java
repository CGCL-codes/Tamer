package jformulaengine.runtime.resolvers;

import java.util.Vector;
import jformulaengine.parser.FormulaParser;
import jformulaengine.parser.ParseException;
import jformulaengine.runtime.Formula;
import jformulaengine.runtime.FormulaException;
import jformulaengine.runtime.Result;
import jformulaengine.runtime.State;
import jxl.BooleanCell;
import jxl.Cell;
import jxl.CellReferenceHelper;
import jxl.CellType;
import jxl.DateCell;
import jxl.FormulaCell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;

/**
 * @author gcocks
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class JxlNameResolver extends NameResolver {

    protected String m_sSheet = null;

    protected int m_nStartRow = 0;

    protected int m_nEndRow = 0;

    protected int m_nStartCol = 0;

    protected int m_nEndCol = 0;

    public Result resolve(State state) throws FormulaException {
        Sheet sheet = null;
        if (state.obj instanceof Workbook) {
            if (m_sSheet != null) {
                sheet = ((Workbook) state.obj).getSheet(m_sSheet);
            } else {
                sheet = ((Workbook) state.obj).getSheet(0);
            }
        } else {
            sheet = (Sheet) state.obj;
        }
        Result res = null;
        if (m_nStartRow == m_nEndRow && m_nStartCol == m_nEndCol) {
            res = getCellValue(sheet.getCell(m_nStartCol, m_nStartRow), sheet);
        } else if (m_nStartRow == m_nEndRow) {
            Vector results = getRow(sheet, m_nStartRow, m_nStartCol, m_nEndCol);
            res = new Result(results, "java.util.Vector");
        } else if (m_nStartCol == m_nEndCol) {
            Vector results = getCol(sheet, m_nStartCol, m_nStartRow, m_nEndRow);
            res = new Result(results, "java.util.Vector");
        } else {
            Vector grid = new Vector();
            for (int i = m_nStartRow; i <= m_nEndRow; i++) {
                Vector results = getRow(sheet, i, m_nStartCol, m_nEndCol);
                grid.add(new Result(results, "java.util.Vector"));
            }
            res = new Result(grid, "java.util.Vector");
        }
        return res;
    }

    public void setName(String sName) {
        if (sName.indexOf("!") > 0) {
            m_sSheet = sName.substring(0, sName.indexOf("!"));
            sName = sName.substring(sName.indexOf("!") + 1, sName.length());
        }
        String sLeft = null;
        String sRight = null;
        if (sName.indexOf(":") > 0) {
            sLeft = sName.substring(0, sName.indexOf(":"));
            sRight = sName.substring(sName.indexOf(":") + 1, sName.length());
        } else {
            sLeft = sName;
        }
        m_nStartCol = CellReferenceHelper.getColumn(sLeft);
        m_nStartRow = CellReferenceHelper.getRow(sLeft);
        if (sRight != null) {
            m_nEndCol = CellReferenceHelper.getColumn(sRight);
            m_nEndRow = CellReferenceHelper.getRow(sRight);
        } else {
            m_nEndCol = m_nStartCol;
            m_nEndRow = m_nStartRow;
        }
    }

    private Vector getRow(Sheet sheet, int nRow, int nStartCol, int nEndCol) throws FormulaException {
        Vector results = new Vector();
        Cell[] row = sheet.getRow(nRow);
        for (int i = nStartCol; i <= nEndCol; i++) {
            Result next = getCellValue(row[i], sheet);
            results.add(next);
        }
        return results;
    }

    private Vector getCol(Sheet sheet, int nCol, int nStartRow, int nEndRow) throws FormulaException {
        Vector results = new Vector();
        Cell[] col = sheet.getColumn(nCol);
        for (int i = nStartRow; i <= nEndRow; i++) {
            Result next = getCellValue(col[i], sheet);
            results.add(next);
        }
        return results;
    }

    private Result getCellValue(Cell cell, Sheet sheet) throws FormulaException {
        Result res = null;
        if (cell.getType().equals(CellType.NUMBER)) {
            NumberCell numCell = (NumberCell) cell;
            double d = numCell.getValue();
            res = new Result(new Double(d), "java.lang.Double");
        } else if (cell.getType().equals(CellType.DATE)) {
            DateCell dateCell = (DateCell) cell;
            res = new Result(dateCell.getDate(), "java.util.Date");
        } else if (cell.getType().equals(CellType.BOOLEAN)) {
            BooleanCell boolCell = (BooleanCell) cell;
            boolean b = boolCell.getValue();
            res = new Result(new Boolean(b), "java.lang.Boolean");
        } else if (cell.getType().equals(CellType.BOOLEAN_FORMULA) || cell.getType().equals(CellType.DATE_FORMULA) || cell.getType().equals(CellType.STRING_FORMULA) || cell.getType().equals(CellType.NUMBER_FORMULA)) {
            FormulaCell formulaCell = (FormulaCell) cell;
            try {
                String sFormula = formulaCell.getFormula();
                FormulaParser parser = new FormulaParser(sFormula);
                Formula formula = parser.parse();
                Object obj = formula.execute(sheet);
                res = new Result(obj, obj.getClass().getName());
            } catch (Exception e) {
                throw new FormulaException(e);
            }
        } else {
            res = new Result(cell.getContents(), "java.lang.String");
        }
        return res;
    }
}
