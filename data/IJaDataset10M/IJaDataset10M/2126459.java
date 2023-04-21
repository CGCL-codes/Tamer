package com.google.gwt.dev.jjs.impl;

import com.google.gwt.dev.jjs.SourceInfo;
import com.google.gwt.dev.jjs.SourceOrigin;
import com.google.gwt.dev.jjs.ast.AccessModifier;
import com.google.gwt.dev.jjs.ast.Context;
import com.google.gwt.dev.jjs.ast.JClassType;
import com.google.gwt.dev.jjs.ast.JConditional;
import com.google.gwt.dev.jjs.ast.JDeclaredType;
import com.google.gwt.dev.jjs.ast.JLocal;
import com.google.gwt.dev.jjs.ast.JLocalRef;
import com.google.gwt.dev.jjs.ast.JMethod;
import com.google.gwt.dev.jjs.ast.JMethodBody;
import com.google.gwt.dev.jjs.ast.JMethodCall;
import com.google.gwt.dev.jjs.ast.JModVisitor;
import com.google.gwt.dev.jjs.ast.JParameter;
import com.google.gwt.dev.jjs.ast.JParameterRef;
import com.google.gwt.dev.jjs.ast.JProgram;
import com.google.gwt.dev.jjs.ast.JReturnStatement;
import com.google.gwt.dev.jjs.ast.JType;
import com.google.gwt.dev.jjs.ast.JTypeOracle;
import com.google.gwt.dev.jjs.ast.js.JMultiExpression;
import com.google.gwt.dev.jjs.impl.MakeCallsStatic.CreateStaticImplsVisitor;
import java.util.HashMap;
import java.util.Map;

/**
 * All call sites that might result in virtual dispatch to a JSO must be
 * rewritten to force static dispatch. This transform may NOT be run multiple
 * times; it will create ever-expanding replacement expressions.
 */
public class JsoDevirtualizer {

    /**
   * Rewrite any virtual dispatches to Object or JavaScriptObject such that
   * dispatch occurs statically for JSOs.
   */
    private class RewriteVirtualDispatches extends JModVisitor {

        /**
     * A method call at this point can be one of 5 things:
     * <ol>
     * <li>a dual dispatch interface</li>
     * <li>a single dispatch trough single-jso interface</li>
     * <li>a java.lang.Object override from JavaScriptObject</li>
     * <li>a regular dispatch (no JSOs involved or static JSO call)</li>
     * <li>in draftMode, a 'static' virtual JSO call that hasn't been made
     * static yet.</li>
     * </ol>
     */
        @Override
        public void endVisit(JMethodCall x, Context ctx) {
            JMethod method = x.getTarget();
            JMethod newMethod;
            JDeclaredType targetType = method.getEnclosingType();
            if (targetType == null) {
                return;
            }
            if (!method.needsVtable()) {
                return;
            }
            JType instanceType = x.getInstance().getType();
            if (instanceType != program.getTypeJavaLangObject() && !program.typeOracle.canBeJavaScriptObject(instanceType)) {
                return;
            }
            if (polyMethodToJsoMethod.containsKey(method)) {
                newMethod = polyMethodToJsoMethod.get(method);
            } else if (program.typeOracle.isDualJsoInterface(targetType)) {
                JMethod overridingMethod = findOverridingMethod(method, program.typeOracle.getSingleJsoImpl(targetType));
                assert overridingMethod != null;
                JMethod jsoStaticImpl = getStaticImpl(overridingMethod);
                newMethod = getOrCreateDevirtualMethod(x, jsoStaticImpl);
                polyMethodToJsoMethod.put(method, newMethod);
            } else if (program.isJavaScriptObject(targetType)) {
                newMethod = getStaticImpl(method);
                polyMethodToJsoMethod.put(method, newMethod);
            } else if (program.typeOracle.isSingleJsoImpl(targetType)) {
                JMethod overridingMethod = findOverridingMethod(method, program.typeOracle.getSingleJsoImpl(targetType));
                assert overridingMethod != null;
                newMethod = getStaticImpl(overridingMethod);
                polyMethodToJsoMethod.put(method, newMethod);
            } else if (targetType == program.getTypeJavaLangObject()) {
                JMethod overridingMethod = findOverridingMethod(method, program.getJavaScriptObject());
                if (overridingMethod != null) {
                    JMethod jsoStaticImpl = getStaticImpl(overridingMethod);
                    newMethod = getOrCreateDevirtualMethod(x, jsoStaticImpl);
                    polyMethodToJsoMethod.put(method, newMethod);
                } else {
                    assert false : "Object method not overriden by JavaScriptObject";
                    return;
                }
            } else {
                return;
            }
            assert (newMethod != null);
            ctx.replaceMe(MakeCallsStatic.makeStaticCall(x, newMethod));
        }

        @Override
        public boolean visit(JMethod x, Context ctx) {
            if (polyMethodToDevirtualMethods.containsValue(x)) {
                return false;
            }
            return true;
        }
    }

    public static void exec(JProgram program) {
        new JsoDevirtualizer(program).execImpl();
    }

    /**
   * Maps each Object instance methods (ie, {@link Object#equals(Object)}) onto
   * its corresponding devirtualizing method.
   */
    protected Map<JMethod, JMethod> polyMethodToJsoMethod = new HashMap<JMethod, JMethod>();

    /**
   * Contains the Cast.isJavaObject method.
   */
    private final JMethod isJavaObjectMethod;

