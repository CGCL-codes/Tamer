package com.g2inc.scap.library.domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;
import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import com.g2inc.scap.library.domain.cpe.CPEDictionaryDocument;
import com.g2inc.scap.library.domain.oval.OvalDefinitionsDocument;
import com.g2inc.scap.library.schema.SchemaLocator;

/**
 * This class contains methods for constructing new SCAPDocuments.
 * 
 * @author ssill2
 */
public class SCAPDocumentFactory {

    private static final Logger log = Logger.getLogger(SCAPDocumentFactory.class);

    /**
	 * Returns a SCAPDocument that wraps the supplied JDOMDocument with the
	 * appropriate Implementing class based on the type.
	 * 
	 * @param type
	 *            The type of document we are dealing with
	 * @param d
	 *            The JDOM Document object to be wrapped.
	 * @return SCAPDocument The implementation specific SCAPDocument
	 */
    public static SCAPDocument getDocument(SCAPDocumentTypeEnum type, Document d) {
        return getDocument(type, d, null);
    }

    /**
	 * Returns a SCAPDocument that wraps the supplied JDOMDocument with the
	 * appropriate Implementing class based on the type.
	 * 
	 * @param type
	 *            The type of document we are dealing with
	 * @param d
	 *            The JDOM Document object to be wrapped.
	 * @return SCAPDocument The implementation specific SCAPDocument
	 */
    private static SCAPDocument getDocument(SCAPDocumentTypeEnum type, Document d, InputStream is) {
        SCAPDocument sd = null;
        switch(type) {
            case OVAL_53:
                sd = new com.g2inc.scap.library.domain.oval.oval53.DefinitionsDocumentImpl(d);
                break;
            case OVAL_54:
                sd = new com.g2inc.scap.library.domain.oval.oval54.DefinitionsDocumentImpl(d);
                break;
            case OVAL_55:
                sd = new com.g2inc.scap.library.domain.oval.oval55.DefinitionsDocumentImpl(d);
                break;
            case OVAL_56:
                sd = new com.g2inc.scap.library.domain.oval.oval56.DefinitionsDocumentImpl(d);
                break;
            case OVAL_57:
                sd = new com.g2inc.scap.library.domain.oval.oval57.DefinitionsDocumentImpl(d);
                break;
            case OVAL_58:
                sd = new com.g2inc.scap.library.domain.oval.oval58.DefinitionsDocumentImpl(d);
                break;
            case OVAL_59:
                sd = new com.g2inc.scap.library.domain.oval.oval59.DefinitionsDocumentImpl(d);
                break;
            case XCCDF_114:
                sd = new com.g2inc.scap.library.domain.xccdf.impl.XCCDF114.XCCDFBenchmark(d);
                break;
            case CPE_20:
                sd = new com.g2inc.scap.library.domain.cpe.cpe20.CPEDictionaryDocumentImpl(d);
                break;
            case CPE_21:
                sd = new com.g2inc.scap.library.domain.cpe.cpe21.CPEDictionaryDocumentImpl(d);
                break;
            case CPE_22:
                sd = new com.g2inc.scap.library.domain.cpe.cpe22.CPEDictionaryDocumentImpl(d);
                break;
            case OCIL_2:
                sd = new com.g2inc.scap.library.domain.ocil.impl.OCIL2.OcilDocument(d);
                if (is == null) {
                    throw new IllegalStateException("Can't instantiate Ocil 2 Document with no input stream");
                }
                break;
        }
        return sd;
    }

    /**
	 * Returns a blank SCAPDocument based on the type.
	 * 
	 * @param type
	 *            The type of document we are dealing with
	 * @return SCAPDocument The implementation specific SCAPDocument
	 */
    public static SCAPDocument createNewDocument(SCAPDocumentTypeEnum type) {
        SCAPDocument sd = null;
        sd = buildNewDocument(type);
        return sd;
    }

