package nl.javel.gisbeans.map;

import java.awt.Color;
import java.awt.Dimension;

/**
 * This class defines the image as defined in the map
 * 
 * @author <a href="mailto:paul.jacobs@javel.nl">Paul Jacobs </a>
 * @since JDK 1.0
 * @version 1.0
 */
public class Image implements ImageInterface {

    private java.awt.Color backgroundColor = new java.awt.Color(255, 255, 255, 255);

    private LegendInterface legend;

    private ScalebarInterface scalebar;

    private java.awt.Dimension size = new java.awt.Dimension(500, 500);

    /**
	 * constructs a new Image
	 */
    public Image() {
        super();
    }

    /**
	 * @see nl.javel.gisbeans.map.ImageInterface#getBackgroundColor()
	 */
    public java.awt.Color getBackgroundColor() {
        return this.backgroundColor;
    }

    /**
	 * @see nl.javel.gisbeans.map.ImageInterface#getLegend()
	 */
    public LegendInterface getLegend() {
        return this.legend;
    }

    /**
	 * @see nl.javel.gisbeans.map.ImageInterface#getScalebar()
	 */
    public ScalebarInterface getScalebar() {
        return this.scalebar;
    }

    /**
	 * @see nl.javel.gisbeans.map.ImageInterface#getSize()
	 */
    public Dimension getSize() {
        return this.size;
    }

    /**
	 * @see nl.javel.gisbeans.map.ImageInterface#setBackgroundColor(java.awt.Color)
	 */
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
	 * @see nl.javel.gisbeans.map.ImageInterface#setLegend(nl.javel.gisbeans.map.LegendInterface)
	 */
    public void setLegend(LegendInterface legend) {
        this.legend = legend;
    }

    /**
	 * @see nl.javel.gisbeans.map.ImageInterface#setScalebar(nl.javel.gisbeans.map.ScalebarInterface)
	 */
    public void setScalebar(ScalebarInterface scalebar) {
        this.scalebar = scalebar;
    }

    /**
	 * @see nl.javel.gisbeans.map.ImageInterface#setSize(java.awt.Dimension)
	 */
    public void setSize(Dimension size) {
        this.size = size;
    }
}
