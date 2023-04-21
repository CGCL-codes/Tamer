package hermes.providers.file;

import hermes.HermesException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueSender;
import org.apache.log4j.Logger;

/**
 * An XML file provider.
 * 
 * @author colincrist@hermesjms.com
 * @version $Id: FileMessageProducer.java,v 1.2 2004/05/08 15:15:48 colincrist
 *          Exp $
 */
public class FileMessageProducer implements QueueSender, Commitable {

    private static final Logger log = Logger.getLogger(FileMessageProducer.class);

    private FileSession session;

    private FileQueue queue;

    private Collection messages = new ArrayList();

    public FileMessageProducer(FileSession session, FileQueue queue) throws JMSException {
        this.session = session;
        this.queue = queue;
    }

    public void close() throws JMSException {
    }

    public int getDeliveryMode() throws JMSException {
        return 1;
    }

    public boolean getDisableMessageID() throws JMSException {
        return false;
    }

    public boolean getDisableMessageTimestamp() throws JMSException {
        return false;
    }

    public int getPriority() throws JMSException {
        return 1;
    }

    public long getTimeToLive() throws JMSException {
        return 0;
    }

    public void setDeliveryMode(int arg0) throws JMSException {
    }

    public void setDisableMessageID(boolean arg0) throws JMSException {
    }

    public void setDisableMessageTimestamp(boolean arg0) throws JMSException {
    }

    public void setPriority(int arg0) throws JMSException {
    }

    public void setTimeToLive(long arg0) throws JMSException {
    }

    public void commit() throws JMSException {
        if (messages.size() > 0) {
            try {
                if (queue == null) {
                    for (Iterator iter = messages.iterator(); iter.hasNext(); ) {
                        Message m = (Message) iter.next();
                        if (m.getJMSDestination() != null) {
                            if (m.getJMSDestination() instanceof FileQueue) {
                                FileQueue queue = (FileQueue) m.getJMSDestination();
                                Collection messages = new ArrayList();
                                messages.add(m);
                                queue.addMessages(session, messages);
                            } else {
                                throw new HermesException("destination is not a FileQueue");
                            }
                        } else {
                            throw new HermesException("message has no destination");
                        }
                    }
                } else {
                    queue.addMessages(session, messages);
                }
            } catch (IOException e) {
                throw new HermesException(e);
            }
        }
    }

    public void rollback() throws JMSException {
        messages.clear();
    }

    public Queue getQueue() throws JMSException {
        return queue;
    }

    public void send(Message arg0, int arg1, int arg2, long arg3) throws JMSException {
        messages.add(arg0);
        if (!session.getTransacted()) {
            commit();
        }
    }

    public void send(Message arg0) throws JMSException {
        messages.add(arg0);
        if (!session.getTransacted()) {
            commit();
        }
    }

    public void send(Queue arg0, Message arg1, int arg2, int arg3, long arg4) throws JMSException {
        arg1.setJMSDestination(arg0);
        messages.add(arg1);
        if (!session.getTransacted()) {
            commit();
        }
    }

    public void send(Queue arg0, Message arg1) throws JMSException {
        arg1.setJMSDestination(arg0);
        messages.add(arg1);
        if (!session.getTransacted()) {
            commit();
        }
    }

    public Destination getDestination() throws JMSException {
        return queue;
    }

    public void send(Destination arg0, Message arg1, int arg2, int arg3, long arg4) throws JMSException {
        arg1.setJMSDestination(arg0);
        messages.add(arg1);
        if (!session.getTransacted()) {
            commit();
        }
    }

    public void send(Destination arg0, Message arg1) throws JMSException {
        arg1.setJMSDestination(arg0);
        messages.add(arg1);
        if (!session.getTransacted()) {
            commit();
        }
    }
}
