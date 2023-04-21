package org.apache.nutch.analysis.fr;

import java.io.Reader;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.nutch.analysis.NutchAnalyzer;

/**
 * A simple French Analyzer that wraps the Lucene one.
 * @author Jerome Charron
 */
public class FrenchAnalyzer extends NutchAnalyzer {

    private static final Analyzer ANALYZER = new org.apache.lucene.analysis.fr.FrenchAnalyzer();

    /** Creates a new instance of FrenchAnalyzer */
    public FrenchAnalyzer() {
    }

    public TokenStream tokenStream(String fieldName, Reader reader) {
        return ANALYZER.tokenStream(fieldName, reader);
    }
}
