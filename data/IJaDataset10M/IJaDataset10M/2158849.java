package com.germinus.xpression.cms.lucene.indexer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import com.germinus.util.concurrent.ThreadPoolFactory;
import com.germinus.xpression.cms.CMSRuntimeException;
import com.germinus.xpression.cms.CmsConfig;
import com.germinus.xpression.cms.contents.Content;
import com.germinus.xpression.cms.contents.ContentManager;
import com.germinus.xpression.cms.contents.ContentNotFoundException;
import com.germinus.xpression.cms.contents.MalformedContentException;
import com.germinus.xpression.cms.hibernate.HibernateUtil;
import com.germinus.xpression.cms.lucene.ContentUrl;
import com.germinus.xpression.cms.lucene.ContentUrlFactory;
import com.germinus.xpression.cms.lucene.ScribeContentUrl;
import com.germinus.xpression.cms.lucene.beans.ContentBean;
import com.germinus.xpression.cms.lucene.beans.ContentDocument;
import com.germinus.xpression.cms.lucene.exception.LuceneException;
import com.germinus.xpression.cms.lucene.search.LuceneCMSQuery;
import com.germinus.xpression.cms.lucene.search.LuceneContentSearcher;
import com.germinus.xpression.cms.lucene.search.LuceneSearchResult;
import com.germinus.xpression.cms.util.ManagerRegistry;
import com.germinus.xpression.cms.worlds.World;
import com.germinus.xpression.cms.worlds.WorldManager;
import com.germinus.xpression.model.ScribeBaseLog;
import com.germinus.xpression.model.ScribeProcess;
import com.germinus.xpression.model.ScribeProcessLog;
import com.germinus.xpression.services.MailServiceHolder;
import com.germinus.xpression.services.ScribeTemplateMessage;

public class IndexerUtil implements ScribeProcess {

    private static final String PROCESS_NAME = "Scribe Indexer";

    private static Log log = LogFactory.getLog(IndexerUtil.class);

    private static final int INITIAL_SYNC_DELAY = 0;

    private static final int SYNC_DELAY = 60;

    private static long ADD_DOCUMENT_DELAY_SECONDS = 2;

    static final String REVISION_FILE_NAME = "revision";

    private static ContentIndexer indexer;

    private static IndexReader indexReader;

    private static ClusterOperationManager operationsManager;

    private static boolean initialized = false;

    private static String indexPath;

    private static ScheduledExecutorService scheduledService;

    private static ClusterRevisionFile clusterRevisionFile;

    private static IndexerUtil instance;

    public IndexerUtil() {
        super();
        this.scribeProcessLog = new ScribeBaseLog(25, log);
    }

    public static synchronized void init() {
        if (!initialized) {
            indexer = new ContentIndexer();
            initialized = true;
            operationsManager = new ClusterOperationManager();
            try {
                indexer.init();
            } catch (LuceneException e1) {
                throw new CMSRuntimeException(e1);
            }
            indexer.closeIndex();
            instance = new IndexerUtil();
            ManagerRegistry.getProcessesRegistry().addProcess(instance);
            instance.start();
        }
    }

    static long loadIndexRevision() throws LuceneClusterException {
        return clusterRevisionFile().get();
    }

    public static synchronized void shutdown() {
        getScheduledService().shutdown();
        try {
            getScheduledService().awaitTermination(200, TimeUnit.SECONDS);
            if (!getScheduledService().isTerminated()) {
                log.error("Forced to shutdown... but not all lucene cluster indexing threads were closed");
            }
        } catch (InterruptedException e) {
            log.error("Error when wating for lucene cluster indexing threads to finish" + e);
        }
    }

    public static void initScheduledService() {
        setScheduledService(ThreadPoolFactory.createSingleThreadPool(PROCESS_NAME));
        getScheduledService().scheduleWithFixedDelay(syncJob(), INITIAL_SYNC_DELAY, SYNC_DELAY, TimeUnit.SECONDS);
    }

