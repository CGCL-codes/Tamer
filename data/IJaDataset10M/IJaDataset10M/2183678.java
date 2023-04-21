package at.fhjoanneum.cgvis.data;

import at.fhj.utils.misc.ProgressTracker;

/**
 * Provides access to a dataset. Data can be read, for instance, from a text
 * file, from an XML file, from a database, or obtained from a remote web
 * service.
 * 
 * @author Ilya Boyandin
 */
public interface IDataSource {

    void init(ProgressTracker progress) throws DataSourceException;

    Object query(Query query);

    void storeMetadata(int pointSetIdx, String key, Object data);

    Object getMetadata(int pointSetIdx, String key);

    /**
     * Unload the dataset and free all occupied resources
     */
    void unload();

    DataUID getDataSourceId();
}
