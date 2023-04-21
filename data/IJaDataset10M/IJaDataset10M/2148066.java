package sf2.service.broker;

import java.io.Serializable;
import sf2.service.ServiceState;

public class ServiceInstallLocal implements Serializable {

    protected ServiceState state;

    public ServiceInstallLocal(ServiceState state) {
        this.state = state;
    }

    public ServiceState getState() {
        return state;
    }
}
