package org.omegat.core.search;

import org.omegat.util.OConsts;

/**
 * Storage for what to search for (search text and options).
 * 
 * @author Keith Godfrey
 * @author Maxym Mykhalchuk
 * @author Henry Pijffers
 * @author Didier Briel
 * @author Martin Fleurke
 * @author Antonio Vilei
 */
public class SearchExpression {

    /**
     * Creates a new search expression based on a given search text.
     * 
     * @param text
     *            The text to search for
     */
    public SearchExpression(String text) {
        this.text = text;
        this.rootDir = null;
        this.recursive = true;
        this.exact = true;
        this.keyword = false;
        this.regex = false;
        this.caseSensitive = false;
        this.tm = true;
        this.allResults = false;
        this.searchSource = true;
        this.searchTarget = true;
        this.searchNotes = true;
        this.searchAuthor = false;
        this.author = "";
        this.searchDateAfter = false;
        this.dateAfter = 0;
        this.searchDateBefore = false;
        this.dateBefore = 0;
        this.numberOfResults = OConsts.ST_MAX_SEARCH_RESULTS;
    }

    /**
     * Creates a new search expression based on specified search text and
     * options.
     * 
     * @param text
     *            The text to search for
     * @param rootDir
     *            The folder to search in
     * @param recursive
     *            Allow searching in subfolders of rootDir
     * @param exact
     *            Search for a substring, including wildcards (*?)
     * @param keyword
     *            Search for keywords, including wildcards (*?)
     * @param regex
     *            Search based on regular expressions
     * @param caseSensitive
     *            Search case sensitive
     * @param tm
     *            Search also in legacy and orphan TM strings
     * @param allResults
     *            Include duplicate results
     * @param searchSource
     *            Search in source text
     * @param searchTarget
     *            Search in target text
     * @param searchNotes
     *            Search in notes
     * @param searchAuthor
     *            Search for tmx segments modified by author id/name
     * @param author
     *            String to search for in TMX attribute modificationId
     * @param searchDateAfter
     *            Search for translation segments modified after the given date
     * @param dateAfter
     *            The date after which the modification date has to be
     * @param searchDateBefore
     *            Search for translation segments modified before the given date
     * @param dateBefore
     *            The date before which the modification date has to be
     * @param The
     *            maximum number of results
     */
    public SearchExpression(String text, String rootDir, boolean recursive, boolean exact, boolean keyword, boolean regex, boolean caseSensitive, boolean tm, boolean allResults, boolean searchSource, boolean searchTarget, boolean searchNotes, boolean searchAuthor, String author, boolean searchDateAfter, long dateAfter, boolean searchDateBefore, long dateBefore, int numberOfResults) {
        this.text = text;
        this.rootDir = rootDir;
        this.recursive = recursive;
        this.exact = exact;
        this.keyword = keyword;
        this.regex = regex;
        this.caseSensitive = caseSensitive;
        this.tm = tm;
        this.allResults = allResults;
        this.searchSource = searchSource;
        this.searchTarget = searchTarget;
        this.searchNotes = searchNotes;
        this.searchAuthor = searchAuthor;
        this.author = author;
        this.searchDateAfter = searchDateAfter;
        this.dateAfter = dateAfter;
        this.searchDateBefore = searchDateBefore;
        this.dateBefore = dateBefore;
        this.numberOfResults = numberOfResults;
    }

    public String text;

    public String rootDir;

    public boolean recursive;

    public boolean exact;

    public boolean keyword;

    public boolean regex;

    public boolean caseSensitive;

    public boolean tm;

    public boolean allResults;

    public boolean searchSource;

    public boolean searchTarget;

    public boolean searchNotes;

    public boolean searchAuthor;

    public String author;

    public boolean searchDateAfter;

    public long dateAfter;

    public boolean searchDateBefore;

    public long dateBefore;

    public int numberOfResults;
}
