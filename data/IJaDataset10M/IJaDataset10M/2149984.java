package de.laures.cewolf;

import java.util.HashMap;
import java.io.Serializable;
import java.awt.Image;
import java.awt.Paint;

/**
 * This class represents a descriptive value object which provides all information
 * needed to render a chart. The description in XML format hold in the JSP page
 * is translated in such an object by the ChartTag and it's nestings.
 * @see de.laures.cewolf.taglib.ChartTag
 * @author  Guido Laures
 */
public interface ChartDescription {

    /**
     * This method returns one of the values of the CharTypes type strings
     * @see ChartTypes#typeNames
     * @return the chart type
     */
    public String getChartType();

    /**
     * This method returns title of the chart.
     * @return the chart title
     */
    public String getTitle();

    public String getXAxisLabel();

    public void setChartType(final String chartType);

    public void setTitle(final String title);

    public void setXAxisLabel(final String xAxisLabel);

    public void setYAxisLabel(final String yAxisLabel);

    public int getHeight();

    public String getYAxisLabel();

    public void setWidth(final int width);

    public int getWidth();

    public void setHeight(final int height);

    public void setBackgroundImage(String imgFile);

    public String getBackgroundImage();

    public void setBackgroundImageAlpha(float alpha);

    public float getBackgroundImageAlpha();

    public void setPaint(Paint p);

    public Paint getPaint();

    public void setDatasetProducer(DatasetProducer dsp);

    public void setAntialias(boolean anti);

    public boolean getAntialias();

    public void setLegend(boolean on);

    public boolean getLegend();

    public DatasetProducer getDatasetProducer();

    /**
     * Returns all params for dataset production which have formally been
     * added as a HashMap containing name/value pairs.
     * @return the param HashMap
     * @see #addDatasetProductionParam
     */
    public HashMap getDatasetProductionParams();

    /**
     * This method adds a parameter to the lists of production parameters.
     * The value must of type <code>Serializable</code> to ensure the 
     * serializablity of this object.
     * @param name the name of the new param, will be the key to retrieve the
     * value from the production params HashMap.
     * @param value the value of the param
     * @see #getDatasetProductionParams
     */
    public void addDatasetProductionParam(String name, Serializable value);
}
