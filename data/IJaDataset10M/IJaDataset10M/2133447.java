package org.spockframework.compiler.model;

import org.codehaus.groovy.ast.MethodNode;

/**
 *
 * @author Peter Niederwieser
 */
public class HelperMethod extends Method {

    public HelperMethod(Spec parent, MethodNode code) {
        super(parent, code);
    }
}
