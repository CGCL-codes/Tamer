package ch.ethz.mxquery.functions.fn;

import java.util.Vector;
import ch.ethz.mxquery.exceptions.MXQueryException;
import ch.ethz.mxquery.model.XDMIterator;
import ch.ethz.mxquery.datamodel.MXQueryDouble;
import ch.ethz.mxquery.model.TokenBasedIterator;
import ch.ethz.mxquery.datamodel.types.Type;
import ch.ethz.mxquery.datamodel.types.TypeInfo;
import ch.ethz.mxquery.contextConfig.Context;
import ch.ethz.mxquery.datamodel.xdm.TextToken;
import ch.ethz.mxquery.datamodel.xdm.TokenInterface;

public class SubString extends TokenBasedIterator {

    protected void init() throws MXQueryException {
        String res = getStringValueOrEmpty(subIters[0]);
        if (res == null) {
            currentToken = new TextToken(null, "");
            return;
        }
        TokenInterface inputToken1 = subIters[1].next();
        int type1 = Type.getEventTypeSubstituted(inputToken1.getEventType(), Context.getDictionary());
        int sPos = -1;
        switch(type1) {
            case Type.INTEGER:
                sPos = (int) inputToken1.getLong();
                break;
            case Type.DOUBLE:
            case Type.FLOAT:
            case Type.DECIMAL:
                MXQueryDouble val = inputToken1.getDouble();
                if (val.isNaN() || val.isNegativeInfinity() || val.isPositiveInfinity()) {
                    currentToken = new TextToken(null, "");
                    return;
                }
                sPos = val.getIntValue();
                if (val.hasFractionPart()) sPos++;
        }
        if (subIters.length == 2) {
            if (sPos < 1) sPos = 1;
            currentToken = new TextToken(null, res.substring(sPos - 1, res.length()));
            return;
        }
        if (subIters.length == 3) {
            TokenInterface inputToken2 = subIters[2].next();
            int type2 = Type.getEventTypeSubstituted(inputToken2.getEventType(), Context.getDictionary());
            int sLen = -1;
            switch(type2) {
                case Type.INTEGER:
                    sLen = (int) inputToken2.getLong();
                    break;
                case Type.DOUBLE:
                case Type.FLOAT:
                case Type.DECIMAL:
                    MXQueryDouble val = inputToken2.getDouble();
                    if (val.isNaN() || val.isNegativeInfinity()) {
                        currentToken = new TextToken(null, "");
                        return;
                    }
                    if (val.isPositiveInfinity()) {
                        sLen = Integer.MAX_VALUE;
                    } else {
                        sLen = val.getIntValue();
                        if (val.hasFractionPart()) sLen++;
                    }
            }
            if (sLen < 1) sLen = 0;
            int endPos = sPos - 1 + sLen;
            if (sPos < 1) sPos = 1;
            if (endPos > res.length()) endPos = res.length();
            currentToken = new TextToken(null, res.substring(sPos - 1, endPos));
            return;
        }
        currentToken = new TextToken(null, "");
    }

    public TypeInfo getStaticType() {
        return new TypeInfo(Type.STRING, Type.OCCURRENCE_IND_EXACTLY_ONE);
    }

    public XDMIterator copy(Context context, XDMIterator[] subIters, Vector nestedPredCtxStack) throws MXQueryException {
        XDMIterator copy = new SubString();
        copy.setContext(context, true);
        copy.setSubIters(subIters);
        return copy;
    }
}
