package com.application.areca.impl.policy;

import java.io.File;
import com.application.areca.ApplicationException;
import com.application.areca.ArchiveMedium;
import com.application.areca.ArecaTechnicalConfiguration;
import com.myJava.file.FileSystemManager;
import com.myJava.file.driver.DefaultFileSystemDriver;
import com.myJava.file.driver.FileSystemDriver;
import com.myJava.file.driver.cache.CachedFileSystemDriver;
import com.myJava.object.Duplicable;
import com.myJava.object.ToStringHelper;
import com.myJava.util.log.Logger;

public class DefaultFileSystemPolicy extends AbstractFileSystemPolicy implements FileSystemPolicy {

    private static final boolean CACHE = ArecaTechnicalConfiguration.get().isRepositoryHDCache();

    private static final int CACHE_DEPTH = ArecaTechnicalConfiguration.get().getRepositoryHDCacheDepth();

    /**
     * Storage path
     */
    protected String archivePath;

    protected ArchiveMedium medium;

    public void validate(boolean extendedTests) throws ApplicationException {
    }

    public FileSystemDriver initFileSystemDriver() throws ApplicationException {
        FileSystemDriver base = new DefaultFileSystemDriver();
        if (CACHE) {
            File storageDir = getArchiveDirectory();
            return new CachedFileSystemDriver(base, FileSystemManager.getParentFile(storageDir), CACHE_DEPTH);
        } else {
            return base;
        }
    }

    public int getMaxRetries() {
        return 0;
    }

    public boolean retrySupported() {
        return false;
    }

    public String getArchivePath() {
        return this.archivePath;
    }

    public void setArchivePath(String archivePath) {
        this.archivePath = archivePath;
    }

    public void copyAttributes(DefaultFileSystemPolicy policy) {
        super.copyAttributes(policy);
        policy.setArchivePath(archivePath);
    }

    public Duplicable duplicate() {
        DefaultFileSystemPolicy policy = new DefaultFileSystemPolicy();
        copyAttributes(policy);
        return policy;
    }

    public String getDisplayableParameters(boolean fullPath) {
        File tmpF = getArchiveDirectory();
        File mainStorageDirectory = FileSystemManager.getParentFile(tmpF);
        File ret;
        if (mainStorageDirectory == null || fullPath) {
            ret = tmpF;
        } else {
            ret = mainStorageDirectory;
        }
        return FileSystemManager.getAbsolutePath(ret);
    }

    public String toString() {
        StringBuffer sb = ToStringHelper.init(this);
        ToStringHelper.append("Path", this.archivePath, sb);
        ToStringHelper.append("Name", this.archiveName, sb);
        return ToStringHelper.close(sb);
    }

    public ArchiveMedium getMedium() {
        return medium;
    }

    public void setMedium(ArchiveMedium medium) {
        this.medium = medium;
    }

    public void synchronizeConfiguration() {
        File archiveStorageDirectory = getArchiveDirectory();
        File rootDirectory = null;
        if (!FileSystemManager.getInstance().isRoot(archiveStorageDirectory)) {
            rootDirectory = FileSystemManager.getParentFile(archiveStorageDirectory);
        } else {
            Logger.defaultLogger().warn("Inconsistent storage directory : " + archivePath, "DefaultFileSystemPolicy.synchronizeConfiguration()");
            rootDirectory = archiveStorageDirectory;
        }
        File newStorageDirectory = new File(rootDirectory, getMedium().getTarget().getUid());
        this.archivePath = FileSystemManager.getAbsolutePath(newStorageDirectory);
    }
}
