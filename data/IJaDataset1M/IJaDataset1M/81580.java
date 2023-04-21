package org.jxul;

/**
 * @author Will Etson
 */
public interface XulListCell extends XulElement {

    String ELEMENT = "listcell";

    String ATTR_CROP = "crop";

    String ATTR_DISABLED = "disabled";

    String ATTR_IMAGE = "image";

    String ATTR_TYPE = "type";

    /**
	 * If the label of the listcell is too small to fit in its given space, the
	 * text will be cropped on the side specified by the crop attribute. An
	 * ellipsis will be used in place of the cropped text. If the box direction
	 * is right to left (rtl), the cropping is reversed.
	 * 
	 * <ul>
	 * <li>start: The text will be cropped on its left side.</li>
	 * <li>end: The text will be cropped on its right side.</li>
	 * <li>left: (Deprecated) The text will be cropped on its left side.</li>
	 * <li>right: (Deprecated) The text will be cropped on its right side.
	 * </li>
	 * <li>center: The text will be cropped on both sides.</li>
	 * <li>none: The text will be not be cropped using an ellipsis. However,
	 * the text will simply be cut off if it is too large. The side depends on
	 * the CSS text alignment.</li>
	 * </ul>
	 * @return
	 */
    String getCrop();

    void setCrop(String crop);

    /**
	 * Indicates whether the listcell is disabled or not. If this attribute is
	 * set to true, the listcell is disabled. This is usually drawn with the
	 * text in grey. If the listcell is disabled, it does not respond to user
	 * actions. The element cannot be focused and the command event will not
	 * fire. The element will still respond to mouse events. To enable the
	 * listcell, leave the attribute out entirely as opposed to setting the
	 * value to false.
	 * 
	 * @return
	 */
    boolean isDisabled();

    void setDisabled(boolean disabled);

    /**
	 * The URL of the image to appear on the listcell. If this is attribute is
	 * left out, no image appears. The position of the image is determined by
	 * the dir and orient attributes. You must use the class 'listcell-iconic'
	 * to have an image appear.
	 * 
	 * @return
	 */
    String getImage();

    void setImage(String image);

    /**
	 * The label that will appear on the listcell. If this is left out, no text
	 * appears.
	 */
    String getLabel();

    void setLabel(String label);

    /**
	 * You can make a cell in a listbox a checkbox by setting this attribute to
	 * the value 'checkbox'.
	 * 
	 * @return
	 */
    String getType();

    void setType(String type);
}
