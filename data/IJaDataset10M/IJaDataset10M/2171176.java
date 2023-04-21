package tei.cr.filters;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import tei.cr.pipeline.AbstractSilentBase;
import tei.cr.pipeline.FilterByNames;
import tei.cr.pipeline.WrongArgsException;
import tei.cr.querydoc.FilterArguments;
import tei.cr.teiDocument.TeiDocument;
import tei.cr.utils.merging.textMerge.Merger;
import tei.cr.utils.merging.textMerge.MoreSource1NeededException;
import tei.cr.utils.saxConverter.SAXConverter;
import tei.cr.utils.saxConverter.SaxConverterException;
import tei.cr.utils.xmlBuffer.SAXEvent;
import tei.cr.utils.xmlBuffer.SAXEventsBuffer;
import tei.cr.utils.xmlBuffer.SAXEventsBufferException;
import java.util.Hashtable;
import java.util.Stack;
import java.util.logging.Logger;

/**
 * <p>Merge the markup of two documents with similar text nodes
 * content.  The two SAX streams are represented as two {@link
 * tei.cr.Utils.XMLBuffer.SAXEventsBuffer
 * SAXEventsBuffer}. The content of the two buffers are aligned 
 * and merged</p>

 * @see tei.cr.Utils.Merging.TextMerge.Merger
 * @see tei.cr.Utils.SAXConverter.SAXConverter
 * @see tei.cr.Utils.XMLBuffer.SAXEventsBuffer
 *
 * @version 0.1
 * @author Sylvain Loiseau &lt;sylvain.loiseau@u-paris10.fr>
 */
public final class EncodingMerger extends AbstractSilentBase {

    private int bufferSize = 10;

    /** 
     * The {@link SAXEventsBuffer} in which are stored the 
     * data receive via the callbacks (source 1).
     */
    private SAXEventsBuffer sourceBuffer;

    /**
     * The {@link Merger} responsible for merging the two 
     * SAXEvent streams
     */
    private Merger merger;

    /**
     * The {@link SAXEventsBuffer} in which are stored the 
     * result produced by the merger 
     */
    private SAXEventsBuffer resultSEB;

    /** 
     * The elements whose content are to be ignored 
     */
    private Hashtable ignoreElementsHash;

    /**
     * The {@link SAXConverter} used by the 
     * {@link SAXEventsBuffer} for retreiving 
     * the source 2 content.
     */
    private SAXConverter saxConverter = null;

    /**
     * The uri of a file to be passed to the {@link SAXConverter} via
     * {@link saxConverter.setArguments(java.lang.String)}. This
     * mecanism insure abstraction toward the format of the data of
     * source 2.
     */
    private String saxConverterArgsFileURI;

    private boolean isEndOfDocument = false;

    private int nbrOfCollectedEvents = 0;

    private Logger log = Logger.getLogger(getClass().getName());

    private Locator locator;

    private ContentHandler target = null;

    private static final boolean debugging = true;

    /**
     * Create the {@link SAXConverter}, used for converting a source 2 into
     * a {@link SAXEventsBuffer} representing an XML stream.
     * 
     * @param javaClassName the qualified java class name of the SAXConverter
     * to be used.
     * @throws WrongArgsException if the creation of an instance of this class
     * is impossible (the class is not in the classpath, or cannot be instanciate, etc.).
     */
    public void setSaxConverterImpl(String javaClassName) throws WrongArgsException {
        try {
            saxConverter = (SAXConverter) Class.forName(javaClassName).newInstance();
        } catch (ClassNotFoundException cnf) {
            throw new WrongArgsException("The filter \"" + javaClassName + "\" can not be created: the class is not found. The class should be in the classpath variable." + " Use CR without the -jar option at the command line.", cnf);
        } catch (IllegalAccessException iae) {
            throw new WrongArgsException("Illegal access for the class \"" + javaClassName + "\"" + ": this class should be declared " + "<code>public</code>.", iae);
        } catch (InstantiationException i) {
            throw new WrongArgsException("Error while instantiating the class \"" + javaClassName + "\"", i);
        } catch (ClassCastException cCE) {
            throw new WrongArgsException("Error with the class \"" + javaClassName + "\"" + ": this class should be a sub-class " + "of org.xml.sax.XMLFilter!", cCE);
        } catch (NoClassDefFoundError ncdfE) {
            throw new WrongArgsException("Error with the class \"" + javaClassName + "\"" + "; missing ressources: " + ncdfE.getMessage());
        }
    }

