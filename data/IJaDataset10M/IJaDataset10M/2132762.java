package net.moraleboost.lucene.analysis.ja;

import static net.moraleboost.lucene.analysis.ja.CJKTokenizer2Test.compareTokens;
import org.junit.Test;
import java.io.StringReader;

public class JapaneseNormalizationFilterTest {

    @Test
    public void testBlank() throws Exception {
        StringReader reader = new StringReader("あ　い　う え\nお\tか");
        SenTokenizer tokenizer = new SenTokenizer(reader, "etc/sen/conf/sen.xml");
        JapaneseNormalizationFilter filter = new JapaneseNormalizationFilter(tokenizer, false, false, true);
        String[] terms = { "あ", " ", "い", " ", "う", "え", "お", "か" };
        int[][] offsets = { { 0, 1 }, { 1, 2 }, { 2, 3 }, { 3, 4 }, { 4, 5 }, { 6, 7 }, { 8, 9 }, { 10, 11 } };
        compareTokens(filter, terms, offsets);
    }

    @Test
    public void testAlphaDigits() throws Exception {
        StringReader reader = new StringReader("Ｖｉｅｗカードを５１枚作った～。");
        SenTokenizer tokenizer = new SenTokenizer(reader, "etc/sen/conf/sen.xml");
        JapaneseNormalizationFilter filter = new JapaneseNormalizationFilter(tokenizer, true, false, false);
        String[] terms = { "V", "i", "e", "w", "カード", "を", "5", "1", "枚", "作っ", "た", "～", "。" };
        int[][] offsets = { { 0, 1 }, { 1, 2 }, { 2, 3 }, { 3, 4 }, { 4, 7 }, { 7, 8 }, { 8, 9 }, { 9, 10 }, { 10, 11 }, { 11, 13 }, { 13, 14 }, { 14, 15 }, { 15, 16 } };
        compareTokens(filter, terms, offsets);
    }

    @Test
    public void testKana() throws Exception {
        StringReader reader = new StringReader("あのｶﾝｶﾞﾙｰはいいﾊﾟﾝﾁを持っている。");
        SenTokenizer tokenizer = new SenTokenizer(reader, "etc/sen/conf/sen.xml");
        JapaneseNormalizationFilter filter = new JapaneseNormalizationFilter(tokenizer, false, true, false);
        String[] terms = { "あの", "カンガルー", "は", "いい", "パンチ", "を", "持っ", "て", "いる", "。" };
        int[][] offsets = { { 0, 2 }, { 2, 8 }, { 8, 9 }, { 9, 11 }, { 11, 15 }, { 15, 16 }, { 16, 18 }, { 18, 19 }, { 19, 21 }, { 21, 22 } };
        compareTokens(filter, terms, offsets);
    }
}
