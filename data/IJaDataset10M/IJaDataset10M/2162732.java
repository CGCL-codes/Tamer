package org.python.compiler.advanced;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import org.python.antlr.ParseException;
import org.python.antlr.PythonTree;
import org.python.antlr.ast.Assign;
import org.python.antlr.ast.ClassDef;
import org.python.antlr.ast.Dict;
import org.python.antlr.ast.FunctionDef;
import org.python.antlr.ast.GeneratorExp;
import org.python.antlr.ast.Global;
import org.python.antlr.ast.Import;
import org.python.antlr.ast.ImportFrom;
import org.python.antlr.ast.Lambda;
import org.python.antlr.ast.List;
import org.python.antlr.ast.Name;
import org.python.antlr.ast.Num;
import org.python.antlr.ast.Return;
import org.python.antlr.ast.Str;
import org.python.antlr.ast.Tuple;
import org.python.antlr.ast.VisitorBase;
import org.python.antlr.ast.Yield;
import org.python.antlr.ast.alias;
import org.python.antlr.ast.arguments;
import org.python.antlr.ast.comprehension;
import org.python.antlr.ast.expr_contextType;
import org.python.antlr.base.expr;
import org.python.antlr.base.stmt;

class ScopeAnalyzer extends VisitorBase<Void> {

    static <T> T scan(SyntaxErrorPolicy errorPolicy, PragmaParser pragmas, ConstantPool constants, Map<PythonTree, T> scopes, final ScopeFactory<T> factory, java.util.List<? extends stmt> body, boolean moduleCells) throws Exception {
        ScopeMaker<T> base = new ScopeMaker<T>(null, factory, "Module", scopes, moduleCells) {

            @Override
            T create(String[] locals, String[] globals, String[] free, String[] cell, String[] purecell) {
                if (purecell.length != 0) {
                    throw new IllegalStateException("Module can not have free variables.");
                }
                if (globals.length != 0 || free.length != 0) {
                    Set<String> variables = new HashSet<String>();
                    for (String variable : locals) {
                        variables.add(variable);
                    }
                    for (String variable : globals) {
                        variables.add(variable);
                    }
                    for (String variable : free) {
                        variables.add(variable);
                    }
                    locals = variables.toArray(locals);
                }
                return factory.createGlobal(locals, cell, this.hasStarImport);
            }
        };
        ScopeAnalyzer analyzer = new ScopeAnalyzer(base, errorPolicy, pragmas, constants);
        analyzer.traverse(body);
        return base.complete();
    }

    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    private static String[] ofStrings(int size) {
        if (size == 0) {
            return EMPTY_STRING_ARRAY;
        }
        return new String[size];
    }

    private final SyntaxErrorPolicy errorPolicy;

    private final PragmaParser pragmas;

    private final ConstantPool constants;

    final ScopeMaker<?> scope;

    private ScopeAnalyzer(ScopeMaker<?> scope, SyntaxErrorPolicy errorPolicy, PragmaParser pragmas, ConstantPool constants) {
        this.scope = scope;
        this.errorPolicy = errorPolicy;
        this.pragmas = pragmas;
        this.constants = constants;
    }

    final String lambdaName() {
        return "<lambda>";
    }

    final String generatorName() {
        return "<generator>";
    }

    private abstract static class ScopeMaker<T> {

        private final ScopeFactory<T> factory;

        private final Map<PythonTree, T> scopes;

        private final java.util.List<NestedScopeAnalyzer<T>> nested = new LinkedList<NestedScopeAnalyzer<T>>();

        private final boolean canHaveCell;

        private final Set<String> assigned = new HashSet<String>();

        private final Set<String> referenced = new HashSet<String>();

        private final Set<String> global = new HashSet<String>();

        private final Set<String> nonlocal = new HashSet<String>();

        private final Set<String> cell = new HashSet<String>();

        private final Set<String> miranda = new HashSet<String>();

