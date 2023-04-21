package net.sf.osadm.linedata.sax;

import org.xml.sax.SAXException;

public interface CellPolisher {

    void visit(SaxContentCreatorConstructor saxContentCreatorConstructor, String text) throws SAXException;
}
