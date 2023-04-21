package puggle.LexicalAnalyzer;

import java.io.Reader;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.el.GreekAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;

/**
 *
 * @author gvasil
 */
public class CombinedAnalyzer extends Analyzer {

    private GreekAnalyzer greek = new GreekAnalyzer();

    public TokenStream tokenStream(String fieldName, Reader reader) {
        TokenStream tokens = greek.tokenStream(fieldName, reader);
        tokens = new StandardFilter(tokens);
        tokens = new LowerCaseFilter(tokens);
        tokens = new PorterStemFilter(tokens);
        return tokens;
    }
}
