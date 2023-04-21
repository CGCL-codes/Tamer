package org.apache.jetspeed.om.registry;

import java.io.Serializable;

/**
 * Interface describing a ContentURL in the registry
 *
 * @author <a href="mailto:taylor@apache.org">David Sean Taylor</a>
 * @version $Id: ContentURL.java,v 1.3 2004/02/23 03:11:39 jford Exp $
 */
public interface ContentURL extends Serializable {

    /** @return the string URL */
    public String getURL();

    /** Sets the string URL
     * 
     * @param value the new URL value
     */
    public void setURL(String value);

    public boolean isCacheKey();

    public void setCachedOnURL(boolean cache);
}
