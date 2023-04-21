package uk.org.ogsadai.resource.property;

import uk.org.ogsadai.resource.ResourceID;

/**
 * Interface for meta-data relating to a data resource.
 *
 * @author The OGSA-DAI Project Team
 */
public interface DataResourceMetaData {

    /**
     * Gets the resource ID of the data resource.
     * 
     * @return resource ID.
     */
    ResourceID getResourceID();

    /**
     * Gets the human reabale description of the data resource.
     * 
     * @return resource description or the empty string if no description is
     *         available.
     */
    String getDescription();

    /**
     * Sets the resource ID of the data resource.
     * 
     * @param resourceID resource ID.
     */
    void setResourceID(ResourceID resourceID);

    /**
     * Sets the optional human readable description of the data resource.
     * 
     * @param description resource description.
     */
    void setDescription(String description);
}
