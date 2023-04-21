package com.xmultra.processor.news;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import com.xmultra.log.Logger;
import com.xmultra.log.MessageLogEntry;
import com.xmultra.util.InitMapHolder;
import com.xmultra.util.XmlParseUtils;

/**
 * Validates an NITF Document using the NITF DTD
 *
 * @author Shannon C. Brown
 * @author Wayne W. Weber
 * @version     $Revision: #1 $
 * @since       1.2
 */
class NitfValidatingParser extends NimParser {

    /**
    * Updated automatically by source control management.
    */
    static final String VERSION = "@version $Revision: #1 $";

    private Logger logger;

    private XmlParseUtils xmlParseUtils;

    private MessageLogEntry msgEntry;

    /**
     * Creates the Parser which validitates a Doc via the NITF DTD.
     *
     * @param initMapHolder Has references to system utilities.
     */
    NitfValidatingParser(InitMapHolder initMapHolder) {
        this.msgEntry = new MessageLogEntry(this, VERSION);
        this.logger = (Logger) initMapHolder.getEntry(InitMapHolder.LOGGER);
        this.xmlParseUtils = (XmlParseUtils) initMapHolder.getEntry(InitMapHolder.XML_PARSE_UTILS);
    }

    /**
     * Extends the NimParser class.  NitfValidatingParser validates the
     * NITF document against the NITF DTD.
     *
     * @param doc        The Doc object containing the NITF document to be validated.
     */
    void parse(Doc doc) {
        if (doc.getDocStatus().get(DocStatus.THROW_AWAY) != null) {
            return;
        }
        String documentString = doc.getNITFDocumentString(true, false);
        try {
            Document nitfDocument = this.xmlParseUtils.convertStringToXmlDocument(documentString, true);
            doc.setNITFDocument(nitfDocument);
        } catch (SAXException se) {
            this.msgEntry.setAppContext("init()").setDocInfo(doc.getFileName());
            this.msgEntry.setMessageText("Invalid NITF document.");
            this.msgEntry.setError(se.getMessage());
            this.logger.logWarning(this.msgEntry);
            doc.getDocStatus().put(DocStatus.INVALID_XML, se.getMessage());
        }
    }
}
