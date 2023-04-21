package org.cumt.view.usecases;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import org.cumt.model.analysis.usecases.Association;
import org.cumt.view.BaseComponent;
import org.cumt.view.TwoPointRelationView;

/**
 * 
 * @author <a href="cdescalzi2001@yahoo.com.ar">Carlos Descalzi</a>
 *
 */
@SuppressWarnings("serial")
public class AssociationView extends TwoPointRelationView<Association> {

    @SuppressWarnings("unchecked")
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        BaseComponent source = getSource();
        BaseComponent target = getTarget();
        if (source == null || target == null) {
            return;
        }
        drawLine(g2d);
        Point[] lastSegment = getLastSegment();
        drawArrow(g, lastSegment[1], lastSegment[0], 30, Math.PI / 8, false, false, getForeground(), null);
    }
}
