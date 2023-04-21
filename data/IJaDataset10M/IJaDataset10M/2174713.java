package org.onemind.swingweb.templaterender.peerdelegate.javax.swing;

import java.awt.Component;
import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.onemind.awtbridge.peer.BridgePeer;
import org.onemind.awtbridge.render.BridgeRenderContext;
import org.onemind.awtbridge.render.RenderingException;
import org.onemind.swingweb.templaterender.peerdelegate.TemplateRenderDelegate;

/**
 * The JTable render delegate
 * @author TiongHiang Lee (thlee@onemindsoft.org)
 * 
 */
public class JTreeRenderDelegate extends JComponentRenderDelegate {

    /** the instance **/
    public static TemplateRenderDelegate INSTANCE = new JTreeRenderDelegate();

    /**
     * Constructor
     */
    public JTreeRenderDelegate() {
        super();
    }

    /**
     * Render the cell
     * @param peer the peer
     * @param context the context
     * @param output the output object
     * @param row the row
     * @param col the column
     * @throws RenderingException if there's rendering problem
     */
    public void renderNode(BridgePeer peer, BridgeRenderContext context, Object output, TreePath path, String treePath) throws RenderingException {
        JTree tree = (JTree) peer.getComponentObject();
        TreeModel model = tree.getModel();
        Component com = null;
        Object value = path.getLastPathComponent();
        boolean enableFlag = true;
        boolean edit = tree.isEditable();
        if (edit) {
            com = (Component) tree.getCellEditor().getTreeCellEditorComponent(tree, value, true, true, model.isLeaf(value), 0);
        } else {
            com = (Component) tree.getCellRenderer().getTreeCellRendererComponent(tree, value, true, true, model.isLeaf(value), 0, true);
        }
        BridgePeer childPeer = context.getContext().getPeer(com);
        if (childPeer == null) {
            tree.add(com);
            com.addNotify();
            childPeer = context.getContext().getPeer(com);
        }
        if (edit) {
            String origId = childPeer.getId();
            String newId = peer.getId() + "_" + treePath;
            childPeer.setId(newId);
            try {
                context.renderOutput(com, output);
            } finally {
                childPeer.setId(origId);
            }
        } else {
            try {
                context.renderOutput(com, output);
            } finally {
                com.setEnabled(enableFlag);
            }
        }
    }
}
