package org.aiotrade.charting.widget;

import java.awt.geom.GeneralPath;
import org.aiotrade.charting.widget.CandleBar.Model;

/**
 *
 * @author  Caoyuan Deng
 * @version 1.0, November 27, 2006, 4:11 PM
 * @since   1.0.4
 */
public class CandleBar extends PathWidget<Model> {

    public static final class Model implements WidgetModel {

        float xCenter;

        float yOpen;

        float yHigh;

        float yLow;

        float yClose;

        float width;

        boolean filled;

        public void set(float xCenter, float yOpen, float yHigh, float yLow, float yClose, float width, boolean filled) {
            this.xCenter = xCenter;
            this.yOpen = yOpen;
            this.yHigh = yHigh;
            this.yLow = yLow;
            this.yClose = yClose;
            this.width = width;
            this.filled = filled;
        }
    }

    public CandleBar() {
        super();
    }

    protected Model createModel() {
        return new Model();
    }

    /**
     *        12341234
     *          |
     *         +-+  |
     *         | | +-+
     *         +-+ | |
     *          |  | |
     *          |  | |
     *             +-+
     *              |
     *
     *          ^   ^
     *          |___|___ barCenter
     */
    protected void plotWidget() {
        final Model model = model();
        final GeneralPath path = getPath();
        path.reset();
        final float xRadius = model.width < 2 ? 0 : (model.width - 2) / 2;
        final float yUpper = Math.min(model.yOpen, model.yClose);
        final float yLower = Math.max(model.yOpen, model.yClose);
        if (model.width <= 2) {
            path.moveTo(model.xCenter, model.yHigh);
            path.lineTo(model.xCenter, model.yLow);
        } else {
            path.moveTo(model.xCenter - xRadius, yUpper);
            path.lineTo(model.xCenter + xRadius, yUpper);
            path.lineTo(model.xCenter + xRadius, yLower);
            path.lineTo(model.xCenter - xRadius, yLower);
            path.closePath();
            path.moveTo(model.xCenter, yUpper);
            path.lineTo(model.xCenter, model.yHigh);
            path.moveTo(model.xCenter, yLower);
            path.lineTo(model.xCenter, model.yLow);
            if (model.filled) {
                for (int i = 1; i < model.width - 2; i++) {
                    path.moveTo(model.xCenter - xRadius + i, yUpper);
                    path.lineTo(model.xCenter - xRadius + i, yLower);
                }
            }
        }
    }
}
