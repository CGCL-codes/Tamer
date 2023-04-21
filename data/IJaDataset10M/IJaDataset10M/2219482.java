package org.decisiondeck.xmcda_oo.concordance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.decisiondeck.xmcda_oo.data.SixRealCarsData;
import org.decisiondeck.xmcda_oo.structure.Alternative;
import org.decisiondeck.xmcda_oo.structure.Criterion;
import org.decisiondeck.xmcda_oo.structure.EvaluationMatrix;
import org.decisiondeck.xmcda_oo.structure.Criterion.PreferenceDirection;
import org.decisiondeck.xmcda_oo.utils.matrix.IFuzzyMatrix;
import org.decisiondeck.xmcda_oo.utils.matrix.OneMinusInverseMatrix;
import org.junit.Test;

public class ConcordanceTest {

    @Test
    public void testConcordance() throws Exception {
        final SixRealCarsData testData = new SixRealCarsData();
        final Concordance conc = new Concordance();
        final IFuzzyMatrix<Alternative, Alternative> res = conc.concordance(testData.retrieveAlternatives(), testData.retrieveCriteria(), testData.retrievePrefs(), testData.retrieveIndiffs(), testData.retrieveWeights(), testData.retrieveEvaluations());
        assertTrue("Concordance does not match.", testData.getConcordance().approxEquals(res, 0.00005f));
    }

    @Test
    public void testConcordanceEquality() throws Exception {
        final Concordance conc = new Concordance();
        final EvaluationMatrix eval = new EvaluationMatrix();
        final Alternative alt1 = new Alternative("01", "01");
        final Alternative alt2 = new Alternative("02", "02");
        final Criterion g = new Criterion("g1", "g1", PreferenceDirection.MAXIMIZE);
        eval.put(alt1, g, 3);
        eval.put(alt2, g, 3);
        final double res = conc.concordancePairwize(alt1, alt2, g, null, null, eval);
        assertEquals("Electre1-style concordance for equal perf must be one.", 1d, res, 0.00005f);
    }

    @Test
    public void testPreference() throws Exception {
        final SixRealCarsData testData = new SixRealCarsData();
        final Concordance conc = new Concordance();
        final IFuzzyMatrix<Alternative, Alternative> res = conc.preference(testData.retrieveAlternatives(), testData.retrieveCriteria(), testData.retrievePrefs(), testData.retrieveIndiffs(), testData.retrieveWeights(), testData.retrieveEvaluations());
        assertTrue("Preference does not match.", testData.getPreference().approxEquals(res, 0.00005f));
    }

    @Test
    public void testPreferenceEqualsOneMinusInverseConcordance() throws Exception {
        final SixRealCarsData testData = new SixRealCarsData();
        final OneMinusInverseMatrix<Alternative> resInverted = new OneMinusInverseMatrix<Alternative>(testData.getConcordance());
        testData.getPreference().approxEquals(resInverted, 0.00005f);
    }

    @Test
    public void testPreferenceEquality() throws Exception {
        final Concordance conc = new Concordance();
        final EvaluationMatrix eval = new EvaluationMatrix();
        final Alternative alt1 = new Alternative("01", "01");
        final Alternative alt2 = new Alternative("02", "02");
        final Criterion g = new Criterion("g1", "g1", PreferenceDirection.MAXIMIZE);
        eval.put(alt1, g, 3);
        eval.put(alt2, g, 3);
        final double res = conc.preferencePairwize(alt1, alt2, g, null, null, eval);
        assertEquals("Promethee-style preference for equal perf must be zero.", 0d, res, 0.00005f);
    }
}