    private static Runnable syncJob() {
        return new Runnable() {

            public void run() {
                long indexRevision;
                instance.lastExecuted = new Date();
                try {
                    indexRevision = getIndexRevision();
                    if (log.isDebugEnabled()) {
                        instance.scribeProcessLog.debug("Checking if sync lucene index operations pending. Index revision: " + indexRevision);
                    }
                } catch (Exception e2) {
                    instance.scribeProcessLog.error("Error obtaining current index revision", e2);
                    return;
                }
                List<ClusterOperation> ops;
                try {
                    ops = operationsManager.getFrom(indexRevision);
                } catch (Exception e1) {
                    instance.scribeProcessLog.error("Error obtaining index operations since " + indexRevision);
                    return;
                }
                if (ops.size() > 0) {
                    try {
                        instance.scribeProcessLog.info("Lucene index operations pending. Found " + ops.size() + " operations");
                        initIndexPath();
                        long lastRevision = indexRevision;
                        for (Iterator<ClusterOperation> itClusterOperation = ops.iterator(); itClusterOperation.hasNext(); ) {
                            ClusterOperation co = (ClusterOperation) itClusterOperation.next();
                            lastRevision = co.getRevision();
                            try {
                                if (StringUtils.equals(co.getType(), ClusterOperation.OP_DEL) || StringUtils.equals(co.getType(), ClusterOperation.OP_UPD)) {
                                    indexReader = IndexReader.open(getIndexPath());
                                    try {
                                        if (StringUtils.equals(co.getType(), ClusterOperation.OP_DEL) && log.isDebugEnabled()) instance.scribeProcessLog.debug("Content deleted from index(cluster): " + co.getContentId());
                                        doDeleteDocument(co.getContentId());
                                    } catch (IOException e) {
                                        instance.scribeProcessLog.error("Error indexing DELETE document", e);
                                    } finally {
                                        indexReader.close();
                                    }
                                }
                                if (StringUtils.equals(co.getType(), ClusterOperation.OP_ADD) || StringUtils.equals(co.getType(), ClusterOperation.OP_UPD)) {
                                    ContentManager contentManager = ManagerRegistry.getContentManager();
                                    WorldManager worldManager = ManagerRegistry.getWorldManager();
                                    try {
                                        contentManager.getCacheAdmin().removeEntry(co.getContentId());
                                        ScribeContentUrl contentUrl = (ScribeContentUrl) ContentUrlFactory.generateUrl(co.getContentId());
                                        Content content = contentManager.getContentByIdNoCache(contentUrl.obtainContenSource().getSimpleId(), contentUrl.getWorkspace());
                                        World world = worldManager.getOwnerWorld(content);
                                        ContentBean cb;
                                        try {
                                            cb = new ContentBean(content, world);
                                        } catch (NullPointerException np) {
                                            throw new MalformedContentException("Content type not found");
                                        }
                                        indexer.init();
                                        try {
                                            doAddDocument(cb);
                                            if (log.isDebugEnabled()) instance.scribeProcessLog.debug("Content updated in index(cluster): " + cb.getName());
                                        } catch (LuceneException e) {
                                            instance.scribeProcessLog.error("Error indexing ADD document", e);
                                        } catch (IOException e) {
                                            instance.scribeProcessLog.error("Error indexing ADD document", e);
                                        } finally {
                                            indexer.closeIndex();
                                        }
                                    } catch (CMSRuntimeException e) {
                                        instance.scribeProcessLog.error("Error accessing CMS when indexing content for cluster: " + co.getContentId() + " " + e);
                                    } catch (ContentNotFoundException e) {
                                        operationsManager.remove(co);
                                        instance.scribeProcessLog.warn("Content not found when indexing content for cluster: " + co.getContentId() + " " + e);
                                    } catch (MalformedContentException e) {
                                        instance.scribeProcessLog.error("Malformed content when indexing content for cluster: " + co.getContentId() + " " + e);
                                    }
                                }
                            } catch (IOException e) {
                                instance.scribeProcessLog.error("Error when updating lucene index with cluster opeartion: " + co + " " + e);
                                IndexerUtil.sendAlertMail(co, e);
                            } catch (LuceneException e) {
                                instance.scribeProcessLog.error("Error when updating lucene index with cluster opeartion: " + co + " " + e);
                                IndexerUtil.sendAlertMail(co, e);
                            } catch (Exception e) {
                                instance.scribeProcessLog.error("Generic error when indexing content for cluster: " + co.getContentId() + " " + e);
                                IndexerUtil.sendAlertMail(co, e);
                            }
                        }
                        if (indexRevision != lastRevision) setIndexRevision(lastRevision);
                        instance.scribeProcessLog.info("Lucene index operations pending. Finished " + ops.size() + " operations");
                    } catch (LuceneClusterException e) {
                        instance.scribeProcessLog.error("Error revision file for lucene cluster action " + e);
                    } catch (Throwable e) {
                        instance.scribeProcessLog.error("Error managing indexes for lucene cluster action ", e);
                    }
                } else {
                    if (log.isDebugEnabled()) instance.scribeProcessLog.debug("Indexing lucene for cluster: no operation pending");
                }
                try {
                    HibernateUtil.closeSession();
                } catch (NestableRuntimeException e) {
                    instance.scribeProcessLog.warn(e);
                }
            }
        };
    }

