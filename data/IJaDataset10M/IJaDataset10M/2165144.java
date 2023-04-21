package org.yaoqiang.bpmn.editor.shape;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Map;
import org.yaoqiang.bpmn.editor.util.GraphConstants;
import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.shape.mxRectangleShape;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;

/**
 * TaskShape
 * 
 * @author Shi Yaoqiang(blenta@126.com)
 */
public class TaskShape extends mxRectangleShape {

    /**
	 * 
	 */
    public void paintShape(mxGraphics2DCanvas canvas, mxCellState state) {
        super.paintShape(canvas, state);
        Map<String, Object> style = state.getStyle();
        double scale = canvas.getScale();
        double imgWidth = 16 * scale;
        double imgHeight = 16 * scale;
        int spacing = 5;
        Rectangle imageBounds = state.getRectangle();
        imageBounds.setRect(imageBounds.getX() + spacing, imageBounds.getY() + spacing - 2, imgWidth, imgHeight);
        canvas.drawImage(imageBounds, canvas.getImageForStyle(style));
        String type = mxUtils.getString(style, GraphConstants.STYLE_TYPE, "");
        if (type.equals("receive")) {
            if (mxUtils.isTrue(style, GraphConstants.STYLE_INSTANTIATE, false)) {
                Graphics2D g = canvas.getGraphics();
                float pw = (float) (mxUtils.getFloat(style, mxConstants.STYLE_STROKEWIDTH, 1) * scale);
                g.setStroke(new BasicStroke(pw));
                g.drawOval((int) imageBounds.getX() - 3, (int) imageBounds.getY() - 2, (int) (imgWidth * 1.39), (int) (imgHeight * 1.39));
            }
        }
        String loop = mxUtils.getString(style, GraphConstants.STYLE_LOOP_IMAGE, " ");
        String compensation = mxUtils.getString(style, GraphConstants.STYLE_COMPENSATION_IMAGE, " ");
        if (!loop.equals(" ") || !compensation.equals(" ")) {
            Rectangle markerImageBounds = state.getRectangle();
            if (!loop.equals(" ") && !compensation.equals(" ")) {
                markerImageBounds.setRect(markerImageBounds.getX() + (markerImageBounds.getWidth() / 2 - imgWidth), markerImageBounds.getY() + markerImageBounds.getHeight() - imgHeight, imgWidth, imgHeight);
                canvas.drawImage(markerImageBounds, loop);
                markerImageBounds.setRect(markerImageBounds.getX() + (markerImageBounds.getWidth() + imgWidth) / 2, markerImageBounds.getY() + markerImageBounds.getHeight() - imgHeight, imgWidth, imgHeight);
                canvas.drawImage(markerImageBounds, compensation);
            } else {
                markerImageBounds.setRect(markerImageBounds.getX() + (markerImageBounds.getWidth() - imgWidth) / 2, markerImageBounds.getY() + markerImageBounds.getHeight() - imgHeight, imgWidth, imgHeight);
                if (!loop.equals(" ")) {
                    canvas.drawImage(markerImageBounds, loop);
                } else {
                    canvas.drawImage(markerImageBounds, compensation);
                }
            }
        }
    }

    public int getArcSize(int w, int h) {
        return GraphConstants.RECTANGLE_ARCSIZE;
    }
}
