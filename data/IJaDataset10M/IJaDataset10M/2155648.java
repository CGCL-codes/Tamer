package org.mobicents.slee.sippresence.pojo.xcaperror;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.mobicents.slee.sippresence.pojo.xcaperror package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private static final QName _CannotDelete_QNAME = new QName("urn:ietf:params:xml:ns:xcap-error", "cannot-delete");

    private static final QName _NotWellFormed_QNAME = new QName("urn:ietf:params:xml:ns:xcap-error", "not-well-formed");

    private static final QName _NoParent_QNAME = new QName("urn:ietf:params:xml:ns:xcap-error", "no-parent");

    private static final QName _ConstraintFailure_QNAME = new QName("urn:ietf:params:xml:ns:xcap-error", "constraint-failure");

    private static final QName _NotXmlAttValue_QNAME = new QName("urn:ietf:params:xml:ns:xcap-error", "not-xml-att-value");

    private static final QName _NotXmlFrag_QNAME = new QName("urn:ietf:params:xml:ns:xcap-error", "not-xml-frag");

    private static final QName _NotUtf8_QNAME = new QName("urn:ietf:params:xml:ns:xcap-error", "not-utf-8");

    private static final QName _CannotInsert_QNAME = new QName("urn:ietf:params:xml:ns:xcap-error", "cannot-insert");

    private static final QName _UniquenessFailure_QNAME = new QName("urn:ietf:params:xml:ns:xcap-error", "uniqueness-failure");

    private static final QName _ErrorElement_QNAME = new QName("urn:ietf:params:xml:ns:xcap-error", "error-element");

    private static final QName _SchemaValidationError_QNAME = new QName("urn:ietf:params:xml:ns:xcap-error", "schema-validation-error");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.mobicents.slee.sippresence.pojo.xcaperror
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SchemaValidationError }
     * 
     */
    public SchemaValidationError createSchemaValidationError() {
        return new SchemaValidationError();
    }

    /**
     * Create an instance of {@link ConstraintFailure }
     * 
     */
    public ConstraintFailure createConstraintFailure() {
        return new ConstraintFailure();
    }

    /**
     * Create an instance of {@link UniquenessFailure.Exists }
     * 
     */
    public UniquenessFailure.Exists createUniquenessFailureExists() {
        return new UniquenessFailure.Exists();
    }

    /**
     * Create an instance of {@link NotWellFormed }
     * 
     */
    public NotWellFormed createNotWellFormed() {
        return new NotWellFormed();
    }

    /**
     * Create an instance of {@link CannotInsert }
     * 
     */
    public CannotInsert createCannotInsert() {
        return new CannotInsert();
    }

    /**
     * Create an instance of {@link NoParent }
     * 
     */
    public NoParent createNoParent() {
        return new NoParent();
    }

    /**
     * Create an instance of {@link NotUtf8 }
     * 
     */
    public NotUtf8 createNotUtf8() {
        return new NotUtf8();
    }

    /**
     * Create an instance of {@link CannotDelete }
     * 
     */
    public CannotDelete createCannotDelete() {
        return new CannotDelete();
    }

    /**
     * Create an instance of {@link NotXmlFrag }
     * 
     */
    public NotXmlFrag createNotXmlFrag() {
        return new NotXmlFrag();
    }

    /**
     * Create an instance of {@link UniquenessFailure }
     * 
     */
    public UniquenessFailure createUniquenessFailure() {
        return new UniquenessFailure();
    }

    /**
     * Create an instance of {@link NotXmlAttValue }
     * 
     */
    public NotXmlAttValue createNotXmlAttValue() {
        return new NotXmlAttValue();
    }

    /**
     * Create an instance of {@link XcapError }
     * 
     */
    public XcapError createXcapError() {
        return new XcapError();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CannotDelete }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:ietf:params:xml:ns:xcap-error", name = "cannot-delete", substitutionHeadNamespace = "urn:ietf:params:xml:ns:xcap-error", substitutionHeadName = "error-element")
    public JAXBElement<CannotDelete> createCannotDelete(CannotDelete value) {
        return new JAXBElement<CannotDelete>(_CannotDelete_QNAME, CannotDelete.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NotWellFormed }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:ietf:params:xml:ns:xcap-error", name = "not-well-formed", substitutionHeadNamespace = "urn:ietf:params:xml:ns:xcap-error", substitutionHeadName = "error-element")
    public JAXBElement<NotWellFormed> createNotWellFormed(NotWellFormed value) {
        return new JAXBElement<NotWellFormed>(_NotWellFormed_QNAME, NotWellFormed.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NoParent }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:ietf:params:xml:ns:xcap-error", name = "no-parent", substitutionHeadNamespace = "urn:ietf:params:xml:ns:xcap-error", substitutionHeadName = "error-element")
    public JAXBElement<NoParent> createNoParent(NoParent value) {
        return new JAXBElement<NoParent>(_NoParent_QNAME, NoParent.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConstraintFailure }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:ietf:params:xml:ns:xcap-error", name = "constraint-failure", substitutionHeadNamespace = "urn:ietf:params:xml:ns:xcap-error", substitutionHeadName = "error-element")
    public JAXBElement<ConstraintFailure> createConstraintFailure(ConstraintFailure value) {
        return new JAXBElement<ConstraintFailure>(_ConstraintFailure_QNAME, ConstraintFailure.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NotXmlAttValue }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:ietf:params:xml:ns:xcap-error", name = "not-xml-att-value", substitutionHeadNamespace = "urn:ietf:params:xml:ns:xcap-error", substitutionHeadName = "error-element")
    public JAXBElement<NotXmlAttValue> createNotXmlAttValue(NotXmlAttValue value) {
        return new JAXBElement<NotXmlAttValue>(_NotXmlAttValue_QNAME, NotXmlAttValue.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NotXmlFrag }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:ietf:params:xml:ns:xcap-error", name = "not-xml-frag", substitutionHeadNamespace = "urn:ietf:params:xml:ns:xcap-error", substitutionHeadName = "error-element")
    public JAXBElement<NotXmlFrag> createNotXmlFrag(NotXmlFrag value) {
        return new JAXBElement<NotXmlFrag>(_NotXmlFrag_QNAME, NotXmlFrag.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NotUtf8 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:ietf:params:xml:ns:xcap-error", name = "not-utf-8", substitutionHeadNamespace = "urn:ietf:params:xml:ns:xcap-error", substitutionHeadName = "error-element")
    public JAXBElement<NotUtf8> createNotUtf8(NotUtf8 value) {
        return new JAXBElement<NotUtf8>(_NotUtf8_QNAME, NotUtf8.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CannotInsert }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:ietf:params:xml:ns:xcap-error", name = "cannot-insert", substitutionHeadNamespace = "urn:ietf:params:xml:ns:xcap-error", substitutionHeadName = "error-element")
    public JAXBElement<CannotInsert> createCannotInsert(CannotInsert value) {
        return new JAXBElement<CannotInsert>(_CannotInsert_QNAME, CannotInsert.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UniquenessFailure }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:ietf:params:xml:ns:xcap-error", name = "uniqueness-failure", substitutionHeadNamespace = "urn:ietf:params:xml:ns:xcap-error", substitutionHeadName = "error-element")
    public JAXBElement<UniquenessFailure> createUniquenessFailure(UniquenessFailure value) {
        return new JAXBElement<UniquenessFailure>(_UniquenessFailure_QNAME, UniquenessFailure.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:ietf:params:xml:ns:xcap-error", name = "error-element")
    public JAXBElement<Object> createErrorElement(Object value) {
        return new JAXBElement<Object>(_ErrorElement_QNAME, Object.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SchemaValidationError }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:ietf:params:xml:ns:xcap-error", name = "schema-validation-error", substitutionHeadNamespace = "urn:ietf:params:xml:ns:xcap-error", substitutionHeadName = "error-element")
    public JAXBElement<SchemaValidationError> createSchemaValidationError(SchemaValidationError value) {
        return new JAXBElement<SchemaValidationError>(_SchemaValidationError_QNAME, SchemaValidationError.class, null, value);
    }
}
