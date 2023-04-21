package org.opensource.jdom.taggen.html;

import org.opensource.jdom.taggen.Tag;

/**
 * The value of these attributes is JavaScript code. This is commonly a 
 * short one-liner or a function call.
 *
 * @author sergio.valdez
 */
public class EventAttributes extends Tag {

    /**
     * The JavaScript of the value is executed when a pointing-device is used to
     * 'click' on the element.
     */
    private String onclick;

    /**
     * The JavaScript of the value is executed when a pointing-device is used to
     * 'double-click' on the element.
     */
    private String ondblclick;

    /**
     * The JavaScript of the value is executed when the button on a
     * pointing-device is pressed down while the cursor is over the element.
     */
    private String onmousedown;

    /**
     * The JavaScript of the value is executed when the button on a
     * pointing-device is released while the cursor is over the element.
     */
    private String onmouseup;

    /**
     * The JavaScript of the value is executed when the cursor is moved onto
     * an element.
     */
    private String onmouseover;

    /**
     * The JavaScript of the value is executed when the cursor is moved over
     * an element.
     */
    private String onmousemove;

    /**
     * The JavaScript of the value is executed when the cursor is moved off an
     * element.
     */
    private String onmouseout;

    /**
     * The JavaScript of the value is executed when an element is in focus and a
     * key on the keyboard is pressed down and released.
     */
    private String onkeypress;

    /**
     * The JavaScript of the value is executed when an element is in focus and a
     * key on the keyboard is pressed down.
     */
    private String onkeydown;

    /**
     * The JavaScript of the value is executed when an element is in focus and a
     * key on the keyboard is released.
     */
    private String onkeyup;

    /**
     * The JavaScript of the value is executed when a pointing-device is used to
     * 'click' on the element.
     * @return the onclick
     */
    public String getOnclick() {
        return onclick;
    }

    /**
     * The JavaScript of the value is executed when a pointing-device is used to
     * 'click' on the element.
     * @param onclick the onclick to set
     */
    public void setOnclick(String onclick) {
        this.onclick = onclick;
    }

    /**
     * The JavaScript of the value is executed when a pointing-device is used to
     * 'double-click' on the element.
     * @return the ondblclick
     */
    public String getOndblclick() {
        return ondblclick;
    }

    /**
     * The JavaScript of the value is executed when a pointing-device is used to
     * 'double-click' on the element.
     * @param ondblclick the ondblclick to set
     */
    public void setOndblclick(String ondblclick) {
        this.ondblclick = ondblclick;
    }

    /**
     * The JavaScript of the value is executed when the button on a
     * pointing-device is pressed down while the cursor is over the element.
     * @return the onmousedown
     */
    public String getOnmousedown() {
        return onmousedown;
    }

    /**
     * The JavaScript of the value is executed when the button on a
     * pointing-device is pressed down while the cursor is over the element.
     * @param onmousedown the onmousedown to set
     */
    public void setOnmousedown(String onmousedown) {
        this.onmousedown = onmousedown;
    }

    /**
     * The JavaScript of the value is executed when the button on a
     * pointing-device is released while the cursor is over the element.
     * @return the onmouseup
     */
    public String getOnmouseup() {
        return onmouseup;
    }

    /**
     * The JavaScript of the value is executed when the button on a
     * pointing-device is released while the cursor is over the element.
     * @param onmouseup the onmouseup to set
     */
    public void setOnmouseup(String onmouseup) {
        this.onmouseup = onmouseup;
    }

    /**
     * The JavaScript of the value is executed when the cursor is moved onto
     * an element.
     * @return the onmouseover
     */
    public String getOnmouseover() {
        return onmouseover;
    }

    /**
     * The JavaScript of the value is executed when the cursor is moved onto
     * an element.
     * @param onmouseover the onmouseover to set
     */
    public void setOnmouseover(String onmouseover) {
        this.onmouseover = onmouseover;
    }

    /**
     * The JavaScript of the value is executed when the cursor is moved over
     * an element.
     * @return the onmousemove
     */
    public String getOnmousemove() {
        return onmousemove;
    }

    /**
     * The JavaScript of the value is executed when the cursor is moved over
     * an element.
     * @param onmousemove the onmousemove to set
     */
    public void setOnmousemove(String onmousemove) {
        this.onmousemove = onmousemove;
    }

    /**
     * The JavaScript of the value is executed when the cursor is moved off an
     * element.
     * @return the onmouseout
     */
    public String getOnmouseout() {
        return onmouseout;
    }

    /**
     * The JavaScript of the value is executed when the cursor is moved off an
     * element.
     * @param onmouseout the onmouseout to set
     */
    public void setOnmouseout(String onmouseout) {
        this.onmouseout = onmouseout;
    }

    /**
     * The JavaScript of the value is executed when an element is in focus and a
     * key on the keyboard is pressed down and released.
     * @return the onkeypress
     */
    public String getOnkeypress() {
        return onkeypress;
    }

    /**
     * The JavaScript of the value is executed when an element is in focus and a
     * key on the keyboard is pressed down and released.
     * @param onkeypress the onkeypress to set
     */
    public void setOnkeypress(String onkeypress) {
        this.onkeypress = onkeypress;
    }

    /**
     * The JavaScript of the value is executed when an element is in focus and a
     * key on the keyboard is pressed down.
     * @return the onkeydown
     */
    public String getOnkeydown() {
        return onkeydown;
    }

    /**
     * The JavaScript of the value is executed when an element is in focus and a
     * key on the keyboard is pressed down.
     * @param onkeydown the onkeydown to set
     */
    public void setOnkeydown(String onkeydown) {
        this.onkeydown = onkeydown;
    }

    /**
     * The JavaScript of the value is executed when an element is in focus and a
     * key on the keyboard is released.
     * @return the onkeyup
     */
    public String getOnkeyup() {
        return onkeyup;
    }

    /**
     * The JavaScript of the value is executed when an element is in focus and a
     * key on the keyboard is released.
     * @param onkeyup the onkeyup to set
     */
    public void setOnkeyup(String onkeyup) {
        this.onkeyup = onkeyup;
    }
}
