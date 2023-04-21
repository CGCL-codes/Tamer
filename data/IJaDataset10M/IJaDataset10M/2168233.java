package uk.gov.dti.og.fox.filetransfer;

import eu.medsea.mimeutil.MimeType;
import eu.medsea.mimeutil.MimeUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.FilterInputStream;
import java.util.Collection;
import java.util.Iterator;
import oracle.sql.BLOB;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.MultipartStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import uk.gov.dti.og.fox.FileUploadType;
import uk.gov.dti.og.fox.FoxRequest;
import uk.gov.dti.og.fox.UCon;
import uk.gov.dti.og.fox.XFUtil;
import uk.gov.dti.og.fox.dom.DOM;
import uk.gov.dti.og.fox.ex.ExInternal;
import uk.gov.dti.og.fox.ex.ExUpload;
import uk.gov.dti.og.fox.io.NonBlockingInputStream;
import uk.gov.dti.og.fox.queue.ServiceQueueHandler;
import uk.gov.dti.og.fox.queue.WorkItem;
import uk.gov.dti.og.fox.track.Track;

public class UploadWorkItem extends WorkItem {

    private FoxRequest mFoxRequest;

    private UploadInfo mUploadInfo;

    private static final int BYTE_READ_QUANTITY = 1024 * 4;

    private static final int MAX_SUBSEQUENT_BUFFER_READS = 30;

    private static final int READ_TIMEOUT_MS = 1000 * 60 * 5;

    private static final int READING_FORM_DATA = 0;

    private static final int READING_FILE_DATA = 1;

    private static final int COMPLETE = 2;

    private static final int FAILED = 3;

    private int mStatus = READING_FORM_DATA;

    private byte[] mBuffer;

    private UCon mUCon;

    private BLOB mUploadDestinationBLOB;

    private OutputStream mBlobOutputStream;

    private InputStream mItemInputStream;

    private NonBlockingInputStream mItemNonBlockingInputStream;

    private ServletFileUpload mUpload;

    private FileItemIterator mItemIter;

    private FileItemStream mCurrentItem;

    private int mItemTotalBytesRead = 0;

    private int mFileItemsRead = 0;

    private long mLastReadTime;

    private long[] mReadLoopIterations = new long[MAX_SUBSEQUENT_BUFFER_READS + 1];

    private VirusScanner[] mVirusScannerArray;

    public Throwable mErrorException;

    public UploadWorkItem(FoxRequest pFoxRequest, UploadInfo pUploadInfo) {
        super(ServiceQueueHandler.UPLOAD_WORKITEM_TYPE, "UploadWorkItem FileId=" + pUploadInfo.getFileId() + " UploadWindowId=" + pUploadInfo.mUploadWindowId);
        mFoxRequest = pFoxRequest;
        mUploadInfo = pUploadInfo;
        setAttribute("MultipartContentLength", new Integer(pFoxRequest.getHttpRequest().getContentLength()));
        setAttribute("FileId", pUploadInfo.getFileId());
        setAttribute("UploadWindowId", pUploadInfo.mUploadWindowId);
        mUploadInfo.setHttpContentLength(pFoxRequest.getHttpRequest().getContentLength());
        mBuffer = new byte[BYTE_READ_QUANTITY];
    }

    public void readFormData() throws Throwable {
        try {
            if (mItemIter == null) throw new ExInternal("Failed to read data because the form item iterator was null.");
            if (!mItemIter.hasNext()) mStatus = COMPLETE;
            while (mItemIter.hasNext()) {
                mCurrentItem = mItemIter.next();
                mItemInputStream = mCurrentItem.openStream();
                if (mCurrentItem.isFormField()) {
                    String lParamName = mCurrentItem.getFieldName();
                    String lFieldValue;
                    if (lParamName.startsWith("cf_")) {
                        String lFieldName = lParamName.substring(3);
                        lFieldValue = Streams.asString(mItemInputStream);
                        mUploadInfo.setCaptureFieldValue(lFieldName, lFieldValue != null && lFieldValue.length() > 0 ? lFieldValue : "");
                    } else if (lParamName.equals("windowType")) {
                        mUploadInfo.mWindowType = Streams.asString(mItemInputStream);
                    } else lFieldValue = Streams.asString(mItemInputStream);
                    mStatus = mItemIter.hasNext() ? READING_FORM_DATA : COMPLETE;
                } else {
                    mItemNonBlockingInputStream = new NonBlockingInputStream(mItemInputStream, BYTE_READ_QUANTITY, MAX_SUBSEQUENT_BUFFER_READS);
                    String lFilename = mCurrentItem.getName();
                    mUploadInfo.setOriginalFileLocation(lFilename);
                    int lBeginningIndex = lFilename.lastIndexOf("\\");
                    if (lFilename != null && lBeginningIndex != -1) {
                        lFilename = lFilename.substring(lBeginningIndex + 1);
                    }
                    mUploadInfo.setFilename(lFilename);
                    String lContentType = mCurrentItem.getContentType();
                    mUploadInfo.setBrowserContentType(lContentType != null ? lContentType : "");
                    if (mFileItemsRead > 0) throw new ExInternal("Tried to stream multiple file items for single request.  This is currently not implemented.");
                    mFileItemsRead++;
                    mStatus = READING_FILE_DATA;
                    mLastReadTime = System.currentTimeMillis();
                    break;
                }
            }
        } catch (Throwable ex1) {
            throw ex1;
        }
    }

