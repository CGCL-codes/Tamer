package ikube.action;

import ikube.index.IndexManager;
import ikube.model.IndexContext;
import ikube.service.SearcherWebService;
import ikube.toolkit.FileUtilities;
import ikube.toolkit.Logging;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiSearcher;
import org.apache.lucene.search.Searchable;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * This action is called to open the searcher on potentially new indexes. The close operation should run just before this action. In that
 * case the searcher in the index context will be null which is an indication that the searcher should be opened on the new index. The
 * trigger then for this action to complete successfully is the searcher in the context being null.
 * 
 * This action will find the latest index directory based on the folder name which is a long(the time the index was started), find all the
 * server directories in the directory and open a searchable on each of the 'server indexes'. A multi searcher will be opened on the
 * SEARCHABLES and this will be set in the index context. An even will be fired to alert all interested parties that there is a new searcher
 * and they can perform whatever logic the need to, like the {@link SearcherWebService} which will then open the single and multi search
 * objects on the new multi searcher.
 * 
 * @author Michael Couck
 * @since 31.10.10
 * @version 01.00
 */
public class Open extends Action<IndexContext<?>, Boolean> {

    /**
	 * {@inheritDoc}
	 */
    @Override
    public boolean executeInternal(final IndexContext<?> indexContext) {
        boolean succeeded = Boolean.FALSE;
        ikube.model.Action action = null;
        try {
            action = start(indexContext.getIndexName(), "");
            return openOnFile(indexContext);
        } finally {
            stop(action);
            logger.info("Succeeded : " + succeeded);
        }
    }

    private boolean openOnFile(final IndexContext<?> indexContext) {
        ArrayList<Searchable> searchers = new ArrayList<Searchable>();
        String indexDirectoryPath = IndexManager.getIndexDirectoryPath(indexContext);
        File latestIndexDirectory = FileUtilities.getLatestIndexDirectory(indexDirectoryPath);
        if (latestIndexDirectory == null) {
            logger.info("No indexes : " + indexDirectoryPath);
            return Boolean.FALSE;
        }
        closeSearchables(indexContext);
        File[] serverIndexDirectories = latestIndexDirectory.listFiles();
        Directory directory = null;
        IndexReader reader = null;
        Searchable searcher = null;
        boolean exceptionOpening = Boolean.TRUE;
        for (File serverIndexDirectory : serverIndexDirectories) {
            try {
                directory = FSDirectory.open(serverIndexDirectory);
                boolean exists = IndexReader.indexExists(directory);
                boolean locked = IndexWriter.isLocked(directory);
                logger.info("Exists : " + exists + ", locked : " + locked);
                if (!exists || locked) {
                    close(directory, reader, searcher);
                    continue;
                }
                reader = IndexReader.open(directory, Boolean.TRUE);
                searcher = new IndexSearcher(reader);
                searchers.add(searcher);
                exceptionOpening = Boolean.FALSE;
                logger.info(Logging.getString("Opened searcher on : ", serverIndexDirectory, "exists : ", exists, "locked : ", locked));
            } catch (Exception e) {
                logger.error("Exception opening directory : " + serverIndexDirectory, e);
            } finally {
                if (exceptionOpening) {
                    close(directory, reader, searcher);
                    boolean removed = searchers.remove(searcher);
                    logger.info("Removed searcher : " + removed + ", " + searcher);
                }
            }
        }
        return open(indexContext, searchers);
    }

    private boolean open(IndexContext<?> indexContext, List<Searchable> searchers) {
        try {
            if (!searchers.isEmpty()) {
                Searchable[] searchables = searchers.toArray(new IndexSearcher[searchers.size()]);
                MultiSearcher multiSearcher = new MultiSearcher(searchables);
                indexContext.getIndex().setMultiSearcher(multiSearcher);
                return Boolean.TRUE;
            }
        } catch (Exception e) {
            logger.error("Exception opening the multi searcher", e);
        }
        return Boolean.FALSE;
    }
}
