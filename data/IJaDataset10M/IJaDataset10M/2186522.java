package com.sitescape.team.module.rss.impl;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import com.sitescape.team.dao.util.OrderBy;
import com.sitescape.team.domain.Binder;
import com.sitescape.team.domain.Entry;
import com.sitescape.team.domain.Folder;
import com.sitescape.team.domain.FolderEntry;
import com.sitescape.team.domain.User;
import com.sitescape.team.lucene.SsfIndexAnalyzer;
import com.sitescape.team.lucene.SsfQueryAnalyzer;
import com.sitescape.team.module.impl.CommonDependencyInjection;
import com.sitescape.team.module.rss.RssModule;
import com.sitescape.team.module.shared.EntityIndexUtils;
import com.sitescape.team.search.QueryBuilder;
import com.sitescape.team.search.SearchObject;
import com.sitescape.team.util.Constants;
import com.sitescape.team.util.FileHelper;
import com.sitescape.team.util.NLT;
import com.sitescape.team.util.SPropsUtil;
import com.sitescape.team.util.SimpleProfiler;
import com.sitescape.team.web.WebKeys;
import com.sitescape.team.web.util.WebHelper;
import com.sitescape.team.web.util.WebUrlUtil;
import com.sitescape.util.PropertyNotFoundException;

public class RssModuleImpl extends CommonDependencyInjection implements RssModule, RssModuleImplMBean {

    private static QueryParser qp = new QueryParser("guid", new SsfQueryAnalyzer());

    private final String ALL_FIELD = "allField";

    final String FS = System.getProperty("file.separator");

    final String TSFILENAME = "timestampfile";

    final String LOCKFILENAME = "lockfile";

    private final long DAYMILLIS = 24L * 60L * 60L * 1000L;

    protected Log logger = LogFactory.getLog(getClass());

    private String rssRootDir;

    int maxDays = 31;

    int maxInactiveDays = 7;

    SimpleDateFormat fmt = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");

    private static final String SCHEME_HOST_PORT_PATTERN = "<link>http.*/ssf/a/";

    private Pattern pattern = Pattern.compile(SCHEME_HOST_PORT_PATTERN);

    public RssModuleImpl() {
        try {
            maxDays = SPropsUtil.getInt("rss.max.elapseddays");
        } catch (PropertyNotFoundException e) {
        }
        try {
            maxInactiveDays = SPropsUtil.getInt("rss.max.inactivedays");
        } catch (PropertyNotFoundException e) {
        }
    }

    public String getRssRootDir() {
        return rssRootDir;
    }

    public void setRssRootDir(String rssRootDir) {
        if (rssRootDir.endsWith(Constants.SLASH)) this.rssRootDir = rssRootDir; else this.rssRootDir = rssRootDir + Constants.SLASH;
    }

    private void generateRssFeed(Binder binder) {
        String rssPathName = getRssPathName(binder);
        if (IndexReader.indexExists(rssPathName)) return;
        File rssdir = new File(rssRootDir);
        if (!rssdir.exists()) rssdir.mkdir();
        generateRssIndex(binder);
    }

    private String getRssPathName(Binder binder) {
        Long id = binder.getId();
        if (binder instanceof Folder) {
            Folder f = (Folder) binder;
            if (!f.isTop()) id = f.getTopFolder().getId();
        }
        String rssPathName = rssRootDir + id;
        return rssPathName;
    }

    public void deleteRssIndex(Binder binder) {
        String rssPathName = getRssPathName(binder);
        File rf = new File(rssPathName);
        if (rf.exists()) FileHelper.deleteRecursively(rf);
    }

