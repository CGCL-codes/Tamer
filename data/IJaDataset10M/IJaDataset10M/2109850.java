package org.deveel.csharpcc.cstree;

public class ASTBNFLookahead extends SimpleNode {

    public ASTBNFLookahead(int id) {
        super(id);
    }

    public ASTBNFLookahead(CSTreeParser p, int id) {
        super(p, id);
    }
}
