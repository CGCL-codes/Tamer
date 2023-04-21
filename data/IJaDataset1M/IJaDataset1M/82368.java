package net.sf.pando.sds;

import java.util.HashMap;
import java.util.List;

public class ServiceDirectoryCore {

    private HashMap<String, List<ServiceForm>> serviceDirectoryMap;

    public ServiceDirectoryCore() {
        serviceDirectoryMap = new HashMap<String, List<ServiceForm>>();
    }

    public void addService(String agent_id, List<ServiceForm> serviceForms) throws Exception {
        if (agent_id == null) throw new Exception("agent_id can not be null");
        if (serviceForms == null) throw new Exception("serviceForms can not be null");
        serviceDirectoryMap.put(agent_id, serviceForms);
    }

    public HashMap<String, List<ServiceForm>> getServiceDirectoryMap() {
        return serviceDirectoryMap;
    }
}