    private void generateRssIndex(Binder binder) {
        RssFeedLock rfl = new RssFeedLock(rssRootDir, binder);
        try {
            if (!rfl.getFeedLock()) {
                logger.info("Couldn't get the RssFeedLock");
            }
            IndexWriter indexWriter = new IndexWriter(this.getRssPathName(binder), new SsfIndexAnalyzer(), true);
            long startDate = new Date().getTime() - (maxDays * DAYMILLIS);
            Date start = new Date(startDate);
            List<FolderEntry> entries = getFolderDao().loadFolderTreeUpdates((Folder) binder, start, new Date(), new OrderBy("HKey.sortKey"), 100);
            for (int i = 0; i < entries.size(); i++) {
                Entry entry = entries.get(i);
                org.apache.lucene.document.Document doc = createDocumentFromEntry(entry);
                indexWriter.addDocument(doc);
            }
            indexWriter.close();
        } catch (Exception e) {
            logger.info("generateRssIndex: " + e.toString());
        } finally {
            rfl.releaseFeedLock();
        }
    }

    /**
	 * See if the rss feed has been inactive for more than a month, if so, delete it.
	 * 
	 * @param entry
	 * @param indexPath
	 * @return
	 */
    private boolean rssFeedInactive(Entry entry, String indexPath) {
        try {
            String lastAccessedFile = indexPath + FS + TSFILENAME;
            File tf = new File(lastAccessedFile);
            if (!tf.exists()) {
                tf.createNewFile();
                return false;
            }
            long lastModified = tf.lastModified();
            long currentTime = System.currentTimeMillis();
            long maxInactive = maxInactiveDays * DAYMILLIS;
            if ((currentTime - lastModified) > maxInactive) {
                deleteRssIndex(entry.getParentBinder());
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    /**
	 * 
	 * @param indexPath
	 */
    private void updateTimestamp(String indexPath) {
        String lastAccessedFile = indexPath + FS + TSFILENAME;
        File tf = new File(lastAccessedFile);
        if (!tf.exists()) {
            try {
                tf.createNewFile();
            } catch (Exception e) {
            }
            return;
        } else {
            tf.setLastModified(System.currentTimeMillis());
        }
    }

    /**
	 * Update the rss feed whenever a new entry is added, or when a current entry
	 * is modified.  If the RSS index for this folder does not exist, or, hasn't been
	 * read in a time period then return immediately.  
	 * 
	 * The feed will be pruned, both by removing duplicate entries (for modify ops) and
	 * by the age of the entries.
	 * 
	 * @param entry
	 */
    public void updateRssFeed(Entry entry) {
        SimpleProfiler.startProfiler("RssModule.updateRssFeed");
        String indexPath = getRssPathName(entry.getParentBinder());
        File rf = new File(indexPath);
        if (!rf.exists()) return;
        RssFeedLock rfl = new RssFeedLock(rssRootDir, entry.getParentBinder());
        try {
            if (!rfl.getFeedLock()) {
                logger.info("Couldn't get the RssFeedLock");
            }
            if (rssFeedInactive(entry, indexPath)) return;
            boolean rssEnabled = SPropsUtil.getBoolean("rss.enable", true);
            if (!rssEnabled) {
                deleteRssIndex(entry.getParentBinder());
                return;
            }
            long endDate = new Date().getTime() - (maxDays * DAYMILLIS);
            String dateRange = "0 TO " + endDate;
            List<Integer> delDocIds = new ArrayList<Integer>();
            IndexSearcher indexSearcher = new IndexSearcher(indexPath);
            String qString = "guid:" + entry.getId().toString();
            Query q = qp.parse(qString);
            Hits hits = indexSearcher.search(q);
            for (int i = 0; i < hits.length(); i++) {
                delDocIds.add(hits.id(i));
            }
            qString = "age:[" + dateRange + "]";
            hits = indexSearcher.search(qp.parse(qString));
            for (int i = 0; i < hits.length(); i++) {
                delDocIds.add(hits.id(i));
            }
            indexSearcher.close();
            if (delDocIds.size() > 0) {
                IndexReader ir = IndexReader.open(indexPath);
                for (int i = 0; i < delDocIds.size(); i++) {
                    ir.deleteDocument((int) delDocIds.get(i));
                }
                ir.close();
            }
            IndexWriter indexWriter = new IndexWriter(indexPath, new SsfIndexAnalyzer(), false);
            indexWriter.addDocument(this.createDocumentFromEntry(entry));
            indexWriter.close();
        } catch (Exception e) {
            logger.info("Rss module error: " + e.toString());
        } finally {
            rfl.releaseFeedLock();
        }
        SimpleProfiler.stopProfiler("RssModule.updateRssFeed");
    }

    private String getRssDisabledString() {
        String rss = addRssHeader("RSS has been disabled for this feed");
        rss += addRssFooter();
        return rss;
    }

    /**
	 * Find the rss feed index. If it doesn't exist, then create it.
	 * Filter results by the requestor's acls.
	 * 
	 * @param request
	 * @param response
	 * @param binder
	 * @param user
	 */
    public String filterRss(HttpServletRequest request, HttpServletResponse response, Binder binder) {
        boolean rssEnabled = SPropsUtil.getBoolean("rss.enable", true);
        if (!rssEnabled) {
            return WebHelper.markupStringReplacement(null, null, request, response, null, this.getRssDisabledString(), WebKeys.MARKUP_VIEW);
        }
        String indexPath = getRssPathName(binder);
        RssFeedLock rfl = new RssFeedLock(rssRootDir, binder);
        try {
            File rf = new File(indexPath);
            if (!rf.exists()) this.generateRssFeed(binder);
            if (!rfl.getFeedLock()) {
                logger.info("Couldn't get the RssFeedLock");
            }
            updateTimestamp(indexPath);
            IndexSearcher indexSearcher = new IndexSearcher(indexPath);
            Query q = buildRssQuery();
            Hits hits = indexSearcher.search(q);
            String rss = addRssHeader(binder.getTitle());
            String adapterRoot = WebUrlUtil.getAdapterRootURL(request, null);
            int count = 0;
            String item;
            while (count < hits.length()) {
                org.apache.lucene.document.Document doc = hits.doc(count);
                item = doc.getField("rssItem").stringValue();
                item = fixupSchemeHostPort(item, adapterRoot);
                rss += item;
                count++;
            }
            indexSearcher.close();
            rss += addRssFooter();
            rss = WebHelper.markupStringReplacement(null, null, request, response, null, rss, WebKeys.MARKUP_VIEW);
            return rss;
        } catch (Exception e) {
            logger.info("filterRss: " + e.toString());
            return "";
        } finally {
            rfl.releaseFeedLock();
        }
    }

    protected String fixupSchemeHostPort(String item, String adapterRoot) {
        Matcher matcher = pattern.matcher(item);
        return matcher.replaceFirst("<link>" + adapterRoot);
    }

    /**
	 * return an Authentication error to the reader
	 * 
	 *  @param request
	 *  @param response
	 */
    public String AuthError(HttpServletRequest request, HttpServletResponse response) {
        Document doc = createEmptyRssDoc(NLT.get("rss.auth.failure"));
        return doc.asXML();
    }

    public String BinderExistenceError(HttpServletRequest request, HttpServletResponse response) {
        Document doc = createEmptyRssDoc(NLT.get("binder.deleted"));
        return doc.asXML();
    }

    private org.apache.lucene.document.Document createDocumentFromEntry(Entry entry) {
        org.apache.lucene.document.Document doc = new org.apache.lucene.document.Document();
        Date eDate = entry.getModification().getDate();
        Field ageField = new Field("age", new Long(eDate.getTime()).toString(), Field.Store.YES, Field.Index.UN_TOKENIZED);
        doc.add(ageField);
        Field guidField = new Field("guid", entry.getId().toString(), Field.Store.YES, Field.Index.TOKENIZED);
        doc.add(guidField);
        Field allField = new Field(ALL_FIELD, "all", Field.Store.NO, Field.Index.UN_TOKENIZED);
        doc.add(allField);
        Field rssItemField = new Field("rssItem", createRssItem(entry), Field.Store.YES, Field.Index.UN_TOKENIZED);
        doc.add(rssItemField);
        EntityIndexUtils.addReadAccess(doc, entry.getParentBinder(), entry, true);
        return doc;
    }

    private Query buildRssQuery() {
        SearchObject so = new SearchObject();
        org.dom4j.Document qTree = DocumentHelper.createDocument();
        Element qTreeRootElement = qTree.addElement(com.sitescape.util.search.Constants.QUERY_ELEMENT);
        Element qTreeAndElement = qTreeRootElement.addElement(com.sitescape.util.search.Constants.AND_ELEMENT);
        Element field = qTreeAndElement.addElement(com.sitescape.util.search.Constants.FIELD_ELEMENT);
        field.addAttribute(com.sitescape.util.search.Constants.FIELD_NAME_ATTRIBUTE, ALL_FIELD);
        Element child = field.addElement(com.sitescape.util.search.Constants.FIELD_TERMS_ELEMENT);
        child.setText("all");
        QueryBuilder qb = new QueryBuilder(true);
        so = qb.buildQuery(qTree);
        return so.getQuery();
    }

    private String createRssItem(Entry entry) {
        String ret = "<item>\n";
        String title = "<![CDATA[ " + entry.getTitle() + "]]>";
        ret += "<title>" + title + "</title>\n";
        ret += "<link>" + WebUrlUtil.getEntryPermalinkURL((FolderEntry) entry).replaceAll("&", "&amp;") + "</link>\n";
        String description = entry.getDescription() == null ? "" : entry.getDescription().getText();
        description = WebHelper.markupStringReplacement(null, null, null, null, entry, description, WebKeys.MARKUP_VIEW);
        description = "<![CDATA[ " + description + "]]>";
        ret += "<description>" + description + "</description>\n";
        ret += "<author>" + entry.getCreation().getPrincipal().getName() + "</author>\n";
        Date eDate = entry.getModification().getDate();
        ret += "<pubDate>" + fmt.format(eDate) + "</pubDate>\n";
        ret += "<age>" + new Long(eDate.getTime()).toString() + "</age>\n";
        ret += "<guid>" + entry.getId().toString() + "</guid>\n";
        ret += "</item>\n";
        return ret;
    }

    private String addRssHeader(String title) {
        String ret = "";
        ret += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
        ret += "<rss version=\"2.0\">\n";
        ret += "<channel>\n";
        ret += "<title>" + title + "</title>\n";
        ret += "<link/>";
        ret += "<description>" + title + "</description>\n";
        ret += "<pubDate>" + fmt.format(new Date()) + "</pubDate>\n";
        ret += "<ttl>60</ttl>\n";
        ret += "<generator feedVersion=\"1.0\">IceCore</generator>\n";
        return ret;
    }

    private String addRssFooter() {
        String ret = "";
        ret += "</channel>\n";
        ret += "</rss>\n";
        return ret;
    }

    private Document createEmptyRssDoc(String title) {
        Document doc = DocumentHelper.createDocument();
        Element root = doc.addElement("rss");
        root.addAttribute("version", "2.0");
        Element channel = root.addElement("channel");
        channel.addElement("title").addText(title);
        channel.addElement("link").addText("");
        channel.addElement("description").addText(title);
        channel.addElement("pubDate").addText(fmt.format(new Date()));
        channel.addElement("ttl").addText("60");
        channel.addElement("generator").addAttribute("feedVersion", "1.0").addText("IceCore");
        return doc;
    }
}

class RssFeedLock {

    protected Log logger = LogFactory.getLog(getClass());

    final String FS = System.getProperty("file.separator");

    final String LOCKFILENAME = "lockfile";

    private FileLock fileLock = null;

    private FileChannel fileChannel = null;

    private String indexPath = null;

    private Binder binder = null;

    RssFeedLock(String ip, Binder b) {
        indexPath = ip;
        binder = b;
    }

    public boolean getFeedLock() {
        String lockName = indexPath + FS + LOCKFILENAME + binder.getId();
        File lf = new File(lockName);
        if (!lf.exists()) {
            try {
                lf.createNewFile();
            } catch (Exception e) {
            }
        }
        try {
            fileChannel = new RandomAccessFile(lf, "rw").getChannel();
            fileLock = fileChannel.lock();
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    public boolean releaseFeedLock() {
        try {
            fileLock.release();
            fileChannel.close();
            return true;
        } catch (Exception e) {
            logger.info("Couldn't release the RssFeedLock");
        } finally {
            try {
                fileChannel.close();
            } catch (Exception e) {
            }
        }
        return false;
    }
}
