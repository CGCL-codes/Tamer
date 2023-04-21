package org.jtools.helper.java.util.regex;

import java.util.regex.Pattern;
import org.jtools.util.PatternAttributes;
import org.jtools.util.PatternSyntax;

public class PatternHelper implements PatternAttributes, PatternAttributes.Holder {

    private static final char flag_caseInsensitive = 'i';

    private static final char flag_comments = 'x';

    private static final char flag_dotall = 's';

    private static final char flag_multiline = 'm';

    private static final char flag_unicodeCase = 'u';

    private static final char flag_unixLines = 'd';

    private boolean canonEq = false;

    private boolean caseInsensitive = false;

    private boolean comments = false;

    private boolean dotall = false;

    private boolean literal = false;

    private boolean multiline = false;

    private boolean unicodeCase = false;

    private boolean unixLines = false;

    private String flags;

    private String regex;

    private PatternSyntax syntax = PatternSyntax.REGEX;

    private int processFlags() {
        if (flags != null) {
            for (char c : flags.toCharArray()) switch(c) {
                case flag_caseInsensitive:
                    caseInsensitive = true;
                    break;
                case flag_comments:
                    comments = true;
                    break;
                case flag_dotall:
                    dotall = true;
                    break;
                case flag_multiline:
                    multiline = true;
                    break;
                case flag_unicodeCase:
                    unicodeCase = true;
                    break;
                case flag_unixLines:
                    unixLines = true;
                    break;
            }
        }
        int x = 0;
        if (canonEq) x |= Pattern.CANON_EQ;
        if (caseInsensitive) x |= Pattern.CASE_INSENSITIVE;
        if (comments) x |= Pattern.COMMENTS;
        if (dotall) x |= Pattern.DOTALL;
        if (literal) x |= Pattern.LITERAL;
        if (multiline) x |= Pattern.MULTILINE;
        if (unicodeCase) x |= Pattern.UNICODE_CASE;
        if (unixLines) x |= Pattern.UNIX_LINES;
        return x;
    }

    public void setCanonEq(boolean canonEq) {
        this.canonEq = canonEq;
    }

    public void setCaseInsensitive(boolean caseInsensitive) {
        this.caseInsensitive = caseInsensitive;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.caseInsensitive = !caseSensitive;
    }

    public void setComments(boolean comments) {
        this.comments = comments;
    }

    public void setDotall(boolean dotall) {
        this.dotall = dotall;
    }

    public void setLiteral(boolean literal) {
        this.literal = literal;
    }

    public void setMultiline(boolean multiline) {
        this.multiline = multiline;
    }

    public void setUnicodeCase(boolean unicodeCase) {
        this.unicodeCase = unicodeCase;
    }

    public void setUnixLines(boolean unixLines) {
        this.unixLines = unixLines;
    }

    public void setFlags(String flags) {
        this.flags = flags;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public Pattern toInstance() {
        return PatternSyntax.compile(this);
    }

    public String getRegEx() {
        return regex;
    }

    public int getFlags() {
        return processFlags();
    }

    public PatternAttributes getPatternAttributes() {
        return this;
    }

    public void setSyntax(PatternSyntax syntax) {
        if (syntax == null) throw new NullPointerException("syntax");
        this.syntax = syntax;
    }

    public PatternSyntax getSyntax() {
        return syntax;
    }
}
