package com.inetmon.jn.statistic.general.views;

import java.awt.Color;

/**
 * Static class which contains constant for the statistics per second views
 * 
 * @author Arnaud MARTIN
 */
public interface IGeneralConstant {

    /**
     * Color for the current values and the average values in the graphs
     */
    public Color COLORS[] = { new Color(0, 0, 255), new Color(255, 0, 0) };

    /**
     * The label to display when no data are recorded
     */
    public String LABEL_NO_DATA = "NO DATA";
}
