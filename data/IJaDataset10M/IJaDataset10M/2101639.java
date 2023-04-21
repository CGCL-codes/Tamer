package org.macchiato.tokenfilter.stopwords;

import java.util.Vector;
import org.junit.Assert;
import org.junit.Test;
import org.macchiato.tokenizer.Token;
import org.macchiato.tokenizer.WhitespaceStringTokenizer;

/**
 * @author fdietz
 */
public class EnglishStopWordFilterTest {

    @Test
    public void test() {
        String input = "by snake but peter and pan the";
        WhitespaceStringTokenizer tokenizer = new WhitespaceStringTokenizer(input);
        EnglishStopWordFilter f = new EnglishStopWordFilter(tokenizer);
        Vector v1 = new Vector();
        v1.add("snake");
        v1.add("peter");
        v1.add("pan");
        Vector v2 = new Vector();
        Token token = new Token();
        while (!token.equals(Token.EOF)) {
            token = f.nextToken();
            if (!token.equals(Token.SKIP)) v2.add(token.getString());
        }
        for (int i = 0; i < v1.size(); i++) {
            Assert.assertEquals((String) v1.get(i), (String) v2.get(i));
        }
    }
}
