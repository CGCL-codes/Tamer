package org.tn5250j.tools;

import java.awt.event.KeyEvent;
import java.util.*;
import java.io.*;

public class Macronizer {

    private static String macroName;

    private static Properties macros;

    private static boolean macrosExist;

    public static void init() {
        if (macros != null) return;
        init("macros");
    }

    public static void init(String macroFileName) {
        if (macros != null) return;
        macroName = macroFileName;
        macros = new Properties();
        macrosExist = loadMacros(macros);
    }

    private static boolean loadMacros(Properties macs) {
        FileInputStream in = null;
        try {
            in = new FileInputStream(macroName);
            macs.load(in);
            return true;
        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe.getMessage());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        } catch (SecurityException se) {
            System.out.println(se.getMessage());
        }
        return false;
    }

    private static final void saveMacros() {
        try {
            FileOutputStream out = new FileOutputStream(macroName);
            macros.store(out, "------ Macros --------");
        } catch (FileNotFoundException fnfe) {
        } catch (IOException ioe) {
        }
    }

    public static final boolean isMacrosExist() {
        return macrosExist;
    }

    public static final int getNumOfMacros() {
        return macros.size();
    }

    public static final String[] getMacroList() {
        String[] macroList = new String[macros.size()];
        Set macroSet = macros.keySet();
        Iterator macroIterator = macroSet.iterator();
        String byName = null;
        int x = 0;
        while (macroIterator.hasNext()) {
            byName = (String) macroIterator.next();
            int period = byName.indexOf(".");
            macroList[x++] = byName.substring(period + 1);
        }
        return macroList;
    }

    public static final String getMacroByNumber(int num) {
        String mac = "macro" + num + ".";
        Set macroSet = macros.keySet();
        Iterator macroIterator = macroSet.iterator();
        String byNum = null;
        while (macroIterator.hasNext()) {
            byNum = (String) macroIterator.next();
            if (byNum.startsWith(mac)) {
                return (String) macros.get(byNum);
            }
        }
        return null;
    }

    public static final String getMacroByName(String name) {
        Set macroSet = macros.keySet();
        Iterator macroIterator = macroSet.iterator();
        String byName = null;
        while (macroIterator.hasNext()) {
            byName = (String) macroIterator.next();
            if (byName.endsWith(name)) {
                return (String) macros.get(byName);
            }
        }
        return null;
    }

    public static final void setMacro(String name, String keyStrokes) {
        int x = 0;
        while (getMacroByNumber(++x) != null) {
        }
        macros.put("macro" + x + "." + name, keyStrokes);
        macrosExist = true;
        saveMacros();
    }
}
