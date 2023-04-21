package org.jazzteam.bpe.model.nodes;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import org.jazzteam.bpe.model.actions.Action;
import org.jazzteam.bpe.model.nodes.interfaces.INextable;

/**
 * Node that contains only action and reference to next node.
 * 
 * @author skars
 * @version $Rev: $
 */
@Entity
@PrimaryKeyJoinColumn(name = "fk_node_id")
public class ActionNode extends Node implements INextable {

    /** Reference to next node in process. */
    private Node nextNode;

    /**
	 * Constructs action node.
	 */
    public ActionNode() {
        super();
    }

    /**
	 * Constructs action node.
	 * 
	 * @param name
	 *            Node name.
	 * @param action
	 *            Node action.
	 */
    public ActionNode(String name, Action action) {
        super(name, action);
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_next_node_id")
    @Override
    public Node getNextNode() {
        return nextNode;
    }

    @Override
    public void setNextNode(Node nextNode) {
        this.nextNode = nextNode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ActionNode node = (ActionNode) obj;
        return (super.equals(node) & nextNode.equals(node.getNextNode()));
    }
}
