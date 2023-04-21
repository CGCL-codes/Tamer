package net.brutex.xmlbridge.entities.gen;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.brutex.xmlbridge.entities.gen package. 
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

    private static final QName _ItemLinkTypeItemID_QNAME = new QName("http://www.brutex.net/schemas/xmlbridge/ItemData", "itemID");

    private static final QName _NameValueTypeValue_QNAME = new QName("http://www.brutex.net/schemas/xmlbridge/ItemData", "value");

    private static final QName _FileAttachmentTypeName_QNAME = new QName("http://www.brutex.net/schemas/xmlbridge/ItemData", "name");

    private static final QName _FileAttachmentTypeFileName_QNAME = new QName("http://www.brutex.net/schemas/xmlbridge/ItemData", "fileName");

    private static final QName _FileAttachmentTypeUrl_QNAME = new QName("http://www.brutex.net/schemas/xmlbridge/ItemData", "url");

    private static final QName _NoteTypeTitle_QNAME = new QName("http://www.brutex.net/schemas/xmlbridge/ItemData", "title");

    private static final QName _NoteTypeNote_QNAME = new QName("http://www.brutex.net/schemas/xmlbridge/ItemData", "note");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.brutex.xmlbridge.entities.gen
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link FieldListType }
     * 
     */
    public FieldListType createFieldListType() {
        return new FieldListType();
    }

    /**
     * Create an instance of {@link ItemLinkListType }
     * 
     */
    public ItemLinkListType createItemLinkListType() {
        return new ItemLinkListType();
    }

    /**
     * Create an instance of {@link ItemHeaderType }
     * 
     */
    public ItemHeaderType createItemHeaderType() {
        return new ItemHeaderType();
    }

    /**
     * Create an instance of {@link ItemLinkType }
     * 
     */
    public ItemLinkType createItemLinkType() {
        return new ItemLinkType();
    }

    /**
     * Create an instance of {@link NameValueType }
     * 
     */
    public NameValueType createNameValueType() {
        return new NameValueType();
    }

    /**
     * Create an instance of {@link FileAttachmentType }
     * 
     */
    public FileAttachmentType createFileAttachmentType() {
        return new FileAttachmentType();
    }

    /**
     * Create an instance of {@link FreeformInstructionType }
     * 
     */
    public FreeformInstructionType createFreeformInstructionType() {
        return new FreeformInstructionType();
    }

    /**
     * Create an instance of {@link NoteType }
     * 
     */
    public NoteType createNoteType() {
        return new NoteType();
    }

    /**
     * Create an instance of {@link URLAttachmentType }
     * 
     */
    public URLAttachmentType createURLAttachmentType() {
        return new URLAttachmentType();
    }

    /**
     * Create an instance of {@link ItemDataType }
     * 
     */
    public ItemDataType createItemDataType() {
        return new ItemDataType();
    }

    /**
     * Create an instance of {@link FileAttachmentContentsType }
     * 
     */
    public FileAttachmentContentsType createFileAttachmentContentsType() {
        return new FileAttachmentContentsType();
    }

    /**
     * Create an instance of {@link ItemDataListType }
     * 
     */
    public ItemDataListType createItemDataListType() {
        return new ItemDataListType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.brutex.net/schemas/xmlbridge/ItemData", name = "itemID", scope = ItemLinkType.class)
    public JAXBElement<String> createItemLinkTypeItemID(String value) {
        return new JAXBElement<String>(_ItemLinkTypeItemID_QNAME, String.class, ItemLinkType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.brutex.net/schemas/xmlbridge/ItemData", name = "value", scope = NameValueType.class)
    public JAXBElement<String> createNameValueTypeValue(String value) {
        return new JAXBElement<String>(_NameValueTypeValue_QNAME, String.class, NameValueType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.brutex.net/schemas/xmlbridge/ItemData", name = "name", scope = FileAttachmentType.class)
    public JAXBElement<String> createFileAttachmentTypeName(String value) {
        return new JAXBElement<String>(_FileAttachmentTypeName_QNAME, String.class, FileAttachmentType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.brutex.net/schemas/xmlbridge/ItemData", name = "fileName", scope = FileAttachmentType.class)
    public JAXBElement<String> createFileAttachmentTypeFileName(String value) {
        return new JAXBElement<String>(_FileAttachmentTypeFileName_QNAME, String.class, FileAttachmentType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.brutex.net/schemas/xmlbridge/ItemData", name = "url", scope = FileAttachmentType.class)
    public JAXBElement<String> createFileAttachmentTypeUrl(String value) {
        return new JAXBElement<String>(_FileAttachmentTypeUrl_QNAME, String.class, FileAttachmentType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.brutex.net/schemas/xmlbridge/ItemData", name = "title", scope = NoteType.class)
    public JAXBElement<String> createNoteTypeTitle(String value) {
        return new JAXBElement<String>(_NoteTypeTitle_QNAME, String.class, NoteType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.brutex.net/schemas/xmlbridge/ItemData", name = "note", scope = NoteType.class)
    public JAXBElement<String> createNoteTypeNote(String value) {
        return new JAXBElement<String>(_NoteTypeNote_QNAME, String.class, NoteType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.brutex.net/schemas/xmlbridge/ItemData", name = "name", scope = URLAttachmentType.class)
    public JAXBElement<String> createURLAttachmentTypeName(String value) {
        return new JAXBElement<String>(_FileAttachmentTypeName_QNAME, String.class, URLAttachmentType.class, value);
    }
}
