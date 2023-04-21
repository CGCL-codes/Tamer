package org.henkels.drawcode.editors.nsdiagram.model.nodes;

import java.util.List;

public class IfCommand extends BaseElementsTokenizer implements ICommandVisitable {

    public String condition;

    public IfCommands ifCommands = new IfCommands();

    public ElseCommands elseCommands = new ElseCommands();

    public IfCommand() {
        super();
        condition = "if condition";
    }

    @Override
    public String toString() {
        return condition;
    }

    @Override
    public void setValue(String newValue) {
        this.condition = newValue;
    }

    @Override
    public boolean accept(ICommandVisitor visitor, Object ctx) {
        if (!visitor.Visit(this, ctx)) {
            return false;
        }
        return ifCommands.accept(visitor, ctx) && elseCommands.accept(visitor, ctx);
    }

    @Override
    public void vlaccept(ICommandVisitor visitor, Object ctx) {
        accept(visitor, ctx);
    }

    @Override
    public List<String> getTokenList() {
        return super.getElementTokenList(condition);
    }
}
