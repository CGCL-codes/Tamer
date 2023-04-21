package org.amlfilter.service;

import java.io.File;
import java.io.IOException;
import org.amlfilter.dao.DAOSuspectFileProcessingStatusInterface;
import org.amlfilter.model.SuspectFileProcessingStatus;
import org.amlfilter.service.GenericService;
import org.amlfilter.util.GeneralUtils;
import org.amlfilter.util.UnZip;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

public class SuspectsLoaderService extends GenericService implements SuspectsLoaderServiceInterface {

    public static final String PROCESSING_STATUS_NOK = "NOK";

    public static final String PROCESSING_STATUS_DOWNLOADING = "DOWNLOADING";

    public static final String PROCESSING_STATUS_NOT_DOWNLOAD = "NOT_DOWNLOAD";

    public static final String PROCESSING_STATUS_DOWNLOADED = "DOWNLOADED";

    public static final String PROCESSING_STATUS_PARSING = "PARSING";

    public static final String PROCESSING_STATUS_PARSED = "PARSED";

    public static final String PROCESSING_STATUS_LOADING = "LOADING";

    public static final String PROCESSING_STATUS_LOADED = "LOADED";

    public static final String PROCESSING_STATUS_PROCESSING = "PROCESSING";

    public static final String PROCESSING_STATUS_PROCESSED = "PROCESSED";

    public static final String PROCESSING_STATUS_QUARANTINED = "QUARANTINED";

    private DAOSuspectFileProcessingStatusInterface mDAOSuspectFileProcessingStatus;

    private String mListDirectory;

    /**
     * Get the suspect file processing status DAO
     * @return Th suspect file processing status DAO
     */
    public DAOSuspectFileProcessingStatusInterface getDAOSuspectFileProcessingStatus() {
        return mDAOSuspectFileProcessingStatus;
    }

    /**
     * Set the suspect file processing status DAO
     * @param pDAOSuspectFileProcessingStatus The suspect file processing status DAO
     */
    public void setDAOSuspectFileProcessingStatus(DAOSuspectFileProcessingStatusInterface pDAOSuspectFileProcessingStatus) {
        mDAOSuspectFileProcessingStatus = pDAOSuspectFileProcessingStatus;
    }

    /**
     * Get the list directory
     * @return The list directory
     */
    public String getListDirectory() {
        return mListDirectory;
    }

    /**
     * Set the list directory
     * @param pListDirectory The list directory
     */
    public void setListDirectory(String pListDirectory) {
        mListDirectory = pListDirectory;
    }

    /**
     * Get the list download directory
     * @return The list download directory
     */
    public String getListDownloadedDirectory() {
        return mListDirectory + "/" + PROCESSING_STATUS_DOWNLOADED.toLowerCase();
    }

    /**
     * Get the list processed directory
     * @return The list processed directory
     */
    public String getListProcessedDirectory() {
        return mListDirectory + "/" + PROCESSING_STATUS_PROCESSED.toLowerCase();
    }

    /**
     * Get the list quarantined directory
     * @return The list quarantined directory
     */
    public String getListQuarantinedDirectory() {
        return mListDirectory + "/" + PROCESSING_STATUS_QUARANTINED.toLowerCase();
    }

    /**
     * Uncompress and save the zipped list file
     * @param pListZippedFile The list zipped file
     * @param pDestinationDirectory The destination directory
     * @param pFileTimeStamp The file time stamp
     * @return The file path with timestamp
     * @throws IOException
     */
    protected File uncompressAndSaveFile(String pListZippedFile, String pDestinationDirectory, long pFileTimeStamp) throws IOException {
        String methodSignature = "File uncompressAndSaveFile(String,String,long): ";
        File outputFile = UnZip.uncompressFile(pListZippedFile, pDestinationDirectory);
        String timeQualifiedProcessingFileName = pDestinationDirectory + "/" + pFileTimeStamp + "_" + outputFile.getName();
        outputFile.renameTo(new File(timeQualifiedProcessingFileName));
        if (isLoggingInfo()) {
            logInfo(methodSignature + " Ucompressed file: " + timeQualifiedProcessingFileName);
        }
        return outputFile;
    }

