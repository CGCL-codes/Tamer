package com.rapidminer.operator.extraction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.LinkedList;
import org.ccil.cowan.tagsoup.Parser;
import org.jaxen.JaxenException;
import org.jdom.CDATA;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.filter.Filter;
import org.jdom.input.SAXBuilder;
import org.xml.sax.XMLReader;
import com.rapidminer.operator.UserError;
import edu.udo.cs.wvtool.config.WVTConfiguration;
import edu.udo.cs.wvtool.generic.inputfilter.TextInputFilter;
import edu.udo.cs.wvtool.main.WVTDocumentInfo;
import edu.udo.cs.wvtool.main.WVTool;
import edu.udo.cs.wvtool.util.WVToolException;

/**
 * Class performs extraction from text/html by delegating XPath and Regex queries.
 * 
 * @author Michael Wurst
 * @version $Id: TextExtractionWrapper.java,v 1.10 2009-03-14 08:48:57 ingomierswa Exp $
 * 
 */
public class TextExtractionWrapper {

    public static final int CONTENT_TYPE_TEXT = 0;

    public static final int CONTENT_TYPE_XML = 1;

    public static final int CONTENT_TYPE_HTML = 2;

    public static final int CONTENT_TYPE_PDF = 3;

    private boolean ignoreCDATA = true;

    private String content;

    private int contentType;

    private org.jdom.Document dom = null;

    /**
     * Prepare the wrapper for the extraction of information from a string.
     * 
     * @param content the document content
     * @param contentType the type of content
     */
    public TextExtractionWrapper(String content, int contentType, boolean ignoreCDATA) {
        this.ignoreCDATA = ignoreCDATA;
        this.content = content;
        this.contentType = contentType;
    }

    /**
     * Prepare the wrapper for the extraction of information from a stream.
     * Notice: This method does use the default content encoding and should
     * therefore only be used for XML and HTML content that provides information
     * about its encoding.
     * 
     * @param inStream the stream from which to extract
     * @param contentType the type of content (text, html, etc.)
     * 
     */
    public TextExtractionWrapper(InputStream inStream, int contentType, boolean ignoreCDATA) throws ExtractionException {
        try {
            this.ignoreCDATA = ignoreCDATA;
            this.contentType = contentType;
            if ((contentType == TextExtractionWrapper.CONTENT_TYPE_XML) || (contentType == TextExtractionWrapper.CONTENT_TYPE_HTML)) {
                readXMLBasedDocument(inStream, null);
            } else readTextBasedDocument(new InputStreamReader(inStream));
        } catch (IOException e) {
            throw new ExtractionException("", e, new UserError(null, 302, new Object[] { "unknown", e }));
        }
    }

    /**
     * Prepare the wrapper for the extraction of information from a file.
     * Notice: This method does use the default content encoding and should
     * therefore only be used for XML and HTML content that provides information
     * about its encoding.
     * 
     * @param inFile the file from which to extract
     * @param contentType the type of content (text, html, etc.)
     * 
     */
    public TextExtractionWrapper(File inFile, int contentType, boolean ignoreCDATA) throws ExtractionException {
        try {
            this.ignoreCDATA = ignoreCDATA;
            this.contentType = contentType;
            if ((contentType == TextExtractionWrapper.CONTENT_TYPE_XML) || (contentType == TextExtractionWrapper.CONTENT_TYPE_HTML)) {
                readXMLBasedDocument(new FileInputStream(inFile), null);
            }
            readTextBasedDocument(new InputStreamReader(new FileInputStream(inFile)));
        } catch (IOException e) {
            throw new ExtractionException("", e, new UserError(null, 302, new Object[] { "unknown", e }));
        }
    }

    /**
     * Prepare the wrapper for the extraction of information from a document.
     * Depending on the type of document and the configuration of the operator,
     * different steps are taken. Text based documents are read using the selected
     * input filter. XML and HTML documents are parsed into a DOM Tree to which XPath
     * queries can be applied directly. For regex queries, this DOM Tree is converted
     * into text.
     * 
     * @param info
     * @param config
     */
    public TextExtractionWrapper(WVTDocumentInfo info, WVTConfiguration config, boolean ignoreCDATA) throws ExtractionException {
        WVTool wvtool = new WVTool(false);
        this.ignoreCDATA = ignoreCDATA;
        contentType = TextExtractionWrapper.CONTENT_TYPE_TEXT;
        int contentTypeWVTool = WVTConfiguration.determineType(info);
        switch(contentTypeWVTool) {
            case WVTConfiguration.TYPE_HTML:
                contentType = TextExtractionWrapper.CONTENT_TYPE_HTML;
                break;
            case WVTConfiguration.TYPE_XML:
                contentType = TextExtractionWrapper.CONTENT_TYPE_XML;
                break;
            case WVTConfiguration.TYPE_TEXT:
                contentType = TextExtractionWrapper.CONTENT_TYPE_TEXT;
                break;
            default:
                contentType = TextExtractionWrapper.CONTENT_TYPE_TEXT;
                break;
        }
        try {
            if ((contentType == TextExtractionWrapper.CONTENT_TYPE_XML) || (contentType == TextExtractionWrapper.CONTENT_TYPE_HTML) || config.getComponentForStep(WVTConfiguration.STEP_INPUT_FILTER, info) instanceof ExtractingInputFilter) {
                readXMLBasedDocument(wvtool.getInputStream(info, config), info);
                TextInputFilter txtFilter = new TextInputFilter();
                Reader inReader = txtFilter.convertToPlainText(wvtool.getInputStream(info, config), info);
                readTextBasedDocument(inReader);
            } else {
                if (!(config.getComponentForStep(WVTConfiguration.STEP_INPUT_FILTER, info) instanceof ExtractingInputFilter)) readTextBasedDocument(wvtool.getReader(info, config)); else {
                    TextInputFilter txtFilter = new TextInputFilter();
                    Reader inReader = txtFilter.convertToPlainText(wvtool.getInputStream(info, config), info);
                    readTextBasedDocument(inReader);
                }
            }
        } catch (IOException e2) {
            throw new ExtractionException("", e2, new UserError(null, 302, new Object[] { info.getSourceName(), e2 }));
        } catch (WVToolException e2) {
            throw new ExtractionException("", e2, new UserError(null, 306, new Object[] { "WVTool", e2 }));
        }
    }

