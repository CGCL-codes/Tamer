package com.swabunga.spell.engine;

import com.swabunga.util.StringUtility;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * A Generic implementation of a transformator takes an 
 * <a href="http://aspell.net/man-html/Phonetic-Code.html">
 * aspell phonetics file</a> and constructs some sort of transformation 
 * table using the inner class TransformationRule.
 * </p>
 * Basically, each transformation rule represent a line in the phonetic file.
 * One line contains two groups of characters separated by white space(s).
 * The first group is the <em>match expression</em>. 
 * The <em>match expression</em> describe letters to associate with a syllable.
 * The second group is the <em>replacement expression</em> giving the phonetic 
 * equivalent of the <em>match expression</em>.
 *
 * @see SpellDictionaryASpell SpellDictionaryASpell for information on getting
 * phonetic files for aspell.
 *
 * @author Robert Gustavsson (robert@lindesign.se)
 */
public class GenericTransformator implements Transformator {

    /**
   * This replace list is used if no phonetic file is supplied or it doesn't
   * contain the alphabet.
   */
    private static final char[] defaultEnglishAlphabet = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

    /**
   * The alphabet start marker.
   * @see GenericTransformator#KEYWORD_ALPHBET KEYWORD_ALPHBET
   */
    public static final char ALPHABET_START = '[';

    /**
   * The alphabet end marker.
   * @see GenericTransformator#KEYWORD_ALPHBET KEYWORD_ALPHBET
   */
    public static final char ALPHABET_END = ']';

    /**
   * Phonetic file keyword indicating that a different alphabet is used 
   * for this language. The keyword must be followed an
   * {@link GenericTransformator#ALPHABET_START ALPHABET_START} marker, 
   * a list of characters defining the alphabet and a
   * {@link GenericTransformator#ALPHABET_END ALPHABET_END} marker.
   */
    public static final String KEYWORD_ALPHBET = "alphabet";

    /**
   * Phonetic file lines starting with the keywords are skipped. 
   * The key words are: version, followup, collapse_result.
   * Comments, starting with '#', are also skipped to the end of line.
   */
    static final String[] IGNORED_KEYWORDS = { "version", "followup", "collapse_result" };

    /**
   * Start a group of characters which can be appended to the match expression
   * of the phonetic file.
   */
    public static final char STARTMULTI = '(';

    /**
   * End a group of characters which can be appended to the match expression
   * of the phonetic file.
   */
    public static final char ENDMULTI = ')';

    /**
   * During phonetic transformation of a word each numeric character is
   * replaced by this DIGITCODE.
   */
    public static final String DIGITCODE = "0";

    /**
   * Phonetic file character code indicating that the replace expression
   * is empty.
   */
    public static final String REPLACEVOID = "_";

    private TransformationRule[] ruleArray;

    private char[] alphabetString = defaultEnglishAlphabet;

    /**
   * Construct a transformation table from the phonetic file
   * @param phonetic the phonetic file as specified in aspell
   * @throws java.io.IOException indicates a problem while reading
   * the phonetic file
   */
    public GenericTransformator(File phonetic) throws IOException {
        buildRules(new BufferedReader(new FileReader(phonetic)));
        alphabetString = washAlphabetIntoReplaceList(getReplaceList());
    }

    /**
   * Construct a transformation table from the phonetic file
   * @param phonetic the phonetic file as specified in aspell
   * @param encoding the character set required
   * @throws java.io.IOException indicates a problem while reading
   * the phonetic file
   */
    public GenericTransformator(File phonetic, String encoding) throws IOException {
        buildRules(new BufferedReader(new InputStreamReader(new FileInputStream(phonetic), encoding)));
        alphabetString = washAlphabetIntoReplaceList(getReplaceList());
    }

