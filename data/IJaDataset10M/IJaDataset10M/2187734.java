package commonapp.gui;

import java.awt.Component;
import java.awt.Font;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
   This class is a tree cell renderer for common applications.
*/
public class ITreeCellRenderer extends DefaultTreeCellRenderer {

    /** Serialization ID. */
    private static final long serialVersionUID = 1L;

    /** Font used for tree cell. */
    private static final Font FONT = FontAttr.BASE.getFont();

    /** List of renderers in use, keyed by object (node) type. */
    private static HashMap<Class<?>, DefaultTreeCellRenderer> ourRenderers = new HashMap<Class<?>, DefaultTreeCellRenderer>();

    /**
     Constructs a new ITreeCellRenderer.
  */
    public ITreeCellRenderer() {
        super();
    }

    /**
     Configures the renderer based on the passed in components.

     @param theTree the JTree on which this renderer operates.

     @param theNode the Object that is being rendered.

     @param theIsSelected indicates whether the object is currently selected.

     @param theIsExpanded indicates whether the object is in expanded form.

     @param theIsLeaf indicates if the object is a leaf node.

     @param theRow the row number of the object.

     @param theHasFocus indicates whether the object has focus.

     @return always returns this renderer.
  */
    @Override
    public Component getTreeCellRendererComponent(JTree theTree, Object theNode, boolean theIsSelected, boolean theIsExpanded, boolean theIsLeaf, int theRow, boolean theHasFocus) {
        DefaultTreeCellRenderer renderer = null;
        ITreeNode node = (ITreeNode) theNode;
        TreeDataNode dataNode = node.getDataNode();
        if (dataNode != null) {
            renderer = ourRenderers.get(dataNode.getClass());
            if (renderer != null) {
                renderer = (DefaultTreeCellRenderer) renderer.getTreeCellRendererComponent(theTree, theNode, theIsSelected, theIsExpanded, theIsLeaf, theRow, theHasFocus);
            } else {
                renderer = new DefaultTreeCellRenderer();
                renderer.setFont(FONT);
                renderer = (DefaultTreeCellRenderer) renderer.getTreeCellRendererComponent(theTree, theNode, theIsSelected, theIsExpanded, theIsLeaf, theRow, theHasFocus);
                ourRenderers.put(dataNode.getClass(), renderer);
            }
            ImageIcon icon = null;
            String iconName = dataNode.getIconName();
            if ((iconName != null) && !iconName.equals("")) {
                icon = IconFactory.main.getIcon(iconName, IconFactory.SIZE_TREE);
            }
            if (icon != null) {
                renderer.setIcon(icon);
            }
            String tip = null;
            tip = dataNode.getNodeToolTip();
            if ((tip != null) && !tip.equals("")) {
                renderer.setToolTipText(tip);
            }
        }
        return renderer;
    }
}
