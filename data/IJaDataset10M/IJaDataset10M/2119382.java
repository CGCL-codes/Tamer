package org.gbif.portal.model.resources;

import org.gbif.portal.model.BaseObject;

/**
 * NetworkMembership
 * 
 * Represents a resource network which may include data resources from 
 * various data providers
 * 
 * Object representation of the NetworkMembership data model concept.
 * 
 * @author Donald Hobern
 */
public class NetworkMembership extends BaseObject {

    /** The data resource */
    protected DataResource dataResource;

    /** The resource network */
    protected ResourceNetwork resourceNetwork;

    /** The data resource id */
    protected Long dataResourceId;

    /** The resource network id */
    protected Long resourceNetworkId;

    /**
	 * Default Constructor.
	 */
    public NetworkMembership() {
    }

    /**
	 * Initialises the ids.
	 */
    public NetworkMembership(long id, DataResource dataResource, ResourceNetwork resourceNetwork) {
        this.id = id;
        this.dataResource = dataResource;
        this.resourceNetwork = resourceNetwork;
    }

    /**
	 * @return the dataResource
	 */
    public DataResource getDataResource() {
        return dataResource;
    }

    /**
	 * @param dataResource the dataResource to set
	 */
    public void setDataResource(DataResource dataResource) {
        this.dataResource = dataResource;
    }

    /**
	 * @return the resourceNetwork
	 */
    public ResourceNetwork getResourceNetwork() {
        return resourceNetwork;
    }

    /**
	 * @param resourceNetwork the resourceNetwork to set
	 */
    public void setResourceNetwork(ResourceNetwork resourceNetwork) {
        this.resourceNetwork = resourceNetwork;
    }

    /**
	 * @return the dataResourceId
	 */
    public Long getDataResourceId() {
        return dataResourceId;
    }

    /**
	 * @param dataResourceId the dataResourceId to set
	 */
    public void setDataResourceId(Long dataResourceId) {
        this.dataResourceId = dataResourceId;
    }

    /**
	 * @return the resourceNetworkId
	 */
    public Long getResourceNetworkId() {
        return resourceNetworkId;
    }

    /**
	 * @param resourceNetworkId the resourceNetworkId to set
	 */
    public void setResourceNetworkId(Long resourceNetworkId) {
        this.resourceNetworkId = resourceNetworkId;
    }
}
