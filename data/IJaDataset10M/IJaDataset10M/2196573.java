package org.eclipse.epsilon.dom.eol.builder;

import org.eclipse.epsilon.commons.parse.AST;
import org.eclipse.epsilon.dom.eol.DomElement;
import org.eclipse.epsilon.dom.eol.EolFactory;
import org.eclipse.epsilon.dom.eol.Expression;
import org.eclipse.epsilon.dom.eol.ReturnStatement;

public class ReturnStatementBuilder extends DomElementBuilder {

    @Override
    public DomElement build(AST ast) {
        ReturnStatement statement = EolFactory.eINSTANCE.createReturnStatement();
        if (ast.getChildCount() == 1) {
            statement.setReturned((Expression) controller.build(ast.getChild(0)));
        }
        return statement;
    }
}
