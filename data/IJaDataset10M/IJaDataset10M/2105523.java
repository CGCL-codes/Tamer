package com.notuvy.xml;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import java.io.*;

/**
 * A bean that has an XML representation.
 *
 * User: murali
 * Date: Jan 29, 2008
 * Time: 6:17:08 AM
 */
public abstract class SimpleJdom {

    protected static final Logger LOG = Logger.getLogger("com.notuvy.xml");

    private static final SimpleJdom DUMMY = new SimpleJdom() {

        protected Element toXml() {
            return null;
        }

        protected void parse(Element pRoot) {
        }

        @Override
        public String toString() {
            return "DUMMY";
        }
    };

    private static final XmlStorable NO_STORAGE = new BaseStorage(DUMMY) {

        public boolean save() {
            return true;
        }

        public boolean read() {
            return true;
        }

        public boolean readIfStoredImageExists() {
            return true;
        }

        public boolean readOrCreate() {
            return true;
        }

        public boolean delete() {
            return true;
        }

        @Override
        public String toString() {
            return "NO STORAGE";
        }
    };

    private static final Format XML_OUTPUT_FORMAT = Format.getRawFormat();

    static {
        XML_OUTPUT_FORMAT.setIndent("  ");
    }

    private Format fFormat = null;

    private transient XmlStorable fStorage = NO_STORAGE;

    protected SimpleJdom() {
    }

    protected SimpleJdom(String pContent) {
        readFromContent(pContent);
    }

    protected Format getFormat() {
        if (fFormat == null) {
            fFormat = createFormat();
        }
        return fFormat;
    }

    public XmlStorable getStorage() {
        return fStorage;
    }

    public void setStorage(XmlStorable pStorage) {
        fStorage = pStorage;
    }

    protected Document toDocument() {
        Document result = new Document();
        result.setRootElement(toXml());
        return result;
    }

    protected boolean isValidRoot(Element pRoot) {
        boolean result = rootName().equals(pRoot.getName());
        if (!result) {
            LOG.error("Unexpected root: " + pRoot.getName());
        }
        return result;
    }

    public boolean saveTo(Writer pWriter) {
        boolean success = false;
        XMLOutputter serializer = new XMLOutputter(getFormat());
        try {
            serializer.output(toDocument(), pWriter);
            success = true;
        } catch (IOException ioe) {
            LOG.error("Problem serializing XML to output.", ioe);
        }
        return success;
    }

    public boolean readFrom(InputStream pInputStream) {
        InputStreamReader reader = new InputStreamReader(pInputStream);
        boolean success = readFrom(reader);
        try {
            reader.close();
        } catch (IOException ioe) {
            LOG.error("XML data read problem.", ioe);
            success = false;
        }
        return success;
    }

    public boolean readFrom(Reader pReader) {
        boolean success = false;
        SAXBuilder builder = new SAXBuilder();
        try {
            Document document = builder.build(pReader);
            parse(document.getRootElement());
            success = true;
        } catch (JDOMException jdome) {
            reportXmlParseError(jdome);
        } catch (IOException ioe) {
            LOG.error("XML data read problem.", ioe);
        }
        return success;
    }

    public boolean readFromContent(String pString) {
        StringReader reader = new StringReader(pString);
        boolean success = readFrom(reader);
        reader.close();
        return success;
    }

    public String toXmlString() {
        StringWriter writer = new StringWriter();
        saveTo(writer);
        try {
            writer.close();
        } catch (IOException ioe) {
            LOG.error("Problem serializing to string.", ioe);
        }
        return writer.toString();
    }

    public Element createRoot() {
        return new Element(rootName());
    }

    protected abstract Element toXml();

    protected abstract void parse(Element pRoot);

    public String rootName() {
        throw new RuntimeException("No root name defined.");
    }

    protected Format createFormat() {
        return XML_OUTPUT_FORMAT;
    }

    public String toString() {
        return toXmlString();
    }

    protected void reportXmlParseError(JDOMException jdome) {
        LOG.error("XML structure problem", jdome);
    }
}
