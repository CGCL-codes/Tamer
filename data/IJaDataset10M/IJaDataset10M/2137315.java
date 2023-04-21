package com.google.gwt.dev.jjs.ast;

/**
 * Interface implemented by anything that can be enclosed by a type. 
 */
public interface HasEnclosingType {

    JDeclaredType getEnclosingType();
}
