package jhomenet.gui.tree;

import javax.swing.ImageIcon;
import jhomenet.system.ServerProperties;

/**
 * TODO: Class description.
 * <br>
 * Id: $Id: ChannelNode.java 1152 2006-01-06 22:50:49Z dhirwinjr $
 *
 * @author dhirwinjr
 */
public class ChannelNode extends AbstractTreeNode<String> {

    /**
	 * Serial version hardwareID information - used for the serialization process.
	 */
    private static final long serialVersionUID = 00001;

    /**
	 * Input channel.
	 */
    private int channel;

    /**
	 * Icon filename.
	 */
    private final String iconFilename = "port.gif";

    /**
	 * @param nodeObject
	 * @param allowsChildren
	 */
    public ChannelNode(Integer channelId) {
        super("Channel: " + channelId, false);
        this.channel = channelId;
        initializeIcon();
    }

    /**
	 * @see jhomenet.gui.tree.AbstractTreeNode#initializeIcon()
	 */
    @Override
    protected void initializeIcon() {
        ImageIcon icon = new ImageIcon(ServerProperties.PropertyNames.ImagesFolder.toString() + iconFilename);
        setIcon(icon);
    }

    /**
	 * Return input channel.
	 * 
	 * @return
	 */
    public int getChannel() {
        return channel;
    }

    public String getToolTipText() {
        return "Channel: " + channel;
    }

    /**
	 * @see jhomenet.gui.tree.AbstractTreeNode#equals(java.lang.Object)
	 */
    @Override
    public boolean equals(Object object) {
        if (getUserObject() == object) {
            return true;
        }
        return false;
    }
}
