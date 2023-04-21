package org.python.compiler.advanced.ast;

import java.util.Stack;
import org.python.antlr.PythonTree;
import org.python.antlr.ast.Assert;
import org.python.antlr.ast.Assign;
import org.python.antlr.ast.Attribute;
import org.python.antlr.ast.AugAssign;
import org.python.antlr.ast.BinOp;
import org.python.antlr.ast.BoolOp;
import org.python.antlr.ast.Break;
import org.python.antlr.ast.Call;
import org.python.antlr.ast.ClassDef;
import org.python.antlr.ast.Compare;
import org.python.antlr.ast.Continue;
import org.python.antlr.ast.Delete;
import org.python.antlr.ast.Dict;
import org.python.antlr.ast.Ellipsis;
import org.python.antlr.ast.Exec;
import org.python.antlr.ast.Expr;
import org.python.antlr.ast.Expression;
import org.python.antlr.ast.ExtSlice;
import org.python.antlr.ast.For;
import org.python.antlr.ast.FunctionDef;
import org.python.antlr.ast.GeneratorExp;
import org.python.antlr.ast.Global;
import org.python.antlr.ast.If;
import org.python.antlr.ast.IfExp;
import org.python.antlr.ast.Import;
import org.python.antlr.ast.ImportFrom;
import org.python.antlr.ast.Index;
import org.python.antlr.ast.Interactive;
import org.python.antlr.ast.Lambda;
import org.python.antlr.ast.List;
import org.python.antlr.ast.ListComp;
import org.python.antlr.ast.Module;
import org.python.antlr.ast.Name;
import org.python.antlr.ast.Num;
import org.python.antlr.ast.Pass;
import org.python.antlr.ast.Print;
import org.python.antlr.ast.Raise;
import org.python.antlr.ast.Repr;
import org.python.antlr.ast.Return;
import org.python.antlr.ast.Slice;
import org.python.antlr.ast.Str;
import org.python.antlr.ast.Subscript;
import org.python.antlr.ast.Suite;
import org.python.antlr.ast.TryExcept;
import org.python.antlr.ast.TryFinally;
import org.python.antlr.ast.Tuple;
import org.python.antlr.ast.UnaryOp;
import org.python.antlr.ast.VisitorIF;
import org.python.antlr.ast.While;
import org.python.antlr.ast.With;
import org.python.antlr.ast.Yield;
import org.python.antlr.ast.cmpopType;
import org.python.antlr.ast.comprehensionType;
import org.python.antlr.ast.excepthandlerType;
import org.python.antlr.ast.exprType;
import org.python.antlr.ast.keywordType;
import org.python.antlr.ast.modType;
import org.python.antlr.ast.operatorType;
import org.python.antlr.ast.sliceType;
import org.python.antlr.ast.stmtType;
import org.python.antlr.ast.unaryopType;
import org.python.bytecode.BinaryOperator;
import org.python.bytecode.BytecodeVisitor;
import org.python.bytecode.ComparisonOperator;
import org.python.bytecode.Label;
import org.python.bytecode.SliceMode;
import org.python.bytecode.UnaryOperator;
import org.python.bytecode.VariableContext;
import org.python.compiler.advanced.BytecodeBundle;
import org.python.compiler.advanced.CodeInfo;
import org.python.compiler.advanced.CompilerFlag;
import org.python.compiler.advanced.CompilerVariable;
import org.python.compiler.advanced.EnvironmentHolder;
import org.python.compiler.advanced.EnvironmentInfo;
import org.python.compiler.advanced.YieldPoint;
import org.python.core.Py;
import org.python.core.PyInteger;
import org.python.core.PyLong;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.PyUnicode;
import org.python.compiler.advanced.CompilerPolicy;
import org.python.compiler.bytecode.BytecodeCompiler;
import org.python.compiler.bytecode.PythonBytecodeCompilingBundle;

/**
 * An AST visitor that feeds a Bytecode visitor.
 * 
 * @author Tobias Ivarsson
 */
public class AstToBytecode implements VisitorIF<BytecodeBundle> {

    private static interface CompileAction {

        /**
         * Generate predefined actions in the compiler.
         * 
         * @param compiler
         *            The compiler that the action should be performed on.
         * @return <code>true</code> if the action is a return action, <code>false</code>
         *         otherwise.
         */
        boolean perform(BytecodeVisitor compiler);
    }

    private static class LoopSkip implements CompileAction {

        private final Label at;

        private final Label to;

        LoopSkip(Label at, Label to) {
            this.at = at;
            this.to = to;
        }

