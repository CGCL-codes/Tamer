package org.ajcontract.annotationprocessor.ast;

import javax.lang.model.element.ExecutableElement;

/**
 * Node that represents a method.
 * 
 * @author Arcadio Rubio Garc�a
 */
public class Method extends Executable<Type<?>> {

    public Method(ExecutableElement element, Type<?> enclosingType) {
        super(element, enclosingType);
    }
}
