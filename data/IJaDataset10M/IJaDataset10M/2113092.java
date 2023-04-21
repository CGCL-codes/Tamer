package com.stfa.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import com.stfa.StFAException;
import com.stfa.cfg.StFAConfiguration;
import com.stfa.core.StFATransactionImpl.Context;

/**
 * 
 * @author J.Z
 *
 */
public class StFASessionImpl extends AbstractStFASession {

    /***********************************************************/
    protected void onSave(String name) throws StFAException {
        Context context = getTransactionContext();
        context.monitorCopyOrSave(null, new File(name));
    }

    protected void doSave(String name, InputStream data) throws StFAException {
        try {
            StFAIO.getInstance().save(name, data, getBufferSize());
        } catch (IOException e) {
            throw new StFAException(e);
        }
    }

    protected void doSave(String name, byte[][] b) throws StFAException {
        try {
            StFAIO.getInstance().save(name, b);
        } catch (IOException e) {
            throw new StFAException(e);
        }
    }

    protected void doSave(String name, byte[] b) throws StFAException {
        try {
            StFAIO.getInstance().save(name, b);
        } catch (IOException e) {
            throw new StFAException(e);
        }
    }

    /***********************************************************/
    protected void onCopy(File src, File dest) throws StFAException {
        Context context = getTransactionContext();
        context.monitorCopyOrSave(src, dest);
    }

    protected void doCopy(File src, File dest) throws StFAException {
        try {
            if (StFAIO.getInstance().isFile(src)) StFAIO.getInstance().cpFile(src, dest, true, getBufferSize()); else if (StFAIO.getInstance().isDir(src)) StFAIO.getInstance().cpDir(src, dest, true, true, getBufferSize());
        } catch (IOException e) {
            throw new StFAException(e);
        }
    }

    /***********************************************************/
    protected void onMove(File src, File dest) throws StFAException {
        Context context = getTransactionContext();
        context.monitorMove(src, dest);
    }

    protected void doMove(File src, File dest) throws StFAException {
        try {
            if (StFAIO.getInstance().isFile(src)) StFAIO.getInstance().mvFile(src, dest, true, getBufferSize()); else if (StFAIO.getInstance().isDir(src)) StFAIO.getInstance().mvDir(src, dest, true, true, getBufferSize());
        } catch (IOException e) {
            throw new StFAException(e);
        }
    }

    /***********************************************************/
    protected void onDelete(File name) throws StFAException {
        Context context = getTransactionContext();
        context.monitorDelete(name);
    }

    protected void doDelete(File name) throws StFAException {
        try {
            if (StFAIO.getInstance().isFile(name)) StFAIO.getInstance().rmFile(name); else if (StFAIO.getInstance().isDir(name)) StFAIO.getInstance().rmDir(name);
        } catch (IOException e) {
            throw new StFAException(e);
        }
    }

    private Context getTransactionContext() {
        return ((StFATransactionImpl) getTransaction()).getContext();
    }

    private int getBufferSize() {
        return Integer.valueOf(StFAPRA.get(StFAConfiguration.XKEY_BUFFER_SIZE).toString());
    }
}
