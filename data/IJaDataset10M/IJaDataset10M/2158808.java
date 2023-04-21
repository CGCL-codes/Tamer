package edu.udo.cs.ai.nemoz.test.util;

import java.util.Map;

/**
 * An interface for all TagSources. The interface provides
 * functions for requesting artist tags, song tags etc.
 * 
 * @author akaspari
 */
public interface TagSource {

    /**
	 * Returns a map of (tagname, weight) pairs for the given artist.
	 * @param artist the artist to query for.  
	 */
    public Map<String, Integer> getTagsForArtist(String artist);

    /**
	 * Returns a map of (artistname, weight) pairs for the given artist.
	 * @param artist the artist to query for.  
	 */
    public Map<String, Integer> getRelatedArtists(String artist);

    /**
	 * 
	 * @param tag
	 * @return
	 */
    public Map<String, Integer> getTopArtistsForTag(String tag);
}
