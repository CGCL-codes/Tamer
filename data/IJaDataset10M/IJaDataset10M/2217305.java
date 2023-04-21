package org.openuss.repository;

/**
 * <p>
 * This Repository service will provide the basic method to put into, retrieve ,
 * or delete a file from the internal repository. Each file will be unique
 * identified by its id.
 * </p>
 */
public interface RepositoryService {

    /**
     * 
     */
    public void saveContent(Long fileId, java.io.InputStream content);

    /**
     * 
     */
    public java.io.InputStream loadContent(Long fileId);

    /**
     * 
     */
    public void removeContent(Long fileId);

    /**
	 * <p>
	 * Defines the repository locations. Normaly a absolute path on the server.
	 * </p>
	 */
    public void setRepositoryLocation(String path);

    /**
	 * <p>
	 * The repository location path.
	 * </p>
	 */
    public String getRepositoryLocation();
}
