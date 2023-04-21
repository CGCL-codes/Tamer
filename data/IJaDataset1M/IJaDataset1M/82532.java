package joshua.corpus.lexprob;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Date;
import joshua.corpus.AlignedParallelCorpus;
import joshua.corpus.Corpus;
import joshua.corpus.CorpusArray;
import joshua.corpus.LabeledSpan;
import joshua.corpus.ParallelCorpus;
import joshua.corpus.Span;
import joshua.corpus.alignment.Alignments;
import joshua.corpus.suffix_array.HierarchicalPhrase;
import joshua.corpus.suffix_array.HierarchicalPhrases;
import joshua.corpus.suffix_array.Pattern;
import joshua.corpus.suffix_array.SuffixArray;
import joshua.corpus.suffix_array.SuffixArrayFactory;
import joshua.corpus.vocab.SymbolTable;
import joshua.corpus.vocab.Vocabulary;
import joshua.prefix_tree.PrefixTree;
import joshua.util.Counts;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit tests for LexProbs class.
 * 
 * TODO This class needs to be extended to add more unit tests that test for proper NULL alignment behavior.
 * 
 * @author Lane Schwartz
 */
public class BetterLexProbsTest {

    LexProbs lexProbs;

    Vocabulary sourceVocab, targetVocab;

    Alignments alignmentArray;

    CorpusArray sourceCorpusArray;

    CorpusArray targetCorpusArray;

    ParallelCorpus parallelCorpus;

    @Test
    public void setupCorpus() throws IOException {
        try {
            System.setOut(new PrintStream(System.out, true, "UTF8"));
            System.setErr(new PrintStream(System.err, true, "UTF8"));
        } catch (UnsupportedEncodingException e1) {
            System.err.println("UTF8 is not a valid encoding; using system default encoding for System.out and System.err.");
        } catch (SecurityException e2) {
            System.err.println("Security manager is configured to disallow changes to System.out or System.err; using system default encoding.");
        }
        String sourceCorpusString = "it makes him and it mars him , it sets him on yet it takes him off ." + "\n" + "resumption of the session ." + "\n" + "of the session" + "\n" + "of the session" + "\n" + "thunder ; lightning" + "\n" + "; blither blather ;";
        String sourceFileName;
        {
            File sourceFile = File.createTempFile("source", new Date().toString());
            PrintStream sourcePrintStream = new PrintStream(sourceFile, "UTF-8");
            sourcePrintStream.println(sourceCorpusString);
            sourcePrintStream.close();
            sourceFileName = sourceFile.getAbsolutePath();
        }
        String targetCorpusString = "das macht ihn und es beschädigt ihn , es setzt ihn auf und es führt ihn aus ." + "\n" + "wiederaufnahme der sitzungsperiode ." + "\n" + "von dem sitzung" + "\n" + "von dem sitzung" + "\n" + "blitzen" + "\n" + "; ;";
        String targetFileName;
        {
            File targetFile = File.createTempFile("target", new Date().toString());
            PrintStream targetPrintStream = new PrintStream(targetFile, "UTF-8");
            targetPrintStream.println(targetCorpusString);
            targetPrintStream.close();
            targetFileName = targetFile.getAbsolutePath();
        }
        String alignmentString = "0-0 1-1 2-2 3-3 4-4 5-5 6-6 7-7 8-8 9-9 10-10 11-11 12-12 13-13 14-14 15-15 16-16 17-17" + "\n" + "0-0 1-1 2-1 3-2 4-3" + "\n" + "0-0 1-1 2-2" + "\n" + "0-0 1-1 2-2" + "\n" + "0-0 2-0" + "\n" + "0-0 3-1";
        String alignmentFileName;
        {
            File alignmentFile = File.createTempFile("alignment", new Date().toString());
            PrintStream alignmentPrintStream = new PrintStream(alignmentFile);
            alignmentPrintStream.println(alignmentString);
            alignmentPrintStream.close();
            alignmentFileName = alignmentFile.getAbsolutePath();
        }
        this.sourceCorpusArray = SuffixArrayFactory.createCorpusArray(sourceFileName);
        this.sourceVocab = (Vocabulary) sourceCorpusArray.getVocabulary();
        SuffixArray sourceSuffixArray = SuffixArrayFactory.createSuffixArray(sourceCorpusArray, SuffixArray.DEFAULT_CACHE_CAPACITY);
        this.targetCorpusArray = SuffixArrayFactory.createCorpusArray(targetFileName);
        this.targetVocab = (Vocabulary) targetCorpusArray.getVocabulary();
        SuffixArray targetSuffixArray = SuffixArrayFactory.createSuffixArray(targetCorpusArray, SuffixArray.DEFAULT_CACHE_CAPACITY);
        this.alignmentArray = SuffixArrayFactory.createAlignments(alignmentFileName, sourceSuffixArray, targetSuffixArray);
        this.parallelCorpus = new AlignedParallelCorpus(sourceCorpusArray, targetCorpusArray, alignmentArray);
    }

