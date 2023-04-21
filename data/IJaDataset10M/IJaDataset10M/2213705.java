package aau;

import java.util.List;

/**
 * @author Fred Durao
 *
 */
public interface ClusterService {

    /**
	 * Calculate the spectral cluster;
	 */
    public void calculateSpectralCluster();

    /**
	 * 
	 */
    public void calculateSpectralClusterSampleForMECO();

    /**
	 * Calculate the spectral cluster;
	 */
    public void calculateSpectralClusterFromSearch(List<SearchResult> searchResults);

    /**
	 * Returns the map of tag clusterings where the keys are tags and the values are the content items
	 */
    public List<TagCluster> listTagCluster();
}
