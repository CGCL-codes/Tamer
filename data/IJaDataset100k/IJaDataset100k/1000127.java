package com.teletalk.jserver.queue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.RandomAccessFile;
import com.teletalk.jserver.StatusTransitionException;
import com.teletalk.jserver.SubComponent;
import com.teletalk.jserver.pool.ObjectPool;
import com.teletalk.jserver.property.Property;
import com.teletalk.jserver.property.StringProperty;
import com.teletalk.jserver.util.NoHeadersObjectInputStream;

/**
 * The default implementation of a persistent storage mechanism for the Queue class. This implementation stores QueueItem 
 * objects to a filesystem as separate files. Each QueueItem object results in two files; one for a serialized version of the object 
 * and one containing only the status of the object. 
 * <br><br>
 * <i>Note!</i> The original (cronological) ordering of the Queue will not be preserved when restoring it from persistent storage 
 * using this queue storage implementation.
 *   
 * @see com.teletalk.jserver.queue.Queue
 * 
 * @author Tobias L�fstrand
 * 
 * @since Beta 1
 */
public final class SimpleFileQueueStorage extends ObjectPool implements QueueStorage {

    /** The QueueItem file suffix. */
    public static final String itemFileSuffix = ".qi";

    /** The QueueItem file suffix of old files. */
    public static final String oldItemFileSuffix = ".old";

    private static final String oldItemFileCompleteSuffix = itemFileSuffix + oldItemFileSuffix;

    private static final int maxAttempts = 3;

    private static final int attemptDelay = 100;

    private StringProperty defaultQueueItemFileNamePrefix;

    private final boolean[] existingSubdirs = new boolean[100];

    private boolean useAlternativeObjectOutputStreamResetMethod = false;

    private final java.io.FilenameFilter queueItemFileFilter = new java.io.FilenameFilter() {

        /**
			 * Method for building a filtered list of filenames.
			 * 
			 * @param dir the directory in which the file was found.
			 * @param name the name of the file.
			 * 
			 * @return true if and only if the name should be included in the file list; false otherwise.
			 */
        public boolean accept(File dir, String name) {
            return name.endsWith(itemFileSuffix);
        }
    };

    private final java.io.FilenameFilter oldQueueItemFileFilter = new java.io.FilenameFilter() {

        /**
			 * Method for building a filtered list of filenames.
			 * 
			 * @param dir the directory in which the file was found.
			 * @param name the name of the file.
			 * 
			 * @return true if and only if the name should be included in the file list; false otherwise.
			 */
        public boolean accept(File dir, String name) {
            return name.endsWith(oldItemFileCompleteSuffix);
        }
    };

    /**
	 * Creates a new SimpleFileQueueStorage object.
	 * 
	 * @param parent the parent of this SimpleFileQueueStorage.
	 */
    public SimpleFileQueueStorage(SubComponent parent) {
        this(parent, "SimpleFileQueueStorage");
    }

    /**
	 * Creates a new SimpleFileQueueStorage object.
	 * 
	 * @param parent the parent of this SimpleFileQueueStorage.
	 * @param name the name of this SimpleFileQueueStorage.
	 */
    public SimpleFileQueueStorage(SubComponent parent, String name) {
        super(parent, name, 100);
        defaultQueueItemFileNamePrefix = new StringProperty(this, "queueItemSavePath", "." + File.separator + parent.getName() + ".storage" + File.separator, StringProperty.MODIFIABLE_OWNER_RESTART);
        addProperty(defaultQueueItemFileNamePrefix);
        if (com.teletalk.jserver.util.JavaBugUtils.isUsingJava1_3_0()) {
            this.setUseAlternativeObjectOutputStreamResetMethod(true);
            logInfo("Defaulting flag for alternative object output stream reset method to true, because Java 2 version 1.3 is used.");
        }
    }

