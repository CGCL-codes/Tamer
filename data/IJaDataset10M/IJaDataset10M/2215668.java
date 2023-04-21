package hu.uszeged.inf.wlab.netspotter.common.port;

import hu.uszeged.inf.wlab.netspotter.common.link.Link;
import java.util.List;

/**
 * Represents a SINGLE PORT of a device, which is physical!
 */
public class Port {

    int portNumber;

    List<Link> links;

    PortRole basicPortRole;

    public Port() {
        this.basicPortRole = new PortRole();
    }

    /**
	 * @return the portNumber
	 */
    public int getPortNumber() {
        return portNumber;
    }

    /**
	 * @param portNumber the portNumber to set
	 */
    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    public PortRole getBasicPortRole() {
        return basicPortRole;
    }

    public void setBasicPortRole(PortRole basicPortRole) {
        this.basicPortRole = basicPortRole;
    }
}
