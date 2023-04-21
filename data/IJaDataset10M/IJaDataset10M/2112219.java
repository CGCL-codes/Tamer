package wjhk.jupload2.test;

import wjhk.jupload2.exception.JUploadException;
import wjhk.jupload2.policies.UploadPolicy;
import wjhk.jupload2.upload.DefaultFileUploadThread;
import wjhk.jupload2.upload.FileUploadManagerThread;
import wjhk.jupload2.upload.FileUploadThread;
import wjhk.jupload2.upload.UploadFileData;

/**
 * This class allows easy construction of non-active instances of
 * FileUploadThread. It is used to execute unit tests on
 * {@link FileUploadManagerThread}.<BR>
 * This instance simulates the start of upload for the first file: for each
 * file, it simulates the fact than one bytes was sent to the server, and then
 * wait 1 seconds before finishing the upload.
 * 
 * @author etienne_sf
 */
public class FileUploadThreadStopDuringUpload extends Thread implements FileUploadThread {

    UploadPolicy uploadPolicy = null;

    FileUploadManagerThread fileUploadManagerThread = null;

    UploadFileData[] filesToUpload = null;

    /**
     * @param uploadPolicy
     */
    public FileUploadThreadStopDuringUpload(UploadPolicy uploadPolicy) {
        this.uploadPolicy = uploadPolicy;
    }

    /**
     * This method loops on the {@link FileUploadManagerThread#getNextPacket()}
     * method, until a set of files is ready. Then, it calls the doUpload()
     * method, to send these files to the server.
     */
    @Override
    public final void run() {
        try {
            while (!this.fileUploadManagerThread.isUploadFinished()) {
                this.filesToUpload = this.fileUploadManagerThread.getNextPacket();
                if (this.filesToUpload != null) {
                    for (int i = 0; i < this.filesToUpload.length; i++) {
                        this.fileUploadManagerThread.nbBytesUploaded(1);
                        try {
                            sleep(DefaultFileUploadThread.TIME_BEFORE_CHECKING_NEXT_PACKET + 500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        this.fileUploadManagerThread.nbBytesUploaded(this.filesToUpload[i].getFileLength() - 1);
                        this.fileUploadManagerThread.anotherFileHasBeenSent(this.filesToUpload[i]);
                    }
                    this.fileUploadManagerThread.currentRequestIsFinished(this.filesToUpload);
                } else {
                    try {
                        sleep(DefaultFileUploadThread.TIME_BEFORE_CHECKING_NEXT_PACKET);
                    } catch (InterruptedException e) {
                    }
                }
            }
        } catch (JUploadException e) {
            this.fileUploadManagerThread.setUploadException(e);
        }
    }

    /** {@inheritDoc} */
    public void close() {
    }

    /** {@inheritDoc} */
    public String getResponseMsg() {
        return this.uploadPolicy.getStringUploadSuccess();
    }

    /** {@inheritDoc} */
    public void setFileUploadThreadManager(FileUploadManagerThread fileUploadManagerThread) {
        this.fileUploadManagerThread = fileUploadManagerThread;
    }
}
