package org.maveryx.AUTGuiObject;

import java.awt.Component;
import abbot.tester.JLabelTester;

public class AUTLabel extends AUTObject {

    private JLabelTester label = new JLabelTester();

    public Object expandPath(Component c) {
        Object obj = null;
        label.actionClick(c);
        return obj;
    }

    public Object collapsePath(Component c) {
        Object obj = null;
        label.actionClick(c);
        return obj;
    }

    public Object click(Component c) {
        Object obj = null;
        label.click(c);
        return obj;
    }

    public Object copy(Component c) {
        Object obj = null;
        return obj;
    }

    public Object cut(Component c) {
        Object obj = null;
        return obj;
    }

    public Object delete(Component c) {
        Object obj = null;
        return obj;
    }

    public Object paste(Component c) {
        Object obj = null;
        return obj;
    }

    public boolean isLeaf(Component c) {
        return false;
    }

    public Object getSubtree(Component c, String[] path) {
        @SuppressWarnings("unused") int intPath = Integer.valueOf(path[0]);
        return null;
    }
}
