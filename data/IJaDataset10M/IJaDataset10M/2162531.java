package ch.ethz.mxquery.iterators;

import java.util.Vector;
import ch.ethz.mxquery.contextConfig.Context;
import ch.ethz.mxquery.datamodel.types.Type;
import ch.ethz.mxquery.datamodel.xdm.Token;
import ch.ethz.mxquery.datamodel.xdm.TokenInterface;
import ch.ethz.mxquery.exceptions.MXQueryException;
import ch.ethz.mxquery.exceptions.QueryLocation;
import ch.ethz.mxquery.model.Iterator;
import ch.ethz.mxquery.model.XDMIterator;

/**
 * This iterator only tells the GFlwor that it could bind, if the where
 * expression evaluates to true. If it does not, this iterator acts as if it
 * could not bind, forcing the GFlwor to try the next binding.
 * 
 * @author flwidmer
 * 
 */
public class GWhereIterator extends Iterator {

    public GWhereIterator(Context ctx, QueryLocation loc, XDMIterator whereExpr) throws MXQueryException {
        super(ctx, new XDMIterator[] { whereExpr }, loc);
    }

    public TokenInterface next() throws MXQueryException {
        TokenInterface tok = subIters[0].next();
        if (tok.getEventType() == Type.BOOLEAN) {
            boolean value = tok.getBoolean();
            if (value && called == 0) {
                called++;
                return Token.START_SEQUENCE_TOKEN;
            } else {
                called++;
                return Token.END_SEQUENCE_TOKEN;
            }
        }
        called++;
        return Token.END_SEQUENCE_TOKEN;
    }

    protected void resetImpl() throws MXQueryException {
        super.resetImpl();
    }

    protected XDMIterator copy(Context context, XDMIterator[] subIters, Vector nestedPredCtxStack) throws MXQueryException {
        GWhereIterator ret = new GWhereIterator(context, loc, subIters[0]);
        return ret;
    }
}
