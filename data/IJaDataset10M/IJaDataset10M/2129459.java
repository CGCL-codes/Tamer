package org.swingerproject.layouts.impl;

import java.awt.Container;
import java.awt.LayoutManager;
import javax.swing.SpringLayout;
import org.swingerproject.layouts.LayoutFactory;

public class SpringLayoutFactory implements LayoutFactory {

    public LayoutManager createLayoutManager(Container target, String... params) {
        return new SpringLayout();
    }
}