    private static SCAPDocument buildNewDocument(SCAPDocumentTypeEnum type) {
        String templateResource = null;
        switch(type) {
            case OVAL_53:
                templateResource = "itemplate/oval/OVAL_53";
                break;
            case OVAL_54:
                templateResource = "itemplate/oval/OVAL_54";
                break;
            case OVAL_55:
                templateResource = "itemplate/oval/OVAL_55";
                break;
            case OVAL_56:
                templateResource = "itemplate/oval/OVAL_56";
                break;
            case OVAL_57:
                templateResource = "itemplate/oval/OVAL_57";
                break;
            case OVAL_58:
                templateResource = "itemplate/oval/OVAL_58";
                break;
            case OVAL_59:
                templateResource = "itemplate/oval/OVAL_59";
                break;
            case XCCDF_113:
                templateResource = "itemplate/xccdf/XCCDF_113";
                break;
            case XCCDF_114:
                templateResource = "itemplate/xccdf/XCCDF_114";
                break;
            case CPE_20:
                templateResource = "itemplate/cpe/CPE_20";
                break;
            case CPE_21:
                templateResource = "itemplate/cpe/CPE_21";
                break;
            case CPE_22:
                templateResource = "itemplate/cpe/CPE_22";
                break;
            case OCIL_2:
                templateResource = "itemplate/ocil/OCIL_2";
                break;
            default:
                throw new RuntimeException("Unable to find template for type " + type);
        }
        templateResource += "/template.xml";
        log.debug("templateResource = " + templateResource);
        SchemaLocator slocator = SchemaLocator.getInstance();
        log.debug("SchemaLocator instance obtained");
        InputStream inStream = slocator.getInputStream(templateResource);
        if (inStream == null) {
            throw new RuntimeException("Unable access template resource " + templateResource);
        }
        SCAPDocument ret = null;
        SAXBuilder builder = new SAXBuilder();
        Document doc = null;
        try {
            doc = builder.build(inStream);
            inStream.close();
            inStream = slocator.getInputStream(templateResource);
            ret = getDocument(type, doc, inStream);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * This is where xml documents are actually parsed into in-memory JDOM documents
     *
     * @param file A File object representing the xml document to be read
     * @return Document
     */
    public static Document parse(File file) throws IOException, JDOMException, CharacterSetEncodingException {
        return parse(file, Charset.forName("UTF-8"));
    }

    /**
     * This is where xml documents are actually parsed into in-memory JDOM documents
     *
     * @param file A File object representing the xml document to be read
     * @param documentEncoding The character set of the document to be read
     * @return Document
     */
    private static Document parse(File file, Charset documentEncoding) throws IOException, JDOMException, CharacterSetEncodingException {
        Document d = null;
        log.info("documentfactory loading file " + file.getAbsolutePath());
        FileInputStream fis = null;
        SAXBuilder builder = null;
        try {
            fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis, documentEncoding);
            builder = new SAXBuilder();
            d = builder.build(isr);
        } catch (IOException e) {
            String message = e.getMessage();
            String userMessage = null;
            if (message.indexOf("Invalid byte 1 of 1-byte UTF-8 sequence") > -1) {
                userMessage = "File " + file.getName() + "'s encoding attribute says it's UTF-8 but characters were found " + "that were encoded in some other character encoding.  Perhaps they were encoding using your system's " + "default character encoding.";
                throw new CharacterSetEncodingException(userMessage, e);
            } else {
                throw e;
            }
        } catch (JDOMException e) {
            throw e;
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                    fis = null;
                }
                if (builder != null) {
                    builder = null;
                }
            } catch (Exception e) {
            }
        }
        return d;
    }

    /**
     * This is where xml documents are actually parsed into in-memory JDOM documents
     *
     * @param file A File object representing the xml document to be read
     * @return Document
     */
    private static Document parse(InputStream is) throws IOException, JDOMException {
        Document d = null;
        SAXBuilder builder = new SAXBuilder();
        d = builder.build(is);
        return d;
    }

    /**
     * Returns a SCAPDocumentTypeEnum that represents the type of document
     * the supplied JDOM document represents.
     * 
     * @param doc JDOM document for which to find the document type
     * @return SCAPDocumentTypeEnum
     */
    public static SCAPDocumentTypeEnum findDocumentType(Document doc) {
        SCAPDocumentTypeEnum type = SCAPDocumentTypeEnum.UNKNOWN;
        Element rootElement = doc.getRootElement();
        if (rootElement.getName().indexOf("oval_definitions") > -1) {
            Element generator = null;
            List children = rootElement.getChildren();
            for (int x = 0; x < children.size(); x++) {
                Element child = (Element) children.get(x);
                if (child.getName().toLowerCase().equals("generator")) {
                    generator = child;
                    break;
                }
            }
            if (generator != null) {
                String version = null;
                Element verElement = null;
                List generatorChildren = generator.getChildren();
                for (int x = 0; x < generatorChildren.size(); x++) {
                    Element child = (Element) generatorChildren.get(x);
                    if (child.getName().toLowerCase().equals("schema_version")) {
                        verElement = child;
                        break;
                    }
                }
                if (verElement != null) {
                    version = verElement.getValue();
                    if (version.equals("6.0")) {
                        type = SCAPDocumentTypeEnum.OVAL_60;
                    } else if (version.equals("5.9")) {
                        type = SCAPDocumentTypeEnum.OVAL_59;
                    } else if (version.equals("5.8")) {
                        type = SCAPDocumentTypeEnum.OVAL_58;
                    } else if (version.equals("5.7")) {
                        type = SCAPDocumentTypeEnum.OVAL_57;
                    } else if (version.equals("5.6")) {
                        type = SCAPDocumentTypeEnum.OVAL_56;
                    } else if (version.equals("5.5")) {
                        type = SCAPDocumentTypeEnum.OVAL_55;
                    } else if (version.equals("5.4")) {
                        type = SCAPDocumentTypeEnum.OVAL_54;
                    } else if (version.equals("5.3")) {
                        type = SCAPDocumentTypeEnum.OVAL_53;
                    }
                } else {
                    log.debug("verElement is NULL");
                }
            }
        } else if (rootElement.getName().toLowerCase().indexOf("benchmark") > -1) {
            type = SCAPDocumentTypeEnum.XCCDF_114;
        } else if (rootElement.getName().indexOf("cpe-list") > -1) {
            Namespace ns = doc.getRootElement().getNamespace();
            if (ns.getURI().equals("http://cpe.mitre.org/dictionary/2.0")) {
                List attributes = doc.getRootElement().getAttributes();
                if (attributes != null && attributes.size() > 0) {
                    for (int x = 0; x < attributes.size(); x++) {
                        Attribute a = (Attribute) attributes.get(x);
                        String aName = a.getName();
                        String aValue = a.getValue();
                        if (aName.equals("schemaLocation")) {
                            String searchString = "cpe-dictionary_";
                            if (aValue != null && aValue.indexOf(searchString) > -1) {
                                int foundLoc = aValue.indexOf(searchString);
                                int startLoc = foundLoc + searchString.length();
                                String substr = aValue.substring(startLoc, startLoc + 3);
                                if (substr.equals("2.2")) {
                                    type = SCAPDocumentTypeEnum.CPE_22;
                                } else if (substr.equals("2.1")) {
                                    type = SCAPDocumentTypeEnum.CPE_21;
                                } else if (substr.equals("2.0")) {
                                    type = SCAPDocumentTypeEnum.CPE_21;
                                } else {
                                    log.info("Defaulting type to " + SCAPDocumentTypeEnum.CPE_22);
                                    type = SCAPDocumentTypeEnum.CPE_22;
                                }
                            } else {
                            }
                        }
                    }
                    if (type == null) {
                        log.info("Defaulting type to " + SCAPDocumentTypeEnum.CPE_20);
                        type = SCAPDocumentTypeEnum.CPE_20;
                    }
                }
            } else {
                log.info("Defaulting type to " + SCAPDocumentTypeEnum.CPE_20);
                type = SCAPDocumentTypeEnum.CPE_20;
            }
        } else if (rootElement.getName().equals("ocil")) {
            type = SCAPDocumentTypeEnum.OCIL_2;
        }
        return type;
    }

    /**
     * Load an SCAP document from the disk.
     * 
     * @param file File to load.
     * @param documentEncoding Character set of the document
     * @return SCAPDocument
     * @throws JDOMException
     * @throws IOException
     */
    public static SCAPDocument loadDocument(File file, Charset documentEncoding) throws JDOMException, IOException, CharacterSetEncodingException, UnsupportedDocumentTypeException {
        SCAPDocument ret = null;
        Document d = null;
        try {
            d = parse(file, documentEncoding);
        } catch (JDOMException je) {
            log.error("JDOMException occurred trying to parse file " + file.getAbsolutePath(), je);
            throw je;
        } catch (IOException ioe) {
            log.error("IOException occurred trying to parse file " + file.getAbsolutePath(), ioe);
            throw ioe;
        }
        SCAPDocumentTypeEnum dType = findDocumentType(d);
        if (dType != SCAPDocumentTypeEnum.UNKNOWN) {
            ret = getDocument(dType, d, new FileInputStream(file));
            ret.setFilename(file.getAbsolutePath());
            if (ret != null) {
                ret.setFilename(file.getAbsolutePath());
                if (ret instanceof OvalDefinitionsDocument) {
                    OvalDefinitionsDocument odd = (OvalDefinitionsDocument) ret;
                    odd.getOvalDefinitions();
                    odd.getOvalTests();
                    odd.getOvalObjects();
                    odd.getOvalStates();
                    odd.getOvalVariables();
                } else if (ret instanceof CPEDictionaryDocument) {
                    CPEDictionaryDocument cdd = (CPEDictionaryDocument) ret;
                    log.debug("calling getItems()");
                    cdd.getItems();
                    log.debug("returned from getItems()");
                }
            }
        } else {
            throw new UnsupportedDocumentTypeException("Document type not currently supported");
        }
        return ret;
    }

    /**
     * Load an SCAP document from the disk.
     * 
     * @param file File to load.
     * 
     * @return SCAPDocument
     * @throws JDOMException
     * @throws IOException
     */
    public static SCAPDocument loadDocument(File file) throws JDOMException, IOException, CharacterSetEncodingException, UnsupportedDocumentTypeException {
        return loadDocument(file, Charset.forName("UTF-8"));
    }

    /**
     * Load an SCAP document from a stream.
     * 
     * @param is Stream to read document from.
     * 
     * @return SCAPDocument
     * @throws JDOMException
     * @throws IOException
     */
    public static SCAPDocument loadDocument(InputStream is) throws JDOMException, IOException, UnsupportedDocumentTypeException {
        SCAPDocument ret = null;
        Document d = parse(is);
        SCAPDocumentTypeEnum dType = findDocumentType(d);
        if (dType != SCAPDocumentTypeEnum.UNKNOWN) {
            ret = getDocument(dType, d, is);
        } else {
            throw new UnsupportedDocumentTypeException("Document type " + dType + " not currently supported");
        }
        return ret;
    }
}
