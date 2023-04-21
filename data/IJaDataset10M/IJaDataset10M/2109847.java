package org.hibnet.lune.core.local;

import java.io.IOException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.ConcurrentMergeScheduler;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.LogByteSizeMergePolicy;
import org.apache.lucene.index.MergePolicy;
import org.apache.lucene.index.MergeScheduler;
import org.apache.lucene.search.Similarity;
import org.apache.lucene.store.LockObtainFailedException;
import org.hibnet.lune.core.WriterFactory;

/**
 * The factory of local writer
 */
public class LocalWriterFactory implements WriterFactory<LocalIndex> {

    private int maxBufferedDeleteTerms = IndexWriter.DEFAULT_MAX_BUFFERED_DELETE_TERMS;

    private int maxBufferedDocs = IndexWriter.DEFAULT_MAX_BUFFERED_DOCS;

    private int maxFieldLength = IndexWriter.DEFAULT_MAX_FIELD_LENGTH;

    private double ramBufferSizeMB = IndexWriter.DEFAULT_RAM_BUFFER_SIZE_MB;

    private int termIndexInterval = IndexWriter.DEFAULT_TERM_INDEX_INTERVAL;

    private boolean useCompoundFile = true;

    private long writeLockTimeout = IndexWriter.WRITE_LOCK_TIMEOUT;

    private Similarity similarity = Similarity.getDefault();

    private Analyzer analyzer = new StandardAnalyzer();

    private MergePolicy mergePolicy = new LogByteSizeMergePolicy();

    private MergeScheduler mergeScheduler = new ConcurrentMergeScheduler();

    public LocalWriter getWriter(LocalIndex index) throws LockObtainFailedException, CorruptIndexException, IOException {
        LocalWriter writer = new LocalWriter(index, analyzer);
        writer.setMaxBufferedDeleteTerms(maxBufferedDeleteTerms);
        writer.setMaxBufferedDocs(maxBufferedDocs);
        writer.setMaxFieldLength(maxFieldLength);
        writer.setMergePolicy(mergePolicy);
        writer.setMergeScheduler(mergeScheduler);
        writer.setRAMBufferSizeMB(ramBufferSizeMB);
        writer.setSimilarity(similarity);
        writer.setTermIndexInterval(termIndexInterval);
        writer.setUseCompoundFile(useCompoundFile);
        writer.setWriteLockTimeout(writeLockTimeout);
        return writer;
    }

    /**
     * @return the analyzer
     */
    public Analyzer getAnalyzer() {
        return analyzer;
    }

    /**
     * @param analyzer
     *            the analyzer to set
     */
    public void setAnalyzer(Analyzer analyzer) {
        this.analyzer = analyzer;
    }

    /**
     * @return the maxBufferedDeleteTerms
     */
    public int getMaxBufferedDeleteTerms() {
        return maxBufferedDeleteTerms;
    }

    /**
     * @param maxBufferedDeleteTerms
     *            the maxBufferedDeleteTerms to set
     */
    public void setMaxBufferedDeleteTerms(int maxBufferedDeleteTerms) {
        this.maxBufferedDeleteTerms = maxBufferedDeleteTerms;
    }

    /**
     * @return the maxBufferedDocs
     */
    public int getMaxBufferedDocs() {
        return maxBufferedDocs;
    }

    /**
     * @param maxBufferedDocs
     *            the maxBufferedDocs to set
     */
    public void setMaxBufferedDocs(int maxBufferedDocs) {
        this.maxBufferedDocs = maxBufferedDocs;
    }

    /**
     * @return the maxFieldLength
     */
    public int getMaxFieldLength() {
        return maxFieldLength;
    }

    /**
     * @param maxFieldLength
     *            the maxFieldLength to set
     */
    public void setMaxFieldLength(int maxFieldLength) {
        this.maxFieldLength = maxFieldLength;
    }

    /**
     * @return the mergePolicy
     */
    public MergePolicy getMergePolicy() {
        return mergePolicy;
    }

    /**
     * @param mergePolicy
     *            the mergePolicy to set
     */
    public void setMergePolicy(MergePolicy mergePolicy) {
        this.mergePolicy = mergePolicy;
    }

    /**
     * @return the mergeScheduler
     */
    public MergeScheduler getMergeScheduler() {
        return mergeScheduler;
    }

    /**
     * @param mergeScheduler
     *            the mergeScheduler to set
     */
    public void setMergeScheduler(MergeScheduler mergeScheduler) {
        this.mergeScheduler = mergeScheduler;
    }

    /**
     * @return the ramBufferSizeMB
     */
    public double getRamBufferSizeMB() {
        return ramBufferSizeMB;
    }

    /**
     * @param ramBufferSizeMB
     *            the ramBufferSizeMB to set
     */
    public void setRamBufferSizeMB(double ramBufferSizeMB) {
        this.ramBufferSizeMB = ramBufferSizeMB;
    }

    /**
     * @return the termIndexInterval
     */
    public int getTermIndexInterval() {
        return termIndexInterval;
    }

    /**
     * @param termIndexInterval
     *            the termIndexInterval to set
     */
    public void setTermIndexInterval(int termIndexInterval) {
        this.termIndexInterval = termIndexInterval;
    }

    /**
     * @return the useCompoundFile
     */
    public boolean isUseCompoundFile() {
        return useCompoundFile;
    }

    /**
     * @param useCompoundFile
     *            the useCompoundFile to set
     */
    public void setUseCompoundFile(boolean useCompoundFile) {
        this.useCompoundFile = useCompoundFile;
    }

    /**
     * @return the writeLockTimeout
     */
    public long getWriteLockTimeout() {
        return writeLockTimeout;
    }

    /**
     * @param writeLockTimeout
     *            the writeLockTimeout to set
     */
    public void setWriteLockTimeout(long writeLockTimeout) {
        this.writeLockTimeout = writeLockTimeout;
    }

    /**
     * @return the similarity
     */
    public Similarity getSimilarity() {
        return similarity;
    }

    /**
     * @param similarity
     *            the similarity to set
     */
    public void setSimilarity(Similarity similarity) {
        this.similarity = similarity;
    }
}
