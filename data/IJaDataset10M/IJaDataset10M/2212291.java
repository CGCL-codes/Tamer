package com.unitt.servicemanager.hazelcast.response;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;
import com.unitt.servicemanager.response.ResponseQueueManager;
import com.unitt.servicemanager.response.UndeliverableMessageHandler;
import com.unitt.servicemanager.util.ValidationUtil;
import com.unitt.servicemanager.websocket.MessageResponse;
import com.unitt.servicemanager.websocket.MessagingWebSocket;

public class HazelcastResponseQueueManager extends ResponseQueueManager {

    private static Logger logger = LoggerFactory.getLogger(HazelcastResponseQueueManager.class);

    private HazelcastInstance hazelcastClient;

    public HazelcastResponseQueueManager() {
    }

    public HazelcastResponseQueueManager(long aQueueTimeoutInMillis, int aNumberOfWorkers, Map<String, MessagingWebSocket> aSockets, UndeliverableMessageHandler aUndeliverableMessageHandler, String aServerId, HazelcastInstance aHazelcastClient) {
        super(aServerId, aQueueTimeoutInMillis, aNumberOfWorkers, aSockets, aUndeliverableMessageHandler);
        setHazelcastClient(aHazelcastClient);
    }

    public void initialize() {
        if (getSockets() == null) {
            setSockets(new HashMap<String, MessagingWebSocket>());
        }
        String missing = null;
        if (hazelcastClient == null) {
            missing = ValidationUtil.appendMessage(missing, "Missing hazelcast client. ");
        }
        if (getSockets() == null) {
            missing = ValidationUtil.appendMessage(missing, "Missing sockets map. ");
        }
        if (missing != null) {
            logger.error(missing);
            throw new IllegalStateException(missing);
        }
        super.initialize();
    }

    public void destroy() {
        super.destroy();
        try {
            IQueue<?> queue = (IQueue<?>) getSocketQueue();
            if (queue != null) {
                queue.destroy();
            }
        } catch (Exception e) {
            logger.error("An error occurred cleaning up the socket queue: " + this, e);
        }
        setHazelcastClient(null);
    }

    public HazelcastInstance getHazelcastClient() {
        return hazelcastClient;
    }

    public void setHazelcastClient(HazelcastInstance aClient) {
        hazelcastClient = aClient;
    }

    @Override
    public BlockingQueue<MessageResponse> getSocketQueue() {
        String queueName = "outgoing:" + getServerId();
        if (getServerId() != null) {
            return getHazelcastClient().getQueue(queueName);
        }
        logger.error("Could not determine socket queue: queueName=" + queueName);
        return null;
    }
}
