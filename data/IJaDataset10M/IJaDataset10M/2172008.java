package frost.fileTransfer.upload;

import java.io.*;
import java.util.*;
import java.util.logging.*;
import frost.*;
import frost.fcp.*;
import frost.fileTransfer.*;
import frost.fileTransfer.sharing.*;
import frost.storage.*;
import frost.util.*;
import frost.util.model.*;

public class UploadManager implements ExitSavable {

    private static final Logger logger = Logger.getLogger(UploadManager.class.getName());

    private UploadModel model;

    private UploadPanel panel;

    private UploadTicker ticker;

    public UploadManager() {
        super();
    }

    public void initialize(final List<FrostSharedFileItem> sharedFiles) throws StorageException {
        getPanel();
        getModel().initialize(sharedFiles);
        if (FcpHandler.isFreenet05()) {
            for (int x = 0; x < getModel().getItemCount(); x++) {
                final FrostUploadItem item = (FrostUploadItem) getModel().getItemAt(x);
                frost.fcp.fcp05.FcpInsert.updateProgress(item);
            }
        }
    }

    /**
     * Count running items in model.
     */
    public void updateFileTransferInformation(final FileTransferInformation infos) {
        int waitingItems = 0;
        int runningItems = 0;
        for (int x = 0; x < model.getItemCount(); x++) {
            final FrostUploadItem ulItem = (FrostUploadItem) model.getItemAt(x);
            if (ulItem.getState() != FrostUploadItem.STATE_DONE && ulItem.getState() != FrostUploadItem.STATE_FAILED) {
                waitingItems++;
            }
            if (ulItem.getState() == FrostUploadItem.STATE_PROGRESS) {
                runningItems++;
            }
        }
        infos.setUploadsRunning(runningItems);
        infos.setUploadsWaiting(waitingItems);
    }

    public void startTicker() {
        if (Core.isFreenetOnline()) {
            getTicker().start();
        }
    }

    public void exitSave() throws StorageException {
        getPanel().getTableFormat().saveTableLayout();
        getModel().exitSave();
    }

    public void addPanelToMainFrame(final MainFrame mainFrame) {
        mainFrame.addPanel("MainFrame.tabbedPane.uploads", getPanel());
    }

    public UploadPanel getPanel() {
        if (panel == null) {
            panel = new UploadPanel();
            panel.setModel(getModel());
            panel.initialize();
        }
        return panel;
    }

    private UploadTicker getTicker() {
        if (ticker == null) {
            ticker = new UploadTicker(getModel());
        }
        return ticker;
    }

    public UploadModel getModel() {
        if (model == null) {
            model = new UploadModel(new UploadTableFormat());
        }
        return model;
    }

    /**
     * Handle a finished file upload, either successful or failed.
     */
    public void notifyUploadFinished(final FrostUploadItem uploadItem, final FcpResultPut result) {
        if (result != null && (result.isSuccess() || result.isKeyCollision())) {
            logger.info("Upload of " + uploadItem.getFile().getName() + " was successful.");
            uploadItem.setKey(result.getChkKey());
            if (uploadItem.isSharedFile()) {
                uploadItem.getSharedFileItem().notifySuccessfulUpload(result.getChkKey());
            }
            uploadItem.setEnabled(Boolean.FALSE);
            uploadItem.setState(FrostUploadItem.STATE_DONE);
            uploadItem.setUploadFinishedMillis(System.currentTimeMillis());
            if (uploadItem.isSharedFile()) {
                getModel().notifySharedFileUploadWasSuccessful(uploadItem);
            } else {
                if (Core.frostSettings.getBoolValue(SettingsClass.LOG_UPLOADS_ENABLED) && !uploadItem.isLoggedToFile()) {
                    final String line = uploadItem.getKey() + "/" + uploadItem.getFile().getName();
                    final String fileName = Core.frostSettings.getValue(SettingsClass.DIR_LOCALDATA) + "Frost-Uploads.log";
                    final File targetFile = new File(fileName);
                    FileAccess.appendLineToTextfile(targetFile, line);
                    uploadItem.setLoggedToFile(true);
                }
            }
            if (Core.frostSettings.getBoolValue(SettingsClass.UPLOAD_REMOVE_FINISHED)) {
                getModel().removeFinishedUploads();
            }
        } else {
            logger.warning("Upload of " + uploadItem.getFile().getName() + " was NOT successful.");
            if (result != null && result.isFatal()) {
                uploadItem.setEnabled(Boolean.FALSE);
                uploadItem.setState(FrostUploadItem.STATE_FAILED);
            } else {
                uploadItem.setRetries(uploadItem.getRetries() + 1);
                if (uploadItem.getRetries() > Core.frostSettings.getIntValue(SettingsClass.UPLOAD_MAX_RETRIES)) {
                    uploadItem.setEnabled(Boolean.FALSE);
                    uploadItem.setState(FrostUploadItem.STATE_FAILED);
                } else {
                    uploadItem.setState(FrostUploadItem.STATE_WAITING);
                }
            }
            if (result != null) {
                uploadItem.setErrorCodeDescription(result.getCodeDescription());
            }
        }
        uploadItem.setLastUploadStopTimeMillis(System.currentTimeMillis());
    }

