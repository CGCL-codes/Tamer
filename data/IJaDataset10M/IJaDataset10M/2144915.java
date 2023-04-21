package joshua.decoder.ff;

import joshua.decoder.ff.tm.BilingualRule;
import joshua.decoder.ff.tm.MonolingualRule;
import joshua.decoder.ff.tm.Rule;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit tests for ArityPhrasePenaltyFF.
 * 
 * @author Lane Schwartz
 * @version $LastChangedDate: 2010-01-18 15:39:52 -0500 (Mon, 18 Jan 2010) $
 */
public class ArityPhrasePenaltyFFTest {

    @Test
    public void alpha() {
        Assert.assertEquals(ArityPhrasePenaltyFF.ALPHA, -Math.log10(Math.E));
    }

    @Test
    public void estimate() {
        int featureID = 0;
        double weight = 0.0;
        int owner = MonolingualRule.DUMMY_OWNER;
        int min = 1;
        int max = 5;
        ArityPhrasePenaltyFF featureFunction = new ArityPhrasePenaltyFF(featureID, weight, owner, min, max);
        int lhs = -1;
        int[] sourceRHS = { 24, -1, 42, 738 };
        int[] targetRHS = { -1, 7, 8 };
        float[] featureScores = { -2.35f, -1.78f, -0.52f };
        int arity = 1;
        Rule dummyRule = new BilingualRule(lhs, sourceRHS, targetRHS, featureScores, arity);
        Assert.assertEquals(featureFunction.estimateLogP(dummyRule, -1), ArityPhrasePenaltyFF.ALPHA);
    }
}
