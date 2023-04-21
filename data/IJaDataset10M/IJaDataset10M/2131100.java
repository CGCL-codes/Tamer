package com.worldware.html;

import java.util.*;

/** Represents a run of text in HTML
 **/
public class HTMLText extends HTMLNode {

    /** Text that makes up this item
	 */
    String m_text = null;

    public HTMLText(String text) {
        m_text = text;
    }

    public void setAttr(String key, String value) {
        throw new RuntimeException("Error: You can't set attributes on text");
    }

    public void addChild(HTMLNode node) {
        throw new RuntimeException("Error: You can't add children to a text node");
    }

    public void render(StringBuffer b) {
        b.append(m_text);
    }

    public void render(StringBuffer b, StringBuffer indent) {
        b.append(m_text);
    }
}
