package de.dgsoftware.notatioantiqua;

import java.awt.Font;
import javax.swing.UIManager;

public class namain {

    public static void main(String[] args) {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        Font defaultfont = new Font("Ubuntu", Font.PLAIN, 14);
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, defaultfont);
            }
        }
        nawindow appMain = new de.dgsoftware.notatioantiqua.nawindow();
        appMain.setVisible(true);
    }
}
