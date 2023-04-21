package com.columboid.testharness.main;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.Enumeration;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import com.columboid.testharness.gui.ColumboidSplash;
import com.columboid.testharness.gui.LoginFrame;
import com.columboid.testharness.util.Log4j;

public class MainApp {

    public static void main(String[] args) {
        new MainApp();
    }

    public MainApp() {
        Log4j.ConfigLog();
        ColumboidSplash splash = new ColumboidSplash();
        Font myFont = new Font("Arial", 0, 12);
        FontUIResource fontRes = new FontUIResource(myFont);
        setUIFont(fontRes);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
            Log4j.logger.error(e.toString());
        }
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log4j.logger.error(e.toString());
        }
        splash.dispose();
        openLoginFrame();
    }

    @SuppressWarnings("unchecked")
    public static void setUIFont(FontUIResource f) {
        Enumeration keys = UIManager.getDefaults().keys();
        do {
            if (!keys.hasMoreElements()) break;
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) UIManager.put(key, f);
        } while (true);
    }

    public void openLoginFrame() {
        LoginFrame frame = new LoginFrame();
        frame.validate();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        if (frameSize.height > screenSize.height) frameSize.height = screenSize.height;
        if (frameSize.width > screenSize.width) frameSize.width = screenSize.width;
        frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        frame.setVisible(true);
    }
}