    @Test(dependsOnMethods = { "setupCorpus" })
    public void verifyCorpusCounts() {
        this.lexProbs = new LexProbs(parallelCorpus, Float.MIN_VALUE);
        Counts<Integer, Integer> counts = lexProbs.getCounts();
        Assert.assertEquals(counts.getCount(sourceVocab.getID(";"), targetVocab.getID(";")), 2);
        Assert.assertEquals(counts.getCount(sourceVocab.getID(";"), null), 1);
    }

    @Test(dependsOnMethods = { "setupCorpus", "verifyCorpusCounts" })
    public void setup() {
    }

    @Test(dependsOnMethods = { "setup" })
    public void verifyTargetVocabulary() {
        Assert.assertEquals(targetVocab.getWord(targetVocab.getID("das")), "das");
        Assert.assertEquals(targetVocab.getWord(targetVocab.getID("macht")), "macht");
        Assert.assertEquals(targetVocab.getWord(targetVocab.getID("ihn")), "ihn");
        Assert.assertEquals(targetVocab.getWord(targetVocab.getID("und")), "und");
        Assert.assertEquals(targetVocab.getWord(targetVocab.getID("es")), "es");
        Assert.assertEquals(targetVocab.getWord(targetVocab.getID("setzt")), "setzt");
        Assert.assertEquals(targetVocab.getWord(targetVocab.getID(",")), ",");
        Assert.assertEquals(targetVocab.getWord(targetVocab.getID("auf")), "auf");
        Assert.assertEquals(targetVocab.getWord(targetVocab.getID("und")), "und");
        Assert.assertEquals(targetVocab.getWord(targetVocab.getID("aus")), "aus");
        Assert.assertEquals(targetVocab.getWord(targetVocab.getID(".")), ".");
        Assert.assertEquals(targetVocab.getWord(targetVocab.getID("wiederaufnahme")), "wiederaufnahme");
        Assert.assertEquals(targetVocab.getWord(targetVocab.getID("der")), "der");
        Assert.assertEquals(targetVocab.getWord(targetVocab.getID("sitzungsperiode")), "sitzungsperiode");
        Assert.assertEquals(targetVocab.getWord(targetVocab.getID("von")), "von");
        Assert.assertEquals(targetVocab.getWord(targetVocab.getID("dem")), "dem");
        Assert.assertEquals(targetVocab.getWord(targetVocab.getID("sitzung")), "sitzung");
        Assert.assertEquals(targetVocab.getWord(targetVocab.getID("führt")), "führt");
        Assert.assertEquals(targetVocab.getWord(targetVocab.getID("beschädigt")), "beschädigt");
    }

