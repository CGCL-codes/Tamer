package be.vds.jtbdive.core.integration.businessdelegate.interfaces;

import java.util.List;
import be.vds.jtbdive.core.core.DiveSite;
import be.vds.jtbdive.core.core.DocumentFormat;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.core.exceptions.DiveLocationUsedException;

/**
 * This business delegate is used by the model to interact with persistence
 * layer. The business delegate cares for any connection to an application
 * server, a database, a file system, ...
 * 
 * @author gautier
 * 
 */
public interface DiveLocationBusinessDelegate {

    public List<DiveSite> findAllDiveLocations() throws DataStoreException;

    public List<DiveSite> findDiveLocationsByName(String name) throws DataStoreException;

    public boolean deleteDiveLocation(DiveSite diveLocation) throws DataStoreException, DiveLocationUsedException;

    public DiveSite saveDiveLocation(DiveSite diveLocation) throws DataStoreException;

    public DiveSite updateDiveLocation(DiveSite diveLocation) throws DataStoreException;

    public void initialize();

    public boolean mergeDiveLocations(DiveSite diveLocationToKeep, DiveSite diveLocationToDelete) throws DataStoreException;

    public DiveSite findDiveLocationsById(long id, short loadType) throws DataStoreException;

    public byte[] loadDocumentContent(long documentId, DocumentFormat format) throws DataStoreException;
}
