package remixlab.remixvym.core.graphics.link;

import processing.core.PApplet;
import processing.core.PGraphics;
import remixlab.remixvym.core.graphics.StyleGraph;
import remixlab.remixvym.core.mindmap.Node;

/** 
 * Wrapper class for drawing links.
 * 
 * Draw the connection link between two nodes using any class that 
 * implements LinkForm {@link remixlab.remixvym.core.graphics.link.LinkForm}.
 * 
 * @param {@link remixlab.remixvym.core.graphics.StyleGraph} st  The style setup.
 */
public class LinkGraph {

    public LinkGraph(StyleGraph st) {
        style = st;
        changeStyle();
    }

    /**Wrapper method. Use the reference kept in {@link #linkType}*/
    public void drawLink(Node node, PApplet parent, PGraphics pg) {
        if (node.hide) return;
        linkType.drawLine(node, parent, pg);
    }

    public void changeStyle() {
        linkType = style.getLinkStyle();
    }

    private LinkForm linkType;

    private StyleGraph style;
}
