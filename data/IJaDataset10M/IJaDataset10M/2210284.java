package net.mikmy.jflowercards.swing;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
class Dock extends JPanel {

    public Dock() {
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        TitledBorder border = BorderFactory.createTitledBorder("Dock");
        border.setTitleColor(Color.WHITE);
        setBorder(border);
        setOpaque(false);
        LayoutManager layout = new GridLayout(2, 1);
        setLayout(layout);
        JPanel hd = new HandDock();
        JPanel ed = new EatenDock();
        JScrollPane scroll = new JScrollPane(ed);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        add(hd);
        add(scroll);
    }
}
