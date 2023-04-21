package edu.biik.visualizations.sourcezoomin.graphics;

import java.awt.Font;
import java.awt.Point;
import edu.biik.framework.BiikGObject;
import edu.biik.framework.ColorPalette;
import edu.biik.framework.VisualUnit;
import no.geosoft.cc.geometry.Geometry;
import no.geosoft.cc.graphics.GPosition;
import no.geosoft.cc.graphics.GSegment;
import no.geosoft.cc.graphics.GStyle;
import no.geosoft.cc.graphics.GText;

public class SourceIPNode extends BiikGObject {

    private String sourceIP = null;

    private GSegment gSegment = null;

    private GText gText = null;

    private int xCorner = 0;

    private int yCorner = 0;

    private int width = 0;

    private int height = 0;

    private GStyle normalStyle = null;

    private GStyle highlightedStyle = null;

    /**
	 * NOTE: CONSTRUCT THE OBJECT SPECIFYING ALL NEEDED PARAMETERS TO DRAW THE OBJECT
	 * check the draw Method to know what parameters are required to draw the object
	 * 
	 * @param visualUnit
	 * @param sourceIP
	 * @param xCorner
	 * @param yCorner
	 * @param width
	 * @param height
	 */
    public SourceIPNode(VisualUnit visualUnit, String sourceIP, int xCorner, int yCorner, int width, int height) {
        super(visualUnit);
        this.sourceIP = sourceIP;
        this.xCorner = xCorner;
        this.yCorner = yCorner;
        this.width = width;
        this.height = height;
        gSegment = new GSegment();
        normalStyle = new GStyle();
        normalStyle.setForegroundColor(ColorPalette.SOURCE_IP_OUTLINE);
        normalStyle.setBackgroundColor(ColorPalette.SOURCE_IP);
        normalStyle.setLineWidth(1);
        setStyle(normalStyle);
        highlightedStyle = new GStyle();
        highlightedStyle.setBackgroundColor(ColorPalette.SOURCE_IP_ACTIVE);
        gText = new GText(sourceIP, GPosition.MIDDLE);
        GStyle textStyle = new GStyle();
        textStyle.setForegroundColor(ColorPalette.SOURCE_IP_TEXT);
        textStyle.setBackgroundColor(null);
        textStyle.setFont(new Font("Arial", Font.PLAIN, 9));
        gText.setStyle(textStyle);
        gSegment.setText(gText);
        addSegment(gSegment);
    }

    @Override
    public void setHighlighted(boolean highlighted) {
        if (highlighted) {
            gSegment.setStyle(highlightedStyle);
        } else {
            gSegment.setStyle(normalStyle);
        }
    }

    public Point getLocation() {
        return new Point(this.xCorner + this.width, this.yCorner + this.height / 2);
    }

    @Override
    public void draw() {
        gSegment.setGeometry(Geometry.createRectangle(this.xCorner, this.yCorner, this.width, this.height));
    }

    public String getSourceIP() {
        return sourceIP;
    }
}
