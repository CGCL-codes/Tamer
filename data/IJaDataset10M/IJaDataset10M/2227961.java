package org.charvolant.tmsnet.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.charvolant.tmsnet.command.GetFileInfos;
import org.charvolant.tmsnet.command.GetFileInfosResponse;
import org.charvolant.tmsnet.command.ListFiles;
import org.charvolant.tmsnet.command.ListFilesResponse;
import org.charvolant.tmsnet.model.Directory;

/**
 * A transaction to fetch a directory.
 * <p>
 * This transaction both gets the file list and any additional recording information that is available.
 *
 * @author Doug Palmer &lt;doug@charvolant.org&gt;
 *
 */
@XmlRootElement()
@XmlAccessorType(XmlAccessType.NONE)
public class FetchDirectoryTransaction extends Transaction<Transactable> {

    /** The directory to fetch */
    private Directory directory;

    /**
   * Construct an empty fetch directory transaction
   *
   */
    public FetchDirectoryTransaction() {
        super();
    }

    /**
   * Construct a transaction for a specific directory
   * 
   * @param directory The directory
   */
    public FetchDirectoryTransaction(Directory directory) {
        super();
        this.directory = directory;
    }

    /**
   * Start the ball rolling by fetching the directory
   *
   * @see org.charvolant.tmsnet.client.Transaction#onExecute()
   */
    @Override
    protected void onExecute() {
        super.onExecute();
        this.queue(new ListFiles(this.directory.getPath()));
    }

    /**
   * Respond to a list of files
   * 
   * @param response The response
   */
    protected void onListFilesResponse(ListFilesResponse response) {
        this.client.getState().addFiles(this.directory, response.getFiles());
    }

    /**
   * Request the recording information from the server
   */
    protected void onFetchInfos() {
        this.queue(new GetFileInfos(this.directory.getPath()));
    }

    /**
   * Respond to a list of recording information
   * 
   * @param response The response
   */
    protected void onGetFileInfosResponse(GetFileInfosResponse response) {
        this.client.getState().addRecordings(this.directory, response.getFiles());
    }
}
