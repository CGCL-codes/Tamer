package edu.whitman.halfway.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.*;
import org.apache.log4j.Logger;

public class TextDescriptionManager {

    protected static Logger log = Logger.getLogger(TextDescriptionManager.class.getName());

    protected static char leftMarker = '<';

    protected static char rightMarker = '>';

    protected File descriptionFile = null;

    protected HashMap descMap;

    public TextDescriptionManager(File descriptionFile) {
        this.descriptionFile = descriptionFile;
        descMap = new HashMap();
        if (log.isDebugEnabled()) log.debug("Reading description info from " + descriptionFile);
        if ((descriptionFile == null) || (!descriptionFile.exists())) return;
        int iletter;
        char letter;
        try {
            BufferedReader in = new BufferedReader(new FileReader(descriptionFile));
            while ((iletter = in.read()) != -1) {
                letter = (char) iletter;
                if (!Character.isWhitespace(letter) && (letter == leftMarker)) {
                    StringBuffer keyName = new StringBuffer();
                    while ((iletter = in.read()) != -1 && ((letter = (char) iletter) != rightMarker)) {
                        keyName.append(letter);
                    }
                    String key = keyName.toString();
                    String endKey = "/" + key;
                    boolean keepreading = true;
                    StringBuffer keyValue = new StringBuffer();
                    keyName = new StringBuffer();
                    while ((iletter = in.read()) != -1 && keepreading) {
                        letter = (char) iletter;
                        if (letter == leftMarker) {
                            while ((iletter = in.read()) != -1 && ((letter = (char) iletter) != rightMarker)) {
                                keyName.append(letter);
                            }
                            String testKey = keyName.toString();
                            if (testKey.trim().equals(endKey.trim())) {
                                String value = keyValue.toString().trim();
                                if (!value.equals("")) {
                                    descMap.put(key, value);
                                }
                                keepreading = false;
                            } else {
                                keyValue.append("<" + testKey + ">");
                                keyName = new StringBuffer();
                            }
                        } else {
                            keyValue.append(letter);
                        }
                    }
                }
            }
            in.close();
            if (log.isDebugEnabled()) log.debug("Current contents of description map " + descMap);
        } catch (Exception e) {
            log.error("Error Opening File " + descriptionFile.toString(), e);
        }
    }

    public Map getMap() {
        return descMap;
    }

    /** renames the field, so now getMap().get(oldName) will return
     * null, and getMap().get(newName) will return the value that used
     * to be associated with oldName.  That is, this method changes
     * the key of a given value.*/
    public void renameField(String oldName, String newName) {
        if (descMap.containsKey(oldName)) {
            if (descMap.containsKey(newName)) {
                throw new IllegalArgumentException(this + "\n\tDescription contains key " + newName + ", can't rename " + oldName);
            }
            Object value = descMap.get(oldName);
            descMap.remove(oldName);
            descMap.put(newName, value);
            assert descMap.containsKey(oldName) == false : "Still contains old key " + oldName;
            assert descMap.containsKey(newName) : "Doesn't contain new key " + newName;
        }
    }

    /** saveFile.  Default implementation is trivial, but superclasses
     * can override to provide a particular ordering in which to to
     * print keys, or to provide output strings different than the
     * toString() methods of the objects in descMap  */
    protected void saveFile() {
        List keys = new LinkedList(descMap.keySet());
        saveFromMap(keys, descMap);
    }

    public void saveFromMap(List keyList, Map printMap) {
        Map descMap = null;
        if (keyList.size() == 0) {
            log.info("No keys to print, deleting File:" + descriptionFile);
            if (descriptionFile != null && descriptionFile.exists()) {
                descriptionFile.delete();
            }
            return;
        }
        try {
            Writer writer = new BufferedWriter(new FileWriter(descriptionFile));
            write(writer, keyList, printMap);
        } catch (Exception e) {
            log.error("Error Writing File" + descriptionFile.toString(), e);
        }
    }

    public File getFile() {
        return descriptionFile;
    }

    public void write(Writer writer, List keyList, Map printMap) {
        if (printMap == null) {
            printMap = descMap;
        }
        if (keyList == null) {
            keyList = new ArrayList(printMap.keySet());
        }
        try {
            PrintWriter out = new PrintWriter(writer);
            Iterator keys = keyList.iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                Object valObj = printMap.get(key);
                if (valObj != null) {
                    String val = valObj.toString();
                    if (!val.trim().equals("")) {
                        out.println("<" + key + ">");
                        out.println(val);
                        out.println("</" + key + ">");
                        out.println();
                    }
                } else {
                    log.warn("Got null value in printMap for key " + key);
                }
            }
            out.close();
        } catch (Exception e) {
            log.error("Error Writing File" + descriptionFile.toString(), e);
        }
    }

    /** get a key from the Map */
    public Object getData(String key) {
        return descMap.get(key);
    }

    /** set the key value in the Map */
    public void setData(String key, Object value) {
        descMap.put(key, value);
    }

    public String toString() {
        return "TextDescriptionManager[" + descriptionFile + "]";
    }
}
