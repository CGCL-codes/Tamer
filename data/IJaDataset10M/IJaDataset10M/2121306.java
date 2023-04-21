package com.rif.client.service.definition;

import java.util.HashMap;
import java.util.Map;

/**
 * @author bruce.liu (mailto:jxta.liu@gmail.com)
 * 2011-7-28 下午09:48:51
 */
public class ClientModelManager {

    public static ClientModelManager INSTANCE = new ClientModelManager();

    private Map<String, ServiceClientModel> serviceClientModelMap = new HashMap<String, ServiceClientModel>();

    private Map<String, TransportClientModel> transportClientModelMap = new HashMap<String, TransportClientModel>();

    private ClientModelManager() {
    }

    public void regiesterService(ServiceClientModel serviceClientModel) {
        serviceClientModelMap.put(serviceClientModel.getRefId(), serviceClientModel);
    }

    public void regiesterTrasport(TransportClientModel transportClientModel) {
        transportClientModelMap.put(transportClientModel.getRefId(), transportClientModel);
    }

    public ServiceClientModel getServiceClientModel(String refId) {
        return serviceClientModelMap.get(refId);
    }

    public TransportClientModel getTransportClientModel(String refId) {
        return transportClientModelMap.get(refId);
    }
}
