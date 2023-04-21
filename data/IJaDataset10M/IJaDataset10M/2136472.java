package pl.omtt.lang.model.ast;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.Tree;
import pl.omtt.lang.analyze.ISymbolTableParticipant;
import pl.omtt.lang.analyze.SymbolTable;
import pl.omtt.lang.model.IVisitable;
import pl.omtt.lang.model.IVisitor;
import pl.omtt.lang.model.types.TypeException;

public class ImportDeclaration extends CommonNode implements IVisitable, ISymbolTableParticipant {

    public ImportDeclaration(int token_type, Token token) {
        super(token);
    }

    public String getUseId() {
        StringBuffer buf = new StringBuffer();
        Tree idnode = getChild(0);
        for (int i = 0; i < idnode.getChildCount(); i++) {
            if (i > 0) buf.append(".");
            buf.append(idnode.getChild(i).toString());
        }
        return buf.toString();
    }

    public String getTargetNs() {
        if (getChildCount() > 1) return getChild(1).getText(); else return null;
    }

    @Override
    public void takeSymbolTable(SymbolTable symbolTable) throws TypeException {
        try {
            symbolTable.getBase().importLibrary(getUseId(), getTargetNs());
        } catch (TypeException e) {
            e.setCauseObject(this);
            throw e;
        }
    }

    public String toString() {
        return "use " + getUseId();
    }

    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }
}
