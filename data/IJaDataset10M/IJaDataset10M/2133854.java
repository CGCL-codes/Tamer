package org.jabusuite.webclient.controls.list;

/**
 * Represents an entry for the <code>JbsStringListBox</code>.
 * @author hilwers
 *
 */
public class JbsStringListBoxEntry {

    private int tag;

    private String value;

    /**
	 * @return the tag
	 */
    public int getTag() {
        return tag;
    }

    /**
	 * @param tag the tag to set
	 */
    public void setTag(int tag) {
        this.tag = tag;
    }

    /**
	 * @return the value
	 */
    public String getValue() {
        return value;
    }

    /**
	 * @param value the value to set
	 */
    public void setValue(String value) {
        this.value = value;
    }
}
