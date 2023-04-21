package net.sourceforge.steelme;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.metal.*;
import java.io.*;
import java.util.*;
import org.apache.log4j.*;

/**
 * Describe class <code>ThemeMenu</code> here.
 *
 * @author <a href="mailto:tj_willis@users.sourceforge.net">T.J. Willis</a>
 * @version 1.0
 * @has 1..n Has - net.sourceforge.steelme.ThemeResistantJMenuItem
 * @has 1..n Has - net.sourceforge.steelme.SteelmeTheme
 * @has 1 Has - net.sourceforge.steelme.ThemeFileFilter
 */
public class ThemeMenu extends JMenu implements ActionListener {

    private JComponent targ;

    private JMenuItem editor;

    private static Category cat = Category.getInstance(SteelmeTheme.class.getName());

    /**
     * Creates a new <code>ThemeMenu</code> instance.
     *
     * @param target a <code>JFrame</code> value
     * @param themeDirectory a <code>File</code> value
     */
    public ThemeMenu(JComponent target, File themeDirectory) {
        super("Themes");
        assert themeDirectory.isDirectory() : "Not a directory";
        targ = target;
        File fileArr[] = themeDirectory.listFiles();
        if (fileArr == null) return;
        int mySize = java.lang.reflect.Array.getLength(fileArr);
        if (mySize == 0) return;
        JMenu sub = new JMenu("Installed Themes");
        add(sub);
        ThemeFileFilter filter = new ThemeFileFilter();
        for (File f : fileArr) {
            if (f == null || f.isDirectory()) continue;
            if ((filter != null) && (!filter.accept(f))) continue;
            try {
                SteelmeTheme t = new SteelmeTheme(f);
                if (t == null) continue;
                ThemeResistantJMenuItem it = new ThemeResistantJMenuItem(t.getName());
                it.setMyBackground(t.getS3());
                it.setMyForeground(t.getT1());
                it.setMyFont(t.getControlTextFont());
                it.setActionCommand(f.getCanonicalPath());
                it.addActionListener(this);
                sub.add(it);
            } catch (Exception ex) {
                cat.warn("Theme loading exception: " + f, ex);
            }
        }
        editor = new JMenuItem("Theme Editor");
        add(editor);
        editor.addActionListener(this);
    }

    private void addFrame(JFrame f) {
        f.setVisible(true);
        f.pack();
    }

    /**
     * Describe <code>actionPerformed</code> method here.
     *
     * @param c an <code>ActionEvent</code> value
     */
    public void actionPerformed(ActionEvent c) {
        Object q = c.getSource();
        if (q.equals(editor)) {
            ThemeEditor editor = new ThemeEditor(targ.getRootPane());
            addFrame(editor);
            javax.swing.SwingUtilities.updateComponentTreeUI(targ.getRootPane());
            return;
        } else {
            File f = new File(c.getActionCommand());
            SteelmeTheme p = new SteelmeTheme(f);
            SteelmeThemeManager.setCurrentTheme(p);
            if (targ != null) {
                p.applyTheme(targ);
            }
        }
    }
}