        public boolean perform(BytecodeVisitor compiler) {
            compiler.visitLabel(at);
            compiler.visitPop();
            compiler.visitJump(to);
            return false;
        }
    }

    private static class LoopEnd implements CompileAction {

        private final Label at;

        private final Label to;

        LoopEnd(Label at, Label to) {
            this.at = at;
            this.to = to;
        }

        public boolean perform(BytecodeVisitor compiler) {
            compiler.visitLabel(at);
            compiler.visitPopBlock();
            compiler.visitJump(to);
            return false;
        }
    }

    private static interface ConditionalAction {

        void perform(BytecodeVisitor compiler, Label dest);
    }

    private static final CompileAction printResult = new CompileAction() {

        public boolean perform(BytecodeVisitor compiler) {
            compiler.visitPrintExpression();
            return false;
        }
    };

    private static final CompileAction popResult = new CompileAction() {

        public boolean perform(BytecodeVisitor compiler) {
            compiler.visitPop();
            return false;
        }
    };

    private static final CompileAction returnResult = new CompileAction() {

        public boolean perform(BytecodeVisitor compiler) {
            compiler.visitReturn();
            return true;
        }
    };

    private static final CompileAction returnNone = new CompileAction() {

        public boolean perform(BytecodeVisitor compiler) {
            compiler.visitLoadConstant(Py.None);
            compiler.visitReturn();
            return true;
        }
    };

    private static final ConditionalAction jumpIfTrue = new ConditionalAction() {

        public void perform(BytecodeVisitor compiler, Label dest) {
            compiler.visitJumpIfTrue(dest);
        }
    };

    private static final ConditionalAction jumpIfFalse = new ConditionalAction() {

        public void perform(BytecodeVisitor compiler, Label dest) {
            compiler.visitJumpIfTrue(dest);
        }
    };

    private final PythonBytecodeCompilingBundle bundle;

    private final String name;

    private final CompilerPolicy policy;

    public AstToBytecode(PythonBytecodeCompilingBundle bundle, CompilerPolicy policy, String name) {
        this.bundle = bundle;
        this.name = name;
        this.policy = policy;
    }

    private String signature() {
        return this.name;
    }

    private EnvironmentHolder context;

    private EnvironmentInfo currentEnvironment;

    private BytecodeCompiler compiler = null;

    private CompileAction expr_action = popResult;

    private boolean lastStatementWasReturn = false;

    private Label nearestLoop;

    private int lastLineNumber = -1;

    private void buildContext(modType node) throws Exception {
        context = node.accept(new ContextBuilder(policy));
    }

    private void sendResumeTable() {
        if (currentEnvironment.isReenterable()) {
            compiler.visitResumeTable(new Label(), currentEnvironment.getEntryPoints());
        }
    }

    private void putLineNumber(PythonTree node) {
        int lnno = node.getLine();
        if (lnno != lastLineNumber) {
            compiler.visitLineNumber(lastLineNumber = lnno);
        }
    }

    public BytecodeBundle visitModule(Module node) throws Exception {
        buildContext(node);
        EnvironmentInfo oldEnvironment = currentEnvironment;
        currentEnvironment = context.getEnvironment(node);
        try {
            compiler = bundle.compile(signature(), (CodeInfo) currentEnvironment, CompilerFlag.module(currentEnvironment.getCompilerFlags()), true);
            putLineNumber(node);
            for (stmtType statement : node.body) {
                statement.accept(this);
            }
            returnNone.perform(compiler);
            compiler.visitStop();
        } finally {
            currentEnvironment = oldEnvironment;
        }
        return bundle;
    }

    public BytecodeBundle visitExpression(Expression node) throws Exception {
        buildContext(node);
        EnvironmentInfo oldEnvironment = currentEnvironment;
        currentEnvironment = context.getEnvironment(node);
        try {
            compiler = bundle.compile(signature(), (CodeInfo) currentEnvironment, CompilerFlag.expression(currentEnvironment.getCompilerFlags()), false);
            putLineNumber(node);
            node.body.accept(this);
            returnResult.perform(compiler);
            compiler.visitStop();
        } finally {
            currentEnvironment = oldEnvironment;
        }
        return bundle;
    }

    public BytecodeBundle visitInteractive(Interactive node) throws Exception {
        buildContext(node);
        EnvironmentInfo oldEnvironment = currentEnvironment;
        currentEnvironment = context.getEnvironment(node);
        CompileAction old_action = expr_action;
        expr_action = printResult;
        try {
            compiler = bundle.compile(signature(), (CodeInfo) currentEnvironment, CompilerFlag.interactive(currentEnvironment.getCompilerFlags()), false);
            putLineNumber(node);
            returnNone.perform(compiler);
            compiler.visitStop();
        } finally {
            expr_action = old_action;
            currentEnvironment = oldEnvironment;
        }
        return bundle;
    }

