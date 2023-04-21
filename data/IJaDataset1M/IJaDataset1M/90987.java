package org.nbrowse.io;

import java.io.*;
import java.util.*;
import java.net.*;
import java.security.AccessControlException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.n3.nanoxml.*;
import java.util.zip.ZipInputStream;
import java.util.zip.GZIPInputStream;
import org.nbrowse.dataset.EdgeTypeMetaData;
import org.nbrowse.dataset.EdgeDataSetMetaData;
import org.nbrowse.dataset.MetaData;
import org.nbrowse.dataset.NodeAttrMetaData;
import org.nbrowse.io.database.SFSInputStream;

/**
  steup the server schema location
  reading the data schema and processing display
 * Calls an url (or from file or whatnot) that has xml for nbrowse meta data
 * that is Link types and datasets
 * and populates EdgeTypeMetaData & EdgeDataSetMetaData classes
 * meta information about edges: types & datasets for a given species/taxon
 * (the edge filter then uses these)
 * node meta data disabled for now
 * TouchGraphXmlAdapter invokes this class.
 * Since TouchGraphXmlAdapter is no longer being used by the jdbc adapter, this class is not longer used either.
 * @author Huey-Ling Kao (ported over from nbrowse 1)
 * used to be called SchemaIO
*/
public class TGXmlMetaDataReader {

    private static TGXmlMetaDataReader singleton;

    private TGXmlMetaDataReader() {
    }

    public static TGXmlMetaDataReader inst() {
        if (singleton == null) singleton = new TGXmlMetaDataReader();
        return singleton;
    }

    public MetaData loadMetaData(int taxonId) throws IOException {
        URL metaDataUrl = UrlManager.inst().getMetaDataUrl(taxonId);
        return read(metaDataUrl);
    }

    private MetaData read(URL url) throws IOException {
        if (!accessAllowed(url)) {
            return readCachedMetaDataUrl();
        }
        InputStream sfsInputStream = makeSfsStream(url);
        String filename = url.toString();
        sfsInputStream = zipCheck(filename, sfsInputStream);
        if (inputHasFailMessage(sfsInputStream)) {
            System.out.println(filename + " failed to give metadata");
            return readCachedMetaDataUrl();
        } else {
            return read(sfsInputStream);
        }
    }

    private MetaData readCachedMetaDataUrl() throws IOException {
        URL url = UrlManager.inst().getCachedMetaDataUrl();
        System.out.println("Trying cached xml: " + url);
        if (url == null) {
            throw new IOException("Cached metadata xml failed, cant get metadata");
        }
        InputStream sfsInputStream = makeSfsStream(url);
        return read(sfsInputStream);
    }

    private boolean accessAllowed(URL url) {
        try {
            url.openConnection().getInputStream();
        } catch (AccessControlException e) {
            System.out.println("Access to " + url + " is not allowed " + e);
            return false;
        } catch (IOException x) {
            System.out.println("Access to " + url + " is not allowed " + x);
            return false;
        }
        return true;
    }

    private SFSInputStream makeSfsStream(URL url) throws IOException {
        URLConnection xmlConn = url.openConnection();
        return new SFSInputStream(xmlConn.getInputStream(), xmlConn.getContentLength());
    }

    /** Check if fileName has .zip or .gzip suffix and if so then wraps
     * ImputStream in Zip/GZIPInputStream
     */
    private InputStream zipCheck(String fileName, InputStream in) throws IOException {
        if (fileName.toLowerCase().endsWith(".zip")) {
            ZipInputStream z = new ZipInputStream(in);
            z.getNextEntry();
            return z;
        } else if (fileName.toLowerCase().endsWith(".gz")) {
            return new GZIPInputStream(in);
        } else {
            return in;
        }
    }

    /** if schema.jsp fails it returns "\nfail\n", check for this case 
   * and if so return true, otherwise return false
   */
    private boolean inputHasFailMessage(InputStream in) throws IOException {
        LineNumberReader failCheck = new LineNumberReader(new InputStreamReader(in));
        String s = failCheck.readLine();
        while (s != null && failCheck.getLineNumber() < 3) {
            if (s.contains("fail")) return true;
            s = failCheck.readLine();
        }
        return false;
    }

