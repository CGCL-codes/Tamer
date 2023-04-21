package wesodi.plugins.info;

public class ProductInformationSOAPSkeleton implements wesodi.plugins.info.ProductInformation_PortType, org.apache.axis.wsdl.Skeleton {

    private wesodi.plugins.info.ProductInformation_PortType impl;

    private static java.util.Map _myOperations = new java.util.Hashtable();

    private static java.util.Collection _myOperationsList = new java.util.ArrayList();

    /**
    * Returns List of OperationDesc objects with this name
    */
    public static java.util.List getOperationDescByName(java.lang.String methodName) {
        return (java.util.List) _myOperations.get(methodName);
    }

    /**
    * Returns Collection of OperationDescs
    */
    public static java.util.Collection getOperationDescs() {
        return _myOperationsList;
    }

    static {
        org.apache.axis.description.OperationDesc _oper;
        org.apache.axis.description.FaultDesc _fault;
        org.apache.axis.description.ParameterDesc[] _params;
        _params = new org.apache.axis.description.ParameterDesc[] { new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "ProductInformationRequestPart"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://wesodi.net/ProductInformation/", "InfoRequest"), wesodi.entities.transi.InfoRequest.class, false, false) };
        _oper = new org.apache.axis.description.OperationDesc("getProductInfo", _params, new javax.xml.namespace.QName("", "ProductInformationResponsePart"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://wesodi.net/ProductInformation/", "DPProductInfoArr"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://wesodi.net/ProductInformation/", "getProductInfo"));
        _oper.setSoapAction("http://www.example.org/ProductInformation/getProductInformation");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getProductInfo") == null) {
            _myOperations.put("getProductInfo", new java.util.ArrayList());
        }
        ((java.util.List) _myOperations.get("getProductInfo")).add(_oper);
    }

    public ProductInformationSOAPSkeleton() {
        this.impl = new wesodi.plugins.info.ProductInformationSOAPImpl();
    }

    public ProductInformationSOAPSkeleton(wesodi.plugins.info.ProductInformation_PortType impl) {
        this.impl = impl;
    }

    public wesodi.entities.transi.DPProductInfo[] getProductInfo(wesodi.entities.transi.InfoRequest productInformationRequestPart) throws java.rmi.RemoteException {
        wesodi.entities.transi.DPProductInfo[] ret = impl.getProductInfo(productInformationRequestPart);
        return ret;
    }
}