    public BytecodeBundle visitSuite(Suite node) throws Exception {
        throw new RuntimeException("Hit a Suite element, this isn't supposed to be used in running code!");
    }

    public BytecodeBundle visitClassDef(ClassDef node) throws Exception {
        CompileAction old_action = expr_action;
        expr_action = popResult;
        try {
            putLineNumber(node);
            compiler.visitLoadConstant(new PyString(node.name));
            if (node.bases != null) {
                for (exprType base : node.bases) {
                    base.accept(this);
                }
            }
            if (node.bases == null || node.bases.length == 0) {
                compiler.visitLoadConstant(Py.EmptyTuple);
            } else {
                compiler.visitBuildTuple(node.bases.length);
            }
            EnvironmentInfo oldEnvironment = currentEnvironment;
            currentEnvironment = context.getEnvironment(node);
            BytecodeCompiler oldCompiler = compiler;
            compiler = compiler.constructClass(node.name, currentEnvironment.closureVariables(), currentEnvironment.getCompilerFlags());
            putLineNumber(node);
            try {
                for (stmtType statement : node.body) {
                    statement.accept(this);
                }
                compiler.visitLoadLocals();
                compiler.visitReturn();
                compiler.visitStop();
            } finally {
                compiler = oldCompiler;
                currentEnvironment = oldEnvironment;
            }
        } finally {
            expr_action = old_action;
        }
        lastStatementWasReturn = false;
        return bundle;
    }

    public BytecodeBundle visitFunctionDef(FunctionDef node) throws Exception {
        CompileAction old_action = expr_action;
        expr_action = popResult;
        try {
            int decoratorCount = 0;
            if (node.decorators != null) {
                decoratorCount = node.decorators.length;
                for (exprType decorator : node.decorators) {
                    decorator.accept(this);
                }
            }
            int numDefaults = 0;
            if (node.args != null && node.args.defaults != null && node.args.defaults.length > 0) {
                numDefaults = node.args.defaults.length;
                for (exprType def : node.args.defaults) {
                    def.accept(this);
                }
            }
            EnvironmentInfo oldEnvironment = currentEnvironment;
            currentEnvironment = context.getEnvironment(node);
            try {
                BytecodeCompiler oldCompiler = compiler;
                compiler = compiler.constructFunction(currentEnvironment.closureVariables(), numDefaults, currentEnvironment.getCompilerFlags(), node.name);
                try {
                    sendResumeTable();
                    for (stmtType statement : node.body) {
                        statement.accept(this);
                    }
                    if (!lastStatementWasReturn) {
                        returnNone.perform(compiler);
                    }
                    compiler.visitStop();
                } finally {
                    compiler = oldCompiler;
                }
            } finally {
                currentEnvironment = oldEnvironment;
            }
            for (int i = 0; i < decoratorCount; i++) {
                compiler.visitCall(false, false, 1, 0);
            }
        } finally {
            expr_action = old_action;
        }
        lastStatementWasReturn = false;
        return bundle;
    }

    public BytecodeBundle visitLambda(Lambda node) throws Exception {
        CompileAction old_action = expr_action;
        expr_action = popResult;
        try {
            int numDefaults = 0;
            if (node.args != null && node.args.defaults != null && node.args.defaults.length > 0) {
                numDefaults = node.args.defaults.length;
                for (exprType def : node.args.defaults) {
                    def.accept(this);
                }
            }
            EnvironmentInfo oldEnvironment = currentEnvironment;
            currentEnvironment = context.getEnvironment(node);
            try {
                BytecodeCompiler oldCompiler = compiler;
                compiler = compiler.constructFunction(currentEnvironment.closureVariables(), numDefaults, currentEnvironment.getCompilerFlags());
                try {
                    sendResumeTable();
                    node.body.accept(this);
                    returnResult.perform(compiler);
                    compiler.visitStop();
                } finally {
                    compiler = oldCompiler;
                }
            } finally {
                currentEnvironment = oldEnvironment;
            }
        } finally {
            expr_action = old_action;
        }
        return bundle;
    }

