package com.application.areca.impl.policy;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import com.application.areca.ApplicationException;
import com.application.areca.ArchiveMedium;
import com.application.areca.ArecaTechnicalConfiguration;
import com.myJava.configuration.FrameworkConfiguration;
import com.myJava.file.FileNameUtil;
import com.myJava.file.FileSystemManager;
import com.myJava.file.driver.remote.AbstractProxy;
import com.myJava.file.driver.remote.FictiveFile;
import com.myJava.file.driver.remote.RemoteFileInfoCache;
import com.myJava.system.OSTool;
import com.myJava.util.log.Logger;

public abstract class AbstractRemoteFileSystemPolicy extends AbstractFileSystemPolicy implements FileSystemPolicy {

    public static final String STORAGE_DIRECTORY_PREFIX = "storage_";

    protected static final boolean CACHE = ArecaTechnicalConfiguration.get().isRepositoryFTPCache();

    protected static final int MAX_RETRIES = ArecaTechnicalConfiguration.get().getMaxFTPRetries();

    protected static final int CACHE_DEPTH = ArecaTechnicalConfiguration.get().getRepositoryFTPCacheDepth();

    protected static final String LOCAL_DIR_PREFIX;

    protected String remoteDirectory;

    protected ArchiveMedium medium;

    static {
        String prefix;
        if (OSTool.isSystemWindows()) {
            prefix = "C:\\areca_rmt";
        } else {
            prefix = "/areca_rmt";
        }
        String tg = prefix;
        int n = 0;
        while (FileSystemManager.exists(new File(tg))) {
            tg = prefix + n++;
        }
        if (OSTool.isSystemWindows()) {
            tg += "\\";
        } else {
            tg += "/";
        }
        LOCAL_DIR_PREFIX = tg;
    }

    public void synchronizeConfiguration() {
    }

    public void validate(boolean extendedTests) throws ApplicationException {
        if (extendedTests) {
            validateExtended();
        } else {
            validateSimple();
        }
    }

    public int getMaxRetries() {
        return MAX_RETRIES;
    }

    public boolean retrySupported() {
        return true;
    }

    public ArchiveMedium getMedium() {
        return medium;
    }

    public void setMedium(ArchiveMedium medium) {
        this.medium = medium;
    }

    public void validateSimple() throws ApplicationException {
        AbstractProxy px = buildProxy();
        px.setFileInfoCache(new RemoteFileInfoCache());
        try {
            px.acquireLock("Basic connection test.");
            px.connect();
        } catch (Exception e) {
            Logger.defaultLogger().error(e);
            throw new ApplicationException("Invalid Parameters - Got the following error : \n" + e.getMessage());
        } finally {
            px.disconnect();
        }
    }

    public void validateExtended() throws ApplicationException {
        AbstractProxy px = buildProxy();
        px.setFileInfoCache(new RemoteFileInfoCache());
        Logger.defaultLogger().info("Testing policy : " + this.toString() + " ...");
        int maxProxies = FrameworkConfiguration.getInstance().getMaxFTPProxies() - 1;
        Logger.defaultLogger().info("Making tests with " + maxProxies + " concurrent connections ...");
        AbstractProxy[] pxs = new AbstractProxy[maxProxies];
        long[] cnxIds = new long[maxProxies];
        for (int i = 0; i < pxs.length; i++) {
            pxs[i] = (AbstractProxy) px.cloneProxy();
            pxs[i].acquireLock("Proxy Test #" + i);
        }
        try {
            Logger.defaultLogger().info("Connecting proxies ...");
            for (int i = 0; i < pxs.length; i++) {
                pxs[i].connect();
                cnxIds[i] = pxs[i].getConnectionId();
            }
            for (int i = 0; i < pxs.length; i++) {
                Logger.defaultLogger().info("*** Testing Proxy #" + i + " ***");
                testProxy(pxs[i]);
                Logger.defaultLogger().info("Proxy #" + i + " OK.");
            }
            Logger.defaultLogger().info("Connection test successfull.");
        } catch (Throwable e) {
            Logger.defaultLogger().error("Error during connextion test procedure", e);
            if (e instanceof ApplicationException) {
                throw (ApplicationException) e;
            } else {
                throw new ApplicationException(e.getMessage());
            }
        } finally {
            String msg = null;
            for (int i = 0; i < maxProxies; i++) {
                try {
                    pxs[i].disconnect();
                } catch (Exception ex) {
                    msg = ex.getMessage();
                }
            }
            if (msg != null) {
                throw new ApplicationException("Error during disconnection : " + msg);
            }
        }
    }

