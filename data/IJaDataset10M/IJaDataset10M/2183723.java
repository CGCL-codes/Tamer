package mimosa.editor;

import java.util.Collection;
import mimosa.util.DefaultObjectCreationPane;

/**
 * Any edge ceration pane must derive from this abstract class to be
 * able to access the from and to node they could have dependent action
 * from.
 *
 * @author Jean-Pierre Muller
 */
@SuppressWarnings("serial")
public abstract class EdgeCreationPane extends DefaultObjectCreationPane {

    private MNode fromNode, toNode;

    private Collection<MEdge> existingEdges;

    /**
	 * @return Returns the fromNode.
	 */
    public MNode getFromNode() {
        return fromNode;
    }

    /**
	 * @param fromNode The fromNode to set.
	 */
    public void setFromNode(MNode fromNode) {
        this.fromNode = fromNode;
    }

    /**
	 * @return Returns the toNode.
	 */
    public MNode getToNode() {
        return toNode;
    }

    /**
	 * @param toNode The toNode to set.
	 */
    public void setToNode(MNode toNode) {
        this.toNode = toNode;
    }

    /**
	 * @return Returns the existingEdges.
	 */
    public Collection<MEdge> getExistingEdges() {
        return existingEdges;
    }

    /**
	 * @param existingEdges The existingEdges to set.
	 */
    public void setExistingEdges(Collection<MEdge> existingEdges) {
        this.existingEdges = existingEdges;
    }
}
