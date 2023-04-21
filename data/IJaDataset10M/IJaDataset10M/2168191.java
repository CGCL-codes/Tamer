package org.tzi.use.uml.ocl.expr;

import org.tzi.use.uml.al.ALAction;
import org.tzi.use.uml.mm.MClass;
import org.tzi.use.uml.mm.MOperation;
import org.tzi.use.uml.ocl.value.ObjectValue;
import org.tzi.use.uml.ocl.value.UndefinedValue;
import org.tzi.use.uml.ocl.value.Value;
import org.tzi.use.uml.ocl.value.VarBindings;
import org.tzi.use.uml.sys.MObject;
import org.tzi.use.uml.sys.MObjectState;
import org.tzi.use.uml.sys.MSystemException;
import org.tzi.use.util.StringUtil;

/**
 * An operation defined by a class.
 *
 * @version     $ProjectVersion: 0.393 $
 * @author  Mark Richters
 */
public final class ExpObjOp extends Expression {

    private MOperation fOp;

    private Expression[] fArgs;

    public ExpObjOp(MOperation op, Expression[] args) throws ExpInvalidException {
        super(op.resultType());
        fOp = op;
        fArgs = args;
        if (!args[0].type().isObjectType()) throw new ExpInvalidException("Target expression of object operation must have " + "object type, found `" + args[0].type() + "'.");
        VarDeclList params = fOp.paramList();
        if (params.size() != (args.length - 1)) throw new ExpInvalidException("Number of arguments does not match declaration of operation `" + fOp.name() + "'. Expected " + params.size() + " argument(s), found " + (args.length - 1) + ".");
        for (int i = 1; i < args.length; i++) if (!args[i].type().isSubtypeOf(params.varDecl(i - 1).type())) throw new ExpInvalidException("Type mismatch in argument `" + params.varDecl(i - 1).name() + "'. Expected type `" + params.varDecl(i - 1).type() + "', found `" + args[i].type() + "'.");
    }

    /**
     * Evaluates expression and returns result value.
     */
    public Value eval(EvalContext ctx) {
        ctx.enter(this);
        Value res = new UndefinedValue(type());
        Value val = fArgs[0].eval(ctx);
        if (!val.isUndefined()) {
            ObjectValue objVal = (ObjectValue) val;
            MObject obj = objVal.value();
            MObjectState objState = isPre() ? obj.state(ctx.preState()) : obj.state(ctx.postState());
            if (objState != null) {
                MClass cls = obj.cls();
                MOperation op = cls.operation(fOp.name(), true);
                EvalContext newCtx = ctx;
                if (op.expression() == null) {
                    newCtx = new EvalContext(ctx.preState(), ctx.postState(), new VarBindings(), null);
                }
                int debugOldSize = newCtx.varBindings().getStackSize();
                Value debugOldResultVal = newCtx.varBindings().getValue("result");
                int stackSize = pushVarBindings(ctx, newCtx, objVal, op);
                if (op.expression() != null) {
                    Expression opExpr = op.expression();
                    res = opExpr.eval(newCtx);
                } else {
                    ALAction action = op.getAction();
                    try {
                        action.exec(newCtx);
                        res = newCtx.getVarValue("result");
                        assert (op.resultType() == null) || (res != null);
                    } catch (MSystemException e) {
                        throw new RuntimeException(e);
                    }
                }
                popVarBindings(newCtx, stackSize);
                assert newCtx.varBindings().getStackSize() == debugOldSize;
                assert debugOldResultVal == newCtx.varBindings().getValue("result");
            }
        }
        ctx.exit(this, res);
        return res;
    }

    private void popVarBindings(EvalContext ctx, int oldStackSize) {
        while (ctx.varBindings().getStackSize() > oldStackSize) ctx.popVarBinding();
    }

    private int pushVarBindings(EvalContext oldCtx, EvalContext newCtx, ObjectValue self, MOperation op) {
        int oldStackSize = newCtx.varBindings().getStackSize();
        Value[] argValues = new Value[fArgs.length - 1];
        for (int i = 0; i < fArgs.length - 1; i++) argValues[i] = fArgs[i + 1].eval(oldCtx);
        VarDeclList params = op.paramList();
        for (int i = 0; i < fArgs.length - 1; i++) {
            VarDecl decl = params.varDecl(i);
            newCtx.pushVarBinding(decl.name(), argValues[i]);
        }
        if (op.getAction() != null && op.resultType() != null) {
            newCtx.pushVarBinding("result", new UndefinedValue(op.resultType()));
        }
        newCtx.pushVarBinding("self", self);
        return oldStackSize;
    }

    public String toString() {
        return fArgs[0] + "." + fOp.name() + atPre() + "(" + StringUtil.fmtSeq(fArgs, 1, ", ") + ")";
    }

    public MOperation getOperation() {
        return fOp;
    }

    public Expression[] getArguments() {
        return fArgs;
    }
}
