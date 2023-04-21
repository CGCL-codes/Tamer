package deltavfhxconverter;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Kyle Flanigan (Ghotbust555)
 */
public class ColumnHeaderToolTips extends MouseMotionAdapter {

    TableColumn curCol;

    Map tips = new HashMap();

    public void setToolTip(TableColumn col, String tooltip) {
        if (tooltip == null) {
            tips.remove(col);
        } else {
            tips.put(col, tooltip);
        }
    }

    @Override
    public void mouseMoved(MouseEvent evt) {
        TableColumn col = null;
        JTableHeader header = (JTableHeader) evt.getSource();
        JTable table = header.getTable();
        TableColumnModel colModel = table.getColumnModel();
        int vColIndex = colModel.getColumnIndexAtX(evt.getX());
        if (vColIndex >= 0) {
            col = colModel.getColumn(vColIndex);
        }
        if (col != curCol) {
            header.setToolTipText((String) tips.get(col));
            curCol = col;
        }
    }
}
