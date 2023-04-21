package gov.lanl.adore.demo.mets.identifier.index;

import gov.lanl.identifier.Identifier;
import gov.lanl.xmltape.identifier.index.record.Record;
import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

public class MetsRecordDOM implements Record {

    private String metsDocId = null;

    private String metsDatestamp = null;

    private ArrayList<Identifier> ids = new ArrayList<Identifier>();

    public MetsRecordDOM() {
    }

    public void createRecord(String record) {
        try {
            SAXReader xmlReader = new SAXReader(false);
            InputSource is = new InputSource(new StringReader(record));
            treeWalk(xmlReader.read(is));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Identifier> getIdentifiers() {
        return ids;
    }

    private void treeWalk(Document document) {
        treeWalk(document.getRootElement());
    }

    private void treeWalk(Element element) {
        for (int i = 0, size = element.nodeCount(); i < size; i++) {
            Node node = element.node(i);
            if (node instanceof Element) {
                Element elem = (Element) node;
                if (elem.getName().equals("metsHdr")) {
                    for (Iterator j = elem.attributeIterator(); j.hasNext(); ) {
                        Attribute att = (Attribute) j.next();
                        if (att.getName().equals("CREATEDATE")) {
                            metsDatestamp = att.getValue();
                        }
                    }
                } else if (elem.getName().equals("file") && metsDocId != null) {
                    for (Iterator j = elem.attributeIterator(); j.hasNext(); ) {
                        Attribute att = (Attribute) j.next();
                        if (att.getName().equals("OWNERID")) {
                            ids.add(new Identifier(metsDocId, att.getValue(), metsDatestamp));
                        }
                    }
                } else {
                    treeWalk(elem);
                }
            } else if (metsDocId == null) {
                if (element.attributeCount() > 0) {
                    for (Iterator j = element.attributeIterator(); j.hasNext(); ) {
                        Attribute att = (Attribute) j.next();
                        if (att.getName().equals("OBJID")) {
                            metsDocId = att.getValue();
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        MetsRecordDOM x = new MetsRecordDOM();
        String y = gov.lanl.util.FileUtil.getContents(new File(args[0]));
        x.createRecord(y);
    }
}
