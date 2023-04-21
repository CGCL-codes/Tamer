package com.rapidminer.operator.text.tools.queries;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.tools.Ontology;

/**
 * A document segmenter that is based on regular expressions.
 * 
 * @author Sebastian Land, Michael Wurst
 * 
 */
public class RegexQuery implements Query {

    public static final String METADATA_START = "match_start";

    private final String targetExpression;

    private final Pattern searchPattern;

    public RegexQuery(String expression) {
        this(expression, "$1");
    }

    public RegexQuery(String expression, String targetExpression) {
        this.searchPattern = Pattern.compile(expression, Pattern.DOTALL);
        this.targetExpression = targetExpression;
    }

    @Override
    public List<Match> getAllMatches(String document) throws OperatorException {
        List<Match> segments = new ArrayList<Match>();
        Matcher matcher = searchPattern.matcher(document);
        int lastAppendPosition = 0;
        while (matcher.find()) {
            StringBuffer buf = new StringBuffer();
            matcher.appendReplacement(buf, targetExpression);
            Match match = new Match(buf.substring(matcher.start() - lastAppendPosition));
            match.addMetaData(METADATA_START, matcher.start(), Ontology.INTEGER);
            segments.add(match);
            lastAppendPosition = matcher.end();
        }
        return segments;
    }

    @Override
    public Match getFirstMatch(String document) throws OperatorException {
        Matcher matcher = searchPattern.matcher(document);
        if (matcher.find()) {
            StringBuffer buf = new StringBuffer();
            matcher.appendReplacement(buf, targetExpression);
            Match match = new Match(buf.substring(matcher.start()));
            match.addMetaData(METADATA_START, matcher.start(), Ontology.INTEGER);
            return match;
        } else {
            return null;
        }
    }

    @Override
    public int hashCode() {
        return searchPattern.hashCode() ^ RegexQuery.class.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null) {
            if (obj instanceof RegexQuery) {
                RegexQuery other = (RegexQuery) obj;
                return (other.searchPattern.equals(searchPattern));
            }
        }
        return false;
    }
}
