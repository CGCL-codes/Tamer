package com.skruk.elvis.admin.manage.marc21;

import com.skruk.elvis.admin.plugin.Marc21Plugin;

/**
 * @author     skruk
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 * @created    20 lipiec 2004
 */
public class Marc21Controlfield implements Marc21Field {

    /**  Description of the Field */
    int start = 0;

    /**  Description of the Field */
    int length = 0;

    /**  Description of the Field */
    String text = null;

    /**  Description of the Field */
    int tag;

    /**
	 * @return    Returns the length.
	 */
    public int getLength() {
        return length;
    }

    /**
	 * @param  length  The length to set.
	 */
    public void setLength(int length) {
        this.length = length;
    }

    /**
	 * @return    Returns the start.
	 */
    public int getStart() {
        return start;
    }

    /**
	 * @param  start  The start to set.
	 */
    public void setStart(int start) {
        this.start = start;
    }

    /**
	 * @return    Returns the text.
	 */
    public String getText() {
        return text;
    }

    /**
	 * @param  text  The text to set.
	 */
    public void setText(String text) {
        this.text = text;
    }

    /**
	 * @return    Returns the tag.
	 */
    public int getTag() {
        return tag;
    }

    /**
	 * @param  tag  The tag to set.
	 */
    public void setTag(int tag) {
        this.tag = tag;
    }

    /**
	 *  Description of the Method
	 *
	 * @return    Description of the Return Value
	 */
    public String createXml() {
        return Marc21Plugin.getInstance().getFormater().getText("marcxml_controlfield", new Object[] { String.valueOf(getTag()), Marc21Description.CONVERT_ENTITY(getText()) });
    }

    /**
	 *  Description of the Method
	 *
	 * @return    Description of the Return Value
	 */
    public String createMarc() {
        System.out.println("|>" + getTag());
        return Marc21Plugin.getInstance().getFormater().getText("marc21_controlfield", new Object[] { new Integer(getTag()), getText() });
    }
}
