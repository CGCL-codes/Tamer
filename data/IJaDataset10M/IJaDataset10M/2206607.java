package com.organic.maynard.outliner.scripting.script;

import com.organic.maynard.outliner.guitree.*;
import com.organic.maynard.outliner.*;
import java.util.*;
import java.io.*;
import org.xml.sax.*;
import javax.swing.*;
import javax.swing.table.*;
import com.organic.maynard.xml.XMLTools;

/**
 * @author  $Author: maynardd $
 * @version $Revision: 1.3 $, $Date: 2004/03/13 06:31:31 $
 */
public class ScriptsManagerModel extends AbstractTableModel {

    public static final int STARTUP_SCRIPT = 0;

    public static final int SHUTDOWN_SCRIPT = 1;

    public static final int USER_SCRIPT = 2;

    protected static String STARTUP_SCRIPT_TEXT = GUITreeLoader.reg.getText("startup");

    protected static String SHUTDOWN_SCRIPT_TEXT = GUITreeLoader.reg.getText("shutdown");

    protected static String USER_SCRIPT_TEXT = GUITreeLoader.reg.getText("user");

    protected static String UNKNOWN_SCRIPT_TEXT = GUITreeLoader.reg.getText("unknown");

    private static String TEXT_SCRIPT = GUITreeLoader.reg.getText("script");

    private static String TEXT_ERROR = GUITreeLoader.reg.getText("error");

    private static String TEXT_RUN = GUITreeLoader.reg.getText("run");

    private static String TEXT_EDIT = GUITreeLoader.reg.getText("edit");

    private ArrayList scripts = new ArrayList();

    private static final int SCRIPT_EVENT_COUNT = 2;

    public ScriptsManagerModel() {
    }

    public static void runStartupScripts() {
        if (Outliner.scriptsManager != null) {
            if (Outliner.scriptsManager.model != null) {
                for (int i = 0, limit = Outliner.scriptsManager.model.getSize(); i < limit; i++) {
                    Script script = Outliner.scriptsManager.model.get(i);
                    if (script.isStartupScript()) {
                        runScript(script, STARTUP_SCRIPT);
                    }
                }
            } else {
                System.out.println("Error: ScriptsManagerModel was null");
            }
        } else {
            System.out.println("Error: ScriptsManager was null");
        }
    }

    public static void runShutdownScripts() {
        for (int i = 0, limit = Outliner.scriptsManager.model.getSize(); i < limit; i++) {
            Script script = Outliner.scriptsManager.model.get(i);
            if (script.isShutdownScript()) {
                runScript(script, SHUTDOWN_SCRIPT);
            }
        }
    }

    public static void runScript(Script script, int scriptType) {
        if (scriptType == SHUTDOWN_SCRIPT) {
            try {
                script.process();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            ScriptThread scriptThread = new ScriptThread(script, scriptType);
            scriptThread.start();
        }
    }

    public int getSize() {
        return scripts.size();
    }

    public boolean isNameUnique(String name) {
        for (int i = 0, limit = scripts.size(); i < limit; i++) {
            if (name.equals(get(i).getName())) {
                return false;
            }
        }
        return true;
    }

    public int indexOf(String name) {
        for (int i = 0, limit = scripts.size(); i < limit; i++) {
            Script script = get(i);
            if (script.getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }

    public Script get(int i) {
        return (Script) scripts.get(i);
    }

    public Script get(String name) {
        int i = indexOf(name);
        if (i != -1) {
            return get(i);
        } else {
            return null;
        }
    }

    public int add(Script script) {
        int i, limit;
        for (i = 0, limit = scripts.size(); i < limit; i++) {
            Script scriptTemp = (Script) scripts.get(i);
            if (scriptTemp.getName().compareTo(script.getName()) >= 0) {
                break;
            }
        }
        scripts.add(i, script);
        fireTableRowsInserted(i, i);
        return i;
    }

    public void remove(int i) {
        scripts.remove(i);
        fireTableRowsDeleted(i, i);
    }

    public int remove(String name) {
        int i = indexOf(name);
        if (i != -1) {
            remove(i);
        }
        return i;
    }

    public String getName(int i) {
        return get(i).getName();
    }

    public boolean getIsStartup(int i) {
        return get(i).isStartupScript();
    }

    public void setIsStartup(int i, Boolean b) {
        get(i).setStartupScript(b.booleanValue());
    }

    public void setIsStartup(int i, boolean b) {
        get(i).setStartupScript(b);
    }

    public void setIsStartup(int i, String b) {
        get(i).setStartupScript((new Boolean(b)).booleanValue());
    }

    public boolean getIsShutdown(int i) {
        return get(i).isShutdownScript();
    }

    public void setIsShutdown(int i, Boolean b) {
        get(i).setShutdownScript(b.booleanValue());
    }

    public void setIsShutdown(int i, boolean b) {
        get(i).setShutdownScript(b);
    }

    public void setIsShutdown(int i, String b) {
        get(i).setShutdownScript((new Boolean(b)).booleanValue());
    }

    public static boolean validateUniqueness(String name) {
        if (Outliner.scriptsManager.model.isNameUnique(name)) {
            return true;
        } else {
            return false;
        }
    }

    public String getColumnName(int col) {
        if (col == 0 || col == 1) {
            return "";
        } else if (col == 2) {
            return TEXT_SCRIPT;
        } else if (col == 3) {
            return STARTUP_SCRIPT_TEXT;
        } else if (col == 4) {
            return SHUTDOWN_SCRIPT_TEXT;
        } else {
            return TEXT_ERROR;
        }
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public int getColumnCount() {
        return 3 + SCRIPT_EVENT_COUNT;
    }

    public int getRowCount() {
        return getSize();
    }

    public Object getValueAt(int row, int col) {
        if (col == 0) {
            return TEXT_RUN;
        } else if (col == 1) {
            return TEXT_EDIT;
        } else if (col == 2) {
            return getName(row);
        } else if (col == 3) {
            return new Boolean(getIsStartup(row));
        } else if (col == 4) {
            return new Boolean(getIsShutdown(row));
        } else {
            return TEXT_ERROR;
        }
    }

    public boolean isCellEditable(int row, int col) {
        if (col == 2) {
            return false;
        } else {
            return true;
        }
    }

    public void setValueAt(Object value, int row, int col) {
        if (col == 0 || col == 1 || col == 2) {
        } else if (col == 3) {
            setIsStartup(row, (Boolean) value);
        } else if (col == 4) {
            setIsShutdown(row, (Boolean) value);
        } else {
        }
    }
}
