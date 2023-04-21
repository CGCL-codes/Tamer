package org.fpdev.apps.rtemaster;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Iterator;
import org.fpdev.core.basenet.BLink;
import org.fpdev.core.basenet.BNode;
import org.fpdev.apps.rtemaster.gui.map.MapCanvas;
import org.fpdev.apps.rtemaster.gui.map.MapDrawable;

/**
 *
 * @author demory
 */
public class LinkSet extends HashSet<BLink> implements MapDrawable {

    private Color color_;

    /** Creates a new instance of LinkSet */
    public LinkSet(Color color) {
        super();
        color_ = color;
    }

    public int connectingLinks(BNode node) {
        Iterator<BLink> links = this.iterator();
        int count = 0;
        while (links.hasNext()) {
            BLink link = links.next();
            if (link.getFNode() == node || link.getTNode() == node) {
                count++;
            }
        }
        return count;
    }

    public void draw(MapCanvas canvas) {
        Iterator<BLink> links = iterator();
        while (links.hasNext()) {
            canvas.setLineWidth(3);
            canvas.setColor(color_);
            BLink link = links.next();
            link.drawCurve(canvas);
        }
    }

    public Rectangle2D getBoundingBox() {
        if (isEmpty()) {
            return null;
        }
        Iterator<BLink> links = iterator();
        Rectangle2D rect = links.next().getBoundingBox();
        while (links.hasNext()) {
            BLink link = links.next();
            if (link != null) {
                rect = rect.createUnion(link.getBoundingBox());
            }
        }
        return rect;
    }
}
