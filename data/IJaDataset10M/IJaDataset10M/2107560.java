package org.eclipse.epsilon.dom.eol.builder;

import org.eclipse.epsilon.commons.parse.AST;
import org.eclipse.epsilon.dom.eol.DomElement;
import org.eclipse.epsilon.dom.eol.EolFactory;

public class ContinueStatementBuilder extends DomElementBuilder {

    @Override
    public DomElement build(AST ast) {
        return EolFactory.eINSTANCE.createContinueStatement();
    }
}
