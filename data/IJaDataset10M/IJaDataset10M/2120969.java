package org.jCharts.test;

import org.jCharts.imageMap.ImageMap;

/*********************************************************************************************
 * Interface class for Chart Objects that can be displayed in a jCharts test.
 *
 **********************************************************************************************/
public interface HTMLChartTestable {

    /*****************************************************************************************
	 *
	 * @param htmlGenerator
	 * @param imageFileName the name of the output test image
	 * @param imageMap if this is NULL we are not creating image map data in html
	 ******************************************************************************************/
    public void toHTML(HTMLGenerator htmlGenerator, String imageFileName, ImageMap imageMap);
}
