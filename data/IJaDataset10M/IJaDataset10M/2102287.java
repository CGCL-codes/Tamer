package org.jmonks.dms.versioncontrol.ri;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import org.jmonks.dms.versioncontrol.api.Repository;
import org.jmonks.dms.versioncontrol.api.RepositoryEntry;

/**
 *
 * @author Suresh Pragada
 *
 * This is the implementation of Repository class in Version Control API. This class gives the entry point
 * to explore the Repository. It treats everything as directories and files. It treats the repository itself
 * a directory. It uses the configuration file with the extension .vc to store the details of each entry. 
 */
public class DefaultRepository extends DefaultDirectoryEntry implements Repository {

    private String repositoryRootPath = ".";

    private int cleanupInterval;

    static final String REPOSITORY_ROOT_PATH_PROPERTY_NAME = "dms.repository.path";

    static final String REPOSITORY_CLEANUP_INTERVAL = "dms.delete.interval";

    static final String REPOSITORY_CONFIG_FILE_EXTENSION = "vc";

    static final long REPOSITORY_ENTRY_ID = Long.MAX_VALUE;

    private static final Logger logger = Logger.getLogger(DefaultRepository.class);

    private static final DefaultRepository repository = new DefaultRepository();

    /** 
     * Creates a an instance of DefaultRepository and assigns the default directory as repository i.e., "."
     */
    private DefaultRepository() {
        Locale locale = Locale.getDefault();
        ResourceBundle bundle = ResourceBundle.getBundle("org.jmonks.dms.application", locale);
        try {
            repositoryRootPath = bundle.getString(DefaultRepository.REPOSITORY_ROOT_PATH_PROPERTY_NAME);
            File file = new File(repositoryRootPath);
            if (!file.exists()) {
                boolean created = file.mkdirs();
                if (!created) {
                    this.createRepositoryDirectoryInHome();
                }
            } else if (!file.isDirectory()) {
                this.createRepositoryDirectoryInHome();
            }
        } catch (java.util.MissingResourceException ex) {
            ex.printStackTrace();
            logger.error("DefaultRepository::DefaultRepository:" + ex.getMessage(), ex);
            logger.error("repository.path property couldnt found in application.properties file.");
            repositoryRootPath = System.getProperty("user.home") + File.separator + "repository";
            File repDir = new File(repositoryRootPath);
            repDir.mkdirs();
        }
        this.absoluteDirectoryName = repositoryRootPath;
        this.entryID = DefaultRepository.REPOSITORY_ENTRY_ID;
        this.parentEntryID = DefaultRepository.REPOSITORY_ENTRY_ID;
        this.entryName = "REPOSITORY";
    }

    private boolean createRepositoryDirectoryInHome() {
        repositoryRootPath = System.getProperty("user.home") + File.separator + "repository";
        File repDir = new File(repositoryRootPath);
        return repDir.mkdirs();
    }

    /**
     * Method which returns the singleton object.
     * @return  Returns the default repository object.
     */
    public static DefaultRepository getInstance() {
        return repository;
    }

    /**
     *@see  org.jmonks.dms.versioncontrol.api.Repository#getRepositoryEntry(long)
     */
    public RepositoryEntry getRepositoryEntry(long entryID) {
        if (entryID == DefaultRepository.REPOSITORY_ENTRY_ID) return DefaultRepository.getInstance();
        File configFile = getConfigFile(new File(this.absoluteDirectoryName), entryID + "." + DefaultRepository.REPOSITORY_CONFIG_FILE_EXTENSION);
        DefaultRepositoryEntry repositoryEntry = null;
        if (configFile != null) {
            repositoryEntry = DefaultRepositoryEntry.getRepositoryEntry(configFile);
        }
        return repositoryEntry;
    }

