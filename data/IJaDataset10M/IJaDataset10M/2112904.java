package frost.fileTransfer;

import java.util.*;
import java.util.logging.*;
import frost.fileTransfer.upload.*;
import frost.storage.*;
import frost.storage.perst.*;

public class NewUploadFilesManager implements ExitSavable {

    private static final Logger logger = Logger.getLogger(NewUploadFilesManager.class.getName());

    private LinkedList<NewUploadFile> newUploadFiles;

    private GenerateShaThread generateShaThread;

    public void initialize() throws StorageException {
        try {
            newUploadFiles = FrostFilesStorage.inst().loadNewUploadFiles();
        } catch (final Throwable e) {
            logger.log(Level.SEVERE, "Error loading new upload files", e);
            throw new StorageException("Error loading new upload files");
        }
        generateShaThread = new GenerateShaThread();
    }

    /**
     * Start the generate SHA thread.
     */
    public void start() {
        generateShaThread.start();
    }

    public void exitSave() throws StorageException {
        synchronized (newUploadFiles) {
            try {
                FrostFilesStorage.inst().saveNewUploadFiles(newUploadFiles);
            } catch (final Throwable e) {
                logger.log(Level.SEVERE, "Error saving new upload files", e);
                throw new StorageException("Error saving new upload files");
            }
        }
    }

    public void addNewUploadFiles(final List<NewUploadFile> newFiles) {
        synchronized (newUploadFiles) {
            for (final NewUploadFile nuf : newFiles) {
                newUploadFiles.add(nuf);
                generateShaThread.addToFileQueue(nuf);
            }
        }
    }

    public void deleteNewUploadFile(final NewUploadFile nuf) {
        synchronized (newUploadFiles) {
            newUploadFiles.remove(nuf);
        }
    }
}
