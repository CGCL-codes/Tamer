package org.jdiameter.server.api;

import org.jdiameter.api.ApplicationId;

/**
 *  This interface describe extends methods of base class
 * 
 * @author erick.svenson@yahoo.com
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface IMetaData extends org.jdiameter.client.api.IMetaData {

    /**
     * Add new Application Id to support application list
     * @param applicationId applicationId
     */
    public void addApplicationId(ApplicationId applicationId);

    /**
     * Remove Application id from support application list
     * @param applicationId applicationId
     */
    public void remApplicationId(ApplicationId applicationId);

    /**
     * @deprecated 
     * Reload parameters
     */
    public void reload();
}
