package org.docsfree.core;

import java.util.*;
import com.extentech.ExtenBean.DataObject;
import com.extentech.ExtenBean.PersistenceEngine;
import com.extentech.ExtenXLS.DocumentHandle;
import org.docsfree.legacy.auth.User;
import org.docsfree.plugin.*;
import org.docsfree.plugin.integration.*;
import com.extentech.ExtenXLS.web.*;
import com.extentech.security.*;
import com.extentech.comm.*;
import com.extentech.toolkit.Logger;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

/** ------------------------------------------------------------
 * DocumentLoader.java
 * 
 * NOTE: there is now one DocumentLoader per doc meme type, to allow clean handling of
 * mime type implementations.  All docs are now implementing DocumentHandle or MemeDocument
 * at this point.
 * 
 * DocumentLoader is an servlet-scoped object that handles loading, 
 * unloading and delivery of Spreadsheet data in various formats.
 * 
 * DocumentLoader stores all Document that are in the instance of luminet.
 * Lookup is based off the memeid of the workbook.
 * 
 * New Documents get a meme id on creation, which means that we are inserting
 * into the database.  If the book is never saved we will use the garbage
 * collection feature to remove from the database;
 *
 *
 * 1. New books/imported books = insert into db, get a real id
 * 2. We just delete workbook during the "reaping" if it has not been saved
 *
 * serve.getAttribute("WorKbookLoader").get(meme_id)
 *
 * DocumentLoader implements the PersistenceEngine interface.  Since WebDocuments and other
 * documents are DataObjects, we can handle persistence-layer Storage implementations here
 * so that WebDocuments and WebDocuments can "load()" and "store()" without knowledge of underlying
 * persistence details.
 * 
 */
public class DocumentLoader implements PersistenceEngine {

    private ServletContext context;

    private Connection conn;

    private HashMap sessionWorkbooks = new HashMap();

    private HashMap cachedWorkbooks = new HashMap();

    private HashMap workbookTimer = new HashMap();

    private HashMap guidLookup = new HashMap();

    private String service_type = "workbook";

    /** The name of the init parameter that contains the provider prefix. */
    private static final String PROVIDER_PROP = "org.docsfree.legacy.provider";

    /**
	 * @param serviceType The service_type to set.
	 */
    public void setServiceType(String serviceType) {
        service_type = serviceType;
    }

    private int workbookTimeoutMs = 100000;

    private boolean storeAsXML = true;

    private boolean storeBookAsFile = false;

    private Storage storage = null;

    /**
     * Initialize the DocumentLoader with the necessary serve context
     * 
     * @param svx the serve context
     * @param the service type whether doc, workbook or presentation
     */
    public DocumentLoader(ServletContext svx, String servicetype) {
        context = svx;
        service_type = servicetype;
        WorkBookReaper reaper = new WorkBookReaper(this);
        this.runInBackgroundThread(reaper);
        Connection dbcon = DatabaseManager.getConnectionNoX(svx);
        this.setConnection(dbcon);
        String storageType = "sheetster";
        if (context.getInitParameter(PROVIDER_PROP) != null) storageType = context.getInitParameter(PROVIDER_PROP);
        if (storageType.equalsIgnoreCase("sheetster")) {
            Auth storageAuth = new SheetsterAuth();
            storage = new SheetsterLocalStorage();
            storage.setAuth(storageAuth);
            ((SheetsterLocalStorage) storage).setConn(dbcon);
            ((SheetsterLocalStorage) storage).setServe(svx);
        } else {
            String aname = storageType + "Auth";
            Auth storageAuth = null;
            try {
                storageAuth = (Auth) Class.forName(aname).newInstance();
                Logger.logInfo("Custom Auth Loading for: " + aname);
            } catch (Exception e) {
                Logger.logErr("Custom Auth Loading failed for: " + aname + " Falling back to Sheetster Auth.");
                storageAuth = new SheetsterAuth();
            }
            String sname = storageType + "Storage";
            Logger.logInfo("Custom Storage Loading for: " + sname);
            try {
                storage = (Storage) Class.forName(sname).newInstance();
            } catch (Exception e) {
                Logger.logErr("Custom Auth Loading failed for: " + sname);
            }
            storage.setAuth(storageAuth);
        }
    }

    public int getWorkbooksInMemory() {
        return sessionWorkbooks.size();
    }

    public int getWorkbooksInDiskCache() {
        return cachedWorkbooks.size();
    }

