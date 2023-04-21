package de.robertkosten.java.roleplaying.autorealm;

/**
 * Represents all graphical objects in a map.
 * 
 * @author Robert Kosten
 *
 */
public abstract class MapObject {

    private Color color = new Color((long) 0, (long) 0, (long) 0);

    private long overlay = (long) 0;

    private double boundLeft = 0.0;

    private double boundTop = 0.0;

    private double boundRight = 0.0;

    private double boundBottom = 0.0;

    /**
	 * @return Returns the boundBottom.
	 */
    public double getBoundBottom() {
        return boundBottom;
    }

    /**
	 * @param boundBottom The boundBottom to set.
	 */
    public void setBoundBottom(double boundBottom) {
        this.boundBottom = boundBottom;
    }

    /**
	 * @return Returns the boundLeft.
	 */
    public double getBoundLeft() {
        return boundLeft;
    }

    /**
	 * @param boundLeft The boundLeft to set.
	 */
    public void setBoundLeft(double boundLeft) {
        this.boundLeft = boundLeft;
    }

    /**
	 * @return Returns the boundRight.
	 */
    public double getBoundRight() {
        return boundRight;
    }

    /**
	 * @param boundRight The boundRight to set.
	 */
    public void setBoundRight(double boundRight) {
        this.boundRight = boundRight;
    }

    /**
	 * @return Returns the boundTop.
	 */
    public double getBoundTop() {
        return boundTop;
    }

    /**
	 * @param boundTop The boundTop to set.
	 */
    public void setBoundTop(double boundTop) {
        this.boundTop = boundTop;
    }

    /**
	 * @return Returns the color.
	 */
    public Color getColor() {
        return color;
    }

    /**
	 * @param color The color to set.
	 */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
	 * @return Returns the overlay.
	 */
    public long getOverlay() {
        return overlay;
    }

    /**
	 * @param overlay The overlay to set.
	 */
    public void setOverlay(long overlay) {
        this.overlay = overlay;
    }

    @Override
    public String toString() {
        String result = "";
        result += "[Object]\n";
        result += "Object.boundBottom: " + this.boundBottom + "\n";
        result += "Object.boundLeft :" + this.boundLeft + "\n";
        result += "Object.boundRight :" + this.boundRight + "\n";
        result += "Object.boundTop :" + this.boundTop + "\n";
        result += "Object.color :" + this.color + "\n";
        result += "Object.overlay :" + this.overlay + "\n";
        return result;
    }
}
