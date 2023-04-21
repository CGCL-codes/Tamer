package org.slasoi.gslam.monitoring.parser.core;

public class ASTFunctionParameter extends SimpleNode {

    public ASTFunctionParameter(int id) {
        super(id);
    }

    public ASTFunctionParameter(UQLParser p, int id) {
        super(p, id);
    }
}
