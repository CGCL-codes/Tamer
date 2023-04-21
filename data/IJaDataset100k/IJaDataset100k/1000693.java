package keyintegrity.orm.jpa;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.apache.log4j.Logger;

public abstract class ReliableQuery<T> {

    private static final Logger logger = Logger.getLogger(ReliableQuery.class);

    /**
	 * Количество попыток на выполнение запроса
	 */
    private int traits = 3;

    private final Storage storage;

    public ReliableQuery(Storage storage) {
        this.storage = storage;
    }

    /**
	 * Этот метод запускает на выполнение запрос, определенный в
	 * {@link #query()}
	 * 
	 * @return Результат, который возвращает {@link #query()}
	 * @throws StorageException 
	 */
    public List<T> execute() throws StorageError {
        EntityManager em = storage.getEM();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            List<T> result = query(em);
            tx.commit();
            return result;
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e.getCause() != null && e.getCause().getMessage() != null && e.getCause().getMessage().contains("Communications link failure")) {
                logger.warn("Database connection lost: ", e);
                return queryAfterReconnect();
            }
            throw e;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new StorageError(e);
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    private List<T> queryAfterReconnect() {
        logger.info("Trying to reconnect: " + traits);
        reconnect();
        EntityManager em = storage.getEM();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            List<T> result = query(em);
            tx.commit();
            logger.info("Database connection is up and running now");
            return result;
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            --traits;
            if (traits < 0) {
                throw e;
            }
            em.close();
            return queryAfterReconnect();
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    private void reconnect() {
        if (storage.emf.isOpen()) {
            storage.emf.close();
        }
        storage.emf = storage.createEMF();
    }

    /**
	 * Запрос, который нужно выполнить.
	 * 
	 * @param em {@link EntityManager} на котором будет выполняться этот запрос.
	 * 
	 * @return Результат запроса. В случае если запрос не должен возвращать
	 *         значений (update/insert/delete) значение не определено (может
	 *         быть null).
	 */
    public abstract List<T> query(EntityManager em);
}
