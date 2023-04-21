package org.tzi.use.uml.sys;

import org.tzi.use.uml.mm.MOperation;
import org.tzi.use.uml.ocl.expr.Evaluator;
import org.tzi.use.uml.ocl.expr.Expression;
import org.tzi.use.uml.ocl.expr.VarDecl;
import org.tzi.use.uml.ocl.expr.VarDeclList;
import org.tzi.use.uml.ocl.value.ObjectValue;
import org.tzi.use.uml.ocl.value.Value;
import org.tzi.use.uml.ocl.value.VarBindings;

/**
 * Call of an operation
 *
 * @version     $ProjectVersion: 0.393 $
 * @author      Mark Richters 
 */
public final class MOperationCall {

    private Expression fTargetExpr;

    private MOperation fOp;

    private Expression[] fArgExprs;

    private MObject fTargetObj;

    private Value[] fArgValues;

    private MSystemState fPreState;

    private VarBindings fVarBindings;

    private Value fOptionalResult;

    private boolean fPostconditionsOkOnExit;

    /**
     * Constructs a new operation call object.
     */
    public MOperationCall(Expression target, MOperation op, Expression[] args) {
        fTargetExpr = target;
        fOp = op;
        fArgExprs = args;
    }

    Expression target() {
        return fTargetExpr;
    }

    Expression[] argExprs() {
        return fArgExprs;
    }

    public MOperation operation() {
        return fOp;
    }

    /**
     * Evaluates source and argument expressions, inserts "self" and
     * parameters into local scope.
     */
    void enter(MSystemState state) {
        Evaluator evaluator = new Evaluator();
        Value v = evaluator.eval(fTargetExpr, state, state.system().topLevelBindings());
        ObjectValue objVal = (ObjectValue) v;
        fTargetObj = objVal.value();
        fArgValues = new Value[fArgExprs.length];
        for (int i = 0; i < fArgExprs.length; i++) {
            fArgValues[i] = evaluator.eval(fArgExprs[i], state, state.system().topLevelBindings());
        }
        fVarBindings = new VarBindings();
        VarDeclList params = fOp.paramList();
        for (int i = 0; i < fArgValues.length; i++) {
            VarDecl decl = params.varDecl(i);
            fVarBindings.push(decl.name(), fArgValues[i]);
        }
        fVarBindings.push("self", objVal);
        fPreState = state;
    }

    /**
     * Sets result information on exit.
     */
    void exit(Value optionalResult, boolean postconditionsOkOnExit) {
        fOptionalResult = optionalResult;
        fPostconditionsOkOnExit = postconditionsOkOnExit;
    }

    public MObject targetObject() {
        return fTargetObj;
    }

    /**
     * Returns the argument values passed on entry. The values are
     * available only after a call to enter().
     */
    public Value[] argValues() {
        return fArgValues;
    }

    /**
     * Returns the result after exit. The value returned is null in
     * case the operation did not specify a return value. 
     */
    public Value resultValue() {
        return fOptionalResult;
    }

    /**
     * Returns true if all postconditions were satisfied on exit.
     */
    public boolean postconditionsOkOnExit() {
        return fPostconditionsOkOnExit;
    }

    /**
     * Returns the variable bindings local to this operation. The
     * bindings contain the "self" variable and operation parameters
     * and are available for pre- and postconditions.  
     */
    VarBindings varBindings() {
        return fVarBindings;
    }

    /**
     * Returns the state in which the operation has been entered.
     */
    MSystemState preState() {
        return fPreState;
    }
}
