package org.exist.debugger.xquery;

import java.io.IOException;
import org.apache.mina.core.session.IoSession;
import org.exist.EXistException;
import org.exist.debuggee.Debuggee;
import org.exist.debuggee.dbgp.packets.Command;
import org.exist.debugger.Utils;
import org.exist.dom.QName;
import org.exist.storage.BrokerPool;
import org.exist.xquery.BasicFunction;
import org.exist.xquery.Cardinality;
import org.exist.xquery.FunctionSignature;
import org.exist.xquery.XPathException;
import org.exist.xquery.XQueryContext;
import org.exist.xquery.value.FunctionParameterSequenceType;
import org.exist.xquery.value.FunctionReturnSequenceType;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.SequenceType;
import org.exist.xquery.value.StringValue;
import org.exist.xquery.value.Type;

/**
 * 
 * @author <a href="mailto:shabanovd@gmail.com">Dmitriy Shabanov</a>
 */
public class StackGet extends BasicFunction {

    public static final FunctionSignature signatures[] = { new FunctionSignature(new QName("stack-get", Module.NAMESPACE_URI, Module.PREFIX), "", new SequenceType[] { new FunctionParameterSequenceType("session id", Type.STRING, Cardinality.EXACTLY_ONE, "") }, new FunctionReturnSequenceType(Type.NODE, Cardinality.EXACTLY_ONE, "")) };

    public StackGet(XQueryContext context, FunctionSignature signature) {
        super(context, signature);
    }

    @Override
    public Sequence eval(Sequence[] args, Sequence contextSequence) throws XPathException {
        try {
            Debuggee dbgr = BrokerPool.getInstance().getDebuggee();
            IoSession session = (IoSession) dbgr.getSession(args[0].getStringValue());
            if (session == null) return Sequence.EMPTY_SEQUENCE;
            Command command = new org.exist.debuggee.dbgp.packets.StackGet(session, "");
            command.exec();
            return Utils.nodeFromString(getContext(), new String(command.responseBytes()));
        } catch (EXistException e) {
            throw new XPathException(this, "", e);
        } catch (IOException e) {
            throw new XPathException(this, "", e);
        }
    }
}
