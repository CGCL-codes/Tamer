package org.juddi.base;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.*;
import java.io.*;
import java.net.URL;
import org.juddi.UDDIException;
import org.juddi.base.*;
import org.juddi.request.*;
import org.juddi.response.*;

/**
 * OverviewURLHandler
 *
 * @author  Alex Ceponkus
 * @author  Steve Viens
 * @author  Graeme Riddell
 * @author  Chris Dellario
 * @version 0.1 9/14/2000
 * @since   JDK1.2.2
 */
public class OverviewURLHandler implements UDDIXMLHandler {

    public OverviewURLHandler() {
    }

    public Object create(Node node) throws UDDIException {
        URL url = null;
        Element element = (Element) node;
        try {
            url = new URL(element.getChildNodes().item(0).getNodeValue());
        } catch (java.net.MalformedURLException e) {
            System.out.println(e.getMessage());
        }
        return url;
    }

    /**
   * test driver
   */
    public static void main(String args[]) {
        final String xmlFile = "temp.xml";
        final String xml = "<overviewUrl>http://www.xx.com</overviewUrl>";
        try {
            BufferedWriter f = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(xmlFile)));
            f.write(xml);
            f.close();
            System.out.println(xml + "\n");
            DOMParser parser = new DOMParser();
            parser.parse(xmlFile);
            Document doc = parser.getDocument();
            Element element = doc.getDocumentElement();
            OverviewURLHandler h = new OverviewURLHandler();
            URL obj = (URL) h.create(element);
            System.out.println("\n" + obj.toString());
        } catch (Exception e) {
            System.out.println("caught exception in main: " + e.getMessage());
        }
    }
}
