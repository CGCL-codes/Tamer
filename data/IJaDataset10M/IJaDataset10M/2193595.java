package org.porphyry.model;

import java.net.*;
import java.io.*;
import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import javax.xml.validation.*;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

public abstract class HyperTopicResource {

    public enum NodeType {

        ITEM, ACTOR, VIEWPOINT, TOPIC, ATTRIBUTE, VALUE, NONE
    }

    private URL url;

    /**
* Note: The protocol given in the base URL must accept the following methods: 
* GET, PUT, and DELETE (like http and https).
* Note: When the id is automatic, the url is first set to the handler.
*/
    public HyperTopicResource(String url) throws MalformedURLException {
        this.url = new URL(url);
    }

    public HyperTopicResource(URL url) {
        try {
            this.url = new URL(url.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static NodeType getNodeType(String url) {
        NodeType t = NodeType.NONE;
        if (url.contains("/viewpoint/")) {
            if (url.contains("/topic/")) {
                t = NodeType.TOPIC;
            } else {
                t = NodeType.VIEWPOINT;
            }
        } else if (url.contains("/attribute/")) {
            if (url.contains("/value/")) {
                t = NodeType.VALUE;
            } else {
                t = NodeType.ATTRIBUTE;
            }
        } else if (url.contains("/actor/")) {
            t = NodeType.ACTOR;
        } else if (url.contains("/item/")) {
            t = NodeType.ITEM;
        }
        return t;
    }

    public static String encode(String xmlValue) {
        return xmlValue.replaceAll("&", "&amp;").replaceAll("\"", "&quot;").replaceAll("<", "&lt;");
    }

    public static String decode(String xmlValue) {
        return xmlValue.replaceAll("&quot;", "\"").replaceAll("&amp;", "&").replaceAll("&lt;", "<");
    }

    public URL getURL() {
        return this.url;
    }

    public URL getAbsoluteURL(String relative) throws MalformedURLException {
        return new URL(this.url, relative);
    }

    public String toXML() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
    }

    public abstract XMLHandler getXMLHandler();

    protected HttpURLConnection getConnection(String method, boolean out) throws IOException, ProtocolException {
        System.out.println(method + " " + this.url);
        HttpURLConnection connection = (HttpURLConnection) this.url.openConnection();
        connection.setRequestMethod(method);
        connection.setDoOutput(out);
        return connection;
    }

    protected void checkError(HttpURLConnection connection) throws HyperTopicException, IOException {
        connection.disconnect();
        int code = connection.getResponseCode();
        if (code / 100 != 2) throw new HyperTopicException(code);
    }

    public void httpDelete() throws IOException, HyperTopicException {
        HttpURLConnection connection = this.getConnection("DELETE", false);
        this.checkError(connection);
    }

    public void httpPut() throws IOException, HyperTopicException {
        String xml = this.toXML();
        HttpURLConnection connection = this.getConnection("PUT", true);
        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
        writer.write(xml);
        writer.close();
        System.out.println(xml);
        this.checkError(connection);
    }

    public void httpPostUpdate(String xml) throws IOException, HyperTopicException {
        HttpURLConnection connection = this.getConnection("POST", true);
        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + xml);
        writer.close();
        System.out.println(xml);
        this.checkError(connection);
    }

    /**
 * precondition: this.url is the handler of the POST method
 * postcondition: this.url is the url of the resource
 */
    public void httpPostCreate() throws IOException, HyperTopicException {
        String xml = this.toXML();
        HttpURLConnection connection = this.getConnection("POST", true);
        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
        writer.write(xml);
        writer.close();
        System.out.println(xml);
        this.checkError(connection);
        this.url = new URL(this.url, connection.getHeaderField("Location"));
    }

    public void httpGet(boolean validating) throws HyperTopicException, IOException, SAXException, ParserConfigurationException {
        this.clear();
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        if (validating) {
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Class<? extends HyperTopicResource> thisClass = this.getClass();
            parserFactory.setSchema(schemaFactory.newSchema(thisClass.getResource(this.getClass().getSimpleName() + ".xsd")));
        }
        SAXParser parser = parserFactory.newSAXParser();
        HttpURLConnection connection = this.getConnection("GET", false);
        parser.parse(connection.getInputStream(), this.getXMLHandler());
        this.checkError(connection);
    }

    public abstract class XMLHandler extends DefaultHandler {

        @Override
        public void error(SAXParseException e) throws SAXException {
            throw e;
        }
    }

    public abstract void clear();
}
