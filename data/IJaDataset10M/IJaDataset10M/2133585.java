package fr.insa.rennes.pelias.pexecutor.sorter;

import com.sun.data.provider.SortCriteria;
import fr.insa.rennes.pelias.framework.Execution;
import fr.insa.rennes.pelias.platform.IRepository;

/**
 *Implémentation de SorCriteria, spécifique à l'état d'une Execution
 * @author Julien Grimault
 */
public abstract class ExecutionSortCriteria extends SortCriteria {

    private IRepository<Execution> repository;

    /**
     *
     * @param executions
     */
    public ExecutionSortCriteria(IRepository<Execution> repository) {
        super();
        this.repository = repository;
    }

    /**
     * 
     * @param executions
     * @param fieldId
     * @param ascending
     */
    public ExecutionSortCriteria(IRepository<Execution> repository, boolean ascending) {
        super("", ascending);
        this.repository = repository;
    }

    /**
     * @return the repository
     */
    public IRepository<Execution> getRepository() {
        return repository;
    }

    /**
     * @param repository the repository to set
     */
    public void setRepository(IRepository<Execution> repository) {
        this.repository = repository;
    }
}
