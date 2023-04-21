package joshua.decoder;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.Scanner;
import joshua.corpus.Corpus;
import joshua.corpus.alignment.AlignmentGrids;
import joshua.corpus.suffix_array.Compile;
import joshua.corpus.suffix_array.SuffixArrayFactory;
import joshua.corpus.vocab.Vocabulary;
import joshua.prefix_tree.ExtractRules;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit tests for decoder thread.
 * 
 * @author Lane Schwartz
 * @version $LastChangedDate: 2009-08-28 12:02:40 -0400 (Fri, 28 Aug 2009) $
 */
public class DecoderThreadTest {

    @Test
    public void setup() {
        String[] sourceSentences = { "a b c d", "a b c d", "a b c d" };
        String[] targetSentences = { "w x y z", "w t u v", "s x y z" };
        String[] alignmentLines = { "0-0 1-1 2-2 3-3", "0-0 1-1 2-2 3-3", "0-0 1-1 2-2 3-3" };
        String[] testSentences = { "a b c" };
        try {
            File sourceFile = File.createTempFile("source", new Date().toString());
            PrintStream sourcePrintStream = new PrintStream(sourceFile, "UTF-8");
            for (String sentence : sourceSentences) {
                sourcePrintStream.println(sentence);
            }
            sourcePrintStream.close();
            String sourceCorpusFileName = sourceFile.getAbsolutePath();
            Vocabulary symbolTable = new Vocabulary();
            int[] sourceLengths = Vocabulary.initializeVocabulary(sourceCorpusFileName, symbolTable, true);
            Assert.assertEquals(sourceLengths.length, 2);
            int numberOfSentences = sourceLengths[1];
            Corpus sourceCorpus = SuffixArrayFactory.createCorpusArray(sourceCorpusFileName, symbolTable, sourceLengths[0], sourceLengths[1]);
            File targetFile = File.createTempFile("target", new Date().toString());
            PrintStream targetPrintStream = new PrintStream(targetFile, "UTF-8");
            for (String sentence : targetSentences) {
                targetPrintStream.println(sentence);
            }
            targetPrintStream.close();
            String targetCorpusFileName = targetFile.getAbsolutePath();
            int[] targetLengths = Vocabulary.initializeVocabulary(targetCorpusFileName, symbolTable, true);
            Assert.assertEquals(targetLengths.length, sourceLengths.length);
            for (int i = 0, n = targetLengths.length; i < n; i++) {
                Assert.assertEquals(targetLengths[i], sourceLengths[i]);
            }
            Corpus targetCorpus = SuffixArrayFactory.createCorpusArray(targetCorpusFileName, symbolTable, targetLengths[0], targetLengths[1]);
            File alignmentsFile = File.createTempFile("alignments", new Date().toString());
            PrintStream alignmentsPrintStream = new PrintStream(alignmentsFile, "UTF-8");
            for (String sentence : alignmentLines) {
                alignmentsPrintStream.println(sentence);
            }
            alignmentsPrintStream.close();
            String alignmentFileName = alignmentsFile.getAbsolutePath();
            AlignmentGrids grids = new AlignmentGrids(new Scanner(alignmentsFile), sourceCorpus, targetCorpus, numberOfSentences);
            File testFile = File.createTempFile("test", new Date().toString());
            PrintStream testPrintStream = new PrintStream(testFile, "UTF-8");
            for (String sentence : testSentences) {
                testPrintStream.println(sentence);
            }
            testPrintStream.close();
            String testFileName = testFile.getAbsolutePath();
            String rulesFileName;
            {
                File rulesFile = File.createTempFile("rules", new Date().toString());
                rulesFileName = rulesFile.getAbsolutePath();
            }
            String joshDirName;
            {
                File joshDir = File.createTempFile(new Date().toString(), "josh");
                joshDirName = joshDir.getAbsolutePath();
                joshDir.delete();
            }
            Compile compileJoshDir = new Compile();
            compileJoshDir.setSourceCorpus(sourceCorpusFileName);
            compileJoshDir.setTargetCorpus(targetCorpusFileName);
            compileJoshDir.setAlignments(alignmentFileName);
            compileJoshDir.setOutputDir(joshDirName);
            compileJoshDir.execute();
            ExtractRules extractRules = new ExtractRules();
            extractRules.setJoshDir(joshDirName);
            extractRules.setTestFile(testFileName);
            extractRules.setOutputFile(rulesFileName);
            extractRules.execute();
        } catch (IOException e) {
            Assert.fail("Unable to write temporary file. " + e.toString());
        } catch (ClassNotFoundException e) {
            Assert.fail("Unable to extract rules. " + e.toString());
        }
    }

    @Test
    public void basicSuffixArrayGrammar() {
    }
}
