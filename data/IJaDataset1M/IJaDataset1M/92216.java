package remixlab.remixvym.core.graphics.node;

import processing.core.PApplet;
import remixlab.remixvym.core.mindmap.Node;

/**
 * This class draws the node borders like rectangles.
 * <p>
 * Extends {@link remixlab.remixvym.core.graphics.node.NodeShape}<p>
 * <b>See examples: </b> NodeStyle
 */
public class RectNode extends NodeShape {

    /**
	 * Draws the node border like rectangles.
	 * <p>
	 * 
	 * @param node
	 * @param parent
	 */
    public void draw(Node node, PApplet parent) {
        parent.smooth();
        parent.stroke(0);
        parent.rect(node.x1, node.y1, node.width, node.height);
    }
}
