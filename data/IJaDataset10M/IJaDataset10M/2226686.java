package net.simpleframework.ado.lucene;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import net.simpleframework.core.ExecutorRunnable;
import net.simpleframework.core.IApplicationAware;
import net.simpleframework.core.ITaskExecutor;
import net.simpleframework.core.ITaskExecutorAware;
import net.simpleframework.core.ado.AbstractDataObjectManager;
import net.simpleframework.core.ado.DataObjectException;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.core.bean.IIdBeanAware;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.FilteredQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractLuceneManager extends AbstractDataObjectManager implements IApplicationAware {

    private final File indexPath;

    public AbstractLuceneManager(final File indexPath) {
        this.indexPath = indexPath;
    }

    public File getIndexPath() {
        return indexPath;
    }

    private Directory directory;

    public Directory getLuceneDirectory() throws IOException {
        if (directory == null) {
            directory = FSDirectory.open(getIndexPath());
        }
        return directory;
    }

    protected boolean indexExists() throws IOException {
        return IndexReader.indexExists(getLuceneDirectory());
    }

    protected boolean isLocked() throws IOException {
        return IndexWriter.isLocked(getLuceneDirectory());
    }

    private QueryParser qparser;

    protected abstract String[] getQueryParserFields();

    protected QueryParser getQueryParser() {
        if (qparser == null) {
            qparser = new MultiFieldQueryParser(Version.LUCENE_30, getQueryParserFields(), getDefaultAnalyzer());
        }
        return qparser;
    }

    private Analyzer defaultAnalyzer;

    protected Analyzer getDefaultAnalyzer() {
        if (defaultAnalyzer == null) {
            defaultAnalyzer = new SmartChineseAnalyzer(Version.LUCENE_30);
        }
        return defaultAnalyzer;
    }

    protected IndexWriter getIndexWriter() throws IOException {
        return new IndexWriter(getLuceneDirectory(), getDefaultAnalyzer(), !IndexReader.indexExists(getLuceneDirectory()), MaxFieldLength.UNLIMITED);
    }

    protected void objectToDocument(final Object object, final IndexWriter iWriter, final Document doc) throws IOException {
        if (object instanceof IIdBeanAware) {
            final String id = ConvertUtils.toString(((IIdBeanAware) object).getId());
            iWriter.deleteDocuments(new Term("id", id));
            doc.add(new Field("id", id, Field.Store.YES, Field.Index.NOT_ANALYZED));
        }
    }

    static final int OPTIMIZE_SIZE = 10;

    private int optimizeCount;

    public void objects2Documents(final Object... objects) {
        IndexWriter iWriter = null;
        try {
            iWriter = getIndexWriter();
            for (final Object object : objects) {
                final Document document = new Document();
                objectToDocument(object, iWriter, document);
                iWriter.addDocument(document);
                if (++optimizeCount >= OPTIMIZE_SIZE) {
                    iWriter.optimize();
                    optimizeCount = 0;
                }
            }
        } catch (final IOException e) {
            throw DataObjectException.wrapException(e);
        } finally {
            closeWriter(iWriter);
        }
    }

    public void objects2DocumentsBackground(final Object... objects) {
        getTaskExecutor().execute(new ExecutorRunnable() {

            @Override
            protected void task() {
                objects2Documents(objects);
            }
        });
    }

    public void deleteDocument(final Object[] beanIds) {
        IndexWriter iWriter = null;
        try {
            iWriter = getIndexWriter();
            for (Object beanId : beanIds) {
                if (beanId instanceof IIdBeanAware) {
                    beanId = ((IIdBeanAware) beanId).getId();
                }
                iWriter.deleteDocuments(new Term("id", ConvertUtils.toString(beanId)));
            }
        } catch (final IOException e) {
            throw DataObjectException.wrapException(e);
        } finally {
            closeWriter(iWriter);
        }
    }

    protected Query addFilter(Query query, final Filter filter) {
        return new FilteredQuery(query, filter);
    }

    public Sort getSort() {
        return null;
    }

    public void deleteDocumentBackground(final Object[] beanIds) {
        getTaskExecutor().execute(new ExecutorRunnable() {

            @Override
            protected void task() {
                deleteDocument(beanIds);
            }
        });
    }

    protected abstract IDataObjectQuery<?> getAllData();

    public void rebuildAll(final boolean deleteAll) {
        final IDataObjectQuery<?> allData = getAllData();
        if (allData == null) {
            return;
        }
        IndexWriter indexWriter = null;
        try {
            if (deleteAll && indexExists()) {
                try {
                    IndexWriter.unlock(getLuceneDirectory());
                    indexWriter = getIndexWriter();
                    indexWriter.deleteAll();
                } finally {
                    closeWriter(indexWriter);
                }
            }
            indexWriter = getIndexWriter();
            Object object;
            int i = 0;
            while ((object = allData.next()) != null) {
                final Document document = new Document();
                objectToDocument(object, indexWriter, document);
                indexWriter.addDocument(document);
                if (i++ % 10 == 0) {
                    logger.debug("rebuild index: " + object);
                }
            }
            indexWriter.optimize();
        } catch (final IOException e) {
            throw DataObjectException.wrapException(e);
        } finally {
            closeWriter(indexWriter);
        }
    }

    private ITaskExecutor getTaskExecutor() {
        return ((ITaskExecutorAware) getApplication()).getTaskExecutor();
    }

    public void rebuildAllBackground(final boolean deleteAll) {
        getTaskExecutor().execute(new ExecutorRunnable() {

            @Override
            protected void task() {
                rebuildAll(deleteAll);
            }
        });
    }

    public static int QUERY_MIN_LENGTH = 10;

    public String[] getQueryTokens(final String queryString) throws IOException {
        final TokenStream tokenStream = getDefaultAnalyzer().tokenStream("", new StringReader(queryString));
        final ArrayList<String> al = new ArrayList<String>();
        while (tokenStream.incrementToken()) {
            final String term = tokenStream.getAttribute(TermAttribute.class).term();
            if (term != null && term.length() > 1) {
                al.add(term);
            }
        }
        if (al.size() == 0) {
            al.add(queryString);
        }
        return al.toArray(new String[al.size()]);
    }

    public LuceneQuery<?> getLuceneQuery(final String queryString) {
        Query query = null;
        try {
            if (StringUtils.hasText(queryString) && indexExists()) {
                try {
                    final QueryParser qp = getQueryParser();
                    if (qp != null) {
                        query = qp.parse(queryString.trim());
                    }
                } catch (final ParseException e) {
                    logger.warn(e);
                }
            }
            return createLuceneQuery(query);
        } catch (final IOException e) {
            throw DataObjectException.wrapException(e);
        }
    }

    protected abstract LuceneQuery<?> createLuceneQuery(Query query) throws IOException;

    private void closeWriter(final IndexWriter indexWriter) {
        try {
            if (indexWriter != null) {
                indexWriter.close();
            }
        } catch (final Exception e) {
        }
    }
}
