package cat.quadriga.parsers.code.statements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import cat.quadriga.parsers.code.BreakException;
import cat.quadriga.parsers.code.BreakOrContinueException;
import cat.quadriga.parsers.code.CodeZone;
import cat.quadriga.parsers.code.ErrorLog;
import cat.quadriga.parsers.code.SymbolTable;
import cat.quadriga.parsers.code.Utils;
import cat.quadriga.parsers.code.expressions.ExpressionNode;
import cat.quadriga.parsers.code.expressions.dataaccess.LiteralData;
import cat.quadriga.parsers.code.symbols.LocalVariableSymbol;
import cat.quadriga.parsers.code.types.BaseType;
import cat.quadriga.parsers.code.types.PrimitiveTypeRef;
import cat.quadriga.runtime.ComputedValue;
import cat.quadriga.runtime.RuntimeEnvironment;

public class SwitchStatementNode extends StatementNodeClass implements BucleOrSwitchInterface {

    public ExpressionNode input;

    public final List<CaseNode> cases;

    public final List<LocalVariableSymbol> localVariables;

    public SwitchStatementNode(ExpressionNode input, List<CaseNode> cases, List<LocalVariableSymbol> localVariables, CodeZone cz) {
        super(cz);
        this.input = input;
        this.cases = new ArrayList<CaseNode>(cases);
        this.localVariables = new ArrayList<LocalVariableSymbol>(localVariables);
    }

    @Override
    public String[] getOperands() {
        List<String> args = new LinkedList<String>();
        args.add(input.treeStringRepresentation());
        if (localVariables.size() > 0) {
            List<String> vars = new LinkedList<String>();
            for (LocalVariableSymbol var : localVariables) {
                vars.add(var.toString());
            }
            args.add(Utils.treeStringRepresentation("Local Variables", vars.toArray(new String[vars.size()])));
        }
        for (CaseNode cas : cases) {
            args.add(cas.toString());
        }
        return args.toArray(new String[args.size()]);
    }

    @Override
    public String getOperation() {
        return "switch";
    }

    private boolean linked = false;

    @Override
    public StatementNodeClass getLinkedVersion(SymbolTable symbolTable, ErrorLog errorLog) {
        if (linked) return this;
        linked = true;
        if (!input.isCorrectlyLinked()) {
            ExpressionNode newInput = input.getLinkedVersion(symbolTable, errorLog);
            if (newInput == null) {
                linked = false;
            } else {
                input = newInput;
            }
        }
        BaseType inputType = input.getType();
        if (!(inputType instanceof PrimitiveTypeRef) || ((PrimitiveTypeRef) inputType).type != PrimitiveTypeRef.Type.INT) {
            errorLog.insertError("Switch input type not accepted", input);
        }
        symbolTable.newContext();
        BucleOrSwitchInterface prev = symbolTable.closestBucleOrSwitch;
        symbolTable.closestBucleOrSwitch = this;
        for (int i = 0; i < localVariables.size(); ++i) {
            LocalVariableSymbol localVariableSymbol = localVariables.get(i);
            if (localVariableSymbol.type.isValid()) {
                symbolTable.addSymbol(localVariableSymbol);
            } else {
                BaseType type = localVariableSymbol.type.getValid(symbolTable, errorLog);
                if (type == null) {
                    linked = false;
                } else {
                    localVariableSymbol.type = type;
                }
                symbolTable.addSymbol(localVariableSymbol);
            }
        }
        for (int i = 0; i < cases.size(); ++i) {
            CaseNode caseNode = cases.get(i);
            LiteralData lt = caseNode.cas.getCompileTimeConstant();
            if (lt == null) {
                errorLog.insertError("Switch 'case' needs a constant", caseNode.cas);
                linked = false;
            } else {
                caseNode.cas = lt;
            }
            BaseType caseType = caseNode.cas.getType();
            if (!inputType.isAssignableFrom(caseType)) {
                errorLog.insertError("Switch case type not accepted", caseNode.cas);
            }
            for (int j = 0; j < caseNode.statements.size(); ++j) {
                StatementNode stmt = caseNode.statements.get(j);
                if (!stmt.isCorrectlyLinked()) {
                    StatementNode newStatement = stmt.getLinkedVersion(symbolTable, errorLog);
                    if (newStatement == null) {
                        linked = false;
                    } else {
                        caseNode.statements.set(j, newStatement);
                    }
                }
            }
        }
        symbolTable.closestBucleOrSwitch = prev;
        symbolTable.deleteContext();
        if (linked) return this; else return null;
    }

    @Override
    public boolean isCorrectlyLinked() {
        return linked;
    }

    @Override
    public void execute(RuntimeEnvironment runtime) throws BreakOrContinueException {
        try {
            assert isCorrectlyLinked();
            ComputedValue val = input.compute(runtime);
            int v = val.getAsInt();
            runtime.newLocalContext();
            for (LocalVariableSymbol lvs : localVariables) {
                runtime.putLocalVariable(lvs, null);
            }
            try {
                int init = -1;
                for (int i = 0; i < cases.size(); i++) {
                    int c = cases.get(i).cas.compute(runtime).getAsInt();
                    if (c == v) {
                        init = i;
                        break;
                    }
                }
                if (init >= 0) {
                    for (int i = init; i < cases.size(); i++) {
                        for (StatementNode statement : cases.get(i).statements) {
                            statement.execute(runtime);
                        }
                    }
                }
            } catch (BreakException e) {
                if (e.tobreak != this) {
                    runtime.deleteLocalContext();
                    throw e;
                }
            }
            runtime.deleteLocalContext();
        } catch (BreakOrContinueException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error in " + beginLine + ":" + beginColumn + " " + endLine + ":" + endColumn + " " + file, e);
        }
    }

    public static final class CaseNode {

        public ExpressionNode cas;

        public List<StatementNode> statements;

        public CaseNode(ExpressionNode cas, List<StatementNode> statements) {
            this.cas = cas;
            this.statements = new ArrayList<StatementNode>(statements);
        }

        public String toString() {
            List<String> args = new LinkedList<String>();
            if (cas != null) {
                args.add(cas.treeStringRepresentation());
            }
            for (StatementNode statement : statements) {
                args.add(statement.treeStringRepresentation());
            }
            String[] aux = args.toArray(new String[args.size()]);
            String operator;
            if (cas == null) {
                operator = "default";
            } else {
                operator = "case";
            }
            return Utils.treeStringRepresentation(operator, aux);
        }
    }
}
