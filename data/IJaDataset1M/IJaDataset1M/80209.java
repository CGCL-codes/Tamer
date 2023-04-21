package net.sf.jzeno.echo.databinding;

import nextapp.echo.ImageReference;
import nextapp.echo.Style;
import echopoint.PushButton;

/**
 * @author codehawk
 */
public class ExtendedButton extends PushButton {

    private static final long serialVersionUID = 1L;

    /** 
     * A style constant for the Button width property. 
     * Values of this key must be of type java.lang.Integer.
     */
    public static final String STYLE_BUTTON_WIDTH = "width";

    /** 
     * A style constant for the Button height property. 
     * Values of this key must be of type java.lang.Integer.
     */
    public static final String STYLE_BUTTON_HEIGHT = "height";

    public static final String WIDTH_CHANGED_PROPERTY = "width";

    public static final String HEIGHT_CHANGED_PROPERTY = "height";

    public static final String SCRIPT_CHANGED_PROPERTY = "script";

    private int width = 0;

    private int height = 0;

    private String script;

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        int oldValue = height;
        this.height = height;
        firePropertyChange(HEIGHT_CHANGED_PROPERTY, oldValue, height);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        int oldValue = width;
        this.width = width;
        firePropertyChange(WIDTH_CHANGED_PROPERTY, oldValue, width);
    }

    /**
     * Returns the client side script to execute on the client.
     * 
     * @return String
     */
    public String getScript() {
        return script;
    }

    /**
     * Sets the client side scipt to be executed on the client.  
     * 
     * @param newValue String
     */
    public void setScript(String newValue) {
        String oldValue = script;
        script = newValue;
        firePropertyChange(SCRIPT_CHANGED_PROPERTY, oldValue, newValue);
    }

    public ExtendedButton() {
        super();
    }

    public ExtendedButton(String arg0) {
        super(arg0);
    }

    public ExtendedButton(String arg0, ImageReference arg1) {
        super(arg0, arg1);
    }

    public ExtendedButton(ImageReference arg0) {
        super(arg0);
    }

    public void applyStyle(Style style) {
        super.applyStyle(style);
        if (style.hasAttribute(STYLE_BUTTON_WIDTH)) {
            setWidth(style.getIntegerAttribute(STYLE_BUTTON_WIDTH));
        }
        if (style.hasAttribute(STYLE_BUTTON_HEIGHT)) {
            setHeight(style.getIntegerAttribute(STYLE_BUTTON_HEIGHT));
        }
    }
}
