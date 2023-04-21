package uk.org.biotext.graphspider.parser;

import java.util.List;

/**
 * The Interface PatternString.
 */
public interface PatternString {

    /**
     * Find all matches.
     * 
     * @param string
     *            the string
     * 
     * @return the list< substring>
     * 
     * @throws PatternMatchException
     *             the pattern match exception
     */
    public List<Substring> findAllMatches(String string) throws PatternMatchException;
}
