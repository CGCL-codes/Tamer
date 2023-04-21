package com.monkygames.sc2bob.gui.panels;

import java.awt.Image;
import java.util.Vector;
import javax.swing.*;
import com.monkygames.sc2bob.objects.Base;

/**
 * Displays SC2 Base Objects.
 * @version 1.0
 */
public class BaseTabbedPane extends JTabbedPane {

    private Vector<BaseTab> basicTabsV;

    public BaseTabbedPane(Vector<BaseTab> basicTabsV) {
        super(JTabbedPane.TOP);
        setTabs(basicTabsV);
    }

    public void setTabs(Vector<BaseTab> basicTabsV) {
        this.basicTabsV = basicTabsV;
        buildTabs();
    }

    /** 
     * Builds all tabs.
     **/
    private void buildTabs() {
        for (int i = 0; i < basicTabsV.size(); i++) {
            buildTab(basicTabsV.elementAt(i));
        }
    }

    /**
     * Builds a specific tab.
     **/
    private void buildTab(BaseTab basicTab) {
        ImageIcon icon = new ImageIcon(basicTab.icon.getImage().getScaledInstance(25, 25, Image.SCALE_AREA_AVERAGING));
        addTab("", icon, new SelectionPanel(basicTab.basicV));
    }
}
