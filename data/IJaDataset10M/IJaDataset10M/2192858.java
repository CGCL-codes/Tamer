package org.dcm4chex.archive.hsm.module.dicey;

import java.io.File;
import java.io.IOException;
import org.apache.log4j.*;
import org.dcm4chex.archive.hsm.module.dicey.DiceyFSModule;
import org.dcm4chex.archive.hsm.module.dicey.FileIOTimeOut;
import org.dcm4chex.archive.hsm.module.HSMException;
import org.dcm4chex.archive.hsm.module.HSMFileBasedModule;
import org.dcm4chex.archive.util.FileUtils;

public class DiceyFSModule extends HSMFileBasedModule {

    private static final Logger log = Logger.getLogger(DiceyFSModule.class);

    private File incomingDir;

    private File absIncomingDir;

    private int readTimeout;

    public final String getIncomingDir() {
        return incomingDir.getPath();
    }

    public final void setIncomingDir(String dir) {
        this.incomingDir = new File(dir);
        this.absIncomingDir = FileUtils.resolve(this.incomingDir);
    }

    public final int getReadTimeout() {
        return readTimeout;
    }

    public final void setReadTimeout(int to) {
        this.readTimeout = to;
    }

    @Override
    protected void checkMount(final String fsID) throws HSMException {
        final Boolean[] notMounted = new Boolean[] { null };
        Thread t = new Thread() {

            public void run() {
                notMounted[0] = FileUtils.toFile(stripTarIdentifier(fsID), getMountFailedCheckFile()).exists();
            }
        };
        t.start();
        try {
            t.join(readTimeout * 1000);
        } catch (InterruptedException e) {
            log.warn("checkMount thread interrupted!", e);
        }
        if (notMounted[0] == null) {
            t.interrupt();
            log.warn("Accessibility of " + fsID + " seems broken! Timeout during mount check!");
            throw new HSMException("Filesystem accessibility broken! fsID:" + fsID);
        } else if (notMounted[0]) {
            log.warn("Mount on " + fsID + " seems broken! mountFailedCheckFile file exists:" + getMountFailedCheckFile());
            throw new HSMException("Filesystem not mounted! fsID:" + fsID);
        }
    }

    @Override
    public File fetchHSMFile(String fsID, String filePath) throws HSMException {
        checkMount(fsID);
        if (absIncomingDir.mkdirs()) {
            log.info("M-WRITE " + absIncomingDir);
        }
        File tarFile;
        File fileToFetch;
        try {
            tarFile = File.createTempFile("hsm_", ".tar", absIncomingDir);
        } catch (IOException x) {
            throw new HSMException("Failed to create temp file in " + absIncomingDir, x);
        }
        fileToFetch = FileUtils.toFile(stripTarIdentifier(fsID), filePath);
        try {
            FileIOTimeOut.copy(fileToFetch, tarFile, readTimeout);
        } catch (IOException x) {
            throw new HSMException("Failed to retrieve " + fileToFetch, x);
        }
        return tarFile;
    }

    @Override
    public void fetchHSMFileFinished(String fsID, String filePath, File file) throws HSMException {
        log.info("M-DELETE " + file);
        if (!file.delete()) log.warn("Deletion failed:" + file);
    }
}