    /**
	 * Enables this SimpleFileQueueStorage by creating the storage directory and nessecary streams.
	 */
    public void doInitialize() {
        super.doInitialize();
        super.initFromConfiguredProperty(this.defaultQueueItemFileNamePrefix, "Queueitem savepath", false, true);
        if (this.isUsingAlternativeObjectOutputStreamResetMethod()) {
            logWarning("Note: this SimpleFileQueueStorage uses an alternative method for resetting the object output streams used when writing QueueItems to disk. This alternative method may be inefficient.");
        }
        File savePathDir = new File(defaultQueueItemFileNamePrefix.stringValue());
        savePathDir.mkdirs();
        if (!savePathDir.isDirectory() || !savePathDir.exists()) {
            throw new StatusTransitionException("Unable to access queue item save path ('" + defaultQueueItemFileNamePrefix.stringValue() + "')!");
        }
        final StringBuffer saveDir = new StringBuffer(defaultQueueItemFileNamePrefix.stringValue());
        int appendIndex = saveDir.length();
        String dirName = null;
        for (int i = 0; i < existingSubdirs.length; i++) {
            if (i >= 10) dirName = String.valueOf(i); else dirName = "0" + String.valueOf(i);
            if (new File(saveDir.replace(appendIndex, saveDir.length(), dirName).toString()).isDirectory()) {
                existingSubdirs[i] = true;
            } else {
                existingSubdirs[i] = false;
            }
        }
    }

    /**
	 * Sets the flag indicating if an alternative method should be used when resetting the object output streams 
	 * used when saving QueueItems to disk. This alternative reset method involvs creating a 
	 * new ObjectOutputStream object (specifically a {@link com.teletalk.jserver.util.NoHeadersObjectOutputStream} object) to reset 
	 * the stream.<br>
	 * <br>
	 * The foremost purpose of this alternative reset method is to correct a bug introduced in Java 2 version 1.3, which makes impossible 
	 * to reset an ObjectOutputStream object and consequently leads to a memory leak.<br>
	 * 
	 * @param useAlternativeResetMethod boolean flag indicating if the alternativ reset method should be used (<code>true</code>), or not (<code>false</code>).
	 */
    public final void setUseAlternativeObjectOutputStreamResetMethod(boolean useAlternativeResetMethod) {
        this.useAlternativeObjectOutputStreamResetMethod = useAlternativeResetMethod;
    }

    /**
	 * Checks if an alternative method will be used when resetting the object output streams 
	 * used when saving QueueItems to disk. This alternative reset method involvs creating a 
	 * new ObjectOutputStream object (specifically a {@link com.teletalk.jserver.util.NoHeadersObjectOutputStream} object) to reset 
	 * the stream.<br>
	 * <br>
	 * The foremost purpose of this alternative reset method is to correct a bug introduced in Java 2 version 1.3, which makes impossible 
	 * to reset an ObjectOutputStream object and consequently leads to a memory leak.<br>
	 * 
	 * @return <code>true</code> if the alternative reset method is currently used, otherwise <code>false</code>. 
	 */
    public final boolean isUsingAlternativeObjectOutputStreamResetMethod() {
        return useAlternativeObjectOutputStreamResetMethod;
    }

    /**
	 * Creates a new SimpleFileQueueStorageFileWriter object to be put in the pool.
	 * 
	 * @return the newly created SimpleFileQueueStorageFileWriter object.
	 * 
	 * @see SimpleFileQueueStorageFileWriter
	 */
    public Object createObject() throws Exception {
        return new SimpleFileQueueStorageFileWriter(this);
    }

    /**
	 * Validates a SimpleFileQueueStorageFileWriter object contained in the pool.
	 * 
	 * @param obj the SimpleFileQueueStorageFileWriter object to validate.
	 * 
	 * @return true if the object passed the validation, otherwise false.
	 * 
	 * @see SimpleFileQueueStorageFileWriter
	 */
    public boolean validateObject(Object obj, boolean cleanUpValidation) {
        return ((SimpleFileQueueStorageFileWriter) obj).validate();
    }

    /**
	 * Finalizes a SimpleFileQueueStorageFileWriter object.
	 * 
	 * @param obj a SimpleFileQueueStorageFileWriter object to be finalized.
	 * 
	 * @see SimpleFileQueueStorageFileWriter
	 */
    public void finalizeObject(Object obj) {
        ((SimpleFileQueueStorageFileWriter) obj).destroy();
        obj = null;
    }

    /**
	 * Called when the value of a property has been modified. This implementation makes sure that 
	 * the QueueItem savepath property ends with the system-dependent default name-separator character (File.separator).
	 * 
	 * @param property the Property that was changed.
	 */
    public void propertyModified(Property property) {
        if (property == defaultQueueItemFileNamePrefix) {
            String value = defaultQueueItemFileNamePrefix.stringValue();
            if (!(value.endsWith("/") || value.endsWith("\\"))) {
                defaultQueueItemFileNamePrefix.setNotificationMode(false);
                defaultQueueItemFileNamePrefix.setValue(value + File.separator);
                defaultQueueItemFileNamePrefix.setNotificationMode(true);
            }
        }
        super.propertyModified(property);
    }

