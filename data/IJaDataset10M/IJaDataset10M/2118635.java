package edu.ucla.sspace.text;

import org.tartarus.snowball.ext.italianStemmer;

/**
 * A wrapper for the italian <a href="http://snowball.tartarus.org/">Snowball
 * Stemmer</a>.  Details for this specific stemmer can be found at <a
 * href="http://snowball.tartarus.org/algorithms/italian/stemmer.html">here</a>.
 *
 * @author Keith Stevens.
 */
public class ItalianStemmer implements Stemmer {

    /**
     * {@inheritDoc}
     */
    public String stem(String token) {
        italianStemmer stemmer = new italianStemmer();
        stemmer.setCurrent(token);
        stemmer.stem();
        return stemmer.getCurrent();
    }
}
