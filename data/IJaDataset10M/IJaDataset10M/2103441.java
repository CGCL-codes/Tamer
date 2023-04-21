package jade.core.resource;

import jade.core.Service;

public interface ResourceManagementSlice extends Service.Slice {

    static final String H_GETRESOURCE = "G";

    byte[] getResource(String name, int fetchMode) throws Exception;
}
