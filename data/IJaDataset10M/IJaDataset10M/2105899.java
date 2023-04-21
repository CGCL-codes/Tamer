package de.hpi.eworld.networkview.objects.handler;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.view.mxCellState;
import de.hpi.eworld.networkview.objects.AbstractView;

public class OutlineHighlighter extends AbstractHighlighter<AbstractView<?>> {

    public OutlineHighlighter(AbstractView<?> parent) {
        super(parent);
    }

    @Override
    public void paintOutline(mxGraphics2DCanvas canvas, mxCellState state) {
        if (!isActive()) return;
        Shape outline = getParent().getOutline();
        if (outline == null) return;
        Graphics2D g2 = (Graphics2D) canvas.getGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(getStroke());
        g2.setPaint(getStrokeColor());
        g2.draw(outline);
        if (isPaintOverlay()) {
            g2.setPaint(getFillColor());
            canvas.fillShape(getParent().getOutline());
        }
        canvas.setGraphics(g2);
    }
}
