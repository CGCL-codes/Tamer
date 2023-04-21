package com.liferay.portlet.workflow.model;

import java.io.Serializable;
import java.util.List;

/**
 * <a href="WorkflowToken.java.html"><b><i>View Source</i></b></a>
 *
 * @author Charles May
 *
 */
public class WorkflowToken implements Serializable {

    public WorkflowToken() {
    }

    public long getTokenId() {
        return _tokenId;
    }

    public void setTokenId(long tokenId) {
        _tokenId = tokenId;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getType() {
        return _type;
    }

    public void setType(String type) {
        _type = type;
    }

    public List getTasks() {
        return _tasks;
    }

    public void setTasks(List tasks) {
        _tasks = tasks;
    }

    public List getChildren() {
        return _children;
    }

    public void setChildren(List children) {
        _children = children;
    }

    private long _tokenId;

    private String _name;

    private String _type;

    private List _tasks;

    private List _children;
}
