package ch.bbv.explorer;

import javax.swing.ImageIcon;

/**
 * The icon selector defines the interface for all icon selectors. An icon
 * selector defines which icon should be associated with a tree node in the
 * navigation panel.
 * @author marcelbaumann
 * @version $Revision: 1.4 $
 */
public interface IconSelector {

    /**
   * Selects the image icon for the given explorer node.
   * @param node node which icon is requested
   * @param expanded indicates if the node is expanded or not
   * @return the rquested image icon if defined otherwise null
   */
    ImageIcon select(ExplorerNode node, boolean expanded);
}
