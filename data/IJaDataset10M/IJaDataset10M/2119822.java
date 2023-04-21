package clp.concept.change;

import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import clp.concept.ConceptNode;
import clp.core.CLPEngineState;
import clp.core.CLPException;
import clp.core.Util;
import clp.ide.CLPTreeNode;
import clp.ide.ObjectTreeMetadata;
import clp.metadata.BusinessObjectMetadata;
import clp.metadata.MetadataManager;
import clp.metadata.PropertyMetadata;

public class DeleteLinkChange extends ConceptChange {

    private String slot;

    private ConceptNode parent;

    private ConceptNode child;

    public DeleteLinkChange(CLPEngineState state, ConceptNode parent, String slot, ConceptNode child) {
        super(state);
        this.slot = slot;
        this.parent = parent;
        this.child = child;
    }

    public String getPrimaryName() {
        return slot;
    }

    public ConceptNode getPrimaryConcept() {
        return parent;
    }

    public void changeMetadata() {
        parent.removeSlot(slot);
    }

    public void undo() {
        parent.setSlot(slot, child);
    }

    public void draw(JTree tree) {
        ConceptTreeNode conceptsNode = getConceptsNode(tree);
        ConceptTreeNode conceptNode = getChildNode(conceptsNode, parent);
        if (conceptNode != null) {
            removeNode(conceptsNode, child);
        }
        tree.updateUI();
    }

    public void undraw(JTree tree) {
        ConceptTreeNode conceptsNode = getConceptsNode(tree);
        ConceptTreeNode conceptNode = getChildNode(conceptsNode, parent);
        if (conceptNode != null && child != null) {
            conceptNode.add(new ConceptTreeNode(child, "[link: " + slot + "] " + child.getValue()));
        }
        tree.updateUI();
    }
}
