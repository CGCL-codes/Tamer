package org.tgworks.commons.regex.impl.jregex;

import org.tgworks.commons.regex.Pattern;
import org.tgworks.commons.regex.PatternCompiler;
import org.tgworks.commons.regex.annotation.PatternCompilerOption;
import org.tgworks.commons.regex.impl.base.AbstractPatternCompiler;

@PatternCompilerOption(shortName = "jregex", patternClass = JRegexPattern.class)
public class JRegexPatternCompiler extends AbstractPatternCompiler {

    private static final PatternCompiler INSTANCE = new JRegexPatternCompiler();

    public static PatternCompiler getInstance() {
        return INSTANCE;
    }

    public Pattern compile(String regex) {
        return JRegexPattern.compile(regex);
    }

    public Pattern compile(String regex, int flags) {
        return JRegexPattern.compile(regex, flags);
    }
}
