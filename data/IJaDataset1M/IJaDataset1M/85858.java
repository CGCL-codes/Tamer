package com.skruk.elvis.admin.manage.resources.gui;

import com.skruk.elvis.admin.manage.resources.*;
import com.skruk.elvis.admin.structure.*;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * @author     skruk
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 * @created    20 lipiec 2004
 */
public class EntryTreeCellRenderer extends DefaultTreeCellRenderer {

    /**  Description of the Field */
    private static com.skruk.elvis.admin.i18n.ResourceFormatter formater = null;

    /**  Description of the Field */
    public static final int ETCR_ELEMENT = 0;

    /**  Description of the Field */
    public static final int ETCR_ATTRIBUTE = 1;

    /**  Description of the Field */
    private static final int ETCR_ICON_SIZE = 16;

    /**  Description of the Field */
    private static final java.awt.Font[] defaultFont = { new java.awt.Font("SansSerif", 1, 9), new java.awt.Font("SansSerif", 0, 9) };

    /**  Description of the Field */
    private static java.awt.FontMetrics[] defaultMetrics = null;

    /** Creates new form EntryTreeCellRenderer */
    public EntryTreeCellRenderer() {
        if (formater == null) {
            formater = com.skruk.elvis.admin.plugin.StructurePlugin.getInstance().getFormater();
        }
        if (defaultMetrics == null) {
            defaultMetrics = new java.awt.FontMetrics[2];
            for (int i = 0; i < 2; i++) {
                defaultMetrics[i] = getFontMetrics(defaultFont[i]);
            }
        }
        xInitComponents();
    }

    /**  Description of the Method */
    protected void xInitComponents() {
        this.setOpaque(false);
        this.setBackground(new java.awt.Color(204, 204, 255));
    }

    /**
	 *  Gets the treeCellRendererComponent attribute of the EntryTreeCellRenderer object
	 *
	 * @param  tree       Description of the Parameter
	 * @param  value      Description of the Parameter
	 * @param  expanded   Description of the Parameter
	 * @param  leaf       Description of the Parameter
	 * @param  row        Description of the Parameter
	 * @param  _selected  Description of the Parameter
	 * @param  _hasFocus  Description of the Parameter
	 * @return            The treeCellRendererComponent value
	 */
    public java.awt.Component getTreeCellRendererComponent(javax.swing.JTree tree, Object value, boolean _selected, boolean expanded, boolean leaf, int row, boolean _hasFocus) {
        super.getTreeCellRendererComponent(tree, value, _selected, expanded, leaf, row, _hasFocus);
        if (value instanceof ResourceEntryTreeNode) {
            ResourceEntryTreeNode setn = (ResourceEntryTreeNode) value;
            ResourceEntry entry = setn.getEntry();
            int type = ETCR_ELEMENT;
            String state = (entry.getSource().getXsdType() == StructureEntry.SE_XSD_TYPE_STRING || entry.isStructureAttribute()) ? "empty" : ((expanded) ? "open" : "close");
            if (entry.isStructureAttribute()) {
                type = ETCR_ATTRIBUTE;
            } else {
                type = ETCR_ELEMENT;
            }
            this.setIcon(formater.getIcon("entry_icon_file", ETCR_ICON_SIZE, state, type));
            StringBuffer sb = new StringBuffer("[");
            sb.append(entry.getRdfsLabel()).append("] ");
            if (entry.isStructureAttribute() && !(entry.getValue() instanceof java.io.File)) {
                sb.append(entry);
            } else if (entry.getValue() instanceof java.io.File) {
                sb.append(entry.getValue()).append(" -> ").append(entry.getRemoteName());
            }
            this.setText(sb.toString());
            this.setOpaque(_selected);
        }
        return this;
    }
}
