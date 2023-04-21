package org.jgistools.data.shapefile;

import java.awt.Color;
import org.geotools.styling.Style;

/**
 * The purpose of this class is to group map
 * data with information about the rendering.
 * @author Teodor Baciu
 *
 */
public class ShapefileInfo {

    /**
	 * Holds the path to the shapefile.
	 */
    protected String filePath;

    /**
	 * Contains the syle to be used for rendering
	 * the features contained in the resource designated
	 * by "fileURL"
	 */
    protected Style renderingStyle = null;

    /**
	 * This is the color asociated with the shapefile. Might
	 * not be used by the rendering style.
	 */
    protected Color featureColor = null;

    /**
	 * Contstructor.
	 * @param path the complete path to the shapefile
	 * @param style the rendering style to be used for
	 * displaying the map data
	 */
    public ShapefileInfo(String path, Style style, Color featureColor) {
        this.filePath = path;
        renderingStyle = style;
        this.featureColor = featureColor;
    }

    /**
	 * Creates a 
	  * @param path the complete path to the shapefile
	 * @param featureColor the color to use to draw the features from the shapefile
	 */
    public ShapefileInfo(String path, Color featureColor) {
        this(path, null, featureColor);
    }

    /**
	 * Creates a new shapefile information with the specified
	 * rendering style.
	 * @param path the complete path to the shapefile
	 * @param style
	 */
    public ShapefileInfo(String path, Style style) {
        this(path, style, Color.BLUE);
    }

    /**
	 * Creates a shapefile info object with
	 * no styling or color information.
	 * @param path the complete path to the shapefile
	 */
    public ShapefileInfo(String path) {
        this(path, null, Color.BLUE);
    }

    public String getFilePath() {
        return filePath;
    }

    public Style getRenderingStyle() {
        return renderingStyle;
    }

    public Color getFeatureColor() {
        return featureColor;
    }

    public void setFeatureColor(Color featureColor) {
        this.featureColor = featureColor;
    }
}
