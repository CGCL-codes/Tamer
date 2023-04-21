package hypercast;

import java.net.*;
import java.io.*;
import java.util.*;
import hypercast.util.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import java.text.ParseException;

public final class HTTP_Server {

    /**
	 * Debug flag.
	 */
    public static final boolean debug = true;

    /** Number of threads to handle HTTP requests. */
    private static int NUM_OF_HTTP_THREADS = 2;

    /** Statistic. */
    private int numberOfRequests;

    /** Statistic. */
    private int numberOfResponses;

    /** Statistic. */
    private int numberOfErrorResponses;

    /** Statistic. */
    private int maxNumberOfRequestsInAnySecond;

    /** Statistic. */
    private int maxNumberOfResponsesInAnySecond;

    /** Vector of Long.  Times of requests - ordered from oldest to newest. */
    private Vector requestTimeQueue;

    /** Vector of Long.  Times of requests - ordered from oldest to newest. */
    private Vector responseTimeQueue;

    /** Socket for new connections. */
    public ServerSocket serverSocket;

    /** Required argument to constructor of java.net.ServerSocket */
    private static final int MAX_BACKLOG = 2;

    /** used to generate unique Overlay ID */
    static String uniquePrefix;

    /** to handle the overlay request */
    HTTP_ServerOverlayHandler overlayHandler;

    /** Attempts to open a ServerSocket at <code>port</code>.   
 * If it fails to open the socket, it exits. */
    HTTP_Server(int port) {
        numberOfRequests = 0;
        numberOfResponses = 0;
        numberOfErrorResponses = 0;
        maxNumberOfRequestsInAnySecond = 0;
        maxNumberOfResponsesInAnySecond = 0;
        requestTimeQueue = new Vector();
        responseTimeQueue = new Vector();
        try {
            serverSocket = new ServerSocket(port, MAX_BACKLOG);
        } catch (IOException ioe) {
            throw new HyperCastFatalRuntimeException("Unable to open server socket.");
        }
        InetAddress localHost = null;
        try {
            localHost = InetAddress.getLocalHost();
        } catch (UnknownHostException uhe) {
            throw new HyperCastFatalRuntimeException("InetAddress.getLocalHost() threw UnknownHostException.");
        }
        uniquePrefix = localHost.getHostAddress() + ":" + serverSocket.getLocalPort();
        overlayHandler = new HTTP_ServerOverlayHandler(uniquePrefix);
    }

    /** Initializes and starts the web server.    
 * The overlayHandler takes care of overlay requests.
 */
    public static void main(String[] args) {
        if (args.length > 1) {
            System.err.println("Too many arguments!");
            System.err.println("USAGE: java HTTP_Server [port#]");
            System.exit(1);
        }
        int port = 0;
        try {
            if (args.length > 0) port = Integer.parseInt(args[0]);
        } catch (NumberFormatException nfe) {
            System.err.println("Error trying to parse argument as number:" + nfe.getMessage());
            System.err.println("USAGE: java HTTP_Server [port#]");
            System.exit(1);
        }
        HTTP_Server ws = new HTTP_Server(port);
        String serverPrefix = "http://" + uniquePrefix;
        System.out.println("HTTPServer is set to " + serverPrefix);
        System.out.println("......");
        for (int i = 0; i < NUM_OF_HTTP_THREADS - 1; i++) {
            HTTP_ServerClientSocketManager scsm = new HTTP_ServerClientSocketManager(ws);
            Thread th = new Thread(scsm);
            th.start();
        }
        HTTP_ServerClientSocketManager scsm = new HTTP_ServerClientSocketManager(ws);
        scsm.run();
    }

    /** Accept a connection from the server socket. 
* For some reason, Java doesn't list <code>accept</code> as a 
* synchronized method. */
    public synchronized Socket acceptConnection() throws IOException {
        return serverSocket.accept();
    }

