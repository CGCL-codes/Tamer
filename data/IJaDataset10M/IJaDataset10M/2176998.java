package org.unicolet.axl;

/**
 * Created:
 * User: unicoletti
 * Date: 11:01:37 PM Oct 25, 2005
 */
public class SimpleMarkerSymbol extends Symbol {

    public SimpleMarkerSymbol() {
    }

    private String antialiasing = "false";

    private String color = "0,0,0";

    private String outline;

    private String overlap = "true";

    private String shadow;

    private String type = "circle";

    private String usecentroid = "true";

    private String width = "3";

    public String getAntialiasing() {
        return antialiasing;
    }

    public void setAntialiasing(String antialiasing) {
        this.antialiasing = antialiasing;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getOutline() {
        return outline;
    }

    public void setOutline(String outline) {
        this.outline = outline;
    }

    public String getOverlap() {
        return overlap;
    }

    public void setOverlap(String overlap) {
        this.overlap = overlap;
    }

    public String getShadow() {
        return shadow;
    }

    public void setShadow(String shadow) {
        this.shadow = shadow;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsecentroid() {
        return usecentroid;
    }

    public void setUsecentroid(String usecentroid) {
        this.usecentroid = usecentroid;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }
}
