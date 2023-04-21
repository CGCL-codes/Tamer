package com.sproketsoft.domlet;

/**
 * @author Dan Howard
 */
public class Option {

    private int index;

    private boolean selected;

    private String text;

    private String value;

    /**
     * 
     */
    public Option() {
        index = -1;
        selected = false;
        text = "";
        value = "";
    }

    /**
     * 
     * @param value The value of the option
     * @param text The display text of the option
     */
    public Option(String value, String text) {
        index = -1;
        selected = false;
        this.text = text;
        this.value = value;
    }

    /**
	 * @return
	 */
    public int getIndex() {
        return index;
    }

    /**
     * @return
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * @return
     */
    public String getText() {
        return text;
    }

    /**
     * @return
     */
    public String getValue() {
        return value;
    }

    /**
	 * @param i
	 */
    protected void setIndex(int i) {
        index = i;
    }

    /**
     * @param b
     */
    public void setSelected(boolean b) {
        selected = b;
    }

    /**
     * @param string
     */
    public void setText(String string) {
        text = string;
    }

    /**
     * @param string
     */
    public void setValue(String string) {
        value = string.trim();
    }

    public String toString() {
        StringBuffer c = new StringBuffer("");
        c.append(System.getProperty("line.separator"));
        c.append("<OPTION value=\"" + this.value + "\"");
        if (this.selected) {
            c.append(" SELECTED>");
        } else {
            c.append(">");
        }
        c.append(this.text + "</OPTION>");
        return new String(c);
    }
}
