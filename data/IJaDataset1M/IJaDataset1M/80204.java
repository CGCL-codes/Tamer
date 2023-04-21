package org.progeeks.mparse.exp;

import java.io.IOException;
import java.util.*;
import org.progeeks.util.ObjectUtils;
import org.progeeks.mparse.*;

/**
 *  Expression matching a single character to one or more
 *  character ranges.
 *
 *  @version   $Revision: 4116 $
 *  @author    Paul Speed
 */
public class CharacterRanges extends AbstractExpression {

    private Range[] ranges;

    private EscapeFilter escape;

    protected CharacterRanges(EscapeFilter escape, Range... ranges) {
        this.ranges = ranges;
        this.escape = escape;
    }

    public CharacterRanges(int... bounds) {
        if ((bounds.length % 2) != 0) throw new IllegalArgumentException("Missing max in range specifications.");
        ranges = new Range[bounds.length / 2];
        for (int i = 0; i < ranges.length; i++) {
            ranges[i] = new Range((char) bounds[i * 2], (char) bounds[i * 2 + 1]);
        }
    }

    public void setEscapeFilter(EscapeFilter filter) {
        this.escape = filter;
    }

    public EscapeFilter getEscapeFilter() {
        return escape;
    }

    /**
     *  Creates a CharacterRanges expression with the same ranges
     *  as this one but that uses the specified excaping filter.
     */
    public CharacterRanges escape(EscapeFilter filter) {
        return new CharacterRanges(filter, ranges);
    }

    /**
     *  Creates a CharacterRanges expression with the same ranges
     *  as this one but that excludes the specified single characters.
     */
    public CharacterRanges exclude(char... exclude) {
        List<Range> original = Arrays.asList(ranges);
        List<Range> expanded = null;
        for (char c : exclude) {
            expanded = new ArrayList<Range>();
            for (Range r : original) {
                if (r.isInRange(c)) {
                    Range left = new Range(r.min, (char) (c - 1));
                    Range right = new Range((char) (c + 1), r.max);
                    expanded.add(left);
                    expanded.add(right);
                } else {
                    expanded.add(r);
                }
            }
            original = expanded;
        }
        Range[] array = expanded.toArray(new Range[expanded.size()]);
        return new CharacterRanges(escape, array);
    }

    public ResultType parse(ParseContext context, CharStream in) throws IOException {
        if (context.consume(this)) {
            System.out.println("--- consumed.");
            return ResultType.MATCHED;
        }
        int i = in.read();
        if (i < 0) return ResultType.UNMATCHED;
        if (escape != null && escape.isEscapeIndicator(i)) {
            int result = escape.parseEscapedChar(context, i, in);
            if (result < 0) {
                in.unread(i);
                return ResultType.UNMATCHED;
            }
            context.addResult(this, (char) result);
            return ResultType.MATCHED;
        }
        for (Range r : ranges) {
            if (r.isInRange(i)) {
                context.addResult(this, (char) i);
                return ResultType.MATCHED;
            }
        }
        in.unread(i);
        return ResultType.UNMATCHED;
    }

    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        CharacterRanges other = (CharacterRanges) o;
        if (!ObjectUtils.areEqual(Arrays.asList(ranges), Arrays.asList(other.ranges))) return false;
        if (!ObjectUtils.areEqual(escape, other.escape)) return false;
        return true;
    }

    public int hashCode() {
        return super.hashCode() + ranges[0].hashCode();
    }

    public String toString(int depth) {
        StringBuilder sb = new StringBuilder("[");
        for (Range r : ranges) {
            if (sb.length() > 1) sb.append(", ");
            sb.append(r);
        }
        if (escape != null) sb.append(", escapeFilter=" + escape);
        sb.append("]");
        return nameString() + sb.toString();
    }

    public static class Range {

        private char min;

        private char max;

        public Range(char min, char max) {
            this.min = min;
            this.max = max;
        }

        public boolean isInRange(int c) {
            return c >= min && c <= max;
        }

        public boolean equals(Object o) {
            if (o == null || o.getClass() != getClass()) return false;
            Range other = (Range) o;
            return other.min == min && other.max == max;
        }

        public int hashCode() {
            return min + max;
        }

        protected String boundsToString(char c) {
            if (c >= 'A' && c <= 'Z') return String.valueOf(c);
            if (c >= 'a' && c <= 'z') return String.valueOf(c);
            if (c >= '0' && c <= '9') return String.valueOf(c);
            if (c > ' ' && c <= 'z') return "0x" + Integer.toHexString(c) + "(" + c + ")";
            return "0x" + Integer.toHexString(c);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(boundsToString(min));
            sb.append('-');
            sb.append(boundsToString(max));
            return sb.toString();
        }
    }
}
