package org.kku.util;

import javax.swing.*;

public abstract class AbstractWorkProgressListener implements WorkProgressListenerIF {

    private Object userObject;

    private String workProgressText;

    public AbstractWorkProgressListener() {
        this(null);
    }

    public AbstractWorkProgressListener(Object userObject) {
        this.userObject = userObject;
    }

    public Object getUserObject() {
        return userObject;
    }

    public void setWorkStart() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                workStart();
            }
        });
    }

    public void setWorkProgress(String text) {
        this.workProgressText = text;
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                workProgress(workProgressText);
            }
        });
    }

    public void setWorkReady() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                workReady();
            }
        });
    }

    public void workStart() {
    }

    public void workProgress(String text) {
    }

    public void workReady() {
    }
}
