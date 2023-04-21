package astcentric.editor.console;

import java.util.List;
import astcentric.structure.basic.Node;
import astcentric.structure.basic.NodeTool;

public class MoveInCommand extends AbstractMoveNodeCommand {

    public static final Command MOVE_IN = new MoveInCommand();

    @Override
    protected Node getNewParent(Node currentNode) {
        Node parent = currentNode.getParent();
        if (parent == null) {
            return null;
        }
        List<Node> children = NodeTool.getNodesOf(parent);
        int childIndex = children.indexOf(currentNode) + 1;
        if (childIndex < children.size()) {
            return children.get(childIndex);
        }
        return null;
    }

    @Override
    protected int calculateChildIndex(Node currentNode) {
        return 0;
    }

    public String getShortDescription() {
        return "Move the current node into its next sibling";
    }
}