    /** Accepts HTTP requests and passes them to the overlayHandler. */
    public String processQuery(StringTokenizer queryLineTokens) {
        long currentTime = System.currentTimeMillis();
        numberOfRequests++;
        requestTimeQueue.addElement(new Long(currentTime));
        purgeQueues(currentTime);
        if (queryLineTokens.nextToken().equals("GET")) {
            String request = queryLineTokens.nextToken();
            if (debug) System.out.println("receive request with length " + request.length() + ".");
            if ("/".equals(request) || "/index.html".equals(request)) {
                numberOfResponses++;
                responseTimeQueue.addElement(new Long(System.currentTimeMillis()));
                return "HTTP/1.0 200 test\r\nExpires: 0\r\n\r\n" + homePage();
            } else if ("/stats.html".equals(request)) {
                numberOfResponses++;
                responseTimeQueue.addElement(new Long(System.currentTimeMillis()));
                return "HTTP/1.0 200 test\r\nExpires: 0\r\n\r\n" + statsPage();
            }
            int startOfParams = request.indexOf('?');
            if (startOfParams < 0) {
                numberOfErrorResponses++;
                return "HTTP/1.0 400 test\r\n\r\n" + "ERROR: Not a CGI request; only CGI requests supported." + "\n";
            }
            String attributeString = request.substring(startOfParams + 1, request.length());
            Hashtable attributes;
            try {
                attributes = parseAttributes(attributeString);
            } catch (IllegalArgumentException iae) {
                numberOfErrorResponses++;
                return "HTTP/1.0 400 test\r\n\r\n" + "ERROR: Poorly formated CGI arguments." + "\n";
            }
            String reply = overlayHandler.generateResponse(attributes);
            if (reply != null) {
                if (reply.startsWith("ERROR")) {
                    numberOfErrorResponses++;
                } else {
                    numberOfResponses++;
                    responseTimeQueue.addElement(new Long(System.currentTimeMillis()));
                }
                return "HTTP/1.0 200 test\r\n\r\n" + reply + "\n";
            }
        } else {
            numberOfErrorResponses++;
            return "HTTP/1.0 501 test\r\n\r\n" + "ERROR: PUT service is not supported. " + "\n";
        }
        return "QUERY WAS NOT HANDLED!";
    }

    /** Reads a string of CGI URL-encoded values and places them into a
 * hashtable. 
 */
    private static Hashtable parseAttributes(String s) {
        Hashtable returnValue = new Hashtable();
        int start = 0;
        int equals;
        int next;
        while (start < s.length()) {
            equals = s.indexOf('=', start);
            if (equals < 0) throw new IllegalArgumentException("Poorly structured CGI arguments");
            next = s.indexOf('&', equals);
            if (next < 0) next = s.length();
            if (debug) System.out.println("HTTP_Server:parseAttributes: get key " + s.substring(start, equals) + ",and value " + s.substring(equals + 1, next));
            returnValue.put(HTTP_ServerUtility.decodeURLString(s.substring(start, equals)), HTTP_ServerUtility.decodeURLString(s.substring(equals + 1, next)));
            start = next + 1;
        }
        return returnValue;
    }

    /** Prints a simple homepage that has forms for accessing data on server. */
    private String homePage() {
        String s = "";
        s = s + "<title>Simple overlay server</title>\n";
        s = s + "<h1>Simple Overlay Server</h1>\n";
        s = s + "<p><hr><p>\n";
        s = s + "<form method=\"GET\" action=\"/Overlays\">\n";
        s = s + "<select name=\"cmd\">\n";
        s = s + "<option value=\"createOverlay\">Create overlay</option>\n";
        s = s + "<option value=\"test\">Test if overlay exists</option>\n";
        s = s + "<option value=\"propsOverlay\">Get configuration attributes</option>\n";
        s = s + "</select><p>\n";
        s = s + "<input name=\"OverlayID\"> OverlayID<p>\n";
        s = s + "<input type=submit value=\"Submit\"><p>\n";
        s = s + "</form>\n";
        s = s + "<p><hr><p>\n";
        s = s + "<A HREF=\"/stats.html\"><h2>Statistics Page</h2></A>\n";
        s = s + "<p><hr><p>\n";
        s = s + "<h2>Active Overlay IDs</h2>\n";
        s = s + "<p><hr><p>\n";
        Enumeration OverlayIDs = overlayHandler.idsToAttributes.keys();
        while (OverlayIDs.hasMoreElements()) {
            String gid = (String) OverlayIDs.nextElement();
            s = s + "<A HREF=\"/Overlays?cmd=propsOverlay&OverlayID=" + HTTP_ServerUtility.encodeURLString(gid) + "\">" + gid + "</A><p>";
        }
        return s;
    }

