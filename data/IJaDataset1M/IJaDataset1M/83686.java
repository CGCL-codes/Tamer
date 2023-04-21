package org.yawlfoundation.yawl.procletService.persistence;

import org.yawlfoundation.yawl.procletService.models.procletModel.ProcletPort;
import org.yawlfoundation.yawl.procletService.state.Performative;
import org.yawlfoundation.yawl.procletService.state.Performatives;
import org.yawlfoundation.yawl.procletService.util.EntityID;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Michael Adams
 * @date 3/02/12
 */
public class StoredPerformative {

    private long pkey;

    private Timestamp timestamp;

    private String channel;

    private String sender;

    private String receiver;

    private String action;

    private String content;

    private String scope;

    private String direction;

    private String entityIDs;

    public StoredPerformative() {
    }

    public StoredPerformative(Timestamp timestamp, String channel, String sender, String receiver, String action, String content, String scope, String direction, String entityIDs) {
        this.timestamp = timestamp;
        this.channel = channel;
        this.sender = sender;
        this.receiver = receiver;
        this.action = action;
        this.content = content;
        this.scope = scope;
        this.direction = direction;
        this.entityIDs = entityIDs;
    }

    public Performative newPerformative() {
        List<String> receivers = new ArrayList<String>();
        Collections.addAll(receivers, receiver.split(","));
        ProcletPort.Direction dir = ProcletPort.getDirectionFromString(direction);
        List<EntityID> eids = Performatives.parseEntityIDsStr(entityIDs);
        return new Performative(timestamp, channel, sender, receivers, action, content, scope, dir, eids);
    }

    public long getPkey() {
        return pkey;
    }

    public void setPkey(long pkey) {
        this.pkey = pkey;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getEntityIDs() {
        return entityIDs;
    }

    public void setEntityIDs(String entityIDs) {
        this.entityIDs = entityIDs;
    }
}
