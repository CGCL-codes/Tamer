package au.gov.naa.digipres.dpr.task.step;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import au.gov.naa.digipres.dpr.core.Constants;
import au.gov.naa.digipres.dpr.dao.DataAccessManager;
import au.gov.naa.digipres.dpr.dao.TransferJobDAO;
import au.gov.naa.digipres.dpr.model.job.JobStatus;
import au.gov.naa.digipres.dpr.model.transferjob.DataObject;
import au.gov.naa.digipres.dpr.model.transferjob.QFDataObjectProcessingRecord;
import au.gov.naa.digipres.dpr.model.transferjob.QFTransferJobProcessingRecord;
import au.gov.naa.digipres.dpr.model.transferjob.TransferJob;
import au.gov.naa.digipres.dpr.model.user.User;
import au.gov.naa.digipres.dpr.task.JobProcessingTask;
import au.gov.naa.digipres.dpr.task.step.ProcessingErrorHandler.ProcessingErrorAction;
import au.gov.naa.digipres.dpr.util.FileUtils;
import au.gov.naa.digipres.dpr.util.carrier.CarrierNotFoundException;
import au.gov.naa.digipres.dpr.util.carrier.DataDirectoryGenerator;
import au.gov.naa.digipres.dpr.util.virus.ScanInformation;
import au.gov.naa.digipres.dpr.util.virus.VirusScanner;
import au.gov.naa.digipres.dpr.util.virus.VirusScannerException;

/**
 * This class represents all the processing required to take a transfer job that has
 * completed the read manifest step and put it into quarantine. This requires the following:
 * <ul>
 * <li>Files are copied from the input media onto the export carrier device,</li>
 * <li>Files are scanned before copying,</li>
 * <li>The checksum of each data object is verified after copying,</li>
 * <li>The quarantine period is set</li>
 * <li>If the period is not of the default duration, then a reason for the short period is entered.</li>
 * </ul>
 * 
 * 
 * @author andy
 *
 */
public class PreQuarantineProcessingStep extends Step {

    public static final String STEP_NAME = "Pre-Quarantine Processing";

    private static final int MAX_ATTEMPTS_TO_GET_MEDIA_LOCATION = 1000;

    private String outputCarrierLocation;

    private String outputCarrierId;

    private String firstMediaLocation;

    private Date expectedDateOut;

    private String quarantinePeriodReason;

    private String scannerPortString;

    private int scannerPort;

    private VirusScanner scanner;

    private DataAccessManager dataAccessManager;

    private TransferJobDAO transferJobDAO;

    private QFTransferJobProcessingRecord qfRecord;

    private PreQuarantineProcessHandler preQuarantineProcessHandler;

    private File qfDataOnCarrierDir;

    private int processedMediaCount;

    private String currentMediaId;

    private String currentMediaLocation;

    private Map<String, List<String>> filesOnMediaNotManifested;

    private Map<String, List<String>> filesMissingFromManifest;

    private Set<String> processedFilenamesOnCurrentMedia;

    private boolean foundMatchOnMedia;

    private boolean changeOfMediaBeforeMediaID;

    private boolean ignoreMissingFiles;