    @Test(dependsOnMethods = { "setup" })
    public void testAlignmentPoints() {
        for (int i = 0; i < 18; i++) {
            int[] sourceIndices = alignmentArray.getAlignedSourceIndices(i);
            Assert.assertNotNull(sourceIndices);
            Assert.assertEquals(sourceIndices.length, 1);
            Assert.assertEquals(sourceIndices[0], i);
            int[] targetIndices = alignmentArray.getAlignedTargetIndices(i);
            Assert.assertNotNull(targetIndices);
            Assert.assertEquals(targetIndices.length, 1);
            Assert.assertEquals(targetIndices[0], i);
        }
    }

    @Test(dependsOnMethods = { "setup" })
    public void calculateLexProbs() {
        int phraseIndex = 0;
        {
            HierarchicalPhrases phrases = getSourcePhrase("it", 0, 1);
            HierarchicalPhrase targetPhrase = getTargetPhrase("das", 0, 1);
            float sourceGivenTarget = lexProbs.lexProbSourceGivenTarget(phrases, phraseIndex, targetPhrase);
            float targetGivenSource = lexProbs.lexProbTargetGivenSource(phrases, phraseIndex, targetPhrase);
            Assert.assertEquals(sourceGivenTarget, 1.0f);
            Assert.assertEquals(targetGivenSource, 0.25f);
        }
        {
            HierarchicalPhrases phrases = getSourcePhrase("makes", 1, 2);
            HierarchicalPhrase targetPhrase = getTargetPhrase("macht", 1, 2);
            float sourceGivenTarget = lexProbs.lexProbSourceGivenTarget(phrases, phraseIndex, targetPhrase);
            float targetGivenSource = lexProbs.lexProbTargetGivenSource(phrases, phraseIndex, targetPhrase);
            Assert.assertEquals(sourceGivenTarget, 1.0f);
            Assert.assertEquals(targetGivenSource, 1.0f);
        }
        {
            HierarchicalPhrases phrases = getSourcePhrase("him", 2, 3);
            HierarchicalPhrase targetPhrase = getTargetPhrase("ihn", 2, 3);
            float sourceGivenTarget = lexProbs.lexProbSourceGivenTarget(phrases, phraseIndex, targetPhrase);
            float targetGivenSource = lexProbs.lexProbTargetGivenSource(phrases, phraseIndex, targetPhrase);
            Assert.assertEquals(sourceGivenTarget, 1.0f);
            Assert.assertEquals(targetGivenSource, 1.0f);
        }
        {
            HierarchicalPhrases phrases = getSourcePhrase("and", 3, 4);
            HierarchicalPhrase targetPhrase = getTargetPhrase("und", 3, 4);
            float sourceGivenTarget = lexProbs.lexProbSourceGivenTarget(phrases, phraseIndex, targetPhrase);
            float targetGivenSource = lexProbs.lexProbTargetGivenSource(phrases, phraseIndex, targetPhrase);
            Assert.assertEquals(sourceGivenTarget, 0.5f);
            Assert.assertEquals(targetGivenSource, 1.0f);
        }
        {
            HierarchicalPhrases phrases = getSourcePhrase("it", 4, 5);
            HierarchicalPhrase targetPhrase = getTargetPhrase("es", 4, 5);
            float sourceGivenTarget = lexProbs.lexProbSourceGivenTarget(phrases, phraseIndex, targetPhrase);
            float targetGivenSource = lexProbs.lexProbTargetGivenSource(phrases, phraseIndex, targetPhrase);
            Assert.assertEquals(sourceGivenTarget, 1.0f);
            Assert.assertEquals(targetGivenSource, 0.75f);
        }
        {
            HierarchicalPhrases phrases = getSourcePhrase("mars", 5, 6);
            HierarchicalPhrase targetPhrase = getTargetPhrase("beschädigt", 5, 6);
            float sourceGivenTarget = lexProbs.lexProbSourceGivenTarget(phrases, phraseIndex, targetPhrase);
            float targetGivenSource = lexProbs.lexProbTargetGivenSource(phrases, phraseIndex, targetPhrase);
            Assert.assertEquals(sourceGivenTarget, 1.0f);
            Assert.assertEquals(targetGivenSource, 1.0f);
        }
        {
            HierarchicalPhrases phrases = getSourcePhrase("him", 6, 7);
            HierarchicalPhrase targetPhrase = getTargetPhrase("ihn", 6, 7);
            float sourceGivenTarget = lexProbs.lexProbSourceGivenTarget(phrases, phraseIndex, targetPhrase);
            float targetGivenSource = lexProbs.lexProbTargetGivenSource(phrases, phraseIndex, targetPhrase);
            Assert.assertEquals(sourceGivenTarget, 1.0f);
            Assert.assertEquals(targetGivenSource, 1.0f);
        }
        {
            HierarchicalPhrases phrases = getSourcePhrase(",", 7, 8);
            HierarchicalPhrase targetPhrase = getTargetPhrase(",", 7, 8);
            float sourceGivenTarget = lexProbs.lexProbSourceGivenTarget(phrases, phraseIndex, targetPhrase);
            float targetGivenSource = lexProbs.lexProbTargetGivenSource(phrases, phraseIndex, targetPhrase);
            Assert.assertEquals(sourceGivenTarget, 1.0f);
            Assert.assertEquals(targetGivenSource, 1.0f);
        }
        {
            HierarchicalPhrases phrases = getSourcePhrase("it", 8, 9);
            HierarchicalPhrase targetPhrase = getTargetPhrase("es", 8, 9);
            float sourceGivenTarget = lexProbs.lexProbSourceGivenTarget(phrases, phraseIndex, targetPhrase);
            float targetGivenSource = lexProbs.lexProbTargetGivenSource(phrases, phraseIndex, targetPhrase);
            Assert.assertEquals(sourceGivenTarget, 1.0f);
            Assert.assertEquals(targetGivenSource, 0.75f);
        }
        {
            HierarchicalPhrases phrases = getSourcePhrase("sets", 9, 10);
            HierarchicalPhrase targetPhrase = getTargetPhrase("setzt", 9, 10);
            float sourceGivenTarget = lexProbs.lexProbSourceGivenTarget(phrases, phraseIndex, targetPhrase);
            float targetGivenSource = lexProbs.lexProbTargetGivenSource(phrases, phraseIndex, targetPhrase);
            Assert.assertEquals(sourceGivenTarget, 1.0f);
            Assert.assertEquals(targetGivenSource, 1.0f);
        }
        {
            HierarchicalPhrases phrases = getSourcePhrase("him", 10, 11);
            HierarchicalPhrase targetPhrase = getTargetPhrase("ihn", 10, 11);
            float sourceGivenTarget = lexProbs.lexProbSourceGivenTarget(phrases, phraseIndex, targetPhrase);
            float targetGivenSource = lexProbs.lexProbTargetGivenSource(phrases, phraseIndex, targetPhrase);
            Assert.assertEquals(sourceGivenTarget, 1.0f);
            Assert.assertEquals(targetGivenSource, 1.0f);
        }
        {
            HierarchicalPhrases phrases = getSourcePhrase("on", 11, 12);
            HierarchicalPhrase targetPhrase = getTargetPhrase("auf", 11, 12);
            float sourceGivenTarget = lexProbs.lexProbSourceGivenTarget(phrases, phraseIndex, targetPhrase);
            float targetGivenSource = lexProbs.lexProbTargetGivenSource(phrases, phraseIndex, targetPhrase);
            Assert.assertEquals(sourceGivenTarget, 1.0f);
            Assert.assertEquals(targetGivenSource, 1.0f);
        }
        {
            HierarchicalPhrases phrases = getSourcePhrase("yet", 12, 13);
            HierarchicalPhrase targetPhrase = getTargetPhrase("und", 12, 13);
            float sourceGivenTarget = lexProbs.lexProbSourceGivenTarget(phrases, phraseIndex, targetPhrase);
            float targetGivenSource = lexProbs.lexProbTargetGivenSource(phrases, phraseIndex, targetPhrase);
            Assert.assertEquals(sourceGivenTarget, 0.5f);
            Assert.assertEquals(targetGivenSource, 1.0f);
        }
        {
            HierarchicalPhrases phrases = getSourcePhrase("it", 13, 14);
            HierarchicalPhrase targetPhrase = getTargetPhrase("es", 13, 14);
            float sourceGivenTarget = lexProbs.lexProbSourceGivenTarget(phrases, phraseIndex, targetPhrase);
            float targetGivenSource = lexProbs.lexProbTargetGivenSource(phrases, phraseIndex, targetPhrase);
            Assert.assertEquals(sourceGivenTarget, 1.0f);
            Assert.assertEquals(targetGivenSource, 0.75f);
        }
        {
            HierarchicalPhrases phrases = getSourcePhrase("takes", 14, 15);
            HierarchicalPhrase targetPhrase = getTargetPhrase("führt", 14, 15);
            float sourceGivenTarget = lexProbs.lexProbSourceGivenTarget(phrases, phraseIndex, targetPhrase);
            float targetGivenSource = lexProbs.lexProbTargetGivenSource(phrases, phraseIndex, targetPhrase);
            Assert.assertEquals(sourceGivenTarget, 1.0f);
            Assert.assertEquals(targetGivenSource, 1.0f);
        }
        {
            HierarchicalPhrases phrases = getSourcePhrase("him", 15, 16);
            HierarchicalPhrase targetPhrase = getTargetPhrase("ihn", 15, 16);
            float sourceGivenTarget = lexProbs.lexProbSourceGivenTarget(phrases, phraseIndex, targetPhrase);
            float targetGivenSource = lexProbs.lexProbTargetGivenSource(phrases, phraseIndex, targetPhrase);
            Assert.assertEquals(sourceGivenTarget, 1.0f);
            Assert.assertEquals(targetGivenSource, 1.0f);
        }
        {
            HierarchicalPhrases phrases = getSourcePhrase("off", 16, 17);
            HierarchicalPhrase targetPhrase = getTargetPhrase("aus", 16, 17);
            float sourceGivenTarget = lexProbs.lexProbSourceGivenTarget(phrases, phraseIndex, targetPhrase);
            float targetGivenSource = lexProbs.lexProbTargetGivenSource(phrases, phraseIndex, targetPhrase);
            Assert.assertEquals(sourceGivenTarget, 1.0f);
            Assert.assertEquals(targetGivenSource, 1.0f);
        }
        {
            HierarchicalPhrases phrases = getSourcePhrase(".", 17, 18);
            HierarchicalPhrase targetPhrase = getTargetPhrase(".", 17, 18);
            float sourceGivenTarget = lexProbs.lexProbSourceGivenTarget(phrases, phraseIndex, targetPhrase);
            float targetGivenSource = lexProbs.lexProbTargetGivenSource(phrases, phraseIndex, targetPhrase);
            Assert.assertEquals(sourceGivenTarget, 1.0f);
            Assert.assertEquals(targetGivenSource, 1.0f);
        }
        {
            HierarchicalPhrases phrases = getSourcePhrase("yet it", 12, 14);
            HierarchicalPhrase targetPhrase = getTargetPhrase("und es", 12, 14);
            float sourceGivenTarget = lexProbs.lexProbSourceGivenTarget(phrases, phraseIndex, targetPhrase);
            float targetGivenSource = lexProbs.lexProbTargetGivenSource(phrases, phraseIndex, targetPhrase);
            Assert.assertEquals(sourceGivenTarget, 0.5f * 1.0f);
            Assert.assertEquals(targetGivenSource, 1.0f * 0.75f);
        }
        {
            HierarchicalPhrases phrases = getSourcePhrase("of the session", 19, 22);
            HierarchicalPhrase targetPhrase = getTargetPhrase("der sitzungsperiode", 19, 21);
            float sourceGivenTarget = lexProbs.lexProbSourceGivenTarget(phrases, phraseIndex, targetPhrase);
            float targetGivenSource = lexProbs.lexProbTargetGivenSource(phrases, phraseIndex, targetPhrase);
            Assert.assertEquals(sourceGivenTarget, 0.5f * 0.5f * 1.0f);
            Assert.assertEquals(targetGivenSource, 0.5f * ((1.0f / 3.0f) + (1.0f / 3.0f)) * (1.0f / 3.0f));
        }
        {
            HierarchicalPhrases phrases = getSourcePhrase("thunder ; lightning", 29, 32);
            HierarchicalPhrase targetPhrase = getTargetPhrase("blitzen", 28, 29);
            float sourceGivenTarget = lexProbs.lexProbSourceGivenTarget(phrases, phraseIndex, targetPhrase);
            float targetGivenSource = lexProbs.lexProbTargetGivenSource(phrases, phraseIndex, targetPhrase);
            Assert.assertEquals(sourceGivenTarget, 0.5f * (1.0f / 3.0f) * 0.5f);
            Assert.assertEquals(targetGivenSource, ((1.0f / 2.0f) * (1.0f + 1.0f)));
        }
    }

