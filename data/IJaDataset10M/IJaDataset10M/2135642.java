package org.spockframework.compiler.model;

import org.codehaus.groovy.ast.MethodNode;

/**
 * AST node representing a fixture method. In source code, a fixture method
 * corresponds to an instance method named one of "setup", "cleanup",
 * "setupSpec", "cleanupSpec".
 * 
 * @author Peter Niederwieser
 */
public class FixtureMethod extends Method {

    public FixtureMethod(Spec parent, MethodNode code) {
        super(parent, code);
    }
}
