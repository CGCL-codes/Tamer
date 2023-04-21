package nl.fountain.xelem.ztest;

import nl.fountain.xelem.Area;
import nl.fountain.xelem.excel.Worksheet;
import nl.fountain.xelem.lex.DefaultExcelReaderListener;
import nl.fountain.xelem.lex.ExcelReader;

/**
 *
 */
class SwappingAreaListener extends DefaultExcelReaderListener {

    private ExcelReader reader;

    public SwappingAreaListener(ExcelReader reader) {
        this.reader = reader;
    }

    public void startWorksheet(int sheetIndex, Worksheet sheet) {
        switch(sheetIndex) {
            case 1:
                reader.setReadArea(new Area("A1:C6"));
                break;
            case 2:
                reader.setReadArea(new Area("G11:G11"));
                break;
            default:
                reader.clearReadArea();
        }
    }

    public void endWorksheet(int sheetIndex, String sheetName) {
        super.endWorksheet(sheetIndex, sheetName);
    }
}