    /**
	 * Unit test to verify correct calculation of
	 * lexical translation probabilities for phrases with gaps.
	 */
    @Test(dependsOnMethods = { "setup" })
    public void calculateHieroLexProbs() {
        Pattern pattern = new Pattern(sourceVocab, sourceVocab.getID("it"), SymbolTable.X, sourceVocab.getID("and"), sourceVocab.getID("it"));
        int[] terminalSequenceStartIndices = { 0, 3 };
        int phraseIndex = 0;
        int[] sentenceNumbers = { 0 };
        HierarchicalPhrases phrases = new HierarchicalPhrases(pattern, terminalSequenceStartIndices, sentenceNumbers);
        int[] targetWords = { targetVocab.getID("das"), SymbolTable.X, targetVocab.getID("und"), targetVocab.getID("es") };
        HierarchicalPhrase targetPhrase = new HierarchicalPhrase(targetWords, new Span(0, 5), Collections.<LabeledSpan>emptyList(), targetCorpusArray);
        float sourceGivenTarget = lexProbs.lexProbSourceGivenTarget(phrases, phraseIndex, targetPhrase);
        float targetGivenSource = lexProbs.lexProbTargetGivenSource(phrases, phraseIndex, targetPhrase);
        Assert.assertEquals(sourceGivenTarget, 1.0f * 0.5f * 1.0f);
        Assert.assertEquals(targetGivenSource, 0.25f * 1.0f * 0.75f);
    }

