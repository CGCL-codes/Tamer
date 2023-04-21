package cej.jhebrew.words;

import java.util.Hashtable;
import java.util.Set;
import java.util.TreeSet;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import cej.jhebrew.voc.Word;

/**
 * @author cjung
 * Build a dictionary
 */
public class DictionaryBuilder {

    /**
     * @author cjung
     * Type of language
     */
    public enum Language {

        /** Hebrew language */
        HEBREW, /** French language */
        FRENCH
    }

    private Hashtable<String, Set<String>> hebrewToLanguage;

    private Hashtable<String, Set<String>> languageToHebrew;

    protected static final String WORD_ELEMENT = "mot";

    protected static final String LANGUAGE_ATTRIBUTE = "f";

    protected static final String HEBREU_ATTRIBUTE = "h";

    /**
     * @param roots the root Xml elements
     */
    public DictionaryBuilder(Element... roots) {
        hebrewToLanguage = new Hashtable<String, Set<String>>();
        languageToHebrew = new Hashtable<String, Set<String>>();
        if (roots != null) {
            for (Element e : roots) {
                buildDictionaries(e);
            }
        }
    }

    /**
     * @param racine
     */
    private void buildDictionaries(Element racine) {
        NodeList mots = racine.getElementsByTagName(WORD_ELEMENT);
        for (int i = 0; i < mots.getLength(); i++) {
            Element e = (Element) mots.item(i);
            Word frenchWord = buildWord(e, Language.FRENCH);
            Word hebrewWord = buildWord(e, Language.HEBREW);
            addEntry(frenchWord, languageToHebrew);
            addEntry(hebrewWord, hebrewToLanguage);
        }
    }

    private void addEntry(Word word, Hashtable<String, Set<String>> dictionary) {
        putInHashtable(dictionary, word.getKey(), word.getTranslations());
        for (String translation : word.getSynonyms()) {
            putInHashtable(dictionary, translation, word.getTranslations());
        }
    }

    private void putInHashtable(Hashtable<String, Set<String>> dictionary, String key, Set<String> translations) {
        Set<String> existingTranslations = dictionary.get(key.toLowerCase());
        if (existingTranslations == null) {
            dictionary.put(key.toLowerCase(), translations);
        } else {
            existingTranslations.addAll(translations);
        }
    }

    protected Word buildWord(Element e, Language language) {
        String mainAttribute;
        String alterAttribute;
        switch(language) {
            case FRENCH:
                mainAttribute = LANGUAGE_ATTRIBUTE;
                alterAttribute = HEBREU_ATTRIBUTE;
                break;
            default:
                mainAttribute = HEBREU_ATTRIBUTE;
                alterAttribute = LANGUAGE_ATTRIBUTE;
                break;
        }
        String key = e.getAttribute(mainAttribute);
        String firstTranslation = e.getAttribute(alterAttribute);
        Set<String> setTranslations = new TreeSet<String>();
        setTranslations.add(firstTranslation);
        addSecundaryAttributes(e, setTranslations, alterAttribute);
        Set<String> setSynonyms = new TreeSet<String>();
        addSecundaryAttributes(e, setSynonyms, mainAttribute);
        return new Word(key, setTranslations, setSynonyms);
    }

    protected void addSecundaryAttributes(Element e, Set<String> set, String attributeName) {
        int idx = 2;
        String bis;
        while ((bis = e.getAttribute(attributeName + idx++)) != "") {
            set.add(bis);
        }
    }

    /**
     * @return table with Hebrew keys and language translations
     */
    public Hashtable<String, Set<String>> getHebrewToLanguage() {
        return hebrewToLanguage;
    }

    /**
     * @return table with language keys and Hebrew translations
     */
    public Hashtable<String, Set<String>> getLanguageToHebrew() {
        return languageToHebrew;
    }
}
