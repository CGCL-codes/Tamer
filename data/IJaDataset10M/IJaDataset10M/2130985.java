package nl.gridshore.monitoring.jdk;

import nl.gridshore.monitoring.Contact;
import nl.gridshore.monitoring.ContactService;
import nl.gridshore.monitoring.InMemoryContactService;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;

/**
 * Runner to demo jmx using the jdk provided mechanism
 *
 * @author Jettro Coenradie
 */
public class Runner {

    public static void main(String[] args) throws Exception {
        MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
        ContactService service = new InMemoryContactService();
        ContactServiceMonitorMXBean mbean = new ContactServiceMonitor(service);
        ObjectName monitorName = new ObjectName("Gridshore:name=contactServiceMonitor");
        mbeanServer.registerMBean(mbean, monitorName);
        String input = "";
        do {
            System.out.println("Type stop if you want to, well euh, stop !");
            input = (new BufferedReader(new InputStreamReader(System.in))).readLine();
            service.addContact(new Contact(0, input));
        } while (!"stop".equals(input));
    }
}
