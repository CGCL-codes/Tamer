package org.geonetwork.gaap.dao;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;
import org.geonetwork.gaap.domain.operation.Operation;
import java.util.List;

/**
 * Dao to manage Operation entities
 *
 * @author Jose
 */
@Transactional
public class OperationDao extends HibernateDaoSupport {

    /**
     * Saves an operation
     *
     * @param operation Operation to save
     */
    public void saveOperation(Operation operation) {
        this.getHibernateTemplate().save(operation);
    }

    public void mergeOperation(Operation operation) {
        this.getHibernateTemplate().merge(operation);
    }

    /**
     * Deletes an operation
     *
     * @param operation Operation to delete
     */
    public void deleteOperation(Operation operation) {
        this.getHibernateTemplate().delete(operation);
    }

    /**
     * Returns a list of all operations
     *
     */
    @SuppressWarnings("unchecked")
    public List<Operation> loadAllOperations() {
        return this.getHibernateTemplate().loadAll(Operation.class);
    }

    /**
     * Find an operation by internal id
     *
     * @param id    Internal operation id
     * @return      Finded operation using id, null if not finded
     */
    public Operation findOperationById(long id) {
        return (Operation) this.getHibernateTemplate().get(Operation.class, id);
    }

    /**
     * Find an operation by name
     *
     * @param name  Internal operation name
     * @return      Finded operation using name, null if not finded
     */
    public Operation findOperationByName(String name) {
        String queryTemplate = "from org.geonetwork.gaap.domain.operation.Operation where name = ?";
        List<Operation> operations = this.getHibernateTemplate().find(queryTemplate, name);
        if (!operations.isEmpty()) {
            return operations.get(0);
        } else {
            return null;
        }
    }
}
