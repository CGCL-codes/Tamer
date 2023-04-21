package oosnippets.learn.webservice.axis;

public class SayHelloProxy implements oosnippets.learn.webservice.axis.SayHello {

    private boolean _useJNDI = true;

    private String _endpoint = null;

    private oosnippets.learn.webservice.axis.SayHello __sayHello = null;

    public SayHelloProxy() {
        _initSayHelloProxy();
    }

    private void _initSayHelloProxy() {
        if (_useJNDI) {
            try {
                javax.naming.InitialContext ctx = new javax.naming.InitialContext();
                __sayHello = ((oosnippets.learn.webservice.axis.SayHelloService) ctx.lookup("java:comp/env/service/SayHelloService")).getSayHello();
            } catch (javax.naming.NamingException namingException) {
            } catch (javax.xml.rpc.ServiceException serviceException) {
            }
        }
        if (__sayHello == null) {
            try {
                __sayHello = (new oosnippets.learn.webservice.axis.SayHelloServiceLocator()).getSayHello();
            } catch (javax.xml.rpc.ServiceException serviceException) {
            }
        }
        if (__sayHello != null) {
            if (_endpoint != null) ((javax.xml.rpc.Stub) __sayHello)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint); else _endpoint = (String) ((javax.xml.rpc.Stub) __sayHello)._getProperty("javax.xml.rpc.service.endpoint.address");
        }
    }

    public void useJNDI(boolean useJNDI) {
        _useJNDI = useJNDI;
        __sayHello = null;
    }

    public String getEndpoint() {
        return _endpoint;
    }

    public void setEndpoint(String endpoint) {
        _endpoint = endpoint;
        if (__sayHello != null) ((javax.xml.rpc.Stub) __sayHello)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    }

    public oosnippets.learn.webservice.axis.SayHello getSayHello() {
        if (__sayHello == null) _initSayHelloProxy();
        return __sayHello;
    }

    public java.lang.String sayHello() throws java.rmi.RemoteException {
        if (__sayHello == null) _initSayHelloProxy();
        return __sayHello.sayHello();
    }
}
