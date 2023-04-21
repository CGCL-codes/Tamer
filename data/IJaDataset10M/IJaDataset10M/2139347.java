package org.dbe.rankingservice;

public class RankingServiceSoapBindingSkeleton implements org.dbe.rankingservice.RankingServicePT, org.apache.axis.wsdl.Skeleton {

    private org.dbe.rankingservice.RankingServicePT impl;

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
        _params = new org.apache.axis.description.ParameterDesc[] { new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "configName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "configLocation"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "failedService"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false) };
        _oper = new org.apache.axis.description.OperationDesc("makeRanking", _params, new javax.xml.namespace.QName("", "chosenService"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:rankingservice.dbe.org", "makeRanking"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("makeRanking") == null) {
            _myOperations.put("makeRanking", new java.util.ArrayList());
        }
        ((java.util.List) _myOperations.get("makeRanking")).add(_oper);
    }

    public RankingServiceSoapBindingSkeleton() {
        this.impl = new org.dbe.rankingservice.RankingServiceSoapBindingImpl();
    }

    public RankingServiceSoapBindingSkeleton(org.dbe.rankingservice.RankingServicePT impl) {
        this.impl = impl;
    }

    public java.lang.String makeRanking(java.lang.String configName, java.lang.String configLocation, java.lang.String failedService) throws java.rmi.RemoteException {
        java.lang.String ret = impl.makeRanking(configName, configLocation, failedService);
        return ret;
    }
}
