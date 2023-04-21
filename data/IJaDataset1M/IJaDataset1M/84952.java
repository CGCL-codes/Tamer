package es.nom.morenojuarez.modulipse.core.lang.ast;

import antlr.collections.AST;
import es.nom.morenojuarez.modulipse.core.lang.Modula2AST;
import es.nom.morenojuarez.modulipse.core.lang.Modula2TokenTypes;

/**
 * 
 */
public class DotSelector implements Selector, Node {

    private String ident;

    private AST lastASTNode;

    public static DotSelector create(Modula2AST ast) {
        if (ast != null && ast.getType() == Modula2TokenTypes.PERIOD) {
            DotSelector node = new DotSelector();
            node.ident = ast.getFirstChild().getText();
            node.lastASTNode = ast.getFirstChild();
            return node;
        }
        return null;
    }

    /**
	 * @return the ident
	 */
    public String getIdent() {
        return ident;
    }

    public AST getLastASTNode() {
        return lastASTNode;
    }

    public String toFormattedString() {
        return "." + getIdent();
    }
}
