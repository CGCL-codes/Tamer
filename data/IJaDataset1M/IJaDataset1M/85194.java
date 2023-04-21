package com.raelity.text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import com.raelity.text.TextUtil.MySegment;

public class RegExpJava extends RegExp {

    public RegExpJava() {
    }

    public static String getDisplayName() {
        return "Java 1.4+ Regular Expression";
    }

    public static int patternType() {
        return RegExp.PATTERN_PERL5;
    }

    public static boolean canInstantiate() {
        return true;
    }

    public static String getAdaptedName() {
        return "java.util.regex.Pattern";
    }

    /** Pick up the underlying java.util.regex.Pattern */
    public Pattern getPattern() {
        return pat;
    }

    /**
     * Prepare this regular expression to use <i>pattern</i>
     * for matching. The string can begin with "(e?=x)" to
     * specify an escape character.
     */
    @Override
    public void compile(String patternString, int compileFlags) throws RegExpPatternError {
        int flags = Pattern.MULTILINE;
        flags |= ((compileFlags & RegExp.IGNORE_CASE) != 0) ? Pattern.CASE_INSENSITIVE : 0;
        char fixupBuffer[];
        if (patternString.startsWith("(?e=") && patternString.length() >= 6 && patternString.charAt(5) == ')') {
            fixupBuffer = patternString.toCharArray();
            patternString = fixupEscape(fixupBuffer, 6, fixupBuffer[4]);
        } else if (escape != '\\') {
            patternString = fixupEscape(patternString.toCharArray(), 0, escape);
        }
        try {
            pat = Pattern.compile(patternString, flags);
        } catch (PatternSyntaxException e) {
            throw new RegExpPatternError(e.getDescription(), e.getIndex(), e);
        }
    }

    @Override
    public boolean search(String input) {
        return search(input, 0);
    }

    @Override
    public boolean search(String input, int start) {
        Matcher m = pat.matcher(input);
        matched = m.find();
        result = new RegExpResultJava(matched ? m : null);
        return matched;
    }

    @Override
    public boolean search(char[] input, int start, int len) {
        MySegment s = new MySegment(input, 0, input.length, -1);
        Matcher m = pat.matcher(s).region(start, start + len);
        m.useAnchoringBounds(false);
        matched = m.find();
        result = new RegExpResultJava(matched ? m : null);
        if (start(0) >= start + len) {
            matched = false;
        }
        return matched;
    }

    @Override
    public RegExpResult getResult() {
        return result;
    }

    @Override
    public int nGroup() {
        return result.nGroup();
    }

    @Override
    public String group(int i) {
        return result.group(i);
    }

    @Override
    public int length(int i) {
        return result.length(i);
    }

    @Override
    public int start(int i) {
        return result.start(i);
    }

    @Override
    public int stop(int i) {
        return result.stop(i);
    }

    private RegExpResultJava result;

    private Pattern pat;
}
