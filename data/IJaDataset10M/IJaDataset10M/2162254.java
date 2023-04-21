package active_endpoints.docs.wsdl.activebpeladmin._2007._01.activebpeladmin_wsdl;

public class AdminFault extends java.lang.Exception {

    private active_endpoints.docs.wsdl.activebpeladmin._2007._01.activebpeladmin_wsdl.ActiveBpelAdminStub.AdminFaultElement faultMessage;

    public AdminFault() {
        super("AdminFault");
    }

    public AdminFault(java.lang.String s) {
        super(s);
    }

    public AdminFault(java.lang.String s, java.lang.Throwable ex) {
        super(s, ex);
    }

    public AdminFault(java.lang.Throwable cause) {
        super(cause);
    }

    public void setFaultMessage(active_endpoints.docs.wsdl.activebpeladmin._2007._01.activebpeladmin_wsdl.ActiveBpelAdminStub.AdminFaultElement msg) {
        faultMessage = msg;
    }

    public active_endpoints.docs.wsdl.activebpeladmin._2007._01.activebpeladmin_wsdl.ActiveBpelAdminStub.AdminFaultElement getFaultMessage() {
        return faultMessage;
    }
}
