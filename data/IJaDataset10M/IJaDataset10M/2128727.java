package com.magicpwd._comn.apps;

import com.magicpwd.__i.lock.ILockServer;

/**
 *
 * @author Amon
 */
public class FileLocker extends Thread implements ILockServer {

    private java.io.File file;

    private java.nio.channels.FileChannel fc;

    private java.nio.channels.FileLock fl;

    public FileLocker() {
    }

    public FileLocker(java.io.File file) {
        this.file = file;
    }

    @Override
    public boolean tryLock() {
        if (file == null) {
            return false;
        }
        if (file.exists()) {
            file.delete();
        }
        try {
            fc = new java.io.RandomAccessFile(file, "rw").getChannel();
            fl = fc.tryLock();
            if (fl == null) {
                fc.close();
                return false;
            }
            Runtime.getRuntime().addShutdownHook(this);
            return true;
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        }
    }

    @Override
    public void run() {
        if (fl != null) {
            try {
                fl.release();
                fc.close();
                file.delete();
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
    }

    /**
     * @return the file
     */
    public java.io.File getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(java.io.File file) {
        this.file = file;
    }
}
