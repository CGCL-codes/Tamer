package saf.ast.nodes;

import saf.ast.Visitor;

public class And extends ASTNode {

    private final String name = "and";

    public And() {
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String getName() {
        return name;
    }
}
