package org.hironico.dbtool2.dbexplorer;

import java.awt.Component;
import java.awt.Font;
import java.sql.DatabaseMetaData;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;
import org.hironico.database.SQLDatabase;
import org.hironico.database.SQLProcedureColumn;
import org.hironico.database.SQLTable;
import org.hironico.database.SQLTableColumn;
import org.hironico.gui.image.ImageCache;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;

/**
 *
 * @author hironico
 */
public class SQLObjectTreeCellRenderer extends JLabel implements TreeCellRenderer {

    protected static final ImageIcon keyIcon = ImageCache.getInstance().loadImageIcon("org/hironico/resource/icons/inconexperience/small/shadow/key1.png");

    protected static final ImageIcon tableIcon = ImageCache.getInstance().loadImageIcon("org/hironico/resource/icons/inconexperience/small/shadow/table.png");

    protected static final ImageIcon columnIcon = ImageCache.getInstance().loadImageIcon("org/hironico/resource/icons/inconexperience/small/shadow/column.png");

    protected static final ImageIcon folderIcon = ImageCache.getInstance().loadImageIcon("org/hironico/resource/icons/inconexperience/small/shadow/folder.png");

    protected static final ImageIcon viewIcon = ImageCache.getInstance().loadImageIcon("org/hironico/resource/icons/inconexperience/small/shadow/view.png");

    protected static final ImageIcon datacopyIcon = ImageCache.getInstance().loadImageIcon("org/hironico/resource/icons/inconexperience/small/shadow/data_copy.png");

    protected static final ImageIcon leftArrowIcon = ImageCache.getInstance().loadImageIcon("org/hironico/resource/icons/16x16/orange_arrow_left.png");

    protected static final ImageIcon rightArrowIcon = ImageCache.getInstance().loadImageIcon("org/hironico/resource/icons/16x16/blue_arrow_right.png");

    public SQLObjectTreeCellRenderer() {
        setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        setIcon(null);
        if (value == null) {
            setText("NULL");
        } else {
            if (value instanceof String) {
                setText(value.toString());
            } else {
                DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode) value;
                Object obj = node.getUserObject();
                setText(obj.toString());
                if (obj instanceof String) {
                    if (obj.toString().startsWith("Please wait")) {
                        setIcon(viewIcon);
                    } else {
                        setIcon(folderIcon);
                    }
                }
                if (obj instanceof SQLDatabase) {
                    setIcon(datacopyIcon);
                }
                if (obj instanceof SQLTable) {
                    setIcon(tableIcon);
                }
                if (obj instanceof SQLTableColumn) {
                    setIcon(columnIcon);
                }
                if (obj instanceof SQLProcedureColumn) {
                    SQLProcedureColumn col = (SQLProcedureColumn) obj;
                    if (col.getMode() == DatabaseMetaData.procedureColumnIn) {
                        setIcon(rightArrowIcon);
                    } else if (col.getMode() == DatabaseMetaData.procedureColumnOut) {
                        setIcon(leftArrowIcon);
                    }
                }
            }
        }
        return this;
    }
}
