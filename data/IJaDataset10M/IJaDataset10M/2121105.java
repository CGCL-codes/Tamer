package com.apelon.dts.transfer;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Created by IntelliJ IDEA.
 * <p>Copyright:  Copyright (c) 2004</p>
 * <p>Company:  Apelon, Inc.</p>
 * 
 * @author Apelon, Inc.
 */
public class SubconceptHandler extends DTSSaxHandler {

    static final String HANDLER_NAME = "subconcept";

    public SubconceptHandler() {
        super();
        resolver.addToCatalog(com.apelon.dts.dtd.result.DTD.DTSCONCEPT_RESULT, com.apelon.dts.dtd.result.DTD.class, com.apelon.dts.dtd.result.DTD.DTSCONCEPT_RESULT_FILE);
    }

    /**
   * Initiate subconcept element.
   *
   * @param      namespaceURI
   * @param      localName
   * @param      qName
   * @param      atts
   * @throws     SAXException
   */
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        if (localName.equals(HANDLER_NAME)) {
            dtsObjectEntry = new DTSObjectEntry();
            SaxEntry saxEntry = new SaxEntry(localName, dtsObjectEntry, START);
            stack.push(saxEntry);
            processSelf(namespaceURI, localName, qName, atts);
        } else {
            delegateEvent(namespaceURI, localName, qName, atts);
        }
    }
}
