package hudson.zipscript.parser.template.element.comparator;

import hudson.zipscript.parser.exception.ParseException;
import hudson.zipscript.parser.template.data.ParsingSession;
import hudson.zipscript.parser.template.element.Element;
import hudson.zipscript.parser.template.element.PatternMatcher;
import java.nio.CharBuffer;
import java.util.List;

public class InComparatorPatternMatcher implements PatternMatcher {

    public char[] getStartToken() {
        return null;
    }

    public char[][] getStartTokens() {
        return new char[][] { " not in ".toCharArray(), " in ".toCharArray() };
    }

    public Element match(char previousChar, char[] startChars, CharBuffer reader, ParsingSession parseData, List elements, StringBuffer unmatchedChars) throws ParseException {
        if (startChars.length == 4) return new InExpression(); else return new NotInExpression();
    }
}
