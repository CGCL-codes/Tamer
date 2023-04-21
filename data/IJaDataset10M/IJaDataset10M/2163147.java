package ch.ethz.mxquery.functions.b;

import java.util.Vector;
import com.google.gwt.user.client.Window.Location;
import ch.ethz.mxquery.contextConfig.Context;
import ch.ethz.mxquery.datamodel.xdm.Token;
import ch.ethz.mxquery.datamodel.xdm.TokenInterface;
import ch.ethz.mxquery.exceptions.ErrorCodes;
import ch.ethz.mxquery.exceptions.MXQueryException;
import ch.ethz.mxquery.model.TokenBasedIterator;
import ch.ethz.mxquery.model.XDMIterator;

public class SetHash extends TokenBasedIterator {

    @Override
    protected void init() throws MXQueryException {
        currentToken = Token.END_SEQUENCE_TOKEN;
        TokenInterface tok = subIters[0].next();
        String hash = tok.getValueAsString();
        if (hash == null) {
            throw new MXQueryException(ErrorCodes.E0004_TYPE_INAPPROPRIATE_TYPE, "invalid argument in setHref", loc);
        }
        setHash(hash);
    }

    protected static native void setHash(String newhash);

    @Override
    protected XDMIterator copy(Context context, XDMIterator[] subIters, Vector nestedPredCtxStack) throws MXQueryException {
        SetHash ret = new SetHash();
        ret.setContext(context, true);
        ret.setSubIters(subIters);
        return ret;
    }
}
