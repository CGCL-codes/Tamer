package joshua.zmert;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import joshua.zmert.BLEU;
import joshua.zmert.EvaluationMetric;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * Unit tests for BLEU class.
 * 
 * @author Lane Schwartz
 * @version $LastChangedDate: 2009-05-04 10:56:46 -0400 (Mon, 04 May 2009) $
 */
public class BLEUTest {

    @Test
    public void metricName() {
        EvaluationMetric.set_numSentences(0);
        EvaluationMetric.set_refsPerSen(1);
        EvaluationMetric.set_refSentences(null);
        BLEU bleu = new BLEU();
        Assert.assertEquals(bleu.get_metricName(), "BLEU");
    }

    @Test
    public void defaultConstructor() {
        EvaluationMetric.set_numSentences(0);
        EvaluationMetric.set_refsPerSen(1);
        EvaluationMetric.set_refSentences(null);
        BLEU bleu = new BLEU();
        Assert.assertEquals(bleu.maxGramLength, 4);
        Assert.assertEquals(bleu.effLengthMethod, BLEU.EffectiveLengthMethod.CLOSEST);
    }

    @Test
    public void simpleTest() {
        String ref = "this is the fourth chromosome whose sequence has been completed to date . it comprises more than 87 million pairs of dna .";
        String test = "this is the fourth chromosome to be fully sequenced up till now and it comprises of over 87 million pairs of deoxyribonucleic acid ( dna ) .";
        String[][] refSentences = new String[1][1];
        refSentences[0][0] = ref;
        EvaluationMetric.set_numSentences(1);
        EvaluationMetric.set_refsPerSen(1);
        EvaluationMetric.set_refSentences(refSentences);
        BLEU bleu = new BLEU();
        String[] testSentences = new String[1];
        testSentences[0] = test;
        try {
            double actualScore = bleu.score(testSentences);
            double expectedScore = 0.2513;
            double acceptableScoreDelta = 0.00001f;
            Assert.assertEquals(actualScore, expectedScore, acceptableScoreDelta);
            int[] actualSS = bleu.suffStats(testSentences);
            int[] expectedSS = { 14, 27, 8, 26, 5, 25, 3, 24, 27, 23 };
            Assert.assertEquals(actualSS[0], expectedSS[0], 0);
            Assert.assertEquals(actualSS[1], expectedSS[1], 0);
            Assert.assertEquals(actualSS[2], expectedSS[2], 0);
            Assert.assertEquals(actualSS[3], expectedSS[3], 0);
            Assert.assertEquals(actualSS[4], expectedSS[4], 0);
            Assert.assertEquals(actualSS[5], expectedSS[5], 0);
            Assert.assertEquals(actualSS[6], expectedSS[6], 0);
            Assert.assertEquals(actualSS[7], expectedSS[7], 0);
            Assert.assertEquals(actualSS[8], expectedSS[8], 0);
            Assert.assertEquals(actualSS[9], expectedSS[9], 0);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Parameters({ "referenceFile", "testFile" })
    @Test
    public void fileTest(String referenceFile, String testFile) throws FileNotFoundException {
        Scanner refScanner = new Scanner(new File(referenceFile));
        while (refScanner.hasNextLine()) {
            String refLine = refScanner.nextLine();
        }
    }
}