    /**
     * Get a workbook from the cache.  If the workbook does not exist
     * within the cache, create it from the db and add to cache.
     * ------------------------------------------------------------
     * 
     * @author John McMahon [ June 3, 2009 ]
     * @param memeId
     * @param resetTimer - If the timer should be reset.  If no, and the workbook has expired, return null
     * @return
     */
    public synchronized MemeDocument getRemoteWorkBook(Integer guiId, User user, MessageListener ml, boolean resetTimer, Map requestParams) throws StorageException {
        MemeDocument thisbook = null;
        try {
            thisbook = this.getCachedWorkbook(guiId, resetTimer);
            if (thisbook != null) {
                try {
                    if (storage.getAuth().hasResourceAccess(guiId, user)) ;
                } catch (AuthException e) {
                    throw new StorageException("Error getting resource access: " + e);
                }
            }
        } catch (IOException e) {
            Logger.logWarn(e.toString());
        }
        if (thisbook == null) {
            if (guiId == -1) {
                thisbook = new WebWorkBook(user);
                thisbook.setStoreBookAsFile(this.storeBookAsFile);
                thisbook.setStoreAsXML(this.storeAsXML);
                thisbook.getMessageManager().registerClient(ml);
                try {
                    thisbook.setMemeId(storage.getNewDocumentId());
                } catch (StorageException e) {
                    e.printStackTrace();
                }
                guiId = thisbook.getMemeId();
            } else {
                thisbook = (WebWorkBook) storage.loadDocument(guiId, user, ml, requestParams);
                thisbook.setMemeId(guiId);
            }
            sessionWorkbooks.put(guiId, thisbook);
        } else {
            thisbook.getMessageManager().registerClient(ml);
        }
        System.setProperty("com.extentech.ExtenXLS.cacheCellHandles", "true");
        String calc_mode = context.getInitParameter("org.docsfree.legacy.workbook_calc_mode");
        if (service_type.equals("workbook")) {
            ((WebWorkBook) thisbook).setFormulaCalculationMode(WebWorkBook.CALCULATE_AUTO);
            if (calc_mode != null) {
                if (calc_mode.equalsIgnoreCase("always")) {
                    ((WebWorkBook) thisbook).setFormulaCalculationMode(WebWorkBook.CALCULATE_ALWAYS);
                }
            }
        }
        return thisbook;
    }

    /**
     * Attempts to fetch an existing spreadsheet for the current user identified by
     * category id.
     * 
     * If there are multiple matches then the first matching spreadsheet is returned.
     * 
     * 
     * Jan 24, 2010
     * @param session
     * @param user
     * @param the int of the category ID to select the sheet with
     * @return
     */
    public synchronized DocumentHandle getDefaultWorkBook(HttpSession session, User user, int tag_id, MessageListener ml) {
        int new_book_id = -1;
        conn = this.getConnection();
        if (session.getAttribute("facebook_sheet") != null) {
            new_book_id = ((Integer) session.getAttribute("facebook_sheet")).intValue();
        } else {
            try {
                String SQLS = "SELECT kb_memes.id FROM kb_memes, kb_meme_category_idx WHERE meme_id = kb_memes.id AND category_id=" + tag_id + " AND owner_id=" + user.getId();
                Statement StatementRecordset1 = conn.createStatement();
                ResultSet rsx1 = StatementRecordset1.executeQuery(SQLS);
                if (rsx1.next()) {
                    new_book_id = rsx1.getInt("id");
                } else {
                    Logger.logInfo("No existing tag id: " + tag_id + " spreadsheets found... creating new initial spreadsheet for: " + user.toString());
                }
                StatementRecordset1.close();
                rsx1.close();
            } catch (Exception ex) {
                Logger.logErr("DocumentLoader.getDefaultWorkBook failed for tag id : " + tag_id + " failed to creat new initial spreadsheet for: " + user.toString(), ex);
            }
        }
        if (new_book_id == -1) {
            return this.getNewWorkBook(user, ml);
        } else {
            return this.getDocument(new_book_id, user, ml, true);
        }
    }

