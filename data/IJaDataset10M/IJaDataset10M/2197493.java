package org.epoline.print.server;

import java.util.Properties;
import net.jini.core.lookup.ServiceID;
import org.apache.log4j.Logger;
import org.epoline.jsf.services.core.JiniService;
import org.epoline.jsf.services.events.ServiceActionListener;
import org.epoline.jsf.utils.Log4jManager;
import org.epoline.print.shared.PrintManagerServiceProxy;
import org.epoline.service.support.ServiceSupport;

public class StartPrintService implements ServiceActionListener, Runnable {

    private static Logger log;

    private PrintManagerService server;

    private PrintManagerServiceMonitor monitor;

    /**
	 * Starts the application.
	 * @param args an array of command-line arguments
	 */
    public static void main(java.lang.String[] args) {
        try {
            StartPrintService service = new StartPrintService();
            service.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            Thread.sleep(10000);
            System.exit(0);
        } catch (InterruptedException e) {
        }
    }

    public void serviceStarted(ServiceID id, String hostName) {
        if (log.isInfoEnabled()) log.info("Service " + id.toString() + " started on host " + hostName);
    }

    public void serviceTerminated(ServiceID id, String hostName) {
        if (log.isInfoEnabled()) log.info("Service " + id.toString() + " terminated on host " + hostName);
        server.stop();
        new Thread(this).start();
    }

    /**
	 * Starts the application.
	 * @param args an array of command-line arguments
	 */
    public void start() throws Exception {
        Properties props = ServiceSupport.getProperties("org.epoline.print.server.PrintManagerService");
        System.getProperties().putAll(props);
        Log4jManager.setLoggerConfig(props);
        log = Log4jManager.getLogger(StartPrintService.class);
        PrintManagementController.setLog(log);
        server = new PrintManagerService(props);
        PrintManagerServiceProxy proxy = new PrintManagerServiceProxy(server);
        JiniService js = new JiniService(proxy, this, props);
        monitor = new PrintManagerServiceMonitor(proxy, props, js, log);
        PrintManagementController.getInstance().setMonitor(monitor);
        if (props.getProperty("ACTIVATEJINI", "No").startsWith("Y")) {
            js.activate();
        }
    }
}