    /** Prints a simple webpage with the server's statistics server. */
    private String statsPage() {
        long currentTime = System.currentTimeMillis();
        purgeQueues(currentTime);
        String s = "";
        s = s + "<title>Webserver Statistics Page</title>\n";
        s = s + "<h1>Stats Page</h1>\n";
        s = s + "<p><hr><p>\n";
        s = s + "<h2>Time</h2>\n";
        s = s + "<p><hr><p>\n";
        s = s + "Milliseconds since epoch: " + currentTime + "\n";
        s = s + "Real time: " + (new Date()) + "\n";
        s = s + "<p><hr><p>\n";
        s = s + "Number of Requests: " + numberOfRequests + "<p>\n";
        s = s + "Number of Error-free Responses: " + numberOfResponses + "<p>\n";
        s = s + "Number of Error Responses: " + numberOfErrorResponses + "<p>\n";
        s = s + "Number of Requests in last second: " + requestTimeQueue.size() + "<p>\n";
        s = s + "Number of Responses in last second: " + responseTimeQueue.size() + "<p>\n";
        s = s + "Maximum Number of Requests in any second: " + maxNumberOfRequestsInAnySecond + "<p>\n";
        s = s + "Maximum Number of Responses in any second: " + maxNumberOfResponsesInAnySecond + "<p>\n";
        s = s + "<p><hr><p>\n";
        return s;
    }

    /** Clears the queues of all elements older than 1 second from <code>t</code>. */
    private void purgeQueues(long t) {
        maxNumberOfRequestsInAnySecond = Math.max(maxNumberOfRequestsInAnySecond, purgeQueue(requestTimeQueue, t));
        maxNumberOfResponsesInAnySecond = Math.max(maxNumberOfResponsesInAnySecond, purgeQueue(responseTimeQueue, t));
    }

    /** Clears the queue of all elements older than 1 second from <code>t</code>. 
 * @return the maximum number of elements per second present in queue. */
    public static int purgeQueue(Vector v, long t) {
        int maxFoundSoFar = 0;
        synchronized (v) {
            int startRange = -1;
            int endRange = 0;
            while (endRange < v.size()) {
                startRange++;
                while (endRange < v.size() && ((Long) v.elementAt(startRange)).longValue() + 1000 > ((Long) v.elementAt(endRange)).longValue()) endRange++;
                maxFoundSoFar = Math.max(maxFoundSoFar, endRange - startRange);
            }
            if (startRange > 0) {
                for (int i = startRange; i < v.size(); i++) v.setElementAt(v.elementAt(i), i - startRange);
                v.setSize(v.size() - startRange);
            }
        }
        return maxFoundSoFar;
    }
}

/** Class that handles connects.  Its main advantage is to parallelize the I/O. */
class HTTP_ServerClientSocketManager implements Runnable {

    private HTTP_Server httpServer;

    HTTP_ServerClientSocketManager(HTTP_Server hs) {
        httpServer = hs;
    }