    /**
     * Get a workbook from the cache.  If the workbook does not exist
     * within the cache, create it from the db and add to cache.
     * ------------------------------------------------------------
     * 
     * @author Nicholas Rab [ Apr 11, 2007 ]
     * @param memeId
     * @param resetTimer - If the timer should be reset.  If no, and the workbook has expired, return null
     * @return
     */
    public synchronized DocumentHandle getDocument(Integer memeId, User user, MessageListener ml, boolean resetTimer) {
        try {
            MemeDocument thisbook = null;
            try {
                thisbook = this.getCachedWorkbook(memeId, resetTimer);
            } catch (IOException e) {
                Logger.logWarn(e.toString());
            }
            if (thisbook == null) {
                thisbook = InitializeWorkBookFromDB(memeId, user, ml);
                sessionWorkbooks.put(memeId, thisbook);
                return thisbook;
            } else {
                if (ml != null) thisbook.getMessageManager().registerClient(ml);
            }
            return thisbook;
        } catch (NullPointerException e) {
            MemeDocument thisbook = InitializeWorkBookFromDB(memeId, user, ml);
            thisbook.setStoreAsXML(this.storeAsXML);
            thisbook.setStoreBookAsFile(this.storeBookAsFile);
            sessionWorkbooks.put(memeId, thisbook);
            return thisbook;
        }
    }

    /**
     * Reset the timer for the memeId
     * ------------------------------------------------------------
     * 
     * @author nick [ Sep 9, 2008 ]
     * @param memeId
     * @return
     */
    private synchronized void resetTimer(Object id) {
        Long l = new Long(System.currentTimeMillis());
        workbookTimer.put(id, l);
    }

    /**
     * Check if the timer is expired on a particular memeId
     * ------------------------------------------------------------
     * 
     * @author nick [ Sep 10, 2008 ]
     * @param memeId
     * @return
     */
    private synchronized boolean isTimerExpired(Object id) {
        Long lng = (Long) workbookTimer.get(id);
        if (lng != null) {
            long l = lng.longValue();
            if ((l + workbookTimeoutMs) < System.currentTimeMillis()) return true;
        } else {
            resetTimer(id);
        }
        return false;
    }

    protected synchronized void cacheExpiredWorkbooks() {
        Set s = workbookTimer.keySet();
        Integer[] expi = new Integer[workbookTimer.size()];
        Iterator i = s.iterator();
        int t = 0;
        while (i.hasNext()) {
            expi[t++] = (Integer) i.next();
        }
        for (Integer ingr : expi) {
            if (this.isTimerExpired(ingr)) {
                try {
                    this.cacheWorkbook(ingr);
                } catch (Exception ex) {
                    Logger.logErr("DocumentLoader.cacheExpiredWorkbooks failed.", ex);
                }
            }
        }
    }

    /**
     * Resets a loaded workbook in the cache.  If the workbook does not exist
     * within the cache, create it from the db and add to cache.
     * ------------------------------------------------------------
     * 
     * @author John McMahon [ Apr 11, 2007 ]
     * @param memeId
     * @return
     */
    public synchronized MemeDocument resetDocument(Integer memeId, User user, MessageListener ml) {
        MemeDocument thisbook = InitializeWorkBookFromDB(memeId, user, ml);
        sessionWorkbooks.put(memeId, thisbook);
        thisbook.getMessageManager().registerClient(ml);
        return thisbook;
    }

    /** manually add a workbook to the cache (used by copy)
     * ------------------------------------------------------------
     * 
     * @author john [ Oct 13, 2008 ]
     * @param wbh
     */
    public void addSessionWorkBook(MemeDocument wbh) {
        this.sessionWorkbooks.put(new Integer(wbh.getMemeId()), wbh);
    }

