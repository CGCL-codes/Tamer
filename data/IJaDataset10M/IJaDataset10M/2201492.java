package org.openemed.CTS;

import nu.xom.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import org.openemed.CTSVAPI.ConceptId;
import org.openemed.CTSVAPI.CodeSystemIdAndVersions;

/**
 * Created by IntelliJ IDEA.
 * User: dwf
 * Date: Jul 14, 2005
 * Time: 11:02:07 AM
 */
public class LexGridMgr {

    public static final String LGBUILTIN = "http://LexGrid.org/schema/2005/01/LexGrid/builtins";

    public static final String LGCOMMON = "http://LexGrid.org/schema/2005/01/LexGrid/commonTypes";

    public static final String LGCON = "http://LexGrid.org/schema/2005/01/LexGrid/concepts";

    public static final String LGREL = "http://LexGrid.org/schema/2005/01/LexGrid/relations";

    public static final String LGCS = "http://LexGrid.org/schema/2005/01/LexGrid/codingSchemes";

    public static final String LGLDAP = "http://LexGrid.org/schema/2005/01/LexGrid/ldap";

    public static final String LGNAMING = "http://LexGrid.org/schema/2005/01/LexGrid/naming";

    public static final String LGSERVICE = "http://LexGrid.org/schema/2005/01/LexGrid/service";

    public static final String LGVD = "http://LexGrid.org/schema/2005/01/LexGrid/valueDomains";

    public static final String LGVER = "http://LexGrid.org/schema/2005/01/LexGrid/versions";

    String database;

    SystemsContainer systemsContainer;

    public LexGridMgr(String database, SystemsContainer container) {
        this.database = database;
        systemsContainer = container;
    }

