package com.sun.org.apache.xpath.internal.functions;

import com.sun.org.apache.xml.internal.dtm.DTMIterator;
import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.objects.XNumber;
import com.sun.org.apache.xpath.internal.objects.XObject;

/**
 * Execute the Count() function.
 * @xsl.usage advanced
 */
public class FuncCount extends FunctionOneArg {

    static final long serialVersionUID = -7116225100474153751L;

    /**
   * Execute the function.  The function must return
   * a valid object.
   * @param xctxt The current execution context.
   * @return A valid XObject.
   *
   * @throws javax.xml.transform.TransformerException
   */
    public XObject execute(XPathContext xctxt) throws javax.xml.transform.TransformerException {
        DTMIterator nl = m_arg0.asIterator(xctxt, xctxt.getCurrentNode());
        int i = nl.getLength();
        nl.detach();
        return new XNumber((double) i);
    }
}
