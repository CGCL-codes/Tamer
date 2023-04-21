package org.rdv.ui;

import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.rdv.DataViewer;
import org.rdv.rbnb.Channel;
import org.rdv.rbnb.RBNBController;
import com.rbnb.sapi.ChannelTree;

/**
 * Renderer for cells in a channel tree.
 * 
 * @author Jason P. Hanley
 */
public class ChannelTreeCellRenderer extends DefaultTreeCellRenderer {

    /** serialization version identifier */
    private static final long serialVersionUID = -8564821584949049965L;

    private static final Icon SERVER_ICON = DataViewer.getIcon("icons/server.gif");

    private static final Icon FOLDER_OPEN_ICON = DataViewer.getIcon("icons/folder_open.gif");

    private static final Icon FOLDER_ICON = DataViewer.getIcon("icons/folder.gif");

    private static final Icon DATA_ICON = DataViewer.getIcon("icons/data.gif");

    private static final Icon JPEG_ICON = DataViewer.getIcon("icons/jpeg.gif");

    private static final Icon TEXT_ICON = DataViewer.getIcon("icons/text.gif");

    private static final Icon FILE_ICON = DataViewer.getIcon("icons/file.gif");

    public ChannelTreeCellRenderer() {
        super();
        setBorder(BorderFactory.createEmptyBorder(1, 0, 1, 0));
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        if (value == tree.getModel().getRoot()) {
            return this;
        }
        ChannelTree.Node node = (ChannelTree.Node) value;
        ChannelTree.NodeTypeEnum type = node.getType();
        setText(node.getName());
        if (type == ChannelTree.SERVER) {
            setIcon(SERVER_ICON);
        } else if (type == ChannelTree.FOLDER || type == ChannelTree.SOURCE || type == ChannelTree.PLUGIN) {
            if (expanded) {
                setIcon(FOLDER_OPEN_ICON);
            } else {
                setIcon(FOLDER_ICON);
            }
        } else if (type == ChannelTree.CHANNEL) {
            Channel channel = RBNBController.getInstance().getMetadataManager().getChannel(node.getFullName());
            String mime = channel.getMetadata("mime");
            if (mime.equals("application/octet-stream")) {
                setIcon(DATA_ICON);
            } else if (mime.equals("image/jpeg") || mime.equals("video/jpeg")) {
                setIcon(JPEG_ICON);
            } else if (mime.equals("text/plain")) {
                setIcon(TEXT_ICON);
            } else {
                setIcon(FILE_ICON);
            }
        }
        return this;
    }
}
