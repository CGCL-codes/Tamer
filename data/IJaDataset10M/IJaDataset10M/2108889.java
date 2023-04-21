package uk.gov.dti.og.fox.dom;

import java.util.HashMap;
import java.util.Iterator;
import java.util.*;
import uk.gov.dti.og.fox.*;
import uk.gov.dti.og.fox.dom.*;
import uk.gov.dti.og.fox.dom.DOMList;
import uk.gov.dti.og.fox.ex.ExActionFailed;
import uk.gov.dti.og.fox.ex.ExBadPath;
import uk.gov.dti.og.fox.ex.ExCardinality;
import uk.gov.dti.og.fox.ex.ExInternal;
import uk.gov.dti.og.fox.ex.ExTooFew;
import uk.gov.dti.og.fox.ex.ExTooMany;

public class XPComplexContextNested extends XP {

    private final String mProcessedExpression;

    private final String mContextOriginLabel;

    private final List mImplicatedNestedLabelList;

    public XPComplexContextNested(String pOriginalExpression, String pProcessedExpression, String pContextOriginLabel, Set pImplicatedNestedLabelSet) {
        mOriginalExpression = pOriginalExpression;
        mProcessedExpression = pProcessedExpression;
        mContextOriginLabel = pContextOriginLabel;
        mImplicatedNestedLabelList = new ArrayList(pImplicatedNestedLabelSet);
        synchronized (gXPCacheMap) {
            gXPCacheMap.put(pOriginalExpression, this);
        }
    }

    public XPResult getXPResult(DOM pRelativeDOM, ContextUElem pContextDOM) throws ExBadPath {
        pContextDOM.mXPathUsageCount++;
        StringBuffer lExpressionBuffer = new StringBuffer(mProcessedExpression);
        DOM lEvalOriginDOM = pContextDOM.getUElem(mContextOriginLabel);
        try {
            _expandPlaceHolderXPATH(lExpressionBuffer, mImplicatedNestedLabelList, "N", pContextDOM, null);
        } catch (ExTooFew x) {
            throw new ExBadPath("XPATH syntax error:\n" + mProcessedExpression, x);
        }
        try {
            return new XPResult(this, lExpressionBuffer.toString(), lEvalOriginDOM, 1);
        } catch (ExBadPath x) {
            throw new ExBadPath("Internal XPATH syntax error:\n" + mProcessedExpression + "\nFor original extended xpath:\n" + mOriginalExpression);
        }
    }
}
