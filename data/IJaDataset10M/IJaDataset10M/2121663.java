package org.brainypdm.modules.customgraph.classdef;

/***
 * 
 * graph label
 * 
 * @author <a href="mailto:nico@brainypdm.org">Nico Bagari</a>
 *
 */
public class GraphLabel {

    /**
	 * the xAxis Title
	 * 
	 */
    String xAxisTitle;

    /**
	 * the y axis title
	 */
    String yAxisTitle;

    /**
	 * the graph title
	 */
    String title;

    public GraphLabel(String axisTitle, String axisTitle2, String title) {
        super();
        xAxisTitle = axisTitle;
        yAxisTitle = axisTitle2;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getXAxisTitle() {
        return xAxisTitle;
    }

    public void setXAxisTitle(String axisTitle) {
        xAxisTitle = axisTitle;
    }

    public String getYAxisTitle() {
        return yAxisTitle;
    }

    public void setYAxisTitle(String axisTitle) {
        yAxisTitle = axisTitle;
    }
}
