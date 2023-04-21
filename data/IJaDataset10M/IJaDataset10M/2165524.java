package org.xtoto.start;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.management.MBeanContainer;
import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;

public class Start {

    public static void main(String[] args) {
        SocketConnector connector = new SocketConnector();
        connector.setMaxIdleTime(1000 * 60 * 60);
        connector.setSoLingerTime(-1);
        connector.setPort(80);
        Server server = new Server();
        server.setConnectors(new Connector[] { connector });
        WebAppContext context = new WebAppContext();
        context.setServer(server);
        context.setContextPath("/");
        context.setWar("web");
        if (args.length == 1 && args[0].equals("InsideIntelliJ")) {
            context.setParentLoaderPriority(true);
        }
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        MBeanContainer mBeanContainer = new MBeanContainer(mBeanServer);
        server.getContainer().addEventListener(mBeanContainer);
        mBeanContainer.start();
        server.addHandler(context);
        try {
            System.out.println("STARTING JETTY SERVER, PRESS ANY KEY TO STOP");
            server.start();
            while (System.in.available() == 0) {
                Thread.sleep(5000);
            }
            server.stop();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
