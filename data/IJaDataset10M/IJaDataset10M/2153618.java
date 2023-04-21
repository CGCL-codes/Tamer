package com.pavelfatin.sleeparchiver.swing;

import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationActionMap;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.ResourceMap;
import javax.swing.*;
import java.awt.*;

public class ApplicationDialog extends JDialog {

    public ApplicationDialog(Frame owner) {
        super(owner);
    }

    public ApplicationDialog(Dialog owner) {
        super(owner);
    }

    protected String getString(String key) {
        return getResources().getString(key);
    }

    protected ResourceMap getResources() {
        return getContext().getResourceMap(getClass());
    }

    protected javax.swing.Action getAction(String name) {
        return getActions().get(name);
    }

    protected ApplicationActionMap getActions() {
        return getContext().getActionMap(this);
    }

    protected ApplicationContext getContext() {
        return Application.getInstance().getContext();
    }
}
