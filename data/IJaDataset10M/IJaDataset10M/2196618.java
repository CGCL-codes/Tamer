package org.javadelic.burrow.query;

import java.util.Vector;
import org.xml.sax.Attributes;

public class AgentEntry extends Object {

    public static boolean bDebug = false;

    private String jid;

    private boolean groupchat;

    private String name;

    private String description;

    private String service;

    private boolean register;

    private boolean search;

    private String transport;

    private String url;

    public AgentEntry(Attributes atts) {
        jid = atts.getValue("jid");
        groupchat = false;
        register = false;
        search = false;
        debug("AgentEntry.jid = " + jid);
    }

    public void setGroupchat() {
        this.groupchat = true;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setService(String service) {
        this.service = service;
    }

    public void setRegister() {
        this.register = true;
    }

    public void setSearch() {
        this.search = true;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getJid() {
        return jid;
    }

    public boolean getGroupchat() {
        return groupchat;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getService() {
        return service;
    }

    public boolean getRegister() {
        return register;
    }

    public boolean getSearch() {
        return search;
    }

    public String getTransport() {
        return transport;
    }

    public String getUrl() {
        return url;
    }

    private void debug(String msg) {
        if (bDebug) {
            System.out.println(msg);
        }
    }
}
