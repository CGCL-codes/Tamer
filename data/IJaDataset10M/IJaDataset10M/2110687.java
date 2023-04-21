package com.sun.tools.javac.comp;

import com.sun.tools.javac.tree.*;
import com.sun.tools.javac.util.*;
import com.sun.tools.javac.code.*;
import com.sun.tools.javac.util.List;
import java.util.ListIterator;
import java.util.Iterator;
import java.lang.reflect.*;
import static com.sun.tools.javac.util.ListBuffer.lb;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.source.tree.Tree;

class RefiningCheck {

    Name handlerEventVariableName;

    Name eventName;

    TreeMaker make;

    public RefiningCheck(Name handlerEventVariableName, Name eventName, TreeMaker make) {
        this.handlerEventVariableName = handlerEventVariableName;
        this.eventName = eventName;
        this.make = make;
    }

    public boolean visit(JCClassDecl a, JCClassDecl b) {
        return false;
    }

    public boolean visit(JCAnnounce a, JCAnnounce b) {
        return visit(a.eventName, b.eventName) && visit(a.arguments, b.arguments) && visit(a.body, b.body);
    }

    public boolean visit(JCEventDecl a, JCEventDecl b) {
        return false;
    }

    public boolean visit(JCMethodDecl a, JCMethodDecl b) {
        return false;
    }

    public boolean visit(JCVariableDecl a, JCVariableDecl b) {
        return visit(a.mods, b.mods) && a.name.toString().equals(b.name.toString()) && visit(a.vartype, b.vartype) && visit(a.init, b.init);
    }

    public boolean visit(JCSkip a, JCSkip b) {
        return true;
    }

    public boolean visit(JCBlock a, JCBlock b) {
        return visit(a.stats, b.stats);
    }

    public boolean visit(JCDoWhileLoop a, JCDoWhileLoop b) {
        return visit(a.body, b.body) && visit(a.cond, b.cond);
    }

    public boolean visit(JCWhileLoop a, JCWhileLoop b) {
        return visit(a.cond, b.cond) && visit(a.body, b.body);
    }

    public boolean visit(JCForLoop a, JCForLoop b) {
        return visit(a.init, b.init) && visit(a.cond, b.cond) && visit(a.step, b.step) && visit(a.body, b.body);
    }

    public boolean visit(JCEnhancedForLoop a, JCEnhancedForLoop b) {
        return visit(a.var, b.var) && visit(a.expr, b.expr) && visit(a.body, b.body);
    }

    public boolean visit(JCLabeledStatement a, JCLabeledStatement b) {
        return a.label.toString().equals(b.label.toString()) && visit(a.body, b.body);
    }

    public boolean visit(JCSwitch a, JCSwitch b) {
        return visit(a.selector, b.selector) && visit(a.cases, b.cases);
    }

    public boolean visit(JCCase a, JCCase b) {
        return visit(a.pat, b.pat) && visit(a.stats, b.stats);
    }

    public boolean visit(JCEventContract a, JCRefining b) {
        class EventSelectErasor extends TreeTranslator {

            public synchronized void visitSelect(JCFieldAccess tree) {
                tree.selected = translate(tree.selected);
                if (tree.selected instanceof JCIdent) {
                    String name = ((JCIdent) tree.selected).name.toString();
                    if (name.equals(eventName.toString()) || name.equals("Contract")) {
                        result = make.at(Position.NOPOS).Ident(tree.name);
                    } else result = tree;
                } else result = tree;
            }
        }
        EventSelectErasor ese = new EventSelectErasor();
        b.spec.requiresPredicate = ese.translate(b.spec.requiresPredicate);
        b.spec.ensuresPredicate = ese.translate(b.spec.ensuresPredicate);
        return visit(a.requiresPredicate, b.spec.requiresPredicate) && visit(a.ensuresPredicate, b.spec.ensuresPredicate);
    }

