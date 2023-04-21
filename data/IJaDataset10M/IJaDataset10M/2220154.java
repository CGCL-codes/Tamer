package net.sf.dropboxmq.destinations;

import javax.jms.JMSException;
import javax.jms.Queue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import net.sf.dropboxmq.LogHelper;

/**
 * Created: 09 Oct 2005
 * @author <a href="mailto:dwayne@schultz.net">Dwayne Schultz</a>
 * @version $Revision: 219 $, $Date: 2010-12-05 16:40:43 -0500 (Sun, 05 Dec 2010) $
 */
public class QueueImpl extends DestinationImpl implements Queue {

    private static final long serialVersionUID = 7088333834468466593L;

    private static final Log log = LogFactory.getLog(QueueImpl.class);

    public QueueImpl() {
        LogHelper.logMethod(log, toObjectString(), "QueueImpl()");
    }

    public QueueImpl(final String name) {
        super(name);
        LogHelper.logMethod(log, toObjectString(), "QueueImpl(), name = " + name);
    }

    public String getQueueName() throws JMSException {
        LogHelper.logMethod(log, toObjectString(), "getQueueName()");
        return getName();
    }
}
