package com.google.gwt.dev.jjs.impl;

import com.google.gwt.dev.jjs.SourceInfo;
import com.google.gwt.dev.jjs.ast.Context;
import com.google.gwt.dev.jjs.ast.JArrayType;
import com.google.gwt.dev.jjs.ast.JCastOperation;
import com.google.gwt.dev.jjs.ast.JClassLiteral;
import com.google.gwt.dev.jjs.ast.JClassType;
import com.google.gwt.dev.jjs.ast.JConditional;
import com.google.gwt.dev.jjs.ast.JExpression;
import com.google.gwt.dev.jjs.ast.JField;
import com.google.gwt.dev.jjs.ast.JInstanceOf;
import com.google.gwt.dev.jjs.ast.JInterfaceType;
import com.google.gwt.dev.jjs.ast.JLocal;
import com.google.gwt.dev.jjs.ast.JLocalRef;
import com.google.gwt.dev.jjs.ast.JMethod;
import com.google.gwt.dev.jjs.ast.JMethodBody;
import com.google.gwt.dev.jjs.ast.JMethodCall;
import com.google.gwt.dev.jjs.ast.JModVisitor;
import com.google.gwt.dev.jjs.ast.JNewArray;
import com.google.gwt.dev.jjs.ast.JParameter;
import com.google.gwt.dev.jjs.ast.JProgram;
import com.google.gwt.dev.jjs.ast.JReferenceType;
import com.google.gwt.dev.jjs.ast.JType;
import com.google.gwt.dev.jjs.ast.JVisitor;
import com.google.gwt.dev.jjs.ast.js.JMultiExpression;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * Replace references to JSO subtypes with JSO itself.
 */
public class JavaScriptObjectNormalizer {

    private class Collector extends JVisitor {

        @Override
        public void endVisit(JClassType x, Context ctx) {
            if (program.isJavaScriptObject(x)) {
                for (JInterfaceType intr : x.implments) {
                    if (isTagInterface(intr) && !jsoType.implments.contains(intr)) {
                        jsoType.implments.add(intr);
                    }
                }
            }
        }
    }

    /**
   * Map types from JSO subtypes to JSO itself.
   */
    private class NormalizeVisitor extends JModVisitor {

        private final Stack<JMethodBody> currentMethodBody = new Stack<JMethodBody>();

        @Override
        public void endVisit(JCastOperation x, Context ctx) {
            JType newType = translate(x.getCastType());
            if (newType != x.getCastType()) {
                ctx.replaceMe(new JCastOperation(program, x.getSourceInfo(), newType, x.getExpr()));
            }
        }

        @Override
        public void endVisit(JClassLiteral x, Context ctx) {
            JType newType = translate(x.getRefType());
            if (newType != x.getRefType()) {
                ctx.replaceMe(program.getLiteralClass(newType));
            }
        }

        @Override
        public void endVisit(JField x, Context ctx) {
            x.setType(translate(x.getType()));
        }

        @Override
        public void endVisit(JInstanceOf x, Context ctx) {
            JReferenceType newType = (JReferenceType) translate(x.getTestType());
            if (newType != x.getTestType()) {
                ctx.replaceMe(new JInstanceOf(program, x.getSourceInfo(), newType, x.getExpr()));
            }
        }

        @Override
        public void endVisit(JLocal x, Context ctx) {
            x.setType(translate(x.getType()));
        }

        @Override
        public void endVisit(JMethod x, Context ctx) {
            x.setType(translate(x.getType()));
        }

        @Override
        public void endVisit(JMethodBody x, Context ctx) {
            if (currentMethodBody.pop() != x) {
                throw new RuntimeException("Unexpected JMethodBody popped");
            }
        }

        /**
     * Polymorphic dispatches to interfaces implemented by both a JSO and a
     * regular type require special dispatch handling. If the instance is not a
     * Java-derived object, the method from the single JSO implementation will
     * be invoked. Otherwise, a polymorphic dispatch to the Java-derived object
     * is made.
     */
        @Override
        public void endVisit(JMethodCall x, Context ctx) {
            JType targetClass = x.getTarget().getEnclosingType();
            if (jsoSingleImpls.containsKey(targetClass)) {
                SourceInfo info = x.getSourceInfo().makeChild(JavaScriptObjectNormalizer.class, "Polymorphic invocation of SingleJsoImpl interface");
                JMethod jsoMethod = findJsoMethod(x.getTarget());
                assert jsoMethod != null;
                if (dualImpl.contains(targetClass)) {
                    JMultiExpression multi = new JMultiExpression(program, info);
                    JExpression instance = maybeMakeTempAssignment(multi, x.getInstance());
                    JMethodCall localCall = new JMethodCall(program, info, instance, x.getTarget());
                    localCall.getArgs().addAll(x.getArgs());
                    CloneExpressionVisitor cloner = new CloneExpressionVisitor(program);
                    JMethodCall jsoCall = new JMethodCall(program, info, cloner.cloneExpression(instance), jsoMethod);
                    jsoCall.getArgs().addAll(cloner.cloneExpressions(x.getArgs()));
                    JConditional newExpr = makeIsJsoConditional(info, cloner.cloneExpression(instance), x.getType(), jsoCall, localCall);
                    multi.exprs.add(newExpr);
                    ctx.replaceMe(multi.exprs.size() == 1 ? multi.exprs.get(0) : multi);
                } else {
                    JMethodCall jsoCall = new JMethodCall(program, info, x.getInstance(), jsoMethod);
                    jsoCall.getArgs().addAll(x.getArgs());
                    ctx.replaceMe(jsoCall);
                }
            }
        }

