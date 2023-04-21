package au.id.jericho.lib.html;

import java.io.*;

/**
 * Implements an {@link OutputSegment} with no content.
 */
final class RemoveOutputSegment implements OutputSegment {

    private final int begin;

    private final int end;

    public RemoveOutputSegment(final int begin, final int end) {
        this.begin = begin;
        this.end = end;
    }

    public RemoveOutputSegment(final Segment segment) {
        this(segment.begin, segment.end);
    }

    public int getBegin() {
        return begin;
    }

    public int getEnd() {
        return end;
    }

    public void writeTo(final Writer writer) {
    }

    public long getEstimatedMaximumOutputLength() {
        return 0;
    }

    public String toString() {
        return "";
    }

    public String getDebugInfo() {
        return "Remove: (p" + begin + "-p" + end + ')';
    }
}
