package net.sf.magicmap.client.core;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.util.Locale;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JWindow;
import net.sf.magicmap.client.controller.Controller;
import net.sf.magicmap.client.gui.MainGUI;
import net.sf.magicmap.client.gui.utils.GUIBuilder;
import net.sf.magicmap.client.gui.utils.GUIUtils;
import net.sf.magicmap.client.utils.Settings;
import net.sf.magicmap.client.utils.Version;

public class MagicMapApplication {

    /**
     * Main entry point for the whole client
     *
     * @param args
     */
    public static void main(String[] args) {
        Settings.setup(args);
        if (GUIUtils.isOsX()) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", "MagicMap");
        }
        JWindow splashScreen = new JWindow();
        JLabel splashLabel = new JLabel(GUIBuilder.getToolIcon("magicmap-splash.png", "magicmapGraphics")) {

            private static final long serialVersionUID = 5735447445835224982L;

            @Override
            public void paint(Graphics g) {
                super.paint(g);
                g.drawString("Client-Version", 23, 190);
                g.drawString(Version.getVersion() + " " + Version.getAppendix(), 120, 190);
                g.drawString("Build", 23, 205);
                g.drawString(Version.getBuild(), 120, 205);
            }
        };
        splashScreen.getContentPane().add(splashLabel, BorderLayout.CENTER);
        splashScreen.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension labelSize = splashLabel.getPreferredSize();
        splashScreen.setLocation(screenSize.width / 2 - (labelSize.width / 2), screenSize.height / 2 - (labelSize.height / 2));
        splashScreen.setVisible(true);
        screenSize = null;
        labelSize = null;
        GUIUtils.setPlasticLookAndFeel();
        GUIUtils.setLocale(Locale.getDefault());
        JFrame frame = MainGUI.getInstance().getMainFrame();
        GUIUtils.locateOnScreen(frame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Controller.getInstance().initializePlugins();
        splashScreen.setVisible(false);
        splashScreen.dispose();
        frame.setVisible(true);
    }
}