    private void readNextFileSegment() throws Throwable {
        int lItemBytesRead = 0;
        mStatus = READING_FILE_DATA;
        if (System.currentTimeMillis() - mLastReadTime > READ_TIMEOUT_MS) throw new ExUpload("Upload timed out. No data received on input channel for " + (READ_TIMEOUT_MS / 1000) + " seconds.");
        try {
            boolean lBreak = false;
            READ_LOOP: for (int i = 0; i < MAX_SUBSEQUENT_BUFFER_READS && lItemBytesRead != -1 && !lBreak; i++) {
                if ((lItemBytesRead = mItemNonBlockingInputStream.read(mBuffer)) != -1) {
                    if (mItemTotalBytesRead == 0 && (lItemBytesRead > 0 || lItemBytesRead == -1)) {
                        performContentCheck();
                    }
                    if (lItemBytesRead > 0) {
                        mLastReadTime = System.currentTimeMillis();
                    }
                    mItemTotalBytesRead += lItemBytesRead;
                    mBlobOutputStream.write(mBuffer, 0, lItemBytesRead);
                    for (int j = 0; j < mVirusScannerArray.length; j++) {
                        if (mVirusScannerArray[j].isError()) {
                            throw new ExInternal("Virus Scanner " + mVirusScannerArray[j].getName() + " threw exception: " + mVirusScannerArray[j].getErrorMessage() + ". Upload cannot complete without Virus Detection.");
                        }
                        try {
                            mVirusScannerArray[j].getOutputStream().write(mBuffer, 0, lItemBytesRead);
                        } catch (IOException ex) {
                            throw new ExInternal("Virus Scanner encountered an input problem. Upload cannot continue.", ex);
                        }
                    }
                    if (lItemBytesRead == 0) {
                        mReadLoopIterations[i]++;
                        lBreak = true;
                    }
                } else {
                    mUploadInfo.setFileSize(mItemTotalBytesRead);
                    mStatus = READING_FORM_DATA;
                    mBlobOutputStream.close();
                    mUploadInfo.setStatus(UploadInfo.STATUS_VIRUS_CHECK);
                    mUploadInfo.setStatusMsg("Virus scanning the uploaded file...");
                    for (int j = 0; j < mVirusScannerArray.length; j++) {
                        mVirusScannerArray[j].getOutputStream().close();
                        mVirusScannerArray[j].getCurrentThread().join(mVirusScannerArray[j].getTimeoutSecs() * 1000);
                        if (!mVirusScannerArray[j].isComplete()) {
                            throw new ExInternal("Virus Scanner " + mVirusScannerArray[j].getName() + " did not complete after waiting " + mVirusScannerArray[j].getTimeoutSecs() + " seconds.");
                        }
                    }
                    mReadLoopIterations[i]++;
                    lBreak = true;
                }
                for (int j = 0; j < mVirusScannerArray.length; j++) {
                    if (mVirusScannerArray[j].isVirusFound()) {
                        mUploadInfo.setStatus(UploadInfo.STATUS_VIRUS_CHECK_FAILED);
                        throw new ExUpload("Virus Detected in uploaded file: " + mVirusScannerArray[j].getScanResultString() + ". You cannot upload infected files.");
                    } else if (mVirusScannerArray[j].isError()) {
                        throw new ExInternal("Virus Detection threw exception: " + mVirusScannerArray[j].getErrorMessage() + ". Upload cannot complete without Virus Detection.");
                    }
                }
            }
            if (!lBreak) {
                mReadLoopIterations[MAX_SUBSEQUENT_BUFFER_READS]++;
            }
        } catch (IOException ioex) {
            throw new ExUpload("An unexpected problem was encountered receving your data. ", ioex);
        } catch (Throwable ex1) {
            throw ex1;
        }
    }

    private void performContentCheck() {
        String lContentCheckResult = mUploadInfo.getFileUploadType().validateContent(mUploadInfo, mBuffer);
        if (lContentCheckResult != FileUploadType.CONTENT_VALID) {
            mUploadInfo.setStatus(UploadInfo.STATUS_CONTENT_CHECK_FAILED);
            throw new ExUpload("Content check failed: " + lContentCheckResult + ". Please check the file conforms to the requirements stated on the upload page.");
        }
    }