    /**
   * Construct a transformation table from the phonetic file
   * @param phonetic the phonetic file as specified in aspell. The file is
   * supplied as a reader.
   * @throws java.io.IOException indicates a problem while reading
   * the phonetic information
   */
    public GenericTransformator(Reader phonetic) throws IOException {
        buildRules(new BufferedReader(phonetic));
        alphabetString = washAlphabetIntoReplaceList(getReplaceList());
    }

    /**
   * Goes through an alphabet and makes sure that only one of those letters
   * that are coded equally will be in the replace list.
   * In other words, it removes any letters in the alphabet
   * that are redundant phonetically.
   *
   * This is done to improve speed in the getSuggestion method.
   *
   * @param alphabet The complete alphabet to wash.
   * @return The washed alphabet to be used as replace list.
   */
    private char[] washAlphabetIntoReplaceList(char[] alphabet) {
        Map<String, Character> letters = new HashMap<String, Character>(alphabet.length);
        for (int i = 0; i < alphabet.length; i++) {
            String tmp = String.valueOf(alphabet[i]);
            String code = transform(tmp);
            if (!letters.containsKey(code)) {
                letters.put(code, new Character(alphabet[i]));
            }
        }
        Object[] tmpCharacters = letters.values().toArray();
        char[] washedArray = new char[tmpCharacters.length];
        for (int i = 0; i < tmpCharacters.length; i++) {
            washedArray[i] = ((Character) tmpCharacters[i]).charValue();
        }
        return washedArray;
    }

    /**
   * Takes out all single character replacements and put them in a char array.
   * This array can later be used for adding or changing letters in getSuggestion().
   * @return char[] An array of chars with replacements characters
   */
    public char[] getCodeReplaceList() {
        if (ruleArray == null) return null;
        List<String> tmp = new ArrayList<String>(ruleArray.length);
        for (TransformationRule rule : ruleArray) {
            if (rule.getReplaceExp().length() == 1) tmp.add(rule.getReplaceExp());
        }
        char[] replacements = new char[tmp.size()];
        for (int i = 0; i < tmp.size(); i++) {
            replacements[i] = tmp.get(i).charAt(0);
        }
        return replacements;
    }

    /**
   * Builds up an char array with the chars in the alphabet of the language as it was read from the
   * alphabet tag in the phonetic file.
   * @return char[] An array of chars representing the alphabet or null if no alphabet was available.
   */
    public char[] getReplaceList() {
        return alphabetString;
    }

    /**
   * Builds the phonetic code of the word.
   * @param word the word to transform
   * @return the phonetic transformation of the word
   */
    public String transform(String word) {
        if (ruleArray == null) return null;
        StringBuffer str = new StringBuffer(word.toUpperCase());
        int strLength = str.length();
        int startPos = 0, add = 1;
        while (startPos < strLength) {
            add = 1;
            if (Character.isDigit(str.charAt(startPos))) {
                StringUtility.replace(str, startPos, startPos + DIGITCODE.length(), DIGITCODE);
                startPos += add;
                continue;
            }
            for (TransformationRule rule : ruleArray) {
                if (rule.startsWithExp() && startPos > 0) continue;
                if (startPos + rule.lengthOfMatch() > strLength) {
                    continue;
                }
                if (rule.isMatching(str, startPos)) {
                    String replaceExp = rule.getReplaceExp();
                    add = replaceExp.length();
                    StringUtility.replace(str, startPos, startPos + rule.getTakeOut(), replaceExp);
                    strLength -= rule.getTakeOut();
                    strLength += add;
                    break;
                }
            }
            startPos += add;
        }
        return str.toString();
    }

    private void buildRules(BufferedReader in) throws IOException {
        String read;
        List<TransformationRule> ruleList = new Vector<TransformationRule>();
        while ((read = in.readLine()) != null) {
            buildRule(realTrimmer(read), ruleList);
        }
        ruleArray = ruleList.toArray(new TransformationRule[ruleList.size()]);
    }

