package org.openscience.cdk.smiles.smarts.parser;

/**
 * An AST node. It represents the atomic mass (<n>) in smarts
 *
 * @author Dazhi Jiao
 * @cdk.created 2007-04-24
 * @cdk.module smarts
 * @cdk.githash
 * @cdk.keyword SMARTS
 */
public class ASTAtomicMass extends SimpleNode {

    /**
	 * The atomic mass value
	 */
    private int mass;

    public ASTAtomicMass(int id) {
        super(id);
    }

    public ASTAtomicMass(SMARTSParser p, int id) {
        super(p, id);
    }

    /**
     * Returns the mass value.
     */
    public int getMass() {
        return mass;
    }

    public Object jjtAccept(SMARTSParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    /**
     * Sets the mass value.
     * 
     * @param mass new mass value
     */
    public void setMass(int mass) {
        this.mass = mass;
    }
}