    private HierarchicalPhrases getSourcePhrase(String sourcePhrase, int startIndex, int endIndex) {
        Pattern pattern = new Pattern(sourceVocab, sourceVocab.getIDs(sourcePhrase));
        int[] terminalSequenceStartIndices = { startIndex };
        int[] sentenceNumbers = { 0 };
        HierarchicalPhrases phrases = new HierarchicalPhrases(pattern, terminalSequenceStartIndices, sentenceNumbers);
        return phrases;
    }

    private HierarchicalPhrase getTargetPhrase(String targetPhrase, int startIndex, int endIndex) {
        return new HierarchicalPhrase(targetVocab.getIDs(targetPhrase), new Span(startIndex, endIndex), Collections.<LabeledSpan>emptyList(), targetCorpusArray);
    }

    @Test(dependsOnMethods = { "setup" })
    public void testSourceGivenTargetString() {
        Assert.assertEquals(lexProbs.sourceGivenTarget(",", ","), 1.0f);
        Assert.assertEquals(lexProbs.sourceGivenTarget(".", "."), 1.0f);
        Assert.assertEquals(lexProbs.sourceGivenTarget("on", "auf"), 1.0f);
        Assert.assertEquals(lexProbs.sourceGivenTarget("off", "aus"), 1.0f);
        Assert.assertEquals(lexProbs.sourceGivenTarget("mars", "beschädigt"), 1.0f);
        Assert.assertEquals(lexProbs.sourceGivenTarget("it", "das"), 1.0f);
        Assert.assertEquals(lexProbs.sourceGivenTarget("it", "es"), 1.0f);
        Assert.assertEquals(lexProbs.sourceGivenTarget("takes", "führt"), 1.0f);
        Assert.assertEquals(lexProbs.sourceGivenTarget("him", "ihn"), 1.0f);
        Assert.assertEquals(lexProbs.sourceGivenTarget("makes", "macht"), 1.0f);
        Assert.assertEquals(lexProbs.sourceGivenTarget("sets", "setzt"), 1.0f);
        Assert.assertEquals(lexProbs.sourceGivenTarget("and", "und"), 0.5f);
        Assert.assertEquals(lexProbs.sourceGivenTarget("yet", "und"), 0.5f);
        Assert.assertEquals(lexProbs.sourceGivenTarget(";", null), (1.0f / 3.0f));
    }

