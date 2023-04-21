package com.windsor.node.common.domain;

import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class ComplexNotification implements Serializable {

    private static final long serialVersionUID = 1;

    private String uri;

    private List notifications;

    private String flowName;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public List getNotifications() {
        return notifications;
    }

    public void setNotifications(List notifications) {
        this.notifications = notifications;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((flowName == null) ? 0 : flowName.hashCode());
        result = prime * result + ((notifications == null) ? 0 : notifications.hashCode());
        result = prime * result + ((uri == null) ? 0 : uri.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final ComplexNotification other = (ComplexNotification) obj;
        if (flowName == null) {
            if (other.flowName != null) return false;
        } else if (!flowName.equals(other.flowName)) return false;
        if (notifications == null) {
            if (other.notifications != null) return false;
        } else if (!notifications.equals(other.notifications)) return false;
        if (uri == null) {
            if (other.uri != null) return false;
        } else if (!uri.equals(other.uri)) return false;
        return true;
    }

    public String toString() {
        ReflectionToStringBuilder rtsb = new ReflectionToStringBuilder(this, new DomainStringStyle());
        rtsb.setAppendStatics(false);
        rtsb.setAppendTransients(false);
        return rtsb.toString();
    }
}
