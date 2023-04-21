package padmig.compiler;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import padmig.Migratory;
import padmig.Undock;
import padmig.lib.EmptyStackFrame;
import padmig.lib.SaveStack;
import padmig.lib.StackFrame;
import spoon.reflect.Factory;
import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.CtAssignment;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtCFlowBreak;
import spoon.reflect.code.CtCatch;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtFieldAccess;
import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtNewClass;
import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtSwitch;
import spoon.reflect.code.CtThrow;
import spoon.reflect.code.CtTry;
import spoon.reflect.code.CtUnaryOperator;
import spoon.reflect.code.CtVariableAccess;
import spoon.reflect.code.CtWhile;
import spoon.reflect.code.UnaryOperatorKind;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.reference.CtArrayTypeReference;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.reference.CtFieldReference;
import spoon.reflect.reference.CtTypeReference;

public class ProcessorUtils {

    public static final String[] disallowedVariableNames = new String[] { "__state", "__tmpState", "__parentState", "__entryPoint", "__stack", "__t", "__gen", "__cFlowBreakLevel", "__tryNestingDepth" };

    private static Set<CtExecutableReference<?>> translatedMigratoryMethods = new HashSet<CtExecutableReference<?>>();

    private static Set<CtExecutableReference<?>> translatedUndockMethods = new HashSet<CtExecutableReference<?>>();

    public static boolean isAppendedCodeReachable(CtBlock<?> b) {
        if (b.getStatements().isEmpty()) {
            return true;
        }
        return !(b.getStatements().get(b.getStatements().size() - 1) instanceof CtCFlowBreak);
    }

