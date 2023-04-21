package org.deri.xquery.saxon;

import net.sf.saxon.lib.*;
import net.sf.saxon.tree.iter.*;
import net.sf.saxon.om.*;
import net.sf.saxon.value.SequenceType;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.StringValue;

/**
 * 
 * @author <a href="mailto:nuno [dot] lopes [at] deri [dot] org">Nuno Lopes</a>
 * @version 1.0
 */
public class turtleGraphToURIExtFunction extends ExtensionFunctionDefinition {

    /**
	 * 
	 */
    private static final long serialVersionUID = 8641294257135052785L;

    /**
   * Name of the function
   * 
   */
    private static StructuredQName funcname = new StructuredQName("_xsparql", "http://xsparql.deri.org/demo/xquery/xsparql.xquery", "turtleGraphToURI");

    public turtleGraphToURIExtFunction() {
    }

    @Override
    public StructuredQName getFunctionQName() {
        return funcname;
    }

    @Override
    public int getMinimumNumberOfArguments() {
        return 2;
    }

    @Override
    public int getMaximumNumberOfArguments() {
        return 2;
    }

    @Override
    public SequenceType[] getArgumentTypes() {
        return new SequenceType[] { SequenceType.SINGLE_STRING, SequenceType.SINGLE_STRING };
    }

    @Override
    public SequenceType getResultType(SequenceType[] suppliedArgumentTypes) {
        return SequenceType.SINGLE_STRING;
    }

    @Override
    public ExtensionFunctionCall makeCallExpression() {
        return new ExtensionFunctionCall() {

            private static final long serialVersionUID = 154082133874153698L;

            @Override
            public SequenceIterator call(SequenceIterator[] arguments, XPathContext context) throws XPathException {
                String prefix = arguments[0].next().getStringValue();
                String n3 = arguments[1].next().getStringValue();
                return SingletonIterator.makeIterator(new StringValue(EvaluatorExternalFunctions.turtleGraphToURI(prefix, n3)));
            }
        };
    }
}
