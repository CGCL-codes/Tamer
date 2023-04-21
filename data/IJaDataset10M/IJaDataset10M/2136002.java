package org.aiotrade.charting.chart.handledchart;

import java.awt.geom.GeneralPath;
import java.util.List;
import org.aiotrade.charting.chart.AbstractChart;
import org.aiotrade.charting.chart.handledchart.ChannelChart.Model;
import org.aiotrade.charting.chart.util.ValuePoint;
import org.aiotrade.charting.widget.PathWidget;
import org.aiotrade.charting.widget.WidgetModel;

/**
 *
 * @author Steve Hu
 */
public class ChannelChart extends AbstractChart<Model> {

    public static final class Model implements WidgetModel {

        List<ValuePoint> points;

        public void set(List<ValuePoint> points) {
            this.points = points;
        }
    }

    protected Model createModel() {
        return new Model();
    }

    protected void plotChart() {
        final Model model = model();
        final int nPoints = model.points.size();
        final float x1 = xb(bt(model.points.get(0).t));
        final float x2 = xb(bt(model.points.get(1).t));
        if (model.points.get(0).e < model.points.get(1).t) {
            model.points.get(0).e = model.points.get(1).t;
        }
        final float e1 = xb(bt(model.points.get(0).e));
        final float y1 = yv(model.points.get(0).t, model.points.get(0).p, 0);
        final float y2 = yv(model.points.get(1).t, model.points.get(0).p, 0);
        final float dx = x2 - x1;
        final float dy = y2 - y1;
        final float k = dx == 0 ? 1 : dy / dx;
        final PathWidget pathWidget = addChild(new PathWidget());
        pathWidget.setForeground(getForeground());
        final GeneralPath path = pathWidget.getPath();
        plotChannel(x1, y1, k, e1, path);
        for (int i = 2; i < nPoints - 1; i++) {
            final float x = xb(bt(model.points.get(i).t));
            if (model.points.get(i).e < model.points.get(0).e) {
                model.points.get(i).e = model.points.get(0).e;
            }
            final float e = xb(bt(model.points.get(i).e));
            float y;
            if (model.points.get(0).p == ValuePoint.BAR_POSITION_HIGH) {
                y = yv(model.points.get(i).t, ValuePoint.BAR_POSITION_LOW, 0);
            } else {
                y = yv(model.points.get(i).t, ValuePoint.BAR_POSITION_HIGH, 0);
            }
            plotChannel(x, y, k, e, path);
        }
    }
}