    /**
     * Write a workbook to a cache file on disk
     * 
     */
    public synchronized void cacheWorkbook(Integer memeId) {
        try {
            File tmpfile = File.createTempFile("tmpbook" + memeId, ".xls");
            tmpfile.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(tmpfile);
            BufferedOutputStream bbout = new BufferedOutputStream(fos);
            Object ob = sessionWorkbooks.get(memeId);
            MemeDocument book = null;
            if (ob != null) {
                book = (MemeDocument) sessionWorkbooks.get(memeId);
            } else {
                Logger.logInfo("DocumentLoader.cacheWorkbook: book not found.");
                return;
            }
            try {
                book.writeBytes(bbout);
            } catch (Exception e) {
                Logger.logErr("Streaming Workbook: " + book.toString() + " failed.", e);
            }
            bbout.flush();
            bbout.close();
            fos.flush();
            fos.close();
            try {
                List rlg = book.getRESTlog();
                File rlgtmp = new File(tmpfile.getAbsolutePath() + ".restlog");
                rlgtmp.deleteOnExit();
                FileOutputStream fosx = new FileOutputStream(rlgtmp);
                BufferedOutputStream bboutx = new BufferedOutputStream(fosx);
                Iterator logit = rlg.iterator();
                while (logit.hasNext()) {
                    String ll = (String) logit.next();
                    bboutx.write(ll.getBytes());
                    bboutx.write("\r\n".getBytes());
                }
                bboutx.flush();
                bboutx.close();
                fosx.flush();
                fosx.close();
            } catch (Exception e) {
                Logger.logWarn("Problem caching workbook REST log for workbook " + book.toString() + " : " + e.toString());
            }
            if (sessionWorkbooks != null) {
                sessionWorkbooks.remove(new Integer(book.getMemeId()));
            } else {
                sessionWorkbooks = new HashMap();
            }
            if (workbookTimer != null) {
                workbookTimer.remove(new Integer(book.getMemeId()));
            } else {
                workbookTimer = new HashMap();
            }
            if (cachedWorkbooks != null) {
                cachedWorkbooks.put(new Integer(book.getMemeId()), new WorkBookCacheInfo(book, tmpfile.getAbsolutePath()));
            } else {
                cachedWorkbooks = new HashMap();
            }
            book = null;
        } catch (IOException e) {
            Logger.logErr("Error writing temp book: " + e);
        }
    }

    /**
     * Get a cached workbook from the disk cache
     * 
     */
    public synchronized MemeDocument getCachedWorkbook(Object id, boolean resetTimer) throws IOException {
        conn = getConnection();
        if (resetTimer) this.resetTimer(id);
        WorkBookCacheInfo cinfo = null;
        MemeDocument wbh;
        try {
            cinfo = (WorkBookCacheInfo) cachedWorkbooks.get(id);
        } catch (Exception e) {
            Logger.logWarn("Unable to find file" + e);
        }
        wbh = (MemeDocument) sessionWorkbooks.get(id);
        if (wbh == null) {
            try {
                if (cachedWorkbooks.containsKey(id)) {
                    try {
                        if (service_type.equals("workbook")) {
                            wbh = new WebWorkBook(cinfo.getTmpFilePath());
                        } else if (service_type.equals("doc")) {
                            wbh = new WebDoc(cinfo.getTmpFilePath());
                        }
                    } catch (Throwable tx) {
                        Logger.logErr("DocumentLoader getCachedWorkbook failed.", tx);
                        return null;
                    }
                    cinfo.resetFields(wbh);
                    wbh.setConnection(conn);
                    wbh.setStoreBookAsFile(this.storeBookAsFile);
                    wbh.setStoreAsXML(this.storeAsXML);
                    sessionWorkbooks.put(id, wbh);
                    cachedWorkbooks.remove(id);
                    try {
                        Vector logl = readFile(cinfo.getTmpFilePath() + ".restlog");
                        wbh.setRESTlog(logl);
                    } catch (Exception ex) {
                        Logger.logWarn(ex.toString());
                    }
                }
            } catch (Exception e) {
                throw new IOException("File not found");
            }
        }
        return wbh;
    }

    /**
     * Reads File from Disk
     * 
     * @param fname path to file
     */
    private static Vector readFile(String fname) {
        Vector fileTxt = new Vector();
        try {
            BufferedReader d = new BufferedReader(new FileReader(fname));
            while (d.ready()) fileTxt.add(d.readLine());
            d.close();
        } catch (FileNotFoundException e) {
            Logger.logErr("File Not Found: " + e);
        } catch (IOException e) {
            Logger.logErr("Error in JSPProcessor reading file: " + fname + ":" + e.getMessage());
        }
        return fileTxt;
    }

    /**
     * Insert a workbook into the database, and add to cache
     * ------------------------------------------------------------
     * 
     * @author Nicholas Rab [ May 2, 2007 ]
     * @param book
     * @param user
     * @return
     * @throws SQLException 
     */
    public int storeNewWorkBook(MemeDocument book, User user, MessageListener ml) throws SQLException {
        book.setOwnerId(user.getId());
        conn = getConnection();
        book.setConnection(conn);
        book.storeNewDocument();
        book.getMessageManager().registerClient(ml);
        int theId = book.getMemeId();
        sessionWorkbooks.put(new Integer(theId), book);
        return theId;
    }

    /**
     * return the XML for this document
     * 
     * @return
     */
    protected String getXML() {
        return "TODO";
    }

