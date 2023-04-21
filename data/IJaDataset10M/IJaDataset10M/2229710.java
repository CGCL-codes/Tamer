package org.ocd.xmlparser;

import java.io.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import org.xml.sax.*;
import org.w3c.dom.*;

/**
 *
 * @author $Author: drichan $
 * @author ocd_dino - ocd_dino@users.sourceforge.net (initial author)
 * @version $Revision: 1.3 $
 * @since $Date: 2002/08/14 07:29:48 $
 */
public interface IXMLParser {

    /**
   * @return  */
    public DocumentBuilderFactory getDocumentBuilderFactory();

    /**
   * @return  */
    public DocumentBuilder getDocumentBuilder();

    /**
   * @return  */
    public TransformerFactory getTransformerFactory();

    /**
   * @return  */
    public Transformer getTransformer();

    /**
   * Write DOM Document to Output Stream
   * @param stream to write Output to.
   */
    public void writeDocument(Document pDoc, OutputStream pStream) throws IOException;

    /**
   * Read an XML Document from an InputStream
   * @param XML DOM Document
   */
    public Document readDocument(InputStream pStream) throws IOException, SAXException;
}
