package org.trdf.trdf4jena.tsparql.algebra;

import com.hp.hpl.jena.query.QueryExecException;
import com.hp.hpl.jena.sparql.algebra.Op;
import com.hp.hpl.jena.sparql.core.Var;
import com.hp.hpl.jena.sparql.engine.ExecutionContext;
import com.hp.hpl.jena.sparql.engine.Plan;
import com.hp.hpl.jena.sparql.engine.QueryIterator;
import com.hp.hpl.jena.sparql.engine.main.QC;
import com.hp.hpl.jena.sparql.serializer.SerializationContext;
import com.hp.hpl.jena.sparql.util.IndentedWriter;
import com.hp.hpl.jena.sparql.util.NodeIsomorphismMap;
import org.trdf.trdf4jena.tsparql.engine.TrustAwareQueryIterator;
import org.trdf.trdf4jena.tsparql.engine.iterator.QueryIterPrTrust;

/**
 * This class represents the trust projection algebra operator.
 * A trust projection operator processes a TRUST AS clause.
 *
 * @author Olaf Hartig
 */
public class OpPrTrust extends OpExt1 {

    private static final String TagPrTrust = "project trust";

    private Var var;

    /**
	 * @param var the query variable of the processed TRUST AS clause
	 * @param subOp the algebra operator that is the child of this trust
	 *              projection operator in the operator tree
	 */
    public OpPrTrust(Var var, Op subOp) {
        super(subOp, TagPrTrust);
        assert var != null;
        this.var = var;
    }

    @Override
    public void output(IndentedWriter out, SerializationContext sCxt) {
        out.print(Plan.startMarker);
        out.print("project trust (?" + var.getVarName() + ")");
        out.ensureStartOfLine();
        out.incIndent();
        subOp.output(out, sCxt);
        out.decIndent();
        out.ensureStartOfLine();
        out.print(Plan.finishMarker);
        out.ensureStartOfLine();
    }

    @Override
    public void output(IndentedWriter out) {
        out.print(Plan.startMarker);
        out.print("project trust (?" + var.getVarName() + ")");
        out.ensureStartOfLine();
        out.incIndent();
        subOp.output(out);
        out.decIndent();
        out.ensureStartOfLine();
        out.print(Plan.finishMarker);
        out.ensureStartOfLine();
    }

    public Op effectiveOp() {
        return subOp;
    }

    public QueryIterator eval(QueryIterator input, ExecutionContext execContext) {
        TrustAwareQueryIterator itChild;
        try {
            itChild = (TrustAwareQueryIterator) QC.execute(subOp, input, execContext);
        } catch (ClassCastException e) {
            throw new QueryExecException("The iterator for the child operator is not trust aware: " + e.getMessage(), e);
        }
        return new QueryIterPrTrust(itChild, var, execContext);
    }

    public String getSubTag() {
        return "project trust";
    }

    public void outputArgs(IndentedWriter out, SerializationContext sCxt) {
        out.print(var.getVarName());
    }

    @Override
    public boolean equalTo(Op otherOp, NodeIsomorphismMap labelMap) {
        if (otherOp == null) return false;
        if (!(otherOp instanceof OpPrTrust)) return false;
        OpPrTrust otherPrTrustOp = (OpPrTrust) otherOp;
        if (!otherPrTrustOp.var.equals(var)) return false;
        return subOp.equalTo(otherPrTrustOp.subOp, labelMap);
    }

    @Override
    public int hashCode() {
        return ((Object) this).hashCode();
    }

    public Op apply(Transform transform, Op subOp) {
        return transform.transform(this, subOp);
    }

    public Op copy(Op subOp) {
        return new OpPrTrust(var, subOp);
    }

    /**
	 * Returns the sub-operation.
	 */
    public Op getSubOp() {
        return subOp;
    }

    /**
	 * Returns the query variable of the processed TRUST AS clause.
	 */
    public Var getVar() {
        return var;
    }
}