    /**
	 * Validates a modification of a property's value. This implementation validates 
	 * the QueueItem savepath property (checkes if it is a directory).
	 * 
	 * @param property The property to be validated.
	 * 
	 * @return boolean value indicating if the property passed (true) validation or not (false).
	 */
    public boolean validatePropertyModification(Property property) {
        if (property == defaultQueueItemFileNamePrefix) {
            String value = defaultQueueItemFileNamePrefix.stringValue();
            if (!(value.endsWith("/") || value.endsWith("\\"))) value = value + File.separator;
            return true;
        } else return super.validatePropertyModification(property);
    }

    /**
	 * Gets the filename used for persistent storage of a specific QueueItem.
	 * 
	 * @param item a QueueItem to get a filename for.
	 * 
	 * @return a filename.
	 */
    public String getQueueItemFileName(final QueueItem item) throws QueueStorageException {
        final StringBuffer fileName = new StringBuffer();
        final String itemId = item.getId();
        String itemIdLast2 = null;
        boolean err = false;
        if (itemId.length() < 2) {
            err = true;
        } else {
            itemIdLast2 = itemId.substring(itemId.length() - 2);
            try {
                final short subDirNo = (short) (Short.parseShort(itemIdLast2, 16) % 100);
                if (subDirNo >= 10) itemIdLast2 = String.valueOf(subDirNo); else itemIdLast2 = "0" + String.valueOf(subDirNo);
                if (!existingSubdirs[subDirNo]) {
                    synchronized (existingSubdirs) {
                        if (!existingSubdirs[subDirNo]) {
                            new File(defaultQueueItemFileNamePrefix.getValueAsString() + itemIdLast2 + File.separator).mkdir();
                            existingSubdirs[subDirNo] = true;
                        }
                    }
                }
            } catch (Exception e) {
                err = true;
            }
        }
        if (err) {
            itemIdLast2 = "00";
            synchronized (existingSubdirs) {
                if (!existingSubdirs[0]) {
                    new File(defaultQueueItemFileNamePrefix.getValueAsString() + itemIdLast2 + File.separator).mkdir();
                    existingSubdirs[0] = true;
                }
            }
        }
        fileName.append(defaultQueueItemFileNamePrefix.getValueAsString());
        fileName.append(itemIdLast2);
        fileName.append(File.separator);
        fileName.append(itemId);
        fileName.append(itemFileSuffix);
        return fileName.toString();
    }

    /**
	 * Adds a QueueItem to persistant storage. This method stores each QueueItem in a single file.
	 * 
	 * @param item the QueueItem to be stored.
	 * 
	 * @exception QueueStorageException if an error occured during the store operation.
	 */
    public void storeQueueItem(final QueueItem item) throws QueueStorageException {
        final String queueItemFileName = getQueueItemFileName(item);
        final SimpleFileQueueStorageFileWriter writer = (SimpleFileQueueStorageFileWriter) this.checkOutWait();
        try {
            writer.store(item, queueItemFileName);
        } catch (IOException e) {
            throw new QueueStorageException("Error while storing QueueItem (" + item + ")", e);
        } finally {
            if (writer != null) this.checkIn(writer);
        }
    }

    /**
	 * Adds several QueueItems to persistent storage. This method stores each QueueItem in a single file.
	 * 
	 * @param items the QueueItems to be stored.
	 * 
	 * @exception QueueStorageException if an error occured during the store operation.
	 */
    public void storeQueueItems(QueueItem[] items) throws QueueStorageException {
        String queueItemFileName;
        final SimpleFileQueueStorageFileWriter writer = (SimpleFileQueueStorageFileWriter) this.checkOutWait();
        try {
            for (int i = 0; i < items.length; i++) {
                queueItemFileName = this.getQueueItemFileName(items[i]);
                writer.store(items[i], queueItemFileName);
            }
        } catch (IOException e) {
            throw new QueueStorageException("Error while storing QueueItems (" + QueueItem.concatIds(items) + ")", e);
        } finally {
            if (writer != null) this.checkIn(writer);
        }
    }

    /**
	 * Updates the persistent state of a previously stored QueueItem.
	 * 
	 * @param item the QueueItem to be updated.
	 * 
	 * @exception QueueStorageException if an error occured during the update operation.
	 */
    public void updateStoredQueueItem(final QueueItem item) throws QueueStorageException {
        storeQueueItem(item);
    }

