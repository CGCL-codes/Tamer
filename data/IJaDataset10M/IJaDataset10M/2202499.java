package net.sf.orexio.jdcp.storage;

import java.io.File;
import java.io.IOException;
import net.sf.orexio.jdcp.common.SystemUtilities;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;

/**
 * @author alois.cochard@gmail.com
 */
public class LuceneIndexManager {

    private static final String INDEX_SUFFIX = "Index";

    private String workingFolderPath = null;

    private String name = null;

    private IndexWriter indexWriter = null;

    private IndexSearcher indexSearcher = null;

    LuceneIndexManager(String workingFolderPath, String name) throws IOException {
        this.workingFolderPath = workingFolderPath;
        this.name = name;
        init();
    }

    private void init() throws IOException {
        if (!workingFolderPath.endsWith(File.separator)) {
            workingFolderPath += File.separator;
        }
        workingFolderPath += name + INDEX_SUFFIX + File.separator;
        SystemUtilities.createFolder(workingFolderPath);
        indexWriter = new IndexWriter(FSDirectory.getDirectory(workingFolderPath), new StandardAnalyzer(), IndexWriter.MaxFieldLength.UNLIMITED);
        indexSearcher = new IndexSearcher(workingFolderPath);
    }

    final IndexWriter getIndexWriter() {
        return indexWriter;
    }

    final IndexSearcher getIndexSearcher() throws IOException {
        IndexReader indexReader = indexSearcher.getIndexReader().reopen();
        if (indexReader != null) {
            indexSearcher.close();
            indexSearcher = new IndexSearcher(indexReader);
        }
        return indexSearcher;
    }
}
