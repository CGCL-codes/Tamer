package com.nexirius.framework.onlinehelp;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * A singleton class which handles online help events
 *
 * @author Marcel Baumann
 *         <p/>
 *         Date        Author           Changes/Enhancements
 *         2000-3-28  Marcel Baumann     Created
 */
public class OnlineHelp {

    private static OnlineHelp instance;

    OnlineHelpManager manager = null;

    private OnlineHelp(OnlineHelpManager m) {
        manager = m;
    }

    public static void init(OnlineHelpManager m) {
        instance = new OnlineHelp(m);
    }

    public static void set(Component component, String resourceKey) {
        if (instance == null) {
            return;
        }
        String text = getInstance().manager.getHelpString(resourceKey);
        if (text != null) {
            component.addKeyListener(new MyKeyListener(text));
        }
    }

    public static OnlineHelp getInstance() {
        if (instance == null) {
            throw new RuntimeException("OnlineHelp.init() was not called");
        }
        return instance;
    }

    public static String getHelpString(String resourceKey) {
        return getInstance().manager.getHelpString(resourceKey);
    }

    public static void help(String text) {
        getInstance().manager.help(text);
    }
}

class MyKeyListener implements KeyListener {

    String text;

    MyKeyListener(String text) {
        this.text = text;
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_F1) {
            System.out.println("KeyListener:keyPressed" + text);
            OnlineHelp.help(text);
        }
    }

    public void keyReleased(KeyEvent e) {
    }
}