    private void buildRule(String str, List<TransformationRule> ruleList) {
        if (str.length() < 1) return;
        for (int i = 0; i < IGNORED_KEYWORDS.length; i++) {
            if (str.startsWith(IGNORED_KEYWORDS[i])) return;
        }
        if (str.startsWith(KEYWORD_ALPHBET)) {
            int start = str.indexOf(ALPHABET_START);
            int end = str.lastIndexOf(ALPHABET_END);
            if (end != -1 && start != -1) {
                alphabetString = str.substring(++start, end).toCharArray();
            }
            return;
        }
        StringBuffer matchExp = new StringBuffer();
        StringBuffer replaceExp = new StringBuffer();
        boolean start = false, end = false;
        int takeOutPart = 0, matchLength = 0;
        boolean match = true, inMulti = false;
        for (int i = 0; i < str.length(); i++) {
            if (Character.isWhitespace(str.charAt(i))) {
                match = false;
            } else {
                if (match) {
                    if (!isReservedChar(str.charAt(i))) {
                        matchExp.append(str.charAt(i));
                        if (!inMulti) {
                            takeOutPart++;
                            matchLength++;
                        }
                        if (str.charAt(i) == STARTMULTI || str.charAt(i) == ENDMULTI) inMulti = !inMulti;
                    }
                    if (str.charAt(i) == '-') takeOutPart--;
                    if (str.charAt(i) == '^') start = true;
                    if (str.charAt(i) == '$') end = true;
                } else {
                    replaceExp.append(str.charAt(i));
                }
            }
        }
        if (replaceExp.toString().equals(REPLACEVOID)) {
            replaceExp = new StringBuffer("");
        }
        TransformationRule rule = new TransformationRule(matchExp.toString(), replaceExp.toString(), takeOutPart, matchLength, start, end);
        ruleList.add(rule);
    }

    private boolean isReservedChar(char ch) {
        if (ch == '<' || ch == '>' || ch == '^' || ch == '$' || ch == '-' || Character.isDigit(ch)) return true;
        return false;
    }

    private String realTrimmer(String row) {
        int pos = row.indexOf('#');
        if (pos != -1) {
            row = row.substring(0, pos);
        }
        return row.trim();
    }

    private static class TransformationRule {

        private String replace;

        private char[] match;

        private int takeOut, matchLength;

        private boolean start, end;

        public TransformationRule(String match, String replace, int takeout, int matchLength, boolean start, boolean end) {
            this.match = match.toCharArray();
            this.replace = replace;
            this.takeOut = takeout;
            this.matchLength = matchLength;
            this.start = start;
            this.end = end;
        }

        public boolean isMatching(StringBuffer word, int wordPos) {
            boolean matching = true, inMulti = false, multiMatch = false;
            char matchCh;
            for (int matchPos = 0; matchPos < match.length; matchPos++) {
                matchCh = match[matchPos];
                if (matchCh == STARTMULTI || matchCh == ENDMULTI) {
                    inMulti = !inMulti;
                    if (!inMulti) matching = matching & multiMatch; else multiMatch = false;
                } else {
                    if (matchCh != word.charAt(wordPos)) {
                        if (inMulti) multiMatch = multiMatch | false; else matching = false;
                    } else {
                        if (inMulti) multiMatch = multiMatch | true; else matching = true;
                    }
                    if (!inMulti) wordPos++;
                    if (!matching) break;
                }
            }
            if (end && wordPos != word.length()) matching = false;
            return matching;
        }

        public String getReplaceExp() {
            return replace;
        }

        public int getTakeOut() {
            return takeOut;
        }

        public boolean startsWithExp() {
            return start;
        }

        public int lengthOfMatch() {
            return matchLength;
        }

        @Override
        public String toString() {
            return "Match:" + String.valueOf(match) + " Replace:" + replace + " TakeOut:" + takeOut + " MatchLength:" + matchLength + " Start:" + start + " End:" + end;
        }
    }
}