    public BytecodeBundle visitGeneratorExp(GeneratorExp node) throws Exception {
        CompileAction old_action = expr_action;
        expr_action = popResult;
        try {
            EnvironmentInfo oldEnvironment = currentEnvironment;
            currentEnvironment = context.getEnvironment(node);
            try {
                BytecodeCompiler oldCompiler = compiler;
                compiler = compiler.constructFunction(currentEnvironment.closureVariables(), 0, currentEnvironment.getCompilerFlags());
                try {
                    sendResumeTable();
                    Stack<CompileAction> endBlocks = new Stack<CompileAction>();
                    Label start = null, prev = null;
                    for (comprehensionType comp : node.generators) {
                        if (start != null) {
                            prev = start;
                        }
                        start = new Label();
                        if (prev == null) {
                            prev = start;
                        }
                        Label end = new Label();
                        Label done = new Label();
                        compiler.visitSetupLoop(end);
                        comp.iter.accept(this);
                        compiler.visitUnaryOperator(UnaryOperator.ITERATOR);
                        compiler.visitLabel(start);
                        compiler.visitForIteration(done);
                        comp.target.accept(this);
                        endBlocks.add(new LoopEnd(done, prev));
                        if (comp.ifs != null) {
                            Label skip = new Label();
                            for (exprType cond : comp.ifs) {
                                cond.accept(this);
                                compiler.visitJumpIfFalse(skip);
                                compiler.visitPop();
                            }
                            endBlocks.add(new LoopSkip(skip, start));
                        }
                    }
                    while (!endBlocks.isEmpty()) {
                        endBlocks.pop().perform(compiler);
                    }
                    compiler.visitStop();
                } finally {
                    compiler = oldCompiler;
                }
            } finally {
                currentEnvironment = oldEnvironment;
            }
            compiler.visitCall(false, false, 0, 0);
        } finally {
            expr_action = old_action;
        }
        return bundle;
    }

    public BytecodeBundle visitExpr(Expr node) throws Exception {
        node.value.accept(this);
        lastStatementWasReturn = expr_action.perform(compiler);
        return bundle;
    }

    public BytecodeBundle visitAssert(Assert node) throws Exception {
        Label ok = new Label();
        node.test.accept(this);
        compiler.visitJumpIfTrue(ok);
        compiler.visitPop();
        compiler.visitLoad(VariableContext.GLOBAL, "AssertionError");
        if (node.msg != null) {
            node.msg.accept(this);
            compiler.visitRaise(2);
        } else {
            compiler.visitRaise(1);
        }
        compiler.visitLabel(ok);
        compiler.visitPop();
        lastStatementWasReturn = false;
        return bundle;
    }

    public BytecodeBundle visitAssign(Assign node) throws Exception {
        node.value.accept(this);
        int lastPos = node.targets.length - 1;
        for (int i = 0; i < node.targets.length; i++) {
            if (i != lastPos) {
                compiler.visitDup(1);
            }
            node.targets[i].accept(this);
        }
        lastStatementWasReturn = false;
        return bundle;
    }

    public BytecodeBundle visitAttribute(Attribute node) throws Exception {
        node.value.accept(this);
        switch(node.ctx) {
            case AugLoad:
                break;
            case AugStore:
                break;
            case Del:
                compiler.visitDeleteAttribute(node.attr);
                break;
            case Load:
                compiler.visitLoadAttribute(node.attr);
                break;
            case Param:
                break;
            case Store:
                compiler.visitStoreAttribute(node.attr);
                break;
            default:
                break;
        }
        return bundle;
    }

    public BytecodeBundle visitAugAssign(AugAssign node) throws Exception {
        if (node.target instanceof Attribute) {
            Attribute attrib = (Attribute) node.target;
            attrib.value.accept(this);
            compiler.visitDup(1);
            compiler.visitLoadAttribute(attrib.attr);
            node.value.accept(this);
            compiler.visitInplaceOperator(binaryOperator(node.op));
            compiler.visitStoreAttribute(attrib.attr);
        } else if (node.target instanceof Name) {
            Name name = (Name) node.target;
            VariableContext ctx = currentEnvironment.getVariableContextFor(name.id);
            compiler.visitLoad(ctx, name.id);
            node.value.accept(this);
            compiler.visitInplaceOperator(binaryOperator(node.op));
            compiler.visitStore(ctx, name.id);
        } else if (node.target instanceof Subscript) {
            Subscript subscr = (Subscript) node.target;
            if (subscr.slice instanceof Slice && ((Slice) subscr.slice).step == null) {
                Slice slice = (Slice) subscr.slice;
            } else {
            }
        } else {
        }
        lastStatementWasReturn = false;
        return bundle;
    }

