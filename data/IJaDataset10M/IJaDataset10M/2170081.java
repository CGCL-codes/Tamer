package org.in4ama.documentengine.repository;

import java.io.InputStream;

/**
 * Represents an information about a pack fragment that is stored in the request
 * repository
 */
public class PackFragmentRepositoryEntry {

    private String projectName;

    private String documentName;

    private int documentType;

    private InputStream inputStream;

    /** Creates a new instance of PackFragment repository entry */
    public PackFragmentRepositoryEntry() {
    }

    /**
	 * Creates a new instance of PackRepositoryEntry.
	 * 
	 * @param documentName
	 *            the name of the document
	 * @param documentType
	 			  the type of the document
	 */
    public PackFragmentRepositoryEntry(String documentName, int documentType) {
        this.documentName = documentName;
        this.documentType = documentType;
    }

    /** Gets the name of the owning project */
    public String getProjectName() {
        return projectName;
    }

    /** Sets the name of the owning project */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /** Gets an input stream containing the fragment */
    public InputStream getInputStream() {
        return inputStream;
    }

    /** Sets an input stream containing the pack fragment */
    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    /** Gets the name of the document */
    public String getDocumentName() {
        return documentName;
    }

    /** Sets the name of the document */
    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    /** Gets the type of the document */
    public int getDocumentType() {
        return documentType;
    }

    /** Sets the type of the document */
    public void setDocumentType(int documentType) {
        this.documentType = documentType;
    }
}
