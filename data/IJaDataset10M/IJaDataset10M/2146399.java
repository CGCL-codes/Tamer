package joshua.corpus.suffix_array;

import java.io.IOException;
import joshua.corpus.CorpusArray;
import joshua.corpus.MatchedHierarchicalPhrases;
import joshua.corpus.Phrase;
import joshua.corpus.suffix_array.BasicPhrase;
import joshua.corpus.suffix_array.SuffixArray;
import joshua.corpus.suffix_array.Suffixes;
import joshua.corpus.suffix_array.mm.MemoryMappedSuffixArray;
import joshua.corpus.vocab.SymbolTable;
import joshua.corpus.vocab.Vocabulary;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * Unit tests for suffix array.
 *
 * @author Lane Schwartz
 */
public class SuffixArrayTest {

    private final Suffixes suffixArray;

    private final Vocabulary vocab;

    @Parameters({ "binaryFileName" })
    public SuffixArrayTest(String binaryFileName) throws IOException, ClassNotFoundException {
        String corpusString = "it makes him and it mars him , it sets him on and it takes him off .";
        vocab = new Vocabulary();
        Phrase exampleSentence = new BasicPhrase(corpusString, vocab);
        exampleSentence = new BasicPhrase(corpusString, vocab);
        int[] sentences = new int[1];
        sentences[0] = 0;
        int[] corpus = new int[exampleSentence.size()];
        for (int i = 0; i < exampleSentence.size(); i++) {
            corpus[i] = exampleSentence.getWordID(i);
        }
        CorpusArray corpusArray = new CorpusArray(corpus, sentences, vocab);
        if (binaryFileName == null || binaryFileName.trim().length() == 0) suffixArray = new SuffixArray(corpusArray); else suffixArray = new MemoryMappedSuffixArray(binaryFileName, corpusArray, MemoryMappedSuffixArray.DEFAULT_CACHE_CAPACITY);
    }

    @Test
    public void findTriviallyHieroPhrase() {
        Assert.assertNotNull(vocab);
        Assert.assertNotNull(suffixArray);
        Pattern pattern = new Pattern(vocab, vocab.getID("it"), vocab.getID("makes"), vocab.getID("him"));
        Assert.assertEquals(pattern.arity(), 0);
        Assert.assertEquals(pattern.size(), 3);
        int minNonterminalSpan = 2;
        int maxPhraseSpan = 5;
        MatchedHierarchicalPhrases matches = suffixArray.createHierarchicalPhrases(pattern, minNonterminalSpan, maxPhraseSpan);
        Assert.assertNotNull(matches);
        Assert.assertEquals(matches.getPattern(), pattern);
        Assert.assertEquals(matches.arity(), 0);
        Assert.assertEquals(matches.size(), 1);
    }

    @Test(dependsOnMethods = { "findTriviallyHieroPhrase" })
    public void findHieroPhrase() {
        Assert.assertNotNull(vocab);
        Assert.assertNotNull(suffixArray);
        {
            Pattern pattern = new Pattern(vocab, vocab.getID("it"), vocab.getID(SymbolTable.X_STRING));
            Assert.assertEquals(pattern.arity(), 1);
            Assert.assertEquals(pattern.size(), 2);
            int minNonterminalSpan = 2;
            int maxPhraseSpan = 5;
            MatchedHierarchicalPhrases matches = suffixArray.createHierarchicalPhrases(pattern, minNonterminalSpan, maxPhraseSpan);
            Assert.assertNotNull(matches);
            Assert.assertEquals(matches.getPattern(), pattern);
            Assert.assertEquals(matches.arity(), 1);
            Assert.assertEquals(matches.size(), 4);
        }
        {
            Pattern pattern = new Pattern(vocab, vocab.getID("it"), vocab.getID(SymbolTable.X_STRING), vocab.getID("and"));
            Assert.assertEquals(pattern.arity(), 1);
            Assert.assertEquals(pattern.size(), 3);
            int minNonterminalSpan = 2;
            int maxPhraseSpan = 5;
            MatchedHierarchicalPhrases matches = suffixArray.createHierarchicalPhrases(pattern, minNonterminalSpan, maxPhraseSpan);
            Assert.assertNotNull(matches);
            Assert.assertEquals(matches.getPattern(), pattern);
            Assert.assertEquals(matches.arity(), 1);
            Assert.assertEquals(matches.size(), 2);
        }
    }

    @Test
    public void findPhrase() {
        Phrase phrase = new BasicPhrase("it makes him", vocab);
        int[] bounds = suffixArray.findPhrase(phrase);
        int expectedSuffixArrayStartIndex = 0;
        int expectedSuffixArrayEndIndex = 0;
        Assert.assertEquals(bounds.length, 2);
        Assert.assertEquals(bounds[0], expectedSuffixArrayStartIndex);
        Assert.assertEquals(bounds[1], expectedSuffixArrayEndIndex);
        phrase = new BasicPhrase("and it", vocab);
        bounds = suffixArray.findPhrase(phrase);
        expectedSuffixArrayStartIndex = 9;
        expectedSuffixArrayEndIndex = 10;
        Assert.assertEquals(bounds.length, 2);
        Assert.assertEquals(bounds[0], expectedSuffixArrayStartIndex);
        Assert.assertEquals(bounds[1], expectedSuffixArrayEndIndex);
    }
}
