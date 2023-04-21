package data;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.io.IOException;
import model.GeneralVorNachteil;
import model.Relation;

public class VorNachteilXMLReader extends FeatureXMLReader {

    /**
    *
    */
    public VorNachteilXMLReader(String uri) {
        super(uri);
    }

    /**
    *
    */
    public VorNachteilXMLReader(Document doc) {
        super(doc);
    }

    /**
    *
    */
    public void evaluateBasics() throws IOException {
        String name = "";
        String id = "";
        String beschreibung = "";
        int gpKosten = 0;
        HashMap relations = null;
        String activeElementName = "";
        Node root = null;
        Node lvl1Item = null;
        Node lvl2Item = null;
        NamedNodeMap attrList1 = null;
        NamedNodeMap attrList2 = null;
        NodeList rootNodes = this.doc.getElementsByTagName("VorNachteile");
        root = rootNodes.item(0);
        NodeList lvl1Nodes = root.getChildNodes();
        int length1 = lvl1Nodes.getLength();
        for (int i = 0; i < length1; i++) {
            lvl1Item = lvl1Nodes.item(i);
            if (lvl1Item.getNodeType() == Node.ELEMENT_NODE) {
                if (lvl1Item.hasAttributes()) {
                    attrList1 = lvl1Item.getAttributes();
                    id = attrList1.getNamedItem("ID").getNodeValue();
                }
                NodeList lvl2Nodes = lvl1Item.getChildNodes();
                int length2 = lvl2Nodes.getLength();
                for (int l = 0; l < length2; l++) {
                    lvl2Item = lvl2Nodes.item(l);
                    switch(lvl2Item.getNodeType()) {
                        case Node.ELEMENT_NODE:
                            activeElementName = lvl2Item.getNodeName();
                            if (lvl2Item.hasAttributes()) attrList2 = lvl2Item.getAttributes(); else attrList2 = null;
                            if (activeElementName.equals("Name")) {
                                name = lvl2Item.getFirstChild().getNodeValue();
                            } else if (activeElementName.equals("Relations")) {
                                relations = this.extractRelations(lvl2Item);
                            } else if (activeElementName.equals("Beschreibung")) {
                                beschreibung = this.extractDescription(lvl2Item);
                            }
                            break;
                        case Node.TEXT_NODE:
                            break;
                        default:
                    }
                }
            }
            if (!id.equals("")) {
                if (!this.featureList.containsKey(id)) {
                    System.out.println("\tPutting VorNachteil ID '" + id + "'");
                    this.featureList.put(new String(id), new GeneralVorNachteil(name, id, gpKosten, relations, beschreibung));
                    id = "";
                    name = "";
                    beschreibung = "";
                    gpKosten = 0;
                    relations = null;
                } else throw new IOException("Duplicate VorNachteil '" + id + "' in XML file!");
            }
        }
    }

    /**
    *
    */
    public void evaluateReferences(HashMap arithmeticItemIndex) throws IOException {
        Iterator iter = featureList.values().iterator();
        GeneralVorNachteil vorNachteil = null;
        HashMap relations = null;
        Map.Entry entry = null;
        String key = null;
        while (iter.hasNext()) {
            vorNachteil = (GeneralVorNachteil) iter.next();
            System.out.println("\tResolving references of VorNachteil ID '" + vorNachteil.getID() + "'");
            relations = vorNachteil.getModifier();
            if (relations != null) {
                Iterator eigModisIter = relations.values().iterator();
                while (eigModisIter.hasNext()) {
                    ((Relation) eigModisIter.next()).resolveReference(arithmeticItemIndex);
                }
            }
        }
    }
}
