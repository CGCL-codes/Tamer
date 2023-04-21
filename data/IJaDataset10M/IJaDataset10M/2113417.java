package lempinen.neatseeker.core;

import lempinen.util.Configuration;
import java.io.InputStream;
import java.io.StreamTokenizer;
import java.io.IOException;

/**
 * Defines the interface for various Indexers.
 * <p>
 * The AbstractIndexer class provides a barebones implementation of this
 * interface. Your indexers should inherit AbstractIndexer.
 *
 * @see lempinen.neatseeker.core.AbstractIndexer
 * @version $Id: Indexer.java,v 1.6 2000/10/22 17:00:30 lempinen Exp $
 */
public interface Indexer {

    /**
    * Initialises the Indexer if an empty constructor was used.
    */
    public void init(Configuration c) throws IOException;

    /**
    * Sets the Collector partner.
    */
    public void setCollector(Collector c);

    /**
    * Gets the Collector partner.
    */
    public Collector getCollector();

    /**
    * Sets the Configuration used by this Indexer.
    */
    public void setConfiguration(Configuration c) throws IOException;

    /**
    * Returns the Configuration used by this Indexer.
    */
    public Configuration getConfiguration();

    /**
    * Returns the Repository used by this Indexer.
    */
    public Repository getRepository();

    /**
    * Adds an entry in the index and returns true on success.
    */
    public boolean add(Entry entry);

    /**
    * Starts the indexing process.
    */
    public void start() throws IOException;

    /**
    * Indexes the data in the InputStream.
    * <p>
    * The Collector partner must provide the Indexer with two
    * arguments: the InputStream from which the data can be read as well as
    * a URI that will be entered in the index.
    */
    public abstract void process(InputStream in, String uri) throws IOException;
}
