package preprocessing.methods.Import.databasedata.gui.components.trees;

import java.util.Enumeration;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * This class represents node of the tree whih can be checked.
 *
 * @author Jiri Petnik
 */
public class SelectableNode extends DefaultMutableTreeNode {

    private boolean isSelected;

    /**
     * inputOutput = true - input attribute
     * inputOutput = false - output attribute
     */
    private boolean inputOutput;

    /**
     * Creates a new instance.
     * 
     * @param userObject user's object to be shown in the tree
     */
    public SelectableNode(Object userObject) {
        this(userObject, true, false);
    }

    /**
     * Creates a new instance.
     * 
     * @param userObject user's object to be shown in the tree
     * @param allowsChildren true if node's children are allowed; false otherwise
     * @param isSelected true if node is selected; false otherwise
     */
    public SelectableNode(Object userObject, boolean allowsChildren, boolean isSelected) {
        super(userObject, allowsChildren);
        this.isSelected = isSelected;
    }

    /**
     * Sets selected status.
     *
     * @param isSelected true if node is selected; false otherwise
     */
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
        if (children != null) {
            Enumeration e = children.elements();
            while (e.hasMoreElements()) {
                SelectableNode node = (SelectableNode) e.nextElement();
                node.setSelected(isSelected);
            }
        }
    }

    /**
     * Retrieves whether node is selected.
     *
     * @return true if so; false otherwise
     */
    public boolean isSelected() {
        return isSelected;
    }

    /**
     * Retrieves whether node is input.
     *
     * @return true if so; false otherwise
     */
    public boolean isInput() {
        return inputOutput;
    }

    /**
     * Retrieves whether node is output.
     *
     * @return true if so; false otherwise
     */
    public boolean isOutput() {
        return !inputOutput;
    }

    /**
     * Sets input/output status as input.
     *
     */
    public void setInput() {
        inputOutput = true;
        if (children != null) {
            Enumeration e = children.elements();
            while (e.hasMoreElements()) {
                SelectableNode node = (SelectableNode) e.nextElement();
                node.setInput();
            }
        }
    }

    /**
     * Sets input/output status as output.
     *
     */
    public void setOutput() {
        inputOutput = false;
        if (children != null) {
            Enumeration e = children.elements();
            while (e.hasMoreElements()) {
                SelectableNode node = (SelectableNode) e.nextElement();
                node.setOutput();
            }
        }
    }
}
