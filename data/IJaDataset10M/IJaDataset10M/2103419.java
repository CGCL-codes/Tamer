package org.eclipse.smd.gef.figure;

import org.eclipse.draw2d.ConnectionLocator;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.PointList;

/**
 * @author Pierrick HYMBERT (phymbert [at] users.sourceforge.net) 
 */
public class TransitionFigure extends PolylineConnection {

    private IFigure contentPane;

    /** Template for a triangle that points to the right when the rotation angle is 0 */
    public TransitionFigure(String name) {
        super();
        PolygonDecoration polygonDecoration = new PolygonDecoration() {

            @Override
            protected void fillShape(Graphics g) {
            }

            @Override
            protected void outlineShape(Graphics g) {
                g.setLineWidth(2);
                PointList pointList = getPoints();
                g.drawLine(pointList.getPoint(0), pointList.getPoint(1));
                g.drawLine(pointList.getPoint(0), pointList.getPoint(2));
            }
        };
        PointList TRIANGLE_TIP = new PointList();
        TRIANGLE_TIP.addPoint(0, 0);
        TRIANGLE_TIP.addPoint(-2, 2);
        TRIANGLE_TIP.addPoint(-2, -2);
        polygonDecoration.setTemplate(TRIANGLE_TIP);
        setTargetDecoration(polygonDecoration);
        contentPane = new Figure();
        contentPane.setLayoutManager(new ToolbarLayout());
        ConnectionLocator connectionLocator = new ConnectionLocator(this);
        add(contentPane, connectionLocator);
    }

    public IFigure getContentPane() {
        return contentPane;
    }
}
