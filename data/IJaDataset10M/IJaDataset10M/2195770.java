package org.exist.xquery.modules.sql;

import org.apache.log4j.Logger;
import org.exist.dom.QName;
import org.exist.xquery.BasicFunction;
import org.exist.xquery.Cardinality;
import org.exist.xquery.FunctionSignature;
import org.exist.xquery.XPathException;
import org.exist.xquery.XQueryContext;
import org.exist.xquery.value.FunctionParameterSequenceType;
import org.exist.xquery.value.IntegerValue;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.SequenceType;
import org.exist.xquery.value.Type;
import java.sql.Connection;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * eXist SQL Module Extension GetJNDIConnectionFunction.
 *
 * <p>Get a connection to a SQL Database via JNDI</p>
 *
 * @author   Adam Retter <adam@exist-db.org>
 * @author   Loren Cahlander
 * @version  1.2
 * @see      org.exist.xquery.BasicFunction#BasicFunction(org.exist.xquery.XQueryContext, org.exist.xquery.FunctionSignature)
 * @serial   2008-05-19
 */
public class GetJNDIConnectionFunction extends BasicFunction {

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(GetJNDIConnectionFunction.class);

    public static final FunctionSignature[] signatures = { new FunctionSignature(new QName("get-jndi-connection", SQLModule.NAMESPACE_URI, SQLModule.PREFIX), "Opens a connection to a SQL Database.", new SequenceType[] { new FunctionParameterSequenceType("jndi-name", Type.STRING, Cardinality.EXACTLY_ONE, "The JNDI name") }, new FunctionParameterSequenceType("handle", Type.LONG, Cardinality.ZERO_OR_ONE, "an xs:long representing the connection handle")), new FunctionSignature(new QName("get-jndi-connection", SQLModule.NAMESPACE_URI, SQLModule.PREFIX), "Opens a connection to a SQL Database.", new SequenceType[] { new FunctionParameterSequenceType("jndi-name", Type.STRING, Cardinality.EXACTLY_ONE, "The JNDI name"), new FunctionParameterSequenceType("username", Type.STRING, Cardinality.EXACTLY_ONE, "The username"), new FunctionParameterSequenceType("password", Type.STRING, Cardinality.EXACTLY_ONE, "The password") }, new FunctionParameterSequenceType("handle", Type.LONG, Cardinality.ZERO_OR_ONE, "an xs:long representing the connection handle")) };

    /**
     * GetJNDIConnectionFunction Constructor.
     *
     * @param  context    The Context of the calling XQuery
     * @param  signature  DOCUMENT ME!
     */
    public GetJNDIConnectionFunction(XQueryContext context, FunctionSignature signature) {
        super(context, signature);
    }

    /**
     * evaluate the call to the xquery get-jndi-connection() function, it is really the main entry point of this class.
     *
     * @param   args             arguments from the get-jndi-connection() function call
     * @param   contextSequence  the Context Sequence to operate on (not used here internally!)
     *
     * @return  A xs:long representing a handle to the connection
     *
     * @throws  XPathException  DOCUMENT ME!
     *
     * @see     org.exist.xquery.BasicFunction#eval(org.exist.xquery.value.Sequence[], org.exist.xquery.value.Sequence)
     */
    public Sequence eval(Sequence[] args, Sequence contextSequence) throws XPathException {
        if (args[0].isEmpty()) {
            return (Sequence.EMPTY_SEQUENCE);
        }
        try {
            Connection con = null;
            String jndiName = args[0].getStringValue();
            Context ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup(jndiName);
            if (args.length == 1) {
                con = ds.getConnection();
            }
            if (args.length == 3) {
                String jndiUser = args[1].getStringValue();
                String jndiPassword = args[2].getStringValue();
                con = ds.getConnection(jndiUser, jndiPassword);
            }
            return (new IntegerValue(SQLModule.storeConnection(context, con)));
        } catch (Exception e) {
            throw (new XPathException(this, e.getMessage()));
        }
    }
}
