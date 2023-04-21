package otservices.mapper.webservice;

public class RemoteExceptionException0 extends java.lang.Exception {

    private otservices.mapper.webservice.MapperServerWebServiceImplStub.RemoteExceptionE faultMessage;

    public RemoteExceptionException0() {
        super("RemoteExceptionException0");
    }

    public RemoteExceptionException0(java.lang.String s) {
        super(s);
    }

    public RemoteExceptionException0(java.lang.String s, java.lang.Throwable ex) {
        super(s, ex);
    }

    public void setFaultMessage(otservices.mapper.webservice.MapperServerWebServiceImplStub.RemoteExceptionE msg) {
        faultMessage = msg;
    }

    public otservices.mapper.webservice.MapperServerWebServiceImplStub.RemoteExceptionE getFaultMessage() {
        return faultMessage;
    }
}
