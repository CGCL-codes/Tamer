package itjava.industry;

import java.io.Reader;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharTokenizer;
import org.apache.lucene.analysis.TokenStream;

/**
 * @author renuka_sr
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class KeywordAnalyzer extends Analyzer {

    public TokenStream tokenStream(String fieldName, Reader reader) {
        return new CharTokenizer(reader) {

            protected boolean isTokenChar(char c) {
                return true;
            }
        };
    }
}
