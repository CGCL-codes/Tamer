package org.achartengine.chartdemo.demo.chart;

import java.util.ArrayList;
import java.util.List;
import org.achartengine.ChartFactory;
import org.achartengine.renderer.DefaultRenderer;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

/**
 * Budget demo pie chart.
 */
public class BudgetDoughnutChart extends AbstractDemoChart {

    /**
	 * Returns the chart name.
	 * 
	 * @return the chart name
	 */
    public String getName() {
        return "Budget chart for several years";
    }

    /**
	 * Returns the chart description.
	 * 
	 * @return the chart description
	 */
    public String getDesc() {
        return "The budget per project for several years (doughnut chart)";
    }

    /**
	 * Executes the chart demo.
	 * 
	 * @param context
	 *            the context
	 * @return the built intent
	 */
    public Intent execute(Context context) {
        List<double[]> values = new ArrayList<double[]>();
        values.add(new double[] { 12, 14, 11, 10, 19 });
        values.add(new double[] { 10, 9, 14, 20, 11 });
        List<String[]> titles = new ArrayList<String[]>();
        titles.add(new String[] { "P1", "P2", "P3", "P4", "P5" });
        titles.add(new String[] { "Project1", "Project2", "Project3", "Project4", "Project5" });
        int[] colors = new int[] { Color.BLUE, Color.GREEN, Color.MAGENTA, Color.YELLOW, Color.CYAN };
        DefaultRenderer renderer = buildCategoryRenderer(colors);
        renderer.setApplyBackgroundColor(true);
        renderer.setBackgroundColor(Color.rgb(222, 222, 200));
        renderer.setLabelsColor(Color.GRAY);
        return ChartFactory.getDoughnutChartIntent(context, buildMultipleCategoryDataset("Project budget", titles, values), renderer, "Doughnut chart demo");
    }
}
