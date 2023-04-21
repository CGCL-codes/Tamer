package com.cosmos.acacia.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import com.cosmos.swingb.JBPanel;

public class TableHolderPanel extends JBPanel {

    public void add(AbstractTablePanel table) {
        int offset = 3;
        setPreferredSize(new Dimension((int) getPreferredSize().getWidth() - offset, (int) getPreferredSize().getHeight() - offset));
        setLayout(new BorderLayout());
        super.add(table);
    }
}
