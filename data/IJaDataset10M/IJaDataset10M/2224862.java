package net.bionicmessage.groupdav;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.logging.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 *
 * @author matt
 */
public class CardDAVExtensions implements IDAVHandler, ServerDriver {

    /** Constant static String */
    public static final String addressData = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>" + "<C:addressbook-query xmlns:D=\"DAV:\"" + " xmlns:C=\"urn:ietf:params:xml:ns:carddav\">" + "<D:prop><D:getetag/><C:address-data/></D:prop>" + "</C:addressbook-query>";

    private groupDAV parent;

    private Logger log;

    private URL server;

    private boolean ready = false;

    private Exception retException = null;

    /**
     * Creates an instance of CardDAVExtensions.
     * @param server Server to target
     * @param base64cache HTTP Authorization line
     * @param log Logger instance to use.
     */
    public CardDAVExtensions(URL server, groupDAV parent, Logger log) {
        init(server, parent, log);
    }

    public void init(URL server, groupDAV parent, Logger l) {
        this.parent = parent;
        this.server = server;
        log = l;
    }

    public boolean doesSupportCardDAV(URL fullURL) throws Exception {
        byte[] query = Common.buildQuery("OPTIONS", fullURL, new byte[0], null, parent);
        String response = Common.sendNonKeepAliveRequest(fullURL, query, parent, log);
        GroupDAVObject rbo = new GroupDAVObject(response, GroupDAVObject.OBJECT_GET);
        String davHeader = (String) rbo.getHeaders().get("dav");
        return davHeader.contains("addressbook");
    }

    public List<GroupDAVObject> pullCalendarDataForPath(URL url) throws Exception {
        HashMap headers = new HashMap();
        headers.put("Depth", "1");
        byte[] query = Common.buildQuery("REPORT", url, addressData.getBytes("UTF-8"), headers, parent);
        String resp = Common.sendNonKeepAliveRequest(url, query, parent, Logger.getLogger("bsh"));
        HTTPInputStream in = Common.sendNonKeepAliveRequest(url, query);
        return extractFromReport(in, new ArrayList());
    }

    public List<GroupDAVObject> doMultiget(URL root, List<String> toDownload, List<GroupDAVObject> addTo) throws Exception {
        StringBuffer reportRequest = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
        reportRequest.append("<C:addressbook-multiget xmlns:D=\"DAV:\"");
        reportRequest.append(" xmlns:C=\"urn:ietf:params:xml:ns:carddav\">\r\n");
        for (String str : toDownload) {
            reportRequest.append("<D:href>");
            reportRequest.append(str);
            reportRequest.append("</D:href>\r\n");
        }
        reportRequest.append("<D:prop><D:getetag/><C:address-data/></D:prop>");
        reportRequest.append("</C:addressbook-multiget>");
        HashMap headers = new HashMap();
        headers.put("Depth", "1");
        byte[] query = Common.buildQuery("REPORT", root, reportRequest.toString().getBytes("UTF-8"), headers, parent);
        HTTPInputStream his = Common.sendNonKeepAliveRequest(server, query);
        return extractFromReport(his, addTo);
    }

    protected List<GroupDAVObject> extractFromReport(HTTPInputStream in, List<GroupDAVObject> addTo) throws Exception {
        ready = false;
        retException = null;
        XMLReader parser = XMLReaderFactory.createXMLReader();
        SAXDAVHandler ceh = new SAXDAVHandler(this);
        parser.setContentHandler(ceh);
        parser.parse(new InputSource(in));
        do {
        } while (!ready);
        if (retException != null) {
            throw retException;
        }
        Stack<String> hrefStack = ceh.hrefs;
        Stack<String> etagsStack = ceh.etags;
        Stack<String> contentsStack = ceh.contents;
        while (!hrefStack.isEmpty()) {
            String href = hrefStack.pop();
            URL modifiedHref = new URL(server, href);
            String etag = etagsStack.pop();
            String contents = contentsStack.pop();
            GroupDAVObject child = new GroupDAVObject(in, contents, etag, modifiedHref.toExternalForm());
            addTo.add(child);
        }
        return addTo;
    }

    /** Method for IDAVHandler */
    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public boolean getReady() {
        return this.ready;
    }

    public void setRetException(Exception retException) {
        this.retException = retException;
    }
}
