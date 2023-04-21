package net.sf.nxqd;

import net.sf.nxqd.common.NxqdUtils;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;

/**
 * The class <code>NxqdContainer</code> is the for
 * interacting with Nxqd containers, such as adding or deleting documents.
 *
 * @author <a href="mailto:webhiker@sourceforge.net">webhiker</a>
 * @version 1.0
 */
public class NxqdContainer extends NxqdConsumer {

    /**
     * The variable <code>logger</code> is used for logging events.
     *
     */
    private static Logger logger = Logger.getLogger(NxqdContainer.class.getName());

    private String containerName;

    protected NxqdContainer(final NxqdManager manager, final String containerName) throws NxqdException {
        super(manager);
        this.containerName = containerName;
    }

    /**
     * The <code>getName</code> method returns the name of this Container.
     *
     * @return a <code>String</code> value
     */
    public final String getName() {
        return this.containerName;
    }

    /**
     * Returns the number of XML Documents in this container.
     *
     * @return an <code>long</code> value
     * @exception XmlException if an error occurs
     */
    public long getNumDocuments() throws NxqdException {
        return getNxqdManager().getNumDocuments(getName());
    }

    /**
     * The <code>putDocument</code> method adds the document to this
     * container using the specified documentId. An exception is thrown if
     * the specified documentId already exists.
     *
     * @param documentId a <code>String</code> value
     * @param document a <code>NxqdXMLValue</code> value
     * @exception NxqdException if an error occurs
     */
    public void putDocument(final String documentId, NxqdXMLValue document) throws NxqdException {
        getNxqdManager().putDocument(getName(), documentId, document.asString());
    }

    /**
     * The <code>getDocument</code> method retrieves the specified document from this
     * container using the specified documentId. An exception is thrown if
     * the specified documentId does not exist.
     *
     * @param documentId a <code>String</code> value
     * @return a <code>NxqdXMLValue</code> value
     * @exception NxqdException if an error occurs
     */
    public NxqdXMLValue getDocument(final String documentId) throws NxqdException {
        return new NxqdXMLValue(getNxqdManager().getDocument(getName(), documentId));
    }

    /**
     * The <code>deleteDocument</code> method deletes the specified document from this
     * container using the specified documentId. An exception is thrown if
     * an error occurred while deleting the document.
     *
     * @param documentId a <code>String</code> value
     * @exception NxqdException if an error occurs
     */
    public void deleteDocument(final String documentId) throws NxqdException {
        getNxqdManager().deleteDocument(getName(), documentId);
    }

    /**
     * The <code>documentExists</code> method checks if
     * the specified document exists. It will return
     * true if the document exists, false otherwise.
     * A NxqdException is thrown if other errors occur.
     *
     * @param documentId a <code>String</code> value
     * @return a <code>boolean</code> value
     * @exception NxqdException if an error occurs
     */
    public boolean documentExists(String documentId) throws NxqdException {
        return getNxqdManager().documentExists(getName(), documentId);
    }

    /**
     * The <code>listDocuments</code> method returns a List containing
     * the document id's managed by the server. This list may
     * be empty, but it will never be null, and contains <code>String</code>
     * values.
     *
     * @return a <code>List</code> value
     * @exception NxqdException if an error occurs
     */
    public List listDocuments() throws NxqdException {
        return getNxqdManager().listDocuments(getName());
    }

    /**
     * The <code>putBlob</code> method stores the specified blob object as a binary
     * resources in the database under the specified id.
     *
     * @param blobId a <code>String</code> value
     * @param blob a <code>NxqdBlobValue</code> value
     * @exception NxqdException if an error occurs
     */
    public void putBlob(final String blobId, final NxqdBlobValue blob) throws NxqdException {
        getNxqdManager().putBlob(getName(), blobId, blob.getBlob());
    }

    /**
     * The <code>getBlob</code> method retrieves the specified blob from the database.
     *
     * @param blobId a <code>String</code> value
     * @return a <code>NxqdBlobValue</code> value
     * @exception NxqdException if an error occurs
     */
    public NxqdBlobValue getBlob(final String blobId) throws NxqdException {
        return new NxqdBlobValue(getNxqdManager().getBlob(getName(), blobId));
    }

    /**
     * The <code>deleteBlob</code> method deletes the specified blob from this
     * container using the specified blobId. An exception is thrown if
     * an error occurred while deleting the document.
     *
     * @param blobId a <code>String</code> value
     * @exception NxqdException if an error occurs
     */
    public void deleteBlob(final String blobId) throws NxqdException {
        getNxqdManager().deleteBlob(getName(), blobId);
    }

    /**
     * The <code>listBlobs</code> method lists the id's of
     * the blobs managed by this container.
     *
     * @return a <code>List</code> value
     * @exception NxqdException if an error occurs
     */
    public List listBlobs() throws NxqdException {
        return getNxqdManager().listBlobs(getName());
    }

    /**
     * The <code>blobExists</code> method checks if
     * the specified blob exists. It will return
     * true if the blob exists, false otherwise.
     * A NxqdException is thrown if other errors occur.
     *
     * @param blobId a <code>String</code> value
     * @return a <code>boolean</code> value
     * @exception NxqdException if an error occurs
     */
    public boolean blobExists(String blobId) throws NxqdException {
        return getNxqdManager().blobExists(getName(), blobId);
    }
}