        private final ScopeMaker<T> parent;

        boolean hasStarImport;

        private final String scopeKind;

        ScopeMaker(ScopeMaker<T> parent, ScopeFactory<T> factory, String kind, Map<PythonTree, T> scopes, boolean canHaveCell) {
            this.parent = parent;
            this.factory = factory;
            this.scopeKind = kind;
            this.scopes = scopes;
            this.canHaveCell = canHaveCell;
        }

        final T complete() throws Exception {
            referenced.removeAll(assigned);
            referenced.removeAll(nonlocal);
            referenced.removeAll(global);
            assigned.removeAll(nonlocal);
            assigned.removeAll(global);
            for (NestedScopeAnalyzer<T> scope : nested) {
                scope.process(scopes);
            }
            for (String name : referenced) {
                if (parent != null && parent.require(name)) {
                    nonlocal.add(name);
                } else {
                    global.add(name);
                }
            }
            return create(assigned.toArray(ofStrings(assigned.size())), global.toArray(ofStrings(global.size())), nonlocal.toArray(ofStrings(nonlocal.size())), cell.toArray(ofStrings(cell.size())), miranda.toArray(ofStrings(miranda.size())));
        }

        abstract T create(String[] locals, String[] globals, String[] free, String[] cell, String[] purecell);

        void def(String name) {
            assigned.add(name);
        }

        void use(String name) {
            referenced.add(name);
        }

        void param(String name) {
            throw new ParseException(scopeKind + " definitions does not have parameter.");
        }

        void del(String name) {
            assigned.add(name);
        }

        void global(String name) {
            if (nonlocal.contains(name)) {
                throw new ParseException("'" + name + "' is defined as both global and nonlocal.");
            }
            global.add(name);
        }

        boolean require(String name) {
            if (canHaveCell) {
                if (assigned.contains(name)) {
                    cell.add(name);
                    return true;
                } else if (parent != null) {
                    if (parent.require(name)) {
                        miranda.add(name);
                        return true;
                    }
                }
            }
            return false;
        }

        void nonlocal(String name) {
            if (parent == null) {
                throw new ParseException("nonlocal definition not allowed on " + scopeKind + " level.");
            }
            if (global.contains(name)) {
                throw new ParseException("'" + name + "' is defined as both global and nonlocal.");
            }
            if (!parent.require(name)) {
                throw new ParseException("No definition of nonlocal '" + name + "' in parent scope.");
            }
            nonlocal.add(name);
        }

        void visitYield() {
            throw new ParseException(scopeKind + " definition may not contain yield.");
        }

        void visitReturn(boolean hasValue) {
            throw new ParseException(scopeKind + " definition may not contain return.");
        }

        final void visitStarImport() {
            hasStarImport = true;
        }

        private ScopeMaker<T> classScope(final String name) {
            return new ScopeMaker<T>(this, factory, "Class", scopes, true) {

                @Override
                T create(String[] locals, String[] globals, String[] free, String[] cell, String[] purecell) {
                    return factory.createClass(name, locals, globals, free, cell, this.hasStarImport);
                }
            };
        }

