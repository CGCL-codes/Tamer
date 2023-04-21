package org.achartengine.chartdemo.demo.chart;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DialRenderer;
import org.achartengine.renderer.DialRenderer.Type;
import org.achartengine.renderer.SimpleSeriesRenderer;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

/**
 * Budget demo pie chart.
 */
public class WeightDialChart extends AbstractDemoChart {

    /**
	 * Returns the chart name.
	 * 
	 * @return the chart name
	 */
    public String getName() {
        return "Weight chart";
    }

    /**
	 * Returns the chart description.
	 * 
	 * @return the chart description
	 */
    public String getDesc() {
        return "The weight indicator (dial chart)";
    }

    /**
	 * Executes the chart demo.
	 * 
	 * @param context
	 *            the context
	 * @return the built intent
	 */
    public Intent execute(Context context) {
        CategorySeries category = new CategorySeries("Weight indic");
        category.add("Current", 75);
        category.add("Minimum", 65);
        category.add("Maximum", 90);
        DialRenderer renderer = new DialRenderer();
        renderer.setChartTitleTextSize(20);
        renderer.setLabelsTextSize(15);
        renderer.setLegendTextSize(15);
        renderer.setMargins(new int[] { 20, 30, 15, 0 });
        SimpleSeriesRenderer r = new SimpleSeriesRenderer();
        r.setColor(Color.BLUE);
        renderer.addSeriesRenderer(r);
        r = new SimpleSeriesRenderer();
        r.setColor(Color.rgb(0, 150, 0));
        renderer.addSeriesRenderer(r);
        r = new SimpleSeriesRenderer();
        r.setColor(Color.GREEN);
        renderer.addSeriesRenderer(r);
        renderer.setLabelsTextSize(10);
        renderer.setLabelsColor(Color.WHITE);
        renderer.setShowLabels(true);
        renderer.setVisualTypes(new DialRenderer.Type[] { Type.ARROW, Type.NEEDLE, Type.NEEDLE });
        renderer.setMinValue(0);
        renderer.setMaxValue(150);
        return ChartFactory.getDialChartIntent(context, category, renderer, "Weight indicator");
    }
}
