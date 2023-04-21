package superabbrevs.migration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import org.gjt.sp.jedit.MiscUtilities;
import org.gjt.sp.jedit.jEdit;
import superabbrevs.model.Abbrev;
import superabbrevs.Paths;
import superabbrevs.Persistence;
import superabbrevs.model.Mode;
import superabbrevs.utilities.Log;

public class Migration {

    public static void Migrate() {
        Log.log(Log.Level.DEBUG, Migration.class, "Migrating old abbreviations");
        removeOldMacros();
    }

    private static void deleteAbbrevsDir() {
        File oldAbbrevsDir = new File(Paths.OLD_ABBREVS_DIR);
        deleteDirectory(oldAbbrevsDir);
    }

    private static void importOldAbbrevs() {
        for (org.gjt.sp.jedit.Mode mode : jEdit.getModes()) {
            Hashtable<String, ArrayList<Abbrev>> newAbbrevs = loadAbbrevs(mode.getName());
            Hashtable<String, String> oldAbbrevs = readModeFile(mode.getName());
            if (oldAbbrevs != null) {
                importOldAbbrevsForMode(mode.getName(), oldAbbrevs, newAbbrevs);
            }
        }
    }

    private static boolean deleteDirectory(File path) {
        if (path == null) {
            return false;
        }
        if (path.exists()) {
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }

    private static void importOldAbbrevsForMode(String modeName, Hashtable<String, String> oldAbbrevs, Hashtable<String, ArrayList<Abbrev>> newAbbrevs) {
        for (Entry<String, String> abbrev : oldAbbrevs.entrySet()) {
            insertIfNotExist(abbrev.getKey(), abbrev.getValue(), newAbbrevs);
        }
        Mode mode = new Mode(modeName);
        mode.getAbbreviations().addAll(flatten(newAbbrevs));
        try {
            Persistence.saveMode(mode);
        } catch (IOException ex) {
            Log.log(Log.Level.ERROR, Migration.class, ex);
        }
    }

    private static ArrayList<Abbrev> flatten(Hashtable<String, ArrayList<Abbrev>> abbrevs) {
        ArrayList<Abbrev> result = new ArrayList<Abbrev>();
        for (ArrayList<Abbrev> abbrevsList : abbrevs.values()) {
            for (Abbrev abbrev : abbrevsList) {
                result.add(abbrev);
            }
        }
        return result;
    }

    private static void insertIfNotExist(String abbrev, String expansion, Hashtable<String, ArrayList<Abbrev>> abbrevs) {
        ArrayList<Abbrev> abbrevsList = abbrevs.get(abbrev);
        if (abbrevsList == null) {
            abbrevsList = new ArrayList<Abbrev>();
            abbrevs.put(abbrev, abbrevsList);
        }
        boolean found = false;
        Iterator<Abbrev> iter = abbrevsList.iterator();
        while (!found && iter.hasNext()) {
            Abbrev ab = iter.next();
            found = ab.expansion.equals(expansion);
        }
        if (!found) {
            Abbrev ab = new Abbrev(abbrev, abbrev, expansion);
            abbrevsList.add(ab);
        }
    }

    private static Hashtable<String, ArrayList<Abbrev>> loadAbbrevs(String modeName) {
        Hashtable<String, ArrayList<Abbrev>> result = new Hashtable<String, ArrayList<Abbrev>>();
        for (Abbrev abbrev : Persistence.loadMode(modeName).getAbbreviations()) {
            ArrayList<Abbrev> abbrevs = result.get(abbrev.abbreviation);
            if (abbrevs == null) {
                abbrevs = new ArrayList<Abbrev>();
                result.put(abbrev.abbreviation, abbrevs);
            }
            abbrevs.add(abbrev);
        }
        return result;
    }

    private static Hashtable<String, String> readModeFile(String name) {
        return readObjectFile(getModeFile(name));
    }

    private static File getModeFile(String name) {
        File modeDir = new File(Paths.OLD_ABBREVS_DIR);
        if (!modeDir.exists()) {
            modeDir.mkdir();
        }
        File modeFile = new File(MiscUtilities.constructPath(Paths.OLD_ABBREVS_DIR, name));
        return modeFile;
    }

    private static void removeOldMacros() {
        File macrosDir = new File(Paths.MACRO_DIR);
        if (macrosDir.exists()) {
            File tabFile = new File(MiscUtilities.constructPath(Paths.MACRO_DIR, Paths.TAB_MACRO));
            tabFile.delete();
            File shiftTabFile = new File(MiscUtilities.constructPath(Paths.MACRO_DIR, Paths.SHIFT_TAB_MACRO));
            shiftTabFile.delete();
            macrosDir.delete();
        }
    }

    @SuppressWarnings("unchecked")
    private static Hashtable<String, String> readObjectFile(File file) {
        if (file.exists()) {
            try {
                FileInputStream in = new FileInputStream(file);
                ObjectInputStream s = new ObjectInputStream(in);
                return (Hashtable<String, String>) s.readObject();
            } catch (FileNotFoundException e) {
                Log.log(Log.Level.ERROR, Migration.class, e);
            } catch (IOException e) {
                Log.log(Log.Level.ERROR, Migration.class, e);
            } catch (ClassNotFoundException e) {
                Log.log(Log.Level.ERROR, Migration.class, e);
            }
        }
        return null;
    }
}