    /** get the stats of workbooks in cache
     * ------------------------------------------------------------
     * 
     * @author John [ Oct 22, 2007 ]
     * @return
     */
    public Map getDocumentSessionStats() {
        HashMap stats = new HashMap();
        Iterator its = sessionWorkbooks.keySet().iterator();
        while (its.hasNext()) {
            Object ky = its.next();
            WebWorkBook bk = (WebWorkBook) sessionWorkbooks.get(ky);
            stats.put(ky, bk.getStats());
        }
        return stats;
    }

    /** get the number of workbooks in cache
     * ------------------------------------------------------------
     * 
     * @author John [ Oct 22, 2007 ]
     * @return
     */
    public int getDocumentSessionCount() {
        return sessionWorkbooks.size();
    }

    /**
     * Create a new workbook, and insert requisite data into the db.  Also add to cache
     * ------------------------------------------------------------
     * 
     * @author Nicholas Rab [ Apr 11, 2007 ]
     * @return
     */
    public WebWorkBook getNewWorkBook(User user, MessageListener ml) {
        WebWorkBook thisbook;
        thisbook = new WebWorkBook(user);
        thisbook.setStoreBookAsFile(this.storeBookAsFile);
        thisbook.setStoreAsXML(this.storeAsXML);
        try {
            conn = getConnection();
            thisbook.setConnection(conn);
            int theId = thisbook.storeNewDocument();
            user.initOwnedACL();
            sessionWorkbooks.put(new Integer(theId), thisbook);
            this.resetTimer(theId);
            thisbook.getMessageManager().registerClient(ml);
        } catch (Exception ex) {
            Logger.logErr("DocumentLoader.getWorkbook failed.", ex);
            thisbook = null;
            return null;
        }
        return thisbook;
    }

    /** check whether the requested meme is public
     * 
     * @param mid
     * @return
     */
    public boolean isPublic(String mid) {
        try {
            return this.storage.getAuth().isPublic(mid);
        } catch (AuthException e) {
            Logger.logErr("Error checking if public " + e);
        }
        return false;
    }

    /**
     * Go to the database, grab the xml for the workbook, and create a WorkBook
     * from that data;
     * 
     * ------------------------------------------------------------
     * 
     * @author Nicholas Rab [ Apr 11, 2007 ]
     * @param memeId
     * @return
     */
    private MemeDocument InitializeWorkBookFromDB(Integer memeId, User user, MessageListener ml) {
        MemeDocument bkx = null;
        try {
            if ((conn == null) || (conn.isClosed())) {
                Connection dbcon = DatabaseManager.getConnectionNoX(context);
                this.setConnection(dbcon);
            }
        } catch (Exception e) {
            Logger.logErr("DocumentLoader.InitializeWorkBookFromDB Failed.", e);
        }
        if (service_type.equalsIgnoreCase("workbook")) {
            bkx = new WebWorkBook(conn, memeId.intValue(), this.storeBookAsFile, this.storeAsXML);
        } else if (service_type.equalsIgnoreCase("doc")) {
            bkx = new WebDoc(conn, memeId.intValue(), this.storeBookAsFile, this.storeAsXML);
        }
        bkx.getMessageManager().registerClient(ml);
        return bkx;
    }

    /**
     * Run the provided Runnable object in a background thread. This method will
     * return as soon as the background thread is started, it does not wait for
     * the thread to complete.
     */
    public synchronized void runInBackgroundThread(Runnable runnable) {
        Thread t = new Thread(runnable);
        t.start();
    }

    /** E360 DB connection 
     * ------------------------------------------------------------
     * 
     * @author John McMahon [ May 1, 2008 ]
     * @return Returns the conn.
     */
    public Connection getConnection() {
        if (conn == null) conn = DatabaseManager.getConnectionNoX(context);
        return conn;
    }

    /** E360 DB connection 
     * ------------------------------------------------------------
     * 
     * @author John McMahon [ May 1, 2008 ]
     * @param conn The conn to set.
     */
    public void setConnection(Connection cx) {
        conn = cx;
    }

    public int getWorkbookTimeoutMs() {
        return workbookTimeoutMs;
    }

    public void setWorkbookTimeoutMs(int workbookTimeoutMs) {
        this.workbookTimeoutMs = workbookTimeoutMs;
    }

    public boolean isStoreAsXML() {
        return storeAsXML;
    }

    public void setStoreAsXML(boolean b) {
        this.storeAsXML = b;
    }

