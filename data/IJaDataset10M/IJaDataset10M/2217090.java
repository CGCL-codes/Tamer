package net.sf.dropboxmq;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Properties;
import java.util.TreeSet;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import junit.framework.TestCase;
import net.sf.dropboxmq.dropboxsupport.MessageData;
import net.sf.dropboxmq.messages.MessageImpl;
import net.sf.dropboxmq.messagetranscoders.DefaultMessageTranscoder;
import net.sf.dropboxmq.sessions.SessionImpl;

/**
 * Created: 12 Feb 2008
 *
 * @author <a href="mailto:dwayne@schultz.net">Dwayne Schultz</a>
 * @version $Revision: 231 $, $Date: 2011-08-12 23:50:47 -0400 (Fri, 12 Aug 2011) $
 */
public class DropboxTest extends TestCase {

    private File incomingDir = null;

    private SessionImpl session = null;

    private MessageProducer producer = null;

    private MessageConsumer consumer = null;

    private Properties setupDropbox() throws NamingException, JMSException {
        final File root = FunctionalHelper.createTestDirectory(getName());
        final Properties properties = FunctionalHelper.createStandardJNDIProperties(root);
        final Queue queue = FunctionalHelper.createQueue("queue", properties);
        final File dropboxDir = FunctionalHelper.getDropboxDir(queue, properties);
        incomingDir = new File(dropboxDir, "incoming");
        final InitialContext context = new InitialContext(properties);
        final ConnectionFactory factory = (ConnectionFactory) context.lookup(Configuration.DEFAULT_CONNECTION_FACTORY_NAME);
        final Connection connection = factory.createConnection();
        connection.start();
        session = (SessionImpl) connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        producer = session.createProducer(queue);
        consumer = session.createConsumer(queue);
        return properties;
    }

    public void testMessageDataSet() throws JMSException, IOException, NamingException {
        setupDropbox();
        final String filename1 = "150.1202784776555000-1010120080213025621.T";
        final String filename2 = "150.1202784776555000-2091020080213025619.T";
        final Message message1 = new DefaultMessageTranscoder(null).decodeMetadata(session, new File("."), filename1, false, null);
        final Message message2 = new DefaultMessageTranscoder(null).decodeMetadata(session, new File("."), filename2, false, null);
        final MessageData data1 = new MessageData(filename1, (MessageImpl) message1);
        final MessageData data2 = new MessageData(filename2, (MessageImpl) message2);
        final Collection<MessageData> set = new TreeSet<MessageData>();
        set.add(data1);
        set.add(data2);
        assertEquals(2, set.size());
    }

    public void testDeleteExpired() throws JMSException, NamingException, InterruptedException {
        setupDropbox();
        final File expiredDir = new File(incomingDir, "expired");
        doTestExpired(expiredDir);
        assertEquals(1, expiredDir.list().length);
        expiredDir.listFiles()[0].delete();
        final Properties properties = setupDropbox();
        properties.setProperty("net.sf.dropboxmq.deleteExpiredMessages", "true");
        doTestExpired(expiredDir);
        assertEquals(0, expiredDir.list().length);
    }

    private void doTestExpired(final File expiredDir) throws JMSException, InterruptedException {
        producer.send(session.createMessage(), Message.DEFAULT_DELIVERY_MODE, Message.DEFAULT_PRIORITY, 1L);
        Thread.sleep(100L);
        final File targetDir = new File(incomingDir, "target");
        assertEquals(1, targetDir.list().length);
        assertEquals(0, expiredDir.list().length);
        assertNull(consumer.receiveNoWait());
        assertEquals(0, targetDir.list().length);
    }
}
