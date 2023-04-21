package org.njo.webapp.root.model.activity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.njo.webapp.root.model.dao.AppPropertiesDAO;
import org.njo.webapp.root.utility.ObjectExistsException;
import org.njo.webapp.root.utility.ObjectNotExistsException;
import org.njo.webapp.root.utility.SystemContext;

/**
 * TODO:comment
 * 
 * @author yu.peng
 * @version 0.01
 */
public class AppPropertyActivity {

    /**
     * Log Object.
     */
    private static Log logger = LogFactory.getLog(AppPropertyActivity.class);

    public List getPropertyList() {
        Connection connection = null;
        AppPropertiesDAO appPropertiesDAO = null;
        try {
            connection = SystemContext.openConnection();
            appPropertiesDAO = new AppPropertiesDAO(connection);
            return appPropertiesDAO.selectAllRecords(1, Integer.MAX_VALUE);
        } catch (SQLException ex) {
            if (logger.isErrorEnabled()) {
                logger.error(ex);
            }
            throw new RuntimeException(ex);
        } finally {
            try {
                if (connection != null) {
                    SystemContext.closeConnection(connection);
                }
            } catch (Throwable tw) {
            }
        }
    }

    public void addProperty(String argPropertyKey, String argPropertyValue, String argPropertyDescription) throws ObjectExistsException {
        Connection connection = null;
        AppPropertiesDAO appPropertiesDAO = null;
        try {
            connection = SystemContext.openConnection();
            appPropertiesDAO = new AppPropertiesDAO(connection);
            appPropertiesDAO.insertProperty(argPropertyKey, argPropertyValue, argPropertyDescription);
            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (Throwable tw) {
                if (logger.isErrorEnabled()) {
                    logger.error(tw);
                }
            }
            if (ex.getErrorCode() == 1) {
                throw new ObjectExistsException("aready inserted.");
            }
            if (ex.getErrorCode() == 1062) {
                throw new ObjectExistsException("aready inserted.");
            }
            if (logger.isErrorEnabled()) {
                logger.error(ex);
            }
            throw new RuntimeException(ex);
        } finally {
            try {
                if (connection != null) {
                    SystemContext.closeConnection(connection);
                }
            } catch (Throwable tw) {
            }
        }
    }

    /**
     * 
     * 
     * @param argMessageKey
     * @return
     * @throws ObjectNotExistsException
     */
    public Map getProperty(String argPropertyKey) throws ObjectNotExistsException {
        Connection connection = null;
        AppPropertiesDAO appPropertiesDAO = null;
        Map propertyMap = null;
        try {
            connection = SystemContext.openConnection();
            propertyMap = new HashMap();
            appPropertiesDAO = new AppPropertiesDAO(connection);
            String[] propertyData = appPropertiesDAO.selectPropertyByKey(argPropertyKey);
            propertyMap.put("key", propertyData[0]);
            propertyMap.put("value", propertyData[1]);
            propertyMap.put("description", propertyData[2]);
            return propertyMap;
        } catch (SQLException ex) {
            if (logger.isErrorEnabled()) {
                logger.error(ex);
            }
            throw new RuntimeException(ex);
        } finally {
            try {
                if (connection != null) {
                    SystemContext.closeConnection(connection);
                }
            } catch (Throwable tw) {
            }
        }
    }

    public void saveProperty(String argPropertyKey, String argPropertyValue, String argPropertyDescription) {
        Connection connection = null;
        AppPropertiesDAO appPropertiesDAO = null;
        try {
            connection = SystemContext.openConnection();
            appPropertiesDAO = new AppPropertiesDAO(connection);
            appPropertiesDAO.updateProperty(argPropertyKey, argPropertyValue, argPropertyDescription);
            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (Throwable tw) {
                if (logger.isErrorEnabled()) {
                    logger.error(tw);
                }
            }
            if (logger.isErrorEnabled()) {
                logger.error(ex);
            }
            throw new RuntimeException(ex);
        } finally {
            try {
                if (connection != null) {
                    SystemContext.closeConnection(connection);
                }
            } catch (Throwable tw) {
            }
        }
    }

    public void removeProperties(String[] argPropertyKeys) {
        Connection connection = null;
        AppPropertiesDAO appPropertiesDAO = null;
        try {
        } catch (Throwable tw) {
            logger.error(tw);
            throw new RuntimeException(tw);
        }
        try {
            connection = SystemContext.openConnection();
            appPropertiesDAO = new AppPropertiesDAO(connection);
            for (int i = 0; argPropertyKeys != null && i < argPropertyKeys.length; i++) {
                String propertyKey = argPropertyKeys[i];
                appPropertiesDAO.deletePropertyByKey(propertyKey);
            }
            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (Throwable tw) {
                if (logger.isErrorEnabled()) {
                    logger.error(tw);
                }
            }
            if (logger.isErrorEnabled()) {
                logger.error(ex);
            }
            throw new RuntimeException(ex);
        } finally {
            try {
                if (connection != null) {
                    SystemContext.closeConnection(connection);
                }
            } catch (Throwable tw) {
            }
        }
    }
}
