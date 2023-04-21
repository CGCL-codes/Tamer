package org.dbe.composer.wfengine.wsdl.def;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.wsdl.Definition;
import javax.wsdl.Fault;
import javax.wsdl.Import;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.Part;
import javax.wsdl.PortType;
import javax.wsdl.Types;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.ExtensionRegistry;
import javax.wsdl.extensions.UnknownExtensibilityElement;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLLocator;
import javax.wsdl.xml.WSDLReader;
import javax.wsdl.xml.WSDLWriter;
import javax.xml.namespace.QName;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.log4j.Logger;
import org.dbe.composer.wfengine.SdlException;
import org.dbe.composer.wfengine.WSDLangException;
import org.dbe.composer.wfengine.bpel.ISdlConstants;
import org.dbe.composer.wfengine.sdl.def.IBPELExtendedSDLConst;
import org.dbe.composer.wfengine.util.SdlCloser;
import org.dbe.composer.wfengine.util.SdlSchemaUtil;
import org.dbe.composer.wfengine.util.SdlUtil;
import org.dbe.composer.wfengine.util.SdlXmlUtil;
import org.dbe.composer.wfengine.wsdl.def.castor.WSDLSchemaResolver;
import org.exolab.castor.net.URIResolver;
import org.exolab.castor.xml.schema.AnyType;
import org.exolab.castor.xml.schema.ComplexType;
import org.exolab.castor.xml.schema.ElementDecl;
import org.exolab.castor.xml.schema.Group;
import org.exolab.castor.xml.schema.Schema;
import org.exolab.castor.xml.schema.SchemaException;
import org.exolab.castor.xml.schema.SchemaNames;
import org.exolab.castor.xml.schema.SimpleType;
import org.exolab.castor.xml.schema.Wildcard;
import org.exolab.castor.xml.schema.XMLType;
import org.exolab.castor.xml.schema.reader.SchemaReader;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

/**
 * Read, write, modify and create BPEL Extended WSDL documents.  This class
 * supports Partner Link Type, Message Property and Message Property Alias
 * extensions to native WSDL elements.  This class takes advantage of the
 * extension architecture provided with the JWSDL API.
 */
public class BPELExtendedWSDLDef implements IBPELExtendedSDLConst {

    /** for deployment logging purposes */
    protected static final Logger logger = Logger.getLogger(BPELExtendedWSDLDef.class.getName());

    /** Construct the anyType Qname for check when finding type. */
    protected static final QName ANY_TYPE = new QName(Schema.DEFAULT_SCHEMA_NS, SchemaNames.ANYTYPE);

    /** Soap encoding namespace. */
    protected static final String SOAP_ENC = "http://schemas.xmlsoap.org/soap/encoding/";

    /** Map of schemas which have already been loaded and we are caching */
    protected static Schema sDefaultSchema = new Schema(Schema.DEFAULT_SCHEMA_NS);

    /** Default WSDL def for unnamed locations  */
    protected static BPELExtendedWSDLDef sDefaultDef = new BPELExtendedWSDLDef();

    /** Map of schemas which have already been loaded and we are caching */
    protected HashMap mSchemaDefs = new HashMap();

    /** List of Partner Link Type extensibility element implementations. */
    private List mPartnerLinkTypeExtElements;

    /** List of message property extensibility element implementations. */
    private List mPropExtElements;

    /** List of message property alias extensibility element implementations. */
    private List mPropAliasExtElements;

    /** Extension Registry for BPEL extensions. */
    private ExtensionRegistry mExtRegistry;

    /** WSDL Definition. */
    private Definition mDefinition;

    /** Location hint used to load the wsdl. */
    private String mLocation;

    /** The standard schema resolver. */
    private IServiceStandardSchemaResolver mStandardResolver;

    /** WSDL locator to use to load schemas. */
    private WSDLLocator mLocator;

