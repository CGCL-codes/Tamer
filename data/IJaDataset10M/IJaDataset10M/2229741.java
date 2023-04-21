package iwork.manager.core;

import java.io.*;
import java.util.*;
import javax.swing.*;
import iwork.manager.core.hijacker.*;
import iwork.manager.core.settings.*;

/**
* Manages a set of configuration entries.
 *
 * <p>Every configuration entry can be classified as either being resolved
 * or being unresolved.</p>
 *
 * <p>Resolved configuration entries are represented by
 * {@link iwork.manager.ConfigEntry} objects. These are fit to use.</p>
 *
 * <p>Unresolved configuration entries are represented by
 * {@link iwork.manager.ConfigReader} objects and are not fit to use except
 * for resolution of other <code>ConfigReader</code> objects.</p>
 *
 * @author Ulf Ochsenfahrt (ulfjack@stanford.edu)
 */
public class ConfigManager {

    List errorMessages = new ArrayList();

    HashMap unresolvedEntries = new HashMap();

    HashMap resolvedEntries = new HashMap();

    HashSet workingSet = new HashSet();

    /**
        * Gets an iterator for the set of resolved configuration entries.
     */
    public Iterator iterator() {
        return resolvedEntries.values().iterator();
    }

    /**
        * Gets a concatenation of all error messages separated by line break
     * characters "\n".
     */
    public String getErrorMessages() {
        Iterator it = errorMessages.iterator();
        StringBuffer buffer = new StringBuffer();
        while (it.hasNext()) {
            buffer.append((String) it.next());
            if (it.hasNext()) buffer.append("\n");
        }
        return buffer.toString();
    }

    /**
        * Adds a <code>ConfigEntry</code> to the set of resolved configurations.
     */
    public void add(ConfigEntry ce) {
        resolvedEntries.put(ce.name, ce);
    }

    /**
        * Adds a <code>ConfigManager</code> to the set of unresolved configurations.
     *
     * @throws ConfigException if the config name already exists
     */
    public void add(ConfigReader cr) throws ConfigException {
        if (unresolvedEntries.get(cr.name) != null) throw new ConfigException(cr.source + ": Duplicate definition of \"" + cr.name + "\"!");
        cr.generateWarnings(errorMessages);
        unresolvedEntries.put(cr.name, cr);
    }

    /**
        * Finds a specified <code>ConfigReader</code> in the set of unresolved
     * configurations.
     */
    public ConfigReader findConfigReader(String s) {
        return (ConfigReader) unresolvedEntries.get(s);
    }

    /**
        * Finds a specified <code>ConfigEntry</code> in the set of resolved entries.
     *
     * @see #resolveAll()
     */
    public ConfigEntry findConfig(String s) {
        return (ConfigEntry) resolvedEntries.get(s);
    }

    /**
        * Finds a specified <code>ConfigEntry</code>, resolving it from an unresolved
     * entry if necessary and possible.
     */
    public void resolveConfig(String s) throws ConfigException {
        ConfigEntry ce = (ConfigEntry) resolvedEntries.get(s);
        if (ce == null) {
            ConfigReader cr = (ConfigReader) unresolvedEntries.get(s);
            if (cr != null) {
                if (workingSet.contains(cr.name)) throw new CircularConfigException("Circular import dependencies detected: " + cr.name);
                workingSet.add(cr.name);
                try {
                    ce = cr.resolve(this);
                } finally {
                    workingSet.remove(cr.name);
                }
                add(ce);
            }
        }
    }

    public void resolveAll() {
        Iterator it = unresolvedEntries.keySet().iterator();
        while (it.hasNext()) {
            String name = (String) it.next();
            try {
                resolveConfig(name);
            } catch (BrokenConfigException e) {
            } catch (Exception e) {
                errorMessages.add(e.getMessage());
            }
        }
    }

    public void loadConfig(File f) throws IOException {
        if (!f.exists()) {
            errorMessages.add("iwork.manager.core.ConfigManager: config file " + f.getName() + " does not exist");
        }
        LineNumberReader br = null;
        try {
            f = f.getAbsoluteFile();
            System.err.println("[ConfigManager] Loading " + f.getParentFile().getName() + " configuration...");
            br = new LineNumberReader(new FileReader(f));
            String prefix = f.getParent() + File.separator;
            while (br.ready()) {
                ConfigReader cr = new ConfigReader(prefix, f.toString(), br);
                try {
                    add(cr);
                } catch (ConfigException e) {
                    errorMessages.add(e.getMessage());
                }
            }
        } catch (ConfigException e) {
            errorMessages.add(e.getMessage());
        } catch (Exception e) {
            errorMessages.add(f.toString() + ": " + e.getMessage());
        } finally {
            if (br != null) br.close();
        }
    }

    public static String[] resolveQuestionMarks(String array[]) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null) array[i] = resolveQuestionMarks(array[i]);
        }
        return array;
    }

    public static String resolveQuestionMarks(String str) {
        SettingList globalSettings = null;
        int i = str.indexOf("?{");
        boolean globalsChanged = false;
        while (i >= 0) {
            if (globalSettings == null) globalSettings = SettingList.globalSettings();
            int j = str.indexOf("}", i);
            String key = str.substring(i + 2, j);
            String resolvedValue;
            resolvedValue = globalSettings.getValue(key);
            if (resolvedValue == null) {
                resolvedValue = ConfigManager.promptForValue(key);
                globalSettings.setValue(key, resolvedValue);
                globalsChanged = true;
            }
            String newStr = str.substring(0, i) + resolvedValue + str.substring(j + 1);
            str = newStr;
            i = str.indexOf("?{");
        }
        if (globalsChanged) {
            try {
                globalSettings.save();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return str;
    }

    public static String promptForValue(String varString) {
        int indexOfBar = varString.indexOf("|");
        String promptName;
        String input;
        String title = "Choose a Value";
        String msg = "Please choose a value for parameter '";
        if (indexOfBar == -1) {
            promptName = varString;
            msg += promptName + "':";
            input = JOptionPane.showInputDialog(null, msg, title, JOptionPane.QUESTION_MESSAGE);
        } else {
            promptName = varString.substring(0, indexOfBar).trim();
            String defaultValue = varString.substring(indexOfBar + 1).trim();
            msg += promptName + "':";
            input = (String) JOptionPane.showInputDialog(null, msg, title, JOptionPane.QUESTION_MESSAGE, null, null, defaultValue);
        }
        return input;
    }

    public void loadConfig(String filename) throws IOException {
        loadConfig(new File(filename));
    }

    public ConfigManager() {
    }
}
