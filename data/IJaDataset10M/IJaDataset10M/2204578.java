package org.bs.mdi.swing;

import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import org.bs.mdi.core.*;
import org.bs.mdi.gui.Command;

/**
 * Alternative MDI window manager using tabs
 */
public class TabMDIWindowManager implements MDIWindowManager {

    java.util.List windows = new ArrayList();

    public String getName() {
        return I18n.tr("windowmanager.tabs");
    }

    public void reset() {
        windows.clear();
    }

    public JComponent createDesktopComponent() {
        JTabbedPane desktopPane = new JTabbedPane();
        desktopPane.addChangeListener(new TabChangeListener());
        return desktopPane;
    }

    public DocumentWindow createDocumentWindow() {
        return new TabDocumentWindow();
    }

    public java.util.List getWindows() {
        return windows;
    }

    public void addWindow(JComponent desktop, DocumentWindow window) {
        String title = window.getTitle();
        if (title == null) title = "";
        TabDocumentWindow tabWindow = (TabDocumentWindow) window;
        JTabbedPane tabDesktop = (JTabbedPane) desktop;
        tabDesktop.addTab(title, tabWindow);
        windows.add(tabWindow);
        Application.getMessageDispatcher().dispatch(this, MessageDispatcher.WINDOW_OPENED, window);
    }

    public void windowRemoved(JComponent desktop, DocumentWindow window) {
        boolean currentWindowRemoved = false;
        TabDocumentWindow tabWindow = (TabDocumentWindow) window;
        JTabbedPane tabDesktop = (JTabbedPane) desktop;
        if (tabDesktop.getSelectedComponent() == tabWindow) {
            currentWindowRemoved = true;
        }
        tabDesktop.remove(tabWindow);
        tabWindow.setVisible(false);
        windows.remove(tabWindow);
        if (currentWindowRemoved) {
            tabWindow = (TabDocumentWindow) tabDesktop.getSelectedComponent();
            if (tabWindow != null) {
                tabWindow.toFront();
            }
            Application.getMessageDispatcher().dispatch(this, MessageDispatcher.WINDOW_SELECTED, tabWindow);
        }
    }

    public void windowSelected(JComponent desktop, DocumentWindow window) {
        if (window == null) return;
        JTabbedPane tabDesktop = (JTabbedPane) desktop;
        TabDocumentWindow tabWindow = (TabDocumentWindow) window;
        tabDesktop.setSelectedComponent(tabWindow);
        tabDesktop.setSelectedIndex(tabDesktop.indexOfComponent(tabWindow));
    }

    public Command[] getSpecialCommands() {
        return null;
    }

    protected void toFront(TabDocumentWindow window) {
        Application.getMessageDispatcher().dispatch(this, MessageDispatcher.WINDOW_SELECTED, window);
    }

    protected void setTitle(TabDocumentWindow window, String title) {
        MainWindow win = (MainWindow) Application.getMainWindow();
        JTabbedPane tabDesktop = (JTabbedPane) win.getDesktop();
        tabDesktop.setTitleAt(tabDesktop.indexOfComponent(window), title);
    }

    class TabChangeListener implements ChangeListener {

        public void stateChanged(ChangeEvent e) {
            MainWindow win = (MainWindow) Application.getMainWindow();
            JTabbedPane tabDesktop = (JTabbedPane) win.getDesktop();
            TabDocumentWindow tabWindow = (TabDocumentWindow) tabDesktop.getSelectedComponent();
            Application.getMessageDispatcher().dispatch(this, MessageDispatcher.WINDOW_SELECTED, tabWindow);
        }
    }
}
