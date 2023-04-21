package com.jguigen.standard;

import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;
import java.awt.event.*;
import java.io.File;
import java.util.Locale;

public class LocaleMenu {

    /**
	 * Description - static class to create a menu option that allows the user to 
	 * change their Locale 
	 * @param String - label to show on the menu option
	 * @param JFrame - Frame that contains the objects whose Locale are changed
	 * @param Model  - the Model Class used throughout JGuiGen programs
	 * @return null or a menu item to add to your menu
	 * 
	 * @author Hale Pringle 
	 */
    static java.util.ResourceBundle modelRb;

    static final boolean DEBUG = false;

    static Model model;

    static String langName = "";

    static String langCode = "";

    static int knt = 0;

    static int maxKnt = 10;

    static String[][] arr = new String[10][2];

    public static JMenu createSwitchLocaleMenu(String label, JFrame f, Model mod) {
        int kntNative = 0;
        model = mod;
        modelRb = model.getResourceBdl("JguigenmodelRb");
        JMenu menu = new JMenu(label);
        int menuCount = 0;
        String[] langInfo = model.getLangCodes();
        for (int i = 0; i < langInfo.length; i++) {
            try {
                String langCode = langInfo[i].substring(langInfo[i].lastIndexOf(" ")).trim();
                String langName = langInfo[i].substring(0, langInfo[i].indexOf(" ")).trim();
                if (DEBUG) {
                    System.out.println("Found Lnnguage: " + langName + " with code: " + langCode);
                }
                LangSwitchAction switchAction = new LangSwitchAction(langName, langCode, f);
                JMenuItem mi = menu.add(switchAction);
                mi.setToolTipText("Language: " + langInfo[i]);
                menuCount++;
            } catch (Throwable t) {
                System.out.println(model.getRbString(modelRb, "Failed loading menu", "model") + " " + langInfo[i] + ":\n" + t);
            }
        }
        return menuCount == 0 ? null : menu;
    }

    protected static class LangSwitchAction extends AbstractAction {

        public LangSwitchAction(String langname, String langcode, JFrame frm) {
            super(langname + " " + (langcode.indexOf("_") == -1 ? "" : langcode.substring(langcode.indexOf("_"))));
            if (DEBUG) {
                System.out.println("in LangSwitchAction: " + langname + " code: " + langcode);
            }
            langName = langname;
            langCode = langcode;
            if (knt < maxKnt) {
                arr[knt][0] = langname + " " + (langcode.indexOf("_") == -1 ? "" : langcode.substring(langcode.indexOf("_")));
                arr[knt][1] = langcode;
                knt = knt + 1;
            }
            f = frm;
        }

        public void actionPerformed(ActionEvent evt) {
            String newLang = evt.getActionCommand();
            String changeTo = "";
            for (int i = 0; i < knt; i++) {
                if (newLang.equals(arr[i][0])) {
                    changeTo = arr[i][1];
                }
            }
            Locale loc = model.getCurrentLocale();
            String language = loc.getLanguage();
            String country = loc.getCountry();
            if (DEBUG) {
                System.out.println("old Lang and country : " + language + "_" + country);
            }
            try {
                if (DEBUG) {
                    System.out.println("before set Language " + changeTo);
                }
                if (changeTo.indexOf("_") > 0) {
                    String cty = changeTo.substring(changeTo.indexOf("_")).trim();
                    String lang = changeTo.substring(0, changeTo.indexOf("_")).trim();
                    Locale locale = new Locale(lang, cty);
                    Locale.setDefault(locale);
                    model.setCurrentLocale(locale);
                } else {
                    Locale locale = new Locale(changeTo);
                    Locale.setDefault(locale);
                    model.setCurrentLocale(locale);
                }
                if (DEBUG) {
                    System.out.println("after set langugae");
                }
                f.invalidate();
                SwingUtilities.updateComponentTreeUI(f);
                f.invalidate();
                f.validate();
                f.repaint();
            } catch (Throwable t) {
                System.out.println(model.getRbString(modelRb, "Failed to change locale: ", "model") + " " + langCode + "\n" + t);
            }
        }
    }

    protected static JFrame f;
}
