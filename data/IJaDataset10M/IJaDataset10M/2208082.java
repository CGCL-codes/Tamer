package org.idspace.dk.aau.iwis.graphtransformation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class DynamicGraph {

    private ArrayList<Bidea> oBideaList = new ArrayList<Bidea>();

    private ArrayList<Bedge> oBedgeList = new ArrayList<Bedge>();

    private Hashtable<String, Object> id2shape = new Hashtable<String, Object>();

    public DynamicGraph() {
        super();
        this.oBideaList = new ArrayList<Bidea>();
        this.oBedgeList = new ArrayList<Bedge>();
        this.id2shape = new Hashtable<String, Object>();
    }

    public void update(String xml) {
        String xmlplus = "<newcontent>" + xml + "</newcontent>";
        try {
            InputStream is = new ByteArrayInputStream(xmlplus.getBytes("UTF-8"));
            SAXBuilder builder = new SAXBuilder(false);
            Document doc = builder.build(is);
            Element root = doc.getRootElement();
            List<Element> changelist = root.getChildren();
            System.out.println(root.getName());
            for (Iterator<Element> iter = changelist.iterator(); iter.hasNext(); ) {
                Element child = (Element) iter.next();
                System.out.println(child.getName());
                if (child.getName().equalsIgnoreCase("mxChildChange")) {
                    List<Element> shapelist = child.getChildren();
                    if (shapelist.size() > 0) {
                        for (Iterator<Element> it = shapelist.iterator(); it.hasNext(); ) {
                            Element shape = (Element) it.next();
                            System.out.println("shape:  " + shape.getName());
                            if (shape.getName().equalsIgnoreCase("Rect")) {
                                String label = shape.getAttributeValue("label");
                                String id = shape.getAttributeValue("id");
                                System.out.println(label);
                                System.out.println(id);
                                Bidea idea = new Bidea(id, label);
                                oBideaList.add(idea);
                                id2shape.put(id, idea);
                            } else if (shape.getName().equalsIgnoreCase("Connector")) {
                                String stmt = shape.getAttributeValue("label");
                                String akey = shape.getAttributeValue("id");
                                Element cell = shape.getChild("mxCell");
                                if ((cell.getAttribute("source") != null) && (cell.getAttribute("target") != null)) {
                                    String src = cell.getAttributeValue("source");
                                    String tar = cell.getAttributeValue("target");
                                    Bedge edge = new Bedge(akey, stmt, "default", src, tar);
                                    oBedgeList.add(edge);
                                    id2shape.put(akey, edge);
                                    System.out.println(stmt);
                                    System.out.println(akey);
                                    System.out.println(src);
                                    System.out.println(tar);
                                }
                            }
                        }
                    } else {
                        System.out.println("removing happening! ");
                        String akey = child.getAttributeValue("child");
                        System.out.println("removing " + akey);
                        if (id2shape.get(akey) instanceof Bidea) {
                            Bidea oBidea = (Bidea) id2shape.get(akey);
                            oBideaList.remove(oBidea);
                        } else if (id2shape.get(akey) instanceof Bedge) {
                            Bedge oBedge = (Bedge) id2shape.get(akey);
                            oBedgeList.remove(oBedge);
                        }
                    }
                } else if (child.getName().equalsIgnoreCase("mxValueChange")) {
                    System.out.println("Value modified!");
                    String cell = child.getAttributeValue("cell");
                    String nlabel = child.getAttributeValue("value");
                    if (id2shape.get(cell) instanceof Bidea) {
                        Bidea oBidea = (Bidea) id2shape.get(cell);
                        oBidea.setName(nlabel);
                        for (Bidea e : oBideaList) {
                            if (e.getAkey().equals(cell)) {
                                e.setName(nlabel);
                            }
                        }
                    } else {
                        Bedge oBedge = (Bedge) id2shape.get(cell);
                        oBedge.setStatement(nlabel);
                        for (Bedge e : oBedgeList) {
                            if (e.getAkey().equals(cell)) {
                                e.setStatement(nlabel);
                            }
                        }
                    }
                    System.out.println(cell);
                    System.out.println(nlabel);
                }
            }
        } catch (UnsupportedEncodingException uee) {
            uee.printStackTrace();
        } catch (JDOMException jdome) {
            jdome.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public ArrayList<Bidea> getOBideaList() {
        return oBideaList;
    }

    public void setOBideaList(ArrayList<Bidea> bideaList) {
        oBideaList = bideaList;
    }

    public ArrayList<Bedge> getOBedgeList() {
        return oBedgeList;
    }

    public void setOBedgeList(ArrayList<Bedge> bedgeList) {
        oBedgeList = bedgeList;
    }

    public static void main(String[] args) {
        String xml = "<mxChildChange parent=\"1\" index=\"0\">" + "<Rect label=\"Rectangle\" href=\"\" id=\"c4f857cd4f4461f0aaab4b6d1014982f-16\">" + "<mxCell vertex=\"1\" parent=\"1\">" + "<mxGeometry x=\"230\" y=\"30\" width=\"80\" height=\"40\" as=\"geometry\"/></mxCell></Rect></mxChildChange>" + "<mxChildChange parent=\"1\" index=\"0\">" + "<Rect label=\"Rectangle\" href=\"\" id=\"c4f857cd4f4461f0aaab4b6d1014982f-16\">" + "<mxCell vertex=\"1\" parent=\"1\"><mxGeometry x=\"230\" y=\"30\" width=\"80\" height=\"40\" as=\"geometry\"/>" + "</mxCell></Rect></mxChildChange>" + "<mxChildChange parent=\"1\" index=\"2\">" + "<Rect label=\"Rectangle\" href=\"\" id=\"c4f857cd4f4461f0aaab4b6d1014982f-18\">" + "<mxCell vertex=\"1\" parent=\"1\">" + "<mxGeometry x=\"140\" y=\"160\" width=\"80\" height=\"40\" as=\"geometry\"/>" + "</mxCell></Rect></mxChildChange><mxChildChange parent=\"1\" index=\"3\">" + "<Connector label=\"\" href=\"\" id=\"c4f857cd4f4461f0aaab4b6d1014982f-19\">" + "<mxCell edge=\"1\" parent=\"1\" source=\"c4f857cd4f4461f0aaab4b6d1014982f-16\" target=\"c4f857cd4f4461f0aaab4b6d1014982f-18\">" + "<mxGeometry relative=\"1\" as=\"geometry\"/>" + "</mxCell></Connector></mxChildChange>" + "<mxTerminalChange cell=\"c4f857cd4f4461f0aaab4b6d1014982f-19\" terminal=\"c4f857cd4f4461f0aaab4b6d1014982f-16\" isSource=\"1\"/>" + "<mxTerminalChange cell=\"c4f857cd4f4461f0aaab4b6d1014982f-19\" terminal=\"c4f857cd4f4461f0aaab4b6d1014982f-18\" isSource=\"0\"/>" + " <mxChildChange parent=\"1\" index=\"4\">" + "<Rect label=\"Rectangle\" href=\"\" id=\"c4f857cd4f4461f0aaab4b6d1014982f-20\">" + "<mxCell vertex=\"1\" parent=\"1\">" + "<mxGeometry x=\"360\" y=\"160\" width=\"80\" height=\"40\" as=\"geometry\"/>" + "</mxCell></Rect></mxChildChange><mxChildChange parent=\"1\" index=\"5\">" + "<Connector label=\"\" href=\"\" id=\"c4f857cd4f4461f0aaab4b6d1014982f-21\">" + "<mxCell edge=\"1\" parent=\"1\" source=\"c4f857cd4f4461f0aaab4b6d1014982f-16\" target=\"c4f857cd4f4461f0aaab4b6d1014982f-20\">" + "<mxGeometry relative=\"1\" as=\"geometry\"/>" + "</mxCell></Connector></mxChildChange>" + "<mxTerminalChange cell=\"c4f857cd4f4461f0aaab4b6d1014982f-21\" terminal=\"c4f857cd4f4461f0aaab4b6d1014982f-16\" isSource=\"1\"/>" + "<mxTerminalChange cell=\"c4f857cd4f4461f0aaab4b6d1014982f-21\" terminal=\"c4f857cd4f4461f0aaab4b6d1014982f-20\" isSource=\"0\"/> " + "<mxValueChange cell=\"c4f857cd4f4461f0aaab4b6d1014982f-16\" value=\"idea 1\"/>" + "<mxValueChange cell=\"c4f857cd4f4461f0aaab4b6d1014982f-18\" value=\"idea 2\"/>" + "<mxValueChange cell=\"c4f857cd4f4461f0aaab4b6d1014982f-20\" value=\"idea 3\"/>" + "<mxValueChange cell=\"c4f857cd4f4461f0aaab4b6d1014982f-19\" value=\"hello\"/>" + "<mxStyleChange cell=\"c4f857cd4f4461f0aaab4b6d1014982f-21\" style=\"verticalConnector\"/>" + "<mxGeometryChange cell=\"c4f857cd4f4461f0aaab4b6d1014982f-21\">" + "<mxGeometry relative=\"1\" as=\"geometry\"><Array as=\"points\"/>" + "</mxGeometry></mxGeometryChange>" + "<mxValueChange cell=\"c4f857cd4f4461f0aaab4b6d1014982f-21\" value=\"world\"/>" + "<mxChildChange parent=\"1\" index=\"6\"><Rect label=\"idea 2\" href=\"\" id=\"c4f857cd4f4461f0aaab4b6d1014982f-22\">" + "<mxCell vertex=\"1\" parent=\"1\"><mxGeometry x=\"160\" y=\"310\" width=\"80\" height=\"40\" as=\"geometry\"/>" + "</mxCell>" + "</Rect></mxChildChange>" + "<mxChildChange parent=\"1\" index=\"7\">" + "<Connector label=\"\" href=\"\" id=\"c4f857cd4f4461f0aaab4b6d1014982f-23\">" + "<mxCell edge=\"1\" parent=\"1\" source=\"c4f857cd4f4461f0aaab4b6d1014982f-18\" target=\"c4f857cd4f4461f0aaab4b6d1014982f-22\">" + "<mxGeometry relative=\"1\" as=\"geometry\"/></mxCell></Connector></mxChildChange>" + "<mxTerminalChange cell=\"c4f857cd4f4461f0aaab4b6d1014982f-23\" terminal=\"c4f857cd4f4461f0aaab4b6d1014982f-18\" isSource=\"1\"/>" + "<mxTerminalChange cell=\"c4f857cd4f4461f0aaab4b6d1014982f-23\" terminal=\"c4f857cd4f4461f0aaab4b6d1014982f-22\" isSource=\"0\"/>" + "<mxGeometryChange cell=\"c4f857cd4f4461f0aaab4b6d1014982f-18\">" + "<mxGeometry x=\"160\" y=\"170\" width=\"80\" height=\"40\" as=\"geometry\"/></mxGeometryChange>" + "<mxChildChange parent=\"1\" index=\"8\"><Rect label=\"idea 2\" href=\"\" id=\"c4f857cd4f4461f0aaab4b6d1014982f-24\">" + "<mxCell vertex=\"1\" parent=\"1\">" + "<mxGeometry x=\"350\" y=\"310\" width=\"80\" height=\"40\" as=\"geometry\"/>" + "</mxCell></Rect></mxChildChange>" + "<mxChildChange parent=\"1\" index=\"9\">" + "<Connector label=\"\" href=\"\" id=\"c4f857cd4f4461f0aaab4b6d1014982f-25\">" + "<mxCell edge=\"1\" parent=\"1\" source=\"c4f857cd4f4461f0aaab4b6d1014982f-18\" target=\"c4f857cd4f4461f0aaab4b6d1014982f-24\">" + "<mxGeometry relative=\"1\" as=\"geometry\"/></mxCell></Connector></mxChildChange>" + "<mxTerminalChange cell=\"c4f857cd4f4461f0aaab4b6d1014982f-25\" terminal=\"c4f857cd4f4461f0aaab4b6d1014982f-18\" isSource=\"1\"/>" + "<mxTerminalChange cell=\"c4f857cd4f4461f0aaab4b6d1014982f-25\" terminal=\"c4f857cd4f4461f0aaab4b6d1014982f-24\" isSource=\"0\"/>" + "<mxValueChange cell=\"c4f857cd4f4461f0aaab4b6d1014982f-22\" value=\"idea 4\"/>" + "<mxValueChange cell=\"c4f857cd4f4461f0aaab4b6d1014982f-24\" value=\"idea 5\"/>";
        String xml2 = "<mxChildChange isAdded=\"0\" child=\"c4f857cd4f4461f0aaab4b6d1014982f-19\"/>" + "<mxChildChange isAdded=\"0\" child=\"c4f857cd4f4461f0aaab4b6d1014982f-23\"/>" + "<mxChildChange isAdded=\"0\" child=\"c4f857cd4f4461f0aaab4b6d1014982f-25\"/>" + "<mxChildChange isAdded=\"0\" child=\"c4f857cd4f4461f0aaab4b6d1014982f-18\"/>";
        String delxml = "<mxChildChange isAdded=\"0\" child=\"c4f857cd4f4461f0aaab4b6d1014982f-3\"/>";
        DynamicGraph odg = new DynamicGraph();
        odg.update(xml);
        for (Bidea idea : odg.getOBideaList()) {
            System.out.println(idea.toString());
        }
        for (Bedge edge : odg.getOBedgeList()) {
            System.out.println(edge.toString());
        }
        System.out.println("\n second update:");
        odg.update(xml2);
        for (Bidea idea : odg.getOBideaList()) {
            System.out.println(idea.toString());
        }
        for (Bedge edge : odg.getOBedgeList()) {
            System.out.println(edge.toString());
        }
    }
}
