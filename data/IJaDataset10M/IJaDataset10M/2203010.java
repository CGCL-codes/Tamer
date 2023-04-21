package org.contextor.content.filter.stem;

import org.contextor.content.term.Term;

/**
 * 
 * 
 * 
 * @author Behrooz Nobakht [behrooz dot nobakht at gmail dot com]
 **/
public interface TermStemmer extends Stemmer {

    /**
	 * @param term
	 * @return
	 */
    Term stem(Term term);
}
