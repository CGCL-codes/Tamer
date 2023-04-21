package net.sf.joafip.jpa;

import java.io.File;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.Metamodel;
import net.sf.joafip.service.FilePersistenceBuilder;
import net.sf.joafip.service.FilePersistenceClassNotFoundException;
import net.sf.joafip.service.FilePersistenceDataCorruptedException;
import net.sf.joafip.service.FilePersistenceException;
import net.sf.joafip.service.FilePersistenceInvalidClassException;
import net.sf.joafip.service.FilePersistenceNotSerializableException;
import net.sf.joafip.service.IFilePersistence;

/**
 * @author Jean-Marc Vanel jeanmarc.vanel@gmail.com
 * 
 */
@SuppressWarnings("PMD")
public class JoaFipEntityManager implements EntityManager {

    @SuppressWarnings({ "unused", "rawtypes" })
    private Map map;

    private IFilePersistence filePersistence;

    private File storageDirectory;

    private Deque<JoaFipEntityTransaction> transactions;

    /**
	 * @param map
	 */
    @SuppressWarnings("rawtypes")
    public JoaFipEntityManager(Map map) {
        this.map = map;
        transactions = new ArrayDeque<JoaFipEntityTransaction>();
        String dir = System.getProperty("java.io.tmpdir") + File.separator + "joafip_jpa_test";
        File file_dir = new File(dir);
        file_dir.mkdirs();
        storageDirectory = file_dir;
        try {
            final FilePersistenceBuilder builder = new FilePersistenceBuilder();
            builder.setPathName(storageDirectory.getPath());
            builder.setGarbageManagement(false);
            builder.setRemoveFiles(false);
            filePersistence = builder.build();
        } catch (FilePersistenceException e) {
            e.printStackTrace();
        } catch (FilePersistenceInvalidClassException e) {
            e.printStackTrace();
        } catch (FilePersistenceNotSerializableException e) {
            e.printStackTrace();
        } catch (FilePersistenceClassNotFoundException e) {
            e.printStackTrace();
        } catch (FilePersistenceDataCorruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void persist(Object entity) {
        try {
            getCurrentTransaction().getSession().setObject(Integer.toString(entity.hashCode()), entity);
        } catch (FilePersistenceException e) {
            e.printStackTrace();
        }
    }

    JoaFipEntityTransaction getCurrentTransaction() {
        return transactions.peek();
    }

    @Override
    public <T> T merge(T entity) {
        return null;
    }

    @Override
    public void remove(Object entity) {
    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey) {
        return null;
    }

    @Override
    public <T> T getReference(Class<T> entityClass, Object primaryKey) {
        return null;
    }

    @Override
    public void flush() {
    }

    @Override
    public void setFlushMode(FlushModeType flushMode) {
    }

    @Override
    public FlushModeType getFlushMode() {
        return null;
    }

    @Override
    public void lock(Object entity, LockModeType lockMode) {
    }

    @Override
    public void refresh(Object entity) {
    }

    @Override
    public void clear() {
    }

    @Override
    public boolean contains(Object entity) {
        return false;
    }

    @Override
    public Query createQuery(String qlString) {
        return null;
    }

    @Override
    public Query createNamedQuery(String name) {
        return null;
    }

    @Override
    public Query createNativeQuery(String sqlString) {
        return null;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Query createNativeQuery(String sqlString, Class resultClass) {
        return null;
    }

    @Override
    public Query createNativeQuery(String sqlString, String resultSetMapping) {
        return null;
    }

    @Override
    public void joinTransaction() {
    }

    @Override
    public Object getDelegate() {
        return null;
    }

    @Override
    public void close() {
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public EntityTransaction getTransaction() {
        JoaFipEntityTransaction joaFipEntityTransaction = new JoaFipEntityTransaction(filePersistence);
        transactions.push(joaFipEntityTransaction);
        return joaFipEntityTransaction;
    }

    @Override
    public <T> TypedQuery<T> createNamedQuery(String arg0, Class<T> arg1) {
        return null;
    }

    @Override
    public <T> TypedQuery<T> createQuery(CriteriaQuery<T> arg0) {
        return null;
    }

    @Override
    public <T> TypedQuery<T> createQuery(String arg0, Class<T> arg1) {
        return null;
    }

    @Override
    public void detach(Object arg0) {
    }

    @Override
    public <T> T find(Class<T> arg0, Object arg1, Map<String, Object> arg2) {
        return null;
    }

    @Override
    public <T> T find(Class<T> arg0, Object arg1, LockModeType arg2) {
        return null;
    }

    @Override
    public <T> T find(Class<T> arg0, Object arg1, LockModeType arg2, Map<String, Object> arg3) {
        return null;
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return null;
    }

    @Override
    public EntityManagerFactory getEntityManagerFactory() {
        return null;
    }

    @Override
    public LockModeType getLockMode(Object arg0) {
        return null;
    }

    @Override
    public Metamodel getMetamodel() {
        return null;
    }

    @Override
    public Map<String, Object> getProperties() {
        return null;
    }

    @Override
    public void lock(Object arg0, LockModeType arg1, Map<String, Object> arg2) {
    }

    @Override
    public void refresh(Object arg0, Map<String, Object> arg1) {
    }

    @Override
    public void refresh(Object arg0, LockModeType arg1) {
    }

    @Override
    public void refresh(Object arg0, LockModeType arg1, Map<String, Object> arg2) {
    }

    @Override
    public void setProperty(String arg0, Object arg1) {
    }

    @Override
    public <T> T unwrap(Class<T> arg0) {
        return null;
    }
}
