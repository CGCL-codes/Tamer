package org.unitmetrics.java.ui.views.graph;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import javax.swing.ImageIcon;
import edu.uci.ics.jung.visualization.FourPassImageShaper;

/**
 * Implements an icon representing a node within the dependency viewer.
 * 
 * <p>A dependency icon is represented by three different size versions, big,
 * normal and small.</p>
 * <p>Note: The constructor blocks until all images are loaded.</p>
 * @author Martin Kersten
 */
class NodeIcon {

    private final ImageIcon normal;

    private final ImageIcon gray;

    private final ImageIcon selected;

    private final ImageIcon graySelected;

    private Shape shape;

    public NodeIcon(String filePrefix) {
        normal = new ImageIcon(NodeIcon.class.getResource(filePrefix + ".png"));
        gray = new ImageIcon(NodeIcon.class.getResource(filePrefix + "_gray.png"));
        selected = new ImageIcon(NodeIcon.class.getResource(filePrefix + "_selected.png"));
        graySelected = new ImageIcon(NodeIcon.class.getResource(filePrefix + "_gray_selected.png"));
        shape = getShape(normal);
    }

    private Shape getShape(ImageIcon imageIcon) {
        Shape shape = FourPassImageShaper.getShape(imageIcon.getImage());
        AffineTransform transform = AffineTransform.getTranslateInstance(-shape.getBounds().width / 2, -shape.getBounds().height / 2);
        return transform.createTransformedShape(shape);
    }

    public ImageIcon getNormalIcon() {
        return normal;
    }

    public ImageIcon getGrayIcon() {
        return gray;
    }

    public ImageIcon getSelectedIcon() {
        return selected;
    }

    public ImageIcon getGraySelectedIcon() {
        return graySelected;
    }

    public Shape getShape() {
        return shape;
    }
}
