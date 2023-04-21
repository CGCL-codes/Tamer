package com.rapidminer.operator.text.io.wordfilter.stopwordlists;

/**
 * @author Sebastian Land
 *
 */
public interface StopWordList {

    /**
	 * Indicates if this word is a stopword
	 */
    public boolean isStopword(String str);
}