    private void testProxy(AbstractProxy px) throws Throwable {
        Logger.defaultLogger().info("Getting remote directory informations ...");
        FictiveFile file = px.getRemoteFileInfos(remoteDirectory);
        Logger.defaultLogger().info("Remote Directory : " + file.toString());
        if (!file.exists()) {
            Logger.defaultLogger().error("Remote directory does not exist.", "AbstractRemoteFileSystemPolicy.validate()");
            throw new ApplicationException("Remote directory does not exist");
        }
        String subdir = this.remoteDirectory;
        if (!FileNameUtil.endsWithSeparator(subdir)) {
            subdir += "/";
        }
        subdir += "areca_cnx_tst";
        String testFile = subdir + "/TEST_FILE";
        String fileContent = "This is a temporary file generated by Areca during the test procedure.";
        Logger.defaultLogger().info("Testing subdirectory creation ...");
        px.mkdir(subdir);
        FictiveFile sd = px.getRemoteFileInfos(subdir);
        if (!sd.exists()) {
            throw new ApplicationException("Invalid Server : Unable to create subdirectories.");
        }
        Logger.defaultLogger().info("Testing file creation ...");
        AbstractProxy pxClone = (AbstractProxy) px.cloneProxy();
        try {
            pxClone.acquireLock("outputStream");
            Logger.defaultLogger().info("Writing file : " + testFile + " ...");
            OutputStream out = pxClone.getFileOutputStream(testFile);
            out.write(fileContent.getBytes());
            out.flush();
            px.getRemoteFileInfos(testFile);
            px.getRemoteFileInfos(testFile + ".non_existing");
            out.write(fileContent.getBytes());
            out.flush();
            out.close();
            Logger.defaultLogger().info("File written.");
        } finally {
            pxClone.disconnect();
        }
        sd = px.getRemoteFileInfos(testFile);
        if (!sd.exists()) {
            throw new ApplicationException("Invalid Server : Unable to create files.");
        }
        Logger.defaultLogger().info("Testing file reading ...");
        pxClone = (AbstractProxy) px.cloneProxy();
        try {
            pxClone.acquireLock("inputStream");
            BufferedReader reader = new BufferedReader(new InputStreamReader(pxClone.getFileInputStream(testFile)));
            String result = reader.readLine();
            reader.close();
            if (result == null || (!result.equals(fileContent + fileContent))) {
                Logger.defaultLogger().error("Invalid Server : Unable to read files : [" + result + "] was read instead of [" + fileContent + fileContent + "]", "AbstractRemoteFileSystemPolicy.validate()");
                throw new ApplicationException("Invalid Server : Unable to read or create files.");
            }
        } finally {
            pxClone.disconnect();
        }
        Logger.defaultLogger().info("Testing file deletion ...");
        px.deleteFile(testFile);
        sd = px.getRemoteFileInfos(testFile);
        if (sd.exists()) {
            throw new ApplicationException("Invalid Server : Unable to delete created files.");
        }
        Logger.defaultLogger().info("Testing subdirectory deletion ...");
        px.deleteDir(subdir);
        sd = px.getRemoteFileInfos(subdir);
        if (sd.exists()) {
            throw new ApplicationException("Invalid Server : Unable to delete created directories.");
        }
    }

    public String getArchivePath() {
        return LOCAL_DIR_PREFIX + getUid() + "/" + STORAGE_DIRECTORY_PREFIX + getUid() + "/";
    }

    protected File getLocalDirectory() {
        return new File(LOCAL_DIR_PREFIX + getUid());
    }

    protected abstract AbstractProxy buildProxy();

    public void copyAttributes(AbstractRemoteFileSystemPolicy policy) {
        super.copyAttributes(policy);
        policy.setRemoteDirectory(this.remoteDirectory);
    }

    public String getRemoteDirectory() {
        return remoteDirectory;
    }

    public void setRemoteDirectory(String remoteDirectory) {
        this.remoteDirectory = remoteDirectory;
    }

    public String getUid() {
        return medium.getTarget().getUid();
    }
}