        private ScopeMaker<T> functionScope(final String name, final boolean is_generator) {
            return new ScopeMaker<T>(this, factory, "Function", scopes, true) {

                private boolean isGenerator = is_generator;

                private boolean returnEncountered = false;

                private final Set<String> parameters = new HashSet<String>();

                @Override
                T create(String[] locals, String[] globals, String[] free, String[] cell, String[] purecell) {
                    return factory.createFunction(name, parameters.toArray(ofStrings(parameters.size())), locals, globals, free, cell, this.isGenerator, this.hasStarImport);
                }

                @Override
                void param(String name) {
                    parameters.add(name);
                }

                @Override
                void nonlocal(String name) {
                    if (parameters.contains(name)) {
                        throw new ParseException("Parameter '" + name + "' may not be declared nonlocal.");
                    }
                    super.nonlocal(name);
                }

                @Override
                void global(String name) {
                    if (parameters.contains(name)) {
                        throw new ParseException("Parameter '" + name + "' may not be declared global.");
                    }
                    super.global(name);
                }

                @Override
                boolean require(String name) {
                    if (parameters.contains(name)) {
                        return true;
                    } else {
                        return super.require(name);
                    }
                }

                @Override
                void visitYield() {
                    if (returnEncountered) {
                        throw new ParseException("One function may not contain both yield and return (with a value).");
                    }
                    isGenerator = true;
                }

                @Override
                void visitReturn(boolean hasValue) {
                    if (isGenerator && hasValue) {
                        throw new ParseException("One function may not contain both yield and return (with a value).");
                    }
                    if (hasValue) {
                        returnEncountered = true;
                    }
                }
            };
        }

        final void add(ScopeAnalyzer parent, final ClassDef node) {
            nested.add(new NestedScopeAnalyzer<T>(parent, classScope(node.getInternalName())) {

                @Override
                PythonTree traverse() throws Exception {
                    traverse(node.getInternalBody());
                    return node;
                }
            });
        }

        final void add(ScopeAnalyzer parent, final FunctionDef node) {
            nested.add(new NestedScopeAnalyzer<T>(parent, functionScope(node.getInternalName(), false)) {

                @Override
                PythonTree traverse() throws Exception {
                    defineArguments(node.getInternalArgs());
                    traverse(node.getInternalBody());
                    return node;
                }
            });
        }

        final void add(ScopeAnalyzer parent, final Lambda node) {
            nested.add(new NestedScopeAnalyzer<T>(parent, functionScope(parent.lambdaName(), false)) {

                @Override
                PythonTree traverse() throws Exception {
                    defineArguments(node.getInternalArgs());
                    node.getInternalBody().accept(this);
                    return node;
                }
            });
        }

        final void add(ScopeAnalyzer parent, final GeneratorExp node) {
            nested.add(new NestedScopeAnalyzer<T>(parent, functionScope(parent.generatorName(), true)) {

                @Override
                PythonTree traverse() throws Exception {
                    int count = 0;
                    for (comprehension comp : node.getInternalGenerators()) {
                        if (count++ > 0) {
                            comp.getInternalIter().accept(this);
                        }
                        comp.getInternalTarget().accept(this);
                        traverse(comp.getInternalIfs());
                    }
                    return node;
                }
            });
        }
    }

    private abstract static class NestedScopeAnalyzer<T> extends ScopeAnalyzer {

        NestedScopeAnalyzer(ScopeAnalyzer parent, ScopeMaker<T> scope) {
            super(scope, parent.errorPolicy, parent.pragmas, parent.constants);
        }

        void process(Map<PythonTree, T> result) throws Exception {
            PythonTree key = traverse();
            @SuppressWarnings("unchecked") T value = (T) scope.complete();
            result.put(key, value);
        }

        abstract PythonTree traverse() throws Exception;
    }

    void traverse(java.util.List<? extends PythonTree> body) throws Exception {
        for (PythonTree statement : body) {
            statement.accept(this);
        }
    }

    @Override
    public void traverse(PythonTree node) throws Exception {
        node.traverse(this);
    }

    @Override
    protected Void unhandled_node(PythonTree node) throws Exception {
        return null;
    }

    void defineArguments(arguments args) throws Exception {
        for (expr arg : args.getInternalArgs()) {
            arg.accept(this);
        }
        param(args.getInternalVararg(), args);
        param(args.getInternalKwarg(), args);
    }

    @Override
    public Void visitClassDef(ClassDef node) throws Exception {
        for (expr expression : node.getInternalDecorator_list()) {
            expression.accept(this);
        }
        for (expr expression : node.getInternalBases()) {
            expression.accept(this);
        }
        def(node.getInternalName(), node);
        scope.add(this, node);
        return null;
    }

