package at.langegger.xlwrap.exec;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import at.langegger.xlwrap.common.Utils;
import at.langegger.xlwrap.common.XLWrapException;
import at.langegger.xlwrap.map.MapTemplate;
import at.langegger.xlwrap.map.XLExprDatatype;
import at.langegger.xlwrap.map.expr.E_RangeRef;
import at.langegger.xlwrap.map.expr.TypeCast;
import at.langegger.xlwrap.map.expr.XLExpr;
import at.langegger.xlwrap.map.expr.XLExpr1;
import at.langegger.xlwrap.map.expr.XLExpr2;
import at.langegger.xlwrap.map.expr.XLExprVisitor;
import at.langegger.xlwrap.map.expr.XLExprWalker;
import at.langegger.xlwrap.map.expr.func.XLExprFunction;
import at.langegger.xlwrap.map.range.AnyRange;
import at.langegger.xlwrap.map.range.Range;
import at.langegger.xlwrap.map.transf.Transformation;
import at.langegger.xlwrap.spreadsheet.XLWrapEOFException;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

/**
 * @author dorgon
 *
 * A transformation stage produces multiple template models for each iteration
 * in a cascaded transformation specification.
 * 
 * It is organized as a cascaded sequence of multiple iterable transformation stages whose
 * implementation of the TransformExector interface is implemented by the actual transform operation.
 * 
 */
public abstract class TransformationStage implements TransformationExector {

    private static final Logger log = LoggerFactory.getLogger(TransformationStage.class);

    /** parent stage (null for root) */
    protected TransformationStage parent = null;

    /** sub stage (null for leaf) */
    protected TransformationStage sub = null;

    /** the execution context */
    protected final ExecutionContext context;

    /** template model of current stage */
    private Model stageTmpl;

    /** this = stage restriction + base restrictions of subs */
    private Map<TransformationStage, Range> restrictions;

    /** this = stage condition + base conditions of subs */
    private Map<TransformationStage, XLExpr> conditions;

    /**
	 * constructor
	 */
    protected TransformationStage(ExecutionContext context) {
        this.context = context;
        this.restrictions = new Hashtable<TransformationStage, Range>();
        this.conditions = new Hashtable<TransformationStage, XLExpr>();
    }

    /**
	 * creates a cascaded sequence of TransformationStages
	 * 
	 * @param context
	 * @return
	 * @throws XLWrapException 
	 */
    public static TransformationStage create(ExecutionContext context) throws XLWrapException {
        MapTemplate activeTmpl = context.getActiveTemplate();
        TransformationStage exec = null;
        TransformationStage parent = null;
        List<Transformation> t = activeTmpl.getTransformations();
        Transformation transf;
        if (t.size() > 0) {
            for (int i = t.size() - 1; i >= 0; i--) {
                transf = t.get(i);
                exec = transf.getExecutor(context);
                exec.initStage(activeTmpl.getTemplateModel(), transf.getRestriction(), transf.getCondition());
                if (parent != null) parent.sub = exec;
                exec.parent = parent;
                parent = exec;
            }
            exec.setSubRestrictionsAndConditions(new Hashtable<TransformationStage, Range>(), new Hashtable<TransformationStage, XLExpr>());
            return exec;
        } else return null;
    }

    /**
	 * set restrictions and conditions of sub stages (bottom-up recursion)
	 * 
	 * @param baseRestrictionsOfSubs collected restrictions of subs
	 * @param baseConditionsOfSubs collected conditions of subs
	 */
    private void setSubRestrictionsAndConditions(Map<TransformationStage, Range> baseRestrictionsOfSubs, Map<TransformationStage, XLExpr> baseConditionsOfSubs) {
        for (TransformationStage exec : baseRestrictionsOfSubs.keySet()) {
            restrictions.put(exec, baseRestrictionsOfSubs.get(exec).copy());
            conditions.put(exec, baseConditionsOfSubs.get(exec).copy());
        }
        baseRestrictionsOfSubs.put(this, getStageRestriction());
        baseConditionsOfSubs.put(this, getStageCondition());
        if (parent != null) parent.setSubRestrictionsAndConditions(baseRestrictionsOfSubs, baseConditionsOfSubs);
    }

    /**
	 * 
	 * @param stageTmpl
	 * @param stageRestriction
	 * @param stageCondition
	 * @throws XLWrapException
	 */
    private void initStage(Model stageTmpl, Range stageRestriction, XLExpr stageCondition) throws XLWrapException {
        this.stageTmpl = Utils.copyModel(stageTmpl);
        setStageRestriction(stageRestriction.copy());
        setStageCondition(stageCondition.copy());
        init();
        proceed();
    }

    /**
	 * proceed if there are more templates in this or higher stages, otherwise return false
	 * also checks condition and continues with next higher stage if false
	 * 
	 * @return true if this has more or any parent has more transformations left
	 * @throws XLWrapException 
	 */
    public boolean proceed() throws XLWrapException {
        if (hasMoreTransformations() && applyTransformation() && conditionTrue(getStageCondition(), context)) {
            return true;
        } else {
            if (parent != null) {
                if (parent.proceed()) {
                    stageTmpl = Utils.copyModel(parent.getStageTemplate());
                    for (TransformationStage key : restrictions.keySet()) {
                        restrictions.put(key, parent.restrictions.get(key).copy());
                        conditions.put(key, parent.conditions.get(key).copy());
                    }
                    init();
                    proceed();
                    return true;
                }
            }
        }
        return false;
    }

