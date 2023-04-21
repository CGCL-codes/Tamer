package com.google.gwt.user.client.ui;

import com.google.gwt.user.client.DOM;

/**
 * A text box that allows multiple lines of text to be entered.
 * 
 * <p>
 * <img class='gallery' src='TextArea.png'/>
 * </p>
 * 
 * <h3>CSS Style Rules</h3>
 * <ul class='css'>
 * <li>.gwt-TextArea { primary style }</li>
 * <li>.gwt-TextArea-readonly { dependent style set when the text area is read-only }</li>
 * </ul>
 * 
 * <p>
 * <h3>Example</h3> {@example com.google.gwt.examples.TextBoxExample}
 * </p>
 */
public class TextArea extends TextBoxBase {

    /**
   * Creates an empty text area.
   */
    public TextArea() {
        super(DOM.createTextArea());
        setStyleName("gwt-TextArea");
    }

    /**
   * Gets the requested width of the text box (this is not an exact value, as
   * not all characters are created equal).
   * 
   * @return the requested width, in characters
   */
    public int getCharacterWidth() {
        return DOM.getElementPropertyInt(getElement(), "cols");
    }

    public int getCursorPos() {
        return getImpl().getTextAreaCursorPos(getElement());
    }

    public int getSelectionLength() {
        return getImpl().getSelectionLength(getElement());
    }

    /**
   * Gets the number of text lines that are visible.
   * 
   * @return the number of visible lines
   */
    public int getVisibleLines() {
        return DOM.getElementPropertyInt(getElement(), "rows");
    }

    /**
   * Sets the requested width of the text box (this is not an exact value, as
   * not all characters are created equal).
   * 
   * @param width the requested width, in characters
   */
    public void setCharacterWidth(int width) {
        DOM.setElementPropertyInt(getElement(), "cols", width);
    }

    /**
   * Sets the number of text lines that are visible.
   * 
   * @param lines the number of visible lines
   */
    public void setVisibleLines(int lines) {
        DOM.setElementPropertyInt(getElement(), "rows", lines);
    }
}