    /**
   * Key is the method signature, value is the number of unique instances with
   * the same signature.
   */
    private Map<String, Integer> jsoMethodInstances = new HashMap<String, Integer>();

    /**
   * Contains the set of devirtualizing methods that replace polymorphic calls
   * to Object methods.
   */
    private final Map<JMethod, JMethod> polyMethodToDevirtualMethods = new HashMap<JMethod, JMethod>();

    private final JProgram program;

    private final CreateStaticImplsVisitor staticImplCreator;

    private JsoDevirtualizer(JProgram program) {
        this.program = program;
        this.isJavaObjectMethod = program.getIndexedMethod("Cast.isJavaObject");
        staticImplCreator = new CreateStaticImplsVisitor(program);
    }

    private void execImpl() {
        JClassType jsoType = program.getJavaScriptObject();
        if (jsoType == null) {
            return;
        }
        RewriteVirtualDispatches rewriter = new RewriteVirtualDispatches();
        rewriter.accept(program);
        assert (rewriter.didChange());
    }

    /**
   * Finds the method that overrides this method, starting with the target
   * class.
   */
    private JMethod findOverridingMethod(JMethod method, JClassType target) {
        if (target == null) {
            return null;
        }
        for (JMethod overridingMethod : target.getMethods()) {
            if (JTypeOracle.methodsDoMatch(method, overridingMethod)) {
                return overridingMethod;
            }
        }
        return findOverridingMethod(method, target.getSuperClass());
    }

    /**
   * Create a conditional method to discriminate between static and virtual
   * dispatch.
   * 
   * <pre>
    * static boolean equals__devirtual$(Object this, Object other) {
    *   return Cast.isJavaObject(this) ? this.equals(other) : JavaScriptObject.equals$(this, other);
    * }
    * </pre>
   */
    private JMethod getOrCreateDevirtualMethod(JMethodCall polyMethodCall, JMethod jsoImpl) {
        JMethod polyMethod = polyMethodCall.getTarget();
        if (polyMethodToDevirtualMethods.containsKey(polyMethod)) {
            return polyMethodToDevirtualMethods.get(polyMethod);
        }
        JClassType jsoType = program.getJavaScriptObject();
        SourceInfo sourceInfo = jsoType.getSourceInfo().makeChild(SourceOrigin.UNKNOWN);
        String prefix;
        Integer methodCount;
        methodCount = jsoMethodInstances.get(polyMethod.getSignature());
        if (methodCount == null) {
            prefix = polyMethod.getName();
            methodCount = 0;
        } else {
            prefix = polyMethod.getName() + methodCount;
            methodCount++;
        }
        jsoMethodInstances.put(polyMethod.getSignature(), methodCount);
        String devirtualName = prefix + "__devirtual$";
        JMethod newMethod = new JMethod(sourceInfo, devirtualName, jsoType, polyMethod.getType(), false, true, true, AccessModifier.PUBLIC);
        newMethod.setBody(new JMethodBody(sourceInfo));
        jsoType.addMethod(newMethod);
        newMethod.setSynthetic();
        JParameter thisParam = JProgram.createParameter(sourceInfo, "this$static", program.getTypeJavaLangObject(), true, true, newMethod);
        for (JParameter oldParam : polyMethod.getParams()) {
            JProgram.createParameter(sourceInfo, oldParam.getName(), oldParam.getType(), true, false, newMethod);
        }
        newMethod.freezeParamTypes();
        newMethod.addThrownExceptions(polyMethod.getThrownExceptions());
        sourceInfo.addCorrelation(sourceInfo.getCorrelator().by(newMethod));
        JLocal temp = JProgram.createLocal(sourceInfo, "maybeJsoInvocation", thisParam.getType(), true, (JMethodBody) newMethod.getBody());
        JMultiExpression multi = new JMultiExpression(sourceInfo);
        multi.exprs.add(JProgram.createAssignmentStmt(sourceInfo, new JLocalRef(sourceInfo, temp), new JParameterRef(sourceInfo, thisParam)).getExpr());
        JMethodCall condition = new JMethodCall(sourceInfo, null, isJavaObjectMethod);
        condition.addArg(new JLocalRef(sourceInfo, temp));
        JMethodCall thenValue = new JMethodCall(sourceInfo, new JLocalRef(sourceInfo, temp), polyMethod);
        for (JParameter param : newMethod.getParams()) {
            if (param != thisParam) {
                thenValue.addArg(new JParameterRef(sourceInfo, param));
            }
        }
        JMethodCall elseValue = new JMethodCall(sourceInfo, null, jsoImpl);
        elseValue.addArg(new JLocalRef(sourceInfo, temp));
        for (JParameter param : newMethod.getParams()) {
            if (param != thisParam) {
                elseValue.addArg(new JParameterRef(sourceInfo, param));
            }
        }
        JConditional conditional = new JConditional(sourceInfo, polyMethod.getType(), condition, thenValue, elseValue);
        multi.exprs.add(conditional);
        JReturnStatement returnStatement = new JReturnStatement(sourceInfo, multi);
        ((JMethodBody) newMethod.getBody()).getBlock().addStmt(returnStatement);
        polyMethodToDevirtualMethods.put(polyMethod, newMethod);
        return newMethod;
    }

    private JMethod getStaticImpl(JMethod method) {
        assert !method.isStatic();
        JMethod staticImpl = program.getStaticImpl(method);
        if (staticImpl == null) {
            staticImplCreator.accept(method);
            staticImpl = program.getStaticImpl(method);
        }
        return staticImpl;
    }
}
