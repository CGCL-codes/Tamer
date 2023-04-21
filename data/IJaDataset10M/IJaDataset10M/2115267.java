package demo.hw.client;

import javax.xml.ws.WebServiceException;
import org.apache.hello_world_soap_http.Greeter;
import org.apache.hello_world_soap_http.PingMeFault;
import org.apache.hello_world_soap_http.types.FaultDetail;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class SpringClient {

    private SpringClient() {
    }

    public static void main(String args[]) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "/demo/hw/client/client-beans.xml" });
        Greeter port = (Greeter) context.getBean("client");
        String resp;
        System.out.println("Invoking sayHi...");
        resp = port.sayHi();
        System.out.println("Server responded with: " + resp);
        System.out.println();
        System.out.println("Invoking greetMe...");
        resp = port.greetMe(System.getProperty("user.name"));
        System.out.println("Server responded with: " + resp);
        System.out.println();
        System.out.println("Invoking greetMe with invalid length string, expecting exception...");
        try {
            resp = port.greetMe("Invoking greetMe with invalid length string, expecting exception...");
        } catch (WebServiceException ex) {
            System.out.println("Caught expected WebServiceException:");
            System.out.println("    " + ex.getMessage());
        }
        System.out.println();
        System.out.println("Invoking greetMeOneWay...");
        port.greetMeOneWay(System.getProperty("user.name"));
        System.out.println("No response from server as method is OneWay");
        System.out.println();
        try {
            System.out.println("Invoking pingMe, expecting exception...");
            port.pingMe();
        } catch (PingMeFault ex) {
            System.out.println("Expected exception: PingMeFault has occurred: " + ex.getMessage());
            FaultDetail detail = ex.getFaultInfo();
            System.out.println("FaultDetail major:" + detail.getMajor());
            System.out.println("FaultDetail minor:" + detail.getMinor());
        }
        System.exit(0);
    }
}
