package druid.util.gui.renderers;

import java.awt.Component;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.dlib.gui.treeview.TreeViewNode;
import druid.data.DatabaseNode;
import druid.interfaces.BasicModule;
import druid.util.gui.ImageFactory;

public class ModuleViewRenderer extends DefaultTreeCellRenderer {

    public static final String DATAGEN = "datagen";

    public static final String DTGEN_DATADICT = "datagen.dataDict";

    public static final String DTGEN_DOCS = "datagen.docs";

    public static final String DTGEN_SUMMARY = "datagen.summary";

    public static final String DTGEN_CODE = "datagen.code";

    public static final String DTGEN_SQL = "datagen.sql";

    public static final String DTGEN_TEMPLATE = "datagen.template";

    public static final String DTGEN_GENERIC = "datagen.generic";

    public static final String DATABASE = "database";

    public static final String JDBC = "jdbc";

    public static final String TREENODE = "treeNode";

    public ModuleViewRenderer() {
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean exp, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, exp, leaf, row, hasFocus);
        TreeViewNode node = (TreeViewNode) value;
        Object o = node.getUserData();
        if (o instanceof BasicModule) setIcon(ImageFactory.MODULE); else if (o instanceof DatabaseNode) {
        } else {
            String modClass = (o == null) ? "" : (String) o;
            if (modClass.equals(DATAGEN)) setIcon(ImageFactory.PAGE); else if (modClass.equals(DATABASE)) setIcon(ImageFactory.DATABASE); else if (modClass.equals(JDBC)) setIcon(ImageFactory.GEAR); else if (modClass.equals(TREENODE)) setIcon(ImageFactory.POPUP); else {
                if (node.isExpanded()) setIcon(ImageFactory.OFOLDER); else setIcon(ImageFactory.CFOLDER);
            }
        }
        return this;
    }
}
