package com.google.gwt.core.ext.soyc.impl;

import com.google.gwt.core.ext.soyc.FunctionMember;
import com.google.gwt.dev.js.ast.JsFunction;

/**
 * An implementation of FunctionMember.
 */
public class StandardFunctionMember extends AbstractMemberWithDependencies implements FunctionMember {

    private final String sourceName;

    /**
   * Constructed by {@link MemberFactory#get(JsFunction)}.
   */
    public StandardFunctionMember(MemberFactory factory, JsFunction function) {
        super(function.getSourceInfo());
        this.sourceName = function.getName().getIdent();
    }

    @Override
    public String getSourceName() {
        return sourceName;
    }

    /**
   * For debugging use only.
   */
    @Override
    public String toString() {
        return "FunctionMember " + sourceName;
    }
}
