package com.wideplay.toolkit.regex;

/**
 * @author Dhanji
 *
 * A factory class that produces regex pattern matchers
 */
public class RegexPatternMatcherFactory {

    public static RegexPatternMatcher createPatternMatcher(String regex, String inputData) {
        RegexPatternMatcherAdapter rpm = new RegexPatternMatcherAdapter(regex);
        rpm.setData(inputData);
        return (RegexPatternMatcher) rpm;
    }

    public static RegexPatternMatcher createPatternMatcher(String regex) {
        RegexPatternMatcherAdapter rpm = new RegexPatternMatcherAdapter(regex);
        return (RegexPatternMatcher) rpm;
    }
}
