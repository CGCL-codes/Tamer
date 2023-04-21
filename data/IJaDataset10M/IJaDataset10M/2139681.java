package edu.colorado.emml.ensemble_deprecated;

import edu.colorado.emml.ensemble.PredictionSet;
import edu.colorado.emml.util.ArrayAssert;

/**
 * Created by: Sam
 * Jan 10, 2008 at 7:10:10 PM
 */
public class ZCompositeEnsembleModelTester extends ArrayAssert {

    public void testSimpleAverage() {
        final double[] a1 = { 1, 2, 1 };
        final double[] a2 = { 3, 4, 5 };
        final double[] a3 = { 2, 3, 3 };
        testSingleCombination(a1, a2, a3, new ICombinationRule.Average());
    }

    public void testSimpleVote() {
        final double[] a1 = { 0.3, 0.2, 0.5 };
        final double[] a2 = { 0.4, 0.1, 0.5 };
        final double[] a3 = { 0, 0, 1.0 };
        testSingleCombination(a1, a2, a3, new ICombinationRule.Vote());
    }

    public void testSimpleAverageIdentity() {
        final double[] a1 = { 1, 2, 3 };
        final double[] a2 = { 1, 2, 3 };
        final double[] a3 = { 1, 2, 3 };
        testSingleCombination(a1, a2, a3, new ICombinationRule.Average());
    }

    private void testSingleCombination(double[] x1, double[] x2, double[] y, ICombinationRule combinationRule) {
        ConstantEnsembleModel a = new ConstantEnsembleModel(new PredictionSet(x1));
        ConstantEnsembleModel b = new ConstantEnsembleModel(new PredictionSet(x2));
        CompositeEnsembleModel c = new CompositeEnsembleModel(combinationRule, new IEnsembleModel[] { a, b });
        IPredictionSet x = c.getEnsemblePrediction(null);
        assertEquals("should have received 1 prediction", x.getPredictionCount(), 1);
        double[] p = x.getPrediction(0);
        assertArrayEquals(p, y);
    }
}
