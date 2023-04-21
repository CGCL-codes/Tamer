package org.hsy.util.xml;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class XMLToXmlMap {

    protected Hashtable hashcontents = null;

    public static String packageName = "com.zte.womcb.yangv.xml.opr";

    private String genClassName(String name) {
        String _name = name.toLowerCase();
        return _name.substring(0, 1).toUpperCase() + _name.substring(1);
    }

    private String genFiledName(String name) {
        return name.toLowerCase();
    }

    private String genFeildsName(String name) {
        return name.toLowerCase().toLowerCase() + "s";
    }

    public XMLToXmlMap(String xmlfile) {
        SAXBuilder builder = new SAXBuilder(false);
        Document document = null;
        try {
            document = builder.build(new File(xmlfile));
        } catch (JDOMException e1) {
            System.out.println("The file '" + xmlfile + "' cann't be parsed!");
            e1.printStackTrace();
        } catch (IOException e1) {
            System.out.println("The file '" + xmlfile + "' cann't be parsed!");
            e1.printStackTrace();
        }
        if (document == null) {
            hashcontents = new Hashtable();
            return;
        }
        Element element = document.getRootElement();
        System.out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        System.out.println("<mapping>");
        dealNodeb(element);
        System.out.println("</mapping>");
    }

    public void dealNodeb(Element element) {
        parseNodeText(element);
        parseNodeAttr(element);
        parseNodeChild(element);
        System.out.println("</class>");
        System.out.println("");
        genChild(element);
    }

    public void genChild(Element element) {
        List list = element.getChildren();
        HashMap sigleChilds = new HashMap();
        HashMap mutiChiads = new HashMap();
        for (int i = 0; i < list.size(); i++) {
            Element childrenElement = (Element) list.get(i);
            if (childrenElement.getText() != null && childrenElement.getText().length() != 0 && childrenElement.getChildren().size() == 0) {
                continue;
            }
            String name = childrenElement.getName();
            if (sigleChilds.containsKey(name)) {
                sigleChilds.remove(name);
                mutiChiads.put(name, childrenElement);
            } else {
                sigleChilds.put(name, childrenElement);
            }
        }
        Iterator iterator = sigleChilds.keySet().iterator();
        while (iterator.hasNext()) {
            Element childrenElement = (Element) sigleChilds.get(iterator.next());
            dealNodeb(childrenElement);
        }
        iterator = mutiChiads.keySet().iterator();
        while (iterator.hasNext()) {
            Element childrenElement = (Element) mutiChiads.get(iterator.next());
            dealNodeb(childrenElement);
        }
    }

    public void parseNodeChild(Element element) {
        List list = element.getChildren();
        HashSet sigleChilds = new HashSet();
        HashSet mutiChiads = new HashSet();
        for (int i = 0; i < list.size(); i++) {
            Element childrenElement = (Element) list.get(i);
            if (childrenElement.getText() != null && childrenElement.getText().length() != 0 && childrenElement.getChildren().size() == 0) {
                System.out.println("  <field name=\"" + genFiledName(childrenElement.getName()) + "\">");
                System.out.println("    <bind-xml name=\"" + childrenElement.getName() + "\" node=\"attribute\"/>");
                System.out.println("  </field>");
                continue;
            }
            String name = childrenElement.getName();
            if (sigleChilds.contains(name)) {
                sigleChilds.remove(name);
                mutiChiads.add(name);
            } else {
                sigleChilds.add(name);
            }
        }
        Object[] sigleObjs = sigleChilds.toArray();
        for (int i = 0; i < sigleObjs.length; i++) {
            System.out.println("  <field name=\"" + genFiledName(sigleObjs[i].toString()) + "\" type=\"" + packageName + "." + genClassName(sigleObjs[i].toString()) + "\">");
            System.out.println("    <bind-xml name=\"" + sigleObjs[i].toString() + "\" node=\"element\"/>");
            System.out.println("  </field>");
        }
        Object[] mutiObjs = mutiChiads.toArray();
        for (int i = 0; i < mutiObjs.length; i++) {
            System.out.println("  <field name=\"" + genFeildsName(mutiObjs[i].toString()) + "\" collection=\"collection\" type=\"" + packageName + "." + genClassName(mutiObjs[i].toString()) + "\">");
            System.out.println("    <bind-xml name=\"" + mutiObjs[i].toString() + "\" node=\"element\"/>");
            System.out.println("  </field>");
        }
    }

    public void parseNodeAttr(Element element) {
        List list = element.getAttributes();
        for (int i = 0; i < list.size(); i++) {
            Attribute attr = (Attribute) list.get(i);
            System.out.println("  <field name=\"" + genFiledName(attr.getName()) + "\">");
            System.out.println("    <bind-xml name=\"" + attr.getName() + "\" node=\"attribute\"/>");
            System.out.println("  </field>");
        }
    }

    public void parseNodeText(Element element) {
        System.out.println("<class name=\"" + packageName + "." + genClassName(element.getName()) + "\">");
        System.out.println("<map-to xml=\"" + element.getName() + "\"/>");
    }

    public static void main(String args[]) {
        new XMLToXmlMap("xml-test.xml");
    }
}
