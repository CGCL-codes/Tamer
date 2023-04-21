package com.frinika.project.scripting;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Stores a script which can be executed by the FrinikaScriptEngine and 
 * loaded/saved by ScriptingDialog.
 * 
 * @see com.frinika.project.scripting.FrinikaScriptEngine
 * @see com.frinika.project.scripting.gui.ScriptingDialog
 * @author Jens Gulden
 */
public class DefaultFrinikaScript implements FrinikaScript, Serializable, Comparable {

    private static final long serialVersionUID = 1L;

    public static final String INITIAL_NAME = "untitled";

    int language = LANGUAGE_JAVASCRIPT;

    String source;

    String filename;

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public String getName() {
        String filename = getFilename();
        if (filename != null) {
            int slashpos = filename.lastIndexOf(File.separatorChar);
            return filename.substring(slashpos + 1);
        } else {
            return INITIAL_NAME;
        }
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
        in.defaultReadObject();
        if (filename != null) {
            File f = new File(filename);
            if (f.exists()) {
                try {
                    String s = FrinikaScriptingEngine.loadString(f);
                    this.source = s;
                } catch (IOException ioe) {
                }
            }
        }
    }

    public int compareTo(Object o) {
        if (!(o instanceof FrinikaScript)) {
            return 1;
        } else {
            return this.getName().compareTo(((FrinikaScript) o).getName());
        }
    }

    private void writeObject(ObjectOutputStream out) throws ClassNotFoundException, IOException {
        out.defaultWriteObject();
    }
}
