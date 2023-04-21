package de.grogra.xl.compiler;

import antlr.collections.AST;
import de.grogra.grammar.*;
import de.grogra.reflect.*;
import de.grogra.xl.compiler.pattern.PatternWrapper;
import de.grogra.xl.expr.Expression;
import de.grogra.xl.compiler.scope.*;
import de.grogra.xl.compiler.scope.Package;

final class Resolver implements CompilerTokenTypes, Members.Resolution {

    static final int RETURN_VAR = Members.MIN_UNUSED;

    static final int TYPES_OBSCURE_PREDICATES = Members.MIN_UNUSED << 1;

    static final int FIND_STATIC_MEMBERS = Members.MIN_UNUSED << 2;

    private final Members members;

    private final Compiler compiler;

    private Object qualifier;

    private String simpleName;

    private Scope initialScope;

    private int resolutionTypes;

    public boolean allowsAmbiguousMembers(Member first) {
        return ((resolutionTypes & FIND_STATIC_MEMBERS) != 0) || (first instanceof Method) || (first instanceof PatternWrapper);
    }

    public boolean isApplicable(Member m, Members.Applicability a, Scope scope) {
        return true;
    }

    public Type[] getArgumentTypes() {
        return Type.TYPE_0;
    }

    private boolean isObscuredByOverride(Member m1, Member m2) {
        return ((resolutionTypes & TYPES_OBSCURE_PREDICATES) != 0) && (m1 instanceof PatternWrapper) && (m2 instanceof Type) && !Reflection.equal(((PatternWrapper) m1).getPatternType(), (Type) m2);
    }

    public boolean isLessThan(Member m1, Members.Applicability a1, Member m2, Members.Applicability a2, Scope scope) {
        return ((resolutionTypes & FIND_STATIC_MEMBERS) != 0) ? ((m2 instanceof Type) && !(m1 instanceof Type)) : (isObscuredByOverride(m1, m2) || (!isObscuredByOverride(m2, m1) && (Members.getMemberType(m1) > Members.getMemberType(m2))));
    }

    Resolver(Compiler compiler) {
        this.compiler = compiler;
        this.members = new Members(compiler);
    }

    void reset() {
        members.reset();
        qualifier = null;
        simpleName = null;
    }

    Field resolveField(Scope scope, Type type, AST id, int flags) {
        if (Reflection.isInvalid(type)) {
            return null;
        }
        members.resetName(id);
        resolutionTypes = Members.FIELD | flags;
        members.addMatches(scope, type, resolutionTypes);
        try {
            Field f = (Field) members.resolve(this);
            initialScope = members.getScopeForResult();
            return f;
        } catch (RecognitionException e) {
            compiler.problems.add(e);
            return null;
        }
    }

    Scope getInitialScope() {
        return initialScope;
    }

