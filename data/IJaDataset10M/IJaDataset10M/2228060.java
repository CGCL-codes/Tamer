package jest.ui;

import javax.swing.tree.TreeNode;

/**
  * A TreePeer is a Component that may be notified that it will
  * be associated in some way with a given TreeNode.
  *
  * @author David J. Trombley
  * @version 1.0
  */
public interface TreePeer {

    public void setPeer(TreeNode peer);
}
