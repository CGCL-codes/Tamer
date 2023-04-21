package ontorama.model.tree.events;

import java.util.List;
import ontorama.model.tree.TreeNode;
import org.tockit.events.Event;

/**
 * @author nataliya
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class NodeClonesRequestEvent implements Event {

    List<TreeNode> subject;

    /**
	 * Constructor for NodeClonesRequestEvent.
	 */
    public NodeClonesRequestEvent(TreeNode node) {
        subject = node.getClones();
    }

    /**
	 * @see org.tockit.events.Event#getSubject()
	 */
    public Object getSubject() {
        return subject;
    }
}