    private Object resolveName0(AST tree, int types, int qtypes, Scope scope) throws RecognitionException {
        Object m;
        String id;
        switch(tree.getType()) {
            case IDENT:
                id = tree.getText();
                simpleName = id;
                members.resetName(tree);
                resolutionTypes = types;
                members.addMatches(scope, types);
                m = members.resolve(this);
                initialScope = members.getScopeForResult();
                if (m instanceof Local) {
                    if ((types & RETURN_VAR) == 0) {
                        return ((Local) m).createExpression(scope, tree);
                    }
                }
                if (m instanceof Field) {
                    MethodScope ms = MethodScope.get(scope);
                    if ((ms != null) && ms.isIllegalUseBeforeDeclaration((Field) m)) {
                        compiler.problems.addSemanticError(Compiler.I18N.msg(ProblemReporter.USE_BEFORE_DECLARATION, id), tree);
                    }
                    if ((types & RETURN_VAR) == 0) {
                        if (compiler.run != Compiler.COMPILATION) {
                            return null;
                        }
                        return compiler.compileFieldExpression((Field) m, compiler.compileInstance((Field) m, initialScope, scope, tree), scope, tree);
                    }
                }
                return m;
            case DOT:
                tree = tree.getFirstChild();
                AST left = tree;
                m = resolveName0(tree, qtypes, qtypes, scope);
                types &= ~Members.FULLY_QUALIFIED;
                qualifier = m;
                if (m == null) {
                    return null;
                }
                tree = tree.getNextSibling();
                assert tree.getType() == IDENT;
                simpleName = tree.getText();
                if (m instanceof Package) {
                    int origTypes = types;
                    if ((types & Members.TOP_LEVEL_PACKAGE) != 0) {
                        types |= Members.SUB_PACKAGE;
                    }
                    types &= Members.TYPE | Members.PREDICATE | Members.SUB_PACKAGE | ~(Members.MEMBER_MASK | Members.TOP_LEVEL_PACKAGE);
                    if (types == 0) {
                        if (compiler.run != Compiler.COMPILATION) {
                            return null;
                        }
                        while (left.getType() == DOT) {
                            left = left.getFirstChild();
                        }
                        throw ProblemReporter.createSemanticError(Compiler.I18N.msg(ProblemReporter.NO_MEMBER_IN_SCOPE, Members.getMembersDescription(origTypes, false), left.getText()), left);
                    }
                    members.resetName(tree);
                    resolutionTypes = types;
                    members.addMatches((Package) m, types);
                    return members.resolve(this);
                }
                return resolveName0(m, tree, types & ~Members.TOP_LEVEL_PACKAGE, scope);
            default:
                throw new AssertionError(tree);
        }
    }

    private Object resolveName0(Object m, AST id, int types, Scope scope) throws RecognitionException {
        if (m == null) {
            return null;
        }
        assert id.getType() == IDENT;
        if (m instanceof Type) {
            if (Reflection.isInvalid((Type) m)) {
                return null;
            }
            types &= Members.FIELD | Members.METHOD | Members.TYPE | Members.PREDICATE | ~Members.MEMBER_MASK;
            members.resetName(id);
            resolutionTypes = types;
            members.addMatches(scope, (Type) m, types);
            m = members.resolve(this);
            if ((m instanceof Field) && ((types & RETURN_VAR) == 0)) {
                return (compiler.run == Compiler.COMPILATION) ? compiler.compileFieldExpression((Field) m, null, scope, id) : null;
            }
            return m;
        } else if (compiler.run == Compiler.COMPILATION) {
            if (m instanceof Expression) {
                types &= Members.FIELD | Members.METHOD | ~Members.MEMBER_MASK;
                Expression expr = (Expression) m;
                if (Reflection.isInvalid(expr.getType())) {
                    return null;
                }
                members.resetName(id);
                resolutionTypes = types;
                members.addMatches(scope, expr.getType(), types);
                m = members.resolve(this);
                if ((m instanceof Field) && ((types & RETURN_VAR) == 0)) {
                    return compiler.compileFieldExpression((Field) m, expr, scope, id);
                }
                return m;
            }
            throw new AssertionError(m);
        } else {
            return null;
        }
    }

    Object resolveName(AST tree, int types, int qtypes, Scope scope) {
        try {
            qualifier = null;
            return resolveName0(tree, types, qtypes, scope);
        } catch (de.grogra.grammar.RecognitionException e) {
            compiler.problems.add(e);
            return null;
        }
    }

    Object resolveName(Object q, AST id, int types, Scope scope) {
        try {
            return resolveName0(q, id, types, scope);
        } catch (de.grogra.grammar.RecognitionException e) {
            compiler.problems.add(e);
            return null;
        }
    }

    Member resolveStaticMember(Type type, AST id, Scope scope) {
        return (Member) resolveName(type, id, Members.FIELD | Members.METHOD | Members.PREDICATE | Members.TYPE | Members.STATIC_ONLY | FIND_STATIC_MEMBERS, scope);
    }

