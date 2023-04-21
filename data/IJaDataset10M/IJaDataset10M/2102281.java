package uk.org.ogsadai.activity.extension;

import uk.org.ogsadai.activity.Activity;
import uk.org.ogsadai.resource.ResourceAccessor;

/**
 * Activity extestion interface for activities that access and interact with a 
 * target resource.
 * 
 * @author The OGSA-DAI Project Team
 */
public interface ResourceActivity extends Activity {

    /**
     * Sets the target resource accessor for the activity.
     * 
     * @param resourceAccessor
     *            target resource accessor
     */
    public void setTargetResourceAccessor(ResourceAccessor resourceAccessor);

    /**
     * Gets the class of target resource for this activity. The activity 
     * framework will ensure that any resource accessor passed to the 
     * <code>setTargetResource</code> method is assignment compatible with the 
     * returned class.
     * 
     * @return resource class
     */
    public Class getTargetResourceAccessorClass();
}
