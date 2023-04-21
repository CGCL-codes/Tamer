package arcus.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import arcus.gui.ext.XFrame;
import arcus.gui.ext.XTabbedPane;
import arcus.I18N;

public class TabbedContainer extends XFrame {

    public static TabbedContainer grEngineTabs = new TabbedContainer(I18N.get("View.title"));

    public static TabbedContainer consoleTabs = new TabbedContainer(I18N.get("Console.title"));

    public static TabbedContainer historyTabs = new TabbedContainer(I18N.get("History.title"));

    public static TabbedContainer editorTabs = new TabbedContainer(I18N.get("Editor.title"));

    private XTabbedPane tabbedPane;

    public TabbedContainer(String title) {
        super(title);
        tabbedPane = new XTabbedPane(title);
        tabbedPane.setName("COMPONENT_HOLDER");
        tabbedPane.addPropertyChangeListener(new TabManagerListener());
        add(tabbedPane);
    }

    public int getTabCount() {
        return tabbedPane.getTabCount();
    }

    public void addComponent(JComponent c) {
        tabbedPane.addTab(null, c);
        if (tabbedPane.getTabCount() == 1) {
            pack();
        }
        setVisible(true);
    }

    public void removeComponent(JComponent c) {
        tabbedPane.remove(c);
        if (tabbedPane.getTabCount() == 0) {
            setVisible(false);
        }
    }

    public void removeAll() {
        tabbedPane.removeAll();
        setVisible(false);
    }

    private class TabManagerListener implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent event) {
            if (event.getPropertyName().equals("zero_tab_count")) {
                setVisible(false);
            }
            if (event.getPropertyName().equals("nonzero_tab_count")) {
                setVisible(true);
            }
        }
    }
}
