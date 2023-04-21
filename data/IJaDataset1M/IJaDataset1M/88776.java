package de.nava.informa.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import de.nava.informa.core.ChannelIF;
import de.nava.informa.core.ItemIF;

/**
 * Utility class which allows to generate respective maintain a
 * fulltext index for news items.
 *
 * @author Niko Schmuck (niko@nava.de)
 */
public final class ChannelIndexer {

    private static Log logger = LogFactory.getLog(ChannelIndexer.class);

    private String indexDir;

    private int nrOfIndexedItems;

    private Analyzer analyzer;

    /**
   * Constructor which allows to specify the index directory.
   * For the full-text indexing process the lucene
   * {@link org.apache.lucene.analysis.standard.StandardAnalyzer}
   * is used.
   *
   * @param indexDir - The directory in which the index files are stored.
   */
    public ChannelIndexer(String indexDir) {
        this.indexDir = indexDir;
        this.nrOfIndexedItems = 0;
        this.analyzer = new StandardAnalyzer();
    }

    /**
   * Index all news items contained in the given channels.
   *
   * @param createNewIndex - wether a new index should be generated or
   *        an existant one should be taken into account
   * @param channels - a collection of ChannelIF objects
   */
    public void indexChannels(boolean createNewIndex, Collection channels) throws java.io.IOException {
        Collection<ItemIF> items = new ArrayList<ItemIF>();
        Iterator itC = channels.iterator();
        while (itC.hasNext()) {
            ChannelIF channel = (ChannelIF) itC.next();
            if (logger.isDebugEnabled()) {
                logger.debug("Searching channel " + channel + " for items.");
            }
            items.addAll(channel.getItems());
        }
        if (!items.isEmpty()) {
            indexItems(createNewIndex, items);
        } else {
            logger.info("No items found for indexing.");
        }
    }

    /**
   * Index all given news items.
   *
   * @param createNewIndex - Wether a new index should be generated or
   *        an existant one should be taken into account.
   * @param items - A collection of ItemIF objects.
   */
    public void indexItems(boolean createNewIndex, Collection<ItemIF> items) throws java.io.IOException {
        logger.info("Start writing index.");
        IndexWriter writer = new IndexWriter(indexDir, analyzer, createNewIndex);
        Iterator<ItemIF> itI = items.iterator();
        while (itI.hasNext()) {
            ItemIF item = itI.next();
            if (logger.isDebugEnabled()) {
                logger.debug("Add item " + item + " to index.");
            }
            writer.addDocument(ItemDocument.makeDocument(item));
        }
        writer.optimize();
        nrOfIndexedItems = writer.docCount();
        writer.close();
        logger.info("Finished writing index.");
    }

    /**
   * Returns the number of documents that were in the index last time
   * the index operation was performed.
   *
   * Note: Use only directly after the indexing process, otherwise the
   * return value may be wrong.
   */
    public int getNrOfIndexedItems() {
        return nrOfIndexedItems;
    }

    public void setIndexDir(String indexDir) {
        this.indexDir = indexDir;
    }

    public String getIndexDir() {
        return indexDir;
    }
}
