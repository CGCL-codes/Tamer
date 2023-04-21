package at.jku.semwiq.mediator.engine.op;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import com.hp.hpl.jena.query.SortCondition;
import com.hp.hpl.jena.sparql.algebra.Op;
import com.hp.hpl.jena.sparql.algebra.OpVars;
import com.hp.hpl.jena.sparql.algebra.OpVisitorByType;
import com.hp.hpl.jena.sparql.algebra.Transform;
import com.hp.hpl.jena.sparql.algebra.TransformBase;
import com.hp.hpl.jena.sparql.algebra.op.Op0;
import com.hp.hpl.jena.sparql.algebra.op.Op1;
import com.hp.hpl.jena.sparql.algebra.op.Op2;
import com.hp.hpl.jena.sparql.algebra.op.OpConditional;
import com.hp.hpl.jena.sparql.algebra.op.OpDiff;
import com.hp.hpl.jena.sparql.algebra.op.OpExt;
import com.hp.hpl.jena.sparql.algebra.op.OpJoin;
import com.hp.hpl.jena.sparql.algebra.op.OpLabel;
import com.hp.hpl.jena.sparql.algebra.op.OpLeftJoin;
import com.hp.hpl.jena.sparql.algebra.op.OpN;
import com.hp.hpl.jena.sparql.algebra.op.OpOrder;
import com.hp.hpl.jena.sparql.algebra.op.OpProject;
import com.hp.hpl.jena.sparql.algebra.op.OpUnion;
import com.hp.hpl.jena.sparql.core.Var;
import com.hp.hpl.jena.sparql.util.SetUtils;

/**
 * a visitor-based walker which applies top-down transformations
 * keeps child-2-parent relationships in a flat identity hash table
 * 
 * using the Transform interface but ignoring the supplied sub ops since
 * this is top-down, dynamically iterating using another dispatcher visitor
 * 
 */
public class ProjectPushdown extends TransformBase {

    private ApplyPushDown apply = new ApplyPushDown(this);

    /** 
	 * keeps parent relationships above currently processed op in a flat lookup table
	 * subsequently build by dispatcher
	 * 
	 * built by dispatcher and updated by switchChild(prev, new)
	 * 
	 * Any changes (e.g. insert) to a sub tree below the currently transformed op don't need special care regarding the child2parent lookup table.
	 * Also okay:
	 *  - remove single op above: just a call to switchChild(op, op.getSubOp()) -> references are updated
	 *  - 
	 */
    protected final IdentityHashMap<Op, Op> child2parent = new IdentityHashMap<Op, Op>();

    public static Op apply(Op op) {
        throw new RuntimeException("untested");
    }

    public Op applyTransformations(Op op) {
        OpLabel root = (OpLabel) OpLabel.create("root dummy", op);
        root.visit(apply);
        return root.getSubOp();
    }

    /**
	 * push project beyond order (required to go down further below joins, etc. when order is used)
	 * must not drop variables from order conditions
	 */
    @Override
    public Op transform(OpOrder opOrder, Op subOp) {
        OpProject proj = getProjectParent(opOrder);
        if (proj != null) {
            Set<Var> origProjVars = new HashSet<Var>();
            origProjVars.addAll(proj.getVars());
            Set<Var> vars = new HashSet<Var>();
            vars.addAll(origProjVars);
            vars.addAll(getSortConditionVars(opOrder.getConditions()));
            Set<Var> scopeVars = OpVars.allVars(subOp);
            Set<Var> projVars = SetUtils.intersection(vars, scopeVars);
            if (projVars.size() < scopeVars.size()) {
                List<Var> projVarsList = new ArrayList<Var>();
                projVarsList.addAll(projVars);
                opOrder.setSubOp(new OpProject(subOp, projVarsList));
            }
            if (projVars.equals(origProjVars)) switchChild(proj, opOrder);
            return opOrder;
        } else return super.transform(opOrder, subOp);
    }

    /**
	 * @param proj
	 * @param op2
	 * @param left
	 * @param right
	 * @return
	 */
    private Op transformOp2(OpProject proj, Op2 op2, Op left, Op right) {
        Set<Var> origProjVars = new HashSet<Var>();
        origProjVars.addAll(proj.getVars());
        Set<Var> vars = new HashSet<Var>();
        vars.addAll(origProjVars);
        if (op2 instanceof OpJoin) {
            if (((OpJoin) op2).getExprs() != null) vars.addAll(((OpJoin) op2).getExprs().getVarsMentioned());
        } else if (op2 instanceof OpLeftJoin) {
            if (((OpLeftJoin) op2).getExprs() != null) vars.addAll(((OpLeftJoin) op2).getExprs().getVarsMentioned());
        }
        Set<Var> leftScope = OpVars.allVars(left);
        Set<Var> rightScope = OpVars.allVars(right);
        Set<Var> leftProj = SetUtils.intersection(vars, leftScope);
        Set<Var> rightProj = SetUtils.intersection(vars, rightScope);
        if (leftProj.size() < leftScope.size()) {
            List<Var> leftVars = new ArrayList<Var>();
            leftVars.addAll(leftProj);
            op2.setLeft(new OpProject(left, leftVars));
        }
        if (rightProj.size() < rightScope.size()) {
            List<Var> rightVars = new ArrayList<Var>();
            rightVars.addAll(rightProj);
            op2.setRight(new OpProject(right, rightVars));
        }
        Set<Var> both = new HashSet<Var>();
        both.addAll(leftProj);
        both.addAll(rightProj);
        if (both.equals(origProjVars)) switchChild(proj, op2);
        return op2;
    }