    @Override
    public Void visitFunctionDef(FunctionDef node) throws Exception {
        for (expr expression : node.getInternalDecorator_list()) {
            expression.accept(this);
        }
        for (expr expression : node.getInternalArgs().getInternalDefaults()) {
            expression.accept(this);
        }
        def(node.getInternalName(), node);
        scope.add(this, node);
        return null;
    }

    @Override
    public Void visitLambda(Lambda node) throws Exception {
        for (expr expression : node.getInternalArgs().getInternalDefaults()) {
            expression.accept(this);
        }
        scope.add(this, node);
        return null;
    }

    @Override
    public Void visitGeneratorExp(GeneratorExp node) throws Exception {
        java.util.List<comprehension> generators = node.getInternalGenerators();
        comprehension comp = (generators == null || generators.isEmpty()) ? null : generators.get(0);
        expr iter = comp != null ? comp.getInternalIter() : null;
        if (iter == null) {
            errorPolicy.astError("No iterator(s) defined for comprehension.", node);
        } else {
            iter.accept(this);
            scope.add(this, node);
        }
        return null;
    }

    @Override
    public Void visitYield(Yield node) throws Exception {
        scope.visitYield();
        return super.visitYield(node);
    }

    @Override
    public Void visitReturn(Return node) throws Exception {
        scope.visitReturn(node.getInternalValue() != null);
        return super.visitReturn(node);
    }

    private void verifyName(String name, PythonTree node) {
        if (constants.isConstant(name)) {
            errorPolicy.syntaxError("'" + name + "' is a constant.", node);
        }
    }

    private void def(String id, PythonTree node) {
        verifyName(id, node);
        try {
            scope.def(id);
        } catch (ParseException pe) {
            errorPolicy.syntaxError(pe, node);
        }
    }

    private void use(String id, PythonTree node) {
        try {
            scope.use(id);
        } catch (ParseException pe) {
            errorPolicy.syntaxError(pe, node);
        }
    }

    private void param(String id, PythonTree node) {
        verifyName(id, node);
        try {
            scope.param(id);
        } catch (ParseException pe) {
            errorPolicy.syntaxError(pe, node);
        }
    }

    private void del(String id, PythonTree node) {
        verifyName(id, node);
        try {
            scope.del(id);
        } catch (ParseException pe) {
            errorPolicy.syntaxError(pe, node);
        }
    }

    @Override
    public Void visitGlobal(Global node) throws Exception {
        for (String id : node.getInternalNames()) {
            try {
                scope.global(id);
            } catch (ParseException pe) {
                errorPolicy.syntaxError(pe, node);
            }
        }
        return null;
    }

    @Override
    public Void visitName(Name node) throws Exception {
        try {
            switch(node.getInternalCtx()) {
                case AugLoad:
                case AugStore:
                    throw new ParseException("The name context " + node.getInternalCtx() + " is not supported!");
                case Del:
                    del(node.getInternalId(), node);
                    break;
                case Load:
                    use(node.getInternalId(), node);
                    break;
                case Param:
                    param(node.getInternalId(), node);
                    break;
                case Store:
                    def(node.getInternalId(), node);
                    break;
                default:
                    throw new ParseException("Unknown name context: " + node.getInternalCtx());
            }
        } catch (ParseException pe) {
            errorPolicy.syntaxError(pe, node);
        }
        return null;
    }

    @Override
    public Void visitImport(Import node) throws Exception {
        for (alias al : node.getInternalNames()) {
            String name;
            if (al.getInternalAsname() != null) {
                name = al.getInternalAsname();
            } else if (al.getInternalName().contains(".")) {
                name = al.getInternalName().substring(al.getInternalName().indexOf("."));
            } else {
                name = al.getInternalName();
            }
            if (constants.isConstant(name)) {
                errorPolicy.syntaxError("Cannot import " + al.getInternalName() + " as " + name + ", " + name + " is a constant.", al);
            } else {
                def(name, al);
            }
        }
        return null;
    }

