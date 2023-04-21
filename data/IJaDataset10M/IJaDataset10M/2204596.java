package org.gbif.portal.dao;

import org.gbif.portal.model.DataResource;

/**
 * The Data Resource DAO
 * @author trobertson
 */
public interface DataResourceDAO {

    /**
	 * Creates the resource
	 * @param dataResource To create
	 * @return The id of the created resource
	 */
    public long create(DataResource dataResource);

    /**
	 * Gets the dataresource
	 * @param id That identifies the data resource
	 * @return The dataresource identified by the id
	 */
    public DataResource getById(long id);

    /**
	 * Gets the resource for the name and data provider
	 * @param name Resource name to retreieve
	 * @param dataProviderId That the resource must be attached to
	 * @return The DataResource or null
	 */
    public DataResource getByNameForProvider(String name, long dataProviderId);

    /**
	 * Gets the resource for the name, url and data provider
	 * The URL is used since there may be a provider collating 
	 * many Resources and name clashes are likely 
	 * @param name Resource name to retreieve
	 * @param url That the resource should be at
	 * @param dataProviderId That the resource must be attached to
	 * @return The DataResource or null
	 */
    public DataResource getByNameAndUrlForProvider(String name, String url, long dataProviderId);

    /**
	 * Gets the resource for the given remote id at url, the url and the data provider
	 * @param remoteId For the RAP
	 * @param url The url 
	 * @param dataProviderId The provider owning the resource
	 * @return The DataResource or null
	 */
    public DataResource getByRemoteIdAtUrlAndUrlForProvider(String remoteId, String url, long dataProviderId);

    /**
	 * Updates the data resource
	 * @param dataResource To update.  Should the ID be null, then create is called 
	 * @return The id of the created record or updated
	 */
    public long updateOrCreate(DataResource dataResource);
}
