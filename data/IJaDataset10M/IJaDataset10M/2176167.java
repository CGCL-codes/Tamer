package com.duroty.task;

import com.duroty.jmx.mbean.ApplicationConstants;
import com.duroty.jmx.mbean.Constants;
import com.duroty.lucene.bookmark.LuceneBookmarkContants;
import com.duroty.lucene.bookmark.indexer.BookmarkIndexerConstants;
import com.duroty.lucene.utils.FileUtilities;
import com.duroty.service.BookmarkOptimizerThread;
import com.duroty.service.Servible;
import com.duroty.utils.log.DLog;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.jboss.varia.scheduler.Schedulable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class BookmarkOptimizerTask implements Schedulable, LuceneBookmarkContants, Servible, BookmarkIndexerConstants {

    /**
    * DOCUMENT ME!
    */
    private static final List pool = new ArrayList(10);

    /**
     * DOCUMENT ME!
     */
    private Context ctx = null;

    /**
     * DOCUMENT ME!
     */
    private boolean init = false;

    /**
     * DOCUMENT ME!
     */
    private int poolSize = 10;

    /**
     * DOCUMENT ME!
     */
    private Analyzer analyzer;

    /**
     * DOCUMENT ME!
     */
    private String defaultLucenePath;

    /**
     * DOCUMENT ME!
     */
    private String tempDir;

    /**
     * Creates a new BookmarkOptimizerTask object.
     * @throws ClassNotFoundException
     * @throws NamingException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws IOException
     */
    public BookmarkOptimizerTask(int poolSize) throws ClassNotFoundException, NamingException, InstantiationException, IllegalAccessException, IOException {
        super();
        this.poolSize = poolSize;
        Map options = ApplicationConstants.options;
        try {
            ctx = new InitialContext();
            HashMap bookmark = (HashMap) ctx.lookup((String) options.get(Constants.BOOKMARK_CONFIG));
            this.defaultLucenePath = (String) bookmark.get(Constants.BOOKMARK_LUCENE_PATH);
            this.tempDir = System.getProperty("java.io.tmpdir");
            if (!this.tempDir.endsWith(File.separator)) {
                this.tempDir = this.tempDir + File.separator;
            }
            FileUtilities.deleteMotLocks(new File(this.tempDir));
            FileUtilities.deleteLuceneLocks(new File(this.tempDir));
            String clazzAnalyzerName = (String) bookmark.get(Constants.BOOKMARK_LUCENE_ANALYZER);
            if ((clazzAnalyzerName != null) && !clazzAnalyzerName.trim().equals("")) {
                Class clazz = null;
                clazz = Class.forName(clazzAnalyzerName.trim());
                this.analyzer = (Analyzer) clazz.newInstance();
            } else {
                this.analyzer = new StandardAnalyzer();
            }
        } finally {
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg0 DOCUMENT ME!
     * @param arg1 DOCUMENT ME!
     */
    public void perform(Date arg0, long arg1) {
        if (isInit()) {
            DLog.log(DLog.DEBUG, this.getClass(), "BookmarkOptimizerTask is running and wait.");
            return;
        }
        File dirLucene = new File(defaultLucenePath);
        File[] dirUsers = dirLucene.listFiles();
        if (dirUsers != null) {
            try {
                setInit(true);
                flush(dirUsers);
            } catch (Exception e) {
                DLog.log(DLog.ERROR, this.getClass(), e);
                pool.clear();
            } finally {
                setInit(false);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param dirUsers DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void flush(File[] dirUsers) throws Exception {
        for (int i = 0; i < dirUsers.length; i++) {
            if (pool.size() >= this.poolSize) {
                break;
            }
            try {
                Thread.sleep(500);
            } catch (Exception e) {
            }
            DLog.log(DLog.DEBUG, this.getClass(), "BookmarkOptimizerTask:" + pool.size() + "/" + this.poolSize);
            File dirUser = dirUsers[i];
            String userPath = dirUser.getPath();
            String simplePath = userPath + File.separator + BOOKMARKS + File.separator + SIMPLE_PATH_NAME;
            String key = userPath + File.separator + BOOKMARKS + File.separator + OPTIMIZED_PATH_NAME;
            File lock = new File(this.tempDir + userPath.substring(userPath.lastIndexOf(File.separator) + 1, userPath.length()) + "bot.lock");
            if (!poolContains(key) && !lock.exists()) {
                lock.createNewFile();
                addPool(key);
                if (!userPath.endsWith(File.separator)) {
                    userPath = userPath + File.separator;
                }
                BookmarkOptimizerThread bot = new BookmarkOptimizerThread(this, lock.getPath(), userPath, simplePath, key, analyzer);
                Thread thread = new Thread(bot);
                thread.start();
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     */
    public void addPool(String key) {
        pool.add(key);
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     */
    public void removePool(String key) {
        pool.remove(key);
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean poolContains(String key) {
        return pool.contains(key);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public synchronized boolean isInit() {
        synchronized (BookmarkOptimizerTask.class) {
            return this.init;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param init DOCUMENT ME!
     */
    public synchronized void setInit(boolean init) {
        synchronized (BookmarkOptimizerTask.class) {
            this.init = init;
        }
    }
}
