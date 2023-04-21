package net.sf.doolin.gui.swing;

import javax.swing.Icon;

/**
 * Association of a text and an icon
 * 
 * @author Damien Coraboeuf
 */
public class LabelInfo {

    private String text;

    private Icon iconObject;

    /**
	 * Constructor
	 * 
	 * @param text
	 *            Text
	 * @param iconObject
	 *            Icon
	 */
    public LabelInfo(String text, Icon iconObject) {
        this.text = text;
        this.iconObject = iconObject;
    }

    /**
	 * Constructor with text only
	 * 
	 * @param text
	 *            Text
	 */
    public LabelInfo(String text) {
        this(text, null);
    }

    /**
	 * Constructor with icon only
	 * 
	 * @param iconObject
	 *            Icon
	 */
    public LabelInfo(Icon iconObject) {
        this(null, iconObject);
    }

    /**
	 * Instantiates a new label info.
	 */
    public LabelInfo() {
    }

    /**
	 * Gets the icon.
	 * 
	 * @return the icon
	 */
    public Icon getIcon() {
        return this.iconObject;
    }

    /**
	 * Sets the icon.
	 * 
	 * @param iconObject
	 *            the new icon
	 */
    public void setIcon(Icon iconObject) {
        this.iconObject = iconObject;
    }

    /**
	 * Gets the text.
	 * 
	 * @return the text
	 */
    public String getText() {
        return this.text;
    }

    /**
	 * Sets the text.
	 * 
	 * @param text
	 *            the new text
	 */
    public void setText(String text) {
        this.text = text;
    }
}
