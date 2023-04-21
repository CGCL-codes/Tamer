package org.gjt.sp.jedit.testframework;

import java.awt.Dialog;
import java.awt.Component;
import org.junit.*;
import static org.junit.Assert.*;
import org.fest.swing.fixture.*;
import org.fest.swing.core.*;
import org.fest.swing.finder.WindowFinder;

public class FirstDialogMatcher implements ComponentMatcher {

    private boolean found = false;

    private String title;

    public FirstDialogMatcher(String title) {
        this.title = title;
    }

    public boolean matches(Component comp) {
        if (found) return false;
        if (comp instanceof Dialog && comp.isVisible()) {
            found = title.equals(((Dialog) comp).getTitle());
        }
        return found;
    }
}
