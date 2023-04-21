package net.sf.rmoffice.ui.renderer;

import java.awt.Component;
import java.util.ResourceBundle;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import net.sf.rmoffice.meta.enums.MagicalItemFeatureType;

/**
 * Renderer for {@link MagicalItemFeatureType}.
 */
public class MagicalItemFeatureTypeTableCellRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 1L;

    private static final ResourceBundle RESOURCE = ResourceBundle.getBundle("conf.i18n.locale");

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        String text = RESOURCE.getString("MagicalItemFeatureType." + ((MagicalItemFeatureType) value).name());
        return super.getTableCellRendererComponent(table, text, isSelected, hasFocus, row, column);
    }
}