    public boolean isStoreBookAsFile() {
        return storeBookAsFile;
    }

    public void setStoreBookAsFile(boolean b) {
        this.storeBookAsFile = b;
    }

    /** the local auth is the "storage" auth
    services have their own auths
   
       @see Auth
     * @return Returns the auth.
     */
    public Storage getStorage() {
        return storage;
    }

    /** the local auth is the "storage" auth
        services have their own auths
       
       @see Auth
     * @param auth The auth to set.
     */
    public void setStorage(Storage a) {
        this.storage = a;
    }

    public class WorkBookReaper extends java.lang.Thread implements java.lang.Runnable {

        private DocumentLoader _parent;

        public WorkBookReaper(DocumentLoader ldr) {
            _parent = ldr;
        }

        public void run() {
            int skippy = _parent.getWorkbookTimeoutMs();
            boolean running = true;
            while (running) {
                try {
                    this.sleep(skippy);
                    _parent.cacheExpiredWorkbooks();
                } catch (InterruptedException e) {
                    Logger.logErr("WARNING: Workbook Reaper Wakeup Failed: " + e);
                } catch (Throwable e) {
                    Logger.logErr("ERROR: WorkbookReaper thread Failed: " + e);
                }
            }
        }
    }

    /**
     * WorkBookCacheInfo is a class that holds data from a WebWorkbook that is not retained through read/write
     * cycles used in caching.
     */
    public class WorkBookCacheInfo {

        ChatMessageManager mgr;

        String wbName, wbScript;

        int memeId, ownerId, status;

        String tmpFilePath;

        /** cache the workbook meta information to the object
         * ------------------------------------------------------------
         * 
         * @author nick [ Oct 29, 2008 ]
         * @param book
         * @param filePath
         */
        public WorkBookCacheInfo(MemeDocument book, String filePath) {
            tmpFilePath = filePath;
            mgr = (ChatMessageManager) book.getMessageManager();
            wbName = book.getName();
            wbScript = book.getScript();
            memeId = book.getMemeId();
            ownerId = book.getOwnerId();
            status = book.getSharingAccess();
        }

        /**
         * set the meta information back onto the object
         * ------------------------------------------------------------
         * 
         * @author nick [ Oct 29, 2008 ]
         * @param book
         */
        public void resetFields(MemeDocument book) {
            try {
                book.setMemeId(memeId);
                book.setOwnerId(ownerId);
                book.setSharingAccess(status);
                book.setMessageManager(mgr);
                book.setName(wbName);
                book.setScript(wbScript);
            } catch (Exception e) {
                Logger.logErr("DocumentLoader CacheInfo.setFields failed.", e);
            }
        }

        public int getMemeId() {
            return memeId;
        }

        public void setMemeId(int memeId) {
            this.memeId = memeId;
        }

        public ChatMessageManager getMgr() {
            return mgr;
        }

        public void setMgr(ChatMessageManager mgr) {
            this.mgr = mgr;
        }

        public String getTmpFilePath() {
            return tmpFilePath;
        }

        public void setTmpFilePath(String tmpFilePath) {
            this.tmpFilePath = tmpFilePath;
        }

        public String getWbName() {
            return wbName;
        }

        public void setWbName(String wbName) {
            this.wbName = wbName;
        }
    }

    /**
	 * @see com.extentech.ExtenBean.PersistenceEngine#createNewDataObject(com.extentech.ExtenBean.DataObject)
	 */
    public DataObject createNewDataObject(DataObject d) throws SQLException {
        return null;
    }

    /**
	 * @see com.extentech.ExtenBean.PersistenceEngine#initDataObject(com.extentech.ExtenBean.DataObject)
	 */
    public DataObject initDataObject(DataObject o) throws SQLException {
        return null;
    }

    /**
	 * @see com.extentech.ExtenBean.PersistenceEngine#removeDataObject(com.extentech.ExtenBean.DataObject)
	 */
    public boolean removeDataObject(DataObject o) throws SQLException {
        return false;
    }

    /**
	 * @see com.extentech.ExtenBean.PersistenceEngine#storeDataObject(com.extentech.ExtenBean.DataObject)
	 */
    public boolean storeDataObject(DataObject o) throws SQLException {
        return false;
    }

    /**
	 * @see com.extentech.ExtenBean.PersistenceEngine#updateDataObject(com.extentech.ExtenBean.DataObject)
	 */
    public boolean updateDataObject(DataObject o) throws SQLException {
        return false;
    }
}