    /**
	 * transform the stage template, restriction, and condition
	 * return false if an expression is out of sheet bounds
	 * 
	 * @throws XLWrapException 
	 */
    private boolean applyTransformation() throws XLWrapException {
        Range stageRestriction = getStageRestriction();
        XLExprVisitor exprTransformer = new TransformRangeReferences(this, stageRestriction);
        SheetBoundsChecker boundsCheck = new SheetBoundsChecker(context);
        stageTmpl = Utils.copyModel(stageTmpl);
        Statement st;
        Node o;
        XLExpr expr;
        XLExpr transformed;
        List<ObjectChange> changes = new ArrayList<ObjectChange>();
        StmtIterator sIt = stageTmpl.listStatements();
        while (sIt.hasNext()) {
            st = sIt.nextStatement();
            o = st.getObject().asNode();
            expr = Utils.getExpression(o);
            if (expr != null) {
                transformed = expr.copy();
                XLExprWalker.walkPostOrder(transformed, exprTransformer);
                if (!boundsCheck.withinSheetBounds(transformed)) {
                    log.debug("At least one expression out of sheet bounds, proceeding with next stage.");
                    return false;
                }
                changes.add(new ObjectChange(st, stageTmpl.createTypedLiteral(transformed, XLExprDatatype.instance)));
                if (log.isTraceEnabled()) log.trace(expr + " => " + transformed);
            }
        }
        for (ObjectChange ch : changes) ch.statement.changeObject(ch.object);
        sIt.close();
        XLExprWalker.walkPostOrder(getStageCondition(), new TransformRangeReferences(this, stageRestriction));
        Range prevRestriction = stageRestriction.copy();
        setStageRestriction(transform(stageRestriction, AnyRange.INSTANCE));
        if (sub != null) transformSubRestrictionsAndConditions(sub, prevRestriction);
        return true;
    }

    /**
	 * @param key
	 * @param restriction
	 * @throws XLWrapException 
	 */
    private void transformSubRestrictionsAndConditions(TransformationStage key, Range restriction) throws XLWrapException {
        XLExprWalker.walkPostOrder(conditions.get(key), new TransformRangeReferences(this, restriction));
        restrictions.put(key, transform(restrictions.get(key), restriction));
        if (key.sub != null) transformSubRestrictionsAndConditions(key.sub, restriction);
    }

    /**
	 * get transformed template model
	 * 
	 * @return template model
	 */
    public Model getStageTemplate() {
        return stageTmpl;
    }

    public Range getStageRestriction() {
        return restrictions.get(this);
    }

    public void setStageRestriction(Range restriction) {
        restrictions.put(this, restriction);
    }

    public XLExpr getStageCondition() {
        return conditions.get(this);
    }

    public void setStageCondition(XLExpr condition) {
        conditions.put(this, condition);
    }

    /**
	 * common method for checking the transformation condition,
	 * 
	 * @param condition
	 * @param context
	 * @return
	 * @throws XLWrapException 
	 */
    public boolean conditionTrue(XLExpr condition, ExecutionContext context) throws XLWrapException {
        try {
            return TypeCast.toBoolean(condition.eval(context), context);
        } catch (XLWrapEOFException e) {
            log.warn("End of spreadsheet file reached while evaluating condition " + condition + ", assuming false.");
            return false;
        }
    }

    /**
	 * @return a status string
	 */
    public abstract String getThisStatus();

    @Override
    public String toString() {
        return ((parent != null) ? parent + "\n   ^\n   |\n" : "") + getThisStatus();
    }

    /**
	 * a simple data structure to track object changes
	 * 
	 * @author dorgon
	 */
    private class ObjectChange {

        Statement statement;

        RDFNode object;

        public ObjectChange(Statement s, RDFNode o) {
            this.statement = s;
            this.object = o;
        }
    }

    class SheetBoundsChecker {

        private ExecutionContext context;

        /**
		 * @param context
		 */
        public SheetBoundsChecker(ExecutionContext context) {
            this.context = context;
        }

        public boolean withinSheetBounds(XLExpr expr) {
            if (expr instanceof E_RangeRef) {
                Range r = ((E_RangeRef) expr).getRange();
                if (!r.withinSheetBounds(context)) return false;
            } else if (expr instanceof XLExpr1) {
                withinSheetBounds(((XLExpr1) expr).getArg());
            } else if (expr instanceof XLExpr2) {
                withinSheetBounds(((XLExpr2) expr).getArg1());
                withinSheetBounds(((XLExpr2) expr).getArg2());
            } else if (expr instanceof XLExprFunction) {
                for (XLExpr arg : ((XLExprFunction) expr).getArgs()) withinSheetBounds(arg);
            }
            return true;
        }
    }
}
