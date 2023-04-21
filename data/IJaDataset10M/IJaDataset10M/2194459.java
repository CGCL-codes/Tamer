package org.openxml4j.opc.internal.unmarshallers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.io.SAXReader;
import org.openxml4j.exceptions.InvalidFormatException;
import org.openxml4j.exceptions.OpenXML4JException;
import org.openxml4j.opc.PackageNamespaces;
import org.openxml4j.opc.PackagePart;
import org.openxml4j.opc.PackageProperties;
import org.openxml4j.opc.ZipPackage;
import org.openxml4j.opc.internal.PackagePropertiesPart;
import org.openxml4j.opc.internal.PartUnmarshaller;
import org.openxml4j.opc.internal.ZipHelper;

/**
 * Package properties unmarshaller.
 * 
 * @author Julien Chable
 * @version 1.0
 */
public class PackagePropertiesUnmarshaller implements PartUnmarshaller {

    private static final Namespace namespaceDC = new Namespace("dc", PackageProperties.NAMESPACE_DC);

    private static final Namespace namespaceCP = new Namespace("cp", PackageNamespaces.CORE_PROPERTIES);

    private static final Namespace namespaceDcTerms = new Namespace("dcterms", PackageProperties.NAMESPACE_DCTERMS);

    private static final Namespace namespaceXML = new Namespace("xml", "http://www.w3.org/XML/1998/namespace");

    private static final Namespace namespaceXSI = new Namespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");

    protected static final String KEYWORD_CATEGORY = "category";

    protected static final String KEYWORD_CONTENT_STATUS = "contentStatus";

    protected static final String KEYWORD_CONTENT_TYPE = "contentType";

    protected static final String KEYWORD_CREATED = "created";

    protected static final String KEYWORD_CREATOR = "creator";

    protected static final String KEYWORD_DESCRIPTION = "description";

    protected static final String KEYWORD_IDENTIFIER = "identifier";

    protected static final String KEYWORD_KEYWORDS = "keywords";

    protected static final String KEYWORD_LANGUAGE = "language";

    protected static final String KEYWORD_LAST_MODIFIED_BY = "lastModifiedBy";

    protected static final String KEYWORD_LAST_PRINTED = "lastPrinted";

    protected static final String KEYWORD_MODIFIED = "modified";

    protected static final String KEYWORD_REVISION = "revision";

    protected static final String KEYWORD_SUBJECT = "subject";

    protected static final String KEYWORD_TITLE = "title";

    protected static final String KEYWORD_VERSION = "version";

    public PackagePart unmarshall(UnmarshallContext context, InputStream in) throws InvalidFormatException, IOException {
        PackagePropertiesPart coreProps = new PackagePropertiesPart(context.getPackage(), context.getPartName());
        if (in == null) {
            if (context.getZipEntry() != null) {
                in = ((ZipPackage) context.getPackage()).getZipArchive().getInputStream(context.getZipEntry());
            } else if (context.getPackage() != null) {
                ZipEntry zipEntry;
                try {
                    zipEntry = ZipHelper.getCorePropertiesZipEntry((ZipPackage) context.getPackage());
                } catch (OpenXML4JException e) {
                    throw new IOException("Error while trying to get the part input stream.");
                }
                in = ((ZipPackage) context.getPackage()).getZipArchive().getInputStream(zipEntry);
            } else throw new IOException("Error while trying to get the part input stream.");
        }
        SAXReader xmlReader = new SAXReader();
        Document xmlDoc;
        try {
            xmlDoc = xmlReader.read(in);
            checkElementForOPCCompliance(xmlDoc.getRootElement());
        } catch (DocumentException e) {
            throw new IOException(e.getMessage());
        }
        coreProps.setCategoryProperty(loadCategory(xmlDoc));
        coreProps.setContentStatusProperty(loadContentStatus(xmlDoc));
        coreProps.setContentTypeProperty(loadContentType(xmlDoc));
        coreProps.setCreatedProperty(loadCreated(xmlDoc));
        coreProps.setCreatorProperty(loadCreator(xmlDoc));
        coreProps.setDescriptionProperty(loadDescription(xmlDoc));
        coreProps.setIdentifierProperty(loadIdentifier(xmlDoc));
        coreProps.setKeywordsProperty(loadKeywords(xmlDoc));
        coreProps.setLanguageProperty(loadLanguage(xmlDoc));
        coreProps.setLastModifiedByProperty(loadLastModifiedBy(xmlDoc));
        coreProps.setLastPrintedProperty(loadLastPrinted(xmlDoc));
        coreProps.setModifiedProperty(loadModified(xmlDoc));
        coreProps.setRevisionProperty(loadRevision(xmlDoc));
        coreProps.setSubjectProperty(loadSubject(xmlDoc));
        coreProps.setTitleProperty(loadTitle(xmlDoc));
        coreProps.setVersionProperty(loadVersion(xmlDoc));
        return coreProps;
    }