    /** Accepts a connection, reads in the request, calls HTTP_Server.processQuery(), writes out result. Repeat.*/
    public void run() {
        while (true) {
            try {
                Socket clientSocket = httpServer.acceptConnection();
                InputStream queryInputStream = null;
                BufferedReader query = null;
                DataOutputStream responseStream = null;
                try {
                    queryInputStream = clientSocket.getInputStream();
                    query = new BufferedReader(new InputStreamReader(queryInputStream), HTTP_ServerUtility.MAX_SERVER_RESPONSE_SIZE);
                    responseStream = new DataOutputStream(clientSocket.getOutputStream());
                    String queryLine = query.readLine();
                    if (queryLine != null) {
                        StringTokenizer queryLineTokens = new StringTokenizer(queryLine);
                        String response = null;
                        try {
                            response = httpServer.processQuery(queryLineTokens);
                        } catch (Exception e) {
                            System.err.println("Exception HTTP_Server.processQuery(): " + e.toString());
                            throw e;
                        }
                        if (HTTP_Server.debug) System.out.println("send reply, length of reply is " + response.length() + "***");
                        responseStream.writeBytes(response);
                        responseStream.flush();
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                        }
                    }
                } finally {
                    if (query != null) query.close(); else if (queryInputStream != null) queryInputStream.close();
                    if (responseStream != null) responseStream.close();
                    if (clientSocket != null) clientSocket.close();
                }
            } catch (Exception e) {
                System.err.println("Exception while handling client connection: " + e.toString());
                e.printStackTrace(System.err);
            }
        }
    }
}

/** The handler for overlay functions (create overlay, does overlay exist, etc.) */
class HTTP_ServerOverlayHandler {

    /** Map of created Overlays to each Overlay's key attributes. */
    Hashtable idsToAttributes;

    /** Map of created Overlays to each Overlay's XML property documents. */
    Hashtable idsToPropertyDocuments;

    /** Unique prefix for this process. It is usually an IP_Address plus port number.
     *  It is used to generate a unique Overlay ID
    */
    String uniqueProcessPrefix;

    /** 
     @param upp The prefix for new OverlayIDs.  It is unique over all processes and machines.
    */
    public HTTP_ServerOverlayHandler(String upp) {
        uniqueProcessPrefix = upp;
        idsToAttributes = new Hashtable();
        idsToPropertyDocuments = new Hashtable();
    }

