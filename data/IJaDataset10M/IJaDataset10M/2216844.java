package net.sourceforge.nrl.parser.ast.action.impl;

import net.sourceforge.nrl.parser.ast.IModelReference;
import net.sourceforge.nrl.parser.ast.INRLAstVisitor;
import net.sourceforge.nrl.parser.ast.action.IRemoveFromCollectionAction;
import org.antlr.runtime.Token;

public class RemoveFromCollectionActionImpl extends ActionImpl implements IRemoveFromCollectionAction {

    public RemoveFromCollectionActionImpl() {
    }

    public RemoveFromCollectionActionImpl(Token token) {
        super(token);
    }

    public void accept(INRLAstVisitor visitor) {
        if (visitor.visitBefore(this)) {
            getElement().accept(visitor);
            getFrom().accept(visitor);
        }
        visitor.visitAfter(this);
    }

    public String dump(int indent) {
        String result = doIndent(indent) + "Remove " + getElement().dump(0) + " from " + getFrom().dump(0);
        return result;
    }

    public IModelReference getElement() {
        return (IModelReference) getChild(0);
    }

    public IModelReference getFrom() {
        return (IModelReference) getChild(1);
    }
}
