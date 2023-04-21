package com.tensegrity.webetlclient.modules.core.client.model;

import java.util.ArrayList;
import java.util.List;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.tensegrity.palowebviewer.modules.util.client.Arrays;
import com.tensegrity.palowebviewer.modules.util.client.Assertions;
import com.tensegrity.palowebviewer.modules.util.client.ImmutableList;

public abstract class ModelNode implements IModelNode, IETLDOMConstants, IModelConstants, IsSerializable {

    private String name;

    private List baseChildren = null;

    private transient ImmutableList children = null;

    private transient IModelNode parent;

    private String comment;

    protected final transient ModelNodeListenerCollection listeners = new ModelNodeListenerCollection();

    public void addListener(IModelNodeListener listener) {
        listeners.addListener(listener);
    }

    public void removeListener(IModelNodeListener listener) {
        listeners.removeListener(listener);
    }

    public List getChildren() {
        if (children == null) {
            children = new ImmutableList(getBaseChildren());
        }
        return children;
    }

    protected List getBaseChildren() {
        if (baseChildren == null) {
            baseChildren = new ArrayList();
        }
        return baseChildren;
    }

    public int getChildCount() {
        int result = getChildren().size();
        return result;
    }

    public IModelNode getChild(int index) {
        return (IModelNode) getChildren().get(index);
    }

    public void addChild(IModelNode node) {
        insertChild(getChildCount(), node);
    }

    public int indexOf(IModelNode child) {
        return getChildren().indexOf(child);
    }

    public void setParent(IModelNode node) {
        if (parent != node) {
            parent = node;
        }
    }

    public void removeChild(IModelNode node) {
        if (children != null) {
            int index = indexOf(node);
            if (index != -1) {
                getBaseChildren().remove(node);
                listeners.onChildRemoved(this, node, index);
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (!Arrays.equals(name, this.name)) {
            this.name = name;
            listeners.onNameChanged(this);
        }
    }

    public void setComment(String comment) {
        if (!Arrays.equals(name, this.name)) {
            this.comment = comment;
            listeners.onCommentChanged(this);
        }
    }

    public String getComment() {
        return comment;
    }

    public IModelNode getParent() {
        return parent;
    }

    public void insertChild(int index, IModelNode node) {
        Assertions.assertNotNull(node, "Child");
        getBaseChildren().add(index, node);
        node.setParent(this);
        fireChildInserted(index);
    }

    protected void fireChildInserted(int index) {
        listeners.onChildInserted(this, index);
    }

    protected void fireNodeChanged() {
        listeners.onNodeChanged(this);
    }

    public void removeFromParent() {
        IModelNode parent = getParent();
        if (parent != null) {
            parent.removeChild(this);
        }
    }
}