    /** Generates single string response to request. */
    public synchronized String generateResponse(Hashtable parsedAttributes) {
        if (null == parsedAttributes.get("cmd")) {
            return "ERROR: Command not specified.";
        } else if ("upload".equals((String) parsedAttributes.get("cmd"))) {
            String propertyString = (String) parsedAttributes.get("configFile");
            if (propertyString == null) propertyString = "";
            Document doc = null;
            try {
                doc = XmlUtil.createDocumentFromString(propertyString);
            } catch (final ParseException ioe) {
                throw new HyperCastConfigException("Cannot create XML Document from uploaded file.");
            }
            if (doc == null) {
                return "ERROR: The uploaded property file is not a valid XML document!";
            }
            String overlayID = this.getAttribute(doc, HyperCastConfig.CONFIG_ATTRIBUTE_OVERLAY_ID);
            if (overlayID == null || "".equals(overlayID.trim())) {
                overlayID = generateID();
                setAttribute(doc, HyperCastConfig.CONFIG_ATTRIBUTE_OVERLAY_ID, overlayID);
                propertyString = XmlUtil.getStringFromDocument(doc);
            }
            if (HTTP_Server.debug) {
                System.out.println("actually stored value is: " + getAttribute(doc, HyperCastConfig.CONFIG_ATTRIBUTE_OVERLAY_ID));
            }
            if (HTTP_Server.debug) System.out.println("HTTP_ServerOverlayHandler:generateResponse: upload overlay ID is " + overlayID + ".");
            if (idsToAttributes.containsKey(overlayID)) {
                return "ERROR: Overlay already exists!";
            }
            String replyString = "OverlayID=" + HTTP_ServerUtility.encodeURLString(overlayID);
            idsToAttributes.put(overlayID, propertyString);
            idsToPropertyDocuments.put(overlayID, doc);
            String NewpropertyString = (String) idsToAttributes.get(overlayID);
            Document NewDoc = (Document) idsToPropertyDocuments.get(overlayID);
            try {
                XmlUtil.writeXml(NewDoc, System.out);
            } catch (IOException error) {
            }
            ;
            return replyString;
        } else if ("download".equals((String) parsedAttributes.get("cmd"))) {
            String overlayID = (String) parsedAttributes.get("OverlayID");
            if (HTTP_Server.debug) System.out.println("HTTP_ServerOverlayHandler:generateResponse: download overlay ID is " + overlayID + ".");
            if ((overlayID == null) || ("".equals(overlayID)) || (!idsToAttributes.containsKey(overlayID))) {
                return "ERROR: Overlay doesn't exists!";
            }
            Document doc = (Document) idsToPropertyDocuments.get(overlayID);
            String newPropertyString = XmlUtil.getStringFromDocument(doc);
            String replyString = "OverlayID=" + HTTP_ServerUtility.encodeURLString(overlayID) + "&" + "configFile=" + HTTP_ServerUtility.encodeURLString(newPropertyString);
            return replyString;
        } else if ("create".equals((String) parsedAttributes.get("cmd"))) {
            String overlayID = (String) parsedAttributes.get("OverlayID");
            if ((overlayID != null) && (!"".equals(overlayID)) && (idsToAttributes.containsKey(overlayID))) {
                return "ERROR: Overlay already exists!";
            }
            String propertyString = (String) parsedAttributes.get("configFileString");
            if (propertyString == null) propertyString = "";
            if (overlayID == null || "".equals(overlayID)) {
                overlayID = generateID();
            }
            Document doc = null;
            try {
                doc = XmlUtil.createDocumentFromString(propertyString);
            } catch (final ParseException ioe) {
                throw new HyperCastConfigException("Cannot create XML Document from uploaded file.");
            }
            if (doc == null) {
                return "ERROR: Property file is not a valid XML document!";
            }
            Node overlayidNode = HyperCastConfig.singleNodeXpathQuery(doc, HyperCastConfig.CONFIG_ATTRIBUTE_OVERLAY_ID);
            if (overlayidNode == null) {
            }
            Node newoverlayidTextNode = doc.createTextNode(overlayID);
            Node firstChild = overlayidNode.getFirstChild();
            if (firstChild != null) {
                overlayidNode.replaceChild(newoverlayidTextNode, firstChild);
            } else {
                overlayidNode.appendChild(newoverlayidTextNode);
            }
            propertyString = XmlUtil.getStringFromDocument(doc);
            String replyString = "OverlayID=" + HTTP_ServerUtility.encodeURLString(overlayID) + "&" + "configFileString=" + HTTP_ServerUtility.encodeURLString(propertyString);
            idsToAttributes.put(overlayID, propertyString);
            idsToPropertyDocuments.put(overlayID, doc);
            return replyString;
        } else if ("test".equals((String) parsedAttributes.get("cmd"))) {
            String overlayID = (String) parsedAttributes.get("OverlayID");
            if (overlayID == null) return "ERROR: No overlay specified!";
            if (idsToAttributes.containsKey(overlayID)) return "YES"; else return "NO";
        } else if ("props".equals((String) parsedAttributes.get("cmd"))) {
            String overlayID = (String) parsedAttributes.get("OverlayID");
            if (overlayID == null) return "ERROR: No overlay specified!";
            if (!idsToAttributes.containsKey(overlayID)) return "ERROR: No such overlay!";
            String propertyString = (String) idsToAttributes.get(overlayID);
            if (propertyString == null) propertyString = "";
            String replyString = "OverlayID=" + HTTP_ServerUtility.encodeURLString(overlayID) + "&" + "configFileString=" + HTTP_ServerUtility.encodeURLString(propertyString);
            return replyString;
        } else if ("createOverlay".equals((String) parsedAttributes.get("cmd"))) {
            String overlayID = (String) parsedAttributes.get("OverlayID");
            if (overlayID == null || "".equals(overlayID)) overlayID = generateID();
            if (idsToAttributes.containsKey(overlayID)) return "ERROR: Overlay already exists!";
            String propertyString = (String) parsedAttributes.get("configFileString");
            if (propertyString == null) propertyString = "";
            String replyString = propertyString;
            idsToAttributes.put(overlayID, propertyString);
            return replyString;
        } else if ("propsOverlay".equals((String) parsedAttributes.get("cmd"))) {
            String overlayID = (String) parsedAttributes.get("OverlayID");
            if (overlayID == null) return "ERROR: No overlay specified!";
            if (!idsToAttributes.containsKey(overlayID)) return "ERROR: No such overlay!";
            String propertyString = (String) idsToAttributes.get(overlayID);
            if (propertyString == null) propertyString = "";
            String replyString = propertyString;
            return replyString;
        } else {
            return "No such command \"" + ((String) parsedAttributes.get("cmd")) + "\"";
        }
    }

