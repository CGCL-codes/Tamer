package es.eucm.eadventure.editor.gui.elementpanels.conversation.representation.graphicnode;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import es.eucm.eadventure.common.data.chapter.conversation.node.ConversationNodeView;
import es.eucm.eadventure.common.gui.TC;

/**
 * Graphic representation of a standard option node
 */
public class OptionGraphicNode extends GraphicNode {

    /**
     * Constructor
     * 
     * @param node
     *            Link to the conversational node
     * @param position
     *            Position of the node
     */
    public OptionGraphicNode(ConversationNodeView node, Point position) {
        super(node, position);
    }

    @Override
    public void drawNode(float scale, Graphics g) {
        Point position = getPosition(scale);
        int side = (int) (NODE_DIAMETER * scale);
        Polygon polygon = getPolygon(side);
        polygon.translate(position.x - side / 2, position.y - side / 2);
        g.setColor(Color.GREEN);
        g.fillPolygon(polygon);
        g.setColor(Color.GRAY);
        g.drawPolygon(polygon);
        Point textPos = getTextPosition(position, scale, side);
        g.setFont(new Font("Monospaced", Font.PLAIN, (int) (15 * scale)));
        if (node.hasEffects()) {
            g.setColor(Color.BLACK);
            g.drawString(TC.get("Effects.Title"), (int) textPos.getX(), (int) textPos.getY());
            textPos.setLocation(textPos.getX(), textPos.getY() - (15 * scale));
        }
        if (selected) {
            g.setColor(Color.RED);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke((int) (4 * scale)));
            g2.drawPolygon(polygon);
            g2.setStroke(new BasicStroke(1));
        }
    }

    private Point getTextPosition(Point position, float scale, int side) {
        Point point = new Point();
        point.setLocation(position.getX(), position.getY() + side / 2 + 5 * scale);
        return point;
    }

    @Override
    public boolean isInside(float scale, Point point) {
        Point position = getPosition(scale);
        int side = (int) (NODE_DIAMETER * scale);
        Polygon polygon = getPolygon(side);
        polygon.translate(position.x - side / 2, position.y - side / 2);
        return polygon.contains(point);
    }

    private Polygon getPolygon(int side) {
        Polygon polygon = new Polygon();
        polygon.addPoint(-side / 4, side / 2);
        polygon.addPoint(side / 2, side);
        polygon.addPoint(side + side / 4, side / 2);
        polygon.addPoint(side / 2, 0);
        return polygon;
    }
}
