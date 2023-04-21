package nu.staldal.lagoon.producer;

import java.util.*;
import java.io.*;
import org.xml.sax.*;
import org.xml.sax.helpers.AttributesImpl;
import nu.staldal.lagoon.core.*;
import nu.staldal.lagoon.util.*;

public class IslandSplit extends Transform implements ContentHandler {

    private static final boolean DEBUG = false;

    private Hashtable outputDict;

    private Hashtable outputExtDict;

    private ContentHandler mainSax;

    private ContentHandler sax;

    private Target target;

    private String targetName;

    private int inPart;

    private int imageNumber;

    private String rootNS;

    private Vector prefixVector;

    private Vector uriVector;

    public void init() throws LagoonException {
        outputDict = new Hashtable();
        outputExtDict = new Hashtable();
        for (int i = 1; ; i++) {
            String ns = getParam("namespace" + i);
            if (ns == null) break;
            String output = getParam("output" + i);
            if (output == null) throw new LagoonException("parameter \'output" + i + "\' must be set");
            String outputExt = getParam("outputext" + i);
            if (outputExt == null) throw new LagoonException("parameter \'outputext" + i + "\' must be set");
            outputDict.put(ns, output);
            outputExtDict.put(ns, outputExt);
        }
        mainSax = null;
        sax = null;
        target = null;
    }

    public void start(ContentHandler sax, Target target) throws IOException, SAXException {
        this.mainSax = sax;
        this.sax = sax;
        this.target = target;
        inPart = 0;
        imageNumber = 0;
        rootNS = null;
        prefixVector = new Vector();
        uriVector = new Vector();
        String targetURL = target.getCurrentTargetURL();
        int slash = targetURL.lastIndexOf('/');
        targetName = (slash < 0) ? targetURL : targetURL.substring(slash + 1);
        getNext().start(this, target);
        this.target = null;
        this.sax = null;
        this.mainSax = null;
    }

    public boolean hasBeenUpdated(long when) throws LagoonException, IOException {
        return getNext().hasBeenUpdated(when);
    }

    public void setDocumentLocator(Locator locator) {
        sax.setDocumentLocator(locator);
    }

    public void startDocument() throws SAXException {
        sax.startDocument();
    }

    public void endDocument() throws SAXException {
        sax.endDocument();
    }

    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        prefixVector.addElement(prefix);
        uriVector.addElement(uri);
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        if (rootNS == null) rootNS = namespaceURI;
        if (DEBUG) System.out.println("rootNS = " + rootNS);
        String output = (String) outputDict.get(namespaceURI);
        String outputExt = (String) outputExtDict.get(namespaceURI);
        if (inPart > 0) {
            inPart++;
        } else if (output != null) {
            try {
                inPart++;
                String imageName = targetName + "_image" + (++imageNumber) + outputExt;
                AttributesImpl imgAtts = new AttributesImpl();
                imgAtts.addAttribute("", "src", "", "CDATA", imageName);
                imgAtts.addAttribute("", "alt", "", "CDATA", "");
                mainSax.startElement(rootNS, "img", "", imgAtts);
                mainSax.endElement(rootNS, "img", "");
                sax = ((FileTarget) target).newAsyncTargetWithOutput(imageName, false, output);
                sax.startDocument();
            } catch (IOException e) {
                throw new SAXException(e);
            }
        }
        for (int i = 0; i < prefixVector.size(); i++) {
            sax.startPrefixMapping((String) prefixVector.elementAt(i), (String) uriVector.elementAt(i));
        }
        prefixVector.clear();
        uriVector.clear();
        Thread.interrupted();
        sax.startElement(namespaceURI, localName, qName, atts);
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        sax.endElement(namespaceURI, localName, qName);
        if (inPart > 0) {
            inPart--;
            if (inPart == 0) {
                sax.endDocument();
                sax = mainSax;
            }
        }
    }

    public void endPrefixMapping(String prefix) throws SAXException {
    }

    public void characters(char ch[], int start, int length) throws SAXException {
        sax.characters(ch, start, length);
    }

    public void ignorableWhitespace(char ch[], int start, int length) throws SAXException {
        sax.ignorableWhitespace(ch, start, length);
    }

    public void processingInstruction(String target, String data) throws SAXException {
        sax.processingInstruction(target, data);
    }

    public void skippedEntity(String name) throws SAXException {
        sax.skippedEntity(name);
    }
}
