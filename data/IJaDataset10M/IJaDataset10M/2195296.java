package com.triplea.rolap.plugins.filter.rules.parser;

public class ASTCellReference extends SimpleNode {

    public ASTCellReference(int id) {
        super(id);
    }

    public ASTCellReference(RuleParser p, int id) {
        super(p, id);
    }
}