    /**
     * The argument for the SAXConverter.
     * 
     * @param URI the URI of a file  
     * @throws WrongArgsException
     */
    public void setSaxConverterArgumentURI(String URI) throws WrongArgsException {
        try {
            saxConverter.setArguments(URI);
        } catch (SaxConverterException e) {
            WrongArgsException ne = new WrongArgsException("Error in the argument of the SAXConverter: " + e.getMessage());
            ne.initCause(e);
            throw ne;
        }
    }

    /**
     * The elements of source 1 whose content is not to be aligned with source 2.
     * @param localName the local name of the elements.
     * @throws WrongArgsException
     */
    public void setIgnoreElement(String[] localName) throws WrongArgsException {
        ignoreElementsHash = new Hashtable();
        Object nothing = new Object();
        for (int i = 0; i < localName.length; i++) {
            ignoreElementsHash.put(localName[i], nothing);
        }
    }

    public void setArguments(FilterArguments fA, FilterByNames nH, TeiDocument doc) throws WrongArgsException {
        NodeList list = fA.getNodeList(FilterArguments.ENCODING_MERGER_IGNORE_ELEMENT);
        String[] localName = new String[list.getLength()];
        for (int i = 0; i < list.getLength(); i++) {
            Node element = list.item(i);
            if (element.getNodeType() != Node.ELEMENT_NODE) {
                throw new IllegalArgumentException("Only element may be selected by the " + FilterArguments.ENCODING_MERGER_IGNORE_ELEMENT + " argument.");
            }
            String type = element.getNodeName();
            if (!type.matches("localName")) {
                throw new IllegalArgumentException("Only \"localName\" may be selected by the " + FilterArguments.ENCODING_MERGER_IGNORE_ELEMENT + " argument.");
            }
            String toBeIgnoredLocalName = fA.getText(element, ".");
            if (toBeIgnoredLocalName == null || toBeIgnoredLocalName.equals("")) {
                throw new IllegalArgumentException("The node text into argument " + FilterArguments.ENCODING_MERGER_IGNORE_ELEMENT + " cannot be empty.");
            }
            localName[i] = toBeIgnoredLocalName;
        }
        setIgnoreElement(localName);
        log.info("Number of element ignored: " + localName.length);
        String saxConverterClassName;
        saxConverterClassName = fA.getText(FilterArguments.ENCODING_MERGER_SAX_CONVERTER_IMPL);
        if (saxConverterClassName == null) {
            throw new WrongArgsException("No SAXConverteur found in argument " + FilterArguments.ENCODING_MERGER_SAX_CONVERTER_IMPL);
        }
        setSaxConverterImpl(saxConverterClassName);
        log.info("SAXConverter implementation class name: " + saxConverterClassName);
        saxConverterArgsFileURI = fA.getText(FilterArguments.ENCODING_MERGER_SAX_CONVERTER_ARG_URI);
        if (saxConverterArgsFileURI == null) {
            throw new WrongArgsException("No SAXConverteur argument file URI found in argument " + FilterArguments.ENCODING_MERGER_SAX_CONVERTER_ARG_URI);
        }
        setSaxConverterArgumentURI(saxConverterArgsFileURI);
        log.info("SAXConverter arguments URI: " + saxConverterArgsFileURI);
        String b;
        b = fA.getText(FilterArguments.ENCODING_MERGER_BUFFER_SIZE);
        if (b != null && !b.equals("")) {
            try {
                bufferSize = Integer.parseInt(b);
            } catch (NumberFormatException e) {
                throw new WrongArgsException("Fail to extract the numeric value in " + FilterArguments.ENCODING_MERGER_BUFFER_SIZE + ": " + e.getMessage());
            }
        }
        if (bufferSize < 4) {
            throw new WrongArgsException("The value of the argument " + FilterArguments.ENCODING_MERGER_BUFFER_SIZE + " cannot be less than 4.");
        }
        log.info("Requested buffer size: " + bufferSize);
    }

