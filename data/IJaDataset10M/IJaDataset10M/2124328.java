package org.nmc.pachyderm.foundation;

import ca.ucalgary.apollo.core.*;
import com.webobjects.foundation.*;
import com.webobjects.eocontrol.*;
import java.net.*;
import java.io.*;
import java.util.*;
import org.nmc.jdom.*;
import org.nmc.jdom.input.*;

public class PXComponentDBDesc extends PXComponentXMLDesc {

    public PXComponentDBDesc(EOEnterpriseObject record) {
        super();
        NSData xmlData = (NSData) record.storedValueForKey("definitionXML");
        Element defElem;
        try {
            InputStream is = xmlData.stream();
            SAXBuilder builder = new SAXBuilder();
            Document defDoc = builder.build(is);
            defElem = defDoc.getRootElement();
        } catch (Exception e) {
            NSLog.err.appendln("WARNING: Unable to initialize component description with identifier: " + record.valueForKey("componentIdentifier") + "\r\tmessage: " + e.getMessage());
            throw new IllegalArgumentException("Unable to initialize component description from enterprise object: " + record);
        }
        super._initWithDefinitionDocument(defElem);
    }
}
