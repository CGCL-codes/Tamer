package DL.DataStore;

/**
 * Generic data store interface definition
 *
 */
public interface IDataStore {

    /** Open datab store session */
    void Open() throws Exception;

    /** Close current data store session */
    void Close() throws Exception;

    /** Get store description */
    String getDesc() throws Exception;
}