    public boolean visit(JCSynchronized a, JCSynchronized b) {
        return visit(a.lock, b.lock) && visit(a.body, b.body);
    }

    public boolean visit(JCTry a, JCTry b) {
        return visit(a.body, b.body) && visit(a.catchers, b.catchers) && visit(a.finalizer, b.finalizer) && visit(a.resources, b.resources);
    }

    public boolean visit(JCCatch a, JCCatch b) {
        return visit(a.param, b.param) && visit(a.body, b.body);
    }

    public boolean visit(JCConditional a, JCConditional b) {
        return visit(a.cond, b.cond) && visit(a.truepart, b.truepart) && visit(a.falsepart, b.falsepart);
    }

    public boolean visit(JCIf a, JCIf b) {
        return visit(a.cond, b.cond) && visit(a.thenpart, b.thenpart) && visit(a.elsepart, b.elsepart);
    }

    public boolean visit(JCExpressionStatement a, JCExpressionStatement b) {
        return visit(a.expr, b.expr);
    }

    public boolean visit(JCBreak a, JCBreak b) {
        return a.label.toString().equals(b.label.toString()) && visit(a.target, b.target);
    }

    public boolean visit(JCContinue a, JCContinue b) {
        return a.label.toString().equals(b.label.toString()) && visit(a.target, b.target);
    }

    public boolean visit(JCReturn a, JCReturn b) {
        return visit(a.expr, b.expr);
    }

    public boolean visit(JCThrow a, JCThrow b) {
        return visit(a.expr, b.expr);
    }

    public boolean visit(JCAssert a, JCAssert b) {
        return visit(a.cond, b.cond) && visit(a.detail, b.detail);
    }

    public boolean visit(JCMethodInvocation a, JCMethodInvocation b) {
        return visit(a.typeargs, b.typeargs) && visit(a.meth, b.meth) && visit(a.args, b.args);
    }

    public boolean visit(JCNewClass a, JCNewClass b) {
        return visit(a.encl, b.encl) && visit(a.typeargs, b.typeargs) && visit(a.clazz, b.clazz) && visit(a.args, b.args) && a.def == null && b.def == null;
    }

    public boolean visit(JCNewArray a, JCNewArray b) {
        return visit(a.elemtype, b.elemtype) && visit(a.dims, b.dims) && visit(a.elems, b.elems);
    }

    public boolean visit(JCParens a, JCParens b) {
        return visit(a.expr, b.expr);
    }

    public boolean visit(JCAssign a, JCAssign b) {
        return visit(a.lhs, b.lhs) && visit(a.rhs, b.rhs);
    }

    public boolean visit(JCAssignOp a, JCAssignOp b) {
        return a.getTag() == b.getTag() && visit(a.lhs, b.lhs) && visit(a.rhs, b.rhs);
    }

    public boolean visit(JCUnary a, JCUnary b) {
        return a.getTag() == b.getTag() && visit(a.arg, b.arg);
    }

    public boolean visit(JCBinary a, JCBinary b) {
        return a.getTag() == b.getTag() && visit(a.lhs, b.lhs) && visit(a.rhs, b.rhs);
    }

    public boolean visit(JCTypeCast a, JCTypeCast b) {
        return visit(a.clazz, b.clazz) && visit(a.expr, b.expr);
    }

    public boolean visit(JCInstanceOf a, JCInstanceOf b) {
        return visit(a.expr, b.expr) && visit(a.clazz, b.clazz);
    }

    public boolean visit(JCArrayAccess a, JCArrayAccess b) {
        return visit(a.indexed, b.indexed) && visit(a.index, b.index);
    }

    public boolean visit(JCFieldAccess a, JCFieldAccess b) {
        return visit(a.selected, b.selected) && a.name.toString().equals(b.name.toString());
    }

    public boolean visit(JCIdent a, JCIdent b) {
        return a.name.toString().equals(b.name.toString());
    }

