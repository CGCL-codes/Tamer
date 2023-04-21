package com.simpledata.bc.datamodel;

import java.io.Serializable;
import java.util.*;

public class TString implements Serializable, Copiable {

    /** contains the language list in prefered order **/
    protected static ArrayList langs;

    /** contains translation objects **/
    protected HashMap translations;

    /** the object that uses this TString **/
    protected Named container;

    /** init langs vector and set default Language to English **/
    static {
        TString.langs = new ArrayList();
        TString.setLang("en");
    }

    /**
	* Constructor, The String in actual language (TSTring.getLang(0))
	* @param translation the string in actual language (TString.getLang(0))
	*/
    public TString(String langCode, String translation) {
        this.translations = new HashMap();
        addTranslation(langCode, translation);
    }

    /**
	* Constructor, The String in actual language (TSTring.getLang(0))
	* @param container is the object in which this String is used
	* @param translation the string in actual language (TString.getLang(0))
	*/
    public TString(Named container, String translation) {
        this.translations = new HashMap();
        this.container = container;
        setActualTranslation(translation);
    }

    /**
	* return the translation in actual language or if not avalaible 
	* in the first prefered available Language
	*/
    public String toString() {
        return getTranslation(getBestLang());
    }

    /**
	* return the best fitted language in actual context
	* @return a langCode
	*/
    public String getBestLang() {
        for (int i = 0; i < getLangsSize(); i++) {
            if (translations.containsKey(getLang(i))) {
                return getLang(i);
            }
        }
        if (translations.containsKey("en")) {
            return "en";
        }
        String[] keys = (String[]) translations.keySet().toArray(new String[0]);
        if (keys.length > 0) return keys[0];
        return "";
    }

    /**
	* return the translation of this TSTring()
	*/
    private String getTranslation(String langCode) {
        if (translations.get(langCode) == null) {
            return "";
        }
        return translations.get(langCode).toString();
    }

    /**
	* set the translation of this TString for this langCode
	* for the default Language
	*/
    public void setActualTranslation(String translation) {
        addTranslation(getLang(0), translation);
    }

    /**
	* set the translation of this TString for this langCode
	*/
    public void addTranslation(String langCode, String translation) {
        translations.put(langCode, translation);
    }

    /**
	* set the language of TStrings to this language
	* @param langCode "en" "fr" "de"
	*/
    public static void setLang(String langCode) {
        TString.setLang(langCode, 0);
    }

    /**
	* set position of this langCode 0 is prefered 
	*/
    public static void setLang(String langCode, int position) {
        langs.remove(langCode);
        if (position < 0) position = 0;
        if (position > getLangsSize()) position = getLangsSize();
        langs.add(position, langCode);
    }

    /**
	* set the prefered languages in order. Erasing all previous languages
	* @param languages is a ArrayList of String "en" "fr" "de" ....
	*/
    public static void setLangs(ArrayList languages) {
        TString.langs = languages;
    }

    /**
	* get the language list size
	*/
    public static int getLangsSize() {
        return TString.langs.size();
    }

    /**
	* get the language list ArrayList denoting the order of prefered languages
	*/
    public static ArrayList getLangs() {
        return langs;
    }

    /**
	* get the language at a specific position.
	* return null if does not exist
	*/
    public static String getLang(int position) {
        if (position < 0 || position > getLangsSize()) return null;
        return langs.get(position).toString();
    }

    /**
	 * get a Copy of this TString.. note!! the copy is not attached to any
	 * named object. (Container is null!!)
	 * @see com.simpledata.bc.datamodel.Copiable#copy()
	 */
    public TString copyTS() {
        TString result = new TString();
        HashMap tempTrans = new HashMap();
        Iterator i = getTranslations().entrySet().iterator();
        Map.Entry me = null;
        while (i.hasNext()) {
            me = (Map.Entry) i.next();
            tempTrans.put(me.getKey().toString(), me.getValue().toString());
        }
        result.setTranslations(tempTrans);
        return result;
    }

    /**
	 * get a Copy of this TString.. note!! the copy is not attached to any
	 * named object. (Container is null!!)
	 * @see com.simpledata.bc.datamodel.Copiable#copy()
	 */
    public Copiable copy() {
        return copyTS();
    }

    /** XML CONSTRUCTOR DO NOT USE **/
    public TString() {
    }

    /**
	 * XML
	 */
    public Named getContainer() {
        return container;
    }

    /**
	 * XML
	 */
    public HashMap getTranslations() {
        return translations;
    }

    /**
	 * XML
	 */
    public void setContainer(Named named) {
        container = named;
    }

    /**
	 * XML
	 */
    public void setTranslations(HashMap map) {
        translations = map;
    }
}