    private static void sendAlertMail(ClusterOperation co, Exception e2) {
        try {
            Map<String, Object> messageModel = new HashMap<String, Object>();
            messageModel.put("contentId", co.contentId);
            messageModel.put("error", e2);
            ScribeTemplateMessage scribeTemplateMessage = new ScribeTemplateMessage("index.alert.message", messageModel);
            MailServiceHolder.getMailService().sendMail(scribeTemplateMessage, CmsConfig.getAdminToEmailAddress(), CmsConfig.getAlertMessageFromAddress());
        } catch (Exception e) {
            log.error("Cannot send alert mail: " + e);
        }
    }

    private static void sendAlertMail(ContentBean cb, Throwable e2) {
        try {
            Map<String, Object> messageModel = new HashMap<String, Object>();
            messageModel.put("contentId", cb.getScribeContentUrl().toString());
            messageModel.put("error", e2);
            ScribeTemplateMessage scribeTemplateMessage = new ScribeTemplateMessage("index.alert.message", messageModel);
            MailServiceHolder.getMailService().sendMail(scribeTemplateMessage, CmsConfig.getAdminToEmailAddress(), CmsConfig.getAlertMessageFromAddress());
        } catch (Exception e) {
            log.error("Cannot send alert mail: " + e);
        }
    }

    protected static void initializeIndexes() throws IOException, LuceneException {
        initIndexPath();
        indexer.init();
        indexReader = IndexReader.open(getIndexPath());
    }

    protected static void closeIndexes() throws IOException {
        indexer.closeIndex();
        indexReader.close();
    }

    private static void initIndexPath() {
        indexPath = LuceneSearcherConf.getContentsIndexWriterPath();
    }

    public static ScheduledFuture<ContentUrl> deleteDocument(ContentBean cb) {
        return deleteDocumentJob(cb.getScribeContentUrl());
    }

    public static ScheduledFuture<ContentUrl> deleteDocument(ContentUrl scribeContentUrl) {
        return deleteDocumentJob(scribeContentUrl);
    }

    public static ScheduledFuture<String> addDocument(final ContentBean cb) {
        return addDocumentJob(cb);
    }

    public static ScheduledFuture<String> updateDocument(final ContentBean cb) {
        return updateDocumentJob(cb);
    }

    private static ScheduledFuture<String> addDocumentJob(final ContentBean cb) {
        Callable<String> addDocumentJob = new Callable<String>() {

            public String call() throws Exception {
                initIndexPath();
                indexer.init();
                try {
                    doAddDocument(cb);
                    if (log.isDebugEnabled()) instance.scribeProcessLog.debug("Content added to index: " + cb.getName());
                    ClusterOperationManager.queueOperation(cb.getScribeContentUrl(), ClusterOperation.OP_ADD);
                } catch (Throwable t) {
                    sendAlertMail(cb, t);
                    instance.scribeProcessLog.error("Error indexing ADD document", t);
                    return null;
                } finally {
                    indexer.closeIndex();
                    try {
                        HibernateUtil.closeSession();
                    } catch (NestableRuntimeException e) {
                        instance.scribeProcessLog.warn(e);
                    }
                }
                return cb.getContentId();
            }
        };
        ScheduledFuture<String> addDocumentFuture = getScheduledService().schedule(addDocumentJob, getADD_DOCUMENT_DELAY_SECONDS(), TimeUnit.SECONDS);
        return addDocumentFuture;
    }

