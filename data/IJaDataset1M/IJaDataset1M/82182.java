package eg.nileu.cis.nilestore.immutable.uploader.port;

import se.sics.kompics.Event;
import eg.nileu.cis.nilestore.common.StatusMsg;
import eg.nileu.cis.nilestore.interfaces.file.UploadResults;

/**
 * The Class UploadingDone.
 * 
 * @author Mahmoud Ismail <mahmoudahmedismail@gmail.com>
 */
public class UploadingDone extends Event {

    /** The SI. */
    private final String SI;

    /** The status. */
    private final StatusMsg status;

    /** The upload results. */
    private final UploadResults uploadResults;

    /**
	 * Instantiates a new uploading done.
	 * 
	 * @param SI
	 *            the sI
	 * @param status
	 *            the status
	 * @param uploadResults
	 *            the upload results
	 */
    public UploadingDone(String SI, StatusMsg status, UploadResults uploadResults) {
        this.SI = SI;
        this.status = status;
        this.uploadResults = uploadResults;
    }

    /**
	 * Gets the storage index.
	 * 
	 * @return the storage index
	 */
    public String getStorageIndex() {
        return SI;
    }

    /**
	 * Gets the status.
	 * 
	 * @return the status
	 */
    public StatusMsg getStatus() {
        return status;
    }

    /**
	 * Gets the upload results.
	 * 
	 * @return the upload results
	 */
    public UploadResults getUploadResults() {
        return uploadResults;
    }
}
