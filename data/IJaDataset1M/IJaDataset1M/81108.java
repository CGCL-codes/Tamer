package marten.aoe.server;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.LinkedList;
import marten.aoe.server.serializable.ChatMessage;
import marten.aoe.server.serializable.ServerNotification;

public class ServerClient {

    private LinkedList<ChatMessage> inbox = new LinkedList<ChatMessage>();

    private LinkedList<ServerNotification> notifier = new LinkedList<ServerNotification>();

    private String username;

    private String secret;

    private String engineUrl = null;

    private long pingTime;

    public ServerClient(String username) {
        this.username = username;
        this.secret = new BigInteger(130, new SecureRandom()).toString(32);
    }

    public String getEngineUrl() {
        return this.engineUrl;
    }

    public void setPingTime(long time) {
        this.pingTime = time;
    }

    public long getPingTime() {
        return this.pingTime;
    }

    public void setEngineUrl(String url) {
        this.engineUrl = url;
    }

    public String getSecret() {
        return this.secret;
    }

    public LinkedList<ServerNotification> getNotifier() {
        return this.notifier;
    }

    public String getUsername() {
        return this.username;
    }

    public synchronized ChatMessage getLastMessage() {
        if (inbox.isEmpty()) {
            return null;
        } else {
            return inbox.pop();
        }
    }

    public synchronized void addMessage(ChatMessage message) {
        inbox.add(message);
        synchronized (this.notifier) {
            this.notifier.add(ServerNotification.NEW_MESSAGE);
            this.notifier.notifyAll();
        }
    }

    public void notify(ServerNotification notification) {
        synchronized (this.notifier) {
            this.notifier.add(notification);
            this.notifier.notifyAll();
        }
    }
}
