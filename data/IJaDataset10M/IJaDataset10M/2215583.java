package org.apache.solr.analysis;

import java.util.Map;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.ngram.EdgeNGramTokenFilter;

/**
 * Creates new instances of {@link EdgeNGramTokenFilter}.
 */
public class EdgeNGramFilterFactory extends BaseTokenFilterFactory {

    private int maxGramSize = 0;

    private int minGramSize = 0;

    private String side;

    @Override
    public void init(Map<String, String> args) {
        super.init(args);
        String maxArg = args.get("maxGramSize");
        maxGramSize = (maxArg != null ? Integer.parseInt(maxArg) : EdgeNGramTokenFilter.DEFAULT_MAX_GRAM_SIZE);
        String minArg = args.get("minGramSize");
        minGramSize = (minArg != null ? Integer.parseInt(minArg) : EdgeNGramTokenFilter.DEFAULT_MIN_GRAM_SIZE);
        side = args.get("side");
        if (side == null) {
            side = EdgeNGramTokenFilter.Side.FRONT.getLabel();
        }
    }

    public EdgeNGramTokenFilter create(TokenStream input) {
        return new EdgeNGramTokenFilter(input, side, minGramSize, maxGramSize);
    }
}
