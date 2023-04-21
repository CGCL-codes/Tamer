package com.unitt.servicemanager.hazelcast.websocket;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.hazelcast.core.HazelcastInstance;
import com.unitt.servicemanager.util.ValidationUtil;
import com.unitt.servicemanager.websocket.MessageRoutingInfo;
import com.unitt.servicemanager.websocket.MessageSerializerRegistry;
import com.unitt.servicemanager.websocket.MessagingWebSocket;
import com.unitt.servicemanager.websocket.SerializedMessageBody;
import com.unitt.servicemanager.websocket.ServerWebSocket;

public class HazelcastWebSocket extends MessagingWebSocket {

    private static Logger logger = LoggerFactory.getLogger(HazelcastWebSocket.class);

    private HazelcastInstance hazelcastClient;

    private BlockingQueue<MessageRoutingInfo> headerQueue;

    private String headerQueueName;

    protected boolean isInitialized;

    public HazelcastWebSocket() {
    }

    public HazelcastWebSocket(String aServerId, MessageSerializerRegistry aSerializers, long aQueueTimeoutInMillis, String aHeaderQueueName, ServerWebSocket aServerWebSocket, HazelcastInstance aHazelcastClient) {
        this(aServerId, aSerializers, aQueueTimeoutInMillis, aHeaderQueueName, aServerWebSocket, aHazelcastClient, null);
    }

    public HazelcastWebSocket(String aServerId, MessageSerializerRegistry aSerializers, long aQueueTimeoutInMillis, String aHeaderQueueName, ServerWebSocket aServerWebSocket, HazelcastInstance aHazelcastClient, String aSocketId) {
        super(aServerId, aSerializers, aQueueTimeoutInMillis, aServerWebSocket, aSocketId);
        setHazelcastClient(aHazelcastClient);
        setHeaderQueueName(aHeaderQueueName);
    }

    public void initialize() {
        String missing = null;
        if (getSocketId() == null) {
            setSocketId(UUID.randomUUID().toString());
        }
        if (getQueueTimeoutInMillis() == 0) {
            setQueueTimeoutInMillis(30000);
        }
        if (getSerializerRegistry() == null) {
            missing = ValidationUtil.appendMessage(missing, "Missing serializer registry. ");
        }
        if (getServerWebSocket() == null) {
            missing = ValidationUtil.appendMessage(missing, "Missing server web socket. ");
        }
        if (getHazelcastClient() == null) {
            missing = ValidationUtil.appendMessage(missing, "Missing hazelcast client. ");
        }
        if (getHazelcastClient().getQueue(getSocketId()) == null) {
            missing = ValidationUtil.appendMessage(missing, "Missing socket queue: " + getSocketId() + ". ");
        }
        if (getHazelcastClient().getMap(getSocketId()) == null) {
            missing = ValidationUtil.appendMessage(missing, "Missing socket map: " + getSocketId() + ". ");
        }
        if (getHeaderQueueName() == null) {
            missing = ValidationUtil.appendMessage(missing, "Missing header queue name.");
        }
        if (getHeaderQueue() == null) {
            missing = ValidationUtil.appendMessage(missing, "Missing header queue: " + getHeaderQueueName() + ". ");
        }
        if (getServerId() == null) {
            missing = ValidationUtil.appendMessage(missing, "Missing server id. ");
        }
        if (missing != null) {
            logger.error(missing);
            throw new IllegalStateException(missing);
        }
        setInitialized(true);
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    public void destroy() {
        try {
            getHazelcastClient().getMap(getSocketId()).destroy();
        } catch (Exception e) {
            logger.error("An error occurred while destroying the body map for websocket: " + getSocketId(), e);
        }
        setHazelcastClient(null);
        setHeaderQueueName(null);
        setServerId(null);
        headerQueue = null;
        isInitialized = false;
    }

    public HazelcastInstance getHazelcastClient() {
        return hazelcastClient;
    }

    public void setHazelcastClient(HazelcastInstance aClient) {
        hazelcastClient = aClient;
    }

    public String getHeaderQueueName() {
        return headerQueueName;
    }

    public void setHeaderQueueName(String aHeaderQueueName) {
        headerQueueName = aHeaderQueueName;
    }

    public ConcurrentMap<String, SerializedMessageBody> getBodyMap() {
        return getHazelcastClient().getMap("body:" + getSocketId());
    }

    public BlockingQueue<MessageRoutingInfo> getHeaderQueue() {
        if (headerQueue == null) {
            headerQueue = getHazelcastClient().getQueue(getHeaderQueueName());
        }
        return headerQueue;
    }
}
