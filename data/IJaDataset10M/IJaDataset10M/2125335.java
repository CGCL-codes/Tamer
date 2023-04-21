package br.ufal.ic.ptl.abstractsyntaxtrees;

import br.ufal.ic.ptl.abstractsyntaxtrees.visitors.Visitor;
import br.ufal.ic.ptl.syntaticanalyzer.lexer.SourcePosition;

public class VarDeclaration extends Declaration {

    private Identifier identifierAST;

    private TypeDenoter typeDenoterAST;

    public VarDeclaration(Identifier identifierAST, TypeDenoter typeDenoterAST, SourcePosition thePosition) {
        super(thePosition);
        this.identifierAST = identifierAST;
        this.typeDenoterAST = typeDenoterAST;
    }

    public Object visit(Visitor v, Object o) {
        return v.visitVarDeclaration(this, o);
    }

    /**
	 * @return the identifierAST
	 */
    public Identifier getIdentifierAST() {
        return identifierAST;
    }

    /**
	 * @param identifierAST
	 *            the identifierAST to set
	 */
    public void setIdentifierAST(Identifier identifierAST) {
        this.identifierAST = identifierAST;
    }

    /**
	 * @return the typeDenoterAST
	 */
    public TypeDenoter getTypeDenoterAST() {
        return typeDenoterAST;
    }

    /**
	 * @param typeDenoterAST
	 *            the typeDenoterAST to set
	 */
    public void setTypeDenoterAST(TypeDenoter typeDenoterAST) {
        this.typeDenoterAST = typeDenoterAST;
    }
}
