package br.usp.iterador.plugin.bacia.average;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import br.usp.iterador.gui.GUIHelper;
import br.usp.iterador.model.Scale;
import br.usp.iterador.plugin.bacia.AveragesInfo;
import br.usp.iterador.plugin.bacia.BasinController;
import br.usp.iterador.plugin.bacia.model.Cloud;
import br.usp.iterador.plugin.bacia.model.MyPolygon;

/**
 * Draws the average canvas.
 * 
 * @author Guilherme Silveira
 */
class AverageCanvasDrawer {

    private final int w, h;

    private final GUIHelper helper;

    private final BasinController controller;

    private final AveragesInfo info;

    public AverageCanvasDrawer(AveragesInfo info, int w, int h, GUIHelper helper, BasinController controller) {
        this.w = w;
        this.h = h;
        this.helper = helper;
        this.info = info;
        this.controller = controller;
    }

    public void drawOldPolygons(Graphics2D g, ArrayList<Cloud> clouds) {
        for (Cloud at : clouds) {
            drawAttractor(at, g);
        }
    }

    private void drawAttractor(Cloud at, Graphics2D g) {
        drawPolygon(g, at.getPoints(), at.getColor());
    }

    private void drawLine(Graphics2D g, double[] p1, double[] p2) {
        Scale xScale = info.getXScale();
        Scale yScale = info.getYScale();
        g.drawLine((int) helper.mudaEscala(p1[0], xScale, 0, w), h - (int) helper.mudaEscala(p1[1], yScale, 0, h), (int) helper.mudaEscala(p2[0], xScale, 0, w), h - (int) helper.mudaEscala(p2[1], yScale, 0, h));
    }

    /**
	 * Draws the current polygon if any should be draw
	 * 
	 * @param g
	 */
    public void drawCurrentPolygon(Graphics2D g) {
        MyPolygon p = controller.getNewAttractorLogic().getCurrentPolygon();
        if (p != null && p.getPoints().size() != 0) drawPolygon(g, p.getPoints(), Color.YELLOW);
    }

    private void drawPolygon(Graphics2D g, List<double[]> points, Color color) {
        double[] firstPoint = points.get(0);
        double[] lastPoint = null;
        g.setColor(color);
        for (double[] point : points) {
            if (lastPoint != null) {
                drawLine(g, lastPoint, point);
            }
            lastPoint = point;
        }
        drawLine(g, firstPoint, lastPoint);
    }
}
