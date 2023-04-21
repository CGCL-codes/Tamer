package org.karticks.mapreduce;

import java.io.InputStream;
import java.util.Map;

/**
 * An interface that defines the "Map" phase of the Map-Reduce pattern.
 * This interface has only one method (<code>doMap</code>), that takes an <code>InputStream</code>
 * as the input. The implementing class should know how to handle the
 * stream, i.e. parse relevant information (e.g. words, URLs, etc.) and
 * store it in the output <code>Map</code>.
 * 
 * @author Kartick Suriamoorthy
 *
 */
public interface Mapper {

    /**
	 * Parses the contents of the stream and updates the contents of the <code>Map</code>
	 * with the relevant information. For example, an implementation to count
	 * words will extract words from the stream (will have to handle punctuation,
	 * line breaks, etc.), or an implementation to mine web-server log files
	 * will have to parse URL patterns, etc. The resulting <code>Map</code> will contain
	 * the relevant information (words, URLs, etc.) and their counts.
	 * 
	 * @param is A <code>InputStream</code> that contains the content that needs to be parsed
	 * @return A <code>Map</code> that contains relevant patterns (words, URLs, etc.) and their counts
	 */
    public Map<String, Integer> doMap(InputStream is);
}
