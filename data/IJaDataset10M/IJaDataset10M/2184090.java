package net.sf.eos.sentence;

import net.sf.eos.analyzer.ResettableTokenizer;
import net.sf.eos.analyzer.SentenceTokenizer;
import net.sf.eos.analyzer.TextBuilder;
import net.sf.eos.document.EosDocument;
import net.sf.eos.medline.MedlineTokenizerSupplier;
import net.sf.eos.sentence.DefaultSentencer;
import net.sf.eos.sentence.Sentencer;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DefaultSentencerTest {

    @Test
    public void toSentence() throws Exception {
        final EosDocument doc = new EosDocument();
        doc.setTitle("the title");
        doc.setText("the is the first sentence. And this the second sentence.");
        final Map<String, List<String>> meta = new HashMap<String, List<String>>();
        final List<String> year = new ArrayList<String>();
        year.add("2008");
        meta.put(EosDocument.YEAR_META_KEY, year);
        doc.setMeta(meta);
        final SentenceTokenizer sentenceTokenizer = new SentenceTokenizer();
        final ResettableTokenizer tokenizer = new MedlineTokenizerSupplier().get();
        final Sentencer sentencer = new DefaultSentencer();
        final Map<String, EosDocument> sentenced = sentencer.toSentenceDocuments(doc, sentenceTokenizer, tokenizer, TextBuilder.SPACE_BUILDER);
        assertEquals(2, sentenced.size());
        for (final Entry<String, EosDocument> entry : sentenced.entrySet()) {
            assertEquals("2008", entry.getValue().getMeta().get(EosDocument.YEAR_META_KEY).get(0));
        }
    }

    @Test
    public void toSentenceHashMap() throws Exception {
        final EosDocument doc1 = new EosDocument();
        doc1.setTitle("the title");
        doc1.setText("This maybe the same frist sentence. " + "And the sencond same sentence.");
        final Map<String, List<String>> meta1 = new HashMap<String, List<String>>();
        final List<String> year1 = new ArrayList<String>();
        year1.add("2008");
        meta1.put(EosDocument.YEAR_META_KEY, year1);
        doc1.setMeta(meta1);
        final EosDocument doc2 = new EosDocument();
        doc2.setTitle("the title");
        doc2.setText("This maybe the same frist sentence. " + "And the sencond same sentence.");
        final Map<String, List<String>> meta2 = new HashMap<String, List<String>>();
        final List<String> year2 = new ArrayList<String>();
        year2.add("2007");
        meta2.put(EosDocument.YEAR_META_KEY, year2);
        doc2.setMeta(meta2);
        final SentenceTokenizer sentenceTokenizer = new SentenceTokenizer();
        final ResettableTokenizer tokenizer = new MedlineTokenizerSupplier().get();
        final Sentencer sentencer = new DefaultSentencer();
        final Map<String, EosDocument> sentenced1 = sentencer.toSentenceDocuments(doc1, sentenceTokenizer, tokenizer, TextBuilder.SPACE_BUILDER);
        final Map<String, EosDocument> sentenced2 = sentencer.toSentenceDocuments(doc2, sentenceTokenizer, tokenizer, TextBuilder.SPACE_BUILDER);
        for (final Entry<String, EosDocument> entry : sentenced1.entrySet()) {
            final String key = entry.getKey();
            assertTrue(sentenced2.keySet().contains(key));
            final EosDocument value1 = entry.getValue();
            final EosDocument value2 = sentenced2.get(key);
            assertEquals(value1.getText(), value2.getText());
            assertEquals(value1.getTitle(), value2.getTitle());
            final String y1 = value1.getMeta().get(EosDocument.YEAR_META_KEY).get(0);
            final String y2 = value2.getMeta().get(EosDocument.YEAR_META_KEY).get(0);
            assertFalse(y1.equals(y2));
        }
    }
}
