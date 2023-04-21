package com.myJava.system;

import java.util.ArrayList;
import java.util.Iterator;
import com.myJava.util.log.Logger;

public abstract class AbstractLauncher {

    public static final int ERR_UNEXPECTED = 1;

    public static final int ERR_SYNTAX = 2;

    public static final int ERR_INVALID_ARCHIVE = 3;

    private int errorCode = 0;

    private ArrayList closeCallBacks = new ArrayList();

    public void launch(String[] args) {
        try {
            initialize();
            checkJavaVersion();
            launchImpl(args);
        } catch (Throwable e) {
            e.printStackTrace();
            Logger.defaultLogger().error("Unexpected error", e);
            setErrorCode(ERR_UNEXPECTED);
        } finally {
            exit();
        }
    }

    public void exit() {
        exit(false);
    }

    public void exit(boolean force) {
        Iterator iter = closeCallBacks.iterator();
        while (iter.hasNext()) {
            Runnable rn = (Runnable) iter.next();
            rn.run();
        }
        if (returnErrorCode() || force) {
            System.exit(errorCode);
        }
    }

    public void addCloseCallBack(Runnable rn) {
        this.closeCallBacks.add(rn);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    protected abstract boolean returnErrorCode();

    protected abstract void initialize();

    protected abstract void launchImpl(String[] args);

    protected abstract void checkJavaVersion();
}
