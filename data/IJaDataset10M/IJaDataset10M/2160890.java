package de.unkrig.commons.util.pattern;

import java.util.ArrayList;
import java.util.List;
import de.unkrig.commons.io.charstream.CharSequenceCharStream;
import de.unkrig.commons.io.charstream.UnexpectedCharacterException;

/**
 * Matches integer subjects against a pattern like "-3;7-10;12-40;50-".
 */
public abstract class IntegerPattern {

    IntegerPattern() {
    }

    public abstract boolean matches(Integer subject);

    public static IntegerPattern newPattern(String pattern) throws UnexpectedCharacterException {
        class Range {

            final int min, max;

            public Range(int min, int max) {
                this.min = min;
                this.max = max;
            }
        }
        final List<Range> ranges = new ArrayList<Range>();
        CharSequenceCharStream cs = new CharSequenceCharStream(pattern);
        for (; ; ) {
            Range range;
            if (cs.peekRead('-')) {
                range = new Range(Integer.MIN_VALUE, readInt(cs));
            } else {
                int min = readInt(cs);
                range = (!cs.peekRead('-') ? new Range(min, min) : cs.peekRead("0123456789") == -1 ? new Range(min, Integer.MAX_VALUE) : new Range(min, readInt(cs)));
            }
            ranges.add(range);
            if (cs.isAtEoi()) break;
            cs.read(";, ");
            while (cs.peekRead(";, ") != -1) ;
        }
        return new IntegerPattern() {

            @Override
            public boolean matches(Integer subject) {
                for (Range range : ranges) {
                    if ((subject.intValue() >= range.min) && (subject.intValue() <= range.max)) return true;
                }
                return false;
            }
        };
    }

    private static int readInt(CharSequenceCharStream cs) throws UnexpectedCharacterException {
        int res = cs.read("0123456789");
        for (; ; ) {
            int x = cs.peekRead("0123456789");
            if (x == -1) return res;
            res = 10 * res + x;
        }
    }
}
