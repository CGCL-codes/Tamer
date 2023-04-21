package de.laures.cewolf.example;

import java.io.Serializable;
import java.util.Map;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.title.TextTitle;
import de.laures.cewolf.ChartPostProcessor;

/** 
 * An example chart postprocessor which adds an extra sub title to a chart.
 * @author  Guido Laures 
 */
public class ExtraTitleEnhancer implements ChartPostProcessor, Serializable {

    /** Creates a new instance of ExtraTitleEnhancer */
    public ExtraTitleEnhancer() {
    }

    public void processChart(Object chart, Map params) {
        String title = (String) params.get("title");
        if (title != null && title.trim().length() > 0) {
            ((JFreeChart) chart).addSubtitle(new TextTitle(title));
        }
    }
}
