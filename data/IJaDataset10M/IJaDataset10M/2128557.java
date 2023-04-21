package hoplugins.teamAnalyzer.ui.renderer;

import hoplugins.Commons;
import plugins.IHelper;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * DOCUMENT ME!
 *
 * @author Tommi Rautava &lt;kenmooda
 *
 * @users.sourceforge.net&gt;
 */
public class PlayerPositionTableCellRenderer extends DefaultTableCellRenderer {

    /** TODO Missing Parameter Documentation */
    private static final long serialVersionUID = 3258412837305923127L;

    private static Map<Object, ImageIcon> map;

    private static IHelper helper;

    /**
     *
     */
    public PlayerPositionTableCellRenderer() {
        super();
        if (map == null) {
            map = new HashMap<Object, ImageIcon>();
        }
        if (helper == null) {
            helper = Commons.getModel().getHelper();
        }
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        if (value instanceof Integer) {
            super.getTableCellRendererComponent(table, null, isSelected, hasFocus, row, col);
            int pos = ((Integer) value).intValue();
            ImageIcon icon;
            if (map.containsKey(value)) {
                icon = map.get(value);
            } else {
                icon = Commons.getModel().getHelper().getImage4Position(helper.getPosition(pos), (byte) 0);
                map.put(value, icon);
            }
            this.setIcon(icon);
            this.setText(helper.getNameForPosition((byte) pos));
        } else {
            this.setIcon(null);
            this.setText(null);
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
        }
        return this;
    }
}
