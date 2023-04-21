package com.cosylab.vdct.vdb;

/**
 * @author ssah
 *
 */
public class VisibilityProperty extends NameValueInfoProperty {

    boolean visible = true;

    /**
	 * @param visible
	 */
    public VisibilityProperty(boolean visible) {
        super("", "");
    }

    /**
	 * @param visible the visible to set
	 */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getVisibility() {
        return visible ? NON_DEFAULT_VISIBLE : NEVER_VISIBLE;
    }
}
