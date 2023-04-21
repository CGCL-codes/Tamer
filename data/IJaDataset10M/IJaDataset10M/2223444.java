package org.apache.solr.spelling;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.analysis.TokenStream;
import java.util.Collection;
import java.util.HashSet;
import java.io.StringReader;
import java.io.IOException;

/**
 *
 * @since solr 1.3
 **/
class SimpleQueryConverter extends SpellingQueryConverter {

    @Override
    public Collection<Token> convert(String origQuery) {
        Collection<Token> result = new HashSet<Token>();
        WhitespaceAnalyzer analyzer = new WhitespaceAnalyzer();
        TokenStream ts = analyzer.tokenStream("", new StringReader(origQuery));
        Token tok = null;
        try {
            while ((tok = ts.next()) != null) {
                result.add(tok);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
