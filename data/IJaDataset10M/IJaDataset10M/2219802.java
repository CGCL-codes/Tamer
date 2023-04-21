package org.jaffa.applications.mylife.admin.components.musiccontentmaintenance;

import org.jaffa.exceptions.ApplicationExceptions;
import org.jaffa.exceptions.FrameworkException;
import org.jaffa.applications.mylife.admin.components.musiccontentmaintenance.dto.*;

/** Interface for MusicContentMaintenance components.
 */
public interface IMusicContentMaintenance {

    /** Persists a new instance of MusicContent.
     * @param input The new values for the domain object.
     * @throws ApplicationExceptions This will be thrown if the input contains invalid data.
     * @throws FrameworkException Indicates some system error.
     * @return The key for the object that gets created.
     */
    public MusicContentMaintenanceCreateOutDto create(MusicContentMaintenanceCreateInDto input) throws FrameworkException, ApplicationExceptions;

    /** Returns the details for MusicContent.
     * @param input The criteria based on which an object will be retrieved.
     * @throws ApplicationExceptions This will be thrown if the criteria contains invalid data.
     * @throws FrameworkException Indicates some system error.
     * @return The object details. A null indicates, the object was not found.
     */
    public MusicContentMaintenanceRetrieveOutDto retrieve(MusicContentMaintenanceRetrieveInDto input) throws FrameworkException, ApplicationExceptions;

    /** Updates an existing instance of MusicContent.
     * @param input The new values for the domain object.
     * @throws ApplicationExceptions This will be thrown if the input contains invalid data.
     * @throws FrameworkException Indicates some system error.
     * @return The key for the object that gets created.
     */
    public void update(MusicContentMaintenanceUpdateInDto input) throws FrameworkException, ApplicationExceptions;

    /** Deletes an existing instance of MusicContent.
     * @param input The key values for the domain object to be deleted.
     * @throws ApplicationExceptions This will be thrown if the input contains invalid data.
     * @throws FrameworkException Indicates some system error.
     * @return The key for the object that gets created.
     */
    public void delete(MusicContentMaintenanceDeleteInDto input) throws FrameworkException, ApplicationExceptions;

    /**
     * This should be invoked, when done with the component.
     */
    public void destroy();
}
