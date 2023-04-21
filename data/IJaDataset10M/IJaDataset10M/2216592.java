package kom.lollipop.base;

import java.io.Serializable;
import java.util.List;
import org.apache.log4j.Logger;

public class DAOImpl<B extends Serializable> implements DAO<B> {

    private static final Logger logger = Logger.getLogger(DAOImpl.class);

    private PersistenceManagerDB persistenceManager;

    public void setPersistenceManager(PersistenceManagerDB persistenceManager) {
        this.persistenceManager = persistenceManager;
    }

    ;

    @Override
    public B find(B beanPk) {
        B result = null;
        try {
            String sql = persistenceManager.getSqlFind(beanPk);
            logger.debug("FIND_SQL=" + sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            result = (B) persistenceManager.getBeanClass().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<B> findByExample(B example) {
        return null;
    }

    @Override
    public List<B> findBySQL(String sql, String... parameters) {
        return null;
    }

    @Override
    public boolean insert(B bean) {
        String sql = "";
        try {
            sql = persistenceManager.getSqlInsertByPk(new Object[] { bean });
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        System.out.println(sql);
        return false;
    }

    @Override
    public boolean update(B bean) {
        return false;
    }

    @Override
    public boolean updateByExample(B example, B dataToSet) {
        return false;
    }

    ;

    @Override
    public boolean delete(B beanPk) {
        return false;
    }

    @Override
    public boolean deleteByExample(B example) {
        return false;
    }
}
