package com.flaptor.clusterfest.exceptions;

import com.flaptor.clusterfest.NodeDescriptor;

public class NodeException extends Exception {

    NodeDescriptor node;

    private static final long serialVersionUID = 1L;

    public NodeException(NodeDescriptor node) {
        super();
        this.node = node;
    }

    public NodeException(NodeDescriptor node, String message) {
        super(message);
    }

    public NodeException(NodeDescriptor node, String message, Throwable cause) {
        super(message, cause);
    }

    public NodeException(NodeDescriptor node, Throwable cause) {
        super(cause);
    }
}