    private static ScheduledFuture<ContentUrl> deleteDocumentJob(final ContentUrl scribeContentUrl) {
        Callable<ContentUrl> deleteDocumentJob = new Callable<ContentUrl>() {

            public ContentUrl call() throws Exception {
                initIndexPath();
                indexReader = IndexReader.open(getIndexPath());
                try {
                    doDeleteDocument(scribeContentUrl);
                    if (log.isDebugEnabled()) instance.scribeProcessLog.debug("Content deleted from index: " + scribeContentUrl);
                    ClusterOperationManager.queueOperation(scribeContentUrl, ClusterOperation.OP_DEL);
                } catch (IOException e) {
                    instance.scribeProcessLog.error("Error indexing DELETE document", e);
                    return null;
                } finally {
                    indexReader.close();
                }
                return scribeContentUrl;
            }
        };
        return getScheduledService().schedule(deleteDocumentJob, getADD_DOCUMENT_DELAY_SECONDS(), TimeUnit.SECONDS);
    }

    private static ScheduledFuture<String> updateDocumentJob(final ContentBean cb) {
        Callable<String> updateDocumentJob = new Callable<String>() {

            public String call() throws Exception {
                initIndexPath();
                indexReader = IndexReader.open(getIndexPath());
                try {
                    String contentId = cb.getScribeContentUrl().toString();
                    doDeleteDocument(contentId);
                } catch (IOException e) {
                    instance.scribeProcessLog.warn("Error indexing deleting before UPDATE document", e);
                } catch (Throwable t) {
                    instance.scribeProcessLog.warn("Error indexing deleting before UPDATE document", t);
                } finally {
                    indexReader.close();
                }
                indexer.init();
                try {
                    doAddDocument(cb);
                    if (log.isDebugEnabled()) instance.scribeProcessLog.debug("Content updated in index: " + cb.getName());
                    ClusterOperationManager.queueOperation(cb.getScribeContentUrl(), ClusterOperation.OP_UPD);
                    return cb.getScribeContentUrl().toString();
                } catch (Throwable t) {
                    sendAlertMail(cb, t);
                    instance.scribeProcessLog.warn("Error indexing UPDATE adding document", t);
                    return null;
                } finally {
                    indexer.closeIndex();
                    try {
                        HibernateUtil.closeSession();
                    } catch (NestableRuntimeException e) {
                        instance.scribeProcessLog.warn(e);
                    }
                }
            }
        };
        return getScheduledService().schedule(updateDocumentJob, getADD_DOCUMENT_DELAY_SECONDS(), TimeUnit.SECONDS);
    }

    private static void doDeleteDocument(ContentUrl scribeContentUrl) throws IOException {
        doDeleteDocument(scribeContentUrl.toString());
    }

    private static void doDeleteDocument(String scribeContentUrl) throws IOException {
        indexReader.deleteDocuments(new Term(ContentBean.SCRIBE_URL, scribeContentUrl));
    }

    private static void doAddDocument(ContentBean cb) throws LuceneException, IOException {
        ContentDocument cd = new ContentDocument(cb);
        Document doc = cd.getLuceneDocument();
        synchronized (indexer) {
            indexer.addDocument(doc);
        }
    }

    static long getIndexRevision() {
        try {
            return loadIndexRevision();
        } catch (LuceneClusterException e) {
            throw new RuntimeException(e);
        }
    }

    static void setIndexRevision(long indexRevision_) throws LuceneClusterException {
        if (getIndexRevision() != indexRevision_) {
            ClusterRevisionFile file2 = clusterRevisionFile();
            file2.set(indexRevision_);
        }
    }

