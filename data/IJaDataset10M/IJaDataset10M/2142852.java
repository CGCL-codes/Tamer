package eu.redseeds.owl.tests;

import eu.redseeds.scl.SCLGraph;

public class OWLSimilarityExperimentResult {

    private String queryName;

    private String caseName;

    private SCLGraph queryGraph;

    private SCLGraph caseGraph;

    private double unclassifiedSimilarity;

    private double classifiedSimilarity;

    public OWLSimilarityExperimentResult(String q, String c, SCLGraph qG, SCLGraph cG) {
        queryName = q;
        caseName = c;
        queryGraph = qG;
        caseGraph = cG;
    }

    public void setUnclassifiedSimilarity(double s) {
        unclassifiedSimilarity = s;
    }

    public void setClassifiedSimilarity(double s) {
        classifiedSimilarity = s;
    }

    public String getQueryName() {
        return queryName;
    }

    public String getCaseName() {
        return caseName;
    }

    public SCLGraph getQueryGraph() {
        return queryGraph;
    }

    public SCLGraph getCaseGraph() {
        return caseGraph;
    }

    public double getUnclassifiedSimilarity() {
        return unclassifiedSimilarity;
    }

    public double getClassifiedSimilarity() {
        return classifiedSimilarity;
    }
}
