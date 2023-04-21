package uk.org.ogsadai.resource.property;

import java.util.Calendar;
import org.w3c.dom.Node;
import uk.org.ogsadai.resource.OnDemandResourcePropertyCallback;
import uk.org.ogsadai.resource.ResourcePropertyName;
import uk.org.ogsadai.resource.ResourcePropertyNames;
import uk.org.ogsadai.resource.ResourcePropertyValue;
import uk.org.ogsadai.resource.ResourceState;

/**
 * A callback that provides values for the termination time and current time
 * properties of a resource.
 *
 * @author The OGSA-DAI Team.
 */
public class ResourceLifetimeCallback implements OnDemandResourcePropertyCallback {

    /** Copyright notice */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh 2007";

    /** Lifetime of the resource */
    protected final ResourceState mState;

    /** Termination time resource property */
    private final ResourcePropertyValue mTerminationTime;

    /** Current time resource property */
    private final ResourcePropertyValue mCurrentTime;

    /**
     * Constructs a new callback for lifetime properties termination time
     * and current time.
     * 
     * @param resourceState
     *            resource state
     */
    public ResourceLifetimeCallback(ResourceState resourceState) {
        mState = resourceState;
        mTerminationTime = new TerminationTimeValue();
        mCurrentTime = new CurrentTimeValue();
    }

    public ResourcePropertyValue getResourcePropertyValue(ResourcePropertyName name) {
        if (ResourcePropertyNames.TERMINATION_TIME.equals(name)) {
            return mTerminationTime;
        } else if (ResourcePropertyNames.CURRENT_TIME.equals(name)) {
            return mCurrentTime;
        } else {
            return null;
        }
    }

    public void setResourcePropertyValue(ResourcePropertyName name, Object value) {
        if (ResourcePropertyNames.TERMINATION_TIME.equals(name) && value instanceof Calendar) {
            Calendar terminationTime = (Calendar) value;
            mState.getResourceLifetime().setTerminationTime(terminationTime);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Wrapper for the termination time of a resource.
     *
     * @author The OGSA-DAI Team.
     */
    protected class TerminationTimeValue implements ResourcePropertyValue {

        /** Copyright notice */
        private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh 2007";

        public Node[] getAsDOM() {
            return NullableDatePropertyConvertor.serialize(mState.getResourceLifetime().getTerminationTime());
        }

        public Object getValue() {
            return mState.getResourceLifetime().getTerminationTime();
        }
    }

    /**
     * Wrapper for the current time property value.
     *
     * @author The OGSA-DAI Team.
     */
    protected class CurrentTimeValue implements ResourcePropertyValue {

        /** Copyright notice */
        private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh 2007";

        public Node[] getAsDOM() {
            return NullableDatePropertyConvertor.serialize(Calendar.getInstance());
        }

        public Object getValue() {
            return Calendar.getInstance();
        }
    }
}
