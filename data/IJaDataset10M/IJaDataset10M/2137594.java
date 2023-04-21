package logbeam.server;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class LogBeamServer {

    private static Logger logger = Logger.getLogger(LogBeamServer.class);

    private AgentMonitor agentMonitor;

    private Runnable messageCleaner;

    private Runnable clientPublisher;

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(args);
        LogBeamServer server = context.getBean(LogBeamServer.class);
        server.run();
        System.exit(0);
    }

    public void run() {
        Thread.currentThread().setName("LogBeam-Server-Thread");
        Thread publisherThread = new Thread(clientPublisher);
        publisherThread.setDaemon(true);
        publisherThread.start();
        Thread cleanerThread = new Thread(messageCleaner, "Message-Cleaner-Thread");
        cleanerThread.setDaemon(true);
        cleanerThread.start();
        logger.info("LogBeam server is running");
        while (true) {
            try {
                Thread.sleep(agentMonitor.getKeepAliveInterval() * 1000L);
            } catch (InterruptedException ie) {
                throw new RuntimeException(ie);
            }
            agentMonitor.keepAliveCheck();
        }
    }

    public AgentMonitor getAgentMonitor() {
        return agentMonitor;
    }

    public void setAgentMonitor(AgentMonitor agentMonitor) {
        this.agentMonitor = agentMonitor;
    }

    public void setMessageCleaner(Runnable messageCleaner) {
        this.messageCleaner = messageCleaner;
    }

    public Runnable getClientPublisher() {
        return clientPublisher;
    }

    public void setClientPublisher(Runnable clientPublisher) {
        this.clientPublisher = clientPublisher;
    }
}