    @Test(dependsOnMethods = { "setup" })
    public void testSourceGivenTargetStringUnaligned() {
        float floorProbability = lexProbs.getFloorProbability();
        Assert.assertEquals(lexProbs.sourceGivenTarget(",", "."), floorProbability);
        Assert.assertEquals(lexProbs.sourceGivenTarget(".", ","), floorProbability);
        Assert.assertEquals(lexProbs.sourceGivenTarget("on", "aoeuaeoa"), floorProbability);
        Assert.assertEquals(lexProbs.sourceGivenTarget("off", "das"), floorProbability);
        Assert.assertEquals(lexProbs.sourceGivenTarget("mars", "das"), floorProbability);
        Assert.assertEquals(lexProbs.sourceGivenTarget("it", "beschädigt"), floorProbability);
        Assert.assertEquals(lexProbs.sourceGivenTarget("it", "führt"), floorProbability);
        Assert.assertEquals(lexProbs.sourceGivenTarget("takes", "."), floorProbability);
        Assert.assertEquals(lexProbs.sourceGivenTarget("him", "es"), floorProbability);
        Assert.assertEquals(lexProbs.sourceGivenTarget("makes", ","), floorProbability);
        Assert.assertEquals(lexProbs.sourceGivenTarget("sets", "."), floorProbability);
        Assert.assertEquals(lexProbs.sourceGivenTarget("and", "es"), floorProbability);
        Assert.assertEquals(lexProbs.sourceGivenTarget("yet", "das"), floorProbability);
    }