    public boolean visit(JCLiteral a, JCLiteral b) {
        return a.typetag == b.typetag && ((a.value == null && b.value == null) || a.value.equals(b.value));
    }

    public boolean visit(JCPrimitiveTypeTree a, JCPrimitiveTypeTree b) {
        return a.typetag == b.typetag;
    }

    public boolean visit(JCArrayTypeTree a, JCArrayTypeTree b) {
        return visit(a.elemtype, b.elemtype);
    }

    public boolean visit(JCTypeApply a, JCTypeApply b) {
        return visit(a.clazz, b.clazz) && visit(a.arguments, b.arguments);
    }

    public boolean visit(JCTypeUnion a, JCTypeUnion b) {
        return visit(a.alternatives, b.alternatives);
    }

    public boolean visit(JCTypeParameter a, JCTypeParameter b) {
        return a.name.toString().equals(b.name.toString()) && visit(a.bounds, b.bounds);
    }

    public boolean visit(JCWildcard a, JCWildcard b) {
        return visit(a.kind, b.kind) && visit(a.inner, b.inner);
    }

    public boolean visit(TypeBoundKind a, TypeBoundKind b) {
        return a.kind.toString().equals(b.kind.toString());
    }

    public boolean visit(JCAnnotation a, JCAnnotation b) {
        return visit(a.annotationType, b.annotationType) && visit(a.args, b.args);
    }

    public boolean visit(JCModifiers a, JCModifiers b) {
        return a.flags == b.flags && visit(a.annotations, b.annotations);
    }

    public boolean visit(LetExpr a, LetExpr b) {
        return visit(a.defs, b.defs) && visit(a.expr, b.expr);
    }

    public boolean visit(JCRegisterExpression a, JCRegisterExpression b) {
        return visit(a.expr, b.expr);
    }

    public boolean visit(JCUnregisterExpression a, JCUnregisterExpression b) {
        return visit(a.expr, b.expr);
    }

    public boolean visit(java.util.List<? extends JCTree> a, java.util.List<? extends JCTree> b) {
        ListIterator<? extends JCTree> ai = a.listIterator();
        ListIterator<? extends JCTree> bi = b.listIterator();
        while (ai.hasNext()) {
            if (bi.hasNext()) {
                JCTree as = ai.next();
                JCTree bs = bi.next();
                if (as instanceof JCSEChoice) {
                    JCSEChoice c = (JCSEChoice) as;
                    boolean success = false;
                    if (b.size() >= bi.previousIndex() + c.first.stats.size()) {
                        if (visit((java.util.List<? extends JCTree>) c.first.stats, b.subList(bi.previousIndex(), bi.previousIndex() + c.first.stats.size()))) {
                            success = true;
                            for (int i = 1; i <= c.first.stats.size() - 1; i++) {
                                bi.next();
                            }
                        }
                    }
                    if (b.size() >= bi.previousIndex() + c.second.stats.size()) {
                        if (visit((java.util.List<? extends JCTree>) c.second.stats, b.subList(bi.previousIndex(), bi.previousIndex() + c.second.stats.size()))) {
                            success = true;
                            for (int i = 1; i <= c.second.stats.size() - 1; i++) {
                                bi.next();
                            }
                        }
                    }
                    if (!success) return false;
                } else if (!visit(as, bs)) return false;
            } else return false;
        }
        if (bi.hasNext()) return false;
        return true;
    }

    @SuppressWarnings({ "rawtypes" })
    public boolean visit(JCTree a, JCTree b) {
        if (a == null || b == null) return a == b;
        try {
            Method m = getClass().getMethod("visit", new Class[] { a.getClass(), b.getClass() });
            return ((java.lang.Boolean) m.invoke(this, new Object[] { a, b })).booleanValue();
        } catch (NoSuchMethodException e) {
            return false;
        } catch (Exception e) {
            System.out.println("This shouldn't ever happen.");
            System.out.println(e.getCause());
            e.printStackTrace();
            System.exit(5555);
            return false;
        }
    }
}
