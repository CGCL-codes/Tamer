package org.openscience.cdk.io.cml;

import java.io.PrintStream;
import org.openscience.cdk.io.cml.cdopi.CDOInterface;
import org.xml.sax.AttributeList;
import org.xml.sax.HandlerBase;

public class XMLHandler extends HandlerBase {

    private Convention conv;

    public XMLHandler(CDOInterface cdo) {
        conv = new Convention(cdo);
    }

    public void doctypeDecl(String name, String publicId, String systemId) throws Exception {
    }

    public void startDocument() {
        conv.startDocument();
    }

    public void endDocument() {
        conv.endDocument();
    }

    public CDOInterface returnCDO() {
        return conv.returnCDO();
    }

    public void characters(char ch[], int start, int length) {
    }

    public void startElement(String name, AttributeList atts) {
    }

    public void endElement(String name) {
    }
}
