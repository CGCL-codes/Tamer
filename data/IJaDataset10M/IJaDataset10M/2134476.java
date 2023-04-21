package org.dcm4che2.cda;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * @author Gunter Zeilinger<gunterze@gmail.com>
 * @version $Revision$ $Date$
 * @since Mar 10, 2008
 */
public class ClinicalDocument extends BaseElement {

    private static final String XML_VERSION_1_0_ENCODING_UTF_8 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    private static boolean incXMLDecl = false;

    private static final BaseElement TYPE_ID = new BaseElement("typeId", new String[] { "extension", "root" }, new Object[] { "POCD_HD000040", "2.16.840.1.113883.1.3" });

    private ID id;

    private Code code;

    private Title title;

    private EffectiveTime effectiveTime;

    private ConfidentialityCode confidentialityCode;

    private LanguageCode languageCode;

    private List<RecordTarget> recordTargets;

    private List<Author> authors;

    private DataEnterer dataEnterer;

    private Custodian custodian;

    private LegalAuthenticator legalAuthenticator;

    private List<DocumentationOf> documentationOfs;

    private Component component;

    public ClinicalDocument() {
        super("ClinicalDocument", new String[] { "xmlns", "xmlns:xsi", "classCode", "moodCode", "xsi:schemaLocation" }, new Object[] { "urn:hl7-org:v3", "http://www.w3.org/2001/XMLSchema-instance", "DOCCLIN", "EVN", "urn:hl7-org:v3 CDA.xsd" });
    }

    public static final boolean isIncludeXMLDeclaration() {
        return incXMLDecl;
    }

    public static final void setIncludeXMLDeclaration(boolean incXMLDecl) {
        ClinicalDocument.incXMLDecl = incXMLDecl;
    }

    public ID getID() {
        return id;
    }

    public ClinicalDocument setID(ID id) {
        this.id = id;
        return this;
    }

    public Code getCode() {
        return code;
    }

    public ClinicalDocument setCode(Code code) {
        this.code = code;
        return this;
    }

    public String getTitle() {
        return title.getText();
    }

    public ClinicalDocument setTitle(String text) {
        this.title = new Title(text);
        return this;
    }

    public EffectiveTime getEffectiveTime() {
        return effectiveTime;
    }

    public ClinicalDocument setEffectiveTime(EffectiveTime effectiveTime) {
        this.effectiveTime = effectiveTime;
        return this;
    }

    public CodeElement getConfidentialityCode() {
        return confidentialityCode;
    }

    public ClinicalDocument setConfidentialityCode(ConfidentialityCode confidentialityCode) {
        this.confidentialityCode = confidentialityCode;
        return this;
    }

    public LanguageCode getLanguageCode() {
        return languageCode;
    }

    public ClinicalDocument setLanguageCode(LanguageCode code) {
        this.languageCode = code;
        return this;
    }

    public List<RecordTarget> getRecordTargets() {
        return recordTargets;
    }

    public ClinicalDocument setRecordTargets(List<RecordTarget> recordTargets) {
        this.recordTargets = recordTargets;
        return this;
    }

    public RecordTarget getRecordTarget() {
        return recordTargets != null && !recordTargets.isEmpty() ? recordTargets.get(0) : null;
    }

    public ClinicalDocument setRecordTarget(RecordTarget recordTarget) {
        this.recordTargets = Collections.singletonList(recordTarget);
        return this;
    }

