package org.apache.solr.analysis;

import java.io.IOException;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.Token;

public class CommonGramsQueryFilter extends BufferedTokenStream {

    private Token prev;

    /**
   * Constructor
   * 
   * @param input must be a CommonGramsFilter!
   * 
   */
    public CommonGramsQueryFilter(CommonGramsFilter input) {
        super(input);
        prev = new Token();
    }

    public void reset() throws IOException {
        super.reset();
        prev = new Token();
    }

    /**
   * Output bigrams whenever possible to optimize queries. Only output unigrams
   * when they are not a member of a bigram. Example:
   * <ul>
   * <li>input: "the rain in spain falls mainly"
   * <li>output:"the-rain", "rain-in" ,"in-spain", "falls", "mainly"
   */
    public Token process(Token token) throws IOException {
        Token next = peek(1);
        if (next == null) {
            if (prev == null) {
                return token;
            }
            if (prev != null && prev.type() != "gram") {
                return token;
            } else {
                return null;
            }
        }
        if (next != null && next.type() == "gram") {
            token = read();
            prev.reinit(token.termBuffer(), 0, token.termLength(), token.startOffset(), token.endOffset(), token.type());
            token.setPositionIncrement(1);
            return token;
        }
        prev.reinit(token.termBuffer(), 0, token.termLength(), token.startOffset(), token.endOffset(), token.type());
        assert token.type() == "word";
        return token;
    }
}