    /**
     * Extract values based on regular expressions
     * 
     * @param extr the extractor
     * @return an iteration of strings
     */
    public Iterator<String> getValues(TextExtractor extr) throws ExtractionException {
        if (extr instanceof RegexExtractor) return getValues((RegexExtractor) extr); else return getValues((XPathExtractor) extr);
    }

    /**
     * Extract values based on regular expressions
     * 
     * @param extr the extractor
     * @return an iteration of strings
     */
    public Iterator<String> getValues(RegexExtractor extr) {
        if (content == null) content = dom.toString();
        return extr.findPatterns(content);
    }

    /**
     * Extract values based on an XPath expression
     * 
     * @param xpathExtractor the query
     * @return an iteration of strings
     */
    public Iterator<String> getValues(XPathExtractor xpathExtractor) throws ExtractionException {
        Iterator<String> result = null;
        try {
            if (dom == null) {
                SAXBuilder builder = null;
                if (contentType == CONTENT_TYPE_HTML) builder = new TagSoupSAXBuilder(); else builder = new SAXBuilder();
                dom = builder.build(new StringReader(content));
            }
            result = xpathExtractor.findPatterns(dom);
        } catch (JaxenException e) {
            throw new ExtractionException("", e, new UserError(null, 401, new Object[] { e }));
        } catch (JDOMException e) {
            throw new ExtractionException("", e, new UserError(null, 401, new Object[] { e }));
        } catch (IOException e) {
            throw new ExtractionException("", e, new UserError(null, 302, new Object[] { "unknown", e }));
        }
        if (result != null) return result; else return new LinkedList<String>().iterator();
    }

    @SuppressWarnings({ "serial", "unchecked" })
    private void readXMLBasedDocument(InputStream inStream, WVTDocumentInfo docInfo) throws ExtractionException, IOException {
        if ((contentType == CONTENT_TYPE_HTML) || (contentType == CONTENT_TYPE_XML)) {
            try {
                if (dom == null) {
                    SAXBuilder builder = null;
                    if (contentType == CONTENT_TYPE_HTML) {
                        builder = new TagSoupSAXBuilder();
                        if (docInfo != null) {
                            TextInputFilter txtFilter = new TextInputFilter();
                            dom = builder.build(txtFilter.convertToPlainText(inStream, docInfo));
                        } else dom = builder.build(inStream);
                        if (ignoreCDATA) {
                            Iterator it = dom.getDescendants(new Filter() {

                                public boolean matches(Object obj) {
                                    if (obj instanceof Element) return true; else return false;
                                }
                            });
                            while (it.hasNext()) {
                                ((Element) it.next()).removeContent(new Filter() {

                                    public boolean matches(Object obj) {
                                        if (obj instanceof CDATA) return true; else return false;
                                    }
                                });
                            }
                        }
                    } else {
                        builder = new SAXBuilder();
                        dom = builder.build(inStream);
                    }
                }
            } catch (JDOMException e) {
                throw new ExtractionException("", e, new UserError(null, 401, new Object[] { e }));
            }
        }
    }

    private void readTextBasedDocument(Reader inReader) throws ExtractionException, IOException {
        StringBuffer contentBuf = new StringBuffer();
        BufferedReader in = new BufferedReader(inReader);
        String buf = null;
        while ((buf = in.readLine()) != null) {
            contentBuf.append(buf);
            contentBuf.append("\n");
        }
        in.close();
        content = contentBuf.toString();
    }

    /**
     * Determines the type of file using its suffix.
     * 
     * @param f the file
     * @return an int representing a file type
     */
    public static int determineType(File f) {
        String sourceName = f.getName();
        String typeStr = "";
        int index = sourceName.lastIndexOf('.');
        if (index >= 0) typeStr = sourceName.substring(index + 1);
        return determineType(typeStr);
    }

    /**
     * Determines the type of file using its suffix.
     * 
     * @param typeStr the file suffix
     * @return an int representing a file type
     */
    public static int determineType(String typeStr) {
        if (typeStr.equalsIgnoreCase("htm")) typeStr = "html";
        if (typeStr.equalsIgnoreCase("pdf")) return CONTENT_TYPE_PDF;
        if (typeStr.equalsIgnoreCase("html")) return CONTENT_TYPE_HTML;
        if (typeStr.equalsIgnoreCase("xml")) return CONTENT_TYPE_XML;
        return CONTENT_TYPE_TEXT;
    }
}

/**
 * Pseudoclass to make the JDOM Parser use the tagsoup SAX Parser.
 * 
 * @author Michael Wurst
 * @version $Id: TextExtractionWrapper.java,v 1.10 2009-03-14 08:48:57 ingomierswa Exp $
 *
 */
class TagSoupSAXBuilder extends SAXBuilder {

    @Override
    protected XMLReader createParser() throws JDOMException {
        XMLReader result = new Parser();
        return result;
    }
}
