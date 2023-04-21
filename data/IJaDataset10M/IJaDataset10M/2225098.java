package net.sf.saxon.expr;

import net.sf.saxon.instruct.*;
import net.sf.saxon.om.NamespaceConstant;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.om.ValueRepresentation;
import net.sf.saxon.trace.ExpressionPresenter;
import net.sf.saxon.trans.XPathException;
import java.util.Iterator;
import java.util.ArrayList;

/**
 *
 */
public class ContinueInstr extends Instruction {

    private WithParam[] actualParams = null;

    private IterateInstr iterateInstr;

    private UserFunction continueFunction;

    static ValueRepresentation[] emptyArgs = new ValueRepresentation[0];

    public static StructuredQName SAXON_CONTINUE = new StructuredQName("saxon", NamespaceConstant.SAXON, "continue");

    public ContinueInstr(IterateInstr iterateInstr) {
        this.iterateInstr = iterateInstr;
        continueFunction = new UserFunction();
        continueFunction.setFunctionName(SAXON_CONTINUE);
    }

    public void setParameters(WithParam[] actualParams) {
        this.actualParams = actualParams;
    }

    public Iterator iterateSubExpressions() {
        ArrayList list = new ArrayList(10);
        WithParam.getXPathExpressions(actualParams, list);
        return list.iterator();
    }

    public TailCall processLeavingTail(XPathContext context) throws XPathException {
        XPathContextMajor cm = (XPathContextMajor) context;
        ParameterSet params = assembleParams(context, actualParams);
        cm.setLocalParameters(params);
        cm.requestTailCall(continueFunction, emptyArgs);
        return null;
    }

    public Expression simplify(ExpressionVisitor visitor) throws XPathException {
        WithParam.simplify(actualParams, visitor);
        return this;
    }

    public Expression copy() {
        return this;
    }

    public void explain(ExpressionPresenter out) {
        out.startElement("saxonContinue");
        if (actualParams != null && actualParams.length > 0) {
            out.startSubsidiaryElement("withParams");
            WithParam.displayExpressions(actualParams, out);
            out.endSubsidiaryElement();
        }
        out.endElement();
    }
}
