package au.id.jericho.lib.html;

import java.io.*;

/**
 * Implements an {@link OutputSegment} whose content is a <code>CharSequence</code>.
 * <p>
 * This class has been removed from the pulic API and the functionality replaced with the
 * {@link OutputDocument#replace(Segment, CharSequence text)} method.
 */
final class StringOutputSegment implements OutputSegment {

    private final int begin;

    private final int end;

    private final CharSequence text;

    /**
	 * Constructs a new <code>StringOutputSegment</code> with the specified begin and end positions and the specified content.
	 * <p>
	 * Specifying a <code>null</code> argument to the <code>text</code> parameter is exactly equivalent to specifying an empty string,
	 * and results in the segment being completely removed from the output document.
	 *
	 * @param begin  the position in the <code>OutputDocument</code> where this output segment begins.
	 * @param end  the position in the <code>OutputDocument</code> where this output segment ends.
	 * @param text  the textual content of the new output segment, or <code>null</code> if no content.
	 */
    public StringOutputSegment(final int begin, final int end, final CharSequence text) {
        this.begin = begin;
        this.end = end;
        this.text = (text == null ? "" : text);
    }

    /**
	 * Constructs a new StringOutputSegment</code> with the same span as the specified {@link Segment}.
	 * <p>
	 * Specifying a <code>null</code> argument to the <code>text</code> parameter is exactly equivalent to specifying an empty string,
	 * and results in the segment being completely removed from the output document.
	 *
	 * @param segment  a segment defining the beginning and ending positions of the new output segment.
	 * @param text  the textual content of the new output segment, or <code>null</code> if no content.
	 */
    public StringOutputSegment(final Segment segment, final CharSequence text) {
        this(segment.begin, segment.end, text);
    }

    public int getBegin() {
        return begin;
    }

    public int getEnd() {
        return end;
    }

    public void writeTo(final Writer writer) throws IOException {
        Util.appendTo(writer, text);
    }

    public long getEstimatedMaximumOutputLength() {
        return text.length();
    }

    public String toString() {
        return text.toString();
    }

    public String getDebugInfo() {
        return "Replace: (p" + begin + "-p" + end + ") " + text;
    }

    public void output(final Writer writer) throws IOException {
        writeTo(writer);
    }
}
