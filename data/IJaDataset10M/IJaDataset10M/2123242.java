package net.sf.cybowmodeller.modelcomposer.view;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import net.sf.cybowmodeller.modelcomposer.model.CompartmentNode;
import net.sf.cybowmodeller.modelcomposer.model.ComponentNode;
import net.sf.cybowmodeller.modelcomposer.model.CompositionElementNode;
import net.sf.cybowmodeller.modelcomposer.model.CompositionElementVisitor;
import net.sf.cybowmodeller.modelcomposer.model.NodeReference;
import net.sf.cybowmodeller.modelcomposer.model.QuantityNode;

/**
 *
 * @author SHIMAYOSHI Takao
 * @version $Revision: 37 $
 */
public class CompositionTreeCellRenderer extends DefaultTreeCellRenderer {

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        final Component c = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        if (value instanceof CompositionElementNode) {
            setIcon(IconSelector.getIcon((CompositionElementNode) value));
        }
        return c;
    }

    private static final class IconSelector implements CompositionElementVisitor<Icon, Void> {

        private static final String ICONFILE_COMPARTMENT = "Compartment.png";

        private static final String ICONFILE_QUANTITY = "Quantity.png";

        private static final String ICONFILE_COMPONENT = "Component.png";

        private static final String ICONFILE_REFERENCE = "Reference.png";

        private static final ImageIcon compartmentIcon = loadIcon(ICONFILE_COMPARTMENT);

        private static final ImageIcon quantityIcon = loadIcon(ICONFILE_QUANTITY);

        private static final ImageIcon componentIcon = loadIcon(ICONFILE_COMPONENT);

        private static final ImageIcon referenceIcon = loadIcon(ICONFILE_REFERENCE);

        private static final IconSelector instance = new IconSelector();

        public static Icon getIcon(final CompositionElementNode node) {
            return node.accept(instance, null);
        }

        public Icon visitCompartment(CompartmentNode node, Void arg) {
            return compartmentIcon;
        }

        public Icon visitComponent(ComponentNode node, Void arg) {
            return componentIcon;
        }

        public Icon visitQuantity(QuantityNode node, Void arg) {
            return quantityIcon;
        }

        public Icon visitReference(NodeReference reference, Void arg) {
            return referenceIcon;
        }

        private static ImageIcon loadIcon(final String filename) {
            return new ImageIcon(CompositionTreeCellRenderer.class.getResource(filename));
        }
    }
}
