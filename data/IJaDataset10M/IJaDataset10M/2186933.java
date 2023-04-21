package oxygen.forum;

import java.io.File;
import java.io.Reader;
import java.util.*;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import oxygen.forum.data.*;
import oxygen.markup.MarkupRenderContext;
import oxygen.markup.indexing.HitsHandler;
import oxygen.markup.indexing.MarkupAnalyzer;
import oxygen.markup.indexing.MarkupIndexingManager;
import oxygen.markup.indexing.MarkupIndexingParser;
import oxygen.util.OxygenConstants;
import oxygen.util.OxygenUtils;
import oxygen.util.StringUtils;

public class ForumIndexingManager extends MarkupIndexingManager {

    private boolean supportFullTextSearch = true;

    public ForumIndexingManager() throws Exception {
        final ForumEngine engine = ForumLocal.getForumEngine();
        analyzer = new MarkupAnalyzer(this);
        analyzerForSearching = new MarkupAnalyzer(this);
        supportFullTextSearch = "true".equals(engine.getProperty(OxygenConstants.FULL_TEXT_SEARCH_SUPPORTED_KEY));
        TimerTask tt = new TimerTask() {

            public void run() {
                try {
                    ForumLocal.setForumEngine(engine);
                    incrementalIndexFiles();
                } catch (Exception exc) {
                    LogFactory.getLog(ForumIndexingManager.class).error("error", exc);
                } finally {
                    ForumLocal.setForumEngine(null);
                }
            }
        };
        engine.addTask(tt, ForumConstants.ONE_DAY, ForumConstants.ONE_DAY);
    }

    public void deleteIndexFiles() throws Exception {
        File f = getIndexDir();
        OxygenUtils.deleteFile(f);
    }

    public synchronized void indexAll() throws Exception {
        File f = getIndexDir();
        IndexWriter iwriter = null;
        try {
            iwriter = new IndexWriter(f, analyzer, true);
            Forum forum = (Forum) ForumLocal.getForumEngine().getForumDAO().getRootForum();
            createDocuments(iwriter, forum);
        } finally {
            close(iwriter);
        }
        updateMiscInfo();
    }

    public synchronized void incrementalIndexFiles() throws Exception {
        File f = getIndexDir();
        if (!IndexReader.indexExists(f)) {
            indexAll();
            return;
        }
        ForumDAO dao = ForumLocal.getForumEngine().getForumDAO();
        Misc m = dao.getMisc(ForumConstants.LAST_INDEX_DATETIME_KEY, "0");
        List topics = dao.getTopicsChangedSince(new Date(Long.parseLong(m.getValue())));
        IndexReader ireader = getIndexReader();
        try {
            for (Iterator itr = topics.iterator(); itr.hasNext(); ) {
                Topic t = (Topic) itr.next();
                Term term = new Term(ForumConstants.SEARCH_INDEX_TOPIC_ID, String.valueOf(t.getId()));
                ireader.deleteDocuments(term);
            }
        } finally {
            returnIndexReader(ireader);
            setWriteDone();
        }
        IndexWriter iwriter = null;
        try {
            iwriter = new IndexWriter(f, analyzer, false);
            for (Iterator itr = topics.iterator(); itr.hasNext(); ) {
                Topic t = (Topic) itr.next();
                createDocuments(iwriter, t);
            }
        } finally {
            close(iwriter);
            setWriteDone();
        }
        updateMiscInfo();
    }

    public void removeTopicsFromIndex(List topics) throws Exception {
        File f = getIndexDir();
        IndexReader ireader = getIndexReader();
        try {
            for (Iterator itr = topics.iterator(); itr.hasNext(); ) {
                Topic t = (Topic) itr.next();
                Term term = new Term(ForumConstants.SEARCH_INDEX_TOPIC_ID, String.valueOf(t.getId()));
                ireader.deleteDocuments(term);
            }
        } finally {
            returnIndexReader(ireader);
            setWriteDone();
        }
    }

    private void createDocuments(IndexWriter iwriter, Forum f) throws Exception {
        for (Iterator itr = f.getTopics().iterator(); itr.hasNext(); ) {
            Topic t = (Topic) itr.next();
            createDocuments(iwriter, t);
        }
        for (Iterator itr = f.getChildForums().iterator(); itr.hasNext(); ) {
            Forum f2 = (Forum) itr.next();
            createDocuments(iwriter, f2);
        }
    }

