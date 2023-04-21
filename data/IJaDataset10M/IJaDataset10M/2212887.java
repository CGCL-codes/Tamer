package net.wesodi.ProductInformation;

public class ProductInformationProxy implements net.wesodi.ProductInformation.ProductInformation_PortType {

    private String _endpoint = null;

    private net.wesodi.ProductInformation.ProductInformation_PortType productInformation_PortType = null;

    public ProductInformationProxy() {
        _initProductInformationProxy();
    }

    public ProductInformationProxy(String endpoint) {
        _endpoint = endpoint;
        _initProductInformationProxy();
    }

    private void _initProductInformationProxy() {
        try {
            productInformation_PortType = (new net.wesodi.ProductInformation.ProductInformation_ServiceLocator()).getProductInformationSOAP();
            if (productInformation_PortType != null) {
                if (_endpoint != null) ((javax.xml.rpc.Stub) productInformation_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint); else _endpoint = (String) ((javax.xml.rpc.Stub) productInformation_PortType)._getProperty("javax.xml.rpc.service.endpoint.address");
            }
        } catch (javax.xml.rpc.ServiceException serviceException) {
        }
    }

    public String getEndpoint() {
        return _endpoint;
    }

    public void setEndpoint(String endpoint) {
        _endpoint = endpoint;
        if (productInformation_PortType != null) ((javax.xml.rpc.Stub) productInformation_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    }

    public net.wesodi.ProductInformation.ProductInformation_PortType getProductInformation_PortType() {
        if (productInformation_PortType == null) _initProductInformationProxy();
        return productInformation_PortType;
    }

    public net.wesodi.ProductInformation.DPProductInfo[] getProductInfo(net.wesodi.ProductInformation.InfoRequest productInformationRequestPart) throws java.rmi.RemoteException {
        if (productInformation_PortType == null) _initProductInformationProxy();
        return productInformation_PortType.getProductInfo(productInformationRequestPart);
    }
}
