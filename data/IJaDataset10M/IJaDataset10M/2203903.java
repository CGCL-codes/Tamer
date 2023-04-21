package Domain;

import Persistance.IFilteringAlgorithmStrategy;
import Persistance.FilteringAlgorithmContext;
import Persistance.CollaborationAlgo;
import Persistance.Exceptions.UnableToPredictRankException;

public class KMeansStrategy implements IFilteringAlgorithmStrategy {

    private KMeansAdapter m_KMeans;

    public KMeansStrategy(KMeansAdapter KMeans) {
        m_KMeans = KMeans;
    }

    public double getPredictionRating(String user, String item) throws UnableToPredictRankException {
        String cluster = m_KMeans.getCluster(user);
        if (m_KMeans.hasRank(cluster, item)) return m_KMeans.getRank(cluster, item);
        FilteringAlgorithmContext filter = new FilteringAlgorithmContext(new CollaborationAlgo(m_KMeans));
        return filter.getPredictionRating(cluster, item);
    }
}
