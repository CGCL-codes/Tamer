package securus.client.utils.tree;

import javax.swing.ImageIcon;

/**
 *
 * @author e.dovgopoliy
 */
public class TemporaryNode extends SortedMutableTreeNode {

    public TemporaryNode() {
        super();
        icon = new ImageIcon(TemporaryNode.class.getResource("/securus/client/images/progress-animation3.gif"));
    }

    @Override
    public String toString() {
        return "Retrieving data...";
    }
}
