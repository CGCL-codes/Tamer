package org.eclipse.gef.examples.logicdesigner.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.RectD2D;

public class CircuitFeedbackBorder extends CircuitBorder {

    private void drawConnectors(Graphics g, RectD2D rec) {
        int y1 = rec.y, width = rec.width, x1, bottom = y1 + rec.height;
        for (int i = 0; i < 4; i++) {
            x1 = rec.x + (2 * i + 1) * width / 8;
            g.drawLine(x1 - 2, y1 + 2, x1 + 3, y1 + 2);
            connector.translate(x1, y1);
            g.drawPolygon(connector);
            connector.translate(-x1, -y1);
            g.drawLine(x1 - 2, bottom - 3, x1 + 3, bottom - 3);
            bottomConnector.translate(x1, bottom);
            g.drawPolygon(bottomConnector);
            bottomConnector.translate(-x1, -bottom);
        }
    }

    public void paint(IFigure figure, Graphics g, Insets in) {
        g.setXORMode(true);
        g.setForegroundColor(ColorConstants.white);
        g.setBackgroundColor(LogicColorConstants.ghostFillColor);
        RectD2D r = figure.getBounds().getCropped(in);
        g.fillRectangle(r.x, r.y + 2, r.width, 6);
        g.fillRectangle(r.x, r.bottom() - 8, r.width, 6);
        g.fillRectangle(r.x, r.y + 2, 6, r.height - 4);
        g.fillRectangle(r.right() - 6, r.y + 2, 6, r.height - 4);
        g.fillRectangle(r.x, r.y + 2, 6, 6);
        g.fillRectangle(r.x, r.bottom() - 8, 6, 6);
        g.fillRectangle(r.right() - 6, r.y + 2, 6, 6);
        g.fillRectangle(r.right() - 6, r.bottom() - 8, 6, 6);
        g.drawPoint(r.x, r.y + 2);
        g.drawPoint(r.x, r.bottom() - 3);
        g.drawPoint(r.right() - 1, r.y + 2);
        g.drawPoint(r.right() - 1, r.bottom() - 3);
        g.drawLine(r.x, r.y + 2, r.right() - 1, r.y + 2);
        g.drawLine(r.x, r.bottom() - 3, r.right() - 1, r.bottom() - 3);
        g.drawLine(r.x, r.y + 2, r.x, r.bottom() - 3);
        g.drawLine(r.right() - 1, r.bottom() - 3, r.right() - 1, r.y + 2);
        r.crop(new Insets(1, 1, 0, 0));
        r.expand(1, 1);
        r.crop(getInsets(figure));
        drawConnectors(g, figure.getBounds().getCropped(in));
    }
}
