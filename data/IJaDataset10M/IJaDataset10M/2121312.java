package com.mytaobao.util;

import java.io.File;
import org.apache.log4j.helpers.LogLog;

/**
 * 文件看门狗, 能重复扫描指定文件,如果发现有所变动,则进行回调处理
 * 具体实现是在后台启动一线程不断检查指定文件最后修改时间
 * 这个类主要部门来自于 log4j 库
 * @author z3y2
 */
public abstract class FileWatchdog extends Thread {

    /**
	 * The default delay between every file modification check, set to 60
	 * seconds.
	 */
    public static final long DEFAULT_DELAY = 60000;

    /**
	 * The name of the file to observe for changes.
	 */
    protected String filename;

    /**
	 * The delay to observe between every check. By default set
	 * {@link #DEFAULT_DELAY}.
	 */
    protected long delay = DEFAULT_DELAY;

    File file;

    long lastModif = 0;

    boolean warnedAlready = false;

    boolean interrupted = false;

    protected FileWatchdog(String filename) {
        this.filename = filename;
        file = new File(filename);
        setDaemon(true);
        checkAndConfigure();
    }

    /**
	 * Set the delay to observe between each check of the file changes.
	 */
    public void setDelay(long delay) {
        this.delay = delay;
    }

    /**
	 * 由子类进行实现, 这种实现也可作为模板方法模式一实例
	 */
    protected abstract void doOnChange();

    protected void checkAndConfigure() {
        boolean fileExists;
        try {
            fileExists = file.exists();
        } catch (SecurityException e) {
            LogLog.warn("Was not allowed to read check file existance, file:[" + filename + "].");
            interrupted = true;
            return;
        }
        if (fileExists) {
            long l = file.lastModified();
            if (l > lastModif) {
                lastModif = l;
                doOnChange();
                warnedAlready = false;
            }
        } else {
            if (!warnedAlready) {
                LogLog.debug("[" + filename + "] does not exist.");
                warnedAlready = true;
            }
        }
    }

    public void run() {
        while (!interrupted) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
            }
            checkAndConfigure();
        }
    }
}
