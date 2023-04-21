package br.inf.ufrgs.simevaluator.tests.analyzer;

import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import br.ufrgs.inf.simevaluator.analyser.Query;
import br.ufrgs.inf.simevaluator.analyser.Similarity;
import br.ufrgs.inf.simevaluator.analyser.SimilarityItem;
import br.ufrgs.inf.simevaluator.datasource.SampleLoader;
import br.ufrgs.inf.simevaluator.similarity.AbstractSimilarityFunction;
import br.ufrgs.inf.simevaluator.similarity.FunctionsContainer;
import br.ufrgs.inf.simevaluator.similarity.simmetrics.SimmetricsJaccard;

public class AnalyzerTest extends TestCase {

    private ArrayList<String> sample;

    protected void setUp() throws Exception {
        sample = SampleLoader.getSample("sample.txt");
    }

    public void testFunction() {
        String target = "Fabio";
        AbstractSimilarityFunction sim = new SimmetricsJaccard();
        Similarity query = new Similarity();
        query.processSample(sample, target, sim);
        for (SimilarityItem similarity : query.getSimilarityItems().subList(0, 9)) {
            System.out.println(similarity.getTarget().trim() + "\t" + similarity.getValue());
        }
        System.out.println(query.getPerformance() + "\t" + query.getSimilarityItems().get(0).getValue());
        assertTrue(query.getPerformance() < 2000);
    }

    public void testProcessFunctions() {
        List<AbstractSimilarityFunction> functions = FunctionsContainer.getFunctions();
        for (AbstractSimilarityFunction function : functions) {
            System.out.println("Function: " + function.getAPI() + " " + function.getName());
            Similarity sim = new Similarity();
            sim.processSample(sample, "FP", function);
            List<SimilarityItem> items = sim.getSimilarityItems();
            for (SimilarityItem item : items) {
                System.out.println(item.getTarget() + "\t" + item.getValue());
            }
            System.out.println("Performance: " + sim.getPerformance() + "\n");
        }
    }

    public void testQueries() {
        List<Query> queries = Query.loadQueries("DBSample.txt", "DBQueries.txt");
        assertEquals(5, queries.size());
    }
}