    /**
     * Move the file based on the state (e.g incoming -> processed or quarantined)
     * @param pSourceFilePath The source file path
     * @param pDestinationDirectoryPath The destination directory path
     */
    public void moveFileBasedOnState(String pSourceFilePath, String pDestinationDirectoryPath) throws IOException {
        final String methodSignature = "void moveFileBasedOnState(String,String): ";
        if (isLoggingInfo()) {
            logInfo(methodSignature + "Source file path: " + pSourceFilePath);
            logInfo(methodSignature + "Destination directory path: " + pDestinationDirectoryPath);
        }
        File sourceFile = new File(pSourceFilePath);
        File destinationDirectory = new File(pDestinationDirectoryPath);
        if (!destinationDirectory.exists()) {
            if (isLoggingInfo()) {
                logInfo(methodSignature + "Destination directory path does not exist - creating directory path: " + pDestinationDirectoryPath);
            }
            FileUtils.forceMkdir(destinationDirectory);
        }
        sourceFile.renameTo(new File(pDestinationDirectoryPath + "/" + sourceFile.getName()));
    }

    /**
     * Move the processing state to the next state
     * TODO: THe flow should never allow reverting back to previous states
     * @param pSuspectFileProcessingStatus The suspect file processing status
     * @param pState The state (e.g INCOMING, PROCESSED, QUARANTINED)
     * @param pErrorCode An error code associated with the operation
     * @param pDescription The description
     */
    public void changeProcessingStatusStateInDB(SuspectFileProcessingStatus pSuspectFileProcessingStatus, String pState, String pErrorCode, String pDescription) {
        final String methodSignature = "void moveProcessingStatusState(SuspectFileProcessingStatus,String,String,String):  ";
        try {
            pSuspectFileProcessingStatus.setProcessingStatus(pState);
            pSuspectFileProcessingStatus.setRecordLastModifiedDate(new Long(System.currentTimeMillis()));
            pSuspectFileProcessingStatus.setErrorCode(pErrorCode);
            pSuspectFileProcessingStatus.setDescription(pSuspectFileProcessingStatus.getDescription() + pDescription);
            getDAOSuspectFileProcessingStatus().updateSuspectFileProcessingStatus(pSuspectFileProcessingStatus);
            if (isLoggingInfo()) {
                logInfo(methodSignature + "Updated suspect file processing status: " + pSuspectFileProcessingStatus);
            }
        } catch (Exception e) {
            String errorMessage = GeneralUtils.getStackTraceAsString(e, 20);
            if (isLoggingError()) {
                logError(methodSignature + errorMessage);
            }
        } finally {
        }
    }

    /**
     * Move the processing status state which essentially
     * + Changes the processing state in the DB
     * + Moves the file to the appropriate location
     * @param pSuspectFileProcessingStatus The suspect file processing status
     * @param pState The state (e.g INCOMING, PROCESSED, QUARANTINED)
     * @param pErrorCode An error code associated with the operation
     * @param pDescription The description
     * @throws IOException
     */
    public void moveProcessingState(SuspectFileProcessingStatus pSuspectFileProcessingStatus, String pState, String pErrorCode, String pDescription) throws IOException {
        String previousState = pSuspectFileProcessingStatus.getProcessingStatus();
        changeProcessingStatusStateInDB(pSuspectFileProcessingStatus, pState, pErrorCode, pDescription);
        String statusDirectory = null;
        if (pState.equals(SuspectsLoaderService.PROCESSING_STATUS_LOADED)) {
            statusDirectory = getListProcessedDirectory();
        } else if (pState.equals(SuspectsLoaderService.PROCESSING_STATUS_QUARANTINED)) {
            statusDirectory = getListQuarantinedDirectory();
        } else {
            throw new IllegalArgumentException("The only valid states to use this method are: " + SuspectsLoaderService.PROCESSING_STATUS_LOADED + ", " + SuspectsLoaderService.PROCESSING_STATUS_QUARANTINED);
        }
        try {
            moveFileBasedOnState(pSuspectFileProcessingStatus.getProcessingFilePath(), statusDirectory);
        } catch (Exception e) {
            pState = previousState;
            changeProcessingStatusStateInDB(pSuspectFileProcessingStatus, pState, pErrorCode, pDescription);
        }
    }

