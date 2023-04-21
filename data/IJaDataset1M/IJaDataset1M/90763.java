package org.simplextensions.ui;

import org.simplextensions.registry.IExtensionRegistry;
import org.simplextensions.ui.menu.WindowMenuManager;
import org.simplextensions.ui.preferences.IPreferences;
import org.simplextensions.ui.view.ISwingViewRegion;
import org.simplextensions.ui.view.TabbedPaneViewManager;
import org.simplextensions.ui.view.ViewManager;
import javax.swing.*;
import java.awt.*;

public class SwingApplicationWindow extends ApplicationWindow {

    private JFrame jFrame;

    private WindowMenuManager windowMenuManager;

    private JPanel jPanel;

    private ViewManager<? extends ISwingViewRegion> viewManager;

    public SwingApplicationWindow(IExtensionRegistry extensionRegistry) {
        super(extensionRegistry);
        this.jFrame = createJFrame();
        windowMenuManager = new WindowMenuManager(this, "mainMenu");
        viewManager = new TabbedPaneViewManager(this);
    }

    protected JFrame createJFrame() {
        JFrame jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.jPanel = new JPanel();
        jFrame.add(jPanel, BorderLayout.CENTER);
        return jFrame;
    }

    public JPanel getjPanel() {
        return jPanel;
    }

    public WindowMenuManager getWindowMenuManager() {
        return windowMenuManager;
    }

    public JFrame getjFrame() {
        return jFrame;
    }

    public void openWindow(IPreferences iPreferences) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                createAndShowGUI();
            }
        });
    }

    private void createAndShowGUI() {
        this.viewManager.createContent();
        jFrame.pack();
        jFrame.setSize(600, 400);
        jFrame.setVisible(true);
    }
}
