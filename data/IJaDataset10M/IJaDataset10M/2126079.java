package Renderer.HTMLDrawer;

import Tree.Node;
import Renderer.HTMLRenderer;
import javax.swing.*;

/**
 * This class processes all Font tag Nodes.
 * @author Nathan Scully
 * @version FINAL
 */
public class Image extends Drawer {

    /**
     * The Constuctor for this class, which calls the Drawer superclass and
     * passes the node and renderer to it.
     * @param node The Node to be passed to Drawer.
     * @param renderer The Renderer to be passed to Drawer.
     */
    public Image(Node node, HTMLRenderer renderer) {
        super(node, renderer);
    }

    /**
     * The overwritten draw method. Creates an ImageIcon object using the path
     * specified by the tag, and sends it to the renderer to be drawn.
     */
    public void draw() {
        String path = ((HTMLParser.HTMLTag.Image) node).getPath();
        ImageIcon image = new ImageIcon(path);
        renderer.placeImage(image);
    }
}
