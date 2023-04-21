package com.windsor.node.service.helper;

public interface DocumentHelper {

    /**
     * Gets an content of a singel document
     * 
     * @param transactionID
     * @param documentID
     * @return
     */
    byte[] getDocumentContent(String transactionId, String documentId);

    /**
     * Gets an instance of a multiple documents
     * 
     * @param transactionID
     * @return
     */
    String[] getDocumentList(String transactionId);

    /**
     * Saves a dpcument to the repository
     * 
     * @param docuemntID
     * @param transactionID
     * @param documentContent
     */
    void saveDocument(String docuemntId, String transactionId, byte[] documentContent);

    /**
     * Deletes all documents for particular transaction
     * 
     * @param transactionID
     */
    void deleteDocuments(String transactionId);
}
