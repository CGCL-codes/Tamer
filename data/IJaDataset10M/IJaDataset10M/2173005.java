package net.sf.cybowmodeller.modelcomposer.view;

import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
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
public final class JCompositionTree extends JTree {

    private static final Editable editable = new Editable();

    public JCompositionTree() {
        super((TreeModel) null);
        setEditable(true);
        setRootVisible(false);
        setShowsRootHandles(true);
        setCellRenderer(new CompositionTreeCellRenderer());
    }

    @Override
    public boolean isPathEditable(final TreePath path) {
        final CompositionElementNode element = (CompositionElementNode) path.getLastPathComponent();
        return element.accept(editable, null);
    }

    private static final class Editable implements CompositionElementVisitor<Boolean, Void> {

        public Boolean visitCompartment(CompartmentNode node, Void arg) {
            return true;
        }

        public Boolean visitComponent(ComponentNode node, Void arg) {
            return true;
        }

        public Boolean visitQuantity(QuantityNode node, Void arg) {
            return true;
        }

        public Boolean visitReference(NodeReference reference, Void arg) {
            return false;
        }
    }
}
