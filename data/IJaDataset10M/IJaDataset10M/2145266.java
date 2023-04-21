package com.ailk.bi.wsi.util;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;
import com.ailk.bi.wsi.Notify;

public class WSClientTest {

    private static final QName SERVICE_NAME = new QName("http://wsi.bi.ailk.com/", "NotifyServiceService");

    private static final QName PORT_NAME = new QName("http://wsi.bi.ailk.com/", "NotifyServicePort");

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Service service = Service.create(SERVICE_NAME);
        String endpointAddress = "http://localhost:8080/wsi-client/services/NotifyService";
        service.addPort(PORT_NAME, SOAPBinding.SOAP11HTTP_BINDING, endpointAddress);
        Notify notifyService = service.getPort(Notify.class);
        System.out.println(notifyService.createComplete(" cvicse!"));
    }
}
