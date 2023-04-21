package wtanaka.praya.disp;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import wtanaka.praya.obj.ChoiceConfigObj;
import wtanaka.praya.obj.Obj;

/**
 * Controls the rendering of Messages and other Objs.
 * @author $Author: wtanaka $
 * @version $Name:  $ $Date: 2003/12/17 01:27:21 $
 **/
class MessageRenderer extends DefaultTableCellRenderer {

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof ChoiceConfigObj) {
            return super.getTableCellRendererComponent(table, ((ChoiceConfigObj) value).getCurrentChoice(), isSelected, hasFocus, row, column);
        } else if (value instanceof ParentDirectory) return super.getTableCellRendererComponent(table, "Parent Directory", isSelected, hasFocus, row, column); else if (value instanceof Obj) {
            return super.getTableCellRendererComponent(table, ((Obj) value).getContents(), isSelected, hasFocus, row, column);
        } else if (value != null) return super.getTableCellRendererComponent(table, value.getClass(), isSelected, hasFocus, row, column); else return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}
