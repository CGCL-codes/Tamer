package org.personalsmartspace.pss_sm_discovery.api.platform;

import org.personalsmartspace.onm.api.pss3p.ICallbackListener;
import org.personalsmartspace.onm.api.pss3p.PSSAdvertisement;
import org.personalsmartspace.pss_sm_api.impl.ServiceMgmtException;
import org.personalsmartspace.sre.api.pss3p.IServiceIdentifier;

public interface IServiceModification {

    /**
     * Notify public foreign PSS service(s)
     * 
     * @param pssAd
     * @return boolean
     * @throws ServiceMgmtException
     */
    public boolean notifyPublicPSSServices(PSSAdvertisement pssAd) throws ServiceMgmtException;

    /**
     * Notify shared foreign PSS service(s)
     * 
     * @param pssAd
     * @return boolean
     * @throws ServiceMgmtException
     */
    public boolean notifySharedPSSServices(PSSAdvertisement pssAd) throws ServiceMgmtException;

    @Deprecated
    public boolean registerService(IServiceIdentifier serviceId, String ontologyURI, String serviceURI) throws ServiceMgmtException;

    @Deprecated
    public boolean unregisterService(IServiceIdentifier serviceId) throws ServiceMgmtException;

    /**
     * Allow the registry to be cleared
     * 
     * @return boolean
     * @throws ServiceMgmtException
     */
    public boolean clearRegistry() throws ServiceMgmtException;

    /**
     * Ask the Service Registry to update a registered service. To be used in the case remote services
     * that have been discovered but the service's OWL_S file has not been imported and the service 
     * fully registered.
     *  
     * @param serviceId
     * @param callback if service is not fully registered the callback will receive notification when it has been
     * @return boolean true if service is fully registered
     */
    public boolean updateServiceDetails(IServiceIdentifier serviceId, ICallbackListener callback);
}
