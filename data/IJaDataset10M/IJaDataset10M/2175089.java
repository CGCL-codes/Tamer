package org.xaware.ide.xadev.processview.model;

import org.eclipse.draw2d.geometry.Dimension;

/**
 * Model for fork/join node
 */
public class ForkJoinModel extends ComponentModel {

    /** ID required to serialize the class */
    private static final long serialVersionUID = 1;

    public static final int FORK_NODE_WIDTH = 11;

    public static final int FORK_NODE_HEIGHT = 42;

    private String type;

    public static final String FORK_NODE = "ForkNode";

    public static final String JOIN_NODE = "JoinNode";

    public ForkJoinModel(final String type) {
        size = new Dimension(FORK_NODE_WIDTH, FORK_NODE_HEIGHT);
        this.type = type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
