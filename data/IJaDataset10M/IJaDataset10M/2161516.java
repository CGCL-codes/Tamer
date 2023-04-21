package com.blah.gwtgames.client.common.dlx;

class Node {

    Node left = this;

    Node right = this;

    Node up = this;

    Node down = this;

    Constraint constraint = null;

    Choice choice = null;

    Node() {
    }

    Node(Constraint constraint) {
        this.constraint = constraint;
    }

    Node(Constraint constraint, Choice choice) {
        this.constraint = constraint;
        this.choice = choice;
    }
}
