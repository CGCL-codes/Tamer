package org.pagger.view.properties;

import java.awt.Component;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.pagger.data.Metadata;
import org.pagger.data.Property;
import org.pagger.util.I18n;

/**
 * @author Franz Wilhelmstötter
 */
class MetadataTreeCellRenderer extends DefaultTreeCellRenderer {

    private static final long serialVersionUID = -9030406356485134109L;

    @Override
    public Component getTreeCellRendererComponent(final JTree tree, final Object value, final boolean sel, final boolean expanded, final boolean leaf, final int row, final boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        if (value instanceof Metadata) {
            final Metadata md = (Metadata) value;
            setText(I18n.getString(md.getName()));
            setToolTipText(I18n.getString(md.getDescription()));
        } else if (value instanceof Property<?>) {
            final Property<?> p = (Property<?>) value;
            setText(I18n.getString(p.getName()) + " (" + p.getType().getClass().getSimpleName() + ")");
            setToolTipText(I18n.getString(p.getDescription()));
        } else if (value instanceof MetadataModel.MetadataElement) {
            final MetadataModel.MetadataElement mde = (MetadataModel.MetadataElement) value;
            setText(I18n.getString(mde.getMetadata().getName()));
            setToolTipText(I18n.getString(mde.getMetadata().getDescription()));
        } else if (value instanceof MetadataModel.PropertyElement) {
            final MetadataModel.PropertyElement pe = (MetadataModel.PropertyElement) value;
            setText(I18n.getString(pe.getProperty().getName()) + " (" + pe.getProperty().getType().getSimpleName() + ")");
            setToolTipText(I18n.getString(pe.getProperty().getDescription()));
        }
        return this;
    }
}
