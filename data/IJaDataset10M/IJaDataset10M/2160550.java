package adm.java.excel;

/**
 * @author KpMac
 * @version 1.1.0 
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestExcelWriter {

    public static void main(String[] args) {
        ExcelWriter ew = new ExcelWriter();
        ew.createWorkBook("myFilePath\\mydoc.xls");
        ew.createSheet("Test_Sheet");
        ew.write(0, "Product breakdown report - 08.28.2006");
        ew.mergeCells(0, 0, 0, 5);
        ew.makeBold(0);
        ew.nextRow(2);
        ew.write(0, "Co");
        ew.write(1, "Div");
        ew.write(2, "Season");
        ew.write(3, "Style");
        ew.write(4, "Special");
        ew.write(5, "Color");
        ew.write(6, "Dim");
        ew.write(7, "$$");
        ew.makeBold(0, 7);
        ew.nextRow();
        ew.freezePane(0, 3, 0, 3);
        ew.writeInt(0, 1);
        ew.writeInt(1, 3);
        ew.nextRow();
        for (int col = 0; col < 200; col++) {
            for (int i = 0; i < 20; i++) {
                if (i % 2 == 0) {
                    ew.write$$(i, "-1.001");
                } else {
                    ew.writeInt(i, (i + col));
                    ew.makeBold(i);
                }
                if (col % 3 == 0) {
                    try {
                        ew.colorCell(i, Color.RED);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (col % 2 == 0) {
                    ew.makeBold(i);
                    try {
                        ew.colorCell(i, Color.YELLOW);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        ew.colorCell(i, Color.BLUE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            ew.nextRow();
        }
        ew.closeWorkbook();
    }
}
