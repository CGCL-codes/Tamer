package com.hifiremote.jp1;

import javax.swing.*;
import java.util.*;

/**
 * The Class DeviceParameter.
 */
public abstract class DeviceParameter extends Parameter {

    /**
   * Instantiates a new device parameter.
   * 
   * @param name the name
   */
    public DeviceParameter(String name) {
        this(name, null);
    }

    /**
   * Instantiates a new device parameter.
   * 
   * @param name the name
   * @param defaultValue the default value
   */
    public DeviceParameter(String name, DefaultValue defaultValue) {
        super(name, defaultValue);
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        name = getDisplayName();
        if (name.length() > 0) label = new JLabel(name + ':', SwingConstants.RIGHT); else label = new JLabel();
    }

    /**
   * Gets the label.
   * 
   * @return the label
   */
    public JLabel getLabel() {
        return label;
    }

    /**
   * Commit.
   */
    public void commit() {
    }

    /**
   * Gets the component.
   * 
   * @return the component
   */
    public abstract JComponent getComponent();

    /**
   * Adds the listener.
   * 
   * @param l the l
   */
    public abstract void addListener(EventListener l);

    /**
   * Removes the listener.
   * 
   * @param l the l
   */
    public abstract void removeListener(EventListener l);

    /** The label. */
    private JLabel label;
}
