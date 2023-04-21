package org.wintersleep.usermgmt.wicket;

import org.apache.log4j.Logger;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.ContextHandlerCollection;
import org.mortbay.jetty.webapp.WebAppContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * You need to run this with JVM option:
 *
 * -javaagent:/udir/stappend/maven/repositories/local/org/springframework/spring-agent/2.5.5/spring-agent-2.5.5.jar
 */
public class Main {

    private static final Logger log = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        String[] configLocations = new String[] { "/spring-jetty.xml" };
        ApplicationContext context = new ClassPathXmlApplicationContext(configLocations);
        Server server = (Server) context.getBean("jettyServer");
        String host = server.getConnectors()[0].getHost();
        if (host == null) {
            host = "localhost";
        }
        int port = server.getConnectors()[0].getPort();
        Handler[] handlers = ((ContextHandlerCollection) server.getHandlers()[0]).getHandlers();
        String contextPath = ((WebAppContext) handlers[0]).getContextPath();
        if (log.isInfoEnabled()) {
            log.info("server started - 'http://" + host + ":" + port + contextPath + "'");
        }
    }
}
