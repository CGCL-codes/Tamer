package org.icenigrid.gridsam.core.plugin.transfer.protocol;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.RandomAccessContent;
import org.apache.commons.vfs.provider.AbstractFileObject;
import org.apache.commons.vfs.util.RandomAccessMode;
import org.icenigrid.gridsam.core.plugin.transfer.exception.FileJobFailException;
import org.icenigrid.gridsam.core.plugin.transfer.exception.FileJobRetryException;
import org.icenigrid.gridsam.core.plugin.transfer.persistence.FileJobInfo;
import org.icenigrid.gridsam.core.plugin.transfer.util.DateTypeConvert;
import org.icenigrid.gridsam.core.plugin.transfer.util.FileJobConstants;

/**
 * <strong>Purpose:</strong><br>
 * this Class implements the Interface <code>TransferAgent</code>. It can
 * support the transmissions through protocols http and sftp. All these
 * transmissions are based on the common-vfs
 * <p>
 * </p>
 * <strong>Partly Support the JSDL-<CreationFlag>APPEND</CreationFlag></strong>
 * 
 * @author Haojie Zhou create : 2008-9-15
 */
public class SFTPAndHTTPTransferAgent extends AbstractTransferAgent implements TransferAgent {

    /**
	 * log of the SFTPTransferAgent Class .
	 */
    private static final Log sLog = LogFactory.getLog(SFTPAndHTTPTransferAgent.class);

    /**
	 * file job Info
	 */
    private FileJobInfo fileJobInfo;

    /**
	 * FileObject of the source file and the destination file .
	 */
    private FileObject xSourceFileObject, xTargetFileObject;

    /**
	 * whether the stream is closed .
	 */
    private boolean disposeEvent;

    public SFTPAndHTTPTransferAgent(FileJobInfo fileJobInfo) {
        this.fileJobInfo = fileJobInfo;
    }

    /**
	 * Transfer through SFTP or HTTP
	 * 
	 * @param isApend
	 *            if the isApend is true, Download method support retry the file
	 *            transfer from the last failure point. the Upload method do not
	 *            support retry the file transfer from the last failure point
	 * @see org.icenigrid.gridsam.core.plugin.transfer.protocol.TransferAgent#transfer()
	 */
    public void transfer(boolean isAppend) throws FileJobRetryException, FileJobFailException {
        disposeEvent = false;
        if (FileJobConstants.FILE_ACTION_STAGEOUT.equalsIgnoreCase(fileJobInfo.getAction())) {
            if (isAppend) {
                sLog.warn("SFTPTransferAgent do not support append mode while uploading file");
            }
            upload();
        } else if (FileJobConstants.FILE_ACTION_STAGEIN.equalsIgnoreCase(fileJobInfo.getAction())) {
            download(isAppend);
        } else {
            throw new FileJobFailException("wrong operation");
        }
        sLog.debug(fileJobInfo.getJobName() + "  " + fileJobInfo.getAction() + " signal : finished" + "	" + DateTypeConvert.getMilliSecondString(System.currentTimeMillis()));
    }

