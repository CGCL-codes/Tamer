package org.nbrowse.io;

import java.io.*;
import java.util.*;
import java.net.*;
import net.n3.nanoxml.*;
import java.util.zip.ZipInputStream;
import java.util.zip.GZIPInputStream;
import org.nbrowse.dataset.GraphEltSet;
import org.nbrowse.dataset.NBNode;
import org.nbrowse.io.database.SFSInputStream;

public class NodeAttrXmlParser {

    private GraphEltSet graphEltSet;

    String nodeAttr;

    public NodeAttrXmlParser(GraphEltSet graphEltSet, String nodeAttr) {
        this.graphEltSet = graphEltSet;
        this.nodeAttr = nodeAttr;
    }

    public void read(URL xmlURL) throws Exception {
        read(xmlURL, null);
    }

    public void read(String query) throws Exception {
        URL u = UrlManager.inst().getNodeAttrXmlUrl(query);
        read(u);
    }

    public void read(URL url, Thread afterReading) throws Exception {
        URLConnection xmlConn = url.openConnection();
        SFSInputStream sfsInputStream = new SFSInputStream(xmlConn.getInputStream(), xmlConn.getContentLength());
        read(url.toString(), sfsInputStream, afterReading);
    }

    public void read(String fileName, InputStream in, Thread afterReading) throws Exception {
        InputStream xmlStream;
        if (fileName.toLowerCase().endsWith(".zip")) {
            xmlStream = new ZipInputStream(in);
            ((ZipInputStream) xmlStream).getNextEntry();
        } else if (fileName.toLowerCase().endsWith(".gz")) {
            xmlStream = new GZIPInputStream(in);
        } else {
            xmlStream = in;
        }
        IXMLParser parser = new StdXMLParser();
        parser.setBuilder(new StdXMLBuilder());
        parser.setValidator(new NonValidator());
        StdXMLReader reader = new StdXMLReader(xmlStream);
        parser.setReader(reader);
        IXMLElement tglbXML = null;
        try {
            tglbXML = (IXMLElement) parser.parse();
        } catch (Exception e) {
            System.out.println("LINE " + reader.getLineNr());
            e.printStackTrace();
        }
        xmlStream.close();
        parseToNodeAttr(tglbXML, afterReading);
    }

    private boolean getBooleanAttr(IXMLElement elt, String name, boolean def) {
        String value = elt.getAttribute(name, def ? "true" : "false");
        return value.toLowerCase().equals("true");
    }

    private void parseToNodeAttr(IXMLElement tglbXML, Thread afterReading) throws TGException {
        IXMLElement nodeSet = (IXMLElement) tglbXML.getChildrenNamed("NODESET").firstElement();
        Enumeration nodeEnum = (nodeSet).enumerateChildren();
        while (nodeEnum.hasMoreElements()) {
            IXMLElement xmlNode = (IXMLElement) (nodeEnum.nextElement());
            String nodeID = xmlNode.getAttribute("nodeID", null);
            if (nodeID == null) throw new TGException(TGException.NODE_NO_ID, "node has no ID");
            NBNode node = graphEltSet.findNodeById(nodeID);
            Vector v;
            if (node != null) {
                String phenoTypeList = null;
                v = xmlNode.getChildrenNamed("NODE_ATTR");
                if (!v.isEmpty()) {
                    IXMLElement nodePheno = (IXMLElement) v.firstElement();
                    phenoTypeList = nodePheno.getAttribute("attr", "");
                    node.setNodeAttr(nodeAttr, phenoTypeList);
                }
            }
        }
    }
}
