package org.sucri.floxs.ext;

import org.sucri.floxs.html.CSS;

/**
 * Created by IntelliJ IDEA.
 * User: Wen Yu
 * Date: Aug 19, 2007
 * Time: 4:36:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class Label {

    public Label() {
        super();
    }

    public Label(String s) {
        mLabel = s;
    }

    public Label(String s, int w) {
        mLabel = s;
        mWidth = w;
    }

    public Label(String s, int w, CSS.Align a) {
        mLabel = s;
        mWidth = w;
        mAlign = a;
    }

    private String mLabel;

    private int mWidth;

    private CSS.Align mAlign = CSS.Align.left;

    public String getLabel() {
        return mLabel;
    }

    public int getWidth() {
        return mWidth;
    }

    public CSS.Align getAlign() {
        return mAlign;
    }

    public void setLabel(String s) {
        mLabel = s;
    }

    public void setWidth(int s) {
        mWidth = s;
    }

    public void setAlign(CSS.Align s) {
        mAlign = s;
    }
}