    /**
	 * Download method support retry the file transfer from the last failure
	 * point.
	 * 
	 * @param isApend
	 * @throws FileJobRetryException
	 */
    private void download(boolean isApend) throws FileJobRetryException {
        long transferredLength = fileJobInfo.getTransferredLength();
        String destURL = fileJobInfo.getDestURL();
        String sourceURL = fileJobInfo.getSourceURL();
        String jobName = fileJobInfo.getJobName();
        RandomAccessFile fo = null;
        InputStream is = null;
        long seekSize = 0;
        try {
            AbstractFileObject fileObject = (AbstractFileObject) getFileObject(sourceURL);
            try {
                RandomAccessContent accessContent = fileObject.getRandomAccessContent(RandomAccessMode.READ);
                if (isApend) {
                    accessContent.seek(transferredLength);
                    sLog.debug("seek length : " + transferredLength);
                } else {
                    accessContent.seek(0);
                }
                is = accessContent.getInputStream();
                sLog.debug("download file using reliable mode");
            } catch (IOException e) {
                is = fileObject.getInputStream();
                sLog.debug("download file using retry mode");
            }
            sLog.debug(fileJobInfo.getJobName() + "  " + fileJobInfo.getAction() + " signal : end init is" + "	" + DateTypeConvert.getMilliSecondString(System.currentTimeMillis()));
            fo = new RandomAccessFile(destURL, "rw");
            if (this.fileJobInfo.isAppend()) {
                seekSize = fileJobInfo.getOriginalLength() + transferredLength;
            } else {
                seekSize = transferredLength;
            }
            fo.seek(seekSize);
            sLog.debug(fileJobInfo.getJobName() + "  " + fileJobInfo.getAction() + " signal : end init os" + "	" + DateTypeConvert.getMilliSecondString(System.currentTimeMillis()));
            byte[] buffer = new byte[BUFFER_LENGTH];
            int transferRound = 0;
            int len = 0;
            while ((len = is.read(buffer)) > 0 && disposeEvent == false) {
                fo.write(buffer, 0, len);
                transferredLength += len;
                transferRound += len;
                if (transferRound > this.STORAGE_SEGMENT_LENGTH) {
                    changeTransferredLengthInDataBase(jobName, transferredLength);
                    transferRound = 0;
                    sLog.debug(" transferredLength : " + getFileSizeString(transferredLength) + " " + DateTypeConvert.getMilliSecondString(System.currentTimeMillis()));
                }
            }
            changeTransferredLengthInDataBase(jobName, transferredLength);
        } catch (FileNotFoundException e1) {
            sLog.error("DestURL '" + destURL + "' is invalid , Throw Exception: " + e1.getMessage());
            throw new FileJobRetryException("DestURL '" + destURL + "' is invalid , Throw Exception: " + e1.getMessage());
        } catch (IOException e2) {
            sLog.error("Faile to copy file " + sourceURL + "->" + destURL + ", Throw Exception : " + e2.getMessage());
            throw new FileJobRetryException("Faile to copy file " + sourceURL + "->" + destURL + ", Throw Exception : " + e2.getMessage());
        } catch (Exception e) {
            sLog.error("Faile to copy file " + sourceURL + "->" + destURL + ", Throw Exception : " + e.getMessage());
            throw new FileJobRetryException("Faile to copy file " + sourceURL + "->" + destURL + ", Throw Exception : " + e.getMessage());
        } finally {
            if (fo != null) {
                try {
                    fo.close();
                } catch (IOException e) {
                    sLog.error("Failed to close the OutputStream" + e.getMessage());
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    sLog.error("Failed to close the InputStream" + e.getMessage());
                }
            }
        }
    }

    /**
	 * Upload method do not support retry the file transfer from the last
	 * failure point.
	 * 
	 * @throws FileJobRetryException
	 */
    private void upload() throws FileJobRetryException {
        String destURL = fileJobInfo.getDestURL();
        String sourceURL = fileJobInfo.getSourceURL();
        String jobName = fileJobInfo.getJobName();
        OutputStream os = null;
        InputStream is = null;
        long seekSize = 0;
        try {
            is = new FileInputStream(new File(sourceURL));
            AbstractFileObject fileObject = (AbstractFileObject) getFileObject(destURL);
            os = fileObject.getOutputStream();
            byte[] buffer = new byte[BUFFER_LENGTH];
            int transferredLength = 0;
            int len = -1;
            while ((len = is.read(buffer)) > 0 && disposeEvent == false) {
                os.write(buffer, 0, len);
                transferredLength = transferredLength + len;
            }
            os.flush();
            changeTransferredLengthInDataBase(fileJobInfo.getJobName(), transferredLength);
        } catch (FileNotFoundException e1) {
            sLog.error("DestURL '" + destURL + "' is invalid , Throw Exception: " + e1.getMessage());
            throw new FileJobRetryException("DestURL '" + destURL + "' is invalid , Throw Exception: " + e1.getMessage());
        } catch (IOException e2) {
            sLog.error("Faile to copy file " + sourceURL + "->" + destURL + ", Throw Exception : " + e2.getMessage());
            throw new FileJobRetryException("Faile to copy file " + sourceURL + "->" + destURL + ", Throw Exception : " + e2.getMessage());
        } catch (Exception e) {
            sLog.error("Faile to copy file " + sourceURL + "->" + destURL + ", Throw Exception : " + e.getMessage());
            throw new FileJobRetryException("Faile to copy file " + sourceURL + "->" + destURL + ", Throw Exception : " + e.getMessage());
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    sLog.error("Failed to close the OutputStream" + e.getMessage());
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    sLog.error("Failed to close the InputStream" + e.getMessage());
                }
            }
        }
    }

    /**
	 * stop SFTP or HTTP transfer
	 * 
	 * @see org.icenigrid.gridsam.core.plugin.transfer.protocol.TransferAgent#disposeAgent()
	 */
    public void disposeAgent() {
        sLog.debug("Stop transfer file ");
        disposeEvent = true;
    }
}