    /**
     * Default Constructor.  Dummy blank entry creation.
     */
    public BPELExtendedWSDLDef() {
        logger.debug("BPELExtendedWSDLDef()");
        try {
            WSDLFactory lFactory = WSDLFactory.newInstance();
            Definition lDef = lFactory.newDefinition();
            mDefinition = lDef;
            mPartnerLinkTypeExtElements = Collections.EMPTY_LIST;
            mPropExtElements = Collections.EMPTY_LIST;
            mPropAliasExtElements = Collections.EMPTY_LIST;
        } catch (javax.wsdl.WSDLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Constructor.
     * @param aLocator
     * @param aLocation The wsdl location hint.
     */
    public BPELExtendedWSDLDef(WSDLLocator aLocator, String aLocation, IServiceStandardSchemaResolver aStandardResolver) throws WSDLangException {
        logger.debug("BPELExtendedWSDLDef() location=" + aLocation);
        mLocation = aLocation;
        mLocator = aLocator;
        mStandardResolver = aStandardResolver;
        read(aLocator);
    }

    /**
     * Constructor.  Reads in a WSDL DOM Document containing potential Partner
     * Link Type, Property and Property Alias extensions.
     * @param aLocator locator resolves WSDL imports used by the given WSDL document
     * @throws WSDLangException.
     */
    public BPELExtendedWSDLDef(WSDLLocator aLocator, IServiceStandardSchemaResolver aStandardResolver) throws WSDLangException {
        this(aLocator, null, aStandardResolver);
    }

    /**
     * Returns true if the part is a complex encoded type.
     * @param aPart
     */
    public boolean isComplexEncodedType(Part aPart) {
        try {
            if (aPart.getElementName() != null) {
                return true;
            }
            XMLType type = findType(aPart.getTypeName());
            if (type != null) {
                return type.isComplexType();
            }
            return false;
        } catch (SdlException e) {
            return false;
        }
    }

    /**
     * Returns true if the type of the part is derived from a simple type.
     * @param aPart
     */
    public boolean isDerivedSimpleType(Part aPart) {
        if (aPart.getElementName() == null) {
            try {
                XMLType type = findType(aPart.getTypeName());
                if (type != null) return type.isSimpleType() && type.getDerivationMethod() != null;
            } catch (SdlException e) {
            }
        }
        return false;
    }

    /**
     * Creates a minimal WSDL Definition model.
     * @param aTargetNS WSDL Target Namespace.
     * @param aDefName WSDL Definitions' name attribute. Can be null.
     * @throws WSDLangException
     */
    public void createWSDL(String aTargetNS, String aDefName) throws WSDLangException {
        logger.debug("createWSDL() targetNs=" + aTargetNS + ", definition=" + aDefName);
        ClassLoader previousClassLoader = null;
        try {
            previousClassLoader = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
            WSDLFactory lFactory = WSDLFactory.newInstance();
            WSDLWriter lWriter = lFactory.newWSDLWriter();
            Definition lDef = lFactory.newDefinition();
            lDef.setTargetNamespace(aTargetNS);
            lDef.addNamespace("tns", aTargetNS);
            if (aDefName != null) lDef.setQName(new QName("", aDefName));
            Document mDoc = lWriter.getDocument(lDef);
            read(mDoc.getDocumentElement());
        } catch (javax.wsdl.WSDLException e) {
            logger.error("Error: " + e);
            throw new WSDLangException(e);
        } finally {
            if (previousClassLoader != null) Thread.currentThread().setContextClassLoader(previousClassLoader);
        }
    }

    /**
     * Reads in a WSDL Element containing potential Partner Link Type, message
     * Property and message Property Alias extension elements.
     * @param aLocator WSDL locator which supports reading of WSDL.
     * @throws WSDLangException
     */
    public void read(WSDLLocator aLocator) throws WSDLangException {
        ClassLoader previousClassLoader = null;
        try {
            previousClassLoader = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
            WSDLReader reader = WSDLFactory.newInstance().newWSDLReader();
            reader.setFeature("javax.wsdl.verbose", false);
            reader.setFeature("javax.wsdl.importDocuments", true);
            reader.setExtensionRegistry(getExtensionRegistry());
            Definition def = reader.readWSDL(aLocator);
            processExtElements(def);
            setWSDLDef(def);
        } catch (IllegalArgumentException e) {
            throw new WSDLangException(e);
        } catch (javax.wsdl.WSDLException e) {
            throw new WSDLangException(e);
        } finally {
            if (previousClassLoader != null) Thread.currentThread().setContextClassLoader(previousClassLoader);
        }
    }

    /**
     * Reads in a WSDL Element containing potential Partner Link Type, message
     * Property and message Property Alias extension elements.
     * @throws WSDLangException
     */
    public void read(Element aWSDLElement) throws WSDLangException {
        try {
            WSDLReader reader = WSDLFactory.newInstance().newWSDLReader();
            reader.setFeature("javax.wsdl.verbose", false);
            reader.setFeature("javax.wsdl.importDocuments", true);
            reader.setExtensionRegistry(getExtensionRegistry());
            Definition def = reader.readWSDL(null, aWSDLElement);
            processExtElements(def);
            setWSDLDef(def);
        } catch (IllegalArgumentException e) {
            throw new WSDLangException(e);
        } catch (javax.wsdl.WSDLException e) {
            throw new WSDLangException(e);
        }
    }

    /**
     * Processes the extensibility elements read by the WSDLReader and sets them
     * in our extended definition.
     * @param aDef the definition which was read
     */
    private void processExtElements(Definition aDef) throws WSDLangException {
        ArrayList partnerLinks = new ArrayList();
        ArrayList properties = new ArrayList();
        ArrayList propertyAliases = new ArrayList();
        for (Iterator iter = aDef.getExtensibilityElements().iterator(); iter.hasNext(); ) {
            ExtensibilityElement extElem = (ExtensibilityElement) iter.next();
            if (extElem instanceof IWsdlPartnerLinkType) {
                partnerLinks.add(extElem);
            } else if (extElem instanceof IWsdlProperty) {
                properties.add(extElem);
            } else if (extElem instanceof IWsdlPropertyAlias) {
                propertyAliases.add(extElem);
            }
        }
        setPartnerLinkTypeExtElements(partnerLinks);
        setPropExtElements(properties);
        setPropAliasExtElements(propertyAliases);
        buildSchemaMap(aDef);
    }

    /**
     * Create an extension registry for the BPEL extensions to WSDL. This
     * registry is used to associate a serializers, deserializers, and
     * implementation object for each extension element. Suppored extensions
     * include Partner Link Types, Message Properties and Message Property Alias.
     *
     * @return ExtensionRegistry - Returns the extension registry.
     */
    private ExtensionRegistry loadExtensionRegistry() {
        ExtensionRegistry lExtRegistry = new ExtensionRegistry();
        lExtRegistry.mapExtensionTypes(Definition.class, new QName(PARTNER_LINK_NAMESPACE, PARTNER_LINK_TYPE_TAG), WsdlPartnerLinkTypeImpl.class);
        lExtRegistry.registerDeserializer(Definition.class, new QName(PARTNER_LINK_NAMESPACE, PARTNER_LINK_TYPE_TAG), new WsdlPartnerLinkTypeIO());
        lExtRegistry.registerSerializer(Definition.class, new QName(PARTNER_LINK_NAMESPACE, PARTNER_LINK_TYPE_TAG), new WsdlPartnerLinkTypeIO());
        lExtRegistry.mapExtensionTypes(Definition.class, new QName(PROPERTY_NAMESPACE, PROPERTY_TAG), WsdlPropertyImpl.class);
        lExtRegistry.registerDeserializer(Definition.class, new QName(PROPERTY_NAMESPACE, PROPERTY_TAG), new WsdlPropertyIO());
        lExtRegistry.registerSerializer(Definition.class, new QName(PROPERTY_NAMESPACE, PROPERTY_TAG), new WsdlPropertyIO());
        lExtRegistry.mapExtensionTypes(Definition.class, new QName(PROPERTY_NAMESPACE, PROPERTY_ALIAS_TAG), WsdlPropertyAliasImpl.class);
        lExtRegistry.registerDeserializer(Definition.class, new QName(PROPERTY_NAMESPACE, PROPERTY_ALIAS_TAG), new WsdlPropertyAliasIO());
        lExtRegistry.registerSerializer(Definition.class, new QName(PROPERTY_NAMESPACE, PROPERTY_ALIAS_TAG), new WsdlPropertyAliasIO());
        return lExtRegistry;
    }

    /**
     * Serializes the WSDL definition out to the given output stream.
     * @param aStream output PrintSteam.
     * @throws AeWSDLExcption
     */
    public void write(PrintStream aStream) throws WSDLangException {
        try {
            Definition aDef = getWSDLDef();
            WSDLFactory mFactory = WSDLFactory.newInstance();
            WSDLWriter mWriter = mFactory.newWSDLWriter();
            mWriter.writeWSDL(aDef, aStream);
            aStream.flush();
        } catch (javax.wsdl.WSDLException e) {
            throw new WSDLangException(e);
        }
    }

    /**
     * Serializes the WSDL definition out to the given writer stream.
     *
     * @param aWriter output character stream.
     * @throws WSDLangException.
     */
    public void write(Writer aWriter) throws WSDLangException {
        Definition aDef = getWSDLDef();
        try {
            WSDLFactory lFactory = WSDLFactory.newInstance();
            WSDLWriter lWriter = lFactory.newWSDLWriter();
            lWriter.writeWSDL(aDef, aWriter);
            aWriter.flush();
        } catch (javax.wsdl.WSDLException e) {
            throw new WSDLangException(e);
        } catch (IOException e) {
            throw new WSDLangException(e);
        }
    }

    /**
     * Serializes the WSDL definition out to the location.
     * Note that the given location maybe in URL format (e.g. "file:/c://..." or "http://...")
     *
     * @param aLocation output location.
     * @throws WSDLangException.
     */
    public void write(String aLocation) throws WSDLangException {
        Writer writer = null;
        try {
            try {
                URL fileURL = new URL(aLocation);
                String protocol = fileURL.getProtocol();
                if ("file".equalsIgnoreCase(protocol)) {
                    aLocation = fileURL.getFile();
                } else if ("http".equalsIgnoreCase(protocol) || "ftp".equalsIgnoreCase(protocol)) {
                    URLConnection conn = fileURL.openConnection();
                    conn.setDoOutput(true);
                    OutputStream stream = conn.getOutputStream();
                    writer = new OutputStreamWriter(stream);
                } else {
                    logger.error("Unknown URL protocol for location" + aLocation);
                    throw new WSDLangException("Unknown URL protocol for location" + aLocation);
                }
            } catch (MalformedURLException me) {
            }
            if (writer == null) writer = new FileWriter(aLocation);
            if (writer != null) {
                write(writer);
                SdlCloser.close(writer);
            }
        } catch (Throwable e) {
            logger.error("Error writing WSDL definition to location" + aLocation + ": " + e.getMessage());
            throw new WSDLangException("Error writing WSDL definition to location" + aLocation + ": " + e.getMessage());
        } finally {
            SdlCloser.close(writer);
        }
    }

    /**
     * Serializes the WSDL definition returning it as a DOM document.
     * @return Document
     * @throws WSDLangException
     */
    public Document write() throws WSDLangException {
        Document lWSDLDoc = null;
        Definition lDef = getWSDLDef();
        if (lDef != null) {
            try {
                WSDLFactory lFactory = WSDLFactory.newInstance();
                WSDLWriter lWriter = lFactory.newWSDLWriter();
                lWSDLDoc = lWriter.getDocument(lDef);
            } catch (javax.wsdl.WSDLException e) {
                throw new WSDLangException(e);
            }
        }
        return lWSDLDoc;
    }

    /**
     * Creates a new Partner Link Type extension element adding it to the WSDL
     * definition.
     *
     * @param aName the name of this Partner Link Type.
     * @return IWsdlPartnerLinkType the created Partner Link implementation.
     *
     * @throws WSDLangException If PLT already exists OR wrapping caught exception creating def.
     */
    public IWsdlPartnerLinkType createPartnerLinkType(String aName) throws WSDLangException {
        logger.debug("createPartnerLinkType() plink=" + aName);
        if (getPartnerLinkType(aName) != null) {
            logger.error("Error: Partner Link Type Exists " + aName);
            throw new WSDLangException("Partner Link Type Exists");
        }
        Definition lDef = getWSDLDef();
        WsdlPartnerLinkTypeImpl lPartnerLinkType = null;
        ExtensionRegistry lExtReg = getExtensionRegistry();
        try {
            lPartnerLinkType = (WsdlPartnerLinkTypeImpl) lExtReg.createExtension(Definition.class, new QName(PARTNER_LINK_NAMESPACE, PARTNER_LINK_TYPE_TAG));
            lPartnerLinkType.setName(aName);
            lPartnerLinkType.setRequired(Boolean.TRUE);
            lDef.addExtensibilityElement(lPartnerLinkType);
            getPartnerLinkTypeExtElements().add(lPartnerLinkType);
        } catch (javax.wsdl.WSDLException e) {
            throw new WSDLangException(e);
        }
        return lPartnerLinkType;
    }

    /**
     * Creates a new Property extension element adding it to the WSDL definition.
     *
     * @param aName the name of this Property.
     * @param aType the type of this Property.
     * @return IWsdlProperty the created Property implementation.
     * @throws WSDLangException
     */
    public IWsdlProperty createProperty(String aName, String aType) throws WSDLangException {
        Definition lDef = getWSDLDef();
        WsdlPropertyImpl lProp = null;
        ExtensionRegistry lExtReg = getExtensionRegistry();
        try {
            lProp = (WsdlPropertyImpl) lExtReg.createExtension(Definition.class, new QName(PROPERTY_NAMESPACE, PROPERTY_TAG));
            lProp.setQName(new QName(getTargetNamespace(), aName));
            lProp.setTypeName(parseQName(aType));
            lProp.setRequired(Boolean.TRUE);
            lDef.addExtensibilityElement(lProp);
            getPropExtElements().add(lProp);
        } catch (javax.wsdl.WSDLException e) {
            throw new WSDLangException(e);
        }
        return lProp;
    }

    /**
     * Creates a new Property Alias extension element adding it to the WSDL
     * definition.
     *
     * @param aName the name of this Property Alias.
     * @param aType the message type of this Property Alias.
     * @param aPart the part value of this Property Alias.
     * @param aQuery the query value of this Property Alias.
     * @return IWsdlPropertyAlias the created Property Alias implementation.
     * @throws WSDLangException
     */
    public IWsdlPropertyAlias createPropertyAlias(String aName, String aType, String aPart, String aQuery) throws WSDLangException {
        Definition lDef = getWSDLDef();
        WsdlPropertyAliasImpl lPropAlias = null;
        ExtensionRegistry lExtReg = getExtensionRegistry();
        try {
            lPropAlias = (WsdlPropertyAliasImpl) lExtReg.createExtension(Definition.class, new QName(PROPERTY_NAMESPACE, PROPERTY_ALIAS_TAG));
            lPropAlias.setPropertyName(parseQName(aName));
            lPropAlias.setMessageType(parseQName(aType));
            lPropAlias.setPart(aPart);
            lPropAlias.setQuery(aQuery);
            lPropAlias.setRequired(Boolean.TRUE);
            lDef.addExtensibilityElement(lPropAlias);
            getPropAliasExtElements().add(lPropAlias);
        } catch (javax.wsdl.WSDLException e) {
            throw new WSDLangException(e);
        }
        return lPropAlias;
    }

    /**
     * Retreive a Partner Link Type extension implementation by name.
     *
     * @param aName the name of the Partner Link Type to retreive.
     * @return AePartnerLinkTypeImpl the Partner Link Type implementation object,
     * null if not found.
     */
    public IWsdlPartnerLinkType getPartnerLinkType(String aName) {
        logger.debug("getPartnerLinkType() plink=" + aName + " have plinks=" + getPartnerLinkTypeExtElements().size() + " plt elements");
        IWsdlPartnerLinkType lPartnerLinkType = null;
        Iterator lIt = getPartnerLinkTypeExtElements().iterator();
        while (lIt.hasNext()) {
            IWsdlPartnerLinkType lPartnerLinkElem = (IWsdlPartnerLinkType) lIt.next();
            logger.debug("comparing " + lPartnerLinkElem.getName() + " with " + aName);
            if ((lPartnerLinkElem.getName()).equals(aName)) {
                lPartnerLinkType = lPartnerLinkElem;
                return lPartnerLinkType;
            }
        }
        logger.warn("returning null from getPartnerLinkType()");
        return null;
    }

    /**
     * Removes a Partner Link Type extension implementation by object instance.
     *
     * @param aPartnerLinkType The partner link type instance to remove.
     */
    public void removePartnerLinkType(IWsdlPartnerLinkType aPartnerLinkType) {
        getPartnerLinkTypeExtElements().remove(aPartnerLinkType);
        getWSDLDef().getExtensibilityElements().remove(aPartnerLinkType);
    }

    /**
     * Removes a Property extension implementation by object instance.
     *
     * @param aProperty The property instance to be removed.
     */
    public void removeProperty(IWsdlProperty aProperty) {
        getPropExtElements().remove(aProperty);
        getWSDLDef().getExtensibilityElements().remove(aProperty);
    }

    /**
     * Removes a Property Alias extension implementation by object instance.
     *
     * @param aAlias the alias instance to be removed.
     */
    public void removePropertyAlias(IWsdlPropertyAlias aAlias) {
        getPropAliasExtElements().remove(aAlias);
        getWSDLDef().getExtensibilityElements().remove(aAlias);
    }

    /**
     * Get an iterator of Operations associated with the given a PortType name.
     * @param aPortType a PortType QName.
     * @return Iterator for list of Operations.
     */
    public Iterator getOperations(QName aPortType) {
        Definition lDef = getWSDLDef();
        List lOperations = null;
        if (lDef != null) {
            PortType lPortType = lDef.getPortType(aPortType);
            if (lPortType != null) {
                lOperations = lPortType.getOperations();
            }
        }
        if (lOperations == null) {
            return Collections.EMPTY_LIST.iterator();
        }
        return lOperations.iterator();
    }

    /**
     * Return true if the portType/operation combination already exists.
     *
     * @param aPortType The port type to check.
     * @param aOpName The operation name to check.
     *
     * @return boolean True if the operation already exists in the definition.
     */
    public boolean operationExists(QName aPortType, String aOpName) {
        Iterator iter = getOperations(aPortType);
        while (iter.hasNext()) {
            Operation oper = (Operation) iter.next();
            if (oper.getName().equals(aOpName.trim())) return true;
        }
        return false;
    }

    /**
     * Returns the named portType, or null if it does not exist.
     *
     * @param aPortType Name of the desired portType.
     *
     * @return PortType
     */
    public PortType getPortType(QName aPortType) {
        Definition lDef = getWSDLDef();
        PortType pt = null;
        if (lDef != null) pt = lDef.getPortType(aPortType);
        return pt;
    }

    /**
     * Add the QNames specfied in the array list. New prefixes, of the form
     * ns1, ns2 ... ns<i>N</i>, etc., will be added as needed and available.
     *
     * @param aQNames The list of required QNames.
     */
    public void addQNames(Collection aQNames) {
        Definition lDef = getWSDLDef();
        if (aQNames.size() > 0 && lDef != null) {
            Set prefixes = lDef.getNamespaces().keySet();
            Iterator iter = aQNames.iterator();
            while (iter.hasNext()) {
                QName name = (QName) iter.next();
                String uri = name.getNamespaceURI();
                String prefix = lDef.getPrefix(uri);
                if (prefix == null || prefix.length() <= 0) {
                    for (int i = 1; i < 1000; i++) {
                        prefix = "ns" + i;
                        if (!prefixes.contains(prefix)) {
                            lDef.addNamespace(prefix, uri);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Add an import reference to the WSDL.
     *
     * @param aLoc The physical location.
     * @param aNS The namespace for.
     */
    public void addImport(String aLoc, String aNS) {
        if (!SdlUtil.isNullOrEmpty(aLoc) && !SdlUtil.isNullOrEmpty(aNS)) {
            Import imp = null;
            if (getWSDLDef() != null) {
                List imports = getWSDLDef().getImports(aNS);
                if (imports != null) {
                    for (Iterator iter = imports.iterator(); iter.hasNext(); ) {
                        imp = (Import) iter.next();
                        if (imp.getLocationURI().equals(aLoc) && imp.getNamespaceURI().equals(aNS)) {
                            return;
                        }
                    }
                }
            }
            imp = getWSDLDef().createImport();
            imp.setLocationURI(aLoc);
            imp.setNamespaceURI(aNS);
            getWSDLDef().addImport(imp);
        }
    }

    /**
     * Add a portType/operation combination to the definition.  Creates the
     * named portType if it does not exist.
     *
     * @param aPortType  The port type's QName.
     * @param aOperation The operation to add.
     * @param aLoc The physical location, to add an Import if required, or null.
     * @param aNS The namespace for an Import if required, or null.
     */
    public void addOperation(QName aPortType, Operation aOperation, String aLoc, String aNS) {
        Definition lDef = getWSDLDef();
        if (lDef != null) {
            PortType pt = getPortType(aPortType);
            if (pt == null) {
                pt = lDef.createPortType();
                if (pt != null) {
                    pt.setQName(aPortType);
                    pt.setUndefined(false);
                    lDef.addPortType(pt);
                }
            }
            if (pt != null) {
                if (!SdlUtil.isNullOrEmpty(aLoc) && !SdlUtil.isNullOrEmpty(aNS)) {
                    Import imp = getWSDLDef().createImport();
                    imp.setLocationURI(aLoc);
                    imp.setNamespaceURI(aNS);
                    getWSDLDef().addImport(imp);
                }
                ArrayList qNames = new ArrayList();
                if (aOperation.getInput() != null) {
                    Message inputMsg = aOperation.getInput().getMessage();
                    if (inputMsg != null) {
                        qNames.add(inputMsg.getQName());
                    }
                }
                if (aOperation.getOutput() != null) {
                    Message outputMsg = aOperation.getOutput().getMessage();
                    if (outputMsg != null) {
                        qNames.add(outputMsg.getQName());
                    }
                }
                Map faultNames = aOperation.getFaults();
                if (faultNames != null) {
                    Iterator iter = faultNames.keySet().iterator();
                    while (iter.hasNext()) {
                        Fault fault = aOperation.getFault((String) iter.next());
                        qNames.add(fault.getMessage().getQName());
                    }
                }
                addQNames(qNames);
                pt.addOperation(aOperation);
            }
        }
    }

    /**
     * Add a Message to the definition.  The message is referenced by the
     * Input and Output "message" attribute.
     *
     * @param aMessage The message to add.
     */
    public void addMessage(Message aMessage) {
        Definition lDef = getWSDLDef();
        if (lDef != null) {
            if (aMessage != null) {
                ArrayList qNames = new ArrayList();
                qNames.add(aMessage.getQName());
                addQNames(qNames);
                lDef.addMessage(aMessage);
            }
        }
    }

    /**
     * Get the types area of the WSDL.
     *
     * @return Types, null if types area is not in WSDL file.
     */
    public Types getTypes() {
        return getWSDLDef().getTypes();
    }

    /**
     * Return an iterator of parsed schema objects defined within the Types section.
     * @return Iterator, iterator of declared Schema objects.
     * @throws SdlWSDLException
     */
    public Iterator getSchemas() throws SdlException {
        return mSchemaDefs.values().iterator();
    }

    /**
     * Build the internal map of schemas and namespaces declared in types area.
     * @param aDef The definition being built, note may ot be put in wsdl def member.
     * @throws SdlWSDLException
     */
    protected void buildSchemaMap(Definition aDef) throws WSDLangException {
        Types types = aDef.getTypes();
        if ((types != null) && !types.getExtensibilityElements().isEmpty()) {
            List schemaElems = new ArrayList();
            for (Iterator it = types.getExtensibilityElements().iterator(); it.hasNext(); ) {
                UnknownExtensibilityElement extElement = (UnknownExtensibilityElement) it.next();
                if ("schema".equals(extElement.getElement().getLocalName())) {
                    Element element = extractSchemaElement(extElement);
                    if (element != null) schemaElems.add(element);
                }
            }
            List schemas = new ArrayList(schemaElems.size());
            for (Iterator iter = schemaElems.iterator(); iter.hasNext(); ) {
                try {
                    Element element = (Element) iter.next();
                    StringWriter sw = new StringWriter(2048);
                    StreamResult result = new StreamResult(sw);
                    TransformerFactory transFactory = TransformerFactory.newInstance();
                    Transformer transformer = transFactory.newTransformer();
                    transformer.transform(new DOMSource(element), result);
                    InputSource input = new InputSource(new StringReader(sw.toString()));
                    SchemaReader reader = new SchemaReader(input);
                    URIResolver resolver = new WSDLSchemaResolver(getWsdlLocator(), schemaElems, aDef.getDocumentBaseURI(), mStandardResolver);
                    reader.setURIResolver(resolver);
                    Schema schema = reader.read();
                    if (schema != null) {
                        schemas.add(schema);
                    }
                } catch (Exception e) {
                    throw new WSDLangException(e);
                }
            }
            schemas = mergeSchemaList(schemas);
            for (Iterator iter = schemas.iterator(); iter.hasNext(); ) {
                Schema schema = (Schema) iter.next();
                catalogSchemaAndImports(schema);
            }
        }
    }

    /**
     * This method will iterate through the list of schemas and merge any schemas with the
     * same target namespace.  Multiple schemas with the same target namespace probably shouldn't
     * be defined in a WSDL file, but this method allows us to handle that case anyway.
     *
     * @param aSchemaList
     * @throws WSDLangException
     */
    private List mergeSchemaList(List aSchemaList) throws WSDLangException {
        Map mergedSchemas = new HashMap();
        try {
            for (Iterator iter = aSchemaList.iterator(); iter.hasNext(); ) {
                Schema schema = (Schema) iter.next();
                String targetNS = schema.getTargetNamespace();
                if (mergedSchemas.containsKey(targetNS)) {
                    Schema schema2 = (Schema) mergedSchemas.get(targetNS);
                    mergedSchemas.put(targetNS, SdlSchemaUtil.mergeSchemas(schema2, schema));
                } else {
                    mergedSchemas.put(targetNS, schema);
                }
            }
        } catch (SchemaException se) {
            throw new WSDLangException("Error found trying to merge schemas with the same target namespace:" + se.getLocalizedMessage());
        }
        return new ArrayList(mergedSchemas.values());
    }

    /**
     * Extract schema definition from passed extensibilty element for parsing by castor.
     * @param aExtElement the schema extensibility element.
     * @return Element the extracted schema element.
     */
    private Element extractSchemaElement(UnknownExtensibilityElement aExtElement) {
        Node parent = aExtElement.getElement();
        Element element = (Element) parent.cloneNode(true);
        while ((parent = parent.getParentNode()) != null) {
            if (parent.getNodeType() == Node.ELEMENT_NODE) {
                NamedNodeMap attrs = parent.getAttributes();
                for (int i = 0; i < attrs.getLength(); ++i) {
                    Attr attr = (Attr) attrs.item(i);
                    if (ISdlConstants.W3C_XMLNS.equals(attr.getNamespaceURI())) {
                        if (!element.hasAttribute(attr.getNodeName())) element.setAttributeNS(ISdlConstants.W3C_XMLNS, attr.getNodeName(), attr.getNodeValue());
                    }
                }
            }
        }
        int prefixCount = 1;
        for (Node child = element.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child.getNodeType() == Node.ELEMENT_NODE && "import".equals(child.getLocalName())) {
                Element schema = (Element) child;
                String namespace = schema.getAttribute("namespace");
                if (!SdlUtil.isNullOrEmpty(namespace)) {
                    String prefix = SdlXmlUtil.getPrefixForNamespace(schema, namespace);
                    if (SdlUtil.isNullOrEmpty(prefix)) {
                        prefix = "ae__temp_ns" + prefixCount++;
                        element.setAttributeNS(ISdlConstants.W3C_XMLNS, "xmlns:" + prefix, namespace);
                    }
                }
            }
        }
        return element;
    }

    /**
     * Recursively catalog schema and process imports which are imported by by the passed schema.
     * @param aSchema schema to process imports from.
     */
    protected void catalogSchemaAndImports(Schema aSchema) {
        String namespace = aSchema.getTargetNamespace();
        if (getSchema(namespace) == null) {
            if (!SdlUtil.isNullOrEmpty(namespace)) putSchema(namespace, aSchema);
            fixupArray(aSchema);
            Enumeration enums = aSchema.getImportedSchema();
            while (enums != null && enums.hasMoreElements()) catalogSchemaAndImports((Schema) enums.nextElement());
        }
    }

    /**
     * Fix any complex types derived from soapenc:Array that are a restricted
     * derivation and fail to allow for child elements.
     * @param aSchema
     */
    protected void fixupArray(Schema aSchema) {
        try {
            for (Enumeration enums = aSchema.getComplexTypes(); enums.hasMoreElements(); ) {
                ComplexType complexType = (ComplexType) enums.nextElement();
                if (complexType.isRestricted() && complexType.getParticleCount() == 0) {
                    ComplexType baseType = (ComplexType) complexType.getBaseType();
                    if (baseType.getName().equals("Array") && baseType.getSchema().getTargetNamespace().equals(SOAP_ENC)) {
                        Group group = new Group();
                        Wildcard wildcard = new Wildcard((ComplexType) null);
                        wildcard.addNamespace("##any");
                        wildcard.setMinOccurs(0);
                        wildcard.setMaxOccurs(-1);
                        group.addWildcard(wildcard);
                        complexType.addGroup(group);
                    }
                }
            }
        } catch (Exception e) {
            SdlException.logError(e, e.getLocalizedMessage());
        }
    }

    /**
     * Returns an iterator of schema simple type QNames that are defined for all
     * schemas.
     * @return Iterator an iterator of schema simple type QNames.
     * @throws SdlWSDLException
     */
    public Iterator getSchemaSimpleTypeNames() throws SdlException {
        List types = new ArrayList();
        Iterator it = getSchemas();
        while (it.hasNext()) {
            Schema schema = (Schema) it.next();
            String tns = schema.getTargetNamespace();
            if (tns == null) tns = "";
            Enumeration simpleList = schema.getSimpleTypes();
            while (simpleList.hasMoreElements()) {
                SimpleType simpleType = (SimpleType) simpleList.nextElement();
                QName qname = new QName(tns, simpleType.getName());
                types.add(qname);
            }
        }
        return types.iterator();
    }

    /**
     * Returns an iterator of schema global (top-level) element definition QNames
     * that defined for all schemas.
     * @return Iterator an iterator of schema global element QNames.
     * @throws SdlWSDLException
     */
    public Iterator getSchemaGlobalElementNames() throws SdlException {
        List elements = new ArrayList();
        Iterator it = getSchemas();
        while (it.hasNext()) {
            Schema schema = (Schema) it.next();
            String tns = schema.getTargetNamespace();
            if (tns == null) tns = "";
            Enumeration elementDecls = schema.getElementDecls();
            while (elementDecls.hasMoreElements()) {
                ElementDecl element = (ElementDecl) elementDecls.nextElement();
                QName qname = new QName(tns, element.getName());
                elements.add(qname);
            }
        }
        return elements.iterator();
    }

    /**
     * Looks in WSDL Definition for a schema associated with the passed namespace.
     * If found it attempts to parse it for return as a schema definition.
     * @param aNamespaceURI The namespace URI of the schema being searched for
     * @return Schema The parsed schema object or null if not found
     */
    public synchronized Schema getSchemaForNamespace(String aNamespaceURI) {
        if (Schema.DEFAULT_SCHEMA_NS.equals(aNamespaceURI)) return sDefaultSchema;
        return getSchema(aNamespaceURI);
    }

    /**
     * Finds a schema declared type.
     * @param aType the type to be found
     * @return the xml type of the passed type name
     * @throws SdlWSDLException thrown when errors encountered finding the type
     */
    public XMLType findType(QName aType) throws SdlException {
        XMLType type = null;
        try {
            Schema schema = getSchemaForNamespace(aType.getNamespaceURI());
            if (schema != null) {
                if (ANY_TYPE.equals(aType)) type = new AnyType(schema);
                if (type == null) type = schema.getComplexType(aType.getLocalPart());
                if (type == null) type = schema.getSimpleType(aType.getLocalPart(), aType.getNamespaceURI());
            }
        } catch (Throwable th) {
            throw new SdlException("Error finding type in schemas for " + aType, th);
        }
        return type;
    }

    /**
     * Finds a schema declared element.
     * @return Message, null is not found.
     * @throws WSDLangException
     */
    public ElementDecl findElement(QName aType) throws SdlException {
        Schema schema = getSchemaForNamespace(aType.getNamespaceURI());
        if (schema != null) return schema.getElementDecl(aType.getLocalPart());
        return null;
    }

    /**
     * Get a WSDL Message object given it's QName.
     *
     * @param aMsg the name of the desired Message.
     * @return Message, null is not found.
     */
    public Message getMessage(QName aMsg) {
        Message lMessage = null;
        Definition lDef = getWSDLDef();
        if (lDef != null) {
            lMessage = lDef.getMessage(aMsg);
        }
        return lMessage;
    }

    /**
     * Returns true if the definition defines the passed message directly.
     *
     * @param aMsg the name of the desired Message.
     */
    public boolean definesMessage(QName aMsg) {
        Message message = null;
        Definition def = getWSDLDef();
        if (def != null && def.getMessages() != null) {
            message = (Message) def.getMessages().get(aMsg);
        }
        return message != null;
    }

    /**
     * Add a namespace association to this objects WSDL definition.
     *
     * @param aPrefix the prefix to use for this namespace. Use null or an empty
     * string to describe the default namespace (i. e. xmlns="...").
     * @param aNamespaceURI the namespace URI to associate the prefix with.
     * If null, the namespace association will be removed.
     */
    public void setNamespace(String aPrefix, String aNamespaceURI) {
        Definition lDef = getWSDLDef();
        if (lDef != null) {
            lDef.addNamespace(aPrefix, aNamespaceURI);
        }
    }

    /**
     * Add a namespace association to the given WSDL definition.
     *
     * @param aDef the WSDL definition.
     * @param aPrefix the prefix to use for this namespace. Use null or an empty
     * string to describe the default namespace (i. e. xmlns="...").
     * @param aNamespaceURI the namespace URI to associate the prefix with.
     * If null, the namespace association will be removed.
     */
    public void setNamespace(Definition aDef, String aPrefix, String aNamespaceURI) {
        if (aDef != null) {
            aDef.addNamespace(aPrefix, aNamespaceURI);
        }
    }

    /**
     * Gets the namespace associated with the given prefix.
     *
     * @param aPrefix The prefix to find the namespace.
     * @return String The namespace for the prefix or null if not found.
     */
    public String getNamespace(String aPrefix) {
        return getWSDLDef().getNamespace(aPrefix);
    }

    /**
     * Get the target namespace in which these WSDL elements are defined.
     * @return String the target namespace
     */
    public String getTargetNamespace() {
        return getWSDLDef().getTargetNamespace();
    }

    /**
     * Get all WSDL Message elements defined here.
     * @return Map list of defined WSDL Messages
     */
    public Map getMessages() {
        return getWSDLDef().getMessages();
    }

    /**
     * Get a list of Message Part objects associated with the Message name.
     * @param aMessageName the Message name.
     * @return Iterator of Part objects.
     */
    public Iterator getMessageParts(QName aMessageName) {
        Message message = getMessage(aMessageName);
        if (message != null) {
            Map partsMap = message.getParts();
            if (partsMap != null) return message.getOrderedParts(null).iterator();
        }
        return Collections.EMPTY_LIST.iterator();
    }

    /**
     * Get all WSDL portType elements defined here.
     * @return Map list of defined WSDL PortType objects
     */
    public Map getPortTypes() {
        return getWSDLDef().getPortTypes();
    }

    /**
     * Get all WSDL binding elements defined here.
     * @return Map list of defined WSDL Binding objects
     */
    public Map getBindings() {
        return getWSDLDef().getBindings();
    }

    /**
     * Get all WSDL service elements defined here.
     * @return Map list of defined WSDL Service objects
     */
    public Map getServices() {
        return getWSDLDef().getServices();
    }

    /**
     * Returns the extension registry.
     *
     * @return ExtensionRegistry
     */
    public ExtensionRegistry getExtensionRegistry() {
        if (mExtRegistry == null) {
            setExtensionRegistry(loadExtensionRegistry());
        }
        return mExtRegistry;
    }

    /**
     * Sets the extension registry.
     *
     * @param aExtRegistry The extRegistry to set
     */
    public void setExtensionRegistry(ExtensionRegistry aExtRegistry) {
        mExtRegistry = aExtRegistry;
    }

    /**
     * Returns the partnerLnkExtElements.
     *
     * @return List of IWsdlPartnerLinkType objects.
     */
    public List getPartnerLinkTypeExtElements() {
        return mPartnerLinkTypeExtElements;
    }

    /**
     * Sets the partnerLnkExtElements.
     *
     * @param partnerLnkExtElements The partnerLnkExtElements to set
     */
    public void setPartnerLinkTypeExtElements(List partnerLnkExtElements) {
        mPartnerLinkTypeExtElements = partnerLnkExtElements;
    }

    /**
     * Returns the propExtElements.
     *
     * @return List of IWsdlProperty objects.
     */
    public List getPropExtElements() {
        return mPropExtElements;
    }

    /**
     * Sets the propExtElements.
     *
     * @param propExtElements The propExtElements to set
     */
    public void setPropExtElements(List propExtElements) {
        mPropExtElements = propExtElements;
    }

    /**
     * Returns the propAliasExtElements.
     *
     * @return List of IWsdlPropertyAlias objects
     */
    public List getPropAliasExtElements() {
        return mPropAliasExtElements;
    }

    /**
     * Sets the propAliasExtElements.
     *
     * @param propAliasExtElements The propAliasExtElements to set
     */
    public void setPropAliasExtElements(List propAliasExtElements) {
        mPropAliasExtElements = propAliasExtElements;
    }

    /**
     * Returns the WSDL definition.
     *
     * @return Definition
     */
    public Definition getWSDLDef() {
        return mDefinition;
    }

    /**
     * Sets the WSDL definition.
     *
     * @param definition The definition to set
     */
    private void setWSDLDef(Definition definition) {
        mDefinition = definition;
    }

    /**
     * Returns the Schema object for the given namespace URI, or null if not found.
     * @param aNamespaceURI the namespace URI we are looking for
     */
    private Schema getSchema(String aNamespaceURI) {
        return (Schema) mSchemaDefs.get(aNamespaceURI);
    }

    /**
     * Adds the Schema object to our HashMap under the given namespace URI key.
     * @param aNamespaceURI the namespace URI of the Schema we are adding
     * @param aSchema the Schema object to be added
     */
    private void putSchema(String aNamespaceURI, Schema aSchema) {
        mSchemaDefs.put(aNamespaceURI, aSchema);
    }

    /**
     * Given a QName object return its associated namespace prefix.
     * @param aQName a QName
     * @return String Namespace prefix
     */
    public String getPrefix(QName aQName) {
        if (aQName == null) return "";
        String lPrefix = getWSDLDef().getPrefix(aQName.getNamespaceURI());
        return lPrefix;
    }

    /**
     * Returns a QName object given a Qname String of the format
     * "Namespace_Prefix: LocalPart". E.g. "tns:testRequest".
     *
     * @param aQstr a String in the form of "tns:element"
     * @return QName
     */
    public QName parseQName(String aQstr) {
        return parseQName(aQstr, getWSDLDef());
    }

    /**
     * Returns a QName string representation given a QName object.
     * @param aQName
     * @return String QName of form "prefix:localPart".
     */
    public String qNameToString(QName aQName) {
        return getWSDLDef().getPrefix(aQName.getNamespaceURI()) + ":" + aQName.getLocalPart();
    }

    /**
     * Get an iterator of Imports for the specified namespaceURI.
     * @param aNamespaceURI the namespaceURI associated with the desired imports.
     * @return Iterator an Iterator of corresponding Imports.
     */
    public Iterator getImports(String aNamespaceURI) {
        List imports = getWSDLDef().getImports(aNamespaceURI);
        if (imports != null) return imports.iterator(); else return Collections.EMPTY_LIST.iterator();
    }

    /**
     * Helper method to return a QName object given a QName String of the format
     * "Namespace_Prefix:LocalPart". E.g. "tns:testRequest".
     * @param aQstr a String in the form of "tns:element"
     * @param aDef the aQstr's WSDL definition. Need for prefix lookup.
     * @return QName
     */
    protected static QName parseQName(String aQstr, Definition aDef) {
        QName qname = null;
        int i = aQstr.indexOf(":");
        if (i == -1) {
            System.err.println(MessageFormat.format("Malformed QName String argument: {0}, a '':'' character expected.", new Object[] { aQstr }));
        } else {
            String lNsPrefix = aQstr.substring(0, i);
            qname = new QName(aDef.getNamespace(lNsPrefix), aQstr.substring(i + 1));
        }
        return qname;
    }

    /**
     * @return the default schema for standard xsd types.
     */
    public static Schema getDefaultSchema() {
        return sDefaultSchema;
    }

    /**
     * TODO this may be unnecessary when getWSDLForNS is modified in design layer
     * @return Returns the default BPELExtendedWSDLDef (empty def object) so nulls won't be encountered.
     */
    public static BPELExtendedWSDLDef getDefaultDef() {
        return sDefaultDef;
    }

    /**
     * Accessor for wsdl location hint.
     * @return Wsdl location hint or null if none was specified at construction time.
     */
    public String getLocationHint() {
        return mLocation;
    }

    /**
     * Returns the WSDL locator for this definition.
     */
    public WSDLLocator getWsdlLocator() {
        return mLocator;
    }
}
