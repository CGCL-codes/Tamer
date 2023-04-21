package com.shpimp.dmoz.sax;

import org.xml.sax.ContentHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.apache.log4j.Category;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import java.util.Vector;
import java.io.File;
import java.io.Writer;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.Reader;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;
import com.shpimp.dmoz.util.DmozConstants;
import com.shpimp.dmoz.util.FilterProperties;
import com.shpimp.dmoz.util.DmozProperties;
import com.shpimp.dmoz.util.DmozUtils;

public class DmozContentHandler extends DmozParentHandler implements ContentHandler {

    private File previousTopicFile = null;

    private String currentQName = null;

    private Document currentDocument = null;

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        try {
            if (getLogCategory() != null) {
                getLogCategory().debug("Made it to DmozContentHandler.startElement");
                getLogCategory().debug("namespaceURI = \"" + namespaceURI + "\"");
                getLogCategory().debug("localName = \"" + localName + "\"");
                if (atts == null) {
                    getLogCategory().debug("atts == null");
                } else {
                    getLogCategory().debug("atts != null");
                }
            }
            if (namespaceURI != null && namespaceURI.equals(DmozConstants.MOZILLA_NAMESPACE) && localName != null && localName.equals(DmozConstants.TOPIC_ELEMENT_NAME) && atts != null) {
                if (getLogCategory() != null) {
                    StringBuffer buffy = new StringBuffer();
                    buffy.append("Processing Content of Topic - \"");
                    buffy.append(atts.getValue(0));
                    buffy.append("\"");
                    getLogCategory().info(buffy);
                }
                if (getOutputWriter() != null) {
                    getOutputWriter().write(DmozConstants.RDF_END_TAG);
                    getOutputWriter().write("\n");
                    getOutputWriter().flush();
                    getOutputWriter().close();
                    setOutputWriter((Writer) null);
                    File sourceFile = new File(DmozProperties.getTemporaryFile());
                    DmozUtils.copy(sourceFile, getPreviousTopicFile());
                    setPreviousTopicFile(null);
                }
                if (atts.getLength() > 0 && atts.getLocalName(0) != null && atts.getLocalName(0).equals(DmozConstants.ID_ATTRIBUTE_NAME) && atts.getURI(0) != null && atts.getURI(0).equals(DmozConstants.WWW_NAMESPACE)) {
                    setOutputElements(isAllowed(atts));
                    setOutputWriter(atts);
                }
            }
            if (namespaceURI != null && namespaceURI.equals(DmozConstants.MOZILLA_NAMESPACE) && localName != null && localName.equals(DmozConstants.EXTERNAL_PAGE_TAG) && atts != null) {
                int attIndex = atts.getIndex(DmozConstants.ABOUT_ATTRIBUTE_NAME);
                if (attIndex != -1) {
                    setCurrentDocument(new Document());
                    Field urlField = null;
                    if (DmozProperties.indexURL()) {
                        urlField = Field.Text(DmozConstants.LUCENE_URL_FIELD, atts.getValue(attIndex));
                    } else {
                        urlField = Field.UnIndexed(DmozConstants.LUCENE_URL_FIELD, atts.getValue(attIndex));
                    }
                    if (urlField != null) {
                        getCurrentDocument().add(urlField);
                    }
                }
            }
            if (getOutputElements()) {
                writeStartElement(qName, atts);
            }
            setCurrentQName(qName);
        } catch (Exception e) {
            throw new SAXException(e);
        }
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        try {
            if (getOutputElements() && getOutputWriter() != null) {
                getOutputWriter().write("</");
                getOutputWriter().write(qName);
                getOutputWriter().write(">");
            }
        } catch (Exception e) {
            throw new SAXException(e);
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
    }

    private void setOutputWriter(Attributes atts) throws IOException {
        setPreviousTopicFile(DmozProperties.getIndexFileWithPath(atts.getValue(0)));
        FileReader freader = new FileReader(getPreviousTopicFile());
        BufferedReader breader = new BufferedReader((Reader) freader);
        FileWriter fwriter = new FileWriter(DmozProperties.getTemporaryFile());
        setOutputWriter((Writer) new BufferedWriter((Writer) fwriter));
        String inputLine = breader.readLine();
        while (inputLine != null && inputLine.indexOf(DmozConstants.RDF_END_TAG) == -1) {
            getOutputWriter().write(inputLine);
            getOutputWriter().write("\n");
            inputLine = breader.readLine();
        }
        if (inputLine != null) {
            int endTagLocation = inputLine.indexOf(DmozConstants.RDF_END_TAG);
            if (endTagLocation != -1) {
                getOutputWriter().write(inputLine.substring(0, endTagLocation));
            } else {
                getOutputWriter().write(inputLine);
            }
        }
        breader.close();
    }

    private File getPreviousTopicFile() {
        return previousTopicFile;
    }

    private void setPreviousTopicFile(String newval) {
        if (newval != null) {
            previousTopicFile = new File(newval);
        } else {
            previousTopicFile = null;
        }
    }

    private String getCurrentQName() {
        return currentQName;
    }

    private void setCurrentQName(String newval) {
        currentQName = newval;
    }

    private Document getCurrentDocument() {
        return currentDocument;
    }

    private void setCurrentDocument(Document newval) {
        currentDocument = newval;
    }

    static {
        setLogCategory(Category.getInstance("com.shpimp.dmoz.sax.DmozContentHandler"));
    }
}
