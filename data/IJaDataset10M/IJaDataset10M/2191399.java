package net.sf.lucis.core;

import java.io.IOException;
import java.util.logging.Logger;
import net.sf.lucis.core.Batch.Addition;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.store.LockObtainFailedException;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

public final class DefaultWriter<T> implements Writer<T> {

    /** Log. */
    private Logger log = Logger.getLogger(getClass().getName());

    public DefaultWriter() {
    }

    public void setLogName(final String name) {
        this.log = Logger.getLogger(name);
    }

    public IndexStatus write(Store<T> store, Indexer<T> indexer) {
        Preconditions.checkNotNull(indexer, "An indexer must be provided.");
        try {
            final T oldCP = store.getCheckpoint();
            final Batch<T> batch;
            try {
                batch = indexer.index(oldCP);
            } catch (RuntimeException e) {
                throw new BatchException(e);
            }
            final T newCP = batch.getCheckpoint();
            if (Objects.equal(oldCP, newCP)) {
                return null;
            }
            if (!batch.isEmpty()) {
                final Analyzer analyzer = indexer.getAnalyzer();
                final IndexWriter writer = new IndexWriter(store.getDirectory(), analyzer, MaxFieldLength.UNLIMITED);
                boolean ok = false;
                try {
                    for (Term term : batch.getDeletions()) {
                        writer.deleteDocuments(term);
                    }
                    for (Addition addition : batch.getAdditions()) {
                        writer.addDocument(addition.getDocument(), Objects.firstNonNull(addition.getAnalyzer(), analyzer));
                    }
                    writer.commit();
                    ok = true;
                    writer.optimize();
                } finally {
                    if (!ok) {
                        rollback(writer);
                    }
                }
                writer.close();
            }
            store.setCheckpoint(newCP);
            return IndexStatus.OK;
        } catch (LockObtainFailedException le) {
            return IndexStatus.LOCKED;
        } catch (CorruptIndexException ce) {
            return IndexStatus.CORRUPT;
        } catch (IOException ioe) {
            return IndexStatus.IOERROR;
        } catch (BatchException be) {
            throw be;
        } catch (Exception e) {
            return IndexStatus.ERROR;
        }
    }

    private void rollback(IndexWriter writer) {
        try {
            writer.rollback();
        } catch (Exception e) {
        }
    }
}