    /**
	 * Method to reflect a change in the state (status value) of a QueueItem object on it's persistent counterpart. This method updates the file used to store the 
	 * specified QueueItem.
	 * 
	 * @param item the QueueItem to be stored.
	 * 
	 * @exception QueueStorageException if an error occured during the store operation.
	 */
    public void updateQueueItemStatus(final QueueItem item) throws QueueStorageException {
        final String queueItemFileName = getQueueItemFileName(item);
        final SimpleFileQueueStorageFileWriter writer = (SimpleFileQueueStorageFileWriter) this.checkOutWait();
        try {
            writer.updateStatus(item, queueItemFileName);
        } catch (IOException e) {
            throw new QueueStorageException("Error while updating QueueItem (" + item + ")", e);
        } finally {
            if (writer != null) this.checkIn(writer);
        }
    }

    /**
	 * Removes a QueueItem from persistant storage.
	 * 
	 * @param item the QueueItem to be removed from persistent storage.
	 */
    public void removeStoredQueueItem(final QueueItem item) {
        final String queueItemFileNameBase = getQueueItemFileName(item);
        final File itemFile = new File(queueItemFileNameBase);
        itemFile.delete();
    }

    /**
	 * Restores all stored QueueItem objects.
	 * 
	 * @return a vector containing QueueItem objects restored from persistent storage.
	 * 
	 * @exception QueueStorageException if an error occured during restoration of the stored QueueItem objects.
	 */
    public java.util.List restoreQueueFromStorage() throws QueueStorageException {
        java.util.ArrayList queueItems = new java.util.ArrayList();
        logInfo("Restoring QueueItems from persistent storage.");
        final StringBuffer baseDir = new StringBuffer(defaultQueueItemFileNamePrefix.getValueAsString());
        int appendIndex = baseDir.length();
        int appendIndex2;
        File subDir;
        String[] files = null;
        String[] oldFiles = null;
        QueueItem item;
        String currentFileName;
        String oldFileName;
        for (int i = 0; i < 100; i++) {
            if (existingSubdirs[i]) {
                baseDir.delete(appendIndex, baseDir.length());
                if (i < 10) baseDir.append("0" + String.valueOf(i)); else baseDir.append(String.valueOf(i));
                baseDir.append(File.separator);
                appendIndex2 = baseDir.length();
                subDir = new File(baseDir.toString());
                for (int attempt = 0; ((files == null) || (oldFiles == null)) && (attempt < maxAttempts); attempt++) {
                    files = subDir.list(queueItemFileFilter);
                    oldFiles = subDir.list(oldQueueItemFileFilter);
                    if ((files == null) || (oldFiles == null)) {
                        try {
                            Thread.sleep(attemptDelay);
                        } catch (InterruptedException ie) {
                        }
                    }
                }
                if ((files == null) || (oldFiles == null)) {
                    logError("Unable to list files in directory '" + defaultQueueItemFileNamePrefix.getValueAsString() + "'!");
                    break;
                }
                for (int f = 0; f < files.length; f++) {
                    baseDir.replace(appendIndex2, baseDir.length(), files[f]);
                    currentFileName = baseDir.toString();
                    int matchingOldItemIndex = -1;
                    item = null;
                    for (int o = 0; o < oldFiles.length; o++) {
                        if (oldFiles[o] != null) {
                            if (oldFiles[o].startsWith(files[f])) {
                                matchingOldItemIndex = o;
                                break;
                            }
                        }
                    }
                    if (matchingOldItemIndex >= 0) {
                        baseDir.replace(appendIndex2, baseDir.length(), oldFiles[matchingOldItemIndex]);
                        oldFileName = baseDir.toString();
                    } else oldFileName = null;
                    try {
                        item = readQueueItem(currentFileName);
                        if (matchingOldItemIndex >= 0) {
                            new File(oldFileName).delete();
                            oldFiles[matchingOldItemIndex] = null;
                        }
                    } catch (Exception e) {
                        if (matchingOldItemIndex >= 0) {
                            if (new File(currentFileName).renameTo(new File(currentFileName + ".err"))) {
                                logError("Error while restoring QueueItem from file (" + currentFileName + "). Renaming file to " + currentFileName + ".err" + ". Attempting to replace QueueItem with existing backup copy (" + oldFileName + "). Exception", e);
                            } else {
                                logError("Error while restoring QueueItem from file (" + currentFileName + "). Failed to rename file to " + currentFileName + ".err. Deleting file. Attempting to replace QueueItem with existing backup copy (" + oldFileName + "). Exception", e);
                                new File(currentFileName).delete();
                            }
                            try {
                                item = readQueueItem(oldFileName);
                                new File(oldFileName).renameTo(new File(currentFileName));
                            } catch (Exception e1) {
                                if (new File(oldFileName).renameTo(new File(oldFileName + ".err"))) {
                                    logError("Error while restoring QueueItem from file (" + oldFileName + "). Renaming file to " + oldFileName + ".err" + ".", e1);
                                } else {
                                    logError("Error while restoring QueueItem from file (" + oldFileName + "). Failed to rename file to " + oldFileName + ".err. Deleting file.", e1);
                                    new File(oldFileName).delete();
                                }
                            } finally {
                                oldFiles[matchingOldItemIndex] = null;
                            }
                        } else {
                            if (new File(currentFileName).renameTo(new File(currentFileName + ".err"))) {
                                logError("Error while restoring QueueItem from file (" + currentFileName + "). Renaming file to " + currentFileName + ".err" + ".", e);
                            } else {
                                logError("Error while restoring QueueItem from file (" + currentFileName + "). Failed to rename file to " + currentFileName + ".err. Deleting file.", e);
                                new File(currentFileName).delete();
                            }
                        }
                    } finally {
                        if (item != null) {
                            queueItems.add(item);
                            item.setRecoveredFromPersistentStorage(true);
                        }
                    }
                }
                for (int o = 0; o < oldFiles.length; o++) {
                    if (oldFiles[o] != null) {
                        baseDir.replace(appendIndex2, baseDir.length(), oldFiles[o]);
                        oldFileName = baseDir.toString();
                        try {
                            logWarning("Found single backup QueueItem file (" + oldFileName + "). Trying to restore.");
                            item = readQueueItem(oldFileName);
                            new File(oldFileName).renameTo(new File(oldFileName.substring(0, oldFileName.length() - oldItemFileSuffix.length())));
                            queueItems.add(item);
                            item.setRecoveredFromPersistentStorage(true);
                        } catch (Exception e) {
                            if (new File(oldFileName).renameTo(new File(oldFileName + ".err"))) {
                                logError("Error while restoring QueueItem from single backup file (" + oldFileName + "). Renaming file to " + oldFileName + ".err.", e);
                            } else {
                                logError("Error while restoring QueueItem from single backup file (" + oldFileName + "). Failed to rename file to " + oldFileName + ".err. Deleting file.", e);
                                new File(oldFileName).delete();
                            }
                        }
                    }
                }
                files = null;
                oldFiles = null;
            }
        }
        return queueItems;
    }

