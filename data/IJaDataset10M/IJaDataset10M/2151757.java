package org.exist.xqj.remote;

import javax.xml.xquery.XQException;
import javax.xml.xquery.XQStaticContext;
import org.exist.xqj.EXistXQExpressionImpl;

/**
 * Base class for XQRemoteExpression. Not to be instantiated.
 * 
 * @author Cherif YAYA <chef.ya@gmail.com>
 */
public abstract class XQRemoteExpressionImpl extends EXistXQExpressionImpl {

    protected XQRemoteConnection conn;

    protected XQRemoteExpressionImpl(XQRemoteConnection conn, XQStaticContext ctxt) throws XQException {
        super(ctxt);
        this.conn = conn;
    }

    protected void throwIfClosed() throws XQException {
        if (conn == null) throw new XQException("The expression is no longer valid.");
        if (expressionClosed) throw new XQException("The expression is no longer valid."); else if (conn.isClosed()) {
            expressionClosed = true;
            throw new XQException("The expression is no longer valid.");
        }
    }

    public XQRemoteConnection getRemoteConnection() {
        return conn;
    }
}
