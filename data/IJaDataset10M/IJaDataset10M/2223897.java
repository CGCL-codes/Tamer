package javax.print.attribute.standard;

import javax.print.attribute.EnumSyntax;
import javax.print.attribute.PrintJobAttribute;
import javax.print.attribute.PrintRequestAttribute;

/**
 * The <code>MultipleDocumentHandling</code> printing attribute controls
 * how certain printing attributes affect printing in case of multiple 
 * documents in a print job. This attribute is only relevant if a job 
 * has multiple documents.
 * <p>
 * <b>IPP Compatibility:</b> MultipleDocumentHandling is an IPP 1.1 attribute.
 * </p>
 * 
 * @author Michael Koch (konqueror@gmx.de)
 * @author Wolfgang Baer (WBaer@gmx.de)
 */
public class MultipleDocumentHandling extends EnumSyntax implements PrintJobAttribute, PrintRequestAttribute {

    private static final long serialVersionUID = 8098326460746413466L;

    /** 
   * Multiple documents are treated as a single document.
   */
    public static final MultipleDocumentHandling SINGLE_DOCUMENT = new MultipleDocumentHandling(0);

    /** 
   * Multiple documents are treated as uncollated copies.
   */
    public static final MultipleDocumentHandling SEPARATE_DOCUMENTS_UNCOLLATED_COPIES = new MultipleDocumentHandling(1);

    /** 
   * Multiple documents are treated as collated copies. 
   */
    public static final MultipleDocumentHandling SEPARATE_DOCUMENTS_COLLATED_COPIES = new MultipleDocumentHandling(2);

    /** 
   * Multiple documents are treated so that every single document starts
   * with a new sheet. 
   */
    public static final MultipleDocumentHandling SINGLE_DOCUMENT_NEW_SHEET = new MultipleDocumentHandling(3);

    private static final String[] stringTable = { "single-document", "separate-documents-uncollated-copies", "separate-documents-collated-copies", "single-document-new-sheet" };

    private static final MultipleDocumentHandling[] enumValueTable = { SINGLE_DOCUMENT, SEPARATE_DOCUMENTS_UNCOLLATED_COPIES, SEPARATE_DOCUMENTS_COLLATED_COPIES, SINGLE_DOCUMENT_NEW_SHEET };

    /**
   * Constructs a <code>MultipleDocumentHandling</code> object.
   * 
   * @param value the enum value
   */
    protected MultipleDocumentHandling(int value) {
        super(value);
    }

    /**
   * Returns category of this class.
   *
   * @return The class <code>MultipleDocumentHandling</code> itself.
   */
    public Class getCategory() {
        return MultipleDocumentHandling.class;
    }

    /**
   * Returns the name of this attribute.
   *
   * @return The name "multiple-document-handling".
   */
    public String getName() {
        return "multiple-document-handling";
    }

    /**
   * Returns a table with the enumeration values represented as strings
   * for this object.
   *
   * @return The enumeration values as strings.
   */
    protected String[] getStringTable() {
        return stringTable;
    }

    /**
   * Returns a table with the enumeration values for this object.
   *
   * @return The enumeration values.
   */
    protected EnumSyntax[] getEnumValueTable() {
        return enumValueTable;
    }
}
