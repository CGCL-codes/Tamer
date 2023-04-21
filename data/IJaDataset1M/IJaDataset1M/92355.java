package com.orientechnologies.odbms.enterprise.comm;

import java.util.Date;
import com.orientechnologies.jdo.oPersistenceManager;

public class ClientSession {

    public ClientSession(oPersistenceManager iManager, int iId) {
        id = iId;
        manager = iManager;
        createdOn = new Date();
    }

    public void close() {
        if (manager != null) manager.close();
    }

    public int getId() {
        return id;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public long getIdle() {
        return System.currentTimeMillis() - createdOn.getTime();
    }

    public oPersistenceManager getManager() {
        return manager;
    }

    public void setManager(oPersistenceManager manager) {
        this.manager = manager;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    private Date createdOn;

    private oPersistenceManager manager;
}