    public BytecodeBundle visitBinOp(BinOp node) throws Exception {
        node.left.accept(this);
        node.right.accept(this);
        compiler.visitBinaryOperator(binaryOperator(node.op));
        return bundle;
    }

    public BytecodeBundle visitBoolOp(BoolOp node) throws Exception {
        ConditionalAction jump;
        switch(node.op) {
            case And:
                jump = jumpIfFalse;
                break;
            case Or:
                jump = jumpIfTrue;
                break;
            default:
                jump = null;
        }
        Label done = new Label();
        int i = 0;
        for (; i < node.values.length - 1; i++) {
            node.values[i].accept(this);
            jump.perform(compiler, done);
            compiler.visitPop();
        }
        node.values[i].accept(this);
        compiler.visitLabel(done);
        return bundle;
    }

    public BytecodeBundle visitBreak(Break node) throws Exception {
        compiler.visitBreak();
        lastStatementWasReturn = false;
        return bundle;
    }

    public BytecodeBundle visitCall(Call node) throws Exception {
        node.func.accept(this);
        int argCount = 0, kwCount = 0;
        if (node.args != null) {
            for (exprType arg : node.args) {
                arg.accept(this);
                argCount++;
            }
        }
        if (node.keywords != null) {
            for (keywordType arg : node.keywords) {
                arg.accept(this);
                kwCount++;
            }
        }
        if (node.starargs != null) {
            node.starargs.accept(this);
        }
        if (node.kwargs != null) {
            node.kwargs.accept(this);
        }
        compiler.visitCall(node.starargs != null, node.kwargs != null, argCount, kwCount);
        return bundle;
    }

    public BytecodeBundle visitCompare(Compare node) throws Exception {
        if (node.comparators.length != node.ops.length) {
        }
        node.left.accept(this);
        if (node.ops.length == 1) {
            node.comparators[0].accept(this);
            compiler.visitCompareOperator(compareOperator(node.ops[0]));
        } else {
            Label fail = new Label();
            int i = 0;
            for (; i < node.ops.length - 1; i++) {
                node.comparators[i].accept(this);
                compiler.visitDup(1);
                compiler.visitRot(3);
                compiler.visitCompareOperator(compareOperator(node.ops[i]));
                compiler.visitJumpIfFalse(fail);
                compiler.visitPop();
            }
            node.comparators[i].accept(this);
            compiler.visitCompareOperator(compareOperator(node.ops[i]));
            Label done = new Label();
            compiler.visitJump(done);
            compiler.visitLabel(fail);
            compiler.visitRot(2);
            compiler.visitPop();
            compiler.visitLabel(done);
        }
        return bundle;
    }

    public BytecodeBundle visitContinue(Continue node) throws Exception {
        compiler.visitContinue(nearestLoop);
        return bundle;
    }

    public BytecodeBundle visitDelete(Delete node) throws Exception {
        for (exprType target : node.targets) {
            target.accept(this);
        }
        lastStatementWasReturn = false;
        return bundle;
    }

    public BytecodeBundle visitDict(Dict node) throws Exception {
        compiler.visitBuildMap(0);
        if (node.keys != null || node.values != null) {
            if (node.values == null || node.keys == null || node.keys.length != node.values.length) {
            }
            for (int i = 0; i < node.keys.length; i++) {
                compiler.visitDup(1);
                node.values[i].accept(this);
                compiler.visitRot(2);
                node.keys[i].accept(this);
                compiler.visitStoreSubscript();
            }
        }
        return bundle;
    }

    public BytecodeBundle visitEllipsis(Ellipsis node) throws Exception {
        compiler.visitLoadConstant(Py.Ellipsis);
        return bundle;
    }

    public BytecodeBundle visitExec(Exec node) throws Exception {
        node.body.accept(this);
        if (node.globals != null) {
            node.globals.accept(this);
        } else {
            compiler.visitLoadConstant(Py.None);
        }
        if (node.locals != null) {
            node.locals.accept(this);
        } else {
            compiler.visitDup(1);
        }
        compiler.visitExec();
        lastStatementWasReturn = false;
        return bundle;
    }

    public BytecodeBundle visitExtSlice(ExtSlice node) throws Exception {
        for (sliceType slice : node.dims) {
            slice.accept(this);
        }
        compiler.visitBuildTuple(node.dims.length);
        return bundle;
    }

    public BytecodeBundle visitFor(For node) throws Exception {
        lastStatementWasReturn = false;
        return bundle;
    }

    public BytecodeBundle visitListComp(ListComp node) throws Exception {
        return bundle;
    }

