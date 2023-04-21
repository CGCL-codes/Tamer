package com.azureus.plugins.aztsearch;

/**
 * Contains application wide constants. Presently, these are only the category
 * designation constants.
 * 
 * @version 0.4
 * @author Dalmazio Brisinda
 * 
 * <p>
 * This software is licensed under the 
 * <a href="http://creativecommons.org/licenses/GPL/2.0/">CC-GNU GPL.</a>
 */
public final class TSConstants {

    /** Torrent search results falling under any category. */
    public static final String CATEGORY_ALL = "all";

    /** Torrent search results falling under the 'book' category. */
    public static final String CATEGORY_BOOKS = "book";

    /** Torrent search results falling under the 'game' category. */
    public static final String CATEGORY_GAMES = "game";

    /** Torrent search results falling under the 'movie' category. */
    public static final String CATEGORY_MOVIES = "movie";

    /** Torrent search results falling under the 'music' category. */
    public static final String CATEGORY_MUSIC = "music";

    /** Torrent search results falling under the 'picture' category. */
    public static final String CATEGORY_PICTURES = "picture";

    /** Torrent search results falling under the 'software' category. */
    public static final String CATEGORY_SOFTWARE = "software";

    /** Torrent search results falling under the 'tv' category. */
    public static final String CATEGORY_TV = "tv";

    /** Torrent search results not in any of the other (except 'all') categories. */
    public static final String CATEGORY_OTHER = "other";

    /** Version string for the torrent search plugin. */
    public static final String TS_PLUGIN_VERSION = "v0.4";
}
