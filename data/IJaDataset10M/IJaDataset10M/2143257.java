package gleam.gateservice.message;

/**
 * Request message body for a GATE-mode call.
 */
public class GATEModeMessageBody extends RequestMessageBody {

    static final long serialVersionUID = -7110640510750787266L;

    /**
   * The document to process, in GATE XML format.
   */
    protected String documentXml;

    public GATEModeMessageBody(String documentXml) {
        super();
        this.documentXml = documentXml;
    }

    public String getDocumentXml() {
        return documentXml;
    }

    public void setDocumentXml(String documentXml) {
        this.documentXml = documentXml;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(", documentXml = ");
        if (documentXml == null) {
            sb.append("null");
        } else {
            sb.append('"');
            sb.append(documentXml);
            sb.append('"');
        }
        return sb.toString();
    }
}
