package org.kku.gui;

import info.clearthought.layout.*;
import org.kku.domain.*;
import org.kku.gui.action.*;
import org.kku.gui.swing.*;
import org.kku.mp3.*;
import org.kku.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.beans.*;
import java.util.*;
import java.util.List;

public class StatusBar extends JPanel {

    private JLabel statusLabel;

    private DukeJukeBoxPanel jukePanel;

    public StatusBar(DukeJukeBoxPanel jukePanel) {
        super();
        this.jukePanel = jukePanel;
        init();
    }

    public void setStatus(String status) {
        statusLabel.setText(status);
    }

    private void init() {
        JukeAction a;
        TableLayout l;
        int column;
        int row;
        double p;
        double f;
        Component c;
        int numRow;
        int numColumn;
        p = TableLayout.PREFERRED;
        f = TableLayout.FILL;
        l = new TableLayout(new double[][] { { p, p, p, p, p, f, p }, { p, p, p, p } });
        numRow = l.getNumRow();
        numColumn = l.getNumColumn();
        setLayout(l);
        c = new JSeparator(JSeparator.HORIZONTAL);
        add(c, "0, 0, " + (numColumn - 1) + ", 0, f, f");
        c = Box.createRigidArea(new Dimension(10, 5));
        add(c, "0, 1");
        a = new JukeAction("prev", "images/stock_properties.png");
        a.setActionMethod(this, "setEditMode");
        c = new ToolToggleButton(a);
        add(c, "1, 2");
        c = Box.createHorizontalStrut(5);
        add(c, "2, 2");
        a = new JukeAction("prev", "images/stock_fullscreen.png");
        a.setActionMethod(this, "setFullScreen");
        c = new ToolToggleButton(a);
        add(c, "3, 2");
        c = Box.createHorizontalStrut(5);
        add(c, "4, 2");
        statusLabel = new JLabel();
        statusLabel.setBorder(new CompoundBorder(BorderFactory.createLineBorder(Color.gray), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        add(statusLabel, "5, 2, " + (numColumn - 2) + ", 2, f, f");
        c = Box.createRigidArea(new Dimension(10, 5));
        add(c, (numColumn - 1) + ", 3");
    }

    private void add(Component c, int row, int column) {
        add(c, column + ", " + row + ", l, t");
    }

    public void setFullScreen(JukeActionEvent ae) {
        boolean fullScreen;
        AbstractButton button;
        button = (AbstractButton) ae.getActionEvent().getSource();
        fullScreen = button.isSelected();
        jukePanel.getDukeJukeBox().setFullScreen(fullScreen);
    }
}
