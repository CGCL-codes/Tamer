package ch.ethz.mxquery.functions.b;

import java.util.LinkedList;
import java.util.Vector;
import ch.ethz.mxqjs.client.Environment;
import ch.ethz.mxquery.contextConfig.Context;
import ch.ethz.mxquery.datamodel.QName;
import ch.ethz.mxquery.datamodel.types.Type;
import ch.ethz.mxquery.datamodel.xdm.FunctionItemToken;
import ch.ethz.mxquery.datamodel.xdm.Token;
import ch.ethz.mxquery.datamodel.xdm.TokenInterface;
import ch.ethz.mxquery.exceptions.DynamicException;
import ch.ethz.mxquery.exceptions.ErrorCodes;
import ch.ethz.mxquery.exceptions.MXQueryException;
import ch.ethz.mxquery.exceptions.QueryLocation;
import ch.ethz.mxquery.exceptions.TypeException;
import ch.ethz.mxquery.model.TokenBasedIterator;
import ch.ethz.mxquery.model.XDMIterator;
import ch.ethz.mxquery.update.store.domImpl.ElementNodeToken;
import ch.ethz.mxquery.util.LogLevel;
import ch.ethz.mxquery.util.browser.dom.Element;

public class RemoveAnonymousEventListener extends TokenBasedIterator {

    protected void init() throws MXQueryException {
        currentToken = Token.END_SEQUENCE_TOKEN;
        String eventname = getStringValue(subIters[1]);
        XDMIterator iter = subIters[0];
        TokenInterface tok;
        int type;
        int level = 0;
        LinkedList<TokenInterface> tokens = new LinkedList<TokenInterface>();
        do {
            tok = iter.next();
            type = tok.getEventType();
            switch(type) {
                case Type.END_SEQUENCE:
                    break;
                case Type.START_TAG:
                    if (level++ == 0) tokens.add(tok);
                    break;
                case Type.END_TAG:
                    level--;
                    break;
                default:
            }
        } while (type != Type.END_SEQUENCE);
        for (TokenInterface token : tokens) {
            removeAnonymousEventHandler(token, eventname);
        }
    }

    private void removeAnonymousEventHandler(TokenInterface tok, String eventname) throws MXQueryException {
        if (!(tok instanceof ElementNodeToken)) {
            return;
        }
        ElementNodeToken nt = (ElementNodeToken) tok;
        Element el = (Element) nt.getNode();
        Context ctx = this.getContext().getParent();
        Environment.removeAnonymousEventListeners(el, eventname, ctx);
    }

    protected XDMIterator copy(Context context, XDMIterator[] subIters, Vector nestedPredCtxStack) throws MXQueryException {
        RemoveAnonymousEventListener copy = new RemoveAnonymousEventListener();
        copy.setContext(context, true);
        copy.setSubIters(subIters);
        return copy;
    }
}