    @Test(dependsOnMethods = { "setup" })
    public void testSourceGivenTarget() {
        Assert.assertEquals(lexProbs.sourceGivenTarget(sourceVocab.getID(","), targetVocab.getID(",")), 1.0f);
        Assert.assertEquals(lexProbs.sourceGivenTarget(sourceVocab.getID("."), targetVocab.getID(".")), 1.0f);
        Assert.assertEquals(lexProbs.sourceGivenTarget(sourceVocab.getID("on"), targetVocab.getID("auf")), 1.0f);
        Assert.assertEquals(lexProbs.sourceGivenTarget(sourceVocab.getID("off"), targetVocab.getID("aus")), 1.0f);
        Assert.assertEquals(lexProbs.sourceGivenTarget(sourceVocab.getID("mars"), targetVocab.getID("beschädigt")), 1.0f);
        Assert.assertEquals(lexProbs.sourceGivenTarget(sourceVocab.getID("it"), targetVocab.getID("das")), 1.0f);
        Assert.assertEquals(lexProbs.sourceGivenTarget(sourceVocab.getID("it"), targetVocab.getID("es")), 1.0f);
        Assert.assertEquals(lexProbs.sourceGivenTarget(sourceVocab.getID("takes"), targetVocab.getID("führt")), 1.0f);
        Assert.assertEquals(lexProbs.sourceGivenTarget(sourceVocab.getID("him"), targetVocab.getID("ihn")), 1.0f);
        Assert.assertEquals(lexProbs.sourceGivenTarget(sourceVocab.getID("makes"), targetVocab.getID("macht")), 1.0f);
        Assert.assertEquals(lexProbs.sourceGivenTarget(sourceVocab.getID("sets"), targetVocab.getID("setzt")), 1.0f);
        Assert.assertEquals(lexProbs.sourceGivenTarget(sourceVocab.getID("and"), targetVocab.getID("und")), 0.5f);
        Assert.assertEquals(lexProbs.sourceGivenTarget(sourceVocab.getID("yet"), targetVocab.getID("und")), 0.5f);
    }

