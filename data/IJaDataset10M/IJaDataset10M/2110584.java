package de.nomule.applogic;

import java.io.File;
import java.util.LinkedList;

public class NoMuleRuntime {

    public static boolean DEBUG = false;

    public static final String VERSION = "2008.09.21";

    public static final int REVISION = 68;

    private static LinkedList<MediaProvider> lMediaProvider = new LinkedList<MediaProvider>();

    private static LinkedList<String> lCategories = new LinkedList<String>();

    private static RuntimeIO runtimeIO;

    public static void setRuntimeIO(RuntimeIO r) {
        runtimeIO = r;
    }

    public static void addMediaProvider(MediaProvider p) {
        lMediaProvider.add(p);
    }

    public static LinkedList<MediaProvider> getMediaProviders() {
        return lMediaProvider;
    }

    public static LinkedList<MediaProvider> getMediaProvidersByContent(String strContent) {
        LinkedList<MediaProvider> l = new LinkedList<MediaProvider>();
        for (MediaProvider m : lMediaProvider) {
            String[] categories = m.getProvidedContent();
            for (String s : categories) {
                if (s.equals(strContent)) l.add(m);
            }
        }
        return l;
    }

    public static LinkedList<String> getOutputProfiles() {
        LinkedList<String> lProfiles = new LinkedList<String>();
        File f = new File("./");
        String[] files = f.list();
        for (String fileName : files) {
            String op = fileName.substring(fileName.length() - "op".length());
            if (op.equals("op")) {
                lProfiles.add(fileName.substring(0, fileName.length() - 3));
            }
        }
        return lProfiles;
    }

    public static void addCategory(String s) {
        if (!lCategories.contains(s)) lCategories.add(s);
    }

    public static LinkedList<String> getCategories() {
        return lCategories;
    }

    public static void showInformation(String s) {
        runtimeIO.showInformation(s);
    }

    public static void showError(String s) {
        runtimeIO.showError(s);
    }

    public static void showDebug(String s) {
        if (DEBUG) runtimeIO.showDebug(s);
    }

    public static void showMessage(String s) {
        runtimeIO.showMessage(s);
    }

    public static boolean showYesNoQuestion(String s) {
        return runtimeIO.showYesNoQuestion(s);
    }
}
