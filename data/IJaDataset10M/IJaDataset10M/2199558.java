package play.view;

import higraph.view.NodeView;
import higraph.view.SubgraphView;
import higraph.view.ZoneView;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.RectangularShape;
import java.util.List;
import play.model.EdgePLAY;
import play.model.EdgePayloadPLAY;
import play.model.NodePLAY;
import play.model.NodePayloadPLAY;
import play.model.SubGraphPLAY;
import play.model.WholeGraphPLAY;
import play.tags.PLAYTags;

/**
 * @author  Charles
 */
public class NodeViewPLAY extends NodeView<NodePayloadPLAY, EdgePayloadPLAY, WholeGraphPLAY, SubGraphPLAY, NodePLAY, EdgePLAY> {

    /**
	 * @uml.property  name="vf"
	 * @uml.associationEnd  
	 */
    private ViewFactoryPLAY vf;

    /**
	 * @uml.property  name="sgv"
	 * @uml.associationEnd  
	 */
    private SubGraphViewPLAY sgv;

    public NodeViewPLAY(ViewFactoryPLAY vf, SubgraphView<NodePayloadPLAY, EdgePayloadPLAY, WholeGraphPLAY, SubGraphPLAY, NodePLAY, EdgePLAY> v, NodePLAY node, Color c, Color f, Stroke s, RectangularShape r) {
        super(vf, v, node, c, f, s, r);
        this.vf = vf;
        this.sgv = (SubGraphViewPLAY) v;
    }

    @Override
    public NodeViewPLAY getChild(int i) {
        return (NodeViewPLAY) super.getChild(i);
    }

    @Override
    protected void drawSelf(Graphics2D screen) {
        PLAYTags tag = this.getNode().getPayload().getTag();
        screen.setColor(Color.black);
        screen.setStroke(new BasicStroke(1));
        screen.draw(getExtent());
        double w = getExtent().getWidth();
        double h = getExtent().getHeight();
        this.setSize(w, h);
        this.color = Color.black;
        super.drawSelf(screen);
        float x = (float) (getExtent().getMinX() + 3);
        float y = (float) (getExtent().getMinY() + 15);
        screen.setColor(Color.red);
        screen.drawString(tag.defaultPayload().getName(), x, y);
        if (tag == PLAYTags.IF) {
            float stringY = (float) this.getChild(1).getExtent().getMinY();
            screen.drawString("then", x, stringY + 15);
            stringY = (float) this.getChild(2).getExtent().getMinY();
            screen.drawString("else", x, stringY + 15);
        }
        if (tag == PLAYTags.WHILE) {
            float stringY = (float) this.getChild(1).getExtent().getMinY();
            screen.drawString("do", x, stringY + 15);
        }
    }

    @Override
    protected void drawChildren(Graphics2D screen) {
        this.color = Color.black;
        super.drawChildren(screen);
    }

    @Override
    public ViewFactoryPLAY getViewFactory() {
        return this.vf;
    }

    public List<ZoneView<NodePayloadPLAY, EdgePayloadPLAY, WholeGraphPLAY, SubGraphPLAY, NodePLAY, EdgePLAY>> getZoneList() {
        return this.zones;
    }

    public SubGraphViewPLAY getSGV() {
        return this.sgv;
    }
}