    @Test(dependsOnMethods = { "setup" })
    public void testTargetGivenSourceString() {
        Assert.assertEquals(lexProbs.targetGivenSource(",", ","), 1.0f);
        Assert.assertEquals(lexProbs.targetGivenSource(".", "."), 1.0f);
        Assert.assertEquals(lexProbs.targetGivenSource("und", "and"), 1.0f);
        Assert.assertEquals(lexProbs.targetGivenSource("ihn", "him"), 1.0f);
        Assert.assertEquals(lexProbs.targetGivenSource("das", "it"), 0.25f);
        Assert.assertEquals(lexProbs.targetGivenSource("es", "it"), 0.75f);
        Assert.assertEquals(lexProbs.targetGivenSource("macht", "makes"), 1.0f);
        Assert.assertEquals(lexProbs.targetGivenSource("beschädigt", "mars"), 1.0f);
        Assert.assertEquals(lexProbs.targetGivenSource("aus", "off"), 1.0f);
        Assert.assertEquals(lexProbs.targetGivenSource("auf", "on"), 1.0f);
        Assert.assertEquals(lexProbs.targetGivenSource("setzt", "sets"), 1.0f);
        Assert.assertEquals(lexProbs.targetGivenSource("führt", "takes"), 1.0f);
        Assert.assertEquals(lexProbs.targetGivenSource("und", "yet"), 1.0f);
    }

    @Test(dependsOnMethods = { "setup" })
    public void testTargetGivenSource() {
        Assert.assertEquals(lexProbs.targetGivenSource(targetVocab.getID(","), sourceVocab.getID(",")), 1.0f);
        Assert.assertEquals(lexProbs.targetGivenSource(targetVocab.getID("."), sourceVocab.getID(".")), 1.0f);
        Assert.assertEquals(lexProbs.targetGivenSource(targetVocab.getID("und"), sourceVocab.getID("and")), 1.0f);
        Assert.assertEquals(lexProbs.targetGivenSource(targetVocab.getID("ihn"), sourceVocab.getID("him")), 1.0f);
        Assert.assertEquals(lexProbs.targetGivenSource(targetVocab.getID("das"), sourceVocab.getID("it")), 0.25f);
        Assert.assertEquals(lexProbs.targetGivenSource(targetVocab.getID("es"), sourceVocab.getID("it")), 0.75f);
        Assert.assertEquals(lexProbs.targetGivenSource(targetVocab.getID("macht"), sourceVocab.getID("makes")), 1.0f);
        Assert.assertEquals(lexProbs.targetGivenSource(targetVocab.getID("beschädigt"), sourceVocab.getID("mars")), 1.0f);
        Assert.assertEquals(lexProbs.targetGivenSource(targetVocab.getID("aus"), sourceVocab.getID("off")), 1.0f);
        Assert.assertEquals(lexProbs.targetGivenSource(targetVocab.getID("auf"), sourceVocab.getID("on")), 1.0f);
        Assert.assertEquals(lexProbs.targetGivenSource(targetVocab.getID("setzt"), sourceVocab.getID("sets")), 1.0f);
        Assert.assertEquals(lexProbs.targetGivenSource(targetVocab.getID("führt"), sourceVocab.getID("takes")), 1.0f);
        Assert.assertEquals(lexProbs.targetGivenSource(targetVocab.getID("und"), sourceVocab.getID("yet")), 1.0f);
    }
}
