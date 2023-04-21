package org.ungoverned.radical.container;

import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTabbedPane;
import org.ungoverned.radical.ContainerDelegate;
import org.ungoverned.radical.ui.JPagedPane;

public class JPagedPaneDelegate implements ContainerDelegate {

    private Container m_container = null;

    private boolean m_designTime = true;

    public Container initialize(Container container, String params) {
        m_container = container;
        JPagedPane pager = (JPagedPane) m_container;
        pager.removeAllPages();
        return m_container;
    }

    public Component[] getContextMenuItems(Point p) {
        JPagedPane pager = (JPagedPane) m_container;
        ArrayList items = new ArrayList();
        boolean isJDK14 = false;
        try {
            JTabbedPane.class.getMethod("getTabLayoutPolicy", null);
            isJDK14 = true;
        } catch (Exception ex) {
        }
        JMenu sub = null;
        sub = new JMenu("Button placement");
        items.add(sub);
        sub.add(new JRadioButtonMenuItem(new AbstractAction("Top") {

            public void actionPerformed(ActionEvent event) {
                JPagedPane pager = (JPagedPane) m_container;
                pager.setButtonPlacement(JPagedPane.TOP);
            }
        }));
        sub.add(new JRadioButtonMenuItem(new AbstractAction("Bottom") {

            public void actionPerformed(ActionEvent event) {
                JPagedPane pager = (JPagedPane) m_container;
                pager.setButtonPlacement(JPagedPane.BOTTOM);
            }
        }));
        sub.add(new JRadioButtonMenuItem(new AbstractAction("Left") {

            public void actionPerformed(ActionEvent event) {
                JPagedPane pager = (JPagedPane) m_container;
                pager.setButtonPlacement(JPagedPane.LEFT);
            }
        }));
        sub.add(new JRadioButtonMenuItem(new AbstractAction("Right") {

            public void actionPerformed(ActionEvent event) {
                JPagedPane pager = (JPagedPane) m_container;
                pager.setButtonPlacement(JPagedPane.RIGHT);
            }
        }));
        if (pager.getButtonPlacement() == JPagedPane.TOP) {
            sub.getItem(0).setSelected(true);
        } else if (pager.getButtonPlacement() == JPagedPane.BOTTOM) {
            sub.getItem(1).setSelected(true);
        } else if (pager.getButtonPlacement() == JPagedPane.LEFT) {
            sub.getItem(2).setSelected(true);
        } else {
            sub.getItem(3).setSelected(true);
        }
        if (pager.getPageCount() > 0) {
            sub = new JMenu("Show page");
            items.add(sub);
            for (int i = 0; i < pager.getPageCount(); i++) {
                sub.add(new JRadioButtonMenuItem(new ShowPageAction(pager, i)));
            }
            if (pager.getSelectedIndex() >= 0) {
                sub.getItem(pager.getSelectedIndex()).setSelected(true);
            }
        }
        if (items.size() > 0) {
            Component[] comps = new Component[items.size()];
            return (Component[]) items.toArray(comps);
        }
        return null;
    }

    public Component[] getComponents() {
        JPagedPane pager = (JPagedPane) m_container;
        Component[] comps = null;
        if (pager.getPageCount() > 0) {
            comps = new Component[pager.getPageCount()];
            for (int i = 0; i < comps.length; i++) {
                comps[i] = pager.getPageComponent(i);
            }
        }
        return comps;
    }

    public Component getComponentAt(Point p) {
        Component target = m_container.getComponentAt(p);
        JPagedPane pager = (JPagedPane) m_container;
        if (target == pager.getSelectedComponent()) {
            return target;
        }
        return null;
    }

    public boolean add(Component comp, Point p) {
        Component target = m_container.getComponentAt(p);
        JPagedPane pager = (JPagedPane) m_container;
        if (target != pager.getSelectedComponent()) {
            String name = comp.getClass().getName();
            name = (name.lastIndexOf('.') >= 0) ? name.substring(name.lastIndexOf('.') + 1) : name;
            pager.addPage(name, comp);
            return true;
        }
        return false;
    }

    public boolean add(Component comp, Object constraint) {
        JPagedPane pager = (JPagedPane) m_container;
        String name = comp.getClass().getName();
        name = (name.lastIndexOf('.') >= 0) ? name.substring(name.lastIndexOf('.') + 1) : name;
        pager.addPage(name, comp);
        return true;
    }

    public boolean remove(Component comp) {
        JPagedPane pager = (JPagedPane) m_container;
        for (int i = 0; i < pager.getPageCount(); i++) {
            if (comp == pager.getPageComponent(i)) {
                pager.removePage(comp);
                return true;
            }
        }
        return false;
    }

    public boolean isDesignMode() {
        return m_designTime;
    }

    public void setDesignMode(boolean b) {
        m_designTime = b;
    }

    public String convertToString() {
        return "";
    }

    public String convertToSource(String panelName) {
        if ((panelName != null) && (panelName.length() != 0)) {
            return "JPagedPane " + panelName + " = new JPagedPane();";
        }
        return null;
    }

    public String addComponentSource(Component component, String name, String panelName) {
        if (panelName.length() != 0) {
            panelName = panelName + ".";
        }
        return panelName + "addPage(\"" + name + "\", " + name + ");";
    }

    public String getComponentParameterString(Component component) {
        return "";
    }

    public Object getComponentParameterObject(String s) {
        return null;
    }

    public String[] getImports() {
        return null;
    }

    private static class ShowPageAction extends AbstractAction {

        private JPagedPane m_pager = null;

        private int m_idx = -1;

        public ShowPageAction(JPagedPane pager, int idx) {
            super(pager.getPageTitle(idx));
            m_pager = pager;
            m_idx = idx;
        }

        public void actionPerformed(ActionEvent event) {
            m_pager.setSelectedIndex(m_idx);
        }
    }
}