    private boolean isUploadFailRequested() {
        return mUploadInfo.mFailUpload;
    }

    public void init() throws ExInternal {
        try {
            mUCon = (UCon) getAttribute("UCon");
            if (mUCon == null) throw new ExInternal("Failed to find a database conection when trying to stream to Blob.");
            mUpload = new ServletFileUpload();
            FiletransferProgressListener lFiletransferProgressListener = new FiletransferProgressListener();
            mUpload.setProgressListener(lFiletransferProgressListener);
            mUploadInfo.mProgressListener = lFiletransferProgressListener;
            mItemIter = mUpload.getItemIterator(mFoxRequest.getHttpRequest());
            readFormData();
        } catch (Throwable ex) {
            throw new ExInternal("Error encountered while trying to initialise a file upload work item.\nOriginal error: " + ex.getMessage());
        }
    }

    public void initBLOB(BLOB pBLOB) {
        try {
            mUploadDestinationBLOB = pBLOB;
            if (mUploadDestinationBLOB == null) throw new ExInternal("Failed to find a temporary Blob in work items attributes.  Unable to stream upload without Blob location to stream to.");
            mBlobOutputStream = mUploadDestinationBLOB.getBinaryOutputStream();
        } catch (Throwable ex) {
            throw new ExInternal("Error encountered while trying to initialise a file upload work item BLOB.\nOriginal error: " + ex.getMessage());
        }
    }

    public void execute() throws Throwable {
        if (isUploadFailRequested()) {
            if (mVirusScannerArray != null) {
                for (int i = 0; i < mVirusScannerArray.length; i++) {
                    try {
                        mVirusScannerArray[i].getOutputStream().close();
                        mVirusScannerArray[i].getInputStream().close();
                    } catch (IOException ignore) {
                    }
                }
            }
            throwForceFailException();
        }
        if (mVirusScannerArray == null) {
            initialiseVirusScanners();
        }
        if (mStatus == READING_FILE_DATA) readNextFileSegment(); else if (mStatus == READING_FORM_DATA) readFormData();
    }

    /**
   * Throws an exception with the relevant text if a force fail has ocurred.
   */
    private void throwForceFailException() throws ExUpload {
        if (UploadInfo.FORCE_FAIL_REASON_INTERRUPT.equals(mUploadInfo.getForceFailReason())) throw new ExUpload("Parent window could not be found. Please do not navigate away from the main page whilst the upload is in progress."); else if (UploadInfo.FORCE_FAIL_REASON_CANCEL.equals(mUploadInfo.getForceFailReason())) throw new ExUpload("Upload window closed.");
        throw new ExUpload("A fail was requested on the Upload Work Item");
    }

    private void initialiseVirusScanners() {
        mVirusScannerArray = mUploadInfo.getApp().createAllVirusScanners();
        for (int i = 0; i < mVirusScannerArray.length; i++) {
            PipedInputStream lInputStream = new PipedInputStream();
            OutputStream lOutputStream;
            try {
                lOutputStream = new PipedOutputStream(lInputStream);
            } catch (IOException e) {
                throw new ExInternal("Virus Scanner pipe construction: " + e);
            }
            mVirusScannerArray[i].setInputStream(lInputStream);
            mVirusScannerArray[i].setOutputStream(lOutputStream);
            new Thread(mVirusScannerArray[i], "VirusScanThread-" + i + "-" + Thread.currentThread().getName()).start();
        }
    }

    public boolean isComplete() {
        if (mStatus == COMPLETE) return true;
        return false;
    }

    public boolean isFailed() {
        if (mStatus == FAILED) return true;
        return false;
    }

    public void finaliseOnSuccess() {
        if (mItemNonBlockingInputStream != null) mItemNonBlockingInputStream.destroy();
    }

    public void finaliseOnError(Throwable pEx) {
        mStatus = FAILED;
        mErrorException = pEx;
        if (mUploadInfo != null) {
            if (mUploadInfo.getStatus() < UploadInfo.STATUS_FAILED) mUploadInfo.setStatus(UploadInfo.STATUS_FAILED);
            mUploadInfo.mSystemMsg = XFUtil.getJavaStackTraceInfo(pEx);
        }
        if (mItemNonBlockingInputStream != null) mItemNonBlockingInputStream.destroy();
    }

    public void writeReadIterationCountsToDOM(DOM pDOM) {
        DOM lDOM = pDOM.addElem("read-loop-iterations");
        for (int i = 0; i < MAX_SUBSEQUENT_BUFFER_READS + 1; i++) {
            lDOM.addElem("iteration").addElem("index", Integer.toString(i)).getParentOrNull().addElem("count", Long.toString(mReadLoopIterations[i]));
        }
    }
}
