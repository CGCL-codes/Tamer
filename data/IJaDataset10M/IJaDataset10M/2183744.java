package org.omegat.core.segmentation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.omegat.core.data.SM;
import org.omegat.util.Language;
import org.omegat.util.PatternConsts;

/**
 * The class that sentences the paragraphs into sentences
 * and glues translated sentences together to form a paragraph.
 * 
 * 
 * @author Maxym Mykhalchuk
 */
public final class Segmenter {

    /** private to disallow creation */
    private Segmenter() {
    }

    /**
     * Segments the paragraph to sentences according to currently setup rules.
     * <p>
     * Bugfix for 
     * <a href="http://sourceforge.net/support/tracker.php?aid=1288742">issue 
     * 1288742</a>: Sentences are returned without spaces in the beginning and
     * at the end of a sentence. 
     * <p>
     * An additional list with space information
     * is returned to be able to glue translation together with the same spaces
     * between them as in original paragraph.
     *
     * @param paragraph the paragraph text
     * @param spaces    list to store information about spaces between sentences
     * @param brules    list to store rules that account to breaks
     * @return list of sentences (String objects)
     */
    public static List<String> segment(Language lang, String paragraph, List<StringBuffer> spaces, List<Rule> brules) {
        List<String> segments = breakParagraph(lang, paragraph, brules);
        List<String> sentences = new ArrayList<String>(segments.size());
        if (spaces == null) spaces = new ArrayList<StringBuffer>();
        spaces.clear();
        for (String one : segments) {
            int len = one.length();
            int b = 0;
            StringBuffer bs = new StringBuffer();
            while (b < len && Character.isWhitespace(one.charAt(b))) {
                bs.append(one.charAt(b));
                b++;
            }
            int e = len - 1;
            StringBuffer es = new StringBuffer();
            while (e >= b && Character.isWhitespace(one.charAt(e))) {
                es.append(one.charAt(e));
                e--;
            }
            es.reverse();
            String trimmed = one.substring(b, e + 1);
            sentences.add(trimmed);
            if (spaces != null) {
                spaces.add(bs);
                spaces.add(es);
            }
        }
        return sentences;
    }

    /**
     * Returns pre-sentences (sentences with spaces between), computed by 
     * breaking paragraph into chunks of text. Also returns the list
     * with "the reasons" why the breaks were made, i.e. the list of break rules
     * that contributed to each of the breaks made.
     * <p>
     * If glued back together, these strings form the same paragraph text 
     * as this function was fed.
     *
     * @param paragraph the paragraph text
     * @param brules    list to store rules that account to breaks
     */
    private static List<String> breakParagraph(Language lang, String paragraph, List<Rule> brules) {
        List<Rule> rules = SRX.getSRX().lookupRulesForLanguage(lang);
        if (brules == null) brules = new ArrayList<Rule>();
        Set<BreakPosition> dontbreakpositions = new TreeSet<BreakPosition>();
        Set<BreakPosition> breakpositions = new TreeSet<BreakPosition>();
        for (int i = rules.size() - 1; i >= 0; i--) {
            Rule rule = rules.get(i);
            List<BreakPosition> rulebreaks = getBreaks(paragraph, rule);
            if (rule.isBreakRule()) {
                breakpositions.addAll(rulebreaks);
                dontbreakpositions.removeAll(rulebreaks);
            } else {
                dontbreakpositions.addAll(rulebreaks);
                breakpositions.removeAll(rulebreaks);
            }
        }
        breakpositions.removeAll(dontbreakpositions);
        List<String> segments = new ArrayList<String>();
        brules.clear();
        int prevpos = 0;
        for (BreakPosition bposition : breakpositions) {
            String oneseg = paragraph.substring(prevpos, bposition.position);
            segments.add(oneseg);
            brules.add(bposition.reason);
            prevpos = bposition.position;
        }
        try {
            String oneseg = paragraph.substring(prevpos);
            if (oneseg.trim().length() == 0 && segments.size() > 0) {
                String prev = segments.get(segments.size() - 1);
                prev += oneseg;
                segments.set(segments.size() - 1, prev);
            } else segments.add(oneseg);
        } catch (IndexOutOfBoundsException iobe) {
        }
        if (SM.sm.splitList == null && SM.sm.mergeList == null) return segments;
        List<String> tempSegments = new ArrayList<String>();
        for (int j = 0; j < segments.size(); j++) {
            boolean matched = false;
            String nowSegment = segments.get(j);
            for (int k = 0; k < SM.sm.splitList.size(); k++) {
                String nowSplit = SM.sm.splitList.get(k);
                if (nowSegment.contains(nowSplit)) {
                    int index = nowSegment.indexOf(nowSplit) + nowSplit.length();
                    tempSegments.add(nowSegment.substring(0, index));
                    tempSegments.add(nowSegment.substring(index + 1));
                    matched = true;
                }
            }
            if (!matched) tempSegments.add(nowSegment);
        }
        segments = tempSegments;
        if (SM.sm.mergeList == null) return segments;
        if (segments.size() > 1) {
            int segmentsLastIndex = segments.size() - 1;
            int segmentsSecondLastIndex = segmentsLastIndex - 1;
            for (int h = segmentsSecondLastIndex; h >= 0; h--) {
                String nowSegment = segments.get(h);
                nowSegment = nowSegment.trim();
                if (SM.sm.mergeList.contains(nowSegment)) {
                    String nextSegment = segments.get(h + 1);
                    nextSegment = nextSegment.trim();
                    nowSegment += " ";
                    segments.set(h, nowSegment + nextSegment);
                    segments.remove(h + 1);
                }
            }
        }
        return segments;
    }

