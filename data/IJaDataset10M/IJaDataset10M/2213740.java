package org.xplanner.soap.version;

public class VersionServiceTestCase extends junit.framework.TestCase {

    public VersionServiceTestCase(java.lang.String name) {
        super(name);
    }

    public void testVersionWSDL() throws Exception {
        javax.xml.rpc.ServiceFactory serviceFactory = javax.xml.rpc.ServiceFactory.newInstance();
        java.net.URL url = new java.net.URL(new org.xplanner.soap.version.VersionServiceLocator().getVersionAddress() + "?WSDL");
        javax.xml.rpc.Service service = serviceFactory.createService(url, new org.xplanner.soap.version.VersionServiceLocator().getServiceName());
        assertTrue(service != null);
    }

    public void test1VersionGetVersion() throws Exception {
        org.xplanner.soap.version.VersionSoapBindingStub binding;
        try {
            binding = (org.xplanner.soap.version.VersionSoapBindingStub) new org.xplanner.soap.version.VersionServiceLocator().getVersion();
        } catch (javax.xml.rpc.ServiceException jre) {
            if (jre.getLinkedCause() != null) jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        assertNotNull("binding is null", binding);
        binding.setTimeout(60000);
        java.lang.String value = null;
        value = binding.getVersion();
    }
}
