package uk.org.ogsadai.resource.dataresource;

import uk.org.ogsadai.resource.Resource;
import uk.org.ogsadai.resource.ResourceState;

/**
 * Represents a data resource.
 *
 * @author The OGSA-DAI Team.
 */
public interface DataResource extends Resource {

    /**
     * The <tt>ResourceState</tt> must be castable to 
     * <tt>uk.org.ogsadai.resource.dataresource.DataResourceState</tt>
     */
    public void initialize(ResourceState resourceState);

    /**
     * <tt>ResourceState</tt> will be castable to 
     * <tt>uk.org.ogsadai.resource.dataresource.DataResourceState</tt>.
     */
    public ResourceState getState();
}
