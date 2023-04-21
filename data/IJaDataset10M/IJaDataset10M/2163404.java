package net.f.ui;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;

public class ColorGroup extends ChoiceGroup {

    public ColorGroup(String selected) {
        super("Color", Choice.POPUP);
        append("", null);
        append("Beige", null);
        append("Blue", null);
        append("Black", null);
        append("Cyan", null);
        append("Gold", null);
        append("Gray", null);
        append("Green", null);
        append("Khaki", null);
        append("Lime", null);
        append("Maroon", null);
        append("Navy", null);
        append("Orange", null);
        append("Pink", null);
        append("Purple", null);
        append("Red", null);
        append("Silver", null);
        append("White", null);
        append("Yellow", null);
        setValue(selected);
    }

    public String getValue() {
        String ret = null;
        int ix = getSelectedIndex();
        if (ix > 0) {
            ret = getString(ix);
        }
        return ret;
    }

    public void setValue(String selected) {
        if (selected != null) {
            int n = size();
            for (int i = 0; i < n; i++) {
                if (selected.equals(getString(i))) {
                    setSelectedIndex(i, true);
                    break;
                }
            }
        }
    }
}