        @Override
        public void endVisit(JNewArray x, Context ctx) {
            x.setType(translate(x.getType()));
        }

        @Override
        public void endVisit(JParameter x, Context ctx) {
            x.setType(translate(x.getType()));
        }

        @Override
        public boolean visit(JMethodBody x, Context ctx) {
            currentMethodBody.push(x);
            return true;
        }

        private JMethod findJsoMethod(JMethod interfaceMethod) {
            JClassType jsoClass = jsoSingleImpls.get(interfaceMethod.getEnclosingType());
            assert program.isJavaScriptObject(jsoClass);
            assert jsoClass != null;
            JMethod toReturn = program.typeOracle.findConcreteImplementation(interfaceMethod, jsoClass);
            assert toReturn != null;
            assert !toReturn.isAbstract();
            assert jsoClass.isFinal() || toReturn.isFinal();
            return toReturn;
        }

        private JConditional makeIsJsoConditional(SourceInfo info, JExpression instance, JType conditionalType, JExpression isJsoExpr, JExpression notJsoExpr) {
            JMethod isJavaScriptObjectMethod = program.getIndexedMethod("Cast.isJavaScriptObjectOrString");
            JMethodCall isJavaScriptObjectExpr = new JMethodCall(program, info, null, isJavaScriptObjectMethod);
            isJavaScriptObjectExpr.getArgs().add(instance);
            return new JConditional(program, info, conditionalType, isJavaScriptObjectExpr, isJsoExpr, notJsoExpr);
        }

        private JExpression maybeMakeTempAssignment(JMultiExpression multi, JExpression instance) {
            if (instance.hasSideEffects()) {
                SourceInfo info = instance.getSourceInfo().makeChild(JavaScriptObjectNormalizer.class, "Temporary assignment for instance with side-effects");
                JLocal local = program.createLocal(info, "maybeJsoInvocation".toCharArray(), instance.getType(), true, currentMethodBody.peek());
                multi.exprs.add(program.createAssignmentStmt(info, new JLocalRef(program, info, local), instance).getExpr());
                instance = new JLocalRef(program, info, local);
            }
            return instance;
        }

        private JType translate(JType type) {
            if (program.isJavaScriptObject(type)) {
                return program.getJavaScriptObject();
            } else if (jsoSingleImpls.containsKey(type) && !dualImpl.contains(type)) {
                return program.getJavaScriptObject();
            } else if (type instanceof JArrayType) {
                JArrayType arrayType = (JArrayType) type;
                JType leafType = arrayType.getLeafType();
                JType replacement = translate(leafType);
                if (leafType != replacement) {
                    return program.getTypeArray(replacement, arrayType.getDims());
                }
            }
            return type;
        }
    }

    public static void exec(JProgram program) {
        new JavaScriptObjectNormalizer(program).execImpl();
    }

    /**
   * Interfaces implemented both by a JSO type and a regular Java type.
   */
    private final Set<JInterfaceType> dualImpl;

    /**
   * Maps SingleJsoImpl interfaces onto the single JSO implementation.
   */
    private final Map<JInterfaceType, JClassType> jsoSingleImpls;

    private final JClassType jsoType;

    private final JProgram program;

    private JavaScriptObjectNormalizer(JProgram program) {
        this.program = program;
        dualImpl = program.typeOracle.getInterfacesWithJavaAndJsoImpls();
        jsoSingleImpls = program.typeOracle.getSingleJsoImpls();
        jsoType = program.getJavaScriptObject();
        jsoType.implments.addAll(dualImpl);
    }

    private void execImpl() {
        new Collector().accept(program);
        NormalizeVisitor visitor = new NormalizeVisitor();
        visitor.accept(program);
    }

    private boolean isTagInterface(JReferenceType intr) {
        if (!(intr instanceof JInterfaceType)) {
            return false;
        }
        if (intr.methods.size() > 1) {
            assert intr.methods.size() == 0 || intr.methods.get(0).getName().equals("$clinit");
            return false;
        }
        for (JInterfaceType t : intr.implments) {
            if (!isTagInterface(t)) {
                return false;
            }
        }
        return true;
    }
}
