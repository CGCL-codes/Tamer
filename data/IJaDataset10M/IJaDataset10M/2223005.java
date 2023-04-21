package csa.jportal.gui;

import csa.jportal.config.Configuration;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.lang.reflect.*;

/**
 *
 * @author Malban
 */
public class Blacker {

    static HashMap<Class, Color> mDefaultDisFGColors = new HashMap<Class, Color>();

    static HashMap<Class, Color> mDefaultBGColors = new HashMap<Class, Color>();

    static HashMap<Class, Color> mDefaultFGColors = new HashMap<Class, Color>();

    static HashMap<Class, Boolean> mDefaultOpaque = new HashMap<Class, Boolean>();

    static HashMap<Class, Color> mBlackDisFGColors = new HashMap<Class, Color>();

    static HashMap<Class, Color> mBlackBGColors = new HashMap<Class, Color>();

    static HashMap<Class, Color> mBlackFGColors = new HashMap<Class, Color>();

    static HashMap<Class, Boolean> mBlackOpaque = new HashMap<Class, Boolean>();

    public static void saveDefault(JComponent c) {
        Class cl = c.getClass();
        Color bg = c.getBackground();
        Color fg = c.getForeground();
        boolean opaque = c.isOpaque();
        mDefaultBGColors.put(cl, bg);
        mDefaultFGColors.put(cl, fg);
        mDefaultOpaque.put(cl, opaque);
        try {
            Method method = cl.getMethod("getDisabledTextColor", new Class[] {});
            method.setAccessible(true);
            Color dfg = (Color) method.invoke(c, new Object[] {});
            mDefaultDisFGColors.put(cl, dfg);
        } catch (Throwable e) {
        }
        for (int j = 0; j < c.getComponentCount(); j++) {
            final java.awt.Component mc = c.getComponent(j);
            if (mc instanceof JComponent) {
                saveDefault((JComponent) mc);
            }
        }
    }

    public static void setValues(JComponent c, HashMap<Class, Color> bgMap, HashMap<Class, Color> fgMap, HashMap<Class, Color> dfgMap, HashMap<Class, Boolean> oMap) {
        if (Configuration.getConfiguration().isNoPanelChanging()) {
            return;
        }
        Set entries = bgMap.entrySet();
        Iterator it = entries.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Class key = (Class) entry.getKey();
            if (key.isAssignableFrom(c.getClass())) {
                Class cl = key;
                Color bg = bgMap.get(cl);
                Color fg = fgMap.get(cl);
                Boolean opaque = oMap.get(cl);
                if (bg != null) {
                    c.setBackground(bg);
                }
                if (fg != null) {
                    c.setForeground(fg);
                }
                if (opaque != null) {
                    c.setOpaque(opaque);
                }
                Color dfg = dfgMap.get(cl);
                if (dfg != null) try {
                    Method method = cl.getMethod("setDisabledTextColor", new Class[] { Color.class });
                    method.setAccessible(true);
                    method.invoke(c, new Object[] { dfg });
                } catch (Throwable e) {
                }
                break;
            }
        }
        Class cl = c.getClass();
        Color bg = bgMap.get(cl);
        Color fg = fgMap.get(cl);
        Boolean opaque = oMap.get(cl);
        if (bg != null) {
            c.setBackground(bg);
        }
        if (fg != null) {
            c.setForeground(fg);
        }
        if (opaque != null) {
            c.setOpaque(opaque);
        }
        Color dfg = dfgMap.get(cl);
        if (dfg != null) try {
            Method method = cl.getMethod("setDisabledTextColor", new Class[] { Color.class });
            method.setAccessible(true);
            method.invoke(c, new Object[] { dfg });
        } catch (Throwable e) {
            e.printStackTrace();
        }
        for (int j = 0; j < c.getComponentCount(); j++) {
            final java.awt.Component mc = c.getComponent(j);
            if (mc instanceof JComponent) {
                setValues((JComponent) mc, bgMap, fgMap, dfgMap, oMap);
            }
        }
    }

    public static void setDefault(JComponent c) {
        htmlFG = "color:#000000";
        setValues((JComponent) c, mDefaultBGColors, mDefaultFGColors, mDefaultDisFGColors, mDefaultOpaque);
    }

    public static void setBlack(JComponent c) {
        htmlFG = "color:#c8c8c8";
        setValues((JComponent) c, mBlackBGColors, mBlackFGColors, mBlackDisFGColors, mBlackOpaque);
    }

    public static String htmlFG = "color:#000000";

    static {
        Color black = new Color(0, 0, 0, 255);
        Color cbg = new Color(100, 50, 50, 100);
        Color cfg = new Color(200, 200, 200);
        Color cdarker = new Color(0, 0, 0, 100);
        mBlackBGColors.put(JPanel.class, cbg);
        mBlackFGColors.put(JPanel.class, cfg);
        mBlackOpaque.put(JPanel.class, true);
        mBlackBGColors.put(JLabel.class, cbg);
        mBlackFGColors.put(JLabel.class, cfg);
        mBlackBGColors.put(JTextField.class, black);
        mBlackFGColors.put(JTextField.class, cfg);
        mBlackOpaque.put(JTextField.class, true);
        mBlackBGColors.put(JTextArea.class, black);
        mBlackFGColors.put(JTextArea.class, cfg);
        mBlackOpaque.put(JTextArea.class, true);
        mBlackDisFGColors.put(JTextArea.class, cfg);
        mBlackBGColors.put(JTabbedPane.class, cbg);
        mBlackFGColors.put(JTabbedPane.class, new Color(125, 125, 125));
        mBlackOpaque.put(JTabbedPane.class, false);
        mBlackBGColors.put(JTextPane.class, black);
        mBlackFGColors.put(JTextPane.class, cfg);
        mBlackDisFGColors.put(JTextPane.class, cfg);
        mBlackOpaque.put(JTextPane.class, true);
        mBlackBGColors.put(JScrollPane.class, cbg);
        mBlackOpaque.put(JScrollPane.class, false);
        mBlackBGColors.put(JComboBox.class, black);
        mBlackFGColors.put(JComboBox.class, cfg);
    }
}
