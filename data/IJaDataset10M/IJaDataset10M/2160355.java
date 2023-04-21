package fedora.server.config.webxml;

import java.util.ArrayList;
import java.util.List;

public class UserDataConstraint {

    private List<String> descriptions;

    private String transportGuarantee;

    public UserDataConstraint() {
        descriptions = new ArrayList<String>();
    }

    public UserDataConstraint(String transportGuarantee) {
        this();
        setTransportGuarantee(transportGuarantee);
    }

    public List<String> getDescriptions() {
        return descriptions;
    }

    public void addDescription(String description) {
        descriptions.add(description);
    }

    public void removeDescription(String description) {
        descriptions.remove(description);
    }

    public String getTransportGuarantee() {
        return transportGuarantee;
    }

    public void setTransportGuarantee(String transportGuarantee) {
        this.transportGuarantee = transportGuarantee;
    }
}
