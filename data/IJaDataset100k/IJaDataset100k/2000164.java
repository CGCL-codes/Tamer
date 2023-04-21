package com.wpjr.simulator.entity.action;

import com.wpjr.simulator.entity.node.Node;

public class TurnOnAction extends Action {

    private Node node;

    public TurnOnAction(Node node) {
        super(0);
        this.node = node;
    }

    public void execute() throws Throwable {
        node.turnOn();
    }
}
