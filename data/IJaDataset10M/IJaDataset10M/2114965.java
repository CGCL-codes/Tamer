package dk.i2m.converge.ejb.facades;

import dk.i2m.converge.core.search.IndexQueueEntry;
import dk.i2m.converge.core.search.QueueEntryOperation;
import dk.i2m.converge.core.search.QueueEntryType;
import dk.i2m.converge.core.search.SearchEngineIndexingException;
import dk.i2m.converge.domain.search.SearchResults;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 * Local interface for the Search Engine service.
 *
 * @author Allan Lykke Christensen
 */
@Local
public interface SearchEngineLocal {

    /**
     * Adds an entity to the index queue.
     * 
     * @param type
     *          Type of entity
     * @param id
     *          Unique identifier of the entity
     * @param operation
     *          Type of operation to perform on the entity
     * @return {@link IndexQueueEntry} added to the queue
     */
    IndexQueueEntry addToIndexQueue(QueueEntryType type, Long id, QueueEntryOperation operation);

    /**
     * Gets a {@link List} of all items in the index queue.
     * 
     * @return {@link List} of items in the index queue
     */
    List<IndexQueueEntry> getIndexQueue();

    /**
     * Remove an item from the indexing queue.
     * 
     * @param id 
     *          Unique identifier of the item to remove
     */
    void removeFromQueue(Long id);

    /**
     * Queries the search engine.
     *
     * @param query
     *          Query string
     * @param start
     *          First record to retrieve
     * @param rows
     *          Number of rows to retrieve
     * @param filterQueries 
     *          Filter queries to include in the search
     * @return {@link SearchResults} matching the {@code query}
     */
    SearchResults search(String query, int start, int rows, String... filterQueries);

    /**
     * Queries the search engine.
     *
     * @param query
     *          Query string
     * @param start
     *          First record to retrieve
     * @param rows
     *          Number of rows to retrieve
     * @param sortField
     *          Field to sort by
     * @param sortOrder
     *          Ascending ({@code true}) or descending ({@code false})
     * @param filterQueries 
     *          Filter queries to include in the search
     * @return {@link SearchResults} matching the {@code query}
     */
    SearchResults search(String query, int start, int rows, String sortField, boolean sortOrder, String... filterQueries);

    SearchResults search(String query, int start, int rows, String sortField, boolean sortOrder, Date dateFrom, Date dateTo, String... filterQueries);

    /**
     * Processes the queue of items to index.
     */
    void processIndexingQueue();

    /**
     * Optimises the search engine index.
     * 
     * @throws SearchEngineIndexingException 
     *          If the index could not be indexed
     */
    void optimizeIndex() throws SearchEngineIndexingException;

    byte[] generateReport(dk.i2m.converge.domain.search.SearchResults results);

    void removeItem(java.lang.Long id);
}
