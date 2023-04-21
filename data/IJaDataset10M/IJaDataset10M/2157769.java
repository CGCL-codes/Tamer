package com.atticlabs.zonelayout.swing;

import java.awt.Component;
import java.awt.Dimension;

final class MinimumSizeView extends SizeView {

    Dimension getSize(ComponentSizeCache c) {
        return c.getMinimumSize();
    }

    int getSize(Section s) {
        return s.getMinimumSize();
    }

    void setSize(Section s, int size) {
        s.setMinimumSize(size);
    }
}