    private static ClusterRevisionFile clusterRevisionFile() throws LuceneClusterException {
        if (clusterRevisionFile == null || !new File(getIndexPath(), REVISION_FILE_NAME).exists()) {
            if (clusterRevisionFile != null) {
                log.info("Re-opening cluster revision file");
                try {
                    clusterRevisionFile.close();
                } catch (LuceneClusterException e) {
                    log.warn("Error closing cluster revision file");
                }
            }
            File file = new File(getIndexPath(), REVISION_FILE_NAME);
            clusterRevisionFile = new ClusterRevisionFile(file);
        }
        return clusterRevisionFile;
    }

    static String getIndexPath() {
        if (StringUtils.isEmpty(indexPath)) {
            initIndexPath();
        }
        return indexPath;
    }

    private static long getADD_DOCUMENT_DELAY_SECONDS() {
        return ADD_DOCUMENT_DELAY_SECONDS;
    }

    static void setADD_DOCUMENT_DELAY_SECONDS(long delayInSeconds) {
        ADD_DOCUMENT_DELAY_SECONDS = delayInSeconds;
    }

    protected static void destroyIndex() throws IOException {
        IndexWriter writer = new IndexWriter(getIndexPath(), null, true, new MaxFieldLength(IndexWriter.DEFAULT_MAX_FIELD_LENGTH));
        writer.close();
    }

    private static void setScheduledService(ScheduledExecutorService scheduledService) {
        IndexerUtil.scheduledService = scheduledService;
    }

    private static ScheduledExecutorService getScheduledService() {
        if (scheduledService == null) {
            IndexerUtil.scheduledService = ThreadPoolFactory.createFixedThreadPool(0, PROCESS_NAME);
        }
        return scheduledService;
    }

    public static void removeAll(World world) {
        addRemoveAllFromWorldJob(world);
    }

    private static void addRemoveAllFromWorldJob(final World world) {
        Callable<String> callable = new Callable<String>() {

            @Override
            public String call() throws Exception {
                log.debug("Removing all contents from world " + world + " from index");
                LuceneCMSQuery luceneCMSQuery = new LuceneCMSQuery();
                ArrayList<World> worlds = new ArrayList<World>();
                worlds.add(world);
                luceneCMSQuery.setWorlds(worlds);
                luceneCMSQuery.setPageSize(2000);
                initIndexPath();
                indexReader = IndexReader.open(getIndexPath());
                HibernateUtil.currentSession();
                try {
                    LuceneSearchResult searchHits = new LuceneContentSearcher().searchHits(luceneCMSQuery);
                    for (int i = 0; i < searchHits.length(); i++) {
                        Document doc = searchHits.doc(i);
                        String scribeContentUrl = doc.get(ContentBean.SCRIBE_URL);
                        doDeleteDocument(scribeContentUrl);
                        ClusterOperationManager.queueOperation(scribeContentUrl, ClusterOperation.OP_DEL);
                        log.debug("Deleted content from index: " + scribeContentUrl);
                    }
                } catch (Exception e) {
                    log.error("Error searching all contents indexed of " + world, e);
                } finally {
                    indexReader.close();
                    try {
                        HibernateUtil.closeSession();
                    } catch (NestableRuntimeException e) {
                        log.warn(e);
                    }
                }
                return null;
            }
        };
        getScheduledService().schedule(callable, 0, TimeUnit.SECONDS);
    }

    private Date lastExecuted;

    private boolean active;

    private ScribeProcessLog scribeProcessLog;

    @Override
    public Date getEstimatedNextExecuted() {
        return new Date(lastExecuted.getTime() + SYNC_DELAY * 1000);
    }

    @Override
    public Date getLastExecuted() {
        return lastExecuted;
    }

    @Override
    public String getName() {
        return PROCESS_NAME;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void restart() {
        stop();
        start();
    }

    @Override
    public void start() {
        initScheduledService();
        active = true;
    }

    @Override
    public void stop() {
        shutdown();
        active = false;
    }

    @Override
    public ScribeProcessLog getScribeProcessLog() {
        return scribeProcessLog;
    }
}