    /** Returns the value of the attribute specified by the given xpath string..  
	*/
    private String getAttribute(Document doc, String xpathString) {
        Node matchNode = HyperCastConfig.singleNodeXpathQuery(doc, xpathString);
        if ((matchNode == null) || (matchNode.getChildNodes().getLength() != 1)) {
            return null;
        } else {
            Node resultNode = matchNode.getChildNodes().item(0);
            if (resultNode.getNodeType() != Node.TEXT_NODE) {
                return resultNode.getNodeName();
            } else {
                return (String) resultNode.getNodeValue();
            }
        }
    }

    /** Sets the value of the attribute specified by the given xpath string..  
	*/
    private void setAttribute(Document doc, String xpathString, String value) {
        Node resultNode = HyperCastConfig.singleNodeXpathQuery(doc, xpathString);
        if (resultNode == null) {
            System.err.println("Attribute \"" + xpathString + "\" doesn't exist!");
        } else if (resultNode.getChildNodes().getLength() > 1) {
            System.err.println("Attribute \"" + xpathString + "\" is not a scalar attribute!");
        } else if (resultNode.getChildNodes().getLength() == 0) {
            resultNode.appendChild(doc.createTextNode(value));
        } else {
            Node resultTextNode = resultNode.getChildNodes().item(0);
            if (resultTextNode.getNodeType() != Node.TEXT_NODE) {
                System.err.println("Attribute \"" + xpathString + "\" is not a writable attribute!");
            } else {
                resultNode.removeChild(resultTextNode);
                resultNode.appendChild(doc.createTextNode(value));
            }
        }
    }

    /** Creates a new unique overlayID.  
* Format: <code>uniqueProcessPrefix</code> + "TS" + milliseconds since epoch.
*/
    private String generateID() {
        long t = System.currentTimeMillis();
        while (idsToAttributes.containsKey(uniqueProcessPrefix + "TS" + t)) t++;
        return uniqueProcessPrefix + "TS" + t;
    }

    /** Extract the "KeyAttributes" from the hashtable and 
	 * place them in a CGI string. 
	*/
    private String getKeyAttributes(Hashtable parsedAttributes) {
        String s = "";
        String keyAttr = (String) parsedAttributes.get("KeyAttributes");
        if (keyAttr == null) keyAttr = "";
        s = s + "KeyAttributes=" + HTTP_ServerUtility.encodeURLString(keyAttr);
        int start = 0;
        int end = keyAttr.indexOf(',', start);
        if (end < 0) end = keyAttr.length();
        while (end >= 0 && end - start > 0) {
            String attr = keyAttr.substring(start, end);
            String value = (String) parsedAttributes.get(attr);
            if (value == null) throw new IllegalArgumentException("ERROR: KeyAttribute \"" + attr + "\" missing!");
            s = s + "&" + HTTP_ServerUtility.encodeURLString(attr) + "=" + HTTP_ServerUtility.encodeURLString(value);
            if (end == keyAttr.length()) break;
            start = end + 1;
            end = keyAttr.indexOf(',', start);
            if (end < 0) end = keyAttr.length();
        }
        return s;
    }
}
