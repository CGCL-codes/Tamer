package de.hfu.mmj.loanbroker.creditBureau.test.client;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import de.hfu.mmj.loanbroker.creditBureau.ws.CreditBureau;

/**
 * This is only a demo client to test the credit bureau webservice
 * 
 * @author mmj
 * 
 */
public class Client {

    private static final QName SERVICE_NAME = new QName("http://ws.creditBureau.loanbroaker.mmj.hfu.de/", "CreditBureau");

    private Client() {
    }

    public static void main(String args[]) throws Exception {
        URL endpointAddress = new URL("http://localhost:8100/creditBureau?wsdl");
        Service service = Service.create(endpointAddress, SERVICE_NAME);
        CreditBureau cb = service.getPort(CreditBureau.class);
        for (int i = 0; i < 10; i++) {
            System.out.println(cb.calculateConsumerRate("SSN-343-53-4534"));
        }
    }
}
