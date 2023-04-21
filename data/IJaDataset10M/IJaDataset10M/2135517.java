package org.henkels.drawcode.editors.nsdiagram.model.nodes;

import java.util.List;

public class InstructionCommand extends BaseElementsTokenizer implements ICommandVisitable {

    public String method;

    @Override
    public boolean accept(ICommandVisitor visitor, Object ctx) {
        return visitor.Visit(this, ctx);
    }

    @Override
    public String toString() {
        return method;
    }

    public InstructionCommand() {
        super();
        method = "new instruction";
    }

    @Override
    public void setValue(String newValue) {
        this.method = newValue;
    }

    @Override
    public void vlaccept(ICommandVisitor visitor, Object ctx) {
        accept(visitor, ctx);
    }

    @Override
    public List<String> getTokenList() {
        return super.getElementTokenList(method);
    }
}