    /**
     * Move file into incoming directory
     * @param pListFile The list file
     * @param pIncomingDirectoryPath The incoming directory path
     * @return The suspect files composite
     * @throws IOException
     */
    protected SuspectFilesComposite moveFileIntoIncomingDirectory(File pListFile, String pIncomingDirectoryPath) throws IOException {
        String methodSignature = "void dumpFileIntoIncomingDirectory(File,String): ";
        long fileTimestamp = System.nanoTime();
        File incomingDirectory = new File(pIncomingDirectoryPath);
        if (!incomingDirectory.exists()) {
            FileUtils.forceMkdir(incomingDirectory);
        }
        FileUtils.copyFileToDirectory(pListFile, incomingDirectory);
        FileUtils.forceDelete(pListFile);
        String fileName = pListFile.getName();
        String incomingQualifiedFilePath = pIncomingDirectoryPath + "/" + fileName;
        SuspectFilesComposite sfc = new SuspectFilesComposite();
        String timeQualifiedProcessingFileName = null;
        sfc.setIncomingFileName(fileName);
        File incomingQualifiedFile = new File(incomingQualifiedFilePath);
        if (incomingQualifiedFilePath.toUpperCase().endsWith(".ZIP")) {
            File actualQualifiedFile = uncompressAndSaveFile(incomingQualifiedFilePath, pIncomingDirectoryPath, fileTimestamp);
            FileUtils.forceDelete(incomingQualifiedFile);
            timeQualifiedProcessingFileName = pIncomingDirectoryPath + "/" + fileTimestamp + "_" + actualQualifiedFile.getName();
            if (isLoggingInfo()) {
                logInfo(methodSignature + ": Incoming Uncompressed suspect file final name: " + timeQualifiedProcessingFileName);
            }
            sfc.setProcessingFilePath(timeQualifiedProcessingFileName);
            return sfc;
        }
        timeQualifiedProcessingFileName = pIncomingDirectoryPath + "/" + fileTimestamp + "_" + fileName;
        if (isLoggingInfo()) {
            logInfo(methodSignature + ": Incoming suspect file final name: " + timeQualifiedProcessingFileName);
        }
        sfc.setProcessingFilePath(timeQualifiedProcessingFileName);
        incomingQualifiedFile.renameTo(new File(timeQualifiedProcessingFileName));
        return sfc;
    }

    /**
     * Dump the mutltipart file into the incoming directory
     * @param pSuspectFile
     * @param pIncomingDirectoryPath
     * @return
     * @throws IOException
     */
    public SuspectFilesComposite dumpFileIntoIncomingDirectory(MultipartFile pSuspectFile, String pIncomingDirectoryPath) throws IOException {
        String methodSignature = "void dumpFileIntoIncomingDirectory(MultipartFile): ";
        long fileTimestamp = System.nanoTime();
        File incomingDirectory = new File(pIncomingDirectoryPath);
        if (!incomingDirectory.exists()) {
            FileUtils.forceMkdir(incomingDirectory);
        }
        String originalFileName = pSuspectFile.getOriginalFilename();
        String incomingQualifiedFilePath = pIncomingDirectoryPath + "/" + originalFileName;
        byte[] fileBytes = pSuspectFile.getBytes();
        if (isLoggingInfo()) {
            logInfo(methodSignature + ": Incoming suspect file path: " + incomingQualifiedFilePath);
            logInfo(methodSignature + ": Incoming suspect file number of bytes: " + pSuspectFile.getSize());
        }
        FileUtils.writeByteArrayToFile(new File(incomingQualifiedFilePath), fileBytes);
        SuspectFilesComposite sfc = new SuspectFilesComposite();
        String timeQualifiedProcessingFileName = null;
        sfc.setIncomingFileName(originalFileName);
        File incomingQualifiedFile = new File(incomingQualifiedFilePath);
        if (incomingQualifiedFilePath.toUpperCase().endsWith(".ZIP")) {
            File actualQualifiedFile = uncompressAndSaveFile(incomingQualifiedFilePath, pIncomingDirectoryPath, fileTimestamp);
            FileUtils.forceDelete(incomingQualifiedFile);
            timeQualifiedProcessingFileName = pIncomingDirectoryPath + "/" + fileTimestamp + "_" + actualQualifiedFile.getName();
            logInfo(methodSignature + ": Incoming Uncompressed suspect file final name: " + timeQualifiedProcessingFileName);
            sfc.setProcessingFilePath(timeQualifiedProcessingFileName);
            return sfc;
        }
        timeQualifiedProcessingFileName = pIncomingDirectoryPath + "/" + fileTimestamp + "_" + pSuspectFile.getOriginalFilename();
        logInfo(methodSignature + ": Incoming suspect file final name: " + timeQualifiedProcessingFileName);
        sfc.setProcessingFilePath(timeQualifiedProcessingFileName);
        incomingQualifiedFile.renameTo(new File(timeQualifiedProcessingFileName));
        return sfc;
    }

