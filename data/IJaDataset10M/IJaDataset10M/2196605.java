package org.jabber.jabberbeans;

import java.util.*;
import org.jabber.jabberbeans.util.*;

/**
 *
 * @author  Shawn
 * @version 
 */
public class PresenceUserNode {

    Hashtable resources = new Hashtable();

    String name = new String();

    public PresenceUserNode(JID jid, String state, String status) {
        String username = jid.getUsername();
        String server = jid.getServer();
        if (username != null) this.name = username + "@";
        if (server != null) this.name += server;
        ResourceNode user = new ResourceNode(jid, state, status);
        resources.put(jid, user);
    }

    public void addResource(JID jid, String state, String status) {
        ResourceNode user = new ResourceNode(jid, state, status);
        resources.put(jid, user);
    }

    public ResourceNode removeResource(JID jid) {
        return (ResourceNode) this.resources.remove(jid);
    }

    public String getName() {
        return name;
    }

    public Hashtable getResources() {
        return this.resources;
    }

    public int getNumResources() {
        return this.resources.size();
    }

    public void clearResources() {
        this.resources = new Hashtable();
    }

    public boolean containsResource(JID jid) {
        if (jid == null) return false;
        return this.resources.containsKey(jid);
    }

    public String getState(JID jid) {
        if ((ResourceNode) this.resources.get(jid) == null) return null;
        return ((ResourceNode) this.resources.get(jid)).getState();
    }

    public void setState(JID jid, String str) {
        ((ResourceNode) this.resources.get(jid)).setState(str);
    }

    public String getStatus(JID jid) {
        if ((ResourceNode) this.resources.get(jid) == null) return null;
        return ((ResourceNode) this.resources.get(jid)).getStatus();
    }

    public void setStatus(JID jid, String str) {
        ((ResourceNode) this.resources.get(jid)).setStatus(str);
    }

    public String toString() {
        return this.name;
    }

    public int hashCode() {
        return toString().toLowerCase().hashCode();
    }
}
