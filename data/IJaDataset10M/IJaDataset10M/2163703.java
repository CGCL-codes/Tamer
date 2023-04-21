package edu.kit.aifb.concept.scorer;

import edu.kit.aifb.concept.IConceptVectorData;

public class KLDivergenceTranslationProbScorer extends WikipediaMinerScorer {

    static final double INLINK_FACTOR_WEIGHT = .7;

    double m_sum = 0;

    double m_queryNorm1;

    double m_docNorm1;

    public void reset(IConceptVectorData queryData, IConceptVectorData docData, int numberOfDocuments) {
        m_sum = 0;
        m_queryNorm1 = queryData.getNorm1();
        m_docNorm1 = docData.getNorm1();
    }

    public void addConcept(int queryConceptId, double queryConceptScore, int docConceptId, double docConceptScore, int conceptFrequency) {
        double inlinkFactorQuery = 1 - 1 / Math.sqrt(1 + .3 * m_queryLanguageInlinks[queryConceptId]);
        double inlinkFactorDoc = 1 - 1 / Math.sqrt(1 + .3 * m_queryLanguageInlinks[docConceptId]);
        m_sum -= (1 - INLINK_FACTOR_WEIGHT + INLINK_FACTOR_WEIGHT * inlinkFactorQuery) * queryConceptScore / m_queryNorm1 * Math.log((1 - INLINK_FACTOR_WEIGHT + INLINK_FACTOR_WEIGHT * inlinkFactorDoc) * docConceptScore / m_docNorm1);
    }

    public void finalizeScore(IConceptVectorData queryData, IConceptVectorData docData) {
    }

    public double getScore() {
        return m_sum;
    }

    public boolean hasScore() {
        return m_sum > 0;
    }
}