    private MetaData read(InputStream in) throws IOException {
        IXMLParser parser = new StdXMLParser();
        parser.setBuilder(new StdXMLBuilder());
        parser.setValidator(new NonValidator());
        StdXMLReader reader = new StdXMLReader(in);
        parser.setReader(reader);
        IXMLElement tglbXML = null;
        try {
            tglbXML = (IXMLElement) parser.parse();
        } catch (Exception e) {
            System.out.println("LINE " + reader.getLineNr());
            e.printStackTrace();
        }
        in.close();
        return makeMetadataFromXml(tglbXML);
    }

    private boolean getBooleanAttr(IXMLElement elt, String name, boolean def) {
        String value = elt.getAttribute(name, def ? "true" : "false");
        return value.toLowerCase().equals("true");
    }

    /** this also builds schema node attributes - commenting out for now
   * currently using jdbc instead - but may bring back...
   * @param tglbXML xml from schema query
   * @param afterReading
   */
    private MetaData makeMetadataFromXml(IXMLElement tglbXML) {
        MetaData metaData = new MetaData();
        IXMLElement serverLocation = (IXMLElement) tglbXML.getChildrenNamed("SERVER_LOCATION").firstElement();
        String serverURL = serverLocation.getAttribute("url", "");
        UrlManager.inst().setServerUrl(serverURL);
        IXMLElement nodeAttrSetXml = tglbXML.getChildrenNamed("PARAM_NODE_SET").firstElement();
        for (IXMLElement paramNS : nodeAttrSetXml.getChildren()) {
            String nodeAttrName = paramNS.getAttribute("name", null);
            String nodeAttrType = paramNS.getAttribute("type", null);
            String nodeAttrMin = paramNS.getAttribute("min", null);
            String nodeAttrMax = paramNS.getAttribute("max", null);
            boolean nodeAttrRFlag = getBooleanAttr(paramNS, "rFlag", false);
            NodeAttrMetaData nodeMeta = new NodeAttrMetaData(nodeAttrName, nodeAttrType);
            if (nodeAttrMin != null && nodeAttrMax != null) {
                try {
                    double min = new Double(nodeAttrMin).doubleValue();
                    double max = new Double(nodeAttrMax).doubleValue();
                    nodeMeta.setMin(min);
                    nodeMeta.setMax(max);
                    nodeMeta.setIsReverse(nodeAttrRFlag);
                } catch (NumberFormatException nfe) {
                    System.out.println("Max/min for node meta data not # " + nfe);
                }
            }
            metaData.addNodeAttrMetaData(nodeMeta);
        }
        IXMLElement edgeTypeSet = (IXMLElement) tglbXML.getChildrenNamed("PARAM_EDGE_SET").firstElement();
        Enumeration edgeTypeSetEnum = edgeTypeSet.enumerateChildren();
        while (edgeTypeSetEnum.hasMoreElements()) {
            EdgeTypeMetaData edgeMeta;
            IXMLElement paramES = (IXMLElement) (edgeTypeSetEnum.nextElement());
            String edgeTypeName = paramES.getAttribute("name", null);
            String edgeTypeDesc = paramES.getAttribute("desc", null);
            int dataSet_num = paramES.getAttribute("dataSet_num", 0);
            boolean directed = getBooleanAttr(paramES, "Directed", false);
            if (edgeTypeName == null) System.out.println("EDGE_PARAM without name attribute!"); else {
                edgeMeta = new EdgeTypeMetaData(edgeTypeName);
                edgeMeta.setDirected(directed);
                edgeMeta.setDesc(edgeTypeDesc);
                Vector v;
                for (int i = 1; i <= dataSet_num; i++) {
                    EdgeDataSetMetaData dsa = new EdgeDataSetMetaData(edgeMeta);
                    String childEleName = "dataSet" + i;
                    v = paramES.getChildrenNamed(childEleName);
                    if (!v.isEmpty()) {
                        IXMLElement dataSet_O = (IXMLElement) v.firstElement();
                        String s = dataSet_O.getAttribute("name", null);
                        String sd = dataSet_O.getAttribute("desc", null);
                        dsa.setName(s);
                        dsa.setDesc(sd);
                        dsa.setHasRange(getBooleanAttr(dataSet_O, "cut_off", false));
                        if (dsa.hasRange() == true) {
                            dsa.setMax(new Double(dataSet_O.getAttribute("max", null)).doubleValue());
                            dsa.setMin(new Double(dataSet_O.getAttribute("min", null)).doubleValue());
                        }
                    }
                }
                metaData.addEdgeType(edgeMeta);
            }
        }
        return metaData;
    }
}
