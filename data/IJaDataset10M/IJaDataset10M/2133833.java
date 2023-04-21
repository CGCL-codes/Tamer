package medimagesuite.workspace2d.openfrombd;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import medimagesuite.development.DevelopmentConstants;

class ExamTableColumnModel extends DefaultTableColumnModel {

    public static final int modalityColumnSize = 100;

    public static final int dateColumnSize = 160;

    public static final int imgQtdColumnSize = 60;

    public static final int tableSize = modalityColumnSize + dateColumnSize + imgQtdColumnSize;

    public void addColumn(TableColumn aColumn) {
        String id = (String) aColumn.getHeaderValue();
        if (DevelopmentConstants.DEBUG_ENABLED) System.out.println("[DEBUG] " + id);
        if (id.equals(ExamTableModel.headerModality)) {
            aColumn.setPreferredWidth(modalityColumnSize);
        } else if (id.equals(ExamTableModel.headerDate)) {
            aColumn.setPreferredWidth(dateColumnSize);
        } else if (id.equals(ExamTableModel.headerQtdImg)) {
            aColumn.setPreferredWidth(imgQtdColumnSize);
        }
        aColumn.setResizable(false);
        super.addColumn(aColumn);
    }
}
