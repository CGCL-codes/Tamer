package org.jdom.adapters;

import java.io.*;
import java.lang.reflect.*;
import org.jdom.*;
import org.w3c.dom.Document;
import org.xml.sax.*;

/**
 * An adapter for the Apache Crimson DOM parser.
 * 
 * @version $Revision: 1.17 $, $Date: 2007/11/10 05:28:59 $
 * @author  Jason Hunter
 */
public class CrimsonDOMAdapter extends AbstractDOMAdapter {

    private static final String CVS_ID = "@(#) $RCSfile: CrimsonDOMAdapter.java,v $ $Revision: 1.17 $ $Date: 2007/11/10 05:28:59 $ $Name: jdom_1_1 $";

    /**
     * This creates a new <code>{@link Document}</code> from an
     * existing <code>InputStream</code> by letting a DOM
     * parser handle parsing using the supplied stream.
     *
     * @param in <code>InputStream</code> to parse.
     * @param validate <code>boolean</code> to indicate if validation should occur.
     * @return <code>Document</code> - instance ready for use.
     * @throws IOException when I/O error occurs.
     * @throws JDOMException when errors occur in parsing.
     */
    public Document getDocument(InputStream in, boolean validate) throws IOException, JDOMException {
        try {
            Class[] parameterTypes = new Class[2];
            parameterTypes[0] = Class.forName("java.io.InputStream");
            parameterTypes[1] = boolean.class;
            Object[] args = new Object[2];
            args[0] = in;
            args[1] = new Boolean(false);
            Class parserClass = Class.forName("org.apache.crimson.tree.XmlDocument");
            Method createXmlDocument = parserClass.getMethod("createXmlDocument", parameterTypes);
            Document doc = (Document) createXmlDocument.invoke(null, args);
            return doc;
        } catch (InvocationTargetException e) {
            Throwable targetException = e.getTargetException();
            if (targetException instanceof org.xml.sax.SAXParseException) {
                SAXParseException parseException = (SAXParseException) targetException;
                throw new JDOMException("Error on line " + parseException.getLineNumber() + " of XML document: " + parseException.getMessage(), parseException);
            } else if (targetException instanceof IOException) {
                IOException ioException = (IOException) targetException;
                throw ioException;
            } else {
                throw new JDOMException(targetException.getMessage(), targetException);
            }
        } catch (Exception e) {
            throw new JDOMException(e.getClass().getName() + ": " + e.getMessage(), e);
        }
    }

    /**
     * This creates an empty <code>Document</code> object based
     * on a specific parser implementation.
     *
     * @return <code>Document</code> - created DOM Document.
     * @throws JDOMException when errors occur.
     */
    public Document createDocument() throws JDOMException {
        try {
            return (Document) Class.forName("org.apache.crimson.tree.XmlDocument").newInstance();
        } catch (Exception e) {
            throw new JDOMException(e.getClass().getName() + ": " + e.getMessage() + " when creating document", e);
        }
    }
}
