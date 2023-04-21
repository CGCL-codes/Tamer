package ch.ethz.mxquery.functions.fn;

import java.util.Vector;
import ch.ethz.mxquery.contextConfig.Context;
import ch.ethz.mxquery.datamodel.types.Type;
import ch.ethz.mxquery.datamodel.xdm.Token;
import ch.ethz.mxquery.datamodel.xdm.TokenInterface;
import ch.ethz.mxquery.exceptions.MXQueryException;
import ch.ethz.mxquery.model.CurrentBasedIterator;
import ch.ethz.mxquery.model.XDMIterator;

public class Head extends CurrentBasedIterator {

    int position = 0;

    public TokenInterface next() throws MXQueryException {
        TokenInterface retToken;
        if (endOfSeq) return Token.END_SEQUENCE_TOKEN;
        if (called == 0) {
            called++;
            current = subIters[0];
        }
        retToken = sub0Next();
        if (position <= 1) {
            if (retToken.getEventType() == Type.END_SEQUENCE) {
            }
            return retToken;
        } else {
            endOfSeq = true;
            return Token.END_SEQUENCE_TOKEN;
        }
    }

    protected void resetImpl() throws MXQueryException {
        super.resetImpl();
        position = 0;
    }

    protected XDMIterator copy(Context context, XDMIterator[] subIters, Vector nestedPredCtxStack) throws MXQueryException {
        XDMIterator copy = new Head();
        copy.setContext(context, true);
        copy.setSubIters(subIters);
        return copy;
    }

    private TokenInterface sub0Next() throws MXQueryException {
        if (depth == 0) {
            position++;
        }
        return super.getNext();
    }
}
