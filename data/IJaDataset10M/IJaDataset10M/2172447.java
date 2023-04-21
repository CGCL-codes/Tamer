package uk.org.ogsadai.activity.extension;

import uk.org.ogsadai.activity.RequestStatusBuilder;

/**
 * Initialisation interface for activities that can write to the request status.
 * It defines one method that passes the <code>RequestStatusBuilder</code>
 * interface to the activity.
 * 
 * @author The OGSA-DAI Project Team
 */
public interface RequestStatusBuildingActivity {

    /**
     * Sets the request status builder for the activity to use for writing
     * result data.
     * 
     * @param builder
     *            request status builder interface
     */
    public void setRequestStatusBuilder(RequestStatusBuilder builder);
}
