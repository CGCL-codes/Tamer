package net.sf.orcc.backends.vhdl.transformations;

import java.util.List;
import org.eclipse.emf.ecore.util.EcoreUtil;
import net.sf.orcc.ir.ExprBinary;
import net.sf.orcc.ir.ExprBool;
import net.sf.orcc.ir.ExprFloat;
import net.sf.orcc.ir.ExprInt;
import net.sf.orcc.ir.ExprList;
import net.sf.orcc.ir.ExprString;
import net.sf.orcc.ir.ExprUnary;
import net.sf.orcc.ir.ExprVar;
import net.sf.orcc.ir.Expression;
import net.sf.orcc.ir.InstAssign;
import net.sf.orcc.ir.InstReturn;
import net.sf.orcc.ir.InstStore;
import net.sf.orcc.ir.Instruction;
import net.sf.orcc.ir.IrFactory;
import net.sf.orcc.ir.Node;
import net.sf.orcc.ir.NodeBlock;
import net.sf.orcc.ir.NodeIf;
import net.sf.orcc.ir.NodeWhile;
import net.sf.orcc.ir.OpBinary;
import net.sf.orcc.ir.OpUnary;
import net.sf.orcc.ir.Type;
import net.sf.orcc.ir.Var;
import net.sf.orcc.ir.impl.IrFactoryImpl;
import net.sf.orcc.ir.util.AbstractActorVisitor;
import net.sf.orcc.ir.util.IrUtil;
import net.sf.orcc.util.EcoreHelper;

/**
 * This class defines an actor transformation that transforms assignments whose
 * right hand side is a boolean expression to if nodes. Note: this
 * transformation must be called after PhiRemoval because it generates non-SSA
 * code.
 * 
 * <p>
 * The algorithm works as follows: Considering a block with instructions [i1,
 * i2, ..., ii, ..., in] where the instruction <code>ii</code> is an assign or a
 * store whose value is a boolean binary/unary expression, then create an NodeIf
 * after the current block that assigns <code>true</code> to the target if
 * <code>true</code>, and assigns <code>false</code> otherwise.
 * </p>
 * 
 * <p>
 * The remaining instructions <code>i(i+1)</code> to <code>in</code> are moved
 * to a new block created after the newly-created NodeIf. The
 * <code>previous</code> method is called on the node iterator so that the new
 * block is to be visited next.
 * </p>
 * 
 * @author Matthieu Wipliez
 * @author Nicolas Siret
 * 
 */
public class BoolExprTransformation extends AbstractActorVisitor<Expression> {

    private boolean negateCondition;

    @Override
    public Expression caseExprBinary(ExprBinary expr) {
        Expression e1 = doSwitch(expr.getE1());
        Expression e2 = doSwitch(expr.getE2());
        OpBinary op = negateCondition ? expr.getOp().getInverse() : expr.getOp();
        Type type = expr.getType();
        return IrFactory.eINSTANCE.createExprBinary(e1, op, e2, type);
    }

    @Override
    public Expression caseExprBool(ExprBool expr) {
        return expr;
    }

    @Override
    public Expression caseExprFloat(ExprFloat expr) {
        return expr;
    }

    @Override
    public Expression caseExprInt(ExprInt expr) {
        return expr;
    }

    @Override
    public Expression caseExprList(ExprList expr) {
        return expr;
    }

    @Override
    public Expression caseExprString(ExprString expr) {
        return expr;
    }

    @Override
    public Expression caseExprUnary(ExprUnary expr) {
        OpUnary op = expr.getOp();
        Expression subExpr = expr.getExpr();
        if (op == OpUnary.LOGIC_NOT) {
            negateCondition = true;
            Expression result = doSwitch(subExpr);
            negateCondition = false;
            return result;
        } else {
            return IrFactory.eINSTANCE.createExprUnary(op, doSwitch(subExpr), EcoreUtil.copy(expr.getType()));
        }
    }

    @Override
    public Expression caseExprVar(ExprVar expr) {
        if (expr.getType().isBool()) {
            return IrFactory.eINSTANCE.createExprBinary(expr, OpBinary.EQ, IrFactory.eINSTANCE.createExprBool(!negateCondition), IrFactory.eINSTANCE.createTypeBool());
        } else {
            return expr;
        }
    }

