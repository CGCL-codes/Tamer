package com.koylu.caffein.model.clazz;

import java.util.Collection;

public interface Node {

    public Node getParent();

    public void setParent(Node parent);

    public Collection<? extends Node> getChildren();
}