    @Override
    public Op transform(OpJoin opJoin, Op left, Op right) {
        OpProject proj = getProjectParent(opJoin);
        if (proj != null) return transformOp2(proj, opJoin, left, right); else return super.transform(opJoin, left, right);
    }

    @Override
    public Op transform(OpLeftJoin opLeftJoin, Op left, Op right) {
        OpProject proj = getProjectParent(opLeftJoin);
        if (proj != null) return transformOp2(proj, opLeftJoin, left, right); else return super.transform(opLeftJoin, left, right);
    }

    @Override
    public Op transform(OpUnion opUnion, Op left, Op right) {
        OpProject proj = getProjectParent(opUnion);
        if (proj != null) return transformOp2(proj, opUnion, left, right); else return super.transform(opUnion, left, right);
    }

    @Override
    public Op transform(OpConditional opCond, Op left, Op right) {
        OpProject proj = getProjectParent(opCond);
        if (proj != null) return transformOp2(proj, opCond, left, right); else return super.transform(opCond, left, right);
    }

    @Override
    public Op transform(OpDiff opDiff, Op left, Op right) {
        OpProject proj = getProjectParent(opDiff);
        if (proj != null) return transformOp2(proj, opDiff, left, right); else return super.transform(opDiff, left, right);
    }

    /**
	 * @param conditions
	 * @return
	 */
    private Set<Var> getSortConditionVars(List<SortCondition> conditions) {
        Set<Var> vars = new HashSet<Var>();
        for (Iterator<SortCondition> it = conditions.iterator(); it.hasNext(); ) {
            SortCondition c = it.next();
            vars.addAll(c.getExpression().getVarsMentioned());
        }
        return vars;
    }

    /**
	 * get parent of op, never null cause we are using an OpLabel dummy root
	 * @param op
	 * @return
	 */
    public Op getParent(Op op) {
        return child2parent.get(op);
    }

    /**
	 * get parent if it's a project
	 * @param op
	 * @return
	 */
    public OpProject getProjectParent(Op op) {
        Op p = child2parent.get(op);
        if (p instanceof OpProject) return (OpProject) p; else return null;
    }

    /** 
	 * set child of previous to newChild
	 * 
	 * @param previous the old child of an operator
	 * @param newChild the new child to set
	 * @return parent
	 */
    private Op switchChild(Op previous, Op newChild) {
        Op parent = getParent(previous);
        if (parent instanceof Op1) ((Op1) parent).setSubOp(newChild); else if (parent instanceof Op2) {
            if (((Op2) parent).getLeft().equals(previous)) ((Op2) parent).setLeft(newChild); else if (((Op2) parent).getRight().equals(previous)) ((Op2) parent).setRight(newChild); else throw new RuntimeException("Illegal operation: previous was not a child of parent.");
        } else if (parent instanceof OpN) {
            List<Op> newSubs = new ArrayList<Op>();
            for (Op sub : ((OpN) parent).getElements()) {
                if (sub.equals(previous)) newSubs.add(newChild); else newSubs.add(sub);
            }
            Op newOpN = ((OpN) parent).copy(newSubs);
            Op grandParent = getParent(parent);
            switchChild(parent, newOpN);
        } else throw new RuntimeException("Illegal operation: invalid parent operator type.");
        child2parent.remove(previous);
        child2parent.put(newChild, parent);
        return parent;
    }

    class ApplyPushDown extends OpVisitorByType {

        private Transform transform;

        /** need another visitor as dispatcher after applying a top-down transform operation */
        private OpVisitorByType dispatcher;

        public ApplyPushDown(Transform transform) {
            this.transform = transform;
            final ApplyPushDown walker = this;
            dispatcher = new OpVisitorByType() {

                @Override
                protected void visit1(Op1 op) {
                    child2parent.put(op.getSubOp(), op);
                    op.getSubOp().visit(walker);
                }

                @Override
                protected void visit2(Op2 op) {
                    child2parent.put(op.getLeft(), op);
                    child2parent.put(op.getRight(), op);
                    op.getLeft().visit(walker);
                }

                @Override
                protected void visitN(OpN op) {
                    for (Op sub : op.getElements()) {
                        child2parent.put(sub, op);
                        sub.visit(walker);
                    }
                }

                @Override
                protected void visit0(Op0 op) {
                }

                @Override
                protected void visitExt(OpExt op) {
                }
            };
        }

        @Override
        protected void visit0(Op0 op) {
            Op x = op.apply(transform);
            x.visit(dispatcher);
        }

        @Override
        protected void visit1(Op1 op) {
            Op x = op.apply(transform, op.getSubOp());
            x.visit(dispatcher);
        }

        @Override
        protected void visit2(Op2 op) {
            Op x = op.apply(transform, op.getLeft(), op.getRight());
            x.visit(dispatcher);
        }

        @Override
        protected void visitN(OpN op) {
            Op x = op.apply(transform, op.getElements());
            x.visit(dispatcher);
        }

        @Override
        protected void visitExt(OpExt op) {
        }
    }
}
