/**
 * InstinetOguchiBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.sf.jspread.marketdata.inetats.webservice;

public class InstinetOguchiBindingStub extends org.apache.axis.client.Stub implements org.sf.jspread.marketdata.inetats.webservice.InstinetOguchi {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[1];
        org.apache.axis.description.OperationDesc oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getStockOguchi");
        oper.addParameter(new javax.xml.namespace.QName("", "token"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "symbol"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "count"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("urn:Instinet", "islandData"));
        oper.setReturnClass(org.sf.jspread.marketdata.inetats.webservice.IslandData.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.enum.Style.RPC);
        oper.setUse(org.apache.axis.enum.Use.ENCODED);
        _operations[0] = oper;

    }

    public InstinetOguchiBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public InstinetOguchiBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public InstinetOguchiBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("urn:Instinet", "ordersType");
            cachedSerQNames.add(qName);
            cls = org.sf.jspread.marketdata.inetats.webservice.OrdersType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("urn:Instinet", "stockType");
            cachedSerQNames.add(qName);
            cls = org.sf.jspread.marketdata.inetats.webservice.StockType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:Instinet", "islandTimeType");
            cachedSerQNames.add(qName);
            cls = org.sf.jspread.marketdata.inetats.webservice.IslandTimeType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("urn:Instinet", "symbolType");
            cachedSerQNames.add(qName);
            cls = org.sf.jspread.marketdata.inetats.webservice.SymbolType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("urn:Instinet", "orderListType");
            cachedSerQNames.add(qName);
            cls = org.sf.jspread.marketdata.inetats.webservice.OrderListType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:Instinet", "recNumbType");
            cachedSerQNames.add(qName);
            cls = org.sf.jspread.marketdata.inetats.webservice.RecNumbType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("urn:Instinet", "marketSessionType");
            cachedSerQNames.add(qName);
            cls = org.sf.jspread.marketdata.inetats.webservice.MarketSessionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("urn:Instinet", "matchListType");
            cachedSerQNames.add(qName);
            cls = org.sf.jspread.marketdata.inetats.webservice.MatchListType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:Instinet", "netChangeType");
            cachedSerQNames.add(qName);
            cls = org.sf.jspread.marketdata.inetats.webservice.NetChangeType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("urn:Instinet", "shortNameType");
            cachedSerQNames.add(qName);
            cls = org.sf.jspread.marketdata.inetats.webservice.ShortNameType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("urn:Instinet", "stockListType");
            cachedSerQNames.add(qName);
            cls = org.sf.jspread.marketdata.inetats.webservice.StockListType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:Instinet", "referenceType");
            cachedSerQNames.add(qName);
            cls = org.sf.jspread.marketdata.inetats.webservice.ReferenceType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("urn:Instinet", "sideType");
            cachedSerQNames.add(qName);
            cls = org.sf.jspread.marketdata.inetats.webservice.SideType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("urn:Instinet", "marketType");
            cachedSerQNames.add(qName);
            cls = org.sf.jspread.marketdata.inetats.webservice.MarketType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("urn:Instinet", "islandData");
            cachedSerQNames.add(qName);
            cls = org.sf.jspread.marketdata.inetats.webservice.IslandData.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:Instinet", "dollarVolumeType");
            cachedSerQNames.add(qName);
            cls = org.sf.jspread.marketdata.inetats.webservice.DollarVolumeType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("urn:Instinet", "rankType");
            cachedSerQNames.add(qName);
            cls = org.sf.jspread.marketdata.inetats.webservice.RankType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("urn:Instinet", "orderType");
            cachedSerQNames.add(qName);
            cls = org.sf.jspread.marketdata.inetats.webservice.OrderType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:Instinet", "priceType");
            cachedSerQNames.add(qName);
            cls = org.sf.jspread.marketdata.inetats.webservice.PriceType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("urn:Instinet", "matchType");
            cachedSerQNames.add(qName);
            cls = org.sf.jspread.marketdata.inetats.webservice.MatchType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:Instinet", "userDataType");
            cachedSerQNames.add(qName);
            cls = org.sf.jspread.marketdata.inetats.webservice.UserDataType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:Instinet", "bookedType");
            cachedSerQNames.add(qName);
            cls = org.sf.jspread.marketdata.inetats.webservice.BookedType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:Instinet", "openType");
            cachedSerQNames.add(qName);
            cls = org.sf.jspread.marketdata.inetats.webservice.OpenType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:Instinet", "sharesType");
            cachedSerQNames.add(qName);
            cls = org.sf.jspread.marketdata.inetats.webservice.SharesType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("urn:Instinet", "matchedType");
            cachedSerQNames.add(qName);
            cls = org.sf.jspread.marketdata.inetats.webservice.MatchedType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

    }

    private org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call =
                    (org.apache.axis.client.Call) super.service.createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
                    _call.setEncodingStyle(org.apache.axis.Constants.URI_SOAP11_ENC);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                        java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                        _call.registerTypeMapping(cls, qName, sf, df, false);
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", t);
        }
    }

    public org.sf.jspread.marketdata.inetats.webservice.IslandData getStockOguchi(java.lang.String token, java.lang.String symbol, int count) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:GoogleSearchAction");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:Instinet", "getStockOguchi"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {token, symbol, new java.lang.Integer(count)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.sf.jspread.marketdata.inetats.webservice.IslandData) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.sf.jspread.marketdata.inetats.webservice.IslandData) org.apache.axis.utils.JavaUtils.convert(_resp, org.sf.jspread.marketdata.inetats.webservice.IslandData.class);
            }
        }
    }

}