    /**
     *  Gets the configuration file with the name "fileName.vc" in given directory.
     *
     * @param   directory   Directory, where configuration file needs to be found.
     * @param   fileName    configuration filename to look for.
     *
     * @return  Return the file "fileName.vc" as file object.
     */
    private File getConfigFile(File directory, String fileName) {
        File[] fileList = directory.listFiles(new FileFilter() {

            public boolean accept(File file) {
                if (file.isFile() && file.getName().endsWith(DefaultRepository.REPOSITORY_CONFIG_FILE_EXTENSION)) return true; else if (file.isDirectory()) return true; else return false;
            }
        });
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isDirectory()) {
                File configFile = this.getConfigFile(fileList[i], fileName);
                if (configFile != null) return configFile; else continue;
            } else if (fileList[i].isFile() && fileList[i].getName().equals(fileName)) {
                return fileList[i];
            }
        }
        return null;
    }

    /**
     *  Generates the uniquie id to be used as a entry ID in Repository implementation. 
     *  This will look for the file ENTRYID.COUNTER in repository root path. 
     * 
     * @return  Returns the unique ID.
     */
    long generateEntryID() {
        synchronized (DefaultRepository.class) {
            String entryIDFilename = repositoryRootPath + File.separator + "ENTRYID.COUNTER";
            long returnValue = 0;
            try {
                RandomAccessFile file = new RandomAccessFile(new File(entryIDFilename), "rwd");
                if (file.length() != 0) {
                    returnValue = file.readLong() + 1;
                    file.seek(0);
                    file.writeLong(returnValue);
                } else {
                    returnValue = 1234;
                    file.writeLong(returnValue);
                }
                file.close();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
                logger.error("DefaultRepository::generateEntryID:" + ex.getMessage(), ex);
                returnValue = System.currentTimeMillis();
            } catch (IOException ex) {
                ex.printStackTrace();
                logger.error("DefaultRepository::generateEntryID:" + ex.getMessage(), ex);
                returnValue = System.currentTimeMillis();
            }
            return returnValue;
        }
    }

    /**
     *  @see org.jmonks.dms.versioncontrol.api.Repository#getEntryNameToShowInUI(RepositoryEntry)
     */
    public String getEntryNameToShowInUI(RepositoryEntry entry) {
        String entryNameToBeShowInUI = "";
        if (entry.getParentRepositoryEntry() == null) {
            entryNameToBeShowInUI = "REPOSITORY";
        } else if (entry instanceof DefaultDirectoryEntry) {
            entryNameToBeShowInUI = "REPOSITORY" + ((DefaultDirectoryEntry) entry).getAbsoluteDirectoryEntryName().substring(this.repositoryRootPath.length());
        } else if (entry instanceof DefaultFileEntry) {
            entryNameToBeShowInUI = "REPOSITORY" + ((DefaultFileEntry) entry).getAbsoluteFileName().substring(this.repositoryRootPath.length());
        } else entryNameToBeShowInUI = "";
        return entryNameToBeShowInUI;
    }

    /**
     *  @see org.jmonks.dms.versioncontrol.api.Repository#getCompleteDirectoryEntryList()
     */
    public Map getCompleteDirectoryEntryList() {
        Map entryMap = new HashMap();
        this.getCompleteDirectoryEntryList(this, entryMap);
        return entryMap;
    }

    private void getCompleteDirectoryEntryList(DefaultDirectoryEntry directoryEntry, Map entryMap) {
        List entryList = directoryEntry.getAllEntries();
        for (Iterator iterator = entryList.iterator(); iterator.hasNext(); ) {
            RepositoryEntry entry = (RepositoryEntry) iterator.next();
            if (entry instanceof DefaultDirectoryEntry && !entry.isDeleted()) {
                DefaultDirectoryEntry childDirectoryEntry = (DefaultDirectoryEntry) entry;
                String entryName = childDirectoryEntry.getAbsoluteDirectoryEntryName();
                entryName = "REPOSITORY" + entryName.substring(this.repositoryRootPath.length());
                entryMap.put(childDirectoryEntry.getEntryID() + "", entryName);
                this.getCompleteDirectoryEntryList(childDirectoryEntry, entryMap);
            }
        }
    }

    /**
     *  @see org.jmonks.dms.versioncontrol.api.Repository#cleanupRepository()
     */
    public boolean cleanupRepository() {
        boolean doneCleanup = true;
        try {
            Locale locale = Locale.getDefault();
            ResourceBundle bundle = ResourceBundle.getBundle("org.jmonks.dms.application", locale);
            this.cleanupInterval = new Integer(bundle.getString(DefaultRepository.REPOSITORY_CLEANUP_INTERVAL)).intValue();
            this.cleanupDeletedEntries(this);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("DefaultRepository::cleanupRepository:Failed to cleanup the deleted entries : " + e.getMessage(), e);
            doneCleanup = false;
        }
        return doneCleanup;
    }

    private void cleanupDeletedEntries(DefaultRepositoryEntry entry) throws Exception {
        if (entry.isDeleted()) {
            int interval = this.cleanupInterval * 86400000;
            if ((new Date().getTime() - entry.getDeletedDate().getTime()) > interval) entry.remove(); else logger.info("Entry " + entry.toString() + " not deleted : " + interval + " " + new Date().getTime() + " " + entry.getDeletedDate().getTime());
        } else if (entry instanceof DefaultDirectoryEntry) {
            List entryList = ((DefaultDirectoryEntry) entry).getAllEntries();
            for (Iterator iterator = entryList.iterator(); iterator.hasNext(); ) {
                DefaultRepositoryEntry listEntry = (DefaultRepositoryEntry) iterator.next();
                cleanupDeletedEntries(listEntry);
            }
        }
    }

    /**
     *  @see org.jmonks.dms.versioncontrol.api.Repository#moveEntry(RepositoryEntry,RepositoryEntry)
     */
    public boolean moveEntry(RepositoryEntry sourceEntry, RepositoryEntry targetEntry) throws IllegalArgumentException {
        boolean moved = true;
        if (targetEntry instanceof DefaultFileEntry) throw new IllegalArgumentException("TargetEntry cannot be a file entry.");
        DefaultDirectoryEntry directoryEntry = (DefaultDirectoryEntry) targetEntry;
        moved = directoryEntry.moveEntry((DefaultRepositoryEntry) sourceEntry);
        return moved;
    }

    /**
     *  @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer retValue = new StringBuffer("{ Default Repository ");
        retValue.append(" [Repository Root Path : " + this.repositoryRootPath + " ]}");
        return retValue.toString();
    }
}
