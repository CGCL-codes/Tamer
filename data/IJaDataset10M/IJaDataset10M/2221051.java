package org.yaoqiang.bpmn.editor.shape;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.util.Map;
import org.yaoqiang.bpmn.editor.util.GraphConstants;
import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.shape.mxBasicShape;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;

/**
 * ChoreographyShape
 * 
 * @author Shi Yaoqiang(blenta@126.com)
 */
public class ParticipantBandShape extends mxBasicShape {

    public void paintShape(mxGraphics2DCanvas canvas, mxCellState state) {
        super.paintShape(canvas, state);
        Map<String, Object> style = state.getStyle();
        if (mxUtils.isTrue(style, GraphConstants.ACTIVITY_STYLE_LOOP_MI, false)) {
            double scale = canvas.getScale();
            double imgWidth = 16 * scale;
            double imgHeight = 16 * scale;
            Rectangle imageBounds = state.getRectangle();
            imageBounds.setRect(imageBounds.getX() + (imageBounds.getWidth() - imgWidth) / 2, imageBounds.getY() + imageBounds.getHeight() - imgHeight, imgWidth, imgHeight);
            canvas.drawImage(imageBounds, canvas.getImageForStyle(style));
        }
    }

    public Shape createShape(mxGraphics2DCanvas canvas, mxCellState state) {
        Map<String, Object> style = state.getStyle();
        Rectangle rect = state.getRectangle();
        String position = mxUtils.getString(style, GraphConstants.STYLE_POSITION, "top");
        boolean border = mxUtils.isTrue(style, GraphConstants.STYLE_BORDER, true);
        int x = rect.x;
        int y = rect.y;
        int w = rect.width;
        int h = rect.height;
        int radius = getArcSize(w, h);
        if (border) {
            RoundRectangle2D roundRect = new RoundRectangle2D.Double();
            if (position.equals("top")) {
                roundRect.setRoundRect(x, y, w, h + h / 2, radius, radius);
            } else {
                roundRect.setRoundRect(x, y - h / 2, w, h + h / 2, radius, radius);
            }
            return roundRect;
        } else {
            return rect;
        }
    }

    public int getArcSize(int w, int h) {
        return GraphConstants.RECTANGLE_ARCSIZE;
    }
}
