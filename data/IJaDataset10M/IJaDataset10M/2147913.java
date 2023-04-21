package org.exist.debugger.dbgp;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.mina.core.session.IoSession;
import org.exist.debugger.DebuggerImpl;
import org.exist.debugger.Response;
import org.exist.memtree.SAXAdapter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * @author <a href="mailto:shabanovd@gmail.com">Dmitriy Shabanov</a>
 * 
 */
public class ResponseImpl implements Response {

    private IoSession session;

    private Element parsedResponse = null;

    public ResponseImpl(IoSession session, InputStream inputStream) {
        this.session = session;
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        InputSource src = new InputSource(inputStream);
        SAXParser parser;
        try {
            parser = factory.newSAXParser();
            XMLReader reader = parser.getXMLReader();
            SAXAdapter adapter = new SAXAdapter();
            reader.setContentHandler(adapter);
            reader.parse(src);
            parsedResponse = (Element) adapter.getDocument().getFirstChild();
            System.out.println("ResponseImpl parsedResponse = " + parsedResponse);
        } catch (ParserConfigurationException e) {
        } catch (SAXException e) {
        } catch (IOException e) {
        }
    }

    protected boolean isValid() {
        return (parsedResponse != null);
    }

    protected DebuggerImpl getDebugger() {
        return (DebuggerImpl) session.getAttribute("debugger");
    }

    public IoSession getSession() {
        return session;
    }

    public String getTransactionID() {
        if (parsedResponse.getNodeName().equals("init")) return "init";
        return getAttribute("transaction_id");
    }

    public boolean hasAttribute(String attr) {
        return parsedResponse.getAttributes().getNamedItem(attr) != null;
    }

    public String getAttribute(String attr) {
        Node item = parsedResponse.getAttributes().getNamedItem(attr);
        if (item == null) return null;
        return item.getNodeValue();
    }

    public String getText() {
        Node node = parsedResponse.getFirstChild();
        if (node.getNodeType() == Node.TEXT_NODE) return ((Text) node).getData();
        return null;
    }

    public NodeList getElemetsByName(String name) {
        return parsedResponse.getElementsByTagName(name);
    }
}