    Object getQualifier() {
        return qualifier;
    }

    String getSimpleName() {
        return simpleName;
    }

    Type resolveTypeName(AST tree, Scope scope) {
        Type t = (Type) resolveName(tree, Members.TYPE, Members.TYPE | Members.TOP_LEVEL_PACKAGE, scope);
        return (t == null) ? Type.INVALID : t;
    }

    Member resolvePatternOrMethodOrTypeName(AST tree, Scope scope) {
        return (Member) resolveName(tree, Members.TYPE | Members.PREDICATE | Members.METHOD, Members.VARIABLE | Members.TYPE | Members.TOP_LEVEL_PACKAGE, scope);
    }

    Object resolveExpressionOrPatternOrMethodName(AST tree, Scope scope) {
        return resolveName(tree, Members.VARIABLE | Members.PREDICATE | Members.METHOD, Members.VARIABLE | Members.TYPE | Members.TOP_LEVEL_PACKAGE, scope);
    }

    Member resolveCanonicalTypeOrPackageName(AST tree, Scope scope) {
        return (Member) resolveName(tree, Members.TYPE | Members.TOP_LEVEL_PACKAGE | Members.DECLARED_ONLY | Members.FULLY_QUALIFIED, Members.TYPE | Members.TOP_LEVEL_PACKAGE | Members.DECLARED_ONLY | Members.FULLY_QUALIFIED, scope);
    }

    Type resolveCanonicalTypeName(AST tree, Scope scope) {
        Type t = (Type) resolveName(tree, Members.TYPE | Members.DECLARED_ONLY | Members.FULLY_QUALIFIED, Members.TYPE | Members.TOP_LEVEL_PACKAGE | Members.DECLARED_ONLY | Members.FULLY_QUALIFIED, scope);
        return (t == null) ? Type.INVALID : t;
    }

    Expression resolveExpressionName(AST tree, Scope scope) {
        return (Expression) resolveName(tree, Members.VARIABLE, Members.VARIABLE | Members.TYPE | Members.TOP_LEVEL_PACKAGE, scope);
    }

    Object resolveExpressionOrTypeName(AST tree, Scope scope) {
        return resolveName(tree, Members.VARIABLE | Members.TYPE, Members.VARIABLE | Members.TYPE | Members.TOP_LEVEL_PACKAGE, scope);
    }

    Object resolveExpressionOrPatternOrMethodOrTypeName(AST tree, Scope scope) {
        return resolveName(tree, Members.VARIABLE | Members.METHOD | Members.TYPE | Members.PREDICATE | TYPES_OBSCURE_PREDICATES, Members.VARIABLE | Members.TYPE | Members.TOP_LEVEL_PACKAGE, scope);
    }

    Object resolveExpressionOrMethodOrTypeName(AST tree, Scope scope) {
        return resolveName(tree, Members.VARIABLE | Members.METHOD | Members.TYPE, Members.VARIABLE | Members.TYPE | Members.TOP_LEVEL_PACKAGE, scope);
    }

    Member resolveMethodOrTypeName(AST tree, Scope scope) {
        return (Member) resolveName(tree, Members.TYPE | Members.METHOD, Members.VARIABLE | Members.TYPE | Members.TOP_LEVEL_PACKAGE, scope);
    }

    Type resolveTypeName(Type type, AST id, Scope scope) {
        return (Type) resolveName(type, id, Members.TYPE, scope);
    }

    Object resolveIfDeclared(AST id, int types, Scope scope) {
        compiler.problems.disableAdd();
        Object o = null;
        try {
            o = resolveName(id, types | Members.DECLARED_ONLY, 0, scope);
        } finally {
            RecognitionExceptionList errs = compiler.problems.enableAdd();
            if (errs.containsErrors()) {
                o = null;
            } else {
                compiler.problems.addAll(errs);
            }
        }
        return o;
    }
}
