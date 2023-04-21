package net.solarnetwork.loadtest;

/**
 * API for a class that populates some load test data.
 * 
 * @author matt
 * @version $Revision: 1181 $
 */
public interface LoadTestPopulator {

    /**
	 * Populate the test data.
	 */
    void populateData();
}