    private void createDocuments(IndexWriter iwriter, Topic t) throws Exception {
        Long lastPostId = ForumLocal.getForumEngine().getForumStatisticsManager().getLastPostIdForTopic(t.getId());
        Post p = (Post) ForumLocal.getForumEngine().getForumDAO().get(Post.class, lastPostId, false);
        if (p != null) {
            Document doc = new Document();
            doc.add(new Field(ForumConstants.SEARCH_INDEX_TOPIC_AUTHOR, t.getAuthor().getName(), Field.Store.YES, Field.Index.UN_TOKENIZED));
            doc.add(new Field(ForumConstants.SEARCH_INDEX_TOPIC_LAST_POST_DATE, String.valueOf(p.getDate().getTime()), Field.Store.YES, Field.Index.UN_TOKENIZED));
            doc.add(new Field(ForumConstants.SEARCH_INDEX_TOPIC_ID, String.valueOf(t.getId()), Field.Store.YES, Field.Index.UN_TOKENIZED));
            doc.add(new Field(ForumConstants.SEARCH_INDEX_FORUM_ID, String.valueOf(t.getForum().getId()), Field.Store.YES, Field.Index.UN_TOKENIZED));
            for (Iterator itr = t.getAllposts().iterator(); itr.hasNext(); ) {
                p = (Post) itr.next();
                doc.add(new Field(ForumConstants.SEARCH_INDEX_POSTER, p.getAuthor().getName(), Field.Store.YES, Field.Index.UN_TOKENIZED));
                if (supportFullTextSearch) {
                    doc.add(new Field(ForumConstants.SEARCH_INDEX_TOPIC_CONTENTS, p.getDetails(), Field.Store.YES, Field.Index.TOKENIZED));
                }
                doc.add(new Field(ForumConstants.SEARCH_INDEX_TITLE, p.getTitle(), Field.Store.YES, Field.Index.TOKENIZED));
            }
            iwriter.addDocument(doc);
        }
    }

    public void search(Map m, HitsHandler hhdlr) throws Exception {
        BooleanQuery query = new BooleanQuery();
        for (Iterator itr = m.keySet().iterator(); itr.hasNext(); ) {
            String key = (String) itr.next();
            Object val = m.get(key);
            if (val instanceof String && !(StringUtils.isBlank((String) val)) && (ForumConstants.SEARCH_INDEX_TOPIC_CONTENTS.equals(key) || ForumConstants.SEARCH_INDEX_POSTER.equals(key) || ForumConstants.SEARCH_INDEX_TITLE.equals(key) || ForumConstants.SEARCH_INDEX_FORUM_ID.equals(key))) {
                query.add(new BooleanClause(new QueryParser(key, analyzerForSearching).parse((String) val), BooleanClause.Occur.MUST));
            } else if (ForumConstants.SEARCH_INDEX_TOPIC_LAST_POST_DATE.equals(key)) {
                Date[] d = (Date[]) val;
                if (d != null && d.length == 2 && (d[0] != null || d[1] != null)) {
                    Term from = (d[0] == null) ? (null) : new Term(key, String.valueOf(d[0].getTime()));
                    Term to = (d[1] == null) ? (null) : new Term(key, String.valueOf(d[1].getTime()));
                    query.add(new BooleanClause(new RangeQuery(from, to, true), BooleanClause.Occur.MUST));
                }
            }
        }
        search(query, hhdlr);
    }

    public void search(Query query, HitsHandler hhdlr) throws Exception {
        File f = getIndexDir();
        IndexReader ireader = getIndexReader();
        try {
            Hits hits = isearcher0.search(query);
            hhdlr.handleHits(hits);
        } finally {
            returnIndexReader(ireader);
        }
    }

    protected File getIndexDir() throws Exception {
        File f = new File(ForumLocal.getForumEngine().getRuntimeDirectory(), "searchindex");
        f.mkdirs();
        return f;
    }

    private void updateMiscInfo() throws Exception {
        ForumDAO dao = ForumLocal.getForumEngine().getForumDAO();
        Misc m = dao.getMisc(ForumConstants.LAST_INDEX_DATETIME_KEY, "0");
        m.setValue(String.valueOf(System.currentTimeMillis()));
    }

    protected MarkupIndexingParser newParser(String name, Reader r, List _tokenstrings, HashSet _hs) throws Exception {
        ForumEngine fe = ForumLocal.getForumEngine();
        ForumRenderEngine fre = ForumLocal.getForumEngine().getForumRenderEngine();
        MarkupRenderContext rc = fre.newRenderContext();
        MarkupIndexingParser mip = new MarkupIndexingParser(fe.getMarkupParserFactory().newMarkupParser(r), rc, fre.getRenderEngine(), name, _tokenstrings, _hs);
        return mip;
    }
}
