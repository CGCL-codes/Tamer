package au.songdi.javapc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * This class is for context management. It save information for processing.
 * This class is singleton.
 * 
 * @author Di SONG
 * @version 0.1
 */
public class ContextManager {

    private static ContextManager instance = new ContextManager();

    private HashMap map;

    private List includeList;

    private Stack backup;

    private String namespaceOfCurrentFile;

    private String dest;

    private boolean export;

    private String comment = "//";

    public static ContextManager getContext() {
        return instance;
    }

    private ContextManager() {
        map = new HashMap(128);
        includeList = new ArrayList(128);
        backup = new Stack();
    }

    /**
	 * This method is called for adding a global variable.
	 * 
	 * @param key
	 *            the name of variable
	 * @param value
	 *            the value of variable
	 */
    public void addDefineGlobalValue(String key, String value) {
        map.put("global." + key, value);
    }

    /**
	 * This method is called for adding a normal variable.
	 * 
	 * @param key
	 *            the name of variable
	 * @param value
	 *            the value of variable
	 */
    public void addDefinelValue(String key, String value) {
        StringBuffer sb = new StringBuffer(128);
        sb.append(this.namespaceOfCurrentFile);
        sb.append(".");
        sb.append(key);
        key = sb.toString();
        map.put(key, value);
    }

    /**
	 * This method is called for getting a variable. First, it search in noraml
	 * variables with the current namespace. If not find, go to search in global
	 * variables.
	 * 
	 * @param key
	 *            the name of variable
	 * @return the value of the key
	 */
    public String getDefineValue(String key) {
        StringBuffer sb = new StringBuffer(128);
        sb.append(this.namespaceOfCurrentFile);
        sb.append(".");
        sb.append(key);
        String tmp = sb.toString();
        if (map.containsKey(tmp)) return (String) map.get(tmp); else if (map.containsKey("global." + key)) return (String) map.get("global." + key); else return null;
    }

    /**
	 * Set the absolute path of the current processing file
	 * 
	 * @param file
	 *            the String of the absolute path of the current processing file
	 */
    public void setNameSpaceOfCurrentFile(String file) {
        this.namespaceOfCurrentFile = file;
    }

    /**
	 * Get the absolute path of the current processing file
	 * 
	 * @return file
	 *            the String of the absolute path of the current processing file
	 */
    public String getNameSpaceOfCurrentFile() {
        return this.namespaceOfCurrentFile;
    }

    /**
	 * Backup the data of the current context. Before processing a include file,
	 * this method need to be done.
	 */
    public void backupContext() {
        backup.push(this.namespaceOfCurrentFile);
    }

    /**
	 * Backup the data of the current context. After processing a include file,
	 * this method need to be done.
	 *
	 */
    public void restoreContext() {
        this.namespaceOfCurrentFile = (String) backup.pop();
    }

    /**
	 * If a file already processed, this file need to be marked with this method
	 * 
	 * @param file
	 *            the String of the absolute path of a processed file
	 */
    public void addIncludeFile(String file) {
        this.includeList.add(file);
    }

    /**
	 * Judge a file whether processed
	 * 
	 * @param file
	 *            the String of the absolute path of a processed file
	 * @return boolean If processed, return true, or false
	 */
    public boolean exist(String file) {
        return this.includeList.contains(file);
    }

    /**
	 * Set the destination directory
	 * 
	 * @param dest
	 *            the String of the absolute path of the destination directory.
	 */
    public void setDestPath(String dest) {
        this.dest = dest;
    }

    /**
	 * Get the destination directory
	 *           
	 * @return String
	 *  the String of the absolute path of the destination directory.
	 */
    public String getDestPath() {
        return this.dest;
    }

    /**
	 * Set the export parameter
	 * 
	 * @param export
	 *            the boolean of export
	 */
    public void setExport(boolean export) {
        this.export = export;
    }

    /**
	 * Get the export parameter
	 *           
	 * @return boolean
	 *  If export, return true, or false.
	 */
    public boolean isExport() {
        return this.export;
    }

    /**
	 * Set the mark string of comment
	 *           
	 * @param comment
	 *            the string of comment mark
	 */
    public void setCommentMark(String comment) {
        this.comment = comment;
    }

    /**
	 * Get the export parameter
	 *           
	 * @return boolean
	 *  If export, return true, or false.
	 */
    public String getCommentMark() {
        return this.comment;
    }

    /**
	 * ONLY for GUI,before it does a process, this method need to be done first.
	 * 
	 */
    public void renew() {
        map.clear();
        includeList.clear();
        backup.clear();
        namespaceOfCurrentFile = null;
        dest = null;
        export = false;
        comment = "//";
    }

    /**
	 * For testing, show all data of the context
	 * 
	 */
    public void showAllValues() {
        System.out.println("/****************** define values ********************/");
        Set set = map.entrySet();
        Iterator it = set.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            System.out.println("Key = " + entry.getKey() + " Value = " + entry.getValue());
        }
        System.out.println("\r\n/****************** include files ********************/");
        it = includeList.iterator();
        while (it.hasNext()) System.out.println(it.next());
        System.out.println("\r\n/****************** NameSpace of current file ********************/");
        System.out.println(this.namespaceOfCurrentFile);
    }
}