    public BytecodeBundle visitGlobal(Global node) throws Exception {
        return bundle;
    }

    public BytecodeBundle visitIf(If node) throws Exception {
        ConditionalAction jump;
        exprType test;
        if (node.test instanceof UnaryOp && ((UnaryOp) node.test).op == unaryopType.Not) {
            jump = jumpIfTrue;
            test = ((UnaryOp) node.test).operand;
        } else {
            jump = jumpIfFalse;
            test = node.test;
        }
        Label orelse = new Label();
        test.accept(this);
        jump.perform(compiler, orelse);
        for (stmtType stmt : node.body) {
            stmt.accept(this);
        }
        if (node.orelse != null) {
            Label done = new Label();
            compiler.visitJump(done);
            compiler.visitLabel(orelse);
            for (stmtType stmt : node.orelse) {
                stmt.accept(this);
            }
            compiler.visitLabel(done);
        } else {
            compiler.visitLabel(orelse);
        }
        lastStatementWasReturn = false;
        return bundle;
    }

    public BytecodeBundle visitIfExp(IfExp node) throws Exception {
        node.test.accept(this);
        Label orelse = new Label();
        Label done = new Label();
        compiler.visitJumpIfFalse(orelse);
        compiler.visitPop();
        node.body.accept(this);
        compiler.visitJump(done);
        compiler.visitLabel(orelse);
        compiler.visitPop();
        node.orelse.accept(this);
        compiler.visitLabel(done);
        return bundle;
    }

    public BytecodeBundle visitImport(Import node) throws Exception {
        lastStatementWasReturn = false;
        return bundle;
    }

    public BytecodeBundle visitImportFrom(ImportFrom node) throws Exception {
        lastStatementWasReturn = false;
        return bundle;
    }

    public BytecodeBundle visitIndex(Index node) throws Exception {
        node.value.accept(this);
        return bundle;
    }

    public BytecodeBundle visitList(List node) throws Exception {
        for (exprType value : node.elts) {
            value.accept(this);
        }
        compiler.visitBuildList(node.elts.length);
        return bundle;
    }

    public BytecodeBundle visitName(Name node) throws Exception {
        compiler.visitLoad(currentEnvironment.getVariableContextFor(node.id), node.id);
        return bundle;
    }

    public BytecodeBundle visitNum(Num node) throws Exception {
        PyObject num;
        if (node.n instanceof PyObject) {
            num = (PyObject) node.n;
        } else if (node.n instanceof Integer) {
            num = new PyInteger((Integer) node.n);
        } else if (node.n instanceof Long) {
            num = new PyLong((Long) node.n);
        } else if (node.n instanceof String) {
            num = new PyLong((String) node.n);
        } else {
            num = null;
        }
        compiler.visitLoadConstant(num);
        return bundle;
    }

    public BytecodeBundle visitPass(Pass node) throws Exception {
        lastStatementWasReturn = false;
        return bundle;
    }

    public BytecodeBundle visitPrint(Print node) throws Exception {
        if (node.dest != null) {
            node.dest.accept(this);
        }
        int i = 0;
        int end = node.values.length - (node.nl ? 0 : 1);
        for (; i < end; i++) {
            if (node.dest != null) {
                compiler.visitDup(1);
            }
            node.values[i].accept(this);
            if (node.dest != null) {
                compiler.visitPrintItemTo();
            } else {
                compiler.visitPrintItem();
            }
        }
        if (node.nl) {
            if (node.dest != null) {
                compiler.visitPrintNewlineTo();
            } else {
                compiler.visitPrintNewline();
            }
        } else {
            node.values[i].accept(this);
            if (node.dest != null) {
                compiler.visitPrintItemTo();
            } else {
                compiler.visitPrintItem();
            }
        }
        lastStatementWasReturn = false;
        return bundle;
    }

    public BytecodeBundle visitRaise(Raise node) throws Exception {
        int numArgs = 0;
        if (node.type != null) {
            numArgs++;
            node.type.accept(this);
            if (node.inst != null) {
                numArgs++;
                node.inst.accept(this);
                if (node.tback != null) {
                    numArgs++;
                    node.tback.accept(this);
                }
            }
        }
        compiler.visitRaise(numArgs);
        lastStatementWasReturn = true;
        return bundle;
    }

    public BytecodeBundle visitRepr(Repr node) throws Exception {
        node.value.accept(this);
        compiler.visitUnaryOperator(UnaryOperator.CONVERT);
        return bundle;
    }

