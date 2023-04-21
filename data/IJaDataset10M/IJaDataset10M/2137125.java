package saadadb.admintool.dnd;

import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import saadadb.admintool.components.AdminComponent;

/** * @version $Id: GestualTree.java 118 2012-01-06 14:33:51Z laurent.mistahl $

 * @author laurentmichel
 *
 */
public class GestualTree extends JTree implements DragGestureListener {

    public GestualTree(DefaultMutableTreeNode top) {
        super(top);
    }

    public void dragGestureRecognized(DragGestureEvent dge) {
        AdminComponent.showSuccess(this, "coucou");
    }
}
