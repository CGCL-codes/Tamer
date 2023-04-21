package sf2.service.broker;

import java.io.Serializable;
import sf2.service.ServiceDescription;

public class ServiceDescResponse implements Serializable {

    protected boolean success;

    protected ServiceDescription desc;

    public ServiceDescResponse(boolean success) {
        this.success = success;
    }

    public ServiceDescResponse(boolean success, ServiceDescription desc) {
        this.success = success;
        this.desc = desc;
    }

    public boolean isSuccess() {
        return success;
    }

    public ServiceDescription describe() {
        return desc;
    }
}