    private String loadCategory(Document xmlDoc) {
        Element el = xmlDoc.getRootElement().element(new QName(KEYWORD_CATEGORY, namespaceCP));
        if (el != null) return el.getStringValue(); else return null;
    }

    private String loadContentStatus(Document xmlDoc) {
        Element el = xmlDoc.getRootElement().element(new QName(KEYWORD_CONTENT_STATUS, namespaceCP));
        if (el != null) return el.getStringValue(); else return null;
    }

    private String loadContentType(Document xmlDoc) {
        Element el = xmlDoc.getRootElement().element(new QName(KEYWORD_CONTENT_TYPE, namespaceCP));
        if (el != null) return el.getStringValue(); else return null;
    }

    private String loadCreated(Document xmlDoc) {
        Element el = xmlDoc.getRootElement().element(new QName(KEYWORD_CREATED, namespaceDcTerms));
        if (el != null) return el.getStringValue(); else return null;
    }

    private String loadCreator(Document xmlDoc) {
        Element el = xmlDoc.getRootElement().element(new QName(KEYWORD_CREATOR, namespaceDC));
        if (el != null) return el.getStringValue(); else return null;
    }

    private String loadDescription(Document xmlDoc) {
        Element el = xmlDoc.getRootElement().element(new QName(KEYWORD_DESCRIPTION, namespaceDC));
        if (el != null) return el.getStringValue(); else return null;
    }

    private String loadIdentifier(Document xmlDoc) {
        Element el = xmlDoc.getRootElement().element(new QName(KEYWORD_IDENTIFIER, namespaceDC));
        if (el != null) return el.getStringValue(); else return null;
    }

    private String loadKeywords(Document xmlDoc) {
        Element el = xmlDoc.getRootElement().element(new QName(KEYWORD_KEYWORDS, namespaceCP));
        if (el != null) return el.getStringValue(); else return null;
    }

    private String loadLanguage(Document xmlDoc) {
        Element el = xmlDoc.getRootElement().element(new QName(KEYWORD_LANGUAGE, namespaceDC));
        if (el != null) return el.getStringValue(); else return null;
    }

    private String loadLastModifiedBy(Document xmlDoc) {
        Element el = xmlDoc.getRootElement().element(new QName(KEYWORD_LAST_MODIFIED_BY, namespaceCP));
        if (el != null) return el.getStringValue(); else return null;
    }

    private String loadLastPrinted(Document xmlDoc) {
        Element el = xmlDoc.getRootElement().element(new QName(KEYWORD_LAST_PRINTED, namespaceCP));
        if (el != null) return el.getStringValue(); else return null;
    }

    private String loadModified(Document xmlDoc) {
        Element el = xmlDoc.getRootElement().element(new QName(KEYWORD_MODIFIED, namespaceDcTerms));
        if (el != null) return el.getStringValue(); else return null;
    }

    private String loadRevision(Document xmlDoc) {
        Element el = xmlDoc.getRootElement().element(new QName(KEYWORD_REVISION, namespaceCP));
        if (el != null) return el.getStringValue(); else return null;
    }

