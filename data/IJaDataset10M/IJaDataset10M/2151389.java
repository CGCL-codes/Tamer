package net.moraleboost.solr;

import java.io.Reader;
import java.util.Map;
import net.moraleboost.lucene.analysis.ja.TinySegmenterTokenizer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.solr.analysis.BaseTokenizerFactory;

/**
 * {@link TinySegmenterTokenizer}のファクトリ。
 * @author taketa
 *
 */
public class TinySegmenterTokenizerFactory extends BaseTokenizerFactory {

    public TinySegmenterTokenizerFactory() {
        super();
    }

    /**
     * パラメータは存在しない。
     */
    public void init(Map<String, String> args) {
        super.init(args);
    }

    public Tokenizer create(Reader in) {
        return new TinySegmenterTokenizer(in);
    }
}
