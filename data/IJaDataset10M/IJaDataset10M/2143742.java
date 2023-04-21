package org.txt2xml.core;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Matches sections of text specified by groups in a 
 * regex pattern. For example when
 * regex='\s*(\d),\s*(\d)' this Processor will 
 * operate against " 1,  2" with the matches: "1", "2".
 * This Processor does not repeat, so in the above case,]
 * it will operate against "1, 2, 3" with "1", "2" and pass on the
 * remainder ", 3" to a subsequent Processor if any.
 * 
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 */
public class RegexMatchProcessor extends AbstractRegexProcessor {

    private static final Logger LOG = Logger.getLogger(RegexMatchProcessor.class.getName());

    private int group;

    private boolean matched;

    protected boolean findMatch() {
        if (LOG.isLoggable(Level.FINER)) LOG.finer("group=" + group);
        assert (chars != null);
        if (matcher == null) {
            throw new IllegalStateException("No matcher for this Processor. Was a Pattern specified?");
        }
        if (!matched) {
            return false;
        }
        ++group;
        if (group > matcher.groupCount()) {
            return false;
        } else {
            return true;
        }
    }

    protected CharSequence getMatchedText() {
        assert (matched);
        assert (group <= matcher.groupCount());
        if (LOG.isLoggable(Level.FINER)) LOG.finer("getMatchedText: " + matcher.group(group));
        return new SubCharSequence(chars, matcher.start(group), matcher.end(group) - matcher.start(group));
    }

    protected CharSequence getRemainderText() {
        assert (chars != null);
        if (!matched) {
            return chars;
        }
        assert (matcher.end() <= chars.length());
        return new SubCharSequence(chars, matcher.end(), chars.length() - matcher.end());
    }

    /**
     * Matches against the regex to find all matched groups
     * that will be stepped through in {@link #findMatch()}.
     */
    protected void resetMatching() {
        super.resetMatching();
        assert (chars != null);
        if (matcher.find()) {
            matched = true;
            group = 0;
        } else {
            matched = false;
        }
    }
}
