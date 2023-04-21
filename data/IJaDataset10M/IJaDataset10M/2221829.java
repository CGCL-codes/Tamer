package com.google.gwt.rpc.client.ast;

import com.google.gwt.rpc.client.ast.RpcCommandVisitor.Context;

/**
 * Encapsulates a boolean value in the command stream.
 */
public class CharValueCommand extends ScalarValueCommand {

    private final char value;

    public CharValueCommand(double value) {
        this.value = (char) value;
    }

    public CharValueCommand(char value) {
        this.value = value;
    }

    public CharValueCommand(Character value) {
        this.value = value;
    }

    @Override
    public Character getValue() {
        return value;
    }

    @Override
    public void traverse(RpcCommandVisitor visitor, Context ctx) {
        visitor.visit(this, ctx);
        visitor.endVisit(this, ctx);
    }
}
