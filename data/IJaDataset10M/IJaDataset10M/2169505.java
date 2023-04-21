package uk.org.ogsadai.resource.dataresource;

import uk.org.ogsadai.resource.OnDemandResourcePropertyCallback;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.resource.ResourceProperty;
import uk.org.ogsadai.resource.ResourcePropertyNames;
import uk.org.ogsadai.resource.ResourceStateVisitor;
import uk.org.ogsadai.resource.ResourceType;
import uk.org.ogsadai.resource.SimpleOnDemandResourceProperty;
import uk.org.ogsadai.resource.SimpleResourceState;
import uk.org.ogsadai.resource.event.ResourceEvent;
import uk.org.ogsadai.resource.property.ResourceLifetimeCallback;

/**
 * Data resource state.
 *
 * @author The OGSA-DAI Team.
 */
public class SimpleDataResourceState extends SimpleResourceState implements DataResourceState {

    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007-2010.";

    /** Data resource class */
    private String mDataResourceClass;

    /**
     * Constructor.
     */
    public SimpleDataResourceState() {
        super();
        mResourceType = ResourceType.DATA_RESOURCE;
        setSticky(false);
        setTransient(false);
    }

    /**
     * Constructor.
     *
     * @param rid
     *     Resource ID.
     * @throws IllegalArgumentException
     *    If <tt>rid</tt> is <tt>null</tt>.
     */
    public SimpleDataResourceState(ResourceID rid) {
        this();
        setResourceID(rid);
    }

    /**
     * <tt>UpdateEventListener</tt>s are notified of an update
     * of type <tt>ResourceEvent.UPDATE_DATA_RESOURCE_CLASS</tt>.
     */
    public void setDataResourceClass(String className) {
        if (className == null) {
            throw new IllegalArgumentException("className must not be null");
        }
        mDataResourceClass = className;
        notifyStateUpdated(ResourceEvent.UPDATE_DATA_RESOURCE_CLASS);
    }

    public String getDataResourceClass() {
        return mDataResourceClass;
    }

    /**
     * Initialises the termination time and current time properties.
     */
    protected void initialiseProperties() {
        super.initialiseProperties();
        OnDemandResourcePropertyCallback callback = new ResourceLifetimeCallback(this);
        ResourceProperty property = new SimpleOnDemandResourceProperty(ResourcePropertyNames.TERMINATION_TIME, callback);
        getResourcePropertySet().addProperty(property);
        property = new SimpleOnDemandResourceProperty(ResourcePropertyNames.CURRENT_TIME, callback);
        getResourcePropertySet().addProperty(property);
    }

    public void accept(ResourceStateVisitor visitor) {
        visitor.visitDataResource(this);
    }
}
