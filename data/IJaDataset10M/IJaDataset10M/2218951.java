package com.rapidminer.gui.new_plotter.engine.jfreechart.renderer;

import java.awt.Color;
import java.awt.Paint;
import java.awt.Shape;
import org.jfree.chart.renderer.xy.ClusteredXYBarRenderer;
import com.rapidminer.gui.new_plotter.engine.jfreechart.RenderFormatDelegate;
import com.rapidminer.gui.new_plotter.utility.DataStructureUtils;

/**
 * @author Marius Helf
 */
public class FormattedClusteredXYBarRenderer extends ClusteredXYBarRenderer implements FormattedRenderer {

    private static final long serialVersionUID = 1L;

    private RenderFormatDelegate formatDelegate = new RenderFormatDelegate();

    public FormattedClusteredXYBarRenderer() {
        super();
    }

    public FormattedClusteredXYBarRenderer(double margin, boolean centerBarAtStartValue) {
        super(margin, centerBarAtStartValue);
    }

    @Override
    public RenderFormatDelegate getFormatDelegate() {
        return formatDelegate;
    }

    @Override
    public Paint getItemPaint(int seriesIdx, int valueIdx) {
        Paint paintFromDelegate = getFormatDelegate().getItemPaint(seriesIdx, valueIdx);
        if (paintFromDelegate == null) {
            return super.getItemPaint(seriesIdx, valueIdx);
        } else {
            return paintFromDelegate;
        }
    }

    @Override
    public Shape getItemShape(int seriesIdx, int valueIdx) {
        Shape shapeFromDelegate = getFormatDelegate().getItemShape(seriesIdx, valueIdx);
        if (shapeFromDelegate == null) {
            return super.getItemShape(seriesIdx, valueIdx);
        } else {
            return shapeFromDelegate;
        }
    }

    @Override
    public Paint getItemOutlinePaint(int seriesIdx, int valueIdx) {
        if (getFormatDelegate().isItemSelected(seriesIdx, valueIdx)) {
            return super.getItemOutlinePaint(seriesIdx, valueIdx);
        } else {
            return DataStructureUtils.setColorAlpha(Color.LIGHT_GRAY, 20);
        }
    }
}