    public ClinicalDocument addRecordTarget(RecordTarget recordTarget) {
        if (recordTargets == null) {
            return setRecordTarget(recordTarget);
        }
        try {
            recordTargets.add(recordTarget);
        } catch (UnsupportedOperationException e) {
            List<RecordTarget> tmp = new ArrayList<RecordTarget>();
            tmp.addAll(recordTargets);
            tmp.add(recordTarget);
            recordTargets = tmp;
        }
        return this;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public ClinicalDocument setAuthors(List<Author> authors) {
        this.authors = authors;
        return this;
    }

    public Author getAuthor() {
        return authors != null && !authors.isEmpty() ? authors.get(0) : null;
    }

    public ClinicalDocument setAuthor(Author author) {
        this.authors = Collections.singletonList(author);
        return this;
    }

    public ClinicalDocument addAuthor(Author author) {
        if (authors == null) {
            return setAuthor(author);
        }
        try {
            authors.add(author);
        } catch (UnsupportedOperationException e) {
            List<Author> tmp = new ArrayList<Author>();
            tmp.addAll(authors);
            tmp.add(author);
            authors = tmp;
        }
        return this;
    }

    public DataEnterer getDataEnterer() {
        return dataEnterer;
    }

    public ClinicalDocument setDataEnterer(DataEnterer dataEnterer) {
        this.dataEnterer = dataEnterer;
        return this;
    }

    public Custodian getCustodian() {
        return custodian;
    }

    public ClinicalDocument setCustodian(Custodian custodian) {
        this.custodian = custodian;
        return this;
    }

    public LegalAuthenticator getLegalAuthenticator() {
        return legalAuthenticator;
    }

    public ClinicalDocument setLegalAuthenticator(LegalAuthenticator legalAuthenticator) {
        this.legalAuthenticator = legalAuthenticator;
        return this;
    }

    public List<DocumentationOf> getDocumentationOfs() {
        return documentationOfs;
    }

    public ClinicalDocument setDocumentationOfs(List<DocumentationOf> documentationOfs) {
        this.documentationOfs = documentationOfs;
        return this;
    }

    public DocumentationOf getDocumentationOf() {
        return documentationOfs != null && !documentationOfs.isEmpty() ? documentationOfs.get(0) : null;
    }

    public ClinicalDocument setDocumentationOf(DocumentationOf documentationOf) {
        this.documentationOfs = Collections.singletonList(documentationOf);
        return this;
    }

    public ClinicalDocument addDocumentationOf(DocumentationOf documentationOf) {
        if (documentationOfs == null) {
            return setDocumentationOf(documentationOf);
        }
        try {
            documentationOfs.add(documentationOf);
        } catch (UnsupportedOperationException e) {
            List<DocumentationOf> tmp = new ArrayList<DocumentationOf>();
            tmp.addAll(documentationOfs);
            tmp.add(documentationOf);
            documentationOfs = tmp;
        }
        return this;
    }

    public Component getComponent() {
        return component;
    }

    public ClinicalDocument setComponent(Component component) {
        this.component = component;
        return this;
    }

    @Override
    protected boolean isEmpty() {
        return false;
    }

    @Override
    public String toXML() {
        return toXML(1024);
    }

    @Override
    public void writeTo(Writer out) throws IOException {
        if (incXMLDecl) {
            out.write(XML_VERSION_1_0_ENCODING_UTF_8);
        }
        super.writeTo(out);
    }

    @Override
    protected void writeContentTo(Writer out) throws IOException {
        writeTo(TYPE_ID, out);
        writeTo(id, out);
        writeTo(code, out);
        writeTo(title, out);
        writeTo(effectiveTime, out);
        writeTo(confidentialityCode, out);
        writeTo(languageCode, out);
        writeTo(recordTargets, out);
        writeTo(authors, out);
        writeTo(dataEnterer, out);
        writeTo(custodian, out);
        writeTo(legalAuthenticator, out);
        writeTo(documentationOfs, out);
        writeTo(component, out);
    }

    private static class Title extends TextElement {

        private Title(String text) {
            super("title", text);
        }
    }

    public static class EffectiveTime extends TimeElement {

        public EffectiveTime(String time) {
            super("effectiveTime", time);
        }

        public EffectiveTime(Date time, boolean tz) {
            super("effectiveTime", time, tz);
        }

        public EffectiveTime(Date time, TimeZone tz) {
            super("effectiveTime", time, tz);
        }
    }
}
