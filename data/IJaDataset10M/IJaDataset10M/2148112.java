package lpr.minikazaa.bootstrap;

/**
 *
 * @author Andrea Di Grazia, Massimiliano Giovine
 * @date 8-gen-2009
 * @file BootstrapRMIWrapper.java
 */
public class BootstrapRMIWrapper {

    private BootStrapServerInterface rmi_stub;

    public BootstrapRMIWrapper() {
        this.rmi_stub = null;
    }

    public void setStub(BootStrapServerInterface stub) {
        this.rmi_stub = stub;
    }

    public BootStrapServerInterface getStub() {
        return this.rmi_stub;
    }
}
