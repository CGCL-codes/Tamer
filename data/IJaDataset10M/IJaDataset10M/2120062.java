package upm.fi.oeg.sparql.lqp.optimiser;

import java.util.ArrayList;
import java.util.List;
import uk.org.ogsadai.dqp.common.CompilerConfiguration;
import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.dqp.lqp.Annotation;
import uk.org.ogsadai.dqp.lqp.Attribute;
import uk.org.ogsadai.dqp.lqp.AttributeImpl;
import uk.org.ogsadai.dqp.lqp.Heading;
import uk.org.ogsadai.dqp.lqp.HeadingImpl;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.Predicate;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.operators.ProjectOperator;
import uk.org.ogsadai.dqp.lqp.operators.RenameOperator;
import uk.org.ogsadai.dqp.lqp.operators.SelectOperator;
import uk.org.ogsadai.dqp.lqp.operators.SelectProjectJoinTableScanQuery;
import uk.org.ogsadai.dqp.lqp.optimiser.Optimiser;
import uk.org.ogsadai.expression.AndExpression;
import uk.org.ogsadai.expression.ComparisonExpression;
import uk.org.ogsadai.expression.EqualExpression;
import uk.org.ogsadai.expression.Expression;
import uk.org.ogsadai.expression.ExpressionUtils;
import uk.org.ogsadai.expression.ExpressionVisitor;
import uk.org.ogsadai.expression.arithmetic.ArithmeticExpression;
import uk.org.ogsadai.expression.arithmetic.visitors.OperandTypeArithmeticExprVisitor;
import uk.org.ogsadai.expression.arithmetic.visitors.OperandTypeArithmeticExprVisitor.OperandType;
import uk.org.ogsadai.parser.SQLParserException;
import uk.org.ogsadai.parser.sql92query.SQLQueryParser;
import uk.org.ogsadai.resource.dataresource.dqp.RequestDQPFederation;
import uk.org.ogsadai.tuple.TupleTypes;
import uk.org.ogsadai.tuple.join.PrimaryComparisonExtractor;
import upm.fi.oeg.sparql.lqp.RDFTableScanOperator;
import upm.fi.oeg.sparql.lqp.RDFTableScanQuery;
import upm.fi.oeg.sparql.lqp.optimiser.visitor.RDFExpressionVisitor;

/**
 * Optimiser trying to implode all TABLE SCAN operators. It can be configured
 * with one or many <code>selectOnly.resource</code> properties values of which
 * inform about resourceIDs for resources that can accept only select
 * predicates.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class RDFTableScanImplosionOptimiser implements Optimiser {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) Universidad Politecnica de Madrid, 2009";

    OperatorID operation = OperatorID.SELECT;

    /**
     * {@inheritDoc}
     */
    public Operator optimise(Operator lqpRoot, RequestDQPFederation requestFederation, CompilerConfiguration compilerConfiguration) throws LQPException {
        try {
            traverse(lqpRoot);
        } catch (SQLParserException e) {
            e.printStackTrace();
        }
        return lqpRoot;
    }

    /**
     * {@inheritDoc}
     */
    public void addProperty(String key, String value) {
    }

    /**
     * Traverses the LQP and calls implode on all TABLE SCAN operators. Right
     * branches are imploded first.
     * 
     * @param currentOperator
     * @throws LQPException
     * @throws SQLParserException 
     */
    private void traverse(Operator currentOperator) throws LQPException, SQLParserException {
        if (currentOperator.getID() == OperatorID.getInstance("RDF_TABLE_SCAN")) {
            RDFTableScanOperator tsop = (RDFTableScanOperator) currentOperator;
            List<Attribute> headingAttributes = new ArrayList<Attribute>();
            implode(tsop, headingAttributes);
        } else {
            if (currentOperator.isBinary()) {
                traverse(currentOperator.getChild(1));
            }
            traverse(currentOperator.getChild(0));
        }
    }

    /**
     * Implodes parent SELECT, PROJECT and JOIN operators into the
     * SQL query.
     * 
     * @param tableScanOp
     *            table scan operator
     * @param headingAttributes 
     * @throws LQPException if an error occurs 
     * @throws SQLParserException 
     */
    private void implode(RDFTableScanOperator tableScanOp, List<Attribute> headingAttributes) throws LQPException, SQLParserException {
        Operator parent = tableScanOp.getParent();
        RDFTableScanQuery query = (RDFTableScanQuery) tableScanOp.getQuery();
        if (parent.getID() == operation) {
            Predicate pred = ((SelectOperator) parent).getPredicate();
            RDFExpressionVisitor myVisitor = new RDFExpressionVisitor();
            String predicate = "";
            pred.getExpression().accept(myVisitor);
            for (Predicate p : pred.splitConjunction()) {
                p.getExpression().accept(myVisitor);
                String tmpString = "";
                ArithmeticExpression leftExpression = myVisitor.getLeftHandExpression();
                ArithmeticExpression rightExpression = myVisitor.getRighHandExpression();
                tmpString = rightExpression.toString();
                if (leftExpression.toString().contains(tableScanOp.getSource() + "." + "subject")) {
                    if (predicate.equals("")) {
                        predicate = tableScanOp.getSource() + ".subject" + " = " + tmpString;
                    } else {
                    }
                } else if (leftExpression.toString().contains(tableScanOp.getSource() + "." + "predicate")) {
                    if (predicate.equals("")) {
                        predicate = tableScanOp.getSource() + ".predicate" + " = " + tmpString;
                    } else {
                    }
                } else if (leftExpression.toString().contains(tableScanOp.getSource() + "." + "object")) {
                    if (predicate.equals("")) {
                        predicate = tableScanOp.getSource() + ".object" + " = " + tmpString;
                    } else {
                    }
                }
                query.addPredicate(predicate);
                predicate = "";
            }
            parent.getParent().replaceChild(parent, tableScanOp);
            parent.disconnect();
            tableScanOp.getParent().update();
            implode(tableScanOp, headingAttributes);
        }
        if (parent.getID() == OperatorID.PROJECT) {
            ProjectOperator project = (ProjectOperator) parent;
            tableScanOp.setProjectAttributes(project.getOrderedUsedAttributes());
            query.setProjectAttributes(project.getOrderedUsedAttributes());
            parent.getParent().replaceChild(parent, tableScanOp);
            parent.disconnect();
            tableScanOp.getParent().update();
            implode(tableScanOp, headingAttributes);
        }
        List<Attribute> renamedAttributes = new ArrayList<Attribute>();
        if (parent.getID() == OperatorID.RENAME) {
            RenameOperator rename = (RenameOperator) parent;
            for (Attribute attribute : rename.getRenameMap().getOriginalAttributeList()) {
                renamedAttributes.add(rename.getRenameMap().getRenamedAttribute(attribute));
            }
            tableScanOp.setProjectAttributes(renamedAttributes);
            query.setRenameMap(rename.getRenameMap());
            query.setProjectAttributes(renamedAttributes);
            parent.getParent().replaceChild(rename, tableScanOp);
            parent.disconnect();
            tableScanOp.getParent().update();
        }
    }

    public Operator optimise(Operator arg0, RequestDQPFederation arg1, CompilerConfiguration arg2, RequestDetails arg3) throws LQPException {
        try {
            traverse(arg0);
        } catch (SQLParserException e) {
            e.printStackTrace();
        }
        return arg0;
    }
}