    private static Pattern DEFAULT_BEFOREBREAK_PATTERN = Pattern.compile(".", Pattern.DOTALL);

    /**
     * Returns the places of possible breaks between sentences.
     */
    private static List<BreakPosition> getBreaks(String paragraph, Rule rule) {
        List<BreakPosition> res = new ArrayList<BreakPosition>();
        Matcher bbm = null;
        if (rule.getBeforebreak() != null) bbm = rule.getCompiledBeforebreak().matcher(paragraph);
        Matcher abm = null;
        if (rule.getAfterbreak() != null) abm = rule.getCompiledAfterbreak().matcher(paragraph);
        if (bbm == null && abm == null) return res;
        if (abm != null) if (!abm.find()) return res;
        if (bbm == null) bbm = DEFAULT_BEFOREBREAK_PATTERN.matcher(paragraph);
        while (bbm.find()) {
            int bbe = bbm.end();
            if (abm == null) res.add(new BreakPosition(bbe, rule)); else {
                int abs = abm.start();
                while (abs < bbe) {
                    boolean found = abm.find();
                    if (!found) return res;
                    abs = abm.start();
                }
                if (abs == bbe) res.add(new BreakPosition(bbe, rule));
            }
        }
        return res;
    }

    /** A class for a break position that knows which rule contributed to it. */
    static class BreakPosition implements Comparable<BreakPosition> {

        /** Break/Exception position. */
        int position;

        /** Rule that contributed to the break. */
        Rule reason;

        /** Creates a new break position. */
        BreakPosition(int position, Rule reason) {
            this.position = position;
            this.reason = reason;
        }

        /** Other BreakPosition is "equal to" this one iff it has the same position. */
        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (!(obj instanceof BreakPosition)) return false;
            BreakPosition that = (BreakPosition) obj;
            return this.position == that.position;
        }

        /** Returns a hash code == position for the object. */
        public int hashCode() {
            return this.position;
        }

        /** 
         * Compares this break position with another. 
         *
         * @return a negative integer if its position is less than the another's, 
         *          zero if they are equal, or a positive integer 
         *          as its position is greater than the another's.
         * @throws ClassCastException if the specified object's type prevents it
         *         from being compared to this Object.
         */
        public int compareTo(BreakPosition that) {
            return this.position - that.position;
        }
    }

    /**
     * Glues the sentences back to paragraph.
     * <p>
     * As sentences are returned by {@link #segment(String, List)}
     * without spaces before and after them, this method adds 
     * spaces if needed:
     * <ul>
     * <li>For translation to Japanese does <b>not</b> add any spaces.
     * <br>
     * A special exceptions are the Break SRX rules that break on space,
     * i.e. before and after patterns consist of spaces 
     * (they get trimmed to an empty string). For such rules all the spaces
     * are added
     * <li>For translation from Japanese adds one space
     * <li>For all other language combinations adds those spaces as were in the
     *     paragraph before.
     * </ul>
     *
     * @param sentences list of translated sentences
     * @param spaces    information about spaces in original paragraph
     * @param brules    rules that account to breaks
     * @return glued translated paragraph
     */
    public static String glue(Language sourceLang, Language targetLang, List<String> sentences, List<StringBuffer> spaces, List<Rule> brules) {
        if (sentences.size() <= 0) return "";
        StringBuffer res = new StringBuffer();
        res.append(sentences.get(0));
        for (int i = 1; i < sentences.size(); i++) {
            StringBuffer sp = new StringBuffer();
            sp.append(spaces.get(2 * i - 1));
            sp.append(spaces.get(2 * i));
            if (CJK_LANGUAGES.contains(targetLang.getLanguageCode().toUpperCase(Locale.ENGLISH))) {
                Rule rule = brules.get(i - 1);
                char lastChar = res.charAt(res.length() - 1);
                if ((lastChar != '.') && (!PatternConsts.SPACY_REGEX.matcher(rule.getBeforebreak()).matches() || !PatternConsts.SPACY_REGEX.matcher(rule.getAfterbreak()).matches())) sp.setLength(0);
            } else if (CJK_LANGUAGES.contains(sourceLang.getLanguageCode().toUpperCase(Locale.ENGLISH)) && sp.length() == 0) sp.append(" ");
            res.append(sp);
            res.append(sentences.get(i));
        }
        return res.toString();
    }

    /** CJK languages. */
    private static final Set<String> CJK_LANGUAGES = new HashSet<String>();

    static {
        CJK_LANGUAGES.add("ZH");
        CJK_LANGUAGES.add("JA");
        CJK_LANGUAGES.add("KO");
    }
}
