package gleam.docservice;

public class DocumentInfo {

    private String documentID, documentName;

    public DocumentInfo() {
    }

    public DocumentInfo(String documentID, String documentName) {
        this.documentID = documentID;
        this.documentName = documentName;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    @Override
    public String toString() {
        return "Docservice document info. ID=" + documentID + " name=" + documentName;
    }
}
