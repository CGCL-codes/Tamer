package com.memoire.bu;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import javax.swing.JFrame;

/**
 * A frame to display an internal frame.
 */
public class BuContentFrame extends JFrame implements PropertyChangeListener {

    private BuInternalFrame content_;

    public BuContentFrame(BuInternalFrame _content) {
        content_ = _content;
        setContentPane(content_.getContentPane());
        setTitle(content_.getTitle());
        content_.addPropertyChangeListener(this);
        pack();
    }

    public void propertyChange(PropertyChangeEvent _evt) {
        if ("closed".equals(_evt.getPropertyName())) {
            if (Boolean.TRUE.equals(_evt.getNewValue())) hide();
        }
    }

    public void show() {
        try {
            content_.setClosed(false);
        } catch (PropertyVetoException ex) {
        }
        super.show();
    }
}