    public BytecodeBundle visitReturn(Return node) throws Exception {
        if (node.value != null) {
            node.value.accept(this);
        } else {
            compiler.visitLoadConstant(Py.None);
        }
        compiler.visitReturn();
        lastStatementWasReturn = true;
        return bundle;
    }

    public BytecodeBundle visitSlice(Slice node) throws Exception {
        if (node.lower != null) {
            node.lower.accept(this);
        } else {
            compiler.visitLoadConstant(Py.None);
        }
        if (node.upper != null) {
            node.upper.accept(this);
        } else {
            compiler.visitLoadConstant(Py.None);
        }
        if (node.step != null) {
            node.step.accept(this);
            compiler.visitBuildSlice(3);
        } else {
            compiler.visitBuildSlice(2);
        }
        return bundle;
    }

    public BytecodeBundle visitStr(Str node) throws Exception {
        PyString string;
        if (node.s instanceof PyString) {
            string = (PyString) node.s;
        } else if (node.s instanceof String) {
            string = new PyString((String) node.s);
        } else {
            string = new PyString(node.s.toString());
        }
        compiler.visitLoadConstant(string);
        return bundle;
    }

    public BytecodeBundle visitSubscript(Subscript node) throws Exception {
        node.value.accept(this);
        if (node.slice instanceof Slice) {
            Slice slice = (Slice) node.slice;
            if (slice.step != null) {
                if (slice.lower != null) {
                    slice.lower.accept(this);
                } else {
                    compiler.visitLoadConstant(Py.None);
                }
                if (slice.upper != null) {
                    slice.upper.accept(this);
                } else {
                    compiler.visitLoadConstant(Py.None);
                }
                slice.step.accept(this);
                compiler.visitBuildSlice(3);
            } else {
                SliceMode mode;
                if (slice.lower != null) {
                    slice.lower.accept(this);
                    if (slice.upper != null) {
                        slice.upper.accept(this);
                        mode = SliceMode.PLUS_3;
                    } else {
                        mode = SliceMode.PLUS_1;
                    }
                } else {
                    if (slice.upper != null) {
                        slice.upper.accept(this);
                        mode = SliceMode.PLUS_2;
                    } else {
                        mode = SliceMode.PLUS_0;
                    }
                }
                compiler.visitLoadSlice(mode);
            }
        } else {
            node.slice.accept(this);
            compiler.visitLoadSubscript();
        }
        return bundle;
    }

    public BytecodeBundle visitTryExcept(TryExcept node) throws Exception {
        Label handle = new Label();
        Label after = new Label();
        compiler.visitSetupExcept(handle);
        for (stmtType stmt : node.body) {
            stmt.accept(this);
        }
        compiler.visitPopBlock();
        compiler.visitJump(after);
        Label done = (node.orelse != null) ? new Label() : after;
        Label next = null;
        compiler.visitLabel(handle);
        for (excepthandlerType handler : node.handlers) {
            if (next != null) {
                compiler.visitLabel(next);
                compiler.visitPop();
            }
            if (handler.type != null) {
                next = new Label();
                compiler.visitDup(1);
                handler.type.accept(this);
                compiler.visitCompareOperator(ComparisonOperator.EXCEPTION_MATCH);
                compiler.visitJumpIfFalse(next);
                compiler.visitPop();
                if (handler.name != null) {
                    compiler.visitPop();
                    handler.name.accept(this);
                    compiler.visitPop();
                } else {
                    compiler.visitPop();
                    compiler.visitPop();
                    compiler.visitPop();
                }
            } else {
                compiler.visitPop();
                compiler.visitPop();
                compiler.visitPop();
            }
            for (stmtType stmt : handler.body) {
                stmt.accept(this);
            }
            compiler.visitJump(done);
        }
        if (next != null) {
            compiler.visitLabel(next);
            compiler.visitPop();
            compiler.visitRaise(3);
        }
        compiler.visitEndFinally();
        if (node.orelse != null) {
            compiler.visitLabel(after);
            for (stmtType stmt : node.orelse) {
                stmt.accept(this);
            }
        }
        compiler.visitLabel(done);
        lastStatementWasReturn = false;
        return bundle;
    }

    public BytecodeBundle visitTryFinally(TryFinally node) throws Exception {
        Label handle = new Label();
        compiler.visitSetupFinally(handle);
        for (stmtType stmt : node.body) {
            stmt.accept(this);
        }
        compiler.visitPopBlock();
        compiler.visitLoadConstant(Py.None);
        compiler.visitLabel(handle);
        for (stmtType stmt : node.finalbody) {
            stmt.accept(this);
        }
        compiler.visitEndFinally();
        lastStatementWasReturn = false;
        return bundle;
    }

