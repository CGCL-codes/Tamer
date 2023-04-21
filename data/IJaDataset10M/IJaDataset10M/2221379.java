package com.rapidminer.operator.text.io.vectorcreation;

import com.rapidminer.operator.text.WordList;

/**
 * Creates a vector from a word list by counting the number of occurrences.
 * 
 * @author Michael Wurst
 */
public class TermOccurrences implements VectorCreator {

    @Override
    public double[] createVector(float[] frequencies, WordList wordList) {
        int numTerms = wordList.size();
        double[] wv = new double[numTerms];
        for (int i = 0; i < wv.length; i++) wv[i] = frequencies[i];
        return wv;
    }
}
