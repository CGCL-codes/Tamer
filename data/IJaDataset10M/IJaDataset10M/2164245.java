package gov.nih.niaid.bcbb.nexplorer3.server.datamodels.layout;

import gov.nih.niaid.bcbb.nexplorer3.server.interfaces.CDAONodeView;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.List;
import salvo.jesus.graph.WeightedEdge;

public class LayoutUnrooted extends LayoutBase {

    static final float STARTING_ANGLE = (float) -Math.PI / 2;

    @Override
    public void layoutImpl() {
        float angle = STARTING_ANGLE + layoutAngle / 360f * (float) Math.PI * 2f;
        layoutNode((CDAONodeView) tree.getRoot(), angle, angle + 2 * Math.PI);
    }

    @Override
    public void drawLine(Graphics2D g, CDAONodeView p, CDAONodeView c) {
        g.draw(new Line2D.Float(c.getX(), c.getY(), p.getX(), p.getY()));
    }

    void layoutNode(CDAONodeView n, double loAngle, double hiAngle) {
        try {
            if (tree.isRoot(n)) {
                setPosition(n, 0, 0);
            }
            if (tree.isLeaf(n)) return;
            float numEnclosed = tree.getNumEnclosedLeaves(n);
            double curX = n.getX();
            double curY = n.getY();
            List<CDAONodeView> children = tree.getChildren(n);
            double curAngle = loAngle;
            for (int i = 0; i < children.size(); i++) {
                CDAONodeView child = children.get(i);
                float childEnclosed = tree.getNumEnclosedLeaves(child);
                double childRatio = childEnclosed / numEnclosed;
                double arcSize = childRatio * (hiAngle - loAngle);
                WeightedEdge we = (WeightedEdge) tree.getEdge(tree.getParent(child), child);
                double length = we.getWeight() * 10;
                double midAngle = curAngle + arcSize / 2;
                double newX = curX + Math.cos(midAngle) * length;
                double newY = curY + Math.sin(midAngle) * length;
                setPosition(child, (float) newX, (float) newY);
                setAngle(child, (float) midAngle);
                layoutNode(child, curAngle, curAngle + arcSize);
                curAngle += arcSize;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
