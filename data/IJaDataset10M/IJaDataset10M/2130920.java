package jp.riken.omicspace.service.translator;

import java.util.ArrayList;
import jp.riken.omicspace.osml.*;
import jp.riken.omicspace.lib.XMLElement;
import org.w3c.dom.*;

public class LineMode extends Translator {

    public LineMode() {
    }

    public Osml translate(Document document, String s, String s1, String s2, String s3) {
        Osml osml = osmlBuilder.createOsml();
        osml.setVersion("1.1");
        try {
            NodeList nodelist = document.getElementsByTagName("TMGFF");
            for (int i = 0; i < nodelist.getLength(); i++) {
                Element element = (Element) nodelist.item(i);
                String s4 = element.getAttribute("source");
                Dataset dataset = osmlBuilder.createDataset();
                dataset.setId(getDatasetId());
                if (s4 != null) dataset.setName(s3 + "." + s4);
                osml.addDataset(dataset);
                Property property = osmlBuilder.createProperty();
                property.setParam("check");
                property.setValue(s1);
                dataset.addProperty(property);
                FunctionalClass functionalclass = osmlBuilder.createFunctionalClass();
                functionalclass.setId(getFunctionalClassId());
                dataset.addFunctionalClass(functionalclass);
                NodeList nodelist1 = element.getChildNodes();
                for (int j = 0; j < nodelist1.getLength(); j++) {
                    Node node = nodelist1.item(j);
                    if (node.getNodeType() != 1 || !node.getNodeName().equals("SEGMENT")) continue;
                    NodeList nodelist2 = node.getChildNodes();
                    for (int k = 0; k < nodelist2.getLength(); k++) {
                        Node node1 = nodelist2.item(k);
                        if (node1.getNodeType() != 1 || !node1.getNodeName().equals("GROUP")) continue;
                        OmicElement omicelement = osmlBuilder.createOmicElement();
                        omicelement.setId(getOmicElementId());
                        functionalclass.addOmicElement(omicelement);
                        String s5 = ((Element) node1).getAttribute("id");
                        if (s5 != null) omicelement.setName(s5);
                        String s6 = ((Element) node1).getAttribute("type");
                        if (s6 != null) dataset.setNote(s6);
                        String s7 = null;
                        String s8 = null;
                        String s9 = null;
                        ArrayList vector = new ArrayList();
                        ArrayList vector1 = new ArrayList();
                        String s10 = "";
                        NodeList nodelist3 = node1.getChildNodes();
                        for (int l = 0; l < nodelist3.getLength(); l++) {
                            Node node2 = nodelist3.item(l);
                            if (node2.getNodeType() != 1) continue;
                            if (node2.getNodeName().equals("START")) s7 = XMLElement.getValue((Element) node2);
                            if (node2.getNodeName().equals("END")) s8 = XMLElement.getValue((Element) node2);
                            if (node2.getNodeName().equals("ORIENTATION")) s9 = XMLElement.getValue((Element) node2);
                            if (node2.getNodeName().equals("NOTE")) {
                                String s11 = XMLElement.getValue((Element) node2);
                                if (s11 != null) omicelement.setNote(XMLElement.getValue((Element) node2));
                            }
                            if (node2.getNodeName().equals("LINK")) {
                                String s12 = ((Element) node2).getAttribute("href");
                                if (s12 != null) omicelement.setUrl(s12);
                            }
                            if (node2.getNodeName().equals("VALUE")) {
                                vector.add(((Element) node2).getAttribute("name"));
                                vector1.add(XMLElement.getValue((Element) node2));
                            }
                            if (!node2.getNodeName().equals("FEATURE")) continue;
                            NodeList nodelist4 = node2.getChildNodes();
                            String s13 = null;
                            String s14 = null;
                            for (int j1 = 0; j1 < nodelist4.getLength(); j1++) {
                                Node node3 = nodelist4.item(j1);
                                if (node3.getNodeType() != 1) continue;
                                String s15;
                                if (node3.getNodeName().equals("TYPE")) s15 = XMLElement.getValue((Element) node3);
                                if (node3.getNodeName().equals("START")) s13 = XMLElement.getValue((Element) node3);
                                if (node3.getNodeName().equals("END")) s14 = XMLElement.getValue((Element) node3);
                                if (node3.getNodeName().equals("ORIENTATION")) s15 = XMLElement.getValue((Element) node3);
                            }
                            if (s13 == null || s14 == null) continue;
                            if (s10.equals("")) s10 = s10 + s + ":" + s2 + ":" + s13 + "-" + s14; else s10 = s10 + "," + s + ":" + s2 + ":" + s13 + "-" + s14;
                        }
                        if (s7 != null && s8 != null) {
                            Property property1 = osmlBuilder.createProperty();
                            property1.setParam("area");
                            property1.setValue(s + ":" + s2 + ":" + s7 + "-" + s8);
                            omicelement.addProperty(property1);
                        }
                        if (s9 != null) {
                            Property property2 = osmlBuilder.createProperty();
                            property2.setParam("strand");
                            property2.setValue(s9);
                            omicelement.addProperty(property2);
                        }
                        for (int i1 = 0; i1 < vector.size(); i1++) {
                            Property property4 = osmlBuilder.createProperty();
                            property4.setParam((String) vector.get(i1));
                            property4.setValue((String) vector1.get(i1));
                            omicelement.addProperty(property4);
                        }
                        if (!s10.equals("")) {
                            Property property3 = osmlBuilder.createProperty();
                            property3.setParam("feature");
                            property3.setValue(s10);
                            omicelement.addProperty(property3);
                        }
                    }
                }
            }
        } catch (Exception exception) {
            System.out.println("Exception:" + exception.getMessage());
        }
        return osml;
    }
}