    public PreQuarantineProcessingStep(User currentUser, JobProcessingTask task, QFTransferJobProcessingRecord qfRecord) {
        super(currentUser, task);
        this.qfRecord = qfRecord;
        filesOnMediaNotManifested = new HashMap<String, List<String>>();
        filesMissingFromManifest = new HashMap<String, List<String>>();
        processedFilenamesOnCurrentMedia = new HashSet<String>();
        ignoreMissingFiles = false;
        changeOfMediaBeforeMediaID = false;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, Constants.DEFAULT_QUARANTINE_PERIOD_IN_DAYS);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date defaultExpectedDateOut = calendar.getTime();
        if (qfRecord.getCarrierDeviceId() != null && qfRecord.getCarrierDeviceLocation() != null) {
            File checkDir = DataDirectoryGenerator.getBaseDataObjectDirectory(qfRecord.getCarrierDeviceLocation(), task.getJobEncapsulator());
            if (checkDir.exists() && checkDir.isDirectory()) {
                properties.put(StepProperties.OUTPUT_CARRIER_LOCATION_PROPERTY_NAME, qfRecord.getCarrierDeviceLocation());
                properties.put(StepProperties.OUTPUT_CARRIER_ID_PROPERTY_NAME, qfRecord.getCarrierDeviceId());
            }
        }
        properties.put(StepProperties.EXPECTED_DATE_OUT_PROPERTY_NAME, new SimpleDateFormat(Constants.DATE_FORMAT).format(defaultExpectedDateOut));
        dataAccessManager = task.getDPRClient().getDataAccessManager();
        transferJobDAO = task.getDPRClient().getDataAccessManager().getTransferJobDAO(task);
    }

    @Override
    protected void abort() {
        logger.fine("Beginning abort processing");
        dataAccessManager.rollbackTransaction();
        logger.fine("Cleaning carrier");
        TransferJob transferJob = task.getJobEncapsulator().getTransferJob();
        String carrierLocation = qfRecord.getCarrierDeviceLocation();
        if (carrierLocation != null) {
            File carrierDir = new File(carrierLocation);
            if (carrierDir.exists() && carrierDir.isDirectory() && carrierDir.canWrite()) {
                File qfDataDir = new File(carrierDir, transferJob.getTransferJobNumber().getPath() + File.separator + Constants.QF_DATA_DIR_NAME);
                if (qfDataDir.exists() && qfDataDir.isDirectory() && qfDataDir.canWrite()) {
                    FileUtils.deleteContentsOfDir(qfDataDir);
                }
            }
        }
        dataAccessManager.beginTransaction();
        qfRecord.setPreQuarantineProcessingStatus(Constants.UNSTARTED_STATE);
        qfRecord.setPreQuarantineProcessingDate(null);
        qfRecord.setPreQuarantineProcessingBy(null);
        qfRecord.setPreQuarantineVirusCheckDefinitionsVersion(null);
        qfRecord.setPreQuarantineVirusCheckDefinitionsCurrent(null);
        qfRecord.setPreQuarantineVirusCheckerName(null);
        qfRecord.setPreQuarantineVirusCheckerVersion(null);
        transferJobDAO.saveTransferJob(transferJob);
        dataAccessManager.commitTransaction();
    }

    @Override
    public void failStep() {
        dataAccessManager.commitTransaction();
        dataAccessManager.beginTransaction();
        qfRecord.setPreQuarantineProcessingStatus(Constants.FAILED_STATE);
        TransferJob transferJob = task.getJobEncapsulator().getTransferJob();
        transferJob.setJobStatus(JobStatus.QF_PRE_QUARANTINE_FAILED);
        stopProcessing();
        transferJobDAO.saveTransferJob(transferJob);
        dataAccessManager.commitTransaction();
    }

    /**
	 * @throws StepException 
	 * @see au.gov.naa.digipres.dpr.task.step.Step#doProcessing(au.gov.naa.digipres.dpr.task.step.ProcessingErrorHandler)
	 * 
	 * 
	 * On calling doProcessing(), the following outcomes may occur:
	 * <ul>
	 * <li>A property is not set; or multiple media are detected and the 
	 * {@link #preQuarantineProcessHandler} field has not been set, a {@link ProcessingException} will be
	 * thrown.</li>
	 * <li>Scanner information is unable to be retrieved, a {@link ProcessingException} will be thrown. </li>
	 * <li>Attempting to get the first media reveals that the location is inavlid and 
	 * the {@link #preQuarantineProcessHandler} field has not been set, a {@link ProcessingException} will be
	 * thrown.</li>
	 * <li>Attempting to get the media from the {@link #preQuarantineProcessHandler} results in 
	 * a {@link ProcessingException} (for instance the calling application can not find the media for some reason).</li>
	 * <li>{@link #scanDataObject(VirusScanner, String, QFDataObjectProcessingRecord, DataObject)} is called, and a virus is detected. If
	 * the {@link #preQuarantineProcessHandler} field has not been set, this will result in a {@link ProcessingException},
	 * if the calling application does not wish to continue then a {@link ProcessingException} will thrown, otherwise scanning
	 * will continue, and the {@link StepResults} will be updated to indicate an error has occured.</li>
	 * <li></li>
	 * <li></li>
	 * <li>Processing completes successfully, {@link StepResults} object returned.</li>
	 *</ul>
	 * 
	 */
    @Override
    public StepResults doProcessing(ProcessingErrorHandler processingErrorHandler) throws StepException {
        fireStepProcessingBeginningEvent(getDescription());
        logger.fine("Starting processing in " + this.getClass().getName());
        verifyStepState();
        StepResults results = new StepResults();
        TransferJob transferJob = task.getJobEncapsulator().getTransferJob();
        validateProperties(transferJob);
        dataAccessManager.beginTransaction();
        qfRecord.setPreQuarantineProcessingStatus(Constants.IN_PROGRESS_STATE);
        transferJobDAO.saveTransferJob(transferJob);
        dataAccessManager.commitTransaction();
        scanner = null;
        try {
            fireStatusChangeEvent("Connecting to virus scanner.");
            logger.finest("Getting scanner information");
            Properties scannerProperties;
            try {
                scanner = new VirusScanner(scannerPort);
                scannerProperties = scanner.getScannerProperties();
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Exception when attempting to connect to virus scanner (port: " + scannerPortString + ")", e);
                throw new ProcessingException("Exception when attempting to connect to virus scanner on port " + scannerPortString, e);
            }
            dataAccessManager.beginTransaction();
            saveScannerInformation(qfRecord, scannerProperties);
            int mediaCount = transferJobDAO.getMediaCount(transferJob);
            if (preQuarantineProcessHandler != null) {
                preQuarantineProcessHandler.setMediaCount(mediaCount);
            }
            currentMediaLocation = "";
            currentMediaId = "";
            processedMediaCount = 0;
            logger.fine("Retrieving data object QF record iterator.");
            fireStatusChangeEvent("Retrieving Data Object list.");
            Iterator<String> mediaIdIterator = transferJobDAO.getMediaIdsForForQFTransferJobProcessingRecord(qfRecord);
            fireItemProcessingBeginEvent(transferJob.getNumDataObjects() * 2);
            logger.finer("Begining processing data objects.");
            boolean processingCancelled = false;
            while (mediaIdIterator.hasNext()) {
                String mediaId = mediaIdIterator.next();
                Iterator<Object> iterator = transferJobDAO.getDataObjectQFRecordsByMediaId(qfRecord, mediaId);
                while (iterator.hasNext()) {
                    QFDataObjectProcessingRecord qfDORecord = (QFDataObjectProcessingRecord) iterator.next();
                    DataObject dataObject = qfDORecord.getDataObject();
                    logger.finest("Scanning data object: " + dataObject.getFileName() + " on media: " + dataObject.getMediaId());
                    fireItemProcessEvent("Scanning: " + dataObject.getFileName() + " on media: " + dataObject.getMediaId());
                    File dataObjectFile;
                    try {
                        dataObjectFile = updateMediaLocationAndGetDataObjectFile(dataObject);
                    } catch (FileNotFoundOnMediaException fnfEx) {
                        if (fnfEx.isContinueCheckingFiles()) {
                            addFileToMissingFiles(dataObject);
                            ignoreMissingFiles = true;
                            continue;
                        }
                        throw fnfEx;
                    }
                    if (dataObjectFile == null) {
                        processingCancelled = true;
                        transferJobDAO.saveDataObject(dataObject);
                        break;
                    }
                    if (foundMatchOnMedia) {
                        filesOnMediaNotManifested.get(currentMediaId).remove(dataObjectFile.getAbsolutePath());
                    } else {
                        foundMatchOnMedia = true;
                        appendAllFilenamesOnMedia(currentMediaLocation, null);
                        filesOnMediaNotManifested.get(currentMediaId).remove(dataObjectFile.getAbsolutePath());
                    }
                    processedFilenamesOnCurrentMedia.add(dataObjectFile.getAbsolutePath());
                    results = scanDataObject(currentMediaLocation, qfDORecord, dataObject, results);
                    transferJobDAO.saveDataObject(dataObject);
                }
                if (!processingCancelled) {
                    iterator = transferJobDAO.getDataObjectQFRecordsByMediaId(qfRecord, mediaId);
                    while (iterator.hasNext()) {
                        QFDataObjectProcessingRecord qfDORecord = (QFDataObjectProcessingRecord) iterator.next();
                        DataObject dataObject = qfDORecord.getDataObject();
                        logger.finest("Copying data object: " + dataObject.getFileName() + " on media: " + dataObject.getMediaId());
                        fireItemProcessEvent("Copying and checksumming: " + dataObject.getFileName() + " on media: " + dataObject.getMediaId());
                        File destinationFile = getDestinationFileForDataObject(dataObject);
                        if (Boolean.FALSE.equals(qfDORecord.isCopiedToCarrier())) {
                            File dataObjectFile;
                            try {
                                dataObjectFile = updateMediaLocationAndGetDataObjectFile(dataObject);
                            } catch (FileNotFoundOnMediaException fnfEx) {
                                if (fnfEx.isContinueCheckingFiles()) {
                                    ignoreMissingFiles = true;
                                    continue;
                                }
                                throw fnfEx;
                            }
                            if (dataObjectFile == null) {
                                processingCancelled = true;
                                transferJobDAO.saveDataObject(dataObject);
                                break;
                            }
                            copyDataObject(qfDORecord, dataObject, dataObjectFile, destinationFile);
                        }
                        verifyCopiedFileChecksum(qfDORecord, dataObject, destinationFile, results);
                        transferJobDAO.saveDataObject(dataObject);
                    }
                }
            }
            fireItemProcessingCompleteEvent();
            if (!processingCancelled) {
                if (hasExtraFiles() && preQuarantineProcessHandler != null) {
                    preQuarantineProcessHandler.notifyExtraFilesOnMedia(filesOnMediaNotManifested);
                }
                if (ignoreMissingFiles && filesMissingFromManifest.size() > 0) {
                    if (preQuarantineProcessHandler == null) {
                        FilesOnMediaException fnfex = new FilesOnMediaException("Missing files from manifest!");
                        fnfex.extraFiles = false;
                        fnfex.stop = true;
                        throw fnfex;
                    }
                    preQuarantineProcessHandler.notifyMissingFilesFromManifest(filesMissingFromManifest);
                }
            }
            if (processingCancelled) {
                results.setPaused(true);
                writeResults(qfRecord, ProcessingResultType.CANCELLED, transferJob);
                transferJobDAO.saveTransferJob(transferJob);
                dataAccessManager.commitTransaction();
            } else if (!results.isErrorOccurred()) {
                writeResults(qfRecord, ProcessingResultType.PASSED, transferJob);
                transferJobDAO.saveTransferJob(transferJob);
                dataAccessManager.commitTransaction();
            } else {
                ProcessingErrorAction actionToTake = processingErrorHandler.determineAction(getStepName(), results);
                switch(actionToTake) {
                    case RESET:
                        logger.warning("Errors detected during processing, attempting to abort step.");
                        results.setStepReset(true);
                        abort();
                        break;
                    case STOP_PROCESSING:
                        logger.warning("Errors detected during processing, saving state and then stopping processing.");
                        writeResults(qfRecord, ProcessingResultType.FAILED, transferJob);
                        break;
                    case SAVE:
                        throw new IllegalStateException("This step cannot be saved in a state of error.");
                }
            }
            if (processingCancelled) {
                results.setResultsMessage("Pre-quarantine processing has been paused.");
            } else if (results.isErrorOccurred()) {
                results.setResultsMessage(results.getErrorCount() + " pre-quarantine processing errors.");
            } else {
                results.setResultsMessage("Pre-quarantine processing successfully completed. " + transferJob.getNumDataObjects() + " data objects verified.");
            }
            fireCompletedStepProcessingEvent(results);
        } catch (FileNotFoundOnMediaException fnfex) {
            writeResults(qfRecord, ProcessingResultType.FAILED, transferJob);
            results.setErrorOccurred(true);
            results.setResultsMessage("Transfer job stopped in pre-quarantine processing.");
            fireCompletedStepProcessingEvent(results);
        } catch (FilesOnMediaException fomEx) {
            if (fomEx.isReset()) {
                if (fomEx.isExtraFiles()) {
                    logger.warning("Extra files where found on media, attempting to abort step.");
                } else {
                    logger.warning("Files where missing from the media, attempting to abort step.");
                }
                results.setStepReset(true);
                results.setErrorOccurred(true);
                abort();
            } else if (fomEx.isStop()) {
                if (fomEx.isExtraFiles()) {
                    logger.warning("Extra files detected on media, saving state and then stopping processing.");
                } else {
                    logger.warning("Files where missing from the media, saving state and then stopping processing.");
                }
                writeResults(qfRecord, ProcessingResultType.FAILED, transferJob);
                results.setErrorOccurred(true);
            }
            fireCompletedStepProcessingEvent(results);
        } catch (StepException pex) {
            abort();
            throw pex;
        } finally {
            try {
                if (scanner != null) {
                    scanner.close();
                }
            } catch (IOException e) {
                logger.log(Level.WARNING, "Exception thrown when attempting to close the scanner.", e);
            }
        }
        return results;
    }

    private void addFileToMissingFiles(DataObject dataObject) {
        if (filesMissingFromManifest.containsKey(currentMediaId)) {
            filesMissingFromManifest.get(currentMediaId).add(dataObject.getFileName());
        } else {
            Vector<String> tmpList = new Vector<String>();
            tmpList.add(dataObject.getFileName());
            filesMissingFromManifest.put(currentMediaId, tmpList);
        }
    }

    private boolean hasExtraFiles() {
        boolean extraFiles = false;
        for (String mediaId : filesOnMediaNotManifested.keySet()) {
            if (filesOnMediaNotManifested.get(mediaId).size() > 0) {
                extraFiles = true;
            }
        }
        return extraFiles;
    }

    /**
	 * Save the scanner information to the QF Transfer job processing record.
	 * @param qfTransferJobRecord The record to save the scanner information
	 * @param scannerProperties The scanner properties
	 * @throws ProcessingException
	 */
    private void saveScannerInformation(QFTransferJobProcessingRecord qfTransferJobRecord, Properties scannerProperties) throws ProcessingException {
        logger.finest("Saving scanner information");
        qfTransferJobRecord.setPreQuarantineProcessingDate(new Date());
        qfTransferJobRecord.setPreQuarantineProcessingBy(currentUser);
        qfTransferJobRecord.setPreQuarantineVirusCheckerName(scannerProperties.getProperty(VirusScanner.SCANNER_PROVIDER_NAME));
        qfTransferJobRecord.setPreQuarantineVirusCheckerVersion(scannerProperties.getProperty(VirusScanner.SCANNER_VERSION_NUMBER));
        qfTransferJobRecord.setPreQuarantineVirusCheckDefinitionsVersion(scannerProperties.getProperty(VirusScanner.SCANNER_DEFINITIONS_VERSION));
        String dateStr = scannerProperties.getProperty(VirusScanner.SCANNER_DEFINITIONS_DATE);
        SimpleDateFormat formatter = new SimpleDateFormat(VirusScanner.DPR_VIRUS_DATE_FORMAT);
        Date definitionsDate;
        try {
            definitionsDate = formatter.parse(dateStr);
        } catch (ParseException e) {
            logger.severe("Invalid definitions date returned from scanner (" + dateStr + ").");
            throw new ProcessingException("Invalid definitions date returned from scanner. ", e);
        }
        long definitionsMillis = definitionsDate.getTime();
        long currentMillis = System.currentTimeMillis();
        boolean definitionsUpdated = true;
        if (currentMillis - definitionsMillis > Constants.MILLIS_PER_WEEK) {
            if (preQuarantineProcessHandler != null && preQuarantineProcessHandler.continueWithOldDefinitions(definitionsDate)) {
                definitionsUpdated = false;
            } else {
                logger.severe("Invalid virus definitions!");
                throw new ProcessingException("Invalid virus definitions!");
            }
        }
        qfTransferJobRecord.setPreQuarantineVirusCheckDefinitionsCurrent(definitionsUpdated ? Boolean.TRUE : Boolean.FALSE);
        logger.finest("Scanner properties saved.");
    }

    /**
	 * <p>Update the location of the 'current media' based on the media id of the supplied data object. If
	 * the media has changed, then this method will attempt to get the NEW location of the media.
	 * At the moment this method is conservative - we REALLY want to make sure we have a valid media.</p>
	 * 
	 * <p>On our first attempt, ie when {@link #processedMediaCount} is 0, current media location is set to the value
	 * of the field {@link #firstMediaLocation}. If this is null, we attempt to get the media location from
	 * our handler.</p>
	 * 
	 * <p>After our first attempt, If the data object's media id is not the same as the current media id, then
	 * increment {@link #processedMediaCount} and get a new media location from our handler.</p>
	 * 
	 * <p>After all this is done, we verify that the file for the data object we are currently processing actually
	 * exists at the media location specified.</p>
	 * 
	 * <p>PRE CONDITION - Validate has been called.
	 * <p>POST CONDITION - If this method returns successfully, then the current media location contains
	 * a file that has the same name as the current data object.
	 * </p>
	 * 
	 * @param dataObject
	 * @throws ProcessingException
	 */
    private File updateMediaLocationAndGetDataObjectFile(DataObject dataObject) throws ProcessingException {
        logger.finest("Getting media location for current data object.");
        if (!currentMediaId.equals(dataObject.getMediaId())) {
            if (currentMediaId != "") {
                processedMediaCount++;
            }
            currentMediaId = dataObject.getMediaId();
            if (processedMediaCount == 0) {
                currentMediaLocation = firstMediaLocation;
                if (currentMediaLocation == null || currentMediaId.length() == 0) {
                    currentMediaLocation = preQuarantineProcessHandler.getMediaLocationForFile(dataObject.getFileName(), dataObject.getMediaId(), currentMediaLocation);
                }
            } else {
                currentMediaLocation = preQuarantineProcessHandler.getMediaLocationForFile(dataObject.getFileName(), dataObject.getMediaId(), currentMediaLocation);
            }
            if (currentMediaLocation != null) {
                foundMatchOnMedia = false;
                changeOfMediaBeforeMediaID = false;
                processedFilenamesOnCurrentMedia.clear();
            }
        }
        if (currentMediaLocation == null) {
            return null;
        }
        File dataObjectFile = new File(currentMediaLocation, dataObject.getFileName());
        int attemptCounter = 0;
        while (!dataObjectFile.exists() && !dataObjectFile.isDirectory()) {
            if (preQuarantineProcessHandler == null) {
                logger.severe("Could not find file " + dataObject.getFileName() + " of media id: " + dataObject.getMediaId() + " at supplied media location: " + currentMediaLocation + " and no handler was supplied.");
                throw new ProcessingException("Could not find file " + dataObject.getFileName() + " of media id: " + dataObject.getMediaId() + " at supplied media location: " + currentMediaLocation + " and no handler was supplied.");
            }
            if (!ignoreMissingFiles) {
                String mediaLocation = preQuarantineProcessHandler.notifyMissingFileFromManifest(dataObject.getFileName(), dataObject.getMediaId(), currentMediaLocation);
                if (mediaLocation != null) {
                    currentMediaLocation = mediaLocation;
                    foundMatchOnMedia = false;
                    changeOfMediaBeforeMediaID = true;
                }
            } else {
                FileNotFoundOnMediaException fnfex = new FileNotFoundOnMediaException(dataObjectFile.getAbsolutePath(), currentMediaId, "File not found on media and told to skip missing files.");
                fnfex.setContinueCheckingFiles(true);
                throw fnfex;
            }
            if (currentMediaLocation == null) {
                return null;
            }
            dataObjectFile = new File(currentMediaLocation, dataObject.getFileName());
            if (++attemptCounter > MAX_ATTEMPTS_TO_GET_MEDIA_LOCATION) {
                logger.severe("Could not get current media location after " + attemptCounter + " attempts. (Looking for: " + dataObject.getFileName() + " on media: " + dataObject.getMediaId() + " and last media location supplied: " + currentMediaLocation);
                throw new ProcessingException("Could not get current media location after " + attemptCounter + " attempts. (Looking for: " + dataObject.getFileName() + " on media: " + dataObject.getMediaId() + " and last media location supplied: " + currentMediaLocation);
            }
        }
        logger.finest("current media location: " + currentMediaLocation);
        return dataObjectFile;
    }

    /**
	 * Scan the data object in question. If a virus is detected, use the {@link #preQuarantineProcessHandler} to determine whether or
	 * not to throw a {@link ProcessingException}. If no {@link #preQuarantineProcessHandler} is set, then always throw a
	 * {@link ProcessingException}. If we are continuing, update the step results and return them.
	 * 
	 * @param mediaLocation
	 * @param qfDORecord
	 * @param dataObject
	 * @throws ProcessingException
	 */
    private StepResults scanDataObject(String mediaLocation, QFDataObjectProcessingRecord qfDORecord, DataObject dataObject, StepResults results) throws ProcessingException {
        try {
            try {
                scanner.pingScanner();
            } catch (IOException ex) {
                fireStatusChangeEvent("Reconnecting to virus scanner.");
                logger.finest("Reconnecting to virus scanner");
                scanner = new VirusScanner(scannerPort);
            }
            logger.finest("Scanning file in folder: " + mediaLocation + " named: " + dataObject.getFileName());
            ScanInformation scanResult;
            scanResult = scanFile(scanner, new File(mediaLocation), dataObject.getFileName(), Constants.MAX_SCANNER_TRIES);
            if (scanResult.isScanPassed()) {
                qfDORecord.setPreQuarantineVirusCheckPassed(Boolean.TRUE);
            } else {
                qfDORecord.setPreQuarantineVirusCheckPassed(Boolean.FALSE);
                Collection<ScanInformation> dataObjectVirusRecords = new ArrayList<ScanInformation>();
                dataObjectVirusRecords.add(scanResult);
                qfDORecord.addVirusRecords(Constants.PRE_QUARANTINE_VIRUS_CHECK_NAME, dataObjectVirusRecords);
                if (preQuarantineProcessHandler == null) {
                    logger.severe("No pre-quarantine handler provided; virus detected, scan halted. (data object name:" + dataObject.getFileName() + " on media: " + dataObject.getMediaId() + " virus: " + scanResult.getVirusName() + ").");
                    throw new ProcessingException("No pre-quarantine handler provided; virus detected, scan halted. (data object name:" + dataObject.getFileName() + " on media: " + dataObject.getMediaId() + " virus: " + scanResult.getVirusName() + ").");
                }
                if (!preQuarantineProcessHandler.continueAfterVirusFound(scanResult)) {
                    logger.severe("Virus detected, Pre-quarantine handler canceled; scan halted. (data object name:" + dataObject.getFileName() + " on media: " + dataObject.getMediaId() + " virus: " + scanResult.getVirusName() + ").");
                    throw new ProcessingException("Virus detected, Pre-quarantine handler canceled; scan halted. (data object name:" + dataObject.getFileName() + " on media: " + dataObject.getMediaId() + " virus: " + scanResult.getVirusName() + ").");
                }
                if (results.getErrorsStored() < Constants.DEFAULT_ERROR_COUNT_TO_STORE) {
                    results.setErrorsStored(results.getErrorsStored() + 1);
                    Map<String, Object> errorResults = new LinkedHashMap<String, Object>();
                    errorResults.put("dataObject file name", dataObject.getFileName());
                    errorResults.put("dataObject media Id", dataObject.getMediaId());
                    errorResults.put("virus name", scanResult.getVirusName());
                    results.addError(errorResults);
                }
            }
        } catch (IOException e) {
            logger.severe("An IO exception occured during virus scanning. (" + dataObject.getFileName() + " on media " + dataObject.getMediaId() + ").");
            throw new ProcessingException("An IO exception occured during virus scanning. (" + dataObject.getFileName() + " on media " + dataObject.getMediaId() + ").", e);
        } catch (VirusScannerException e) {
            logger.severe("A virus scanner exception occurred during virus scanning. (" + dataObject.getFileName() + " on media " + dataObject.getMediaId() + ").");
            logger.severe("Exception message: " + e.getErrorMessage());
            if (e.getCause() != null) {
                logger.severe("Exception cause: " + e.getCause());
            } else {
                logger.severe("Exception cause was null");
            }
            e.printStackTrace();
            throw new ProcessingException("A virus scanner exception occurred during virus scanning. (" + dataObject.getFileName() + " on media " + dataObject.getMediaId() + ").", e);
        }
        return results;
    }

    /**
	 * Recursive method to attempt to scan a file. If a {@link VirusScannerException} is thrown and we have any attempts remaining,
	 * then decrement our remaining attempts and call this method again. Eventually, if the exception is still being
	 * thrown, the {@link VirusScannerException} will be thrown, and this will propagate back up through the stack.
	 * 
	 * @param scanner
	 * @param sourceDirectory
	 * @param fileName
	 * @param remainingAttempts
	 * @return
	 * @throws VirusScannerException
	 * @throws IOException
	 */
    private ScanInformation scanFile(VirusScanner scanner, File sourceDirectory, String fileName, int remainingAttempts) throws VirusScannerException, IOException {
        try {
            return scanner.scanFile(sourceDirectory, fileName);
        } catch (VirusScannerException vse) {
            logger.severe("Virus scanner exception thrown! " + vse.getMessage());
            if (remainingAttempts > 0) {
                scanner.reset();
                return scanFile(scanner, sourceDirectory, fileName, remainingAttempts - 1);
            }
            throw vse;
        }
    }

    /**
	 * Simply get the output folder that this data object is going to. If we can't get it, it means there is
	 * something wrong with the carrier.
	 * @param dataObject The current data object we are processing.
	 * @return The File object that points to the destination 'file' for the data object.
	 * @throws ProcessingException
	 */
    private File getDestinationFileForDataObject(DataObject dataObject) throws ProcessingException {
        File destDir = new File(qfDataOnCarrierDir, Constants.makeMediaDataPath(dataObject.getMediaId()));
        destDir.mkdirs();
        if (!destDir.exists() || !destDir.isDirectory()) {
            logger.severe("Could not create output location on carrier. (" + destDir.getName() + ").");
            throw new ProcessingException("Could not create output location on carrier. (" + destDir.getName() + ").");
        }
        File destinationFile = new File(destDir, dataObject.getFileName());
        return destinationFile;
    }

    /**
	 * Copy the file from the media to the carrier, throw an exception if there's an error. By this stage, if there is an IO error,
	 * we are looking at something like the media being bad or the carrier full; either way, by now it's gone on too long.
	 * 
	 * TODO: potentially the handler could be extended to let the application 'retry', for instance to remove a CD and clean it or some
	 * such.
	 * 
	 * @param qfDORecord
	 * @param dataObject
	 * @param dataObjectFile
	 * @param destinationFile
	 * @throws ProcessingException
	 */
    private void copyDataObject(QFDataObjectProcessingRecord qfDORecord, DataObject dataObject, File dataObjectFile, File destinationFile) throws ProcessingException {
        try {
            logger.finest("Copying file: " + dataObjectFile.getAbsolutePath() + " to " + destinationFile.getAbsolutePath());
            FileUtils.fileCopy(dataObjectFile, destinationFile);
            qfDORecord.setCopiedToCarrier(Boolean.TRUE);
            dataObject.setDateLastModified(new Date(dataObjectFile.lastModified()));
        } catch (IOException e) {
            logger.severe("Could not find data object: " + dataObject.getFileName() + " on media " + dataObject.getMediaId() + " (absolute path: " + dataObjectFile.getAbsolutePath() + ")");
            throw new ProcessingException("Could not find data object: " + dataObject.getFileName() + " on media " + dataObject.getMediaId() + " (absolute path: " + dataObjectFile.getAbsolutePath() + ")");
        }
    }

    /**
	 * Verify the copied file checksum.
	 * @param qfDORecord
	 * @param dataObject
	 * @param destinationFile
	 * @throws ProcessingException 
	 */
    private void verifyCopiedFileChecksum(QFDataObjectProcessingRecord qfDORecord, DataObject dataObject, File destinationFile, StepResults results) throws ProcessingException {
        try {
            logger.finest("Verifying file checksum: " + destinationFile.getAbsolutePath());
            String copiedFileChecksum = FileUtils.getChecksum(destinationFile, dataObject.getAlgorithm());
            if (copiedFileChecksum.equals(dataObject.getChecksum())) {
                qfDORecord.setPreQuarantineChecksumPassed(Boolean.TRUE);
            } else {
                if (preQuarantineProcessHandler != null && preQuarantineProcessHandler.continueAfterInvalidChecksum()) {
                    qfDORecord.setPreQuarantineChecksumPassed(Boolean.FALSE);
                    if (results.getErrorsStored() < Constants.DEFAULT_ERROR_COUNT_TO_STORE) {
                        results.setErrorsStored(results.getErrorsStored() + 1);
                        Map<String, Object> errorResults = new LinkedHashMap<String, Object>();
                        errorResults.put("Data Object File Name", dataObject.getFileName());
                        errorResults.put("Data Object Media Id", dataObject.getMediaId());
                        errorResults.put("Error", "Checksum Mismatch");
                        results.addError(errorResults);
                    }
                } else {
                    logger.severe("Checksum failure on copied file: " + destinationFile.getAbsolutePath());
                    throw new ProcessingException("Checksum failure on copied file: " + destinationFile.getAbsolutePath());
                }
            }
        } catch (IOException e) {
            logger.severe("Checksum failure on copied file: " + destinationFile.getAbsolutePath());
            throw new ProcessingException("Checksum failure on copied file: " + destinationFile.getAbsolutePath());
        }
    }

    /**
	 * Save results of the step.
	 * @param qfTransferJobRecord
	 * @param transferJob
	 * @param success
	 */
    private void writeResults(QFTransferJobProcessingRecord qfTransferJobRecord, ProcessingResultType resultType, TransferJob transferJob) {
        qfTransferJobRecord.setPreQuarantineProcessingDate(new Date());
        qfTransferJobRecord.setPreQuarantineProcessingBy(currentUser);
        qfTransferJobRecord.setIntoQuarantineBy(currentUser);
        qfTransferJobRecord.setIntoQuarantineDate(new Date());
        qfTransferJobRecord.setQuarantinePeriodReason(quarantinePeriodReason);
        qfTransferJobRecord.setCarrierDeviceLocation(outputCarrierLocation);
        switch(resultType) {
            case PASSED:
                qfTransferJobRecord.setExpectedDateOutOfQuarantine(expectedDateOut);
                qfTransferJobRecord.setPreQuarantineProcessingStatus(Constants.PASSED_STATE);
                transferJob.setJobStatus(JobStatus.QF_PRE_QUARANTINE_PASSED);
                logger.fine("Processing Completed successfully");
                break;
            case FAILED:
                failStep();
                logger.fine("Processing Completed with failure.");
                break;
            case CANCELLED:
                qfTransferJobRecord.setPreQuarantineProcessingStatus(Constants.PAUSED_STATE);
                logger.fine("Processing cancelled - step is now paused.");
                break;
        }
    }

    @Override
    public Set<String> getRequiredPropertyNames() {
        Set<String> requiredPropertyNames = new LinkedHashSet<String>();
        requiredPropertyNames.add(StepProperties.SCANNER_PORT_PROPERTY_NAME);
        requiredPropertyNames.add(StepProperties.OUTPUT_CARRIER_LOCATION_PROPERTY_NAME);
        requiredPropertyNames.add(StepProperties.OUTPUT_CARRIER_ID_PROPERTY_NAME);
        requiredPropertyNames.add(StepProperties.EXPECTED_DATE_OUT_PROPERTY_NAME);
        return requiredPropertyNames;
    }

    @Override
    public Set<String> getAllPropertyNames() {
        Set<String> propertyNames = getRequiredPropertyNames();
        propertyNames.add(StepProperties.REASON_FOR_SHORT_QUARANTINE_PROPERTY_NAME);
        propertyNames.add(StepProperties.FIRST_MEDIA_LOCATION_PROPERTY_NAME);
        return propertyNames;
    }

    /**
	 * <p>Validate the required properties (or fields) for this step. If any fields are invalid,
	 * log the problem then throw a processing exception.</p>
	 * 
	 * <p>Verify that if the first media has not been set, or there are multiple media, 
	 * that a {@link PreQuarantineProcessHandler} has been provided.</p>
	 * @param transferJob
	 * @throws ProcessingException
	 */
    private void validateProperties(TransferJob transferJob) throws ProcessingException {
        if (outputCarrierLocation == null || outputCarrierLocation.length() == 0) {
            outputCarrierLocation = properties.getProperty(StepProperties.OUTPUT_CARRIER_LOCATION_PROPERTY_NAME);
        }
        File outputCarrierDir = new File(outputCarrierLocation);
        if (!outputCarrierDir.exists()) {
            logger.severe("Carrier location not set or invalid:" + outputCarrierLocation + " (exists == false)");
            throw new CarrierNotFoundException("Carrier location not set or invalid:" + outputCarrierLocation + " (exists == false)");
        }
        if (!outputCarrierDir.isDirectory()) {
            logger.severe("Carrier location not set or invalid:" + outputCarrierLocation + " (isDir == false)");
            throw new CarrierNotFoundException("Carrier location not set or invalid:" + outputCarrierLocation + " (isDir == false)");
        }
        qfDataOnCarrierDir = new File(outputCarrierLocation, transferJob.getTransferJobNumber().getPath() + File.separatorChar + Constants.QF_DATA_DIR_NAME);
        qfDataOnCarrierDir.mkdirs();
        if (!qfDataOnCarrierDir.exists() || !qfDataOnCarrierDir.isDirectory()) {
            logger.severe("Could not create qf data directory on carrier.");
            throw new StepPropertiesValidationException(StepProperties.OUTPUT_CARRIER_LOCATION_PROPERTY_NAME, "Could not create qf data directory on carrier.");
        }
        if (scannerPortString == null || scannerPortString.length() == 0) {
            scannerPortString = properties.getProperty(StepProperties.SCANNER_PORT_PROPERTY_NAME);
        }
        if (scannerPortString == null || scannerPortString.length() == 0) {
            logger.severe("Scanner port was not set.");
            throw new StepPropertiesValidationException(StepProperties.SCANNER_PORT_PROPERTY_NAME, "Scanner port was not set.");
        }
        try {
            scannerPort = Integer.parseInt(scannerPortString);
        } catch (NumberFormatException e) {
            logger.severe("Scanner port invalid (" + scannerPortString + ")");
            throw new StepPropertiesValidationException(StepProperties.SCANNER_PORT_PROPERTY_NAME, "Scanner port was not a valid number - " + scannerPortString);
        }
        if (firstMediaLocation == null || firstMediaLocation.length() == 0) {
            firstMediaLocation = properties.getProperty(StepProperties.FIRST_MEDIA_LOCATION_PROPERTY_NAME);
        }
        if (preQuarantineProcessHandler == null) {
            if (firstMediaLocation == null || firstMediaLocation.length() == 0) {
                logger.severe("First media location was not set and no pre Quarantine Handler was provided.");
                throw new StepPropertiesValidationException(StepProperties.FIRST_MEDIA_LOCATION_PROPERTY_NAME, "First media location was not set and no pre Quarantine Handler was provided.");
            }
            File firstMedia = new File(firstMediaLocation);
            if (!firstMedia.exists() || !firstMedia.isDirectory() || !firstMedia.canRead()) {
                logger.severe("The provided media location does not appear to be valid, (" + firstMediaLocation + ") and no pre-quarantine handler was provided.");
                throw new StepPropertiesValidationException(StepProperties.FIRST_MEDIA_LOCATION_PROPERTY_NAME, "The provided media location does not appear to be valid, (" + firstMediaLocation + ") and no pre-quarantine handler was provided.");
            }
        }
        int mediaCount = transferJobDAO.getMediaCount(transferJob);
        if (mediaCount > 1 && preQuarantineProcessHandler == null) {
            logger.severe("Multiple media detected and no prequarantine handler set.");
            throw new ProcessingException("Multiple media detected and no prequarantine handler set.");
        }
        if (quarantinePeriodReason == null || quarantinePeriodReason.length() == 0) {
            quarantinePeriodReason = properties.getProperty(StepProperties.REASON_FOR_SHORT_QUARANTINE_PROPERTY_NAME);
        }
        if (expectedDateOut == null) {
            String dateString = properties.getProperty(StepProperties.EXPECTED_DATE_OUT_PROPERTY_NAME);
            if (dateString == null) {
                logger.severe("Expected date out of quarantine was not set! ( Property: " + StepProperties.EXPECTED_DATE_OUT_PROPERTY_NAME + "). ");
                throw new StepPropertiesValidationException(StepProperties.EXPECTED_DATE_OUT_PROPERTY_NAME, "Expected date out of quarantine was not set! ( Property: " + StepProperties.EXPECTED_DATE_OUT_PROPERTY_NAME + "). ");
            }
            if (dateString.length() != Constants.DATE_FORMAT.length()) {
                logger.severe("Invalid date (expected to be in form of " + Constants.DATE_FORMAT + ", as a string). recieved: \"" + properties.getProperty(StepProperties.EXPECTED_DATE_OUT_PROPERTY_NAME) + "\"). ");
                throw new StepPropertiesValidationException(StepProperties.EXPECTED_DATE_OUT_PROPERTY_NAME, "Invalid date (expected to be in form of " + Constants.DATE_FORMAT + ", as a string). recieved: \"" + properties.getProperty(StepProperties.EXPECTED_DATE_OUT_PROPERTY_NAME) + "\"). ");
            }
            try {
                expectedDateOut = new SimpleDateFormat(Constants.DATE_FORMAT).parse(properties.getProperty(StepProperties.EXPECTED_DATE_OUT_PROPERTY_NAME));
            } catch (ParseException pe) {
                logger.severe("Invalid date (expected to be in form of " + Constants.DATE_FORMAT + ", as a string). " + properties.getProperty(StepProperties.EXPECTED_DATE_OUT_PROPERTY_NAME));
                throw new StepPropertiesValidationException("Invalid date (expected to be in the form of " + Constants.DATE_FORMAT + " as a string, recieved: \"" + properties.getProperty(StepProperties.EXPECTED_DATE_OUT_PROPERTY_NAME) + "\"). ", pe);
            }
        }
        if (expectedDateOut == null) {
            logger.severe("No date out was provided.");
            throw new StepPropertiesValidationException(StepProperties.EXPECTED_DATE_OUT_PROPERTY_NAME, "No date out was provided.");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.AM_PM, Calendar.AM);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (expectedDateOut.before(new Date(calendar.getTime().getTime()))) {
            logger.severe("The expected date out (" + calendar.getTime().toString() + " ) must not be BEFORE the the date the job is going into quarantine (" + new Date().toString() + ", zeroed time:" + calendar.getTime() + ").");
            throw new StepPropertiesValidationException(StepProperties.EXPECTED_DATE_OUT_PROPERTY_NAME, "The expected date out (" + calendar.getTime().toString() + " ) must not be BEFORE the the date the job is going into quarantine (" + new Date().toString() + ", zeroed time:" + calendar.getTime() + ").");
        }
        calendar.add(Calendar.DAY_OF_YEAR, Constants.DEFAULT_QUARANTINE_PERIOD_IN_DAYS);
        Date defaultDateOut = calendar.getTime();
        if (expectedDateOut.before(defaultDateOut) && !expectedDateOut.equals(defaultDateOut)) {
            if (quarantinePeriodReason == null || quarantinePeriodReason.length() == 0) {
                logger.severe("The expected date out (" + calendar.getTime().toString() + " ) was BEFORE the default period, (" + calendar.getTime().toString() + "), and no reason for the short period was provided.");
                throw new NoReasonForShortQuarantineException("The expected date out (" + calendar.getTime().toString() + " ) was BEFORE the default period, (" + calendar.getTime().toString() + "), and no reason for the short period was provided.");
            }
        }
    }

    @Override
    public String getStepName() {
        return STEP_NAME;
    }

    @Override
    public String getStepStatus() {
        return qfRecord.getPreQuarantineProcessingStatus();
    }

    public Date getDefaultDateOut() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, Constants.DEFAULT_QUARANTINE_PERIOD_IN_DAYS);
        calendar.set(Calendar.AM_PM, Calendar.AM);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
	 * @return the outputCarrierLocation
	 */
    public String getOutputCarrierLocation() {
        return outputCarrierLocation;
    }

    /**
	 * @param outputCarrierLocation the outputCarrierLocation to set
	 */
    public void setOutputCarrierLocation(String outputCarrierLocation) {
        this.outputCarrierLocation = outputCarrierLocation;
    }

    /**
	 * @return the outputCarrierId
	 */
    public String getOutputCarrierId() {
        return outputCarrierId;
    }

    /**
	 * @param outputCarrierId the outputCarrierId to set
	 */
    public void setOutputCarrierId(String outputCarrierId) {
        this.outputCarrierId = outputCarrierId;
    }

    /**
	 * @return the firstMediaLocation
	 */
    public String getFirstMediaLocation() {
        return firstMediaLocation;
    }

    /**
	 * @param firstMediaLocation the firstMediaLocation to set
	 */
    public void setFirstMediaLocation(String firstMediaLocation) {
        this.firstMediaLocation = firstMediaLocation;
    }

    /**
	 * @return the expectedDateOut
	 */
    public Date getExpectedDateOut() {
        return expectedDateOut;
    }

    /**
	 * @param expectedDateOut the expectedDateOut to set
	 */
    public void setExpectedDateOut(Date expectedDateOut) {
        this.expectedDateOut = expectedDateOut;
    }

    /**
	 * @return the quarantinePeriodReason
	 */
    public String getQuarantinePeriodReason() {
        return quarantinePeriodReason;
    }

    /**
	 * @param quarantinePeriodReason the quarantinePeriodReason to set
	 */
    public void setQuarantinePeriodReason(String quarantinePeriodReason) {
        this.quarantinePeriodReason = quarantinePeriodReason;
    }

    /**
	 * @return the preQuarantineMediaHandler
	 */
    public PreQuarantineProcessHandler getPreQuarantineMediaHandler() {
        return preQuarantineProcessHandler;
    }

    /**
	 * @param preQuarantineMediaHandler the preQuarantineMediaHandler to set
	 */
    public void setPreQuarantineProcessingHandler(PreQuarantineProcessHandler preQuarantineMediaHandler) {
        preQuarantineProcessHandler = preQuarantineMediaHandler;
    }

    /**
	 * @return the scannerPortString
	 */
    public String getScannerPortString() {
        return scannerPortString;
    }

    /**
	 * @param scannerPortString the scannerPortString to set
	 */
    public void setScannerPortString(String scannerPortString) {
        this.scannerPortString = scannerPortString;
    }

    @Override
    public String getDescription() {
        return "Scan data objects for viruses, check their checksums and set up the output carrier";
    }

    /**
	 * Add all filenames of the files on the current media location to the allFilesOnMedia data structure. 
	 * @param mediaLocation The location of a directory containing media.
	 * @param mediaFiles The current list of files found on this media location. This should be null, it is only required by itself for recursion.
	 */
    private void appendAllFilenamesOnMedia(String mediaLocation, List<String> mediaFilesParam) {
        File mediaDir = new File(mediaLocation);
        if (mediaDir.exists() && mediaDir.isDirectory()) {
            List<String> mediaFiles = mediaFilesParam;
            if (mediaFiles == null) {
                mediaFiles = new Vector<String>();
            }
            File[] files = mediaDir.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    appendAllFilenamesOnMedia(file.getAbsolutePath(), mediaFiles);
                } else {
                    if (changeOfMediaBeforeMediaID) {
                        if (!processedFilenamesOnCurrentMedia.contains(file.getAbsolutePath())) {
                            mediaFiles.add(file.getAbsolutePath());
                        }
                    } else {
                        mediaFiles.add(file.getAbsolutePath());
                    }
                }
            }
            if (mediaLocation == currentMediaLocation) {
                filesOnMediaNotManifested.put(currentMediaId, mediaFiles);
            }
        }
    }

    /**
	 * Handle requests for media and so on.
	 * @author andy
	 */
    public static interface PreQuarantineProcessHandler {

        public void setMediaCount(int mediaCount);

        public boolean continueAfterInvalidChecksum();

        public boolean continueAfterVirusFound(ScanInformation scanResult);

        /**
		 * 
		 * @param fileName
		 * @param mediaId
		 * @param currentLocation
		 * @return
		 * @throws FileNotFoundOnMediaException if the decision to StopProcessing was made
		 */
        public String getMediaLocationForFile(String fileName, String mediaId, String currentLocation) throws FileNotFoundOnMediaException;

        public boolean continueWithOldDefinitions(Date definitionsDate);

        public boolean notifyExtraFilesOnMedia(Map<String, List<String>> extraFilesByMedia) throws FilesOnMediaException;

        /**
		 * Notify that where files from the media that appear in the manifest.
		 * The only option in this case is to reset step or stop processing.
		 * @param filesByMedia A map of missing files to the media ID. 
		 * @throws FilesOnMediaException 
		 */
        public void notifyMissingFilesFromManifest(Map<String, List<String>> filesByMedia) throws FilesOnMediaException;

        public String notifyMissingFileFromManifest(String fileName, String mediaId, String currentLocation) throws FileNotFoundOnMediaException;
    }

    /**
	 * Exception to represent the situation where a job has been given a quarantine period that is shorter than the default,
	 * but no reason for this has been given.
	 */
    public static class NoReasonForShortQuarantineException extends ProcessingException {

        private static final long serialVersionUID = 1L;

        public NoReasonForShortQuarantineException(String message) {
            super(message);
        }
    }

    /**
	 * Exception to represent the situation where a file cannot be found on the given media,
	 * or any other media attempted.
	 * @author Justin Waddell
	 *
	 */
    public static class FileNotFoundOnMediaException extends ProcessingException {

        private static final long serialVersionUID = 1L;

        private String fileName;

        private String mediaId;

        private boolean continueCheckingFiles;

        public FileNotFoundOnMediaException(String fileName, String mediaId, String message) {
            super(message);
            this.fileName = fileName;
            this.mediaId = mediaId;
        }

        public String getFileName() {
            return fileName;
        }

        public String getMediaId() {
            return mediaId;
        }

        public void setContinueCheckingFiles(boolean continueCheckingFiles) {
            this.continueCheckingFiles = continueCheckingFiles;
        }

        public boolean isContinueCheckingFiles() {
            return continueCheckingFiles;
        }
    }

    /**
	 * Exception to represent the situation where extra files were found on the given media.
	 * or any other media attempted.
	 * @author Matthew Oliver
	 */
    public static class FilesOnMediaException extends ProcessingException {

        private static final long serialVersionUID = 1L;

        private boolean reset;

        private boolean stop;

        private boolean extraFiles;

        public FilesOnMediaException(String message) {
            super(message);
            setReset(false);
            setStop(false);
            setExtraFiles(false);
        }

        public void setStop(boolean stop) {
            this.stop = stop;
        }

        public boolean isStop() {
            return stop;
        }

        public void setReset(boolean reset) {
            this.reset = reset;
        }

        public boolean isReset() {
            return reset;
        }

        /**
		 * Set the whether this exception is in relation to a extra files on the media device
		 * @param extraFiles
		 */
        public void setExtraFiles(boolean extraFiles) {
            this.extraFiles = extraFiles;
        }

        public boolean isExtraFiles() {
            return extraFiles;
        }
    }
}
