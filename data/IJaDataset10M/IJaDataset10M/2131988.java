package net.sourceforge.processdash.tool.bridge.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sourceforge.processdash.util.HTMLUtils;
import net.sourceforge.processdash.util.RobustFileOutputStream;
import net.sourceforge.processdash.util.XMLUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

public class TeamServerPointerFile {

    public static final String FILE_NAME = "teamServer.xml";

    public static final String BASE_URL_ATTR = "baseURL";

    public static final String INSTANCE_ID_ATTR = "instanceID";

    public static final String INSTANCE_URL_ATTR = "instanceURL";

    private File file;

    private Map<String, ServerEntry> serverEntries;

    private long entriesAsOf;

    public TeamServerPointerFile(File dir) {
        this.file = new File(dir, FILE_NAME);
        try {
            readFile();
        } catch (Exception e) {
            serverEntries = new HashMap<String, ServerEntry>();
        }
    }

    public String toString() {
        return "TeamServerPointerFile[" + file.getPath() + "]";
    }

    public List<String> getInstanceURLs() {
        List<String> result = new ArrayList<String>();
        for (ServerEntry e : serverEntries.values()) {
            result.add(e.getInstanceURL());
        }
        return result;
    }

    public void addServerEntry(String instanceUrl) throws IOException {
        int slashPos = instanceUrl.lastIndexOf('/');
        if (slashPos == -1) throw new IOException("Unexpected URL pattern");
        String serverBaseUrl = instanceUrl.substring(0, slashPos);
        String uniqueID = HTMLUtils.urlDecode(instanceUrl.substring(slashPos + 1));
        Map<String, String> attrs = new HashMap<String, String>();
        attrs.put(TeamServerPointerFile.BASE_URL_ATTR, serverBaseUrl);
        attrs.put(TeamServerPointerFile.INSTANCE_ID_ATTR, uniqueID);
        attrs.put(TeamServerPointerFile.INSTANCE_URL_ATTR, instanceUrl);
        addServerEntry(attrs);
    }

    public void addServerEntry(Map<String, String> attrs) throws IOException {
        maybeRereadFile();
        ServerEntry e = new ServerEntry(attrs);
        String baseURL = e.getBaseURL();
        if (baseURL == null) throw new NullPointerException("baseURL attribute is null");
        serverEntries.put(baseURL, e);
        writeFile();
    }

    public void removeServerEntry(Map<String, String> attrs) throws IOException {
        maybeRereadFile();
        ServerEntry e = new ServerEntry(attrs);
        String baseURL = e.getBaseURL();
        if (serverEntries.remove(baseURL) != null) {
            if (serverEntries.isEmpty()) file.delete(); else writeFile();
        }
    }

    private void readFile() throws Exception {
        InputStream in = new BufferedInputStream(new FileInputStream(file));
        Element doc;
        try {
            doc = XMLUtils.parse(in).getDocumentElement();
        } catch (Exception e) {
            in.close();
            throw e;
        }
        Map<String, ServerEntry> entriesRead = new HashMap<String, ServerEntry>();
        NodeList nl = doc.getElementsByTagName(SERVER_ENTRY_TAG);
        for (int i = 0; i < nl.getLength(); i++) {
            ServerEntry se = new ServerEntry((Element) nl.item(i));
            entriesRead.put(se.getBaseURL(), se);
        }
        serverEntries = entriesRead;
        entriesAsOf = file.lastModified();
    }

    private void maybeRereadFile() throws IOException {
        long currentFileTimestamp = file.lastModified();
        if (currentFileTimestamp > entriesAsOf) {
            try {
                readFile();
            } catch (Exception e) {
            }
        }
    }

    private void writeFile() throws IOException {
        try {
            Writer out = new OutputStreamWriter(new BufferedOutputStream(new RobustFileOutputStream(file, false)), "UTF-8");
            out.write("<?xml version='1.0' encoding='UTF-8'?>\r\n\r\n");
            out.write("<" + SERVER_DOCUMENT_TAG + ">\r\n");
            for (ServerEntry se : serverEntries.values()) {
                se.getAsXML(out);
            }
            out.write("</" + SERVER_DOCUMENT_TAG + ">\r\n");
            out.close();
            entriesAsOf = file.lastModified();
        } catch (IOException ioe) {
            IOException ioee = new IOException("Could not write to file " + file.getPath());
            ioee.initCause(ioe);
            throw ioee;
        }
    }

    private class ServerEntry {

        private Map<String, String> attrs;

        public ServerEntry(Map<String, String> attrs) {
            this.attrs = attrs;
        }

        public ServerEntry(Element e) {
            attrs = new HashMap<String, String>();
            NamedNodeMap incoming = e.getAttributes();
            for (int i = 0; i < incoming.getLength(); i++) {
                Attr a = (Attr) incoming.item(i);
                attrs.put(a.getName(), a.getValue());
            }
        }

        public String getBaseURL() {
            return attrs.get(BASE_URL_ATTR);
        }

        public String getInstanceURL() {
            return attrs.get(INSTANCE_URL_ATTR);
        }

        public void getAsXML(Writer out) throws IOException {
            out.write("\t<");
            out.write(SERVER_ENTRY_TAG);
            for (Map.Entry<String, String> e : attrs.entrySet()) {
                writeAttr(out, e.getKey(), e.getValue());
            }
            out.write("/>\r\n");
        }
    }

    private static void writeAttr(Writer out, String attr, String value) throws IOException {
        if (value != null) {
            out.write(" ");
            out.write(attr);
            out.write("='");
            out.write(XMLUtils.escapeAttribute(value));
            out.write("'");
        }
    }

    private static final String SERVER_DOCUMENT_TAG = "teamServerData";

    private static final String SERVER_ENTRY_TAG = "teamServer";
}
