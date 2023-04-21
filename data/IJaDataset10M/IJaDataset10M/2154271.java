package com.alveole.studio.web.designer.graph;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import com.alveole.studio.web.data.NodeContainer;
import com.alveole.studio.web.designer.Plugins;
import edu.uci.ics.jung.graph.ArchetypeVertex;
import edu.uci.ics.jung.graph.decorators.VertexIconFunction;
import edu.uci.ics.jung.visualization.PickedInfo;

/**
 * This class find the icon to be displayed for each node of the graph.
 * 
 * @author sylvain
 *
 */
public class NodeIconLabeller implements VertexIconFunction {

    /**
	 * The Junk pick info.
	 */
    private PickedInfo pi;

    /**
	 * The package navigator data model.
	 */
    private PackageNavigatorModel model;

    /**
	 * Constructor.
	 * @param model Navigator data model.
	 * @param pi Picked info.
	 */
    public NodeIconLabeller(PackageNavigatorModel model, PickedInfo pi) {
        this.pi = pi;
        this.model = model;
    }

    /**
	 * Get icon, before checking for picked state.
	 * @param v The vertex to display.
	 * @return The icon.
	 */
    private Image getInnerIcon(ArchetypeVertex v) {
        NodeContainer nc = (NodeContainer) v.getUserDatum("node");
        return Plugins.getPlugins().getNodeDisplayIcon(nc);
    }

    /**
	 * Get icon to display.
	 * @param v The vertex to display.
	 * @return The icon.
	 */
    public Icon getIcon(ArchetypeVertex v) {
        Image ret = getInnerIcon(v);
        if (ret != null) {
            NodeContainer nc = (NodeContainer) v.getUserDatum("node");
            if (!model.getCurrentPackage().getSubNodes().contains(nc)) {
                BufferedImage img = new BufferedImage(ret.getWidth(null), ret.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
                Graphics2D g = (Graphics2D) img.getGraphics();
                g.drawImage(ret, 0, 0, null);
                try {
                    g.drawImage(ImageIO.read(getClass().getClassLoader().getResource("/icons/little_folder.png")), img.getWidth() - 16, img.getHeight() - 16, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                g.dispose();
                ret = img;
            }
            if (pi.isPicked(v)) {
                BufferedImage img = new BufferedImage(ret.getWidth(null), ret.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
                Graphics2D g = (Graphics2D) img.getGraphics();
                AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC, 0.5f);
                g.setComposite(composite);
                g.drawImage(ret, 0, 0, null);
                g.dispose();
                ret = img;
            }
        }
        return new ImageIcon(ret);
    }

    /**
	 * get picked-info.
	 * @return The picked-info.
	 */
    public PickedInfo getPi() {
        return pi;
    }

    /**
	 * Set picked-info.
	 * @param pi The picked info.
	 */
    public void setPi(PickedInfo pi) {
        this.pi = pi;
    }
}
