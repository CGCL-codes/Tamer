package reportingModule;

/**
 *
 * @author user
 */
public class DataFetcherPeerSearch {

    private String documentName;

    private String plagValue;

    private String suspectedDocName;

    public DataFetcherPeerSearch(String docNameTemp, String suspectedTemp, String percentage) {
        setDocumentName(docNameTemp);
        setPlagValue(percentage);
        setSuspectedDocName(suspectedTemp);
    }

    public DataFetcherPeerSearch() {
    }

    /**
     * @return the docName
     */
    public String getDocumentName() {
        return documentName;
    }

    /**
     * @param docName the docName to set
     */
    public void setDocumentName(String docName) {
        this.documentName = docName;
    }

    /**
     * @return the plagValue
     */
    public String getPlagValue() {
        return plagValue;
    }

    /**
     * @param plagValue the plagValue to set
     */
    public void setPlagValue(String plagValue) {
        this.plagValue = plagValue;
    }

    /**
     * @return the suspectedDocName
     */
    public String getSuspectedDocName() {
        return suspectedDocName;
    }

    /**
     * @param suspectedDocName the suspectedDocName to set
     */
    public void setSuspectedDocName(String suspectedDocName) {
        this.suspectedDocName = suspectedDocName;
    }
}
