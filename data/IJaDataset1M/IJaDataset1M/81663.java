package org.formaria.editor.langed;

import org.formaria.util.TextDefaults;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

/**
  * EdLanguage
  * Extends the basic language class to provide write functionality.
 * <p> Copyright (c) Formaria Ltd., 2001-2006, This software is licensed under
 * the GNU Public License (GPL), please see license.txt for more details. If
 * you make commercial use of this software you must purchase a commercial
 * license from Formaria.</p>
 * <p> $Revision: 1.11 $</p>
  * @author Luan O'Carroll
  */
public class EdLanguage extends Language {

    public EdLanguage(EdLangMgr lm) {
        langMgr = lm;
        langName = new String("<>");
    }

    /**
      *  Gets the language name e.g. "English"
      */
    public String getLangName() {
        return langName;
    }

    /**
      *  Sets the language name e.g. "English"
      * @param name The new language name.
      */
    public void setLangName(String name) {
        langName = name;
    }

    /**
      *  Gets the language country code e.g. "IRL"
      */
    public String getLangCode() {
        return langCode;
    }

    /**
      *  Sets the language country code e.g. "IRL"
      * @param cc The new language code.
      */
    public void setLangCode(String cc) {
        langCode = cc;
    }

    public String getStringAt(int idx) {
        int numStrings = langStrings.size();
        if (idx > numStrings) System.out.println("Invalid string index");
        return ((LangItem) langStrings.elementAt(idx)).langStr;
    }

    public Object getElementAt(int idx) {
        int numStrings = langStrings.size();
        if (idx >= numStrings) return null;
        return ((LangItem) langStrings.elementAt(idx));
    }

    public int getNumStrings() {
        return langStrings.size();
    }

    public void read(Vector translations, int field, boolean bSubstrings) {
        char buffer[] = new char[1024];
        int numItems = translations.size();
    }

    public int getSize() {
        return langStrings.size();
    }

    public String getString(int id) {
        int numStrings = langStrings.size();
        for (int i = 0; i < numStrings; i++) {
            LangItem li = (LangItem) langStrings.elementAt(i);
            if (li.id == id) return li.langStr;
        }
        return "";
    }

    LangItem getLangItem(int id) {
        int numStrings = langStrings.size();
        for (int i = 0; i < numStrings; i++) {
            LangItem li = (LangItem) langStrings.elementAt(i);
            if (li.id == id) return li;
        }
        return null;
    }

    /**
    * Looks up and returns a string.
    */
    public int findString(String key) {
        int numStrings = langStrings.size();
        for (int i = 0; i < numStrings; i++) {
            LangItem li = (LangItem) langStrings.elementAt(i);
            if (li.keyStr.equals(key)) return li.id; else if (li.langStr.equals(key)) return li.id;
        }
        return -1;
    }

    public int getStringId(int idx) {
        int numStrings = langStrings.size();
        if (idx >= numStrings) {
            System.out.println("Invalid string index");
            return numStrings - 1;
        }
        return ((LangItem) langStrings.elementAt(idx)).id;
    }

    public int getStringIndex(int idx) {
        int numStrings = langStrings.size();
        for (int i = 0; i < numStrings; i++) {
            if (((LangItem) langStrings.elementAt(i)).id == idx) return i;
        }
        return -1;
    }

    /**
     * Looks up and returns a string.
     * @param newKeyStr the new language key
     * @param newValueStr the new language value
     */
    public void setString(int id, String newKeyStr, String newValueStr) {
        int numStrings = langStrings.size();
        for (int i = 0; i < numStrings; i++) {
            if (((LangItem) langStrings.elementAt(i)).id == id) {
                ((LangItem) langStrings.elementAt(i)).setLangStr(newValueStr);
                return;
            }
        }
        addString(id, newKeyStr, newValueStr);
    }

    /**
      * Looks up and returns a string.
      */
    public void setKeyString(int id, String newStr) {
        int numStrings = langStrings.size();
        for (int i = 0; i < numStrings; i++) {
            if (((LangItem) langStrings.elementAt(i)).id == id) {
                ((LangItem) langStrings.elementAt(i)).setKeyString(newStr);
                return;
            }
        }
    }

    /**
     * Add a new string to the language.
     * @param id the id of this new language string
     * @param newKeyStr the new langauge key
     * @param newValueStr the new langauge value
     */
    public int addString(int id, String newKeyStr, String newValueStr) {
        int numStrings = langStrings.size();
        langStrings.ensureCapacity(++numStrings);
        LangItem li = new LangItem();
        if (id >= 0) li.id = id; else li.id = id = getMaxId() + 1;
        li.keyStr = newKeyStr;
        li.langStr = newValueStr;
        li.status = li.NEW_ITEM;
        langStrings.addElement(li);
        return getStringIndex(id);
    }

    /**
     * Add a new string to the language.
     * @param newKeyStr the new langauge key
     * @param newValueStr the new langauge value
     */
    public int addString(String newKeyStr, String newValueStr) {
        int numStrings = langStrings.size();
        int maxId = getMaxId();
        langStrings.ensureCapacity(++numStrings);
        LangItem li = new LangItem();
        li.id = ++maxId;
        li.keyStr = newKeyStr;
        li.langStr = newValueStr;
        li.status = li.NEW_ITEM;
        langStrings.addElement(li);
        return maxId;
    }

    /**
     * Gets the maximum string ID in use.
     * @return
     */
    public int getMaxId() {
        int numStrings = langStrings.size();
        int maxId = 0;
        for (int iItem = 0; iItem < numStrings; iItem++) {
            int srcId = ((LangItem) langStrings.elementAt(iItem)).id;
            if (srcId > maxId) maxId = srcId;
        }
        return maxId;
    }

    public void removeString(int idx) {
        int numStrings = langStrings.size();
        if (idx >= numStrings) return;
        langStrings.removeElementAt(idx);
    }

    private String insertTags(String original) {
        return original.replace(TextDefaults.CRLF_PAIR.subSequence(0, TextDefaults.CRLF_PAIR.length()), TextDefaults.CRLF_PAIR_ENCODING.subSequence(0, TextDefaults.CRLF_PAIR_ENCODING.length()));
    }

    private String replaceTags(String original) {
        return original.replace(TextDefaults.CRLF_PAIR_ENCODING.subSequence(0, TextDefaults.CRLF_PAIR_ENCODING.length()), TextDefaults.CRLF_PAIR.subSequence(0, TextDefaults.CRLF_PAIR.length()));
    }

    public void saveProperties(String newFile, String encoding) throws IOException {
        BufferedWriter bw;
        if (encoding == null) bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFile))); else bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFile), encoding));
        writeln(bw, "#" + new Date().toString());
        int numItems = langStrings.size();
        for (int i = 0; i < numItems; i++) {
            LangItem li = ((LangItem) langStrings.elementAt(i));
            writeln(bw, langMgr.getKey(li.id) + "=" + li.langStr);
        }
        bw.flush();
        bw.close();
    }

    private static void writeln(BufferedWriter bw, String s) throws IOException {
        bw.write(s);
        bw.newLine();
    }

    public void sort(Comparator c) {
        Object[] langItems = langStrings.toArray();
        int size = langItems.length;
        Arrays.sort(langItems, c);
        langStrings = new Vector();
        for (int i = 0; i < size; i++) langStrings.add(langItems[i]);
    }

    private String langName;

    private String langCode;

    private Vector langStrings = new Vector();

    private EdLangMgr langMgr;
}
