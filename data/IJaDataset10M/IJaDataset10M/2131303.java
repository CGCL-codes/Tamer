package grammarscope.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import edu.stanford.nlp.trees.EnglishGrammaticalRelations;
import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.trees.tregex.TregexPattern;

public class XGrammaticalRelation extends GrammaticalRelation {

    private static final long serialVersionUID = 1L;

    public XGrammaticalRelation(final String shortName, final String longName, final GrammaticalRelation parent, final String sourcePattern, final String[] targetPatterns, final String specificString) {
        super(shortName, longName, parent, XGrammaticalRelation.makeSourcePattern(sourcePattern), XGrammaticalRelation.makeTargetPatterns(targetPatterns), specificString);
    }

    static Pattern makeSourcePattern(final String sourcePattern) {
        if (sourcePattern != null) {
            try {
                return Pattern.compile(sourcePattern);
            } catch (final java.util.regex.PatternSyntaxException e) {
                throw new RuntimeException("Bad pattern: " + sourcePattern);
            }
        }
        return null;
    }

    static List<TregexPattern> makeTargetPatterns(final String[] targetPatterns) {
        final List<TregexPattern> patterns = new ArrayList<TregexPattern>();
        if (targetPatterns != null) {
            for (final String pattern : targetPatterns) {
                try {
                    final TregexPattern p = TregexPattern.compile(pattern);
                    patterns.add(p);
                } catch (final Exception pe) {
                    throw new RuntimeException("Bad pattern: " + pattern, pe);
                }
            }
        }
        return patterns;
    }

    public static void main(final String[] args) {
        final GrammaticalRelation r0 = EnglishGrammaticalRelations.NOMINAL_SUBJECT;
        final GrammaticalRelation r1 = new XGrammaticalRelation("nsubj", "Nominal subject", null, null, null, null);
        final GrammaticalRelation r2 = new XGrammaticalRelation("nsubj", "Nominal subject", null, null, null, null);
        System.out.println(r0);
        System.out.println(r1);
        System.out.println(r2);
    }
}
