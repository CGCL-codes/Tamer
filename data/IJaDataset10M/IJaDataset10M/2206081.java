package it.webscience.kpeople.be;

/**
 * Tabella DOCUMENT_TYPE.
 *
 */
public class DocumentType extends DataTraceClass {

    /**
     * Chiave primaria.
     * Identificativo univoco per un record della tabella DOCUMENT_TYPE
     */
    private int idDocumentType;

    /**
     * Campo obbligatorio.
     * Campo obligatorio. Nome della tipologia di documento.
     */
    private String name;

    /** Costruttore. */
    public DocumentType() {
        super();
    }

    /** Costruttore.
     * @param in id
     */
    public DocumentType(final int in) {
        super();
        this.idDocumentType = in;
    }

    /**
     * @return the name
     */
    public final String getName() {
        return name;
    }

    /**
     * @param in the name to set
     */
    public final void setName(final String in) {
        this.name = in;
    }

    /**
     * @return the idDocumentType
     */
    public final int getIdDocumentType() {
        return idDocumentType;
    }

    /**
     * @param in the idDocumentType to set
     */
    public final void setIdDocumentType(final int in) {
        this.idDocumentType = in;
    }
}
