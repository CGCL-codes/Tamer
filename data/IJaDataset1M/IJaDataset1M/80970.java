package tgreiner.amy.chess.book;

/**
 * Encapsulates lookups in the ECO database.
 *
 * @author <a href = "mailto:thorsten.greiner@googlemail.com">Thorsten Greiner</a>
 */
public interface EcoDB {

    /**
     * Retrieve an ECO entry.
     *
     * @param hash the hash key
     * @return the EcoEntry or <code>null</code> if non found.
     * @throws Exception if an I/O error occurs
     */
    EcoEntry get(long hash) throws Exception;

    /**
     * Put an entry.
     *
     * @param hash the hash code
     * @param entry the entry
     * @throws Exception if an I/O error occurs
     */
    void put(long hash, EcoEntry entry) throws Exception;

    /**
     * Commit changes.
     *
     * @throws Exception if an I/O error occurs
     */
    void commit() throws Exception;
}
