package HTMLParser;

import java.io.*;
import java.util.*;

/**
 * Defines the interface for an OutputSegment, which is used in an {@link OutputDocument} to
 * replace segments of the source document with other text.
 * <p>
 * All text in the OutputDocument between the character positions defined by {@link #getBegin()} and {@link #getEnd()}
 * is replaced by the content of this OutputSegment.
 * If {@link #getBegin()} and {@link #getEnd()} are the same, the content is inserted at this position.
 *
 * @see OutputDocument
 */
public interface IOutputSegment {

    /** The comparator used to sort output segments in the {@link OutputDocument}. */
    public static final Comparator COMPARATOR = new OutputSegmentComparator();

    /**
	 * Returns the character position in the OutputDocument where this segment begins.
	 * @return  the character position in the OutputDocument where this segment begins.
	 */
    public int getBegin();

    /**
	 * Returns the character position in the OutputDocument where this segment ends.
	 * @return  the character position in the OutputDocument where this segment ends.
	 */
    public int getEnd();

    /**
	 * Outputs the content of this OutputSegment to the specified Writer.
	 * @param  out  the Writer to which the output is to be sent.
	 * @throws  IOException  if an I/O exception occurs.
	 */
    public void output(Writer out) throws IOException;
}
