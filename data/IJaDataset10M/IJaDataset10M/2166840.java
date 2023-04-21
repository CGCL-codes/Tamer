package org.openofficesearch.data;

/**
 * <p>Holds information about a posting.</p>
 * <p>A posting links a dictionary term to a document and indicates how many
 * times the term appears in the document.</p>
 * Created: 2005
 * @author Connor Garvey
 * @version 0.0.1
 * @since 0.0.1
 */
public class Posting {

    private Integer termID;

    private Integer documentID;

    private Integer termCount;

    /**
   * Creates a new posting
   */
    public Posting() {
        this.setTermID(null);
        this.setDocumentID(null);
        this.setTermCount(null);
    }

    /**
   * Sets the ID of the dictionary term
   * @param termID The ID to set
   */
    public void setTermID(Integer termID) {
        this.termID = termID;
    }

    /**
   * Sets the ID of the document
   * @param documentID The ID of the document to set
   */
    public void setDocumentID(Integer documentID) {
        this.documentID = documentID;
    }

    /**
   * Sets the number of times the term appears in the document
   * @param termCount The number of appearances of the term in the document
   */
    public void setTermCount(Integer termCount) {
        this.termCount = termCount;
    }

    /**
   * Gets the ID of the term
   * @return The ID of the posting's term
   */
    public Integer getTermID() {
        return termID;
    }

    /**
   * Gets the ID of the document
   * @return The posting's document ID
   */
    public Integer getDocumentID() {
        return documentID;
    }

    /**
   * Gets the number of times the term occurs in the document
   * @return The term posting count
   */
    public Integer getTermCount() {
        return termCount;
    }
}
