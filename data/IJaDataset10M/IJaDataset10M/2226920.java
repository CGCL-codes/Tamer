package net.sf.anima.test.routing.jms;

import javax.naming.InitialContext;
import net.sf.anima.persistence.ejb.Message;
import org.apache.log4j.Logger;
import de.kisner.util.Connector;
import de.kisner.util.LoggerInit;
import de.kisner.util.jms.ptp.PtpPublisher;
import de.kisner.util.xml.XmlConfig;

public class JmsProducer {

    static Logger logger = Logger.getLogger(JmsProducer.class);

    public static void main(String args[]) throws Exception {
        LoggerInit loggerInit = new LoggerInit("log4j.xml");
        loggerInit.addAltPath("resources/config");
        loggerInit.init();
        XmlConfig xCnf = new XmlConfig("Anima.xml");
        String jndiHost = xCnf.getAttribute("net/jndi", "host") + ":" + xCnf.getAttribute("net/jndi", "port");
        String messageSelector = "";
        logger.debug("JNDI -> " + jndiHost);
        InitialContext ctx = Connector.getContext(jndiHost);
        JmsConsumer jmsC = new JmsConsumer();
        PtpPublisher ptp = new PtpPublisher(ctx, "queue/XmppMessage");
        Message msg = new Message();
        msg.setFrom("Alice");
        msg.setTo("Bob");
        msg.setBody("Nachrichtentext ...");
        ptp.sendObject(msg);
        ptp.stop();
    }
}
