package org.spark.util.concurrent;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.channels.FileLock;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spark.Identifiable;
import org.spark.util.RuntimeUtils;

public class FileLockExecutor extends LockExecutor {

    private static final Logger LOG = LoggerFactory.getLogger(FileLockExecutor.class);

    ThreadLocal<FileOutputStream> ostreamContext = new ThreadLocal<FileOutputStream>();

    ThreadLocal<FileLock> fileLockContext = new ThreadLocal<FileLock>();

    String fileLockDir = RuntimeUtils.JAVA_IO_TEMPDIR;

    public String getFileLockDir() {
        return fileLockDir;
    }

    public void setFileLockDir(String fileLockDir) {
        this.fileLockDir = fileLockDir;
        File lockDir = new File(fileLockDir);
        if (!lockDir.exists()) lockDir.mkdirs();
    }

    void closeQuitely(FileLock fileLock) {
        try {
            fileLock.release();
        } catch (Throwable err) {
        }
        try {
            fileLock.channel().close();
        } catch (Throwable err) {
        }
    }

    FileLock lock(String id) {
        String lockFileName = fileLockDir + "/" + id + ".lock.tmp";
        FileOutputStream ostream = null;
        FileLock fileLock = null;
        try {
            ostream = new FileOutputStream(lockFileName);
            ostreamContext.set(ostream);
            fileLock = ostream.getChannel().lock();
            return fileLock;
        } catch (Throwable err) {
            LOG.error("Failed to get lock " + lockFileName, err);
            return null;
        } finally {
            if (fileLock == null) IOUtils.closeQuietly(ostream);
        }
    }

    void release() {
        FileLock fileLock = fileLockContext.get();
        if (fileLock != null) closeQuitely(fileLock);
        fileLockContext.set(null);
        FileOutputStream ostream = ostreamContext.get();
        if (ostream != null) {
            IOUtils.closeQuietly(ostream);
            ostreamContext.set(null);
        }
    }

    public void execute(Runnable command) {
        if (command instanceof Identifiable) {
            FileLock fileLock = fileLockContext.get();
            boolean lockByOther = (fileLock != null);
            try {
                if (fileLock == null) {
                    fileLock = lock(((Identifiable) command).getId());
                    if (fileLock != null && fileLock.isValid()) fileLockContext.set(fileLock);
                }
                if (fileLock != null && fileLock.isValid()) command.run(); else LOG.error("Failed to get lock for " + ((Identifiable) command).getId());
            } finally {
                if (!lockByOther) release();
            }
        } else super.execute(command);
    }
}