    /**
     * Start upload now (manually).
     */
    public boolean startUpload(final FrostUploadItem ulItem) {
        if (FileTransferManager.inst().getPersistenceManager() != null) {
            return FileTransferManager.inst().getPersistenceManager().startUpload(ulItem);
        } else {
            return ticker.startUpload(ulItem);
        }
    }

    /**
     * Chooses next upload item to start from upload table.
     * @return the next upload item to start uploading or null if a suitable one was not found.
     */
    public FrostUploadItem selectNextUploadItem() {
        final ArrayList<FrostUploadItem> waitingItems = new ArrayList<FrostUploadItem>();
        final long currentTime = System.currentTimeMillis();
        for (int i = 0; i < model.getItemCount(); i++) {
            final FrostUploadItem ulItem = (FrostUploadItem) model.getItemAt(i);
            boolean itemIsEnabled = (ulItem.isEnabled() == null ? true : ulItem.isEnabled().booleanValue());
            if (!itemIsEnabled) {
                continue;
            }
            if (ulItem.isExternal()) {
                continue;
            }
            if (FileTransferManager.inst().getPersistenceManager() != null) {
                if (FileTransferManager.inst().getPersistenceManager().isDirectTransferInProgress(ulItem)) {
                    continue;
                }
            }
            if (ulItem.getState() != FrostUploadItem.STATE_WAITING) {
                continue;
            }
            if (FcpHandler.isFreenet05() && ulItem.getKey() == null) {
                continue;
            }
            final long waittimeMillis = Core.frostSettings.getIntValue(SettingsClass.UPLOAD_WAITTIME) * 60L * 1000L;
            if ((currentTime - ulItem.getLastUploadStopTimeMillis()) < waittimeMillis) {
                continue;
            }
            waitingItems.add(ulItem);
        }
        if (waitingItems.size() == 0) {
            return null;
        }
        if (waitingItems.size() > 1) {
            Collections.sort(waitingItems, nextItemCmp);
        }
        return waitingItems.get(0);
    }

    public void notifyUploadItemEnabledStateChanged(final FrostUploadItem ulItem) {
        if (FileTransferManager.inst().getPersistenceManager() == null) {
            return;
        }
        if (ulItem.isExternal()) {
            return;
        }
        if (ulItem.getState() != FrostUploadItem.STATE_PROGRESS) {
            return;
        }
        final boolean itemIsEnabled = (ulItem.isEnabled() == null ? true : ulItem.isEnabled().booleanValue());
        if (itemIsEnabled) {
            final int prio = Core.frostSettings.getIntValue(SettingsClass.FCP2_DEFAULT_PRIO_FILE_UPLOAD);
            FileTransferManager.inst().getPersistenceManager().changeItemPriorites(new ModelItem[] { ulItem }, prio);
        } else {
            FileTransferManager.inst().getPersistenceManager().changeItemPriorites(new ModelItem[] { ulItem }, 6);
        }
    }

    private static final Comparator<FrostUploadItem> nextItemCmp = new Comparator<FrostUploadItem>() {

        public int compare(FrostUploadItem value1, FrostUploadItem value2) {
            int cmp1 = Mixed.compareLong(value1.getUploadAddedMillis(), value2.getUploadAddedMillis());
            if (cmp1 != 0) {
                return cmp1;
            }
            int blocksTodo1;
            int blocksTodo2;
            if (value1.getTotalBlocks() > 0 && value1.getDoneBlocks() > 0) {
                blocksTodo1 = value1.getTotalBlocks() - value1.getDoneBlocks();
            } else if (FcpHandler.isFreenet05() && value1.getFileSize() <= frost.fcp.fcp05.FcpInsert.smallestChunk) {
                blocksTodo1 = 1;
            } else {
                blocksTodo1 = Integer.MAX_VALUE;
            }
            if (value2.getTotalBlocks() > 0 && value2.getDoneBlocks() > 0) {
                blocksTodo2 = value2.getTotalBlocks() - value2.getDoneBlocks();
            } else if (FcpHandler.isFreenet05() && value2.getFileSize() <= frost.fcp.fcp05.FcpInsert.smallestChunk) {
                blocksTodo2 = 1;
            } else {
                blocksTodo2 = Integer.MAX_VALUE;
            }
            int cmp2 = Mixed.compareInt(blocksTodo1, blocksTodo2);
            if (cmp2 != 0) {
                return cmp2;
            }
            return Mixed.compareLong(value1.getFileSize(), value2.getFileSize());
        }
    };
}
