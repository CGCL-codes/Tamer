package org.zkoss.zkex.zul.api;

/**
 * A fisheye item.
 * 
 * <p>
 * Default {@link org.zkoss.zkex.zul.Fisheye#getZclass}: z-fisheye.
 * 
 * @author jumperchen
 * @since 3.5.0
 */
public interface Fisheye extends org.zkoss.zul.impl.api.XulElement {

    /**
	 * Returns the label (never null).
	 * <p>
	 * Default: "".
	 */
    public String getLabel();

    /**
	 * Sets the label.
	 */
    public void setLabel(String label);

    /**
	 * Returns the image URI.
	 * <p>
	 * Default: null.
	 */
    public String getImage();

    /**
	 * Sets the image URI.
	 */
    public void setImage(String image);
}