    public void setDocumentLocator(Locator locator) {
        this.locator = locator;
        super.getXMLFilter().getContentHandler().setDocumentLocator(locator);
    }

    public void startElement(String namespaceURI, String lName, String qName, Attributes attrs) throws SAXException {
        AttributesImpl clone = new AttributesImpl(attrs);
        SAXEvent se = SAXEvent.startElement(namespaceURI, lName, qName, (Attributes) clone);
        se.setLineNumber(locator.getLineNumber());
        se.setColumnNumber(locator.getColumnNumber());
        sourceBuffer.add(se);
        nbrOfCollectedEvents++;
    }

    public void endElement(String namespaceURI, String lName, String qName) throws SAXException {
        SAXEvent se = SAXEvent.endElement(namespaceURI, lName, qName);
        se.setLineNumber(locator.getLineNumber());
        se.setColumnNumber(locator.getColumnNumber());
        sourceBuffer.add(se);
        nbrOfCollectedEvents++;
        if (nbrOfCollectedEvents > bufferSize) {
            processBuffer();
        }
    }

    public void characters(char[] buf, int offset, int len) throws SAXException {
        char[] copy = new char[len];
        System.arraycopy(buf, offset, copy, 0, len);
        SAXEvent se = SAXEvent.characters(copy, 0, len);
        se.setLineNumber(locator.getLineNumber());
        se.setColumnNumber(locator.getColumnNumber());
        sourceBuffer.add(se);
        nbrOfCollectedEvents++;
    }

    public void ignorableWhitespace(char[] ch, int start, int len) throws SAXException {
        characters(ch, start, len);
    }

    public void endDocument() throws SAXException {
        isEndOfDocument = true;
        log.fine("endDocument reached.");
        processBuffer();
        target.endDocument();
    }

    public void startDocument() throws SAXException {
        target.startDocument();
        resultSEB.setContentHandler(target);
    }

    public void startPipeline() throws tei.cr.filters.FilterException {
        target = (ContentHandler) super.getXMLFilter().getContentHandler();
        merger = new Merger(saxConverter, ignoreElementsHash);
        sourceBuffer = new SAXEventsBuffer();
        resultSEB = merger.getResultBuffer();
        super.startPipeline();
    }

    private void processBuffer() throws FilterException {
        if (debugging) log.fine("Processing the buffer; buffer size: " + sourceBuffer.size());
        try {
            if (isEndOfDocument) {
                merger.setEndOfSource1();
            }
            merger.merge(sourceBuffer);
        } catch (MoreSource1NeededException sE) {
            if (debugging) log.fine("MoreSource1NeededException---------------------------" + sE.getMessage());
        } catch (tei.cr.utils.merging.textMerge.MergerException e) {
            flushResult();
            throw new FilterException("Unable to merge the streams: " + e.getMessage(), e);
        }
        Stack opened = sourceBuffer.getOpenedElement();
        sourceBuffer = new SAXEventsBuffer();
        sourceBuffer.setOpenedElement(opened);
        nbrOfCollectedEvents = 0;
        flushResult();
    }

    private void flushResult() throws FilterException {
        try {
            resultSEB.flush();
        } catch (SAXEventsBufferException sE) {
            throw new FilterException("Unable to throw the merged stream: " + sE.getMessage(), sE);
        } catch (SAXException sE) {
            throw new FilterException("Unable to throw the merged stream: " + sE.getMessage(), sE);
        }
    }
}
