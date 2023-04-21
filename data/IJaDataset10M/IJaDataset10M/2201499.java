package org.zaval.tools.i18n.translator;

import java.text.MessageFormat;
import java.util.*;
import org.zaval.io.PropertiesFile;

public class BundleSet implements TranslatorConstants {

    static final String BROKEN_FORMAT_SUFFIX = "!broken";

    public static final String FORMAT_SUFFIX = "_FMT";

    static Comparator DEFAULT_COMPARATOR = String.CASE_INSENSITIVE_ORDER;

    private TreeMap items;

    private Vector lng;

    BundleSet() {
        items = new TreeMap(DEFAULT_COMPARATOR);
        lng = new Vector();
    }

    public static void setDefaultComparator(Comparator comparator) {
        DEFAULT_COMPARATOR = comparator;
    }

    void addLanguage(String slng, String desc) {
        if (getLanguage(slng) != null) return;
        LangItem newl = new LangItem(slng, desc);
        lng.addElement(newl);
        correctFileName(newl);
    }

    int getLangCount() {
        return lng.size();
    }

    LangItem getLanguage(int idx) {
        return (LangItem) lng.elementAt(idx);
    }

    LangItem getLanguage(String lng) {
        int j = getLangIndex(lng);
        return j < 0 ? null : getLanguage(j);
    }

    int getLangIndex(String lng) {
        int j, k = getLangCount();
        for (j = 0; j < k; ++j) {
            LangItem lx = getLanguage(j);
            if (lx.getLangId().equals(lng)) return j;
        }
        return -1;
    }

    LangItem[] getLanguageByDescription(String lng) {
        int j, k = getLangCount();
        Vector ask = new Vector();
        for (j = 0; j < k; ++j) {
            LangItem lx = getLanguage(j);
            if (lx.getLangDescription().equals(lng)) ask.addElement(lx);
        }
        if (ask.size() == 0) return null;
        LangItem[] li = new LangItem[k = ask.size()];
        for (j = 0; j < k; ++j) li[j] = (LangItem) ask.elementAt(j);
        return li;
    }

    int getItemCount() {
        return items.size();
    }

    BundleItem getItem(String key) {
        return (BundleItem) items.get(key);
    }

    Iterator iterator() {
        return items.values().iterator();
    }

    void addKey(String key) {
        if (!items.containsKey(key)) items.put(key, new BundleItem(key));
    }

    void removeKey(String key) {
        items.remove(key);
    }

    void removeKeysBeginningWith(String key) {
        Iterator i = items.keySet().iterator();
        while (i.hasNext()) {
            String itemKey = (String) i.next();
            if (itemKey.startsWith(key)) i.remove();
        }
    }

    void updateValue(String key, String lang, String value) {
        BundleItem bi = getItem(key);
        if (bi != null) bi.setTranslation(lang, value);
    }

    private Locale parseLanguage(String suffix) {
        if (suffix == null || suffix.length() == 0) return null;
        int undInd = suffix.indexOf('_');
        String sl = suffix;
        String sc = "";
        if (undInd > 0) {
            sl = suffix.substring(0, undInd);
            sc = suffix.substring(undInd + 1);
        }
        return new Locale(sl, sc);
    }

    void addLanguage(String lng) {
        Locale loc = parseLanguage(lng);
        if (loc != null) {
            String desc = loc.getDisplayLanguage();
            String sCountry = loc.getDisplayCountry();
            if (sCountry != null && sCountry.length() > 0) desc += " (" + sCountry + ")";
            addLanguage(lng, desc);
        }
    }

    private String makeLine(String key, String val) {
        if (val == null) return null;
        int capacity = key.length() + val.length() + 2;
        StringBuffer sb = new StringBuffer(capacity);
        sb.append(key);
        sb.append('=');
        sb.append(val);
        return sb.toString();
    }

    Vector store(String lng) {
        LangItem lang = getLanguage(lng);
        Vector lines = new Vector();
        lines.addElement("# Java Resource Bundle");
        lines.addElement("# Modified by Zaval JRC Editor (C) Zaval CE Group");
        lines.addElement("# http://www.zaval.org/products/jrc-editor/");
        lines.addElement("#");
        lines.addElement("");
        Iterator i = items.values().iterator();
        while (i.hasNext()) {
            BundleItem bi = (BundleItem) i.next();
            if (bi.getComment() != null) lines.addElement("#" + bi.getComment());
            if (bi.getTranslation(lng) == null) continue;
            lines.addElement(makeLine(bi.getId(), bi.getTranslation(lng)));
        }
        return lines;
    }

    private void correctFileName(LangItem lang) {
        if (getLangCount() < 1) return;
        LangItem lan0 = getLanguage(0);
        if (lan0 == lang) return;
        if (lang.getLangFile() != null) return;
        if (lan0.getLangFile() == null) return;
        String base = lan0.getLangFile();
        int j = base.lastIndexOf('.');
        if (j < 0) return;
        base = base.substring(0, j) + "_" + lang.getLangId() + base.substring(j);
        lang.setLangFile(base);
    }

    public Properties getProperties(String lng) {
        return getProperties(lng, null);
    }

    public Properties getProperties(String lng, BundleSet defaultTranslations) {
        Properties p = new PropertiesFile();
        Iterator i = items.values().iterator();
        while (i.hasNext()) {
            BundleItem bi = (BundleItem) i.next();
            String value = bi.getTranslation(lng);
            if (value != null && value.trim().length() != 0 && !isDefaultValue(bi, defaultTranslations, lng)) {
                String key = bi.getId();
                key = maybeAddBrokenSuffix(key, value);
                p.setProperty(key, value);
            }
        }
        return p;
    }

    private boolean isDefaultValue(BundleItem bi1, BundleSet defaultTranslations, String lng) {
        if (defaultTranslations == null) return false;
        BundleItem bi2 = defaultTranslations.getItem(bi1.getId());
        if (bi2 == null) return false;
        String value1 = bi1.getTranslation(lng);
        String value2 = bi2.getTranslation(lng);
        if (value1 != null && value1.equals(value2)) return true;
        return false;
    }

    public void putProperties(String lng, String prefix, Properties p) {
        Iterator iter = p.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry element = (Map.Entry) iter.next();
            String dname = (String) element.getKey();
            String value = (String) element.getValue();
            if (prefix != null) dname = prefix + TranslatorConstants.KEY_SEPARATOR + dname;
            dname = maybeRemoveBrokenSuffix(dname);
            addKey(dname);
            updateValue(dname, lng, value);
            getItem(dname).setComment(null);
        }
    }

    private String maybeRemoveBrokenSuffix(String dname) {
        if (dname.endsWith(BROKEN_FORMAT_SUFFIX)) {
            int len = dname.length() - BROKEN_FORMAT_SUFFIX.length();
            dname = dname.substring(0, len);
        }
        return dname;
    }

    private String maybeAddBrokenSuffix(String key, String val) {
        if (key.endsWith(FORMAT_SUFFIX)) try {
            new MessageFormat(val);
        } catch (Exception e) {
            key = key + BROKEN_FORMAT_SUFFIX;
        }
        return key;
    }
}
