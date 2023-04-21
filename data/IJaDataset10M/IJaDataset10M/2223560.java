package com.entelience.objects.net;

import java.util.Date;

/**
 * Bean for net severity (see net.t_severity)
 */
public class NetAction implements java.io.Serializable {

    private Integer actionId;

    private String actionName;

    private Boolean blocking;

    private Date creationDate;

    public NetAction() {
    }

    public void setActionId(Integer _value) {
        actionId = _value;
    }

    public Integer getActionId() {
        return actionId;
    }

    public void setActionName(String _value) {
        actionName = _value;
    }

    public String getActionName() {
        return actionName;
    }

    public void setBlocking(Boolean _value) {
        blocking = _value;
    }

    public Boolean isBlocking() {
        return blocking;
    }

    public Boolean getBlocking() {
        return blocking;
    }

    public void setCreationDate(Date _value) {
        creationDate = _value;
    }

    public Date getCreationDate() {
        return creationDate;
    }
}
