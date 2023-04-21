package org.jamesq.core.agent;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.jms.JMSException;
import javax.jms.MessageListener;
import org.jamesq.core.agent.JobQueueClient;
import org.jamesq.core.common.JobQueueInfo;

/**
 * Job queue information querier.
 * 
 * The class simply implements a simple message producer to connect to the
 * broker to get a specific queue information. 
 * 
 * @author Hurng-Chun Lee <hurngchunlee@gmail.com>
 */
public class JobQueueInfoQuerier extends JobQueueClient {

    private JobQueueInfoQuerier(URI brokerURI, String queueName) {
        super(brokerURI, queueName, JobQueueClient.CLIENT_PRODUCER);
    }

    /**
     * gets information of a list of queues.
     * 
     * @param brokerURI the JAMESQ broker URI
     * @param queues a list of job queues in full name (e.g. jamesq.sched.queue1 )
     * @return a list of job queue information represented by the {@link JobQueueInfo} object of each
     * @throws javax.jms.JMSException
     */
    public static List<JobQueueInfo> getQueuesInfo(URI brokerURI, List<String> queues) throws JMSException {
        JobQueueInfoQuerier querier = new JobQueueInfoQuerier(brokerURI, "");
        List<JobQueueInfo> info = new ArrayList();
        try {
            querier.connect();
            Iterator<String> it = queues.iterator();
            while (it.hasNext()) {
                querier.setQueueName(it.next());
                info.add(querier.getQueueInfo());
            }
        } finally {
            querier.disconnect();
        }
        return info;
    }

    @Override
    public MessageListener getNewJobListener() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
