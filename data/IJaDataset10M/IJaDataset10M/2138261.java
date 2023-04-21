package sts.gui.fontsize;

import java.util.*;
import java.util.prefs.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.metal.*;

public class FontSizeManager {

    private static final String prefsKey = "default-font-size";

    private static Map<JTable, Object> jtables = new WeakHashMap<JTable, Object>();

    private static boolean active = false;

    private static int tableCellHeight = 16;

    public static void initialize(int size) throws Exception {
        setFontSize(size, null);
        UIManager.addAuxiliaryLookAndFeel(new AuxLookAndFeel());
    }

    public static void addJTable(JTable jtable) {
        jtables.put(jtable, null);
        if (active) jtable.setRowHeight(tableCellHeight);
    }

    public static void setFontSize(int size, Component rootComponent) throws Exception {
        MetalTheme theme;
        String lafName = "javax.swing.plaf.metal.MetalLookAndFeel";
        if (size == 0) {
            active = false;
            theme = new OceanTheme();
            tableCellHeight = 16;
        } else {
            active = true;
            theme = new TexasTheme(size);
            tableCellHeight = size + 4;
        }
        MetalLookAndFeel.setCurrentTheme(theme);
        UIManager.setLookAndFeel(lafName);
        for (JTable jtable : jtables.keySet()) {
            jtable.setRowHeight(tableCellHeight);
        }
        if (rootComponent != null) {
            SwingUtilities.updateComponentTreeUI(rootComponent);
        }
    }
}
