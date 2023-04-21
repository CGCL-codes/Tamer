package dom;

import org.w3c.dom.Document;
import org.w3c.dom.Text;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/**
 * Encapsulates a DOM parser.
 *
 * @version $Id: ParserWrapper.java,v 1.1.1.1 2002/10/31 15:40:27 pettys Exp $
 */
public interface ParserWrapper {

    /** Parses the specified URI and returns the document. */
    public Document parse(String uri) throws Exception;

    /**
     * Set the state of a feature.
     *
     * Set the state of any feature in a SAX2 parser.  The parser
     * might not recognize the feature, and if it does recognize
     * it, it might not be able to fulfill the request.
     *
     * @param featureId The unique identifier (URI) of the feature.
     * @param state The requested state of the feature (true or false).
     *
     * @exception org.xml.sax.SAXNotRecognizedException If the
     *            requested feature is not known.
     * @exception org.xml.sax.SAXNotSupportedException If the
     *            requested feature is known, but the requested
     *            state is not supported.
     * @exception org.xml.sax.SAXException If there is any other
     *            problem fulfilling the request.
     */
    public void setFeature(String featureId, boolean state) throws SAXNotRecognizedException, SAXNotSupportedException;

    /** Returns the document information. */
    public DocumentInfo getDocumentInfo();

    /**
     * This interface is here to query information about the document
     * implementation returned by the <code>ParserWrapper#parse</code>
     * method.
     *
     * @author Andy Clark, IBM
     */
    public interface DocumentInfo {

        /** 
         * Returns true if the specified text node is ignorable whitespace. 
         */
        public boolean isIgnorableWhitespace(Text text);
    }
}
