package de.xirp.settings;

import org.eclipse.swt.graphics.RGB;

/**
 * A preferences value type for RGB colors.
 * 
 * @author Rabea Gransberger
 */
public class RGBValue extends FreeValue {

    /**
	 * Constructs a new preferences value which allows the user to
	 * input a free text RGB color.
	 * 
	 * @param key
	 *            the key used for saving the typed value
	 * @param value
	 *            the initial value
	 * @param defaultValue
	 *            The default value, might be <code>null</code>
	 */
    protected RGBValue(String key, String value, String defaultValue) {
        super(key, value, defaultValue);
    }

    /**
	 * Gets an RGB value parsed from the given string.
	 * 
	 * @param value
	 *            the value string of the form <code>r:g:b</code>
	 * @return the RGB value
	 */
    public RGB getRGB(String value) {
        String[] split = value.split(":");
        RGB rgb = new RGB(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
        return rgb;
    }

    /**
	 * Sets current value to this RGB value.
	 * 
	 * @param rgb
	 *            the RGB value to set
	 * @param fromUI
	 *            <code>true</code> if it's called from the UI
	 *            displaying this value
	 */
    public void setRGB(RGB rgb, boolean fromUI) {
        String newValue = RGBValue.getString(rgb);
        setValue(newValue, fromUI);
    }

    /**
	 * Sets current value to this RGB value.
	 * 
	 * @param rgb
	 *            the RGB value to set
	 */
    public void setSavedRGB(RGB rgb) {
        String newValue = RGBValue.getString(rgb);
        super.setSavedValue(newValue);
    }

    /**
	 * Gets the RGB value of {@link #getDisplayValue()}.
	 * 
	 * @return the current RGB
	 */
    public RGB getCurrentRGB() {
        return getRGB(getDisplayValue());
    }

    /**
	 * Gets the RGB value of {@link #getSaveValue()}.
	 * 
	 * @return the saved RGB
	 */
    public RGB getSavedRGB() {
        return getRGB(getSaveValue());
    }

    /**
	 * Gets the RGB value of {@link #getDefaultValue()}.
	 * 
	 * @return the default RGB
	 */
    public RGB getDefaultRGB() {
        return getRGB(getDefaultValue());
    }

    /**
	 * Gets a string from the RGB value.
	 * 
	 * @param rgb
	 *            the RGB value
	 * @return the string of the form <code>r:g:b</code>
	 */
    public static String getString(RGB rgb) {
        return rgb.red + ":" + rgb.green + ":" + rgb.blue;
    }
}
