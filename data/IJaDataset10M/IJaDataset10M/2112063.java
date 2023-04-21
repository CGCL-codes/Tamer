package org.robotframework.javalib.factory;

import org.robotframework.javalib.keyword.Keyword;

/**
 * Creates instances of keywords.
 */
public interface KeywordFactory<T extends Keyword> {

    /**
     * Creates an instance of the class implementing the given keyword
     * name
     *
     * @param keywordName keyword name (will be normalized, so pretty much
     *                       any formatting will do)
     * @return keyword instance
     */
    T createKeyword(String keywordName);

    /**
     * Returns all the names of the keywords that this factory can create
     *
     * @return names of available keywords
     */
    String[] getKeywordNames();
}