    @Override
    public Void visitImportFrom(ImportFrom node) throws Exception {
        try {
            if (pragmas != null) pragmas.checkPragma(node);
        } catch (ParseException pe) {
            errorPolicy.error(pe);
        }
        if (node.getInternalNames() == null || node.getInternalNames().isEmpty() || (node.getInternalNames().size() == 1 && "*".equals(node.getInternalNames().get(0).getInternalName()))) {
            try {
                scope.visitStarImport();
            } catch (ParseException pe) {
                errorPolicy.syntaxError(pe, node);
            }
        } else {
            for (alias al : node.getInternalNames()) {
                String name = al.getInternalAsname() != null ? al.getInternalAsname() : al.getInternalName();
                if (constants.isConstant(name)) {
                    errorPolicy.syntaxError("Cannot import " + al.getInternalName() + " as " + name + ", " + name + " is a constant.", al);
                } else {
                    def(name, al);
                }
            }
        }
        return null;
    }

    @Override
    public Void visitAssign(Assign node) throws Exception {
        for (expr target : node.getInternalTargets()) {
            checkAssign(target, node.getInternalValue());
        }
        return super.visitAssign(node);
    }

    private void checkAssign(expr target, expr value) {
        if (target instanceof Name) {
            Name name = (Name) target;
            if (constants.isConstantWithUnacceptedAssignment(name.getInternalId(), value)) {
                errorPolicy.syntaxError("Cannot assign to " + name.getInternalId() + ", " + name.getInternalId() + " is a constant.", target);
            }
        } else if (target instanceof Tuple) {
            checkAll(((Tuple) target).getInternalElts(), value);
        } else if (target instanceof List) {
            checkAll(((List) target).getInternalElts(), value);
        } else if (target instanceof Dict) {
            checkDict((Dict) target, value);
        }
    }

    private void checkAll(java.util.List<expr> targets, expr value) {
        java.util.List<expr> values = Collections.emptyList();
        if (value instanceof Tuple) {
            values = ((Tuple) value).getInternalElts();
        } else if (value instanceof List) {
            values = ((List) value).getInternalElts();
        }
        int i = 0;
        for (; i < targets.size() && i < values.size(); i++) {
            checkAssign(targets.get(i), values.get(i));
        }
        for (; i < targets.size(); i++) {
            checkAssign(targets.get(i), null);
        }
    }

    private void checkDict(Dict dict, expr value) {
        if (value instanceof Dict) {
            Dict valDict = (Dict) value;
            Map<String, expr> knownValues = new HashMap<String, expr>();
            for (int i = 0; i < valDict.getInternalKeys().size(); i++) {
                if (valDict.getInternalKeys().get(i) instanceof Str) {
                    knownValues.put(((Str) valDict.getInternalKeys().get(i)).getInternalS().toString(), valDict.getInternalValues().get(i));
                }
            }
            for (int i = 0; i < dict.getInternalKeys().size(); i++) {
                if (dict.getInternalKeys().get(i) instanceof Str) {
                    checkAssign(dict.getInternalValues().get(i), knownValues.get(((Str) dict.getInternalKeys().get(i)).toString()));
                } else {
                    checkAssign(dict.getInternalValues().get(i), null);
                }
            }
        } else {
            checkAll(dict.getInternalValues(), null);
        }
    }

    @Override
    public Void visitNum(Num node) throws Exception {
        constants.createConstant(node);
        return null;
    }

    @Override
    public Void visitStr(Str node) throws Exception {
        constants.createConstant(node);
        return null;
    }

    @Override
    public Void visitTuple(Tuple node) throws Exception {
        expr_contextType ctx = node.getInternalCtx();
        if (ctx == expr_contextType.Load || ctx == expr_contextType.AugLoad) {
            constants.createConstant(node);
        }
        return null;
    }
}
