package cn.edu.nju.software.sd.torm;

import java.io.*;
import java.util.List;
import java.util.Iterator;
import org.jdom.*;
import org.jdom.input.*;
import org.jdom.xpath.XPath;

/**
 * Text exportor,to export a human-readable text file .
 * 
 * @author xingsheng
 */
public class TextExportor {

    private String xmlPath = "Mapping.xml";

    private SAXBuilder builder;

    private Document doc;

    private Element database;

    private PrintWriter print;

    private String path;

    /**
	 * Creates a new instance of TextExportor
	 * 
	 * @param path
	 *            the path to export.
	 */
    public TextExportor(String path) {
        this.path = path;
        builder = new SAXBuilder(false);
        try {
            doc = builder.build(xmlPath);
            database = doc.getRootElement();
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Export option
	 * 
	 * @param boo
	 *            boolean if current mapping strategy is table-per-
	 *            hierarchy,set boo values true.else table-per-subclass,boo
	 *            values false.
	 */
    public void export() {
        try {
            FileWriter filewriter = new FileWriter(this.path);
            print = new PrintWriter(filewriter);
            print.println("database : " + database.getName());
            print.println();
            List l = database.getChildren("class");
            Iterator iterator = l.iterator();
            while (iterator.hasNext()) {
                Element element = (Element) iterator.next();
                if (element.getAttributeValue("strategy").equalsIgnoreCase("table-per-hierarchy")) hieprintOnetable(element); else if (element.getAttributeValue("strategy").equalsIgnoreCase("table-per-subclass")) subprintOnecollection(element);
            }
            filewriter.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    private void hieprintOnetable(Element e) {
        print.println(e.getAttributeValue("table"));
        try {
            List collist = XPath.selectNodes(e, "//column");
            Iterator i = collist.iterator();
            while (i.hasNext()) {
                Element element = (Element) i.next();
                if (ishere(element.getParentElement().getParentElement(), e)) {
                    print.println(element.getAttributeValue("name") + ": " + element.getAttributeValue("type"));
                }
            }
            Element element = e.getChild("discriminator");
            print.println(element.getAttributeValue("column") + ": " + element.getAttributeValue("type"));
            print.println();
        } catch (JDOMException j) {
            j.printStackTrace();
        }
    }

    private boolean ishere(Element gra, Element root) {
        Element tojudge = gra;
        boolean ishere = false;
        if (tojudge.getName().equals("class")) {
            if (tojudge.equals(root)) {
                ishere = true;
            } else ishere = false;
        } else {
            ishere = ishere(tojudge.getParentElement(), root);
        }
        return ishere;
    }

    private void subprintOnecollection(Element e) {
        Element idcol = e.getChild("id").getChild("column");
        try {
            List sub = XPath.selectNodes(e, "//*[@table]");
            Iterator itr = sub.iterator();
            while (itr.hasNext()) {
                Element ele = (Element) itr.next();
                if (ishere(ele, e)) {
                    printOnetable(ele, idcol);
                }
            }
            print.println();
        } catch (JDOMException j) {
        }
    }

    private void printOnetable(Element e, Element idcol) {
        print.println(e.getAttributeValue("table"));
        print.println(idcol.getAttributeValue("name") + ": " + idcol.getAttributeValue("type"));
        List pro = e.getChildren("property");
        Iterator itr = pro.iterator();
        while (itr.hasNext()) {
            Element ele = ((Element) itr.next()).getChild("column");
            print.println(ele.getAttributeValue("name") + ": " + ele.getAttributeValue("type"));
        }
    }
}
