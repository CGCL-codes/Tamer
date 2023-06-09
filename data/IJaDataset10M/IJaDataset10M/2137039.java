package opennlp.ccg.lexicon;

import org.jdom.*;
import opennlp.ccg.grammar.Grammar;

/**
 * A data structure for morphological entries.
 *
 * @author      Jason Baldridge
 * @author      Gann Bierner
 * @author      Michael White
 * @version     $Revision: 1.11 $, $Date: 2009/10/17 20:46:20 $
 */
public class MorphItem {

    private static final String[] emptyStringArray = new String[0];

    private Word surfaceWord;

    private Word word;

    private Word coartIndexingWord = null;

    private String[] macros = emptyStringArray;

    private String[] excluded = emptyStringArray;

    private boolean coart = false;

    /** Constructor. */
    public MorphItem() {
    }

    ;

    /** Constructor from XML element. */
    public MorphItem(Element e) {
        String coartString = e.getAttributeValue("coart");
        if ("true".equals(coartString)) coart = true;
        String wordString = e.getAttributeValue("word");
        boolean strictFactors = coart;
        Word tokenizedWord = Grammar.theGrammar.lexicon.tokenizer.parseToken(wordString, strictFactors);
        surfaceWord = Word.createSurfaceWord(tokenizedWord);
        String stem = e.getAttributeValue("stem");
        if (stem == null) stem = surfaceWord.getForm();
        String POS = e.getAttributeValue("pos");
        String supertag = null;
        String semClass = e.getAttributeValue("class");
        word = Word.createFullWord(surfaceWord, stem, POS, supertag, semClass);
        String macrosString = e.getAttributeValue("macros");
        if (macrosString != null) {
            macros = macrosString.split("\\s+");
        }
        String excludedString = e.getAttributeValue("excluded");
        if (excludedString != null) {
            excluded = excludedString.split("\\s+");
        }
        if (coart) {
            String indexAttr = wordString.substring(0, wordString.indexOf("-"));
            String indexVal = surfaceWord.getVal(indexAttr);
            coartIndexingWord = Word.createWord(indexAttr, indexVal);
        }
    }

    /** Returns whether the name, qualified name or family name of the given entries item is in the excluded list. */
    public boolean excluded(EntriesItem eItem) {
        if (excluded.length == 0) return false;
        for (int i = 0; i < excluded.length; i++) {
            if (eItem.getName().equals(excluded[i])) return true;
            if (eItem.getQualifiedName().equals(excluded[i])) return true;
            if (eItem.getFamilyName().equals(excluded[i])) return true;
        }
        return false;
    }

    /** Returns the full word. */
    public Word getWord() {
        return word;
    }

    /** Returns the surface word (without the stem, POS and semantic class). */
    public Word getSurfaceWord() {
        return surfaceWord;
    }

    /** Returns the macro names. */
    public String[] getMacros() {
        return macros;
    }

    /** Returns the names of the excluded entries. */
    public String[] getExcluded() {
        return excluded;
    }

    /** Returns whether the morph item is a coarticulation, eg a pitch accent. */
    public boolean isCoart() {
        return coart;
    }

    /** Returns the word for indexing this coarticulation (or null if not a coarticulation). */
    public Word getCoartIndexingWord() {
        return coartIndexingWord;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append('[');
        for (int i = 0; i < macros.length; i++) {
            sb.append(macros[i]);
            if (i < macros.length - 1) sb.append(',');
        }
        sb.append(']');
        return "{" + word + " => " + sb + "}";
    }
}
