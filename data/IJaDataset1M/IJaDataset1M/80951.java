package com.jsf.jqplugins.jqplot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;

/**
 * JQuery Plug-ins Bridge
 *
 * @version 0.1
 *
 * @copyright 2010 Amir Sadrinia
 * @license http://www.gnu.org/copyleft/gpl.html (GPL) GENERAL PUBLIC LICENSE
 *
 * @author Amir Sadrinia <amir.sadrinia@gmail.com>
 */
@FacesComponent("com.jsf.jqplugins.bubbleChart")
public class BubbleChart extends JqplotChart {

    @Override
    protected String drawChart(ArrayList<String> data) {
        String script = "";
        script += "[" + data.get(0) + "]";
        this.addToDefaultSeries("renderer", "renderer:$.jqplot.BubbleRenderer");
        this.addToDefaultSeries(ChartConstants.ATTR_POINT_LABELS, "pointLabels:{show:false}");
        this.addToRendererOptions("autoScale", " autoscalePointsFactor: -.15, bubbleAlpha: 0.8,highlightAlpha: 0.7");
        this.addLibrary(ChartConstants.JQPLOT_LIB_BUBBLE);
        return script;
    }

    @Override
    protected ArrayList<String> validate_Convert_Data(Object collection) throws InvalidDataException {
        if (collection == null) {
            logger.severe(" NULL Object has been passed in. ");
            throw new InvalidDataException();
        }
        this.size = 0;
        StringBuilder script = new StringBuilder();
        ArrayList<String> convertedData = new ArrayList<String>();
        try {
            Iterator<Entry<String, Number[]>> data = ((Map<String, Number[]>) collection).entrySet().iterator();
            script.append("[ ");
            while (data.hasNext()) {
                Entry<String, Number[]> content = (Entry<String, Number[]>) data.next();
                Number[] values = content.getValue();
                script.append("[").append(values[0]).append(",").append(values[1]).append(",").append(values[2]).append(",'").append(content.getKey()).append("'],");
                this.size++;
            }
            script.deleteCharAt(script.length() - 1);
            script.append("]");
            convertedData.add(script.toString());
        } catch (ClassCastException exp) {
            logger.severe("Invalid Class type. [only numeric values are valid] [Make sure your passing data structure matches the type of chart you are using]");
            throw new InvalidDataException();
        } catch (ArrayIndexOutOfBoundsException exp) {
            logger.severe("Invalid Array Size. [arrays of three is required]");
            throw new InvalidDataException();
        }
        return convertedData;
    }
}
