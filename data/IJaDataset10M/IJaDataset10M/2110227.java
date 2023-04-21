package br.jabuti.gvf;

import java.awt.*;
import java.awt.geom.*;
import br.jabuti.project.*;
import br.jabuti.util.*;
import br.jabuti.graph.*;

public class GVFExitNode extends GVFNode {

    public GVFExitNode(GraphNode gn, ClassMethod m) {
        super(gn, m);
    }

    public void draw(Graphics g) {
        int c = 0;
        updateShapeSize(g);
        updateLocation();
        g.clearRect(X - ((int) width / 2), Y - ((int) height / 2), width, height);
        g.setColor(color);
        Integer colorNumber = (Integer) gn.getUserData(ToolConstants.LABEL_COLOR);
        if (colorNumber != null) {
            c = colorNumber.intValue();
        }
        Ellipse2D.Float circle = new Ellipse2D.Float();
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(5.0F));
        Rectangle2D rect = shape.getBounds2D();
        circle.setFrame(rect);
        g2.draw(circle);
        g2.setColor(ToolConstants.getColor(c));
        g2.fill(circle);
        g2.setColor(color);
        drawLabel(g);
    }
}
