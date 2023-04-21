package org.xactor.ws.atomictx;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import org.jboss.mx.util.ObjectNameFactory;
import org.xactor.tm.recovery.RecoveryCommand;

/**
 * A <code>RecoveryCommand</code> that starts the Tomcat connectors.
 * 
 * @author <a href="mailto:ivanneto@gmail.com">Ivan Neto</a>
 */
public class StartTomcatConnectorsRecoveryCommand implements RecoveryCommand {

    private MBeanServer server;

    public StartTomcatConnectorsRecoveryCommand(MBeanServer server) {
        this.server = server;
    }

    public void execute() {
        ObjectName service = ObjectNameFactory.create("jboss.web:service=WebServer");
        Object[] args = {};
        String[] sig = {};
        try {
            server.invoke(service, "startConnectors", args, sig);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