    private String loadSubject(Document xmlDoc) {
        Element el = xmlDoc.getRootElement().element(new QName(KEYWORD_SUBJECT, namespaceDC));
        if (el != null) return el.getStringValue(); else return null;
    }

    private String loadTitle(Document xmlDoc) {
        Element el = xmlDoc.getRootElement().element(new QName(KEYWORD_TITLE, namespaceDC));
        if (el != null) return el.getStringValue(); else return null;
    }

    private String loadVersion(Document xmlDoc) {
        Element el = xmlDoc.getRootElement().element(new QName(KEYWORD_VERSION, namespaceCP));
        if (el != null) return el.getStringValue(); else return null;
    }

    /**
	 * Check the element for the following OPC compliance rules:
	 * 
	 * Rule M4.2: A format consumer shall consider the use of the Markup
	 * Compatibility namespace to be an error.
	 * 
	 * Rule M4.3: Producers shall not create a document element that contains
	 * refinements to the Dublin Core elements, except for the two specified in
	 * the schema: <dcterms:created> and <dcterms:modified> Consumers shall
	 * consider a document element that violates this constraint to be an error.
	 * 
	 * Rule M4.4: Producers shall not create a document element that contains
	 * the xml:lang attribute. Consumers shall consider a document element that
	 * violates this constraint to be an error.
	 * 
	 * Rule M4.5: Producers shall not create a document element that contains
	 * the xsi:type attribute, except for a <dcterms:created> or
	 * <dcterms:modified> element where the xsi:type attribute shall be present
	 * and shall hold the value dcterms:W3CDTF, where dcterms is the namespace
	 * prefix of the Dublin Core namespace. Consumers shall consider a document
	 * element that violates this constraint to be an error.
	 */
    public void checkElementForOPCCompliance(Element el) throws InvalidFormatException {
        List declaredNamespaces = el.declaredNamespaces();
        Iterator itNS = declaredNamespaces.iterator();
        while (itNS.hasNext()) {
            Namespace ns = (Namespace) itNS.next();
            if (ns.getURI().equals(PackageNamespaces.MARKUP_COMPATIBILITY)) throw new InvalidFormatException("OPC Compliance error [M4.2]: A format consumer shall consider the use of the Markup Compatibility namespace to be an error.");
        }
        if (el.getNamespace().getURI().equals(PackageProperties.NAMESPACE_DCTERMS) && !(el.getName().equals(KEYWORD_CREATED) || el.getName().equals(KEYWORD_MODIFIED))) throw new InvalidFormatException("OPC Compliance error [M4.3]: Producers shall not create a document element that contains refinements to the Dublin Core elements, except for the two specified in the schema: <dcterms:created> and <dcterms:modified> Consumers shall consider a document element that violates this constraint to be an error.");
        if (el.attribute(new QName("lang", namespaceXML)) != null) throw new InvalidFormatException("OPC Compliance error [M4.4]: Producers shall not create a document element that contains the xml:lang attribute. Consumers shall consider a document element that violates this constraint to be an error.");
        if (el.getNamespace().getURI().equals(PackageProperties.NAMESPACE_DCTERMS)) {
            String elName = el.getName();
            if (!(elName.equals(KEYWORD_CREATED) || elName.equals(KEYWORD_MODIFIED))) throw new InvalidFormatException("Namespace error : " + elName + " shouldn't have the following naemspace -> " + PackageProperties.NAMESPACE_DCTERMS);
            Attribute typeAtt = el.attribute(new QName("type", namespaceXSI));
            if (typeAtt == null) throw new InvalidFormatException("The element '" + elName + "' must have the '" + namespaceXSI.getPrefix() + ":type' attribute present !");
            if (!typeAtt.getValue().equals("dcterms:W3CDTF")) throw new InvalidFormatException("The element '" + elName + "' must have the '" + namespaceXSI.getPrefix() + ":type' attribute with the value 'dcterms:W3CDTF' !");
        }
        Iterator itChildren = el.elementIterator();
        while (itChildren.hasNext()) checkElementForOPCCompliance((Element) itChildren.next());
    }
}
