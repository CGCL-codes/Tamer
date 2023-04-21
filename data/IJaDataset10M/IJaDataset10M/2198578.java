package org.jaffa.components.audit.components.audittransactionmaintenance;

import org.jaffa.exceptions.ApplicationExceptions;
import org.jaffa.exceptions.FrameworkException;
import org.jaffa.components.audit.components.audittransactionmaintenance.dto.*;

/** Interface for AuditTransactionMaintenance components.
 */
public interface IAuditTransactionMaintenance {

    /** This method is used to perform prevalidations before creating a new instance of AuditTransaction.
     * @param input The new values for the domain object.
     * @throws ApplicationExceptions This will be thrown if the input contains invalid data.
     * @throws FrameworkException Indicates some system error.
     * @return The object details.
     */
    public AuditTransactionMaintenancePrevalidateOutDto prevalidateCreate(AuditTransactionMaintenanceCreateInDto input) throws FrameworkException, ApplicationExceptions;

    /** Persists a new instance of AuditTransaction.
     * @param input The new values for the domain object.
     * @throws ApplicationExceptions This will be thrown if the input contains invalid data.
     * @throws FrameworkException Indicates some system error.
     * @return The object details.
     */
    public AuditTransactionMaintenanceRetrieveOutDto create(AuditTransactionMaintenanceCreateInDto input) throws FrameworkException, ApplicationExceptions;

    /** Returns the details for AuditTransaction.
     * @param input The criteria based on which an object will be retrieved.
     * @throws ApplicationExceptions This will be thrown if the criteria contains invalid data.
     * @throws FrameworkException Indicates some system error.
     * @return The object details. A null indicates, the object was not found.
     */
    public AuditTransactionMaintenanceRetrieveOutDto retrieve(AuditTransactionMaintenanceRetrieveInDto input) throws FrameworkException, ApplicationExceptions;

    /** This method is used to perform prevalidations before updating an existing instance of AuditTransaction.
     * @param input The new values for the domain object.
     * @throws ApplicationExceptions This will be thrown if the input contains invalid data.
     * @throws FrameworkException Indicates some system error.
     * @return The object details.
     */
    public AuditTransactionMaintenancePrevalidateOutDto prevalidateUpdate(AuditTransactionMaintenanceUpdateInDto input) throws FrameworkException, ApplicationExceptions;

    /** Updates an existing instance of AuditTransaction.
     * @param input The new values for the domain object.
     * @throws ApplicationExceptions This will be thrown if the input contains invalid data.
     * @throws FrameworkException Indicates some system error.
     * @return The object details.
     */
    public AuditTransactionMaintenanceRetrieveOutDto update(AuditTransactionMaintenanceUpdateInDto input) throws FrameworkException, ApplicationExceptions;

    /** Deletes an existing instance of AuditTransaction.
     * @param input The key values for the domain object to be deleted.
     * @throws ApplicationExceptions This will be thrown if the input contains invalid data.
     * @throws FrameworkException Indicates some system error.
     */
    public void delete(AuditTransactionMaintenanceDeleteInDto input) throws FrameworkException, ApplicationExceptions;

    /**
     * This should be invoked, when done with the component.
     */
    public void destroy();
}