    /**
     * Create a suspect file processing status object
     * @return The suspect file processing status object
     */
    public SuspectFileProcessingStatus createSuspectFileProcessingStatus(String pProcessingFilePath, String pIncomingFileName, String pLoadType, String pFeedType, String pProcessingStatus, String pListName, String pDescription, String pErrorCode, long pRecordCreationDate, long pRecordLastModifiedDate) {
        SuspectFileProcessingStatus suspectFileProcessingStatus = new SuspectFileProcessingStatus();
        suspectFileProcessingStatus.setProcessingFilePath(pProcessingFilePath);
        suspectFileProcessingStatus.setIncomingFileName(pIncomingFileName);
        suspectFileProcessingStatus.setLoadType(pLoadType);
        suspectFileProcessingStatus.setFeedType(pFeedType);
        suspectFileProcessingStatus.setProcessingStatus(pProcessingStatus);
        suspectFileProcessingStatus.setRecordCreationDate(pRecordCreationDate);
        suspectFileProcessingStatus.setRecordLastModifiedDate(pRecordLastModifiedDate);
        suspectFileProcessingStatus.setListName(pListName);
        suspectFileProcessingStatus.setDescription(pDescription);
        suspectFileProcessingStatus.setErrorCode(pErrorCode);
        return suspectFileProcessingStatus;
    }

    public SuspectFileProcessingStatus persistSuspectFileDBRecord(String pProcessingFilePath, String pIncomingFileName, String pLoadType, String pFeedType, String pProcessingStatus, String pListName, String pErrorCode, String pDescription) {
        final String methodSignature = "SuspectFileProcessingStatus persistSuspectFileDBRecord(String,String,String,String,String,String,String): ";
        SuspectFileProcessingStatus suspectFileProcessingStatus = null;
        suspectFileProcessingStatus = createSuspectFileProcessingStatus(pProcessingFilePath, pIncomingFileName, pLoadType, pFeedType, pProcessingStatus, pListName, pDescription, pErrorCode, System.currentTimeMillis(), System.currentTimeMillis());
        logInfo(methodSignature + suspectFileProcessingStatus);
        getDAOSuspectFileProcessingStatus().storeSuspectFileProcessingStatus(suspectFileProcessingStatus);
        return suspectFileProcessingStatus;
    }

    /**
     * Store file into list incoming directory
     * @param pListFile The list file
     * @param pListName The list name
     * @param pLoadType The load type
     * @return The load type
     * @throws Exception
     */
    public synchronized SuspectFileProcessingStatus storeFileIntoListIncomingDir(File pListFile, String pLoadType, String pFeedType, String pListName, String pErrorCode, String pDescription) throws Exception {
        String methodSignature = "SuspectFileProcessingStatus storeFileIntoPublicListIncomingDir(File,String,String,String,String,String): ";
        SuspectFilesComposite sfc = moveFileIntoIncomingDirectory(pListFile, getListDownloadedDirectory());
        SuspectFileProcessingStatus sfps = getDAOSuspectFileProcessingStatus().getSuspectFileProcessingStatusByProcessingFilePath(sfc.getProcessingFilePath());
        if (null == sfps) {
            return persistSuspectFileDBRecord(sfc.getProcessingFilePath(), sfc.getIncomingFileName(), pLoadType, pFeedType, PROCESSING_STATUS_DOWNLOADED, pListName, pErrorCode, pDescription);
        }
        getDAOSuspectFileProcessingStatus().deleteSuspectFileProcessingStatus(sfps);
        return persistSuspectFileDBRecord(sfc.getProcessingFilePath(), sfc.getIncomingFileName(), pLoadType, pFeedType, PROCESSING_STATUS_DOWNLOADED, pListName, pErrorCode, pDescription);
    }
}
