package com.reserveamerica.jirarmi.beans.issue;

import java.io.Serializable;
import com.reserveamerica.jirarmi.beans.JiraRemoteBean;

/**
 * @author BStasyszyn
 */
public class FieldRemote implements JiraRemoteBean, Serializable {

    private static final long serialVersionUID = -3631789061455058590L;

    private final String id;

    private String key;

    private String name;

    public FieldRemote(String id) {
        this.id = id;
    }

    public FieldRemote(String id, String key, String name) {
        this(id);
        this.key = key;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append("id: " + id);
        sb.append(", key: " + key);
        sb.append(", name: " + name);
        sb.append("]");
        return sb.toString();
    }
}
