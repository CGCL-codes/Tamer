package ui.workbook;

import model.Sheet;
import model.Cell;

public class TableData {

    private model.Sheet sheet;

    public TableData(String name, int row, int col) {
        sheet = new Sheet(name, row, col);
    }

    public String getCellData(int row, int col) {
        return sheet.getCell(row, col).getContents();
    }

    public void setCellData(String newData, int row, int col) {
        Cell cell = sheet.getCell(row, col);
        if (cell == null) ;
        cell.setContents(newData);
    }

    public int getRowCount() {
        return sheet.getRows();
    }

    public int getColumnCount() {
        return sheet.getColumns();
    }

    public model.Sheet getSheet() {
        return sheet;
    }
}
