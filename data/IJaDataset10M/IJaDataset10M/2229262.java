package com.oz.lanslim.gui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

class TabbedPaneCloseButtonUI extends BasicTabbedPaneUI {

    private static final String PROPERTY_CHANGE_NAME = "tabOpen";

    private static final int minimumTabWidth = 100;

    public TabbedPaneCloseButtonUI() {
        super();
    }

    protected void paintTab(Graphics g, int tabPlacement, Rectangle[] recta, int tabIndex, Rectangle iconRect, Rectangle textRect) {
        super.paintTab(g, tabPlacement, recta, tabIndex, iconRect, textRect);
        Rectangle rect = recta[tabIndex];
        g.setColor(Color.BLACK);
        g.drawRect(rect.x + rect.width - 19, rect.y + 4, 13, 12);
        g.setColor(Color.RED);
        g.fillRect(rect.x + rect.width - 18, rect.y + 5, 11, 10);
        g.setColor(Color.WHITE);
        g.drawLine(rect.x + rect.width - 16, rect.y + 7, rect.x + rect.width - 10, rect.y + 13);
        g.drawLine(rect.x + rect.width - 10, rect.y + 7, rect.x + rect.width - 16, rect.y + 13);
        g.drawLine(rect.x + rect.width - 15, rect.y + 7, rect.x + rect.width - 9, rect.y + 13);
        g.drawLine(rect.x + rect.width - 9, rect.y + 7, rect.x + rect.width - 15, rect.y + 13);
    }

    protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
        int w = super.calculateTabWidth(tabPlacement, tabIndex, metrics) + 24;
        if (w < minimumTabWidth) {
            return minimumTabWidth;
        }
        return w;
    }

    protected MouseListener createMouseListener() {
        return new MyMouseHandler();
    }

    class MyMouseHandler extends MouseHandler {

        boolean removeEnable = true;

        public MyMouseHandler() {
            super();
        }

        private int getTabIndex(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            int tabIndex = -1;
            int tabCount = tabPane.getTabCount();
            for (int i = 0; i < tabCount; i++) {
                if (rects[i].contains(x, y)) {
                    tabIndex = i;
                    break;
                }
            }
            return tabIndex;
        }

        public void mousePressed(MouseEvent e) {
            int tabIndex = getTabIndex(e);
            if (tabPane.getSelectedIndex() == tabIndex) {
                removeEnable = true;
            } else {
                removeEnable = false;
            }
            super.mousePressed(e);
        }

        public void mouseReleased(MouseEvent e) {
            int tabIndex = getTabIndex(e);
            if (tabIndex >= 0 && !e.isPopupTrigger()) {
                int x = e.getX();
                int y = e.getY();
                Rectangle tabRect = rects[tabIndex];
                y = y - tabRect.y;
                if ((x >= tabRect.x + tabRect.width - 18) && (x <= tabRect.x + tabRect.width - 8) && (y >= 5) && (y <= 15)) {
                    PropertyChangeListener[] pcla = tabPane.getComponentAt(tabIndex).getPropertyChangeListeners(PROPERTY_CHANGE_NAME);
                    if (pcla != null) {
                        PropertyChangeEvent evt = new PropertyChangeEvent(tabPane.getComponentAt(tabIndex), PROPERTY_CHANGE_NAME, new Integer(0), new Integer(1));
                        for (int i = 0; i < pcla.length; i++) {
                            pcla[i].propertyChange(evt);
                        }
                    }
                    if (removeEnable) {
                        tabPane.remove(tabIndex);
                    }
                    ChangeListener[] cla = tabPane.getChangeListeners();
                    if (pcla != null) {
                        ChangeEvent evt = new ChangeEvent(tabPane);
                        for (int i = 0; i < cla.length; i++) {
                            cla[i].stateChanged(evt);
                        }
                    }
                }
            }
        }
    }
}
