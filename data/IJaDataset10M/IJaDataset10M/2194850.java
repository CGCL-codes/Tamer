package ch.ethz.mxquery.query.parser;

import ch.ethz.mxquery.contextConfig.Context;
import ch.ethz.mxquery.datamodel.types.TypeInfo;
import ch.ethz.mxquery.exceptions.MXQueryException;
import ch.ethz.mxquery.iterators.GFLWORIterator;
import ch.ethz.mxquery.iterators.GroupByIterator;
import ch.ethz.mxquery.iterators.PFFLWORIterator;
import ch.ethz.mxquery.iterators.PGFLWORIterator;
import ch.ethz.mxquery.iterators.PGroupBy;
import ch.ethz.mxquery.iterators.VariableIterator;
import ch.ethz.mxquery.iterators.forseq.ForseqIterator;
import ch.ethz.mxquery.iterators.forseq.ForseqWindowEarlyBindingParallel;
import ch.ethz.mxquery.model.Iterator;
import ch.ethz.mxquery.model.WindowVariable;
import ch.ethz.mxquery.model.XDMIterator;

/**
 * Extended version of the parser that enables features and optimizations that
 * are only available on J2SE 1.5 and higher
 * 
 * @author Peter M. Fischer
 * 
 */
public class J56Parser extends SEParser {

    protected Iterator generateFLWOR(XDMIterator where, XDMIterator ret, Context outerFFLWORScope, boolean containsForseq, XDMIterator[] iters) throws MXQueryException {
        Iterator flwor;
        if (co.isParallelExecution()) {
            if (fflworIndex == 0 && containsForseq && ret.getExpressionCategoryType(co.isScripting()) == XDMIterator.EXPR_CATEGORY_SIMPLE) {
                flwor = new PFFLWORIterator(outerFFLWORScope, iters, where, ret, getCurrentLoc());
            } else {
                flwor = new PGFLWORIterator(outerFFLWORScope, iters, where, ret, getCurrentLoc());
            }
        } else {
            flwor = new GFLWORIterator(outerFFLWORScope, iters, ret, getCurrentLoc(), false);
        }
        return flwor;
    }

    protected ForseqIterator generateForseqIterator(ch.ethz.mxquery.datamodel.QName varQName, TypeInfo type, XDMIterator seq, int windowType, Context outerContext, WindowVariable[] startVars, XDMIterator startExpr, boolean forceEnd, WindowVariable[] endVars, boolean onNewStart, XDMIterator endExpr) throws MXQueryException {
        ForseqIterator res;
        if (windowType == ForseqIterator.SLIDING_WINDOW && co.isParallelExecution()) {
            res = new ForseqWindowEarlyBindingParallel(outerContext, windowType, varQName, type, seq, startVars, startExpr, endVars, endExpr, forceEnd, onNewStart, ForseqIterator.ORDER_MODE_END, getCurrentLoc());
        } else {
            res = super.generateForseqIterator(varQName, type, seq, windowType, outerContext, startVars, startExpr, forceEnd, endVars, onNewStart, endExpr);
        }
        if (co.isParallelExecution()) res.setParallelAccess(true);
        return res;
    }

    protected XDMIterator generateGroupBy(VariableIterator[] arrExprs, VariableIterator[] groupContents) throws MXQueryException {
        if (co.isParallelExecution() && co.isXquery11()) {
            return new PGroupBy(getCurrentContext(), arrExprs, groupContents, getCurrentLoc());
        } else {
            return new GroupByIterator(getCurrentContext(), arrExprs, groupContents, getCurrentLoc());
        }
    }
}
