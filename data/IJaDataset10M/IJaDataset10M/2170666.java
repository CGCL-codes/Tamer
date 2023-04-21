package vars.services;

/**
 *
 * @author brian
 */
public interface DataPersistenceService {

    /**
     * TODO: Add JavaDoc
     *
     * @param object
     */
    public void makePersistent(Object object);

    /**
     * TODO: Add JavaDoc
     *
     * @param object
     */
    public void makeTransient(Object object);
}
