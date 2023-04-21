package org.monet.services.docservice.timeoutadapter;

public class Docservice_ServiceLocator extends org.monet.services.docservice.Docservice_ServiceLocator {

    private static final long serialVersionUID = 581570085958846298L;

    public Docservice_ServiceLocator() {
    }

    public Docservice_ServiceLocator(String endPoint) {
        docservicePort_address = endPoint;
    }

    public void setEndpointAddress(String endPoint) {
        docservicePort_address = endPoint;
    }

    public org.monet.services.docservice.Docservice_PortType getdocservicePort(Integer timeout) throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(docservicePort_address);
            org.monet.services.docservice.DocservicePortBindingStub _stub = new org.monet.services.docservice.DocservicePortBindingStub(endpoint, this);
            _stub.setPortName(getdocservicePortWSDDServiceName());
            if (timeout != null) _stub.setTimeout(timeout);
            return _stub;
        } catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        } catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }
}