    public BytecodeBundle visitTuple(Tuple node) throws Exception {
        for (exprType expr : node.elts) {
            expr.accept(this);
        }
        compiler.visitBuildTuple(node.elts.length);
        return bundle;
    }

    public BytecodeBundle visitUnaryOp(UnaryOp node) throws Exception {
        node.operand.accept(this);
        compiler.visitUnaryOperator(unaryOperator(node.op));
        return bundle;
    }

    public BytecodeBundle visitWhile(While node) throws Exception {
        Label start = new Label();
        Label end = new Label();
        Label done = new Label();
        compiler.visitSetupLoop(end);
        compiler.visitLabel(start);
        node.test.accept(this);
        compiler.visitJumpIfFalse(done);
        compiler.visitPop();
        for (stmtType stmt : node.body) {
            stmt.accept(this);
        }
        compiler.visitJump(start);
        compiler.visitLabel(done);
        compiler.visitPop();
        compiler.visitPopBlock();
        if (node.orelse != null) {
            for (stmtType stmt : node.orelse) {
                stmt.accept(this);
            }
        }
        compiler.visitLabel(end);
        lastStatementWasReturn = false;
        return bundle;
    }

    public BytecodeBundle visitWith(With node) throws Exception {
        node.context_expr.accept(this);
        CompilerVariable exitTicket = compiler.storeContextManagerExit();
        CompilerVariable contextVariable = compiler.enterContextManager();
        Label end = new Label();
        compiler.visitSetupFinally(end);
        if (node.optional_vars != null) {
            compiler.loadVariable(contextVariable);
            node.optional_vars.accept(this);
        }
        for (stmtType stmt : node.body) {
            stmt.accept(this);
        }
        compiler.visitPopBlock();
        compiler.visitLoadConstant(Py.None);
        compiler.loadVariable(exitTicket);
        compiler.visitWithCleanup();
        compiler.visitEndFinally();
        lastStatementWasReturn = false;
        return bundle;
    }

    public BytecodeBundle visitYield(Yield node) throws Exception {
        if (node.value != null) {
            node.value.accept(this);
        } else {
            compiler.visitLoadConstant(Py.None);
        }
        YieldPoint yielder = context.getYieldPoint(node);
        compiler.visitYield(yielder.index, yielder.label);
        lastStatementWasReturn = false;
        return bundle;
    }

    private BinaryOperator binaryOperator(operatorType op) {
        switch(op) {
            case Add:
                return BinaryOperator.ADD;
            case BitAnd:
                return BinaryOperator.AND;
            case BitOr:
                return BinaryOperator.OR;
            case BitXor:
                return BinaryOperator.XOR;
            case Div:
                return BinaryOperator.DIVIDE;
            case FloorDiv:
                return BinaryOperator.FLOOR_DIVIDE;
            case LShift:
                return BinaryOperator.SHIFT_LEFT;
            case Mod:
                return BinaryOperator.MODULO;
            case Mult:
                return BinaryOperator.MULTIPLY;
            case Pow:
                return BinaryOperator.POWER;
            case RShift:
                return BinaryOperator.SHIFT_RIGHT;
            case Sub:
                return BinaryOperator.SUBTRACT;
            default:
                return null;
        }
    }

    private ComparisonOperator compareOperator(cmpopType op) {
        switch(op) {
            case Eq:
                return ComparisonOperator.EQUAL;
            case Gt:
                return ComparisonOperator.GREATER_THAN;
            case GtE:
                return ComparisonOperator.GREATER_THAN_OR_EQUAL;
            case In:
                return ComparisonOperator.IN;
            case Is:
                return ComparisonOperator.IS;
            case IsNot:
                return ComparisonOperator.IS_NOT;
            case Lt:
                return ComparisonOperator.LESS_THAN;
            case LtE:
                return ComparisonOperator.LESS_THAN_OR_EQUAL;
            case NotEq:
                return ComparisonOperator.NOT_EQUAL;
            case NotIn:
                return ComparisonOperator.NOT_IN;
            default:
                return null;
        }
    }

    private UnaryOperator unaryOperator(unaryopType op) {
        switch(op) {
            case Invert:
                return UnaryOperator.INVERT;
            case Not:
                return UnaryOperator.NOT;
            case UAdd:
                return UnaryOperator.POSITIVE;
            case USub:
                return UnaryOperator.NEGATIVE;
            default:
                return null;
        }
    }
}
