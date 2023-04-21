package mecca.portal.element;

import java.io.Serializable;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class Module2 extends Module implements Serializable {

    private boolean isMarked = false;

    private String customTitle = "";

    private int col = 1;

    public Module2(String module_id, String module_title, String module_class, boolean b) {
        super(module_id, module_title, module_class);
        isMarked = b;
    }

    public Module2(String module_id, String module_title, String module_class, boolean b, String s) {
        super(module_id, module_title, module_class);
        isMarked = b;
        customTitle = s;
    }

    public Module2(String module_id, String module_title, String module_class, String s) {
        super(module_id, module_title, module_class);
        customTitle = s;
    }

    public Module2(String module_id, String module_title, String module_class, String s, int i) {
        super(module_id, module_title, module_class);
        customTitle = s;
        col = i;
    }

    public boolean getMarked() {
        return isMarked;
    }

    public void setMarked(boolean b) {
        isMarked = b;
    }

    public String getCustomTitle() {
        return customTitle;
    }

    public void setCustomTitle(String s) {
        customTitle = s;
    }

    public void setColumn(int i) {
        col = i;
    }

    public int getColumn() {
        return col;
    }
}
