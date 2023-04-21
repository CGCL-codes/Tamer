package ch.ethz.mxquery.functions.fn;

import java.util.Vector;
import ch.ethz.mxquery.contextConfig.Context;
import ch.ethz.mxquery.datamodel.types.Type;
import ch.ethz.mxquery.datamodel.types.TypeInfo;
import ch.ethz.mxquery.datamodel.xdm.BooleanToken;
import ch.ethz.mxquery.exceptions.DynamicException;
import ch.ethz.mxquery.exceptions.ErrorCodes;
import ch.ethz.mxquery.exceptions.MXQueryException;
import ch.ethz.mxquery.model.TokenBasedIterator;
import ch.ethz.mxquery.model.XDMIterator;
import ch.ethz.mxquery.util.Set;

public class StartsWith extends TokenBasedIterator {

    protected void init() throws MXQueryException {
        String res = getStringValueOrEmpty(subIters[0]);
        String res2 = getStringValueOrEmpty(subIters[1]);
        if (subIters.length > 2) {
            String collUri = getStringValue(subIters[2]);
            Set collations = context.getCollations();
            if (!collations.contains(collUri)) throw new DynamicException(ErrorCodes.F0010_UNSUPPORTED_COLLATION, "Unsupported Collation", loc);
        }
        if (res2 == null || res2.equals("")) {
            currentToken = BooleanToken.TRUE_TOKEN;
            return;
        }
        if (res == null || res.equals("")) currentToken = BooleanToken.FALSE_TOKEN; else {
            if (res.startsWith(res2)) {
                currentToken = BooleanToken.TRUE_TOKEN;
            } else currentToken = BooleanToken.FALSE_TOKEN;
        }
    }

    public TypeInfo getStaticType() {
        return new TypeInfo(Type.BOOLEAN, Type.OCCURRENCE_IND_EXACTLY_ONE);
    }

    protected XDMIterator copy(Context context, XDMIterator[] subIters, Vector nestedPredCtxStack) throws MXQueryException {
        XDMIterator copy = new StartsWith();
        copy.setContext(context, true);
        copy.setSubIters(subIters);
        return copy;
    }
}