    public static boolean isAppendedCodeReachable(CtTry t) {
        if (t.getBody().getStatements().isEmpty()) {
            return true;
        }
        if (!(t.getBody().getStatements().get(t.getBody().getStatements().size() - 1) instanceof CtCFlowBreak)) {
            return true;
        }
        for (CtCatch c : t.getCatchers()) {
            if (c.getBody().getStatements().isEmpty()) {
                return true;
            }
            if (!(c.getBody().getStatements().get(c.getBody().getStatements().size() - 1) instanceof CtCFlowBreak)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isMultiEvalStable(CtExpression<?> e) {
        if (e instanceof CtVariableAccess || e instanceof CtLiteral) {
            return true;
        } else if (e instanceof CtBinaryOperator) {
            return isMultiEvalStable(((CtBinaryOperator<?>) e).getLeftHandOperand()) && isMultiEvalStable(((CtBinaryOperator<?>) e).getRightHandOperand());
        } else if (e instanceof CtUnaryOperator) {
            UnaryOperatorKind uok = ((CtUnaryOperator<?>) e).getKind();
            if (uok == UnaryOperatorKind.POS || uok == UnaryOperatorKind.NEG || uok == UnaryOperatorKind.NOT || uok == UnaryOperatorKind.COMPL) {
                return isMultiEvalStable(((CtUnaryOperator<?>) e).getOperand());
            }
        }
        return false;
    }

    public static void methodWillBeTranslated(CtExecutableReference<?> m, boolean undockMethod) {
        if (undockMethod) {
            translatedUndockMethods.add(m.getFactory().Core().clone(m));
        } else {
            translatedMigratoryMethods.add(m.getFactory().Core().clone(m));
        }
    }

    public static boolean isMethodMigratable(CtExecutableReference<?> m, boolean ignoreUndockables) {
        if (ignoreUndockables) {
            return m.getAnnotation(Migratory.class) != null || translatedMigratoryMethods.contains(m);
        } else {
            return m.getAnnotation(Migratory.class) != null || m.getAnnotation(Undock.class) != null || translatedMigratoryMethods.contains(m) || translatedUndockMethods.contains(m);
        }
    }

    public static <T> CtClass<StackFrame> createStackFrameClass(Factory f, CtMethod<T> m, CtClass<?> parent) {
        boolean isStatic = m.hasModifier(ModifierKind.STATIC);
        String stackFrameClassName = m.getSimpleName().substring(0, 1).toUpperCase() + m.getSimpleName().substring(1) + "StackFrame";
        boolean isOverloaded = false;
        List<CtTypeReference<?>> paramsWithStackframe = new LinkedList<CtTypeReference<?>>(m.getReference().getParameterTypes());
        paramsWithStackframe.add(f.Type().createReference(StackFrame.class));
        for (CtMethod<?> m2 : parent.getAllMethods()) {
            if (m2.getSimpleName().equals(m.getSimpleName()) && !m2.equals(m) && !m2.getReference().getParameterTypes().equals(paramsWithStackframe)) {
                isOverloaded = true;
                break;
            }
        }
        if (isOverloaded) {
            for (CtParameter<?> param : m.getParameters()) {
                stackFrameClassName += "_" + param.getType().getQualifiedName().replace('.', '_');
            }
        }
        CtClass<StackFrame> stackFrameClass = f.Class().create(parent, stackFrameClassName);
        Set<ModifierKind> publicMod = new HashSet<ModifierKind>();
        publicMod.add(ModifierKind.PUBLIC);
        if (isStatic) {
            Set<ModifierKind> publicStaticMod = new HashSet<ModifierKind>();
            publicStaticMod.add(ModifierKind.PUBLIC);
            publicStaticMod.add(ModifierKind.STATIC);
            stackFrameClass.setModifiers(publicStaticMod);
        } else {
            stackFrameClass.setModifiers(publicMod);
        }
        stackFrameClass.setSuperclass(f.Type().createReference(StackFrame.class));
        parent.getNestedTypes().add(stackFrameClass);
        Set<CtTypeReference<? extends Throwable>> contThrowables = new HashSet<CtTypeReference<? extends Throwable>>();
        contThrowables.add(f.Type().createReference(Exception.class));
        contThrowables.add(f.Type().createReference(SaveStack.class));
        CtBlock<?> contBody = f.Core().createBlock();
        CtNewClass<EmptyStackFrame> nc = f.Core().createNewClass();
        nc.setType(f.Type().createReference(EmptyStackFrame.class));
        @SuppressWarnings("unchecked") CtLocalVariable<StackFrame> frameVar = f.Code().createLocalVariable(f.Type().createReference(StackFrame.class), "frame", (CtNewClass) nc);
        contBody.insertEnd(frameVar);
        CtFieldAccess<StackFrame> fa = f.Core().createFieldAccess();
        fa.setVariable(f.Field().createReference(f.Type().createReference(StackFrame.class), f.Type().createReference(StackFrame.class), "child"));
        fa.setTarget(f.Code().createVariableAccess(frameVar.getReference(), false));
        CtAssignment<StackFrame, StackFrame> a = f.Core().createAssignment();
        a.setAssigned(fa);
        a.setAssignment(f.Code().createThisAccess(stackFrameClass.getReference()));
        contBody.insertEnd(a);
        CtInvocation<T> helperInvocation = f.Core().createInvocation();
        helperInvocation.setExecutable(m.getReference());
        List<CtExpression<?>> args = new LinkedList<CtExpression<?>>();
        for (CtParameter<?> param : m.getParameters()) {
            if (param.getType().isPrimitive()) {
                if (param.getType().getActualClass() == Boolean.TYPE) {
                    args.add(f.Code().createLiteral(false));
                } else {
                    args.add(f.Code().createLiteral(0));
                }
            } else {
                args.add(f.Code().createLiteral(null));
            }
        }
        args.add(f.Code().createVariableAccess(frameVar.getReference(), false));
        helperInvocation.setArguments(args);
        if (!isStatic) {
            CtFieldAccess<Serializable> fa2 = f.Core().createFieldAccess();
            CtFieldReference<Serializable> fr = f.Core().createFieldReference();
            fr.setSimpleName("self");
            fa2.setVariable(fr);
            List<CtTypeReference<?>> casts = new LinkedList<CtTypeReference<?>>();
            casts.add(parent.getReference());
            fa2.setTypeCasts(casts);
            helperInvocation.setTarget(fa2);
        }
        CtReturn<T> r = f.Core().createReturn();
        if (m.getType().getActualClass() == Void.TYPE) {
            contBody.insertEnd(helperInvocation);
            CtLiteral<T> l = f.Core().createLiteral();
            l.setValue(null);
            r.setReturnedExpression(l);
        } else {
            r.setReturnedExpression(helperInvocation);
        }
        contBody.insertEnd(r);
        f.Method().create(stackFrameClass, publicMod, f.Type().createReference(Object.class), "continueExecution", null, contThrowables, contBody);
        for (CtParameter<?> param : m.getParameters()) {
            f.Field().create(stackFrameClass, publicMod, param.getType(), param.getSimpleName());
        }
        return stackFrameClass;
    }

    public static void addLocalsToStackFrameClass(Factory f, CtClass<StackFrame> stackFrameClass, Unfolder unfolder) {
        Map<String, CtTypeReference<?>> localsToMigrate = unfolder.getLocals(true);
        Set<ModifierKind> publicMod = new HashSet<ModifierKind>();
        publicMod.add(ModifierKind.PUBLIC);
        for (String s : localsToMigrate.keySet()) {
            f.Field().create(stackFrameClass, publicMod, localsToMigrate.get(s), s);
        }
    }

    public static <T> CtBlock<T> createMigratoryMethodBody(Factory f, CtMethod<T> m, CtClass<Serializable> parent, CtParameter<StackFrame> stateParam, CtClass<StackFrame> stackFrameClass, Unfolder unfolder) {
        boolean isStatic = m.hasModifier(ModifierKind.STATIC);
        CtBlock<T> body = f.Core().createBlock();
        @SuppressWarnings("unchecked") CtLocalVariable<StackFrame> stateVar = f.Code().createLocalVariable(stackFrameClass.getReference(), "__state", (CtLiteral) f.Code().createLiteral(null));
        body.insertEnd(stateVar);
        body.insertEnd(f.Code().createLocalVariable(stackFrameClass.getReference(), "__tmpState", null));
        CtLocalVariable<Integer> entryPointVar = f.Code().createLocalVariable(f.Type().createReference(Integer.TYPE), "__entryPoint", f.Code().createLiteral(0));
        body.insertEnd(entryPointVar);
        CtLocalVariable<Integer> cFlowBreakLevelVar = f.Code().createLocalVariable(f.Type().createReference(Integer.TYPE), "__cFlowBreakLevel", null);
        body.insertEnd(cFlowBreakLevelVar);
        CtLocalVariable<Integer> tryNestingDepthVar = f.Code().createLocalVariable(f.Type().createReference(Integer.TYPE), "__tryNestingDepth", null);
        body.insertEnd(tryNestingDepthVar);
        Map<String, CtTypeReference<?>> localsToMigrate = unfolder.getLocals(true);
        for (String s : localsToMigrate.keySet()) {
            CtTypeReference<?> ref = localsToMigrate.get(s);
            if (ref.isPrimitive() && !(ref instanceof CtArrayTypeReference)) {
                if (ref.getActualClass() == Boolean.TYPE) {
                    @SuppressWarnings("unchecked") CtLocalVariable lv = f.Code().createLocalVariable(ref, s, (CtLiteral) f.Code().createLiteral(false));
                    body.insertEnd(lv);
                } else {
                    @SuppressWarnings("unchecked") CtLocalVariable lv = f.Code().createLocalVariable(ref, s, (CtLiteral) f.Code().createLiteral(0));
                    body.insertEnd(lv);
                }
            } else {
                @SuppressWarnings("unchecked") CtLocalVariable lv = f.Code().createLocalVariable(ref, s, (CtLiteral) f.Code().createLiteral(null));
                body.insertEnd(lv);
            }
        }
        Map<String, CtTypeReference<?>> localsNotToMigrate = unfolder.getLocals(false);
        for (String s : localsNotToMigrate.keySet()) {
            CtTypeReference<?> ref = localsNotToMigrate.get(s);
            if (ref.isPrimitive() && !(ref instanceof CtArrayTypeReference)) {
                if (ref.getActualClass() == Boolean.TYPE) {
                    @SuppressWarnings("unchecked") CtLocalVariable lv = f.Code().createLocalVariable(ref, s, (CtLiteral) f.Code().createLiteral(false));
                    body.insertEnd(lv);
                } else {
                    @SuppressWarnings("unchecked") CtLocalVariable lv = f.Code().createLocalVariable(ref, s, (CtLiteral) f.Code().createLiteral(0));
                    body.insertEnd(lv);
                }
            } else {
                @SuppressWarnings("unchecked") CtLocalVariable lv = f.Code().createLocalVariable(ref, s, (CtLiteral) f.Code().createLiteral(null));
                body.insertEnd(lv);
            }
        }
        CtIf parentNullCheckIf = f.Core().createIf();
        CtBinaryOperator<Boolean> op = f.Code().createBinaryOperator(f.Code().createVariableAccess(stateParam.getReference(), false), f.Code().createLiteral(null), BinaryOperatorKind.NE);
        parentNullCheckIf.setCondition(op);
        CtBlock<T> parentNullCheckBlock = f.Core().createBlock();
        CtFieldAccess<StackFrame> fa = f.Core().createFieldAccess();
        fa.setVariable(f.Field().createReference(f.Type().createReference(StackFrame.class), f.Type().createReference(StackFrame.class), "child"));
        fa.setTarget(f.Code().createVariableAccess(stateParam.getReference(), false));
        List<CtTypeReference<?>> casts = new LinkedList<CtTypeReference<?>>();
        casts.add(stackFrameClass.getReference());
        fa.setTypeCasts(casts);
        CtAssignment<StackFrame, StackFrame> a = f.Core().createAssignment();
        a.setAssigned(f.Code().createVariableAccess(stateVar.getReference(), false));
        a.setAssignment(fa);
        parentNullCheckBlock.insertEnd(a);
        CtVariableAccess<StackFrame> stateRef = f.Code().createVariableAccess(stateVar.getReference(), false);
        CtFieldAccess<Integer> stateEntryPointRef = f.Core().createFieldAccess();
        stateEntryPointRef.setVariable(f.Field().createReference(stackFrameClass.getReference(), f.Type().createReference(Integer.class), "entryPoint"));
        stateEntryPointRef.setTarget(stateRef);
        parentNullCheckBlock.insertEnd(f.Code().createVariableAssignment(entryPointVar.getReference(), false, stateEntryPointRef));
        for (CtParameter<?> param : m.getParameters()) {
            CtFieldAccess fa2 = f.Core().createFieldAccess();
            fa2.setVariable(f.Field().createReference(stackFrameClass.getReference(), param.getType(), param.getSimpleName()));
            fa2.setTarget(stateRef);
            parentNullCheckBlock.insertEnd(f.Code().createVariableAssignment(param.getReference(), false, fa2));
        }
        for (String s : localsToMigrate.keySet()) {
            CtTypeReference<?> ref = localsToMigrate.get(s);
            CtFieldAccess fa2 = f.Core().createFieldAccess();
            fa2.setVariable(f.Field().createReference(stackFrameClass.getReference(), ref, s));
            fa2.setTarget(stateRef);
            parentNullCheckBlock.insertEnd(f.Code().createVariableAssignment(f.Code().createLocalVariableReference(ref, s), false, fa2));
        }
        parentNullCheckIf.setThenStatement(parentNullCheckBlock);
        body.insertEnd(parentNullCheckIf);
        CtTry helperTry = f.Core().createTry();
        body.insertEnd(helperTry);
        CtBlock<?> helperTryBlock = f.Core().createBlock();
        helperTry.setBody(helperTryBlock);
        CtWhile endlessLoop = f.Core().createWhile();
        endlessLoop.setLoopingExpression(f.Code().createLiteral(true));
        helperTryBlock.insertEnd(endlessLoop);
        CtBlock<?> loopBody = f.Core().createBlock();
        endlessLoop.setBody(loopBody);
        loopBody.insertEnd(f.Code().createVariableAssignment(cFlowBreakLevelVar.getReference(), false, f.Code().createLiteral(-1)));
        loopBody.insertEnd(f.Code().createVariableAssignment(tryNestingDepthVar.getReference(), false, f.Code().createLiteral(-1)));
        CtSwitch<Integer> switcher = f.Core().createSwitch();
        CtVariableAccess<Integer> entryPointRef = f.Core().createVariableAccess();
        entryPointRef.setVariable(entryPointVar.getReference());
        switcher.setSelector(entryPointRef);
        loopBody.insertEnd(switcher);
        switcher.setCases(unfolder.getCases());
        CtCatch c = f.Core().createCatch();
        LinkedList<CtCatch> cl = new LinkedList<CtCatch>();
        cl.add(c);
        helperTry.setCatchers(cl);
        CtBlock<?> helperCatchBlock = f.Core().createBlock();
        CtLocalVariable<SaveStack> stackVar = f.Code().createLocalVariable(f.Type().createReference(SaveStack.class), "__stack", null);
        c.setParameter(stackVar);
        c.setBody(helperCatchBlock);
        CtNewClass<StackFrame> nc = f.Core().createNewClass();
        nc.setType(stackFrameClass.getReference());
        a = f.Core().createAssignment();
        a.setAssigned(stateRef);
        a.setAssignment(nc);
        helperCatchBlock.insertEnd(a);
        if (!isStatic) {
            CtAssignment<Serializable, Serializable> a2 = f.Core().createAssignment();
            CtFieldAccess<Serializable> stateSelfRef = f.Core().createFieldAccess();
            stateSelfRef.setVariable(f.Field().createReference(stackFrameClass.getReference(), f.Type().createReference(Serializable.class), "self"));
            stateSelfRef.setTarget(stateRef);
            a2.setAssigned(stateSelfRef);
            a2.setAssignment(f.Code().createThisAccess(parent.getReference()));
            helperCatchBlock.insertEnd(a2);
        }
        CtAssignment<Integer, Integer> a3 = f.Core().createAssignment();
        a3.setAssigned(stateEntryPointRef);
        a3.setAssignment(entryPointRef);
        helperCatchBlock.insertEnd(a3);
        for (CtParameter<?> param : m.getParameters()) {
            CtAssignment a4 = f.Core().createAssignment();
            CtFieldAccess fa2 = f.Core().createFieldAccess();
            fa2.setVariable(f.Field().createReference(stackFrameClass.getReference(), param.getType(), param.getSimpleName()));
            fa2.setTarget(stateRef);
            a4.setAssigned(fa2);
            a4.setAssignment(f.Code().createVariableAccess(param.getReference(), false));
            helperCatchBlock.insertEnd(a4);
        }
        for (String s : localsToMigrate.keySet()) {
            CtTypeReference<?> ref = localsToMigrate.get(s);
            CtAssignment a4 = f.Core().createAssignment();
            CtFieldAccess fa2 = f.Core().createFieldAccess();
            fa2.setVariable(f.Field().createReference(stackFrameClass.getReference(), ref, s));
            fa2.setTarget(stateRef);
            a4.setAssigned(fa2);
            a4.setAssignment(f.Code().createVariableAccess(f.Code().createLocalVariableReference(ref, s), false));
            helperCatchBlock.insertEnd(a4);
        }
        a = f.Core().createAssignment();
        CtFieldAccess<StackFrame> stateChildRef = f.Core().createFieldAccess();
        stateChildRef.setVariable(f.Field().createReference(stackFrameClass.getReference(), f.Type().createReference(StackFrame.class), "child"));
        stateChildRef.setTarget(stateRef);
        a.setAssigned(stateChildRef);
        CtVariableAccess<SaveStack> stackRef = f.Code().createVariableAccess(stackVar.getReference(), false);
        CtFieldAccess<StackFrame> bottomOfStackRef = f.Core().createFieldAccess();
        bottomOfStackRef.setVariable(f.Field().createReference(stackRef.getType(), f.Type().createReference(StackFrame.class), "bottomOfStack"));
        bottomOfStackRef.setTarget(stackRef);
        a.setAssignment(bottomOfStackRef);
        helperCatchBlock.insertEnd(a);
        a = f.Core().createAssignment();
        a.setAssigned(bottomOfStackRef);
        a.setAssignment(stateRef);
        helperCatchBlock.insertEnd(a);
        CtThrow t = f.Core().createThrow();
        t.setThrownExpression(stackRef);
        helperCatchBlock.insertEnd(t);
        return body;
    }
}
