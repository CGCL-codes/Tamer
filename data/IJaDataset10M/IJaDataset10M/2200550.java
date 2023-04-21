package org.exist.xquery;

import org.apache.log4j.Logger;
import org.exist.Namespaces;
import org.exist.dom.QName;
import org.exist.xquery.util.ExpressionDumper;

public class TimerPragma extends Pragma {

    public static final QName TIMER_PRAGMA = new QName("timer", Namespaces.EXIST_NS, "exist");

    private static final Logger LOG = Logger.getLogger(TimerPragma.class);

    private long start;

    public TimerPragma(QName qname, String contents) throws XPathException {
        super(qname, contents);
    }

    public void after(XQueryContext context, Expression expression) throws XPathException {
        long elapsed = System.currentTimeMillis() - start;
        if (LOG.isTraceEnabled()) LOG.trace("Elapsed: " + elapsed + "ms. for expression:\n" + ExpressionDumper.dump(expression));
    }

    public void before(XQueryContext context, Expression expression) throws XPathException {
        start = System.currentTimeMillis();
    }
}
