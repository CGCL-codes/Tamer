package system.net;

import java.util.ArrayList;
import java.util.Properties;
import remedium.Remedium;
import org.junit.Test;
import system.mq.msg;
import static org.junit.Assert.*;

/**
 *
 * @author Nuno Brito, 18th of May 2011 in Pittsburgh, USA.
 */
public class network_test implements msg {

    Remedium A = new Remedium(), B = new Remedium();

    public network_test() {
    }

    @Test
    public void simulateTickets() throws InterruptedException {
        Properties parameters = new Properties();
        System.out.println("Starting communication simulation between " + "two instances");
        parameters.setProperty(FIELD_PORT, PORT_A);
        parameters.setProperty(FIELD_ID, "A");
        A = addFilters(A);
        B = addFilters(B);
        A.start(parameters);
        parameters.setProperty(FIELD_PORT, PORT_B);
        parameters.setProperty(FIELD_ID, "B");
        parameters.setProperty(LISTEN, "");
        B.start(parameters);
        Properties message = new Properties();
        message.setProperty(FIELD_FROM, "Nuno");
        message.setProperty(FIELD_TO, "CentrumServer");
        message.setProperty(FIELD_ADDRESS, addressB);
        message.setProperty(FIELD_MESSAGE, "Hi there Centrum server!");
        System.out.println("Sending message to instance B at " + addressB);
        A.getMQ().send(message);
        System.out.println("Waiting some seconds..");
        utils.time.wait(5);
        System.out.println("Enough waiting");
        ArrayList<Properties> AppB = B.getMQ().get("CentrumServer");
        if (AppB.isEmpty()) fail("There exists no message on the queue of instance B");
        Properties Hello = AppB.get(0);
        B.getMQ().deleteTicket(Hello.getProperty(FIELD_TICKET));
        Hello.setProperty(FIELD_STATUS, Integer.toString(COMPLETED));
        Hello.setProperty(FIELD_MESSAGE, "Hi there guy, how are things?");
        B.getMQ().send(Hello);
        utils.time.wait(8);
        parameters.clear();
        parameters.setProperty("DELETE", "");
        A.stop(parameters);
        B.stop(parameters);
    }

    private static Remedium addFilters(Remedium instance) {
        instance.addLogFilter("apps");
        instance.addLogFilter("log");
        instance.addLogFilter("database");
        instance.addLogFilter("main");
        instance.addLogFilter(manager);
        instance.addLogFilter(sentinel);
        instance.addLogFilter(centrum);
        instance.addLogFilter(sentinel + "/indexer");
        instance.addLogFilter(sentinel + "/scanner");
        instance.addLogFilter("network_server");
        instance.addLogFilter("triumvir/client");
        return instance;
    }
}