    /**
	 * Reads a QueueItem object from a file. This method uses doReadQueueItem to do the actual reading of the 
	 * file and makes it possible to make several attempts at reading. 
	 * 
	 * @param fileName the name of the file to read a QueueItem object from.
	 * 
	 * @return a QueueItem object.
	 */
    private QueueItem readQueueItem(String fileName) throws Exception {
        IOException error = null;
        QueueItem item = null;
        for (int attempt = 0; (item == null) && (attempt < maxAttempts); attempt++) {
            try {
                item = doReadQueueItem(fileName);
                error = null;
            } catch (IOException e) {
                item = null;
                error = e;
                try {
                    Thread.sleep(attemptDelay);
                } catch (InterruptedException ie) {
                }
            }
        }
        if (error != null) {
            throw error;
        } else {
            return item;
        }
    }

    /**
	 * Reads a QueueItem object from a file. This method does the actual reading of the file.
	 * 
	 * @param fileName the name of the file to read a QueueItem object from.
	 * 
	 * @return a QueueItem object.
	 */
    private QueueItem doReadQueueItem(String fileName) throws Exception {
        QueueItem item = null;
        ObjectInputStream objInput = null;
        byte[] objBytes = null;
        RandomAccessFile queueItemFile = null;
        short status;
        try {
            queueItemFile = new RandomAccessFile(fileName, "r");
            status = queueItemFile.readShort();
            objBytes = new byte[(int) (queueItemFile.length()) - 2];
            queueItemFile.readFully(objBytes);
            queueItemFile.close();
            queueItemFile = null;
            objInput = new NoHeadersObjectInputStream(new ByteArrayInputStream(objBytes));
            item = (QueueItem) objInput.readObject();
            item.forceStatus(status);
            objInput.close();
            objInput = null;
            return item;
        } finally {
            try {
                if (queueItemFile != null) queueItemFile.close();
            } catch (Exception ex1) {
            }
            try {
                if (objInput != null) objInput.close();
            } catch (Exception ex2) {
            }
        }
    }
}
