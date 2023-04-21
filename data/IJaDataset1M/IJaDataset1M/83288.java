package nu.staldal.lagoon.producer;

import java.io.*;
import java.util.*;
import java.text.*;
import org.xml.sax.*;
import nu.staldal.lagoon.core.*;
import nu.staldal.xmlutil.*;
import nu.staldal.util.Utils;

public class LSSITransformer extends Transform {

    public void init() throws LagoonException, IOException {
    }

    public void start(org.xml.sax.ContentHandler sax, final Target target) throws IOException, SAXException {
        sax.startDocument();
        Set includedFiles = new HashSet();
        getNext().start(new LSSIHandler(getSourceMan(), sax, target, includedFiles), target);
        putObjectIntoRepository("includedFiles-" + Utils.encodePath(getSourceMan().getSourceURL()), includedFiles);
        sax.endDocument();
    }

    public boolean hasBeenUpdated(long when) throws LagoonException, IOException {
        Set includedFiles = (Set) getObjectFromRepository("includedFiles-" + Utils.encodePath(getSourceMan().getSourceURL()));
        if (includedFiles == null) return true;
        for (Iterator it = includedFiles.iterator(); it.hasNext(); ) {
            String file = (String) it.next();
            if (getSourceMan().fileHasBeenUpdated(file, when)) return true;
        }
        return getNext().hasBeenUpdated(when);
    }
}

class LSSIHandler implements ContentHandler {

    private static final String LSSI_NS = "http://staldal.nu/Lagoon/LSSI";

    private SourceManager sourceMan;

    private ContentHandler sax;

    private Target target;

    private Locator locator;

    private int inDirective;

    private Set includedFiles;

    LSSIHandler(SourceManager sourceMan, ContentHandler sax, Target target, Set includedFiles) {
        this.sourceMan = sourceMan;
        this.sax = sax;
        this.target = target;
        this.locator = null;
        this.includedFiles = includedFiles;
        inDirective = 0;
    }

    public void setDocumentLocator(Locator locator) {
        this.locator = locator;
        sax.setDocumentLocator(locator);
    }

    public void startDocument() throws SAXException {
    }

    public void endDocument() throws SAXException {
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        if (namespaceURI.equals(LSSI_NS)) {
            inDirective++;
            if (localName.equals("include")) {
                String file = atts.getValue("file");
                if (file == null) throw new SAXParseException("lssi:include missing parameter", locator);
                includedFiles.add(file);
                try {
                    sourceMan.getFileAsSAX(file, new LSSIHandler(sourceMan, sax, target, includedFiles), target);
                } catch (FileNotFoundException e) {
                    throw new SAXParseException(e.getMessage(), locator);
                } catch (IOException e) {
                    throw new SAXException(e);
                }
            } else if (localName.equals("date")) {
                String format = atts.getValue("format");
                if (format == null) format = "yyyy-MM-dd";
                DateFormat df = new SimpleDateFormat(format);
                String tz = atts.getValue("tz");
                if (tz != null) df.setTimeZone(TimeZone.getTimeZone(tz));
                String theDate = df.format(new Date());
                sax.characters(theDate.toCharArray(), 0, theDate.length());
            } else if (localName.equals("lastmod")) {
                String format = atts.getValue("format");
                if (format == null) format = "yyyy-MM-dd";
                DateFormat df = new SimpleDateFormat(format);
                String tz = atts.getValue("tz");
                if (tz != null) df.setTimeZone(TimeZone.getTimeZone(tz));
                try {
                    String url = atts.getValue("file");
                    if (url == null) url = sourceMan.getSourceURL();
                    File file = sourceMan.getFile(url);
                    if (file == null) {
                        throw new SAXParseException("No file to check timestamp on", locator);
                    }
                    String theDate = df.format(new Date(file.lastModified()));
                    sax.characters(theDate.toCharArray(), 0, theDate.length());
                } catch (FileNotFoundException e) {
                    throw new SAXException(e);
                }
            } else if (localName.equals("root")) {
            } else {
                throw new SAXParseException("Unknown LSSI element: " + localName, locator);
            }
        } else {
            sax.startElement(namespaceURI, localName, qName, atts);
        }
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        if (namespaceURI.equals(LSSI_NS)) {
            inDirective--;
        } else {
            sax.endElement(namespaceURI, localName, qName);
        }
    }

    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        sax.startPrefixMapping(prefix, uri);
    }

    public void endPrefixMapping(String prefix) throws SAXException {
        sax.endPrefixMapping(prefix);
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
