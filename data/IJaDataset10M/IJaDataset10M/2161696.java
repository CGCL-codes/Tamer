package org.gbif.portal.dao.resources;

import java.util.Date;
import java.util.List;
import org.gbif.portal.model.occurrence.BasisOfRecord;
import org.gbif.portal.model.resources.DataProvider;
import org.gbif.portal.model.resources.DataResource;

/**
 * The DAO for the DataResource model objects.
 *
 * @author Dave Martin
 */
public interface DataResourceDAO {

    /**
	 * Returns the DataResource with the specified id value.
	 * 
	 * @param dataResourceId The id of the dataResource
	 * @return The DataResource object for this id.
	 */
    public DataResource getDataResourceFor(long dataResourceId);

    /**
	 * Retrieve the data resources for a data provider.
	 * 
	 * @param dataProviderId The id of the dataProvider
	 * @return The Data Resources for the provider. 
	 */
    public List<DataResource> getDataResourcesForProvider(long dataProviderId);

    /**
	 * Returns the last Data Resource added to the system.
	 * 
	 * @return the last Data Resource added to the system.
	 */
    public DataResource getLastDataResourceAdded();

    /**
	 * Returns a count of all the data resources within the system.
	 * 
	 * @return the total number of data resources
	 */
    public int getTotalDataResourceCount();

    /**
	 * Returns a list of Data Resources with their own unshared
	 * taxonomies.
	 * 
	 * @return a list of data resources
	 */
    public List<DataResource> getDataResourcesWithNonSharedTaxonomies();

    /**
	 * Retrieve a list of data resources and matching the supplied name 
	 *
	 * @param nameStub the name to search for
	 * @param fuzzy whether to do a wildcard search
	 * @param basisofrecord for resource
	 * @param date on or after which resource has been modified
	 * @param startIndex the result to start at
	 * @param maxResults max number of results to return
	 * @return a list of DataResource model objects
	 */
    public List<DataResource> findDataResources(String nameStub, boolean fuzzy, DataProvider dataProvider, BasisOfRecord basisOfRecord, Date modifiedSince, int startIndex, int maxResults);

    /**
	 * Count data resources matching the supplied name 
	 *
	 * @param nameStub the name to search for
	 * @param fuzzy whether to do a wildcard search
	 * @param basisofrecord for resource
	 * @param date on or after which resource has been modified
	 * @return a list of DataResource model objects
	 */
    public Long countDataResources(String nameStub, boolean fuzzy, DataProvider dataProvider, BasisOfRecord basisOfRecord, Date modifiedSince);

    /**
	 * Retrieve a list of data resources and data providers matching 
     * the supplied name 
	 *
	 * @param nameStub the name to search for
	 * @param fuzzy whether to do a wildcard search
	 * @param anyOccurrence effectively adds a wildcard at front of method
	 * @param startIndex the result to start at
	 * @param maxResults max number of results to return
	 * @return a list of DataResource and DataProvider model objects
	 */
    public List findDataResourcesAndProvidersAndNetworks(String nameStub, boolean fuzzy, boolean anyOccurrence, boolean includeCountrySearch, int startIndex, int maxResults);

    /**
	 * Retrieve a list of data resources belonging to the identified network
	 *
	 * @param resourceNetworkId id of network for which to find resources
	 * @return a list of DataResource model objects
	 */
    public List<DataResource> getDataResourcesForResourceNetwork(long resourceNetworkId);

    /**
	 * Retrieve counts for resources
	 * 
	 * @param entityId
	 * @return scalar arrays of data resource id, data resource name, data provider name and count 
	 */
    public List<Object[]> getDataResourceCountsForCountry(String isoCountryCode, boolean geoRefOnly);

    /**
	 * Retrieve counts for resources
	 * 
	 * @param entityId
	 * @return scalar arrays of data resource id, data resource name, data provider name and count 
	 */
    public List<Object[]> getCountryCountsForDataResource(Long dataResourceId, boolean geoRefOnly);

    /**
	 * Retrieves a list of distinct first characters from the names of the data resources/providers
	 * @return list of characters
	 */
    public List<Character> getDatasetAlphabet();

    /**
	 * Retrieves a list of key resource name pairs.
	 * 
	 * @param dataProviderId
	 * @return
	 */
    public List getDataResourceList(Long dataProviderId);

    /**
	 * Retrieves all data resources. Potentially very expensive.
	 * @return
	 */
    public List<DataResource> getAllDataResources();

    /**
	 * Retrieves a list of resources with occurrences for this concept.
	 * 
	 * @param parseKey
	 * @param rank
	 * @param georeferenced
	 * @return 
	 */
    public List<Object[]> getDataResourceWithOccurrencesFor(Long parseKey, String rank, boolean georeferenced);
}