    /**
     * read the LexGrid data from the InputStream and convert to a CodeSystem
     *
     * @param in
     */
    public void readFile(InputStream in) {
        Builder builder = new Builder();
        try {
            Document doc = builder.build(in);
            System.out.println(doc.getBaseURI());
            Element el = doc.getRootElement();
            System.out.println(el.getLocalName());
            if (el.getLocalName().equals("codingSchemes")) {
                Elements codingSchemes = el.getChildElements();
                for (int i = 0; i < codingSchemes.size(); i++) {
                    Element scheme = codingSchemes.get(i);
                    String uri = scheme.getNamespaceURI();
                    CodeSystem system = getSystem(scheme, uri);
                    if (system != null) systemsContainer.addSystem(system);
                }
            } else {
                if (!el.getLocalName().equals("codingScheme")) System.err.println("incorrect localname of element: " + el.getLocalName());
                String uri = el.getNamespaceURI();
                CodeSystem system = getSystem(el, uri);
                if (system != null) systemsContainer.addSystem(system);
            }
        } catch (ParsingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * convert the LexGrid relations into Relationship objects
     *
     * @param relation
     * @param system
     */
    private void getRelations(Element relation, CodeSystem system) {
        String codeSystem_id = system.getSystemId();
        Elements relations = relation.getChildElements();
        System.out.println("adding: " + relations.size() + " relation(s)");
        for (int i = 0; i < relations.size(); i++) {
            Element e = relations.get(i);
            int counts = e.getAttributeCount();
            ArrayList attributes = new ArrayList();
            String assocName = "";
            String forwardName = "";
            String reverseName = "";
            for (int j = 0; j < counts; j++) {
                Attribute attribute = e.getAttribute(j);
                if (attribute.getLocalName().equals("association")) assocName = attribute.getValue(); else if (attribute.getLocalName().equals("forwardName")) forwardName = attribute.getValue(); else if (attribute.getLocalName().equals("reverseName")) reverseName = attribute.getValue(); else attributes.add(new NameValue(attribute.getLocalName(), attribute.getValue()));
            }
            Association assoc = system.newAssociation();
            assoc.setAssociationName(assocName);
            assoc.setForwardName(forwardName);
            assoc.setReverseName(reverseName);
            for (int j = 0; j < attributes.size(); j++) {
                assoc.addAttribute((NameValue) attributes.get(j));
            }
            system.addAssociation(assoc);
            Elements sources = e.getChildElements();
            String uri = e.getNamespaceURI();
            for (int j = 0; j < sources.size(); j++) {
                Element sourceConcept = sources.get(j);
                Elements targets = sourceConcept.getChildElements();
                for (int k = 0; k < targets.size(); k++) {
                    Element targetConcept = targets.get(k);
                    String source = sourceConcept.getAttribute("sourceConcept").getValue();
                    String target = targetConcept.getAttribute("targetConcept").getValue();
                    Relationship relationship = new Relationship(new ConceptId(codeSystem_id, source), assoc.getAssociationName(), new ConceptId(codeSystem_id, target));
                    system.addRelationship(relationship);
                }
            }
        }
    }

    /**
     * convert the LexGrid concepts into CodedConcepts
     *
     * @param concept
     * @param system
     * @param version
     */
    private void getConcepts(Element concept, CodeSystem system, String version) {
        Elements concepts = concept.getChildElements();
        System.out.println("adding: " + concepts.size() + " concepts");
        for (int i = 0; i < concepts.size(); i++) {
            Element e = concepts.get(i);
            String conceptCode = e.getAttribute("conceptCode").getValue();
            String conceptStatus = "true";
            if (e.getAttribute("conceptStatus") != null) conceptStatus = e.getAttribute("conceptStatus").getValue();
            String active = "true";
            if (e.getAttribute("isActive") != null) active = e.getAttribute("isActive").getValue();
            boolean isActive = new Boolean(active).booleanValue();
            String anonymous = "false";
            if (e.getAttribute("isAnonymous") != null) anonymous = e.getAttribute("isAnonymous").getValue();
            boolean isAnonymous = new Boolean(anonymous).booleanValue();
            CodedConcept codedConcept = system.newCodedConcept();
            codedConcept.setConceptId(conceptCode);
            codedConcept.setIsActive(isActive);
            codedConcept.setIsAnonymous(isAnonymous);
            codedConcept.setStatus(conceptStatus);
            codedConcept.setCodeSystemId(system.getSystemId());
            codedConcept.setCodeSystemVersion(version);
            Elements props = e.getChildElements();
            for (int j = 0; j < props.size(); j++) {
                Element prop = props.get(j);
                String uri = prop.getNamespaceURI();
                if (prop.getLocalName().equals("property") || prop.getLocalName().equals("definition") || prop.getLocalName().equals("presentation") || prop.getLocalName().equals("comment") || prop.getLocalName().equals("instruction")) {
                    Property conceptProperty = new Property();
                    int count = prop.getAttributeCount();
                    for (int k = 0; k < count; k++) {
                        Attribute att = prop.getAttribute(k);
                        if (att.getLocalName().equals("property")) conceptProperty.setCode(att.getValue());
                        if (att.getLocalName().equals("propertyId")) conceptProperty.setId(att.getValue());
                        if (att.getLocalName().equals("isPreferred")) conceptProperty.setIsPreferred(new Boolean(att.getValue()).booleanValue());
                        if (att.getLocalName().equals("language")) conceptProperty.setLanguage(att.getValue());
                        if (att.getLocalName().equals("presentationFormat")) conceptProperty.setPresentationFormat(att.getValue());
                        if (att.getLocalName().equals("mimeType")) {
                            conceptProperty.setMimeType(att.getValue());
                        }
                        conceptProperty.addAttribute(new NameValue(att.getLocalName(), att.getValue()));
                    }
                    Element text = prop.getFirstChildElement("text", uri);
                    if (text == null) {
                        System.out.println(prop.getLocalName());
                    } else conceptProperty.setValue(text.getValue());
                    codedConcept.addProperties(conceptProperty);
                } else if (prop.getLocalName().equals("entityDescription")) {
                    codedConcept.setDescription(prop.getValue());
                }
            }
            system.addCodedConcept(codedConcept);
        }
    }

    private CodeSystem getSystem(Element scheme, String uri) {
        String version = scheme.getAttribute("representsVersion").getValue();
        String name = scheme.getAttribute("codingScheme").getValue();
        String id = scheme.getAttribute("registeredName").getValue();
        String language = scheme.getAttribute("defaultLanguage").getValue();
        Attribute att = scheme.getAttribute("approxNumConcepts");
        int numberOfConcepts = 0;
        if (att != null) numberOfConcepts = Integer.parseInt(att.getValue());
        String fullName = scheme.getAttribute("formalName").getValue();
        CodeSystem system = systemsContainer.getCodeSystem(id, version);
        if (system != null) {
            System.out.println("System " + system.getSystemName() + " version=" + version + " already in database");
            return null;
        }
        system = systemsContainer.newCodeSystem();
        system.init(database, name, version);
        system.setSystemId(id);
        system.setFullName(fullName);
        system.setNumberOfConcepts(numberOfConcepts);
        system.setCodeSystemVersion(version);
        system.setDefaultLanguage(language);
        Attribute isNative = scheme.getAttribute("isNative");
        if (isNative != null) system.setIsNative(new Boolean(isNative.getValue()).booleanValue());
        System.out.println(scheme.getQualifiedName() + ":" + scheme.getAttribute("formalName").getValue());
        System.out.println("codeScheme: " + system.getSystemName() + " " + system.getCodeSystemInfo().getCodeSystemDescription());
        Elements items = scheme.getChildElements();
        for (int j = 0; j < items.size(); j++) {
            Element e = items.get(j);
            if (e.getLocalName().equals("entityDescription")) {
                String description = "";
                description = e.getValue();
                system.setDescription(description);
            }
            if (e.getLocalName().equals("relations")) getRelations(e, system);
            if (e.getLocalName().equals("concepts")) getConcepts(e, system, version);
            if (e.getLocalName().equals("supportedLanguage")) system.addLanguageCode(e.getValue());
            if (e.getLocalName().equals("supportedProperty")) system.addSupportedPropertyCode(e.getValue());
            if (e.getLocalName().equals("supportedAssociationQualifier")) system.addRelationQualifierCode(e.getValue());
            if (e.getLocalName().equals("supportedFormat")) system.addMimeTypeCode(e.getValue());
            if (e.getLocalName().equals("source")) system.setSource(e.getValue());
            if (e.getLocalName().equals("localName")) system.addLocalName(e.getValue());
            if (e.getLocalName().equals("supportedAssociation")) {
                String associationCode = e.getValue();
                if (system.existRelationshipCode(associationCode)) System.out.println("Association " + associationCode + " exists");
            }
        }
        return system;
    }

    /**
     * convert the specified CodeSystem into an XML Document (nu.xom.Document);
     *
     * @param system
     * @return Document
     */
    public static Document translate(CodeSystem system) {
        Element root = new Element("codingSchemes", LGCS);
        root.addNamespaceDeclaration("lgBuiltin", LGBUILTIN);
        root.addNamespaceDeclaration("lgCommon", LGCOMMON);
        root.addNamespaceDeclaration("lgCon", LGCON);
        root.addNamespaceDeclaration("lgCS", LGCS);
        root.addNamespaceDeclaration("lgLDAP", LGLDAP);
        root.addNamespaceDeclaration("lgNaming", LGNAMING);
        root.addNamespaceDeclaration("lgRel", LGREL);
        root.addNamespaceDeclaration("lgService", LGSERVICE);
        root.addNamespaceDeclaration("lgVD", LGVD);
        root.addNamespaceDeclaration("lgVer", LGVER);
        root.addAttribute(new Attribute("dc", "CodingSchemes"));
        Document doc = new Document(root);
        Element codingScheme = new Element("codingScheme");
        root.appendChild(codingScheme);
        codingScheme.addAttribute(new Attribute("codingScheme", system.getSystemName()));
        CodeSystemIdAndVersions version = system.getCodeSystemIdAndVersions();
        String[] versions = version.getCodeSystem_versions();
        codingScheme.addAttribute(new Attribute("representsVersion", versions[0]));
        codingScheme.addAttribute(new Attribute("registeredName", system.getSystemId()));
        codingScheme.addAttribute(new Attribute("formalName", system.getFullName()));
        Element desc = new Element("entityDescription");
        desc.appendChild(system.getDescription());
        desc.setNamespaceURI(LGCOMMON);
        desc.setNamespacePrefix("lgCommon");
        codingScheme.appendChild(desc);
        String[] localNames = system.getLocalNames();
        for (int i = 0; i < localNames.length; i++) {
            Element name = new Element("localName");
            name.appendChild(localNames[i]);
            codingScheme.appendChild(name);
        }
        String source = system.getSource();
        if (source != null) {
            Element esource = new Element("source");
            esource.appendChild(source);
            codingScheme.appendChild(esource);
        }
        String[] languages = system.getLanguageCodes();
        for (int i = 0; i < languages.length; i++) {
            Element lang = new Element("supportedLanguage");
            lang.appendChild(languages[i]);
            codingScheme.appendChild(lang);
        }
        String[] props = system.getSupportedPropertyCodes();
        for (int i = 0; i < props.length; i++) {
            Element elem = new Element("supportedProperty");
            elem.appendChild(props[i]);
            codingScheme.appendChild(elem);
        }
        String[] quals = system.getRelationQualifierCodes();
        for (int i = 0; i < quals.length; i++) {
            Element elem = new Element("supportedAssociationQualifier");
            elem.appendChild(quals[i]);
            codingScheme.appendChild(elem);
        }
        String[] formats = system.getMimetypeCodes();
        for (int i = 0; i < formats.length; i++) {
            Element elem = new Element("supportedFormat");
            elem.appendChild(formats[i]);
            codingScheme.appendChild(elem);
        }
        Iterator it = system.getAssociations();
        while (it.hasNext()) {
            Association assoc = (Association) it.next();
            String name = assoc.getAssociationName();
            Element elem = new Element("supportedAssociation");
            elem.appendChild(name);
            codingScheme.appendChild(elem);
        }
        Element concepts = new Element("concepts");
        concepts.addAttribute(new Attribute("dc", "Concepts"));
        retrieveConcepts(concepts, system);
        codingScheme.appendChild(concepts);
        Element relations = new Element("relations");
        relations.addAttribute(new Attribute("dc", "relations"));
        relations.addAttribute(new Attribute("isNative", "true"));
        retrieveRelations(relations, system);
        codingScheme.appendChild(relations);
        return doc;
    }

    /**
     * write nu.xom.Document to the specified OutputStream
     *
     * @param doc
     * @param out
     */
    public static void printDocument(Document doc, OutputStream out) {
        try {
            Serializer serializer = new Serializer(out, "UTF-8");
            serializer.setIndent(3);
            serializer.write(doc);
            serializer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * print specified CodeSystem to the OutputStream
     *
     * @param system
     * @param out
     */
    public static void printCodingScheme(CodeSystem system, OutputStream out) {
        Document doc = translate(system);
        printDocument(doc, out);
    }

    /**
     * convert the CodedConcepts into LexGrid XML Format
     *
     * @param concepts
     * @param system
     */
    private static void retrieveConcepts(Element concepts, CodeSystem system) {
        Iterator it = system.getCodedConcepts();
        while (it.hasNext()) {
            CodedConcept concept = (CodedConcept) it.next();
            Element elem = new Element("concept");
            elem.setNamespaceURI(LGCON);
            elem.setNamespacePrefix("lgCon");
            elem.addAttribute(new Attribute("conceptCode", concept.getId()));
            elem.addAttribute(new Attribute("conceptStatus", concept.getStatus()));
            if (concept.isAnonymous()) {
                elem.addAttribute(new Attribute("isAnonymous", "true"));
            }
            String description = concept.getDescription();
            if (description != null && !description.equals("")) {
                Element desc = new Element("entityDescription");
                desc.setNamespaceURI(LGCOMMON);
                desc.setNamespacePrefix("lgCommon");
                desc.appendChild(concept.getDescription());
                elem.appendChild(desc);
            }
            Property[] props = concept.getProperties();
            for (int i = 0; i < props.length; i++) {
                String value = props[i].getValue();
                String code = props[i].getCode();
                String name = "property";
                if (code.equals("textualPresentation")) name = "presentation";
                if (code.equals("definition") || name.equals("comment") || name.equals("instruction")) name = code;
                Element prop = new Element(name);
                prop.setNamespaceURI(LGCON);
                prop.setNamespacePrefix("lgCon");
                prop.addAttribute(new Attribute("property", code));
                prop.addAttribute(new Attribute("propertyId", props[i].getId()));
                String language = props[i].getLanguage();
                if (language != null) prop.addAttribute(new Attribute("language", language));
                boolean isPreferred = props[i].isPreferred();
                prop.addAttribute(new Attribute("isPreferred", Boolean.toString(isPreferred)));
                Element text = new Element("text");
                text.setNamespaceURI(LGCON);
                text.setNamespacePrefix("lgCon");
                text.appendChild(value);
                prop.appendChild(text);
                elem.appendChild(prop);
            }
            concepts.appendChild(elem);
        }
    }

    /**
     * convert the Relations into LexGrid XML form
     *
     * @param relations
     * @param system
     */
    private static void retrieveRelations(Element relations, CodeSystem system) {
        Iterator it = system.getAssociations();
        while (it.hasNext()) {
            Association assoc = (Association) it.next();
            Element elem = new Element("association");
            elem.setNamespaceURI(LGREL);
            elem.setNamespacePrefix("lgRel");
            elem.addAttribute(new Attribute("association", assoc.getAssociationName()));
            elem.addAttribute(new Attribute("forwardName", assoc.getForwardName()));
            elem.addAttribute(new Attribute("reverseName", assoc.getReverseName()));
            NameValue[] nv = assoc.getAttributes();
            for (int i = 0; i < nv.length; i++) {
                String value = nv[i].getValue();
                if (!value.equalsIgnoreCase("false")) elem.addAttribute(new Attribute(nv[i].getName(), value));
            }
            Iterator itConcepts = system.getCodedConcepts();
            while (itConcepts.hasNext()) {
                ConceptId conceptId = ((CodedConcept) itConcepts.next()).getConceptId();
                ArrayList list = system.getSourceRelations(conceptId, assoc.getAssociationName());
                if (list.size() > 0) {
                    Element sourceConcept = new Element("sourceConcept");
                    sourceConcept.setNamespaceURI(LGREL);
                    sourceConcept.setNamespacePrefix("lgRel");
                    sourceConcept.addAttribute(new Attribute("sourceConcept", conceptId.getConcept_code()));
                    for (int i = 0; i < list.size(); i++) {
                        String targetCode = ((Relationship) list.get(i)).getTargetConceptId().getConcept_code();
                        Element targetConcept = new Element("targetConcept");
                        targetConcept.setNamespaceURI(LGREL);
                        targetConcept.setNamespacePrefix("lgRel");
                        targetConcept.addAttribute(new Attribute("targetConcept", targetCode));
                        sourceConcept.appendChild(targetConcept);
                    }
                    elem.appendChild(sourceConcept);
                }
            }
            relations.appendChild(elem);
        }
    }
}
