package com.lake.pim.api;

import java.awt.Color;

public class ColourSettings {

    private String[] label = { "current", "family", "sports", "hobby", "health", "work", "business", "travel" };

    private Color[] colours = { Color.blue, Color.blue, Color.green, Color.yellow, Color.red, Color.pink, Color.orange, Color.cyan };

    public ColourSettings() {
    }

    public ColourSettings(String[] label, Color[] colours) {
        this.label = label;
        this.colours = colours;
    }

    /**
     *  Get Labels 
     *
     * @return    
     */
    public String[] getLabels() {
        return label;
    }

    /**
     *  Set Labels
     *
     */
    public void setLabels(String[] labels) {
        this.label = labels;
        return;
    }

    /**
     *  Get Colours 
     *
     * @return    
     */
    public Color[] getColours() {
        return colours;
    }

    /**
     *  Set Colours
     *
     */
    public void setColours(Color[] colours) {
        this.colours = colours;
        return;
    }
}
