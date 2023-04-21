package control;

import webServices.WebServices;
import javax.xml.ws.Endpoint;
import java.util.concurrent.Callable;

public class WSThread implements Callable {

    private WebServices webServices;

    private String address;

    private void startServices() {
        try {
            Object implementor = webServices;
            System.out.println("About to start ...." + address);
            Endpoint.publish(address, implementor);
            System.out.println("started web services ");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Object call() throws Exception {
        startServices();
        return null;
    }

    public void setWebServices(WebServices webServices) {
        this.webServices = webServices;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
