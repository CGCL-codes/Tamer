package net.sf.dropboxmq;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created: 18 Jun 2008
 *
 * @author <a href="mailto:dwayne@schultz.net">Dwayne Schultz</a>
 * @version $Revision: 231 $, $Date: 2011-08-12 23:50:47 -0400 (Fri, 12 Aug 2011) $
 */
public class RouterTest extends TestCase {

    private static final Log log = LogFactory.getLog(RouterTest.class);

    private File incomingDir;

    private File routingDir;

    private Session session;

    private MessageProducer producer;

    private MessageConsumer consumer;

    @Override
    protected void setUp() throws NamingException, JMSException {
        final File root = FunctionalHelper.createTestDirectory(getName());
        final Properties properties = FunctionalHelper.createStandardJNDIProperties(root);
        final Queue queue = FunctionalHelper.createQueue("queue", properties);
        final File dropboxDir = FunctionalHelper.getDropboxDir(queue, properties);
        incomingDir = new File(dropboxDir, "incoming");
        routingDir = new File(incomingDir, "routing");
        final InitialContext context = new InitialContext(properties);
        final ConnectionFactory factory = (ConnectionFactory) context.lookup(Configuration.DEFAULT_CONNECTION_FACTORY_NAME);
        final Connection connection = factory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        producer = session.createProducer(queue);
        consumer = session.createConsumer(queue);
    }

    public void testRouting() throws NamingException, JMSException, InterruptedException, IOException {
        final File oddDir = new File(routingDir, "odd");
        oddDir.mkdirs();
        new File(routingDir, "odd.in-use").createNewFile();
        final Properties properties = new Properties();
        properties.setProperty("odd.selector", "JMSType = 'odd'");
        properties.setProperty("all-routed.selector", "true");
        writeRoutingProperties(properties);
        final int iterations = 20;
        for (int i = 0; i < iterations; i++) {
            final TextMessage message = session.createTextMessage(String.valueOf(i));
            message.setJMSType(i % 2 == 0 ? "even" : "odd");
            if (i >= iterations - 2) {
                message.setBooleanProperty("JMS_dropboxmq_do_not_route", true);
            }
            producer.send(message);
        }
        final File allRoutedDir = new File(routingDir, "all-routed");
        int count = 0;
        while (consumer.receiveNoWait() != null) {
            count++;
            if (count >= 5) {
                allRoutedDir.mkdirs();
                new File(routingDir, "all-routed.in-use").createNewFile();
            }
        }
        assertEquals(7, count);
        assertEquals(4, oddDir.list().length);
        assertEquals(9, allRoutedDir.list().length);
    }

    private void writeRoutingProperties(final Properties properties) throws IOException {
        final FileOutputStream out = new FileOutputStream(new File(incomingDir, "routing.properties"));
        properties.store(out, null);
        out.close();
    }

    public void testThoughput() throws JMSException, IOException, InterruptedException {
        final int count = 1000;
        doTestThroughput(count, "No routes", false);
        doTestThroughput(count, "No routes", false);
        final Properties properties = new Properties();
        properties.setProperty("one.selector", "nothing = 'matched'");
        writeRoutingProperties(properties);
        doTestThroughput(count, "One no-op route, not in use", false);
        final File oneDir = new File(routingDir, "one");
        oneDir.mkdirs();
        doTestThroughput(count, "One no-op route with dir, not in use", false);
        new File(routingDir, "odd.in-use").createNewFile();
        doTestThroughput(count, "One no-op route with dir, in use", false);
        final int other = 49;
        for (int i = 0; i < other; i++) {
            properties.setProperty("other-" + i + ".selector", "nothing" + i + " = 'matched" + i + "'");
        }
        writeRoutingProperties(properties);
        doTestThroughput(count, "50 no-op routes, one in use", false);
        for (int i = 0; i < other; i++) {
            final File otherDir = new File(routingDir, "other-" + i);
            otherDir.mkdirs();
        }
        doTestThroughput(count, "50 no-op routes with dirs, one in use", false);
        for (int i = 0; i < other; i++) {
            new File(routingDir, "other-" + i + ".in-use").createNewFile();
        }
        doTestThroughput(count, "50 no-op routes with dirs, in use", false);
        properties.setProperty("all.selector", "true");
        writeRoutingProperties(properties);
        doTestThroughput(count, "All route, 50 no-op routes, 50 in use", false);
        final File allDir = new File(routingDir, "all");
        allDir.mkdirs();
        doTestThroughput(count, "All route with dir, 50 no-op routes with dirs, 50 in use", false);
        new File(routingDir, "all.in-use").createNewFile();
        doTestThroughput(count, "All route with dir, 50 no-op routes with dirs, all in use", true);
    }

    private void doTestThroughput(final int count, final String message, final boolean allRouteActive) throws JMSException, InterruptedException {
        produceMessages(count);
        Thread.sleep(5000L);
        final long consumeStart = System.currentTimeMillis();
        consumeMessages(allRouteActive ? 0 : count);
        log.error("XXX " + message + ", " + (System.currentTimeMillis() - consumeStart) + " ms");
    }

    private void produceMessages(final int count) throws JMSException {
        for (int i = 0; i < count; i++) {
            final TextMessage message = session.createTextMessage(String.valueOf(i));
            producer.send(message);
        }
    }

    private void consumeMessages(final int expectedCount) throws JMSException {
        int count = 0;
        while (consumer.receiveNoWait() != null) {
            count++;
        }
        assertEquals(expectedCount, count);
    }

    public void testRouteOrder() throws IOException, JMSException, InterruptedException {
        final Properties properties = new Properties();
        properties.setProperty("a.selector", "true");
        properties.setProperty("b.selector", "true");
        properties.setProperty("c.selector", "true");
        properties.setProperty("d.selector", "true");
        properties.setProperty("e.selector", "true");
        properties.setProperty("f.selector", "true");
        properties.setProperty("g.selector", "true");
        writeRoutingProperties(properties);
        final File aDir = new File(routingDir, "a");
        aDir.mkdirs();
        new File(routingDir, "b").mkdirs();
        new File(routingDir, "c").mkdirs();
        new File(routingDir, "d").mkdirs();
        new File(routingDir, "e").mkdirs();
        new File(routingDir, "f").mkdirs();
        new File(routingDir, "g").mkdirs();
        new File(routingDir, "a.in-use").createNewFile();
        new File(routingDir, "b.in-use").createNewFile();
        new File(routingDir, "c.in-use").createNewFile();
        new File(routingDir, "d.in-use").createNewFile();
        new File(routingDir, "e.in-use").createNewFile();
        new File(routingDir, "f.in-use").createNewFile();
        new File(routingDir, "g.in-use").createNewFile();
        final TextMessage message = session.createTextMessage("message");
        producer.send(message);
        consumer.receiveNoWait();
        assertEquals(1, aDir.list().length);
    }
}
