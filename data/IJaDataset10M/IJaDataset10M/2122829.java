package jpdl.accessplan;

import java.util.Stack;
import jpdl.parser.analysis.DepthFirstAdapter;
import jpdl.parser.node.*;
import jpdl.patterns.PatternPredicateMaker;
import jpdl.pointcuts.*;
import jpdl.pointcuts.predicates.PatternPointCut;

public class PlanTreeBuilder extends DepthFirstAdapter {

    public static PlanNode buildPlan(Node ast, ExecutionContext context) {
        PlanTreeBuilder pb = new PlanTreeBuilder(context);
        ast.apply(pb);
        PlanNode startNode = pb.planNodeStack.pop();
        PlanOptimizer po = new PlanOptimizer();
        po.Optimize(startNode);
        return startNode;
    }

    private ExecutionContext context;

    private PlanTreeBuilder(ExecutionContext context) {
        this.context = context;
    }

    private Stack<PlanNode> planNodeStack = new Stack<PlanNode>();

    @Override
    public void outABinaryPrim(ABinaryPrim node) {
        super.outABinaryPrim(node);
        String ident = node.getIdent().getText();
        RelationPlanNode relation = (RelationPlanNode) planNodeStack.peek();
        if (context.containsRelation(ident)) {
            for (RelationPointCut rpc : context.getRelation(ident)) {
                relation.addRelation(rpc);
            }
        } else {
            RelationPointCut.RelationDescriptor<? extends RelationPointCut> desc = RelationPointCut.getRelation(ident);
            relation.addRelation(desc.instance());
        }
    }

    @Override
    public void outAUnaryPrim(AUnaryPrim node) {
        super.outAUnaryPrim(node);
        String ident = node.getIdent().getText();
        if (context.containsPredicate(ident)) {
            planNodeStack.push(context.getPredicate(ident));
        } else {
            PredicatePointCut.PredicateDescriptor<? extends PredicatePointCut> desc = PredicatePointCut.getPredicate(ident);
            PredicatePlanNode planNode = new PredicatePlanNode(context, desc.instance());
            planNodeStack.push(planNode);
        }
    }

    @Override
    public void outAPatternPointcut(APatternPointcut node) {
        try {
            PatternPointCut ppc = PatternPredicateMaker.getPredicate(node.getPattern());
            PredicatePlanNode planNode = new PredicatePlanNode(context, ppc);
            planNodeStack.push(planNode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void outAAndBinaryOperator(AAndBinaryOperator node) {
        super.outAAndBinaryOperator(node);
        PlanNode righPlanNode = planNodeStack.pop();
        PlanNode leftPlanNode = planNodeStack.pop();
        AndPlanNode andNode = new AndPlanNode(context);
        andNode.addChild(leftPlanNode);
        andNode.addChild(righPlanNode);
        planNodeStack.push(andNode);
    }

    @Override
    public void outAOrBinaryOperator(AOrBinaryOperator node) {
        super.outAOrBinaryOperator(node);
        PlanNode righPlanNode = planNodeStack.pop();
        PlanNode leftPlanNode = planNodeStack.pop();
        OrPlanNode orNode = new OrPlanNode(context);
        orNode.addChild(leftPlanNode);
        orNode.addChild(righPlanNode);
        planNodeStack.push(orNode);
    }

    @Override
    public void inARelationPointcut(ARelationPointcut node) {
        super.inARelationPointcut(node);
        RelationGroupPlanNode groupNode = new RelationGroupPlanNode(context);
        planNodeStack.push(groupNode);
    }

    @Override
    public void outARelationPointcut(ARelationPointcut node) {
        super.outARelationPointcut(node);
        PlanNode innerNode = planNodeStack.pop();
        RelationGroupPlanNode outerNode = (RelationGroupPlanNode) planNodeStack.peek();
        outerNode.setInnerNode(innerNode);
    }

    @Override
    public void inAQualifPointcut(AQualifPointcut node) {
        super.inAQualifPointcut(node);
        TQualif qualifToken = node.getQualif();
        Qualifier qualif = Qualifier.EXISTS;
        if (qualifToken.getText().equals("forall")) {
            qualif = Qualifier.FORALL;
        }
        QualifierPlanNode qualifNode = new QualifierPlanNode(context, new QualifierPointCut(qualif));
        planNodeStack.push(qualifNode);
    }

    @Override
    public void outANotUnaryOparator(ANotUnaryOparator node) {
        NotPlanNode notPlanNode = new NotPlanNode(context);
        notPlanNode.setChildPlanNode(planNodeStack.pop());
        planNodeStack.push(notPlanNode);
    }

    @Override
    public void outAQualifPointcut(AQualifPointcut node) {
        super.outAQualifPointcut(node);
        PlanNode innerNode = planNodeStack.pop();
        QualifierPlanNode outerNode = (QualifierPlanNode) planNodeStack.peek();
        outerNode.setInnerNode(innerNode);
    }

    @Override
    public void caseAQualifPointcut(AQualifPointcut node) {
        inAQualifPointcut(node);
        if (node.getQualif() != null) {
            node.getQualif().apply(this);
        }
        if (node.getRelation() != null) {
            node.getRelation().apply(this);
        }
        if (node.getPointcut() != null) {
            node.getPointcut().apply(this);
        }
        outAQualifPointcut(node);
    }

    @Override
    public void caseARelationPointcut(ARelationPointcut node) {
        inARelationPointcut(node);
        if (node.getRelation() != null) {
            node.getRelation().apply(this);
        }
        if (node.getPointcut() != null) {
            node.getPointcut().apply(this);
        }
        outARelationPointcut(node);
    }

    @Override
    public void caseACompositeRelation(ACompositeRelation node) {
        inACompositeRelation(node);
        if (node.getInner() != null) {
            node.getInner().apply(this);
        }
        if (node.getOuter() != null) {
            node.getOuter().apply(this);
        }
        outACompositeRelation(node);
    }

    @Override
    public void caseAUnaryOpPointcut(AUnaryOpPointcut node) {
        inAUnaryOpPointcut(node);
        if (node.getPointcut() != null) {
            node.getPointcut().apply(this);
        }
        if (node.getUnaryOparator() != null) {
            node.getUnaryOparator().apply(this);
        }
        outAUnaryOpPointcut(node);
    }

    @Override
    public void caseABinaryOpPointcut(ABinaryOpPointcut node) {
        inABinaryOpPointcut(node);
        if (node.getLeft() != null) {
            node.getLeft().apply(this);
        }
        if (node.getRight() != null) {
            node.getRight().apply(this);
        }
        if (node.getBinaryOperator() != null) {
            node.getBinaryOperator().apply(this);
        }
        outABinaryOpPointcut(node);
    }
}
