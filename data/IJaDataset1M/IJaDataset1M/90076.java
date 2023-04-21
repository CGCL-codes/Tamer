package ch.ethz.mxquery.functions.fn;

import java.util.Vector;
import ch.ethz.mxquery.exceptions.ErrorCodes;
import ch.ethz.mxquery.exceptions.MXQueryException;
import ch.ethz.mxquery.exceptions.TypeException;
import ch.ethz.mxquery.functions.fn.Replace;
import ch.ethz.mxquery.model.XDMIterator;
import ch.ethz.mxquery.model.TokenBasedIterator;
import ch.ethz.mxquery.datamodel.types.Type;
import ch.ethz.mxquery.datamodel.types.TypeInfo;
import ch.ethz.mxquery.contextConfig.Context;
import ch.ethz.mxquery.datamodel.xdm.TextToken;
import ch.ethz.mxquery.datamodel.xdm.TokenInterface;

public class Replace extends TokenBasedIterator {

    protected void init() throws MXQueryException {
        String res = getStringValueOrEmpty(subIters[0]);
        if (res == null) {
            currentToken = new TextToken(null, "");
            return;
        }
        String pattern = getStringValue(subIters[1]);
        String replacement = getStringValue(subIters[2]);
        currentToken = new TextToken(null, res);
    }

    public TypeInfo getStaticType() {
        return new TypeInfo(Type.STRING, Type.OCCURRENCE_IND_EXACTLY_ONE);
    }

    public XDMIterator copy(Context context, XDMIterator[] subIters, Vector nestedPredCtxStack) throws MXQueryException {
        XDMIterator copy = new Replace();
        copy.setContext(context, true);
        copy.setSubIters(subIters);
        return copy;
    }
}
