package applications.cryptotoolj;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.applet.Applet;

/**
 *  Implements a menu system for CryptoToolJ.
 */
public class Menus {

    private static final int STATIC_WINDOW_MENU_ITEMS = 2;

    private static MenuBar mbar;

    private static CryptoToolJ ctj;

    private static Applet applet;

    public static Menu windowMenu;

    public Menus(CryptoToolJ ctg, Applet applet) {
        this.ctj = ctg;
        this.applet = applet;
    }

    public void addMenuBar() {
        Menu fileMenu = new Menu("File");
        addMenuItem(fileMenu, "New Cipher...", KeyEvent.VK_N, false);
        addMenuItem(fileMenu, "Open File...", KeyEvent.VK_O, false);
        addMenuItem(fileMenu, "Close Cipher...", KeyEvent.VK_W, false);
        fileMenu.addSeparator();
        addMenuItem(fileMenu, "Save", KeyEvent.VK_S, false);
        addMenuItem(fileMenu, "Save As", KeyEvent.VK_A, false);
        fileMenu.addSeparator();
        addMenuItem(fileMenu, "Print", KeyEvent.VK_P, false);
        fileMenu.addSeparator();
        addMenuItem(fileMenu, "Quit", KeyEvent.VK_Q, false);
        Menu editMenu = new Menu("Edit");
        addMenuItem(editMenu, "Cut", KeyEvent.VK_X, false);
        addMenuItem(editMenu, "Copy", KeyEvent.VK_C, false);
        addMenuItem(editMenu, "Paste", KeyEvent.VK_V, false);
        addMenuItem(editMenu, "Select All", 0, false);
        windowMenu = new Menu("Window");
        CheckboxMenuItem item = new CheckboxMenuItem("CryptoToolJ");
        windowMenu.add(item);
        item.setState(true);
        item.addActionListener(ctj);
        windowMenu.addSeparator();
        Menu helpMenu = new Menu("Help");
        addMenuItem(helpMenu, "About CryptoToolJ...", 0, false);
        mbar = new MenuBar();
        mbar.add(fileMenu);
        mbar.add(editMenu);
        mbar.add(windowMenu);
        mbar.add(createAnalysisMenu());
        mbar.setHelpMenu(helpMenu);
        ctj.setMenuBar(mbar);
    }

    private Menu createAnalysisMenu() {
        Menu analysisMenu = new Menu("Analysis");
        addMenuItem(analysisMenu, "Index Of Coincidence", KeyEvent.VK_I, false);
        addMenuItem(analysisMenu, "Text Histogram", KeyEvent.VK_H, false);
        addMenuItem(analysisMenu, "English FAQ", KeyEvent.VK_Q, false);
        addMenuItem(analysisMenu, "Pattern Word Searcher", KeyEvent.VK_W, false);
        analysisMenu.addSeparator();
        addMenuItem(analysisMenu, "Affine Analyzer", KeyEvent.VK_F, false);
        addMenuItem(analysisMenu, "Caesar Analyzer", KeyEvent.VK_R, false);
        addMenuItem(analysisMenu, "Vigenere Analyzer", KeyEvent.VK_G, false);
        analysisMenu.addSeparator();
        addMenuItem(analysisMenu, "Digram Substitution Analyzer", KeyEvent.VK_U, false);
        addMenuItem(analysisMenu, "Ngram GA Substitution Analyzer", 0, false);
        addMenuItem(analysisMenu, "WordBased Substitution Analyzer", 0, false);
        addMenuItem(analysisMenu, "WordBased GA Substitution Analyzer", 0, false);
        analysisMenu.addSeparator();
        if (applet == null) addPluginAnalyzers(analysisMenu);
        return analysisMenu;
    }

    /**
	 * addPluginAnalyzers() gets the names of the classes in the analyzers 
	 *  directory and adds them to the analyzer menu.
	 */
    private void addPluginAnalyzers(Menu m) {
        String className = "";
        try {
            System.out.println("Analyzers should be here: " + Tools.getAnalyzerDirectory());
            File file = new File(Tools.getAnalyzerDirectory());
            String analyzers[] = file.list();
            if (analyzers != null) for (int k = 0; k < analyzers.length; k++) {
                className = analyzers[k];
                if (className.endsWith("Analyzer.class")) {
                    String menuName = className.substring(0, className.indexOf("."));
                    ctj.display.append("Adding plugin analyzer: " + menuName + "\n");
                    addMenuItem(m, menuName, 0, false);
                }
            } else {
                ctj.display.append("No analyzers found\n");
                System.out.println("No analyzers found");
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    void addMenuItem(Menu menu, String label, int shortcut, boolean shift) {
        MenuItem item;
        if (shortcut == 0) item = new MenuItem(label); else {
            if (shift) {
                item = new MenuItem(label, new MenuShortcut(shortcut, true));
            } else {
                item = new MenuItem(label, new MenuShortcut(shortcut));
            }
        }
        item.setActionCommand(label);
        menu.add(item);
        item.addActionListener(ctj);
    }

    public static MenuBar getMenuBar() {
        return mbar;
    }

    public static void updateMenus() {
        CheckboxMenuItem item = null;
        int nItems = windowMenu.getItemCount();
        CipherFrame cf = WindowManager.getActiveCipherFrame();
        if (cf != null) {
            item = (CheckboxMenuItem) windowMenu.getItem(0);
            item.setState(false);
            for (int i = STATIC_WINDOW_MENU_ITEMS; i < nItems; i++) {
                item = (CheckboxMenuItem) windowMenu.getItem(i);
                item.setState(item.getLabel().indexOf(cf.getWindowId()) != -1);
            }
            return;
        }
        item = (CheckboxMenuItem) windowMenu.getItem(0);
        item.setState(true);
        for (int i = STATIC_WINDOW_MENU_ITEMS; i < nItems; i++) {
            item = (CheckboxMenuItem) windowMenu.getItem(i);
            item.setState(false);
        }
        return;
    }

    public static void extendWindowMenu(String name) {
        if (ctj == null) return;
        CheckboxMenuItem item = new CheckboxMenuItem(name);
        item.addActionListener(ctj);
        item.setState(true);
        windowMenu.add(item);
        item = (CheckboxMenuItem) windowMenu.getItem(0);
        item.setState(false);
    }

    public static void trimWindowMenu(String id) {
        for (int k = STATIC_WINDOW_MENU_ITEMS; k < windowMenu.getItemCount(); k++) {
            CheckboxMenuItem item = (CheckboxMenuItem) windowMenu.getItem(k);
            if (item.getLabel().indexOf(id) != -1) {
                windowMenu.remove(item);
                return;
            }
        }
        return;
    }
}
