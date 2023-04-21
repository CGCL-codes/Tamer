package no.ugland.utransprod.service.impl;

import java.io.Serializable;
import java.util.List;
import no.ugland.utransprod.dao.EmployeeTypeDAO;
import no.ugland.utransprod.model.EmployeeType;
import no.ugland.utransprod.service.EmployeeTypeManager;

/**
 * Implemetasjons av serviceklasse for tabell EMPLOYEE_TYPE.
 * @author atle.brekka
 */
public class EmployeeTypeManagerImpl extends ManagerImpl<EmployeeType> implements EmployeeTypeManager {

    /**
     * @see no.ugland.utransprod.service.EmployeeTypeManager#findAll()
     */
    public final List<EmployeeType> findAll() {
        return dao.getObjects("employeeTypeName");
    }

    /**
     * @param employeeType
     * @return ansattyper
     */
    public final List<EmployeeType> findByEmployeeType(final EmployeeType employeeType) {
        return dao.findByExampleLike(employeeType);
    }

    /**
     * @param object
     * @return ansattyper
     * @see no.ugland.utransprod.service.OverviewManager#findByObject(java.lang.Object)
     */
    public final List<EmployeeType> findByObject(final EmployeeType object) {
        return findByEmployeeType(object);
    }

    /**
     * @param object
     * @see no.ugland.utransprod.service.OverviewManager#refreshObject(java.lang.Object)
     */
    public final void refreshObject(final EmployeeType object) {
        ((EmployeeTypeDAO) dao).refreshObject(object);
    }

    /**
     * @param employeeType
     */
    public final void removeEmployeeType(final EmployeeType employeeType) {
        dao.removeObject(employeeType.getEmployeeTypeId());
    }

    /**
     * @param object
     * @see no.ugland.utransprod.service.OverviewManager#removeObject(java.lang.Object)
     */
    public final void removeObject(final EmployeeType object) {
        removeEmployeeType(object);
    }

    /**
     * @param employeeType
     */
    public final void saveEmployeeType(final EmployeeType employeeType) {
        dao.saveObject(employeeType);
    }

    /**
     * @param object
     * @see no.ugland.utransprod.service.OverviewManager#saveObject(java.lang.Object)
     */
    public final void saveObject(final EmployeeType object) {
        saveEmployeeType(object);
    }

    @Override
    protected Serializable getObjectId(EmployeeType object) {
        return object.getEmployeeTypeId();
    }
}
