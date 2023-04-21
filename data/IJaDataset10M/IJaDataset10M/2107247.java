package ch.ethz.mxquery.functions.fn;

import java.util.Vector;
import ch.ethz.mxquery.bindings.WindowFactory;
import ch.ethz.mxquery.contextConfig.Context;
import ch.ethz.mxquery.datamodel.QName;
import ch.ethz.mxquery.datamodel.types.Type;
import ch.ethz.mxquery.datamodel.types.TypeInfo;
import ch.ethz.mxquery.datamodel.xdm.Token;
import ch.ethz.mxquery.datamodel.xdm.TokenInterface;
import ch.ethz.mxquery.exceptions.ErrorCodes;
import ch.ethz.mxquery.exceptions.MXQueryException;
import ch.ethz.mxquery.functions.RequestTypeMulti;
import ch.ethz.mxquery.iterators.SequenceTypeIterator;
import ch.ethz.mxquery.iterators.UserdefFuncCallLateBinding;
import ch.ethz.mxquery.iterators.VariableIterator;
import ch.ethz.mxquery.model.CurrentBasedIterator;
import ch.ethz.mxquery.model.TokenSequenceIterator;
import ch.ethz.mxquery.model.VariableHolder;
import ch.ethz.mxquery.model.Window;
import ch.ethz.mxquery.model.XDMIterator;
import ch.ethz.mxquery.util.Hashtable;

public class Fold extends CurrentBasedIterator implements RequestTypeMulti {

    private int direction;

    private int DIRECTION_LEFT = 0;

    private int DIRECTION_RIGHT = 1;

    public TokenInterface next() throws MXQueryException {
        if (called == 0) {
            QName qnSeq = context.registerAnonymousVariable();
            VariableHolder vh1 = context.getVariable(qnSeq);
            QName qnZero = context.registerAnonymousVariable();
            VariableHolder vh2 = context.getVariable(qnZero);
            called++;
            TypeInfo[] params = new TypeInfo[2];
            params[0] = new TypeInfo(Type.ITEM, Type.OCCURRENCE_IND_ZERO_OR_MORE);
            params[1] = new TypeInfo(Type.ITEM, Type.OCCURRENCE_IND_ZERO_OR_MORE);
            TypeInfo tInfo = new TypeInfo(Type.FUNCTION_ITEM, Type.OCCURRENCE_IND_EXACTLY_ONE, params, new TypeInfo(Type.ITEM, Type.OCCURRENCE_IND_ZERO_OR_MORE), new Hashtable());
            SequenceTypeIterator st = new SequenceTypeIterator(tInfo, true, true, context, loc, true);
            st.setSubIters(subIters[0]);
            st.setResettable(true);
            called++;
            XDMIterator reduceFunc = new UserdefFuncCallLateBinding(context);
            if (direction == DIRECTION_LEFT) reduceFunc.setSubIters(new XDMIterator[] { st, new VariableIterator(context, qnZero, true, loc), new VariableIterator(context, qnSeq, true, loc) }); else reduceFunc.setSubIters(new XDMIterator[] { st, new VariableIterator(context, qnSeq, true, loc), new VariableIterator(context, qnZero, true, loc) });
            reduceFunc.staticInit();
            reduceFunc.setResettable(true);
            Window wnd1 = WindowFactory.getNewWindow(context, subIters[2]);
            current = WindowFactory.getNewWindow(context, subIters[1]);
            if (direction == DIRECTION_LEFT) while (wnd1.hasNextItem()) {
                if (vh1.getIter() != null) ((Window) vh1.getIter()).destroyWindow();
                vh1.setIter(wnd1.nextItem());
                vh2.setIter(current);
                collect(reduceFunc);
            } else {
                int numItems = 1;
                while (wnd1.hasItem(numItems)) {
                    numItems++;
                }
                numItems--;
                for (int i = numItems; i > 0; i--) {
                    if (vh1.getIter() != null) ((Window) vh1.getIter()).destroyWindow();
                    vh1.setIter(wnd1.getItem(i));
                    vh2.setIter(current);
                    collect(reduceFunc);
                }
            }
        }
        if (endOfSeq) return Token.END_SEQUENCE_TOKEN;
        TokenInterface tk = current.next();
        return tk;
    }

    void collect(XDMIterator reduceFunc) throws MXQueryException {
        Vector acc = new Vector();
        TokenInterface tok = reduceFunc.next();
        while (tok.getEventType() != Type.END_SEQUENCE) {
            acc.addElement(tok);
            tok = reduceFunc.next();
        }
        current = WindowFactory.getNewWindow(context, new TokenSequenceIterator(context, acc));
        reduceFunc.reset();
    }

    protected XDMIterator copy(Context context, XDMIterator[] subIters, Vector nestedPredCtxStack) throws MXQueryException {
        Fold copy = new Fold();
        copy.setContext(context, true);
        copy.setSubIters(subIters);
        copy.direction = direction;
        return copy;
    }

    public void setOperation(String type) throws MXQueryException {
        if (type.equals("left")) direction = DIRECTION_LEFT; else if (type.equals("right")) direction = DIRECTION_RIGHT; else throw new MXQueryException(ErrorCodes.E0009_STATIC_SCHEMA_IMPORTS_NOT_SUPPORTED, "invalid value for fold: " + type, loc);
    }

    public void setReturnType(int type) throws MXQueryException {
    }
}