    @Override
    public Expression caseInstAssign(InstAssign assign) {
        Var target = assign.getTarget().getVariable();
        if (target.getType().isBool()) {
            Expression expr = assign.getValue();
            if (expr.isBinaryExpr() || expr.isUnaryExpr()) {
                createIfNode(assign, target, expr);
                NodeBlock block = assign.getBlock();
                IrUtil.delete(assign);
                createNewBlock(block);
            }
        }
        return null;
    }

    @Override
    public Expression caseInstReturn(InstReturn returnInstr) {
        if (procedure.getReturnType().isBool()) {
            Expression expr = returnInstr.getValue();
            if (expr.isBinaryExpr() || expr.isUnaryExpr()) {
                Var local = newVariable();
                returnInstr.setValue(IrFactory.eINSTANCE.createExprVar(local));
                createIfNode(returnInstr, local, expr);
                createNewBlock(returnInstr.getBlock());
            }
        }
        return null;
    }

    @Override
    public Expression caseInstStore(InstStore store) {
        Var target = store.getTarget().getVariable();
        if (target.getType().isBool()) {
            Expression expr = store.getValue();
            if (expr.isBinaryExpr() || expr.isUnaryExpr()) {
                Var local = newVariable();
                store.setValue(IrFactory.eINSTANCE.createExprVar(local));
                createIfNode(store, local, expr);
                createNewBlock(store.getBlock());
            }
        }
        return null;
    }

    @Override
    public Expression caseNodeIf(NodeIf nodeIf) {
        nodeIf.setCondition(doSwitch(nodeIf.getCondition()));
        super.caseNodeIf(nodeIf);
        return null;
    }

    @Override
    public Expression caseNodeWhile(NodeWhile nodeWhile) {
        nodeWhile.setCondition(doSwitch(nodeWhile.getCondition()));
        super.caseNodeWhile(nodeWhile);
        return null;
    }

    /**
	 * Creates an "if" node that assign <code>true</code> or <code>false</code>
	 * to <code>target</code> if the given expression is <code>true</code>,
	 * respectively <code>false</code>/
	 * 
	 * @param target
	 *            target local variable
	 * @param expr
	 *            an expression
	 */
    private void createIfNode(Instruction instruction, Var target, Expression expr) {
        NodeIf nodeIf = IrFactoryImpl.eINSTANCE.createNodeIf();
        nodeIf.setCondition(expr);
        nodeIf.setJoinNode(IrFactoryImpl.eINSTANCE.createNodeBlock());
        NodeBlock block = IrFactoryImpl.eINSTANCE.createNodeBlock();
        nodeIf.getThenNodes().add(block);
        InstAssign assign = IrFactory.eINSTANCE.createInstAssign(target, IrFactory.eINSTANCE.createExprBool(true));
        block.add(assign);
        block = IrFactoryImpl.eINSTANCE.createNodeBlock();
        nodeIf.getElseNodes().add(block);
        assign = IrFactory.eINSTANCE.createInstAssign(target, IrFactory.eINSTANCE.createExprBool(false));
        block.add(assign);
        List<Node> nodes = EcoreHelper.getContainingList(EcoreHelper.getContainerOfType(instruction, Node.class));
        nodes.add(indexNode + 1, nodeIf);
    }

    /**
	 * Creates a new block node that will contain the remaining instructions of
	 * the block that is being visited. The new block is added after the NodeIf.
	 * 
	 * @param iit
	 *            list iterator
	 */
    private void createNewBlock(NodeBlock block) {
        NodeBlock targetBlock = IrFactoryImpl.eINSTANCE.createNodeBlock();
        List<Node> nodes = EcoreHelper.getContainingList(block);
        nodes.add(indexNode + 2, targetBlock);
        List<Instruction> instructions = block.getInstructions();
        targetBlock.getInstructions().addAll(instructions.subList(indexInst, instructions.size()));
    }

    /**
	 * Returns a new boolean local variable.
	 * 
	 * @return a new boolean local variable
	 */
    private Var newVariable() {
        return procedure.newTempLocalVariable(IrFactory.eINSTANCE.createTypeBool(), "bool_expr");
    }
}
