package org.itsocial.framework.persistence.queryhandler;

import java.util.Date;
import java.util.HashMap;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.itsocial.framework.exception.SystemException;
import java.io.InputStream;
import javax.inject.Inject;
import org.itsocial.framework.base.BaseGeneric;
import org.itsocial.framework.exception.ConfigurationException;
import org.itsocial.framework.persistence.lookup.LookupDataCache;
import org.itsocial.framework.persistence.lookup.ReferenceData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author raghav Mo
 */
@Component
public class QueryHandler extends BaseGeneric {

    private static HashMap<String, ReferenceData> referenceDataMap = null;

    @Inject
    public QueryHandler(@Value("${queries_configuration_file}") String configFileLocation) throws ConfigurationException {
        InputStream inputStreamXMLFile;
        log.info("QUERY_HANDLER_CONFIG_FILE  1           : " + configFileLocation);
        inputStreamXMLFile = QueryHandler.class.getResourceAsStream(configFileLocation);
        log.debug(inputStreamXMLFile);
        if (inputStreamXMLFile == null) {
            log.error(inputStreamXMLFile + " QueryHanlder Config File is null");
            throw new ConfigurationException("QueryHanlder Config File is null");
        }
        try {
            referenceDataMap = LookupDataCache.getReferenceDataMapFromXML(inputStreamXMLFile);
        } catch (ConfigurationException ex) {
            ex.printStackTrace();
            log.error(ex);
            throw new ConfigurationException(ex.toString());
        }
    }

    /**
     * An accessor method to get the lookup data for a specific object type.
     * 
     * @param objectName
     *            Name of the object, as configured in the lookup data XML
     *            config file.
     * @return A collection of objects of given type.
     * @throws SystemException
     *             This exception is thrown when the given object name is not
     *       /**
    found in the internal cache. The reason could that the
     *             corresponding entries are missing in the lookup data config
     *             file.
     */
    public Object getResultList(String queryId, HashMap<String, Object> parameters) {
        EntityManagerFactory emf = null;
        EntityManager em = null;
        ReferenceData referenceData = referenceDataMap.get(queryId);
        Object lookupDataObjects = null;
        log.debug("Quering (QUERY) Reference Data.." + referenceData);
        String persistentUnitName = referenceData.getPersistenceUnit();
        try {
            emf = Persistence.createEntityManagerFactory(persistentUnitName);
            em = emf.createEntityManager();
            log.debug("PERSISTENCE_UNIT :::" + referenceData.getPersistenceUnit());
            log.debug("QUERY ::::::::::::::" + referenceData.getQuery());
            log.debug("PARAMETERS::::::::::" + referenceData.getParameters());
            Query query = em.createQuery(referenceData.getQuery());
            if (!referenceData.getParameters().isEmpty()) {
                for (String key : referenceData.getParameters().keySet()) {
                    Object value = referenceData.getParameters().get(key);
                    log.debug("Config Paramter key -->" + key + " Value -->" + value);
                    query.setParameter(key, value);
                }
            }
            if (parameters != null) {
                for (String key : parameters.keySet()) {
                    Object value = parameters.get(key);
                    log.debug("User given Paramter key -->" + key + " Value -->" + value);
                    query.setParameter(key, value);
                }
            }
            if (referenceData.getMaxResults() != null) {
                query.setMaxResults(Integer.parseInt(referenceData.getMaxResults()));
            }
            query.setHint("toplink.refresh", "true");
            if ("single".equalsIgnoreCase(referenceData.getResultType())) {
                lookupDataObjects = query.getSingleResult();
            } else {
                lookupDataObjects = query.getResultList();
            }
            referenceData.setRefreshedDatetime(new Date());
            referenceData.setAccessCount(referenceData.getAccessCount() + 1);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.debug(ex);
            log.error(ex);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        log.info("Refreshed (QUERY) Reference Data.." + referenceData);
        return lookupDataObjects;
    }

    public static HashMap<String, ReferenceData> getReferenceDataMap() {
        return referenceDataMap;
    }
}
