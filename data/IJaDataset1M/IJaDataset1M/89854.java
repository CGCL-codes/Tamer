package com.pbxworkbench.commons;

import javax.swing.event.ChangeListener;

public interface Changeable {

    void addChangeListener(ChangeListener listener);

    void removeChangeListener(ChangeListener listener);

    void fireChanged();
}
