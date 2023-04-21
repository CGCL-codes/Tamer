package org.jems.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.hibernate.EntityMode;
import org.hibernate.ejb.HibernateEntityManagerFactory;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.Type;
import org.jems.dao.types.DaoFilter;
import org.jems.dao.types.DaoParameter;
import org.jems.dao.types.DaoSort;
import org.jems.dao.types.EntityAssociationMetaData;
import org.jems.dao.types.EntityMetaData;
import org.jems.dao.types.EntityPropertyMetaData;
import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.orm.jpa.support.JpaDaoSupport;

/** JpaDao class */
public class JpaDao extends JpaDaoSupport implements FileFilter, IDao {

    private Map<String, EntityMetaData> m_entityMetaDataMap;

    protected Map<String, String> m_queryTable;

    protected String m_queryDirectoryName;

    protected static Logger m_log = Logger.getLogger(JpaDao.class);

    public JpaDao(String queryDirectoryName) throws DaoException {
        File queryDirectory = new File(queryDirectoryName);
        m_queryTable = getQueryTable(queryDirectory);
    }

    /** accept */
    public boolean accept(File file) {
        return file.getAbsolutePath().toLowerCase().endsWith(".jql");
    }

    /** create */
    public <T> T create(T entity) {
        JpaTemplate template = getJpaTemplate();
        return template.merge(entity);
    }

    /** get */
    public <T> T get(Class<T> cl, DaoFilter filters[]) throws DaoNotFoundException {
        return get(cl, filters, false);
    }

    /** get */
    public <T> T get(Class<T> cl, DaoFilter filters[], boolean allowNull) throws DaoNotFoundException {
        T result = getEntity(cl, filters);
        if (result == null && !allowNull) {
            throw new DaoNotFoundException("Cannot find entity: " + cl.getName());
        }
        return result;
    }

    /** get */
    public <T> T get(String queryName, DaoParameter params[], DaoFilter filters[]) throws DaoNotFoundException {
        JpaTemplate template = getJpaTemplate();
        String queryString = m_queryTable.get(queryName);
        Date start = new Date();
        if (queryString == null) {
            Map<String, Object> paramMap = getParameters(params);
            List<T> result = template.findByNamedQueryAndNamedParams(queryName, paramMap);
            m_log.info(queryName + " executed in " + (new Date().getTime() - start.getTime()) + " ms");
            return get(result, queryName);
        } else {
            DaoSort sorts[] = new DaoSort[0];
            DaoQueryCallback<T> cb = new DaoQueryCallback<T>(queryString, params, filters, sorts, 0, 0);
            List<T> result = template.executeFind(cb);
            m_log.info(queryString + " executed in " + (new Date().getTime() - start.getTime()) + " ms");
            return get(result, queryName);
        }
    }

    /** get */
    public <T> T get(SQLQuery query, DaoParameter params[], DaoFilter filters[]) throws DaoNotFoundException {
        JpaTemplate template = getJpaTemplate();
        DaoSort sorts[] = new DaoSort[0];
        String queryString = query.getQuery();
        DaoQueryCallback<T> cb = new DaoQueryCallback<T>(queryString, params, filters, sorts, 0, 0);
        Date start = new Date();
        List<T> result = template.executeFind(cb);
        m_log.info(query.getQuery() + " executed in " + (new Date().getTime() - start.getTime()) + " ms");
        return get(result, queryString);
    }

    /** getEntityMetaData */
    public Map<String, EntityMetaData> getEntityMetaData() {
        if (m_entityMetaDataMap == null) {
            HibernateEntityManagerFactory emf = (HibernateEntityManagerFactory) getJpaTemplate().getEntityManagerFactory();
            m_entityMetaDataMap = getEntityMetaData(emf.getSessionFactory().getAllClassMetadata());
        }
        return m_entityMetaDataMap;
    }

    /** getList */
    public <T> List<T> getList(Class<T> cl) {
        return getList(cl, new DaoFilter[0], new DaoSort[0], 0, 0);
    }

    /** getList */
    public <T> List<T> getList(Class<T> cl, DaoFilter filters[], DaoSort sorts[]) {
        return getList(cl, filters, sorts, 0, 0);
    }

    /** getList */
    public <T> List<T> getList(Class<T> cl, DaoFilter filters[], DaoSort sorts[], int startRow, int maxRows) {
        DaoQueryCallback<T> cb = new DaoQueryCallback<T>(cl, filters, sorts, startRow, maxRows);
        JpaTemplate template = getJpaTemplate();
        Date start = new Date();
        List<T> result = template.executeFind(cb);
        m_log.info(cl.getSimpleName() + " retrieved in " + (new Date().getTime() - start.getTime()) + " ms");
        return result;
    }

    /** getList */
    public <T> List<T> getList(String queryName) {
        return getList(queryName, new DaoParameter[0], new DaoFilter[0], new DaoSort[0]);
    }

    /** getList */
    public <T> List<T> getList(String queryName, DaoParameter params[], DaoFilter filters[], DaoSort sorts[]) {
        return getList(queryName, params, filters, sorts, 0, 0);
    }

    /** getList */
    public <T> List<T> getList(String queryName, DaoParameter params[], DaoFilter filters[], DaoSort sorts[], int startRow, int maxRows) {
        JpaTemplate template = getJpaTemplate();
        String queryString = m_queryTable.get(queryName);
        Date start = new Date();
        if (queryString == null) {
            Map<String, Object> paramMap = getParameters(filters);
            List<T> result = template.findByNamedQueryAndNamedParams(queryName, paramMap);
            m_log.info(queryName + " executed in " + (new Date().getTime() - start.getTime()) + " ms");
            return result;
        } else {
            DaoQueryCallback<T> cb = new DaoQueryCallback<T>(queryString, params, filters, sorts, startRow, maxRows);
            List<T> result = template.executeFind(cb);
            m_log.info(queryString + " executed in " + (new Date().getTime() - start.getTime()) + " ms");
            return result;
        }
    }

    /** getList */
    public <T> List<T> getList(SQLQuery query) {
        return getList(query, new DaoParameter[0], new DaoFilter[0], new DaoSort[0]);
    }

    /** getList */
    public <T> List<T> getList(SQLQuery query, DaoParameter params[], DaoFilter filters[], DaoSort sorts[]) {
        return getList(query, params, filters, sorts, 0, 0);
    }

    /** getList */
    public <T> List<T> getList(SQLQuery query, DaoParameter params[], DaoFilter filters[], DaoSort sorts[], int startRow, int maxRows) {
        JpaTemplate template = getJpaTemplate();
        String queryString = query.getQuery();
        DaoQueryCallback<T> cb = new DaoQueryCallback<T>(queryString, params, filters, sorts, startRow, maxRows);
        Date start = new Date();
        List<T> result = template.executeFind(cb);
        m_log.info(query.getQuery() + " executed in " + (new Date().getTime() - start.getTime()) + " ms");
        return result;
    }

    /** getListCount */
    public <T> int getListCount(Class<T> cl, DaoFilter filters[]) {
        String query = "SELECT COUNT(*) FROM " + cl.getName();
        DaoParameter params[] = new DaoParameter[0];
        DaoSort sorts[] = new DaoSort[0];
        DaoQueryCallback<T> cb = new DaoQueryCallback(query, params, filters, sorts, 0, 0);
        JpaTemplate template = getJpaTemplate();
        Date start = new Date();
        List<Long> list = template.executeFind(cb);
        m_log.info(query + " executed in " + (new Date().getTime() - start.getTime()) + " ms");
        return list.get(0).intValue();
    }

    /** setQueryDirectoryName */
    public void setQueryDirectoryName(String queryDirectoryName) {
        m_queryDirectoryName = queryDirectoryName;
    }

    /** remove */
    public <T> void remove(T entity) {
        JpaTemplate template = getJpaTemplate();
        template.remove(update(entity));
    }

    /** update */
    public <T> T update(T entity) {
        JpaTemplate template = getJpaTemplate();
        return template.merge(entity);
    }

    /** addEntityMetaData */
    protected void addEntityPropertyMetaData(ClassMetadata classMD, EntityMetaData entityMD, String propertyName) {
        Type type = classMD.getPropertyType(propertyName);
        if (!type.isAssociationType()) {
            EntityPropertyMetaData entityPropertyMetaData = new EntityPropertyMetaData();
            Class<?> cl = type.getReturnedClass();
            entityPropertyMetaData.setName(propertyName);
            entityPropertyMetaData.setTypeName(cl.getSimpleName());
            if (classMD.hasIdentifierProperty() && propertyName.equals(classMD.getIdentifierPropertyName())) {
                entityPropertyMetaData.setPrimaryKey(true);
            }
            entityMD.addEntityPropertyMetaData(entityPropertyMetaData);
        } else {
            if (!type.isCollectionType()) {
                EntityAssociationMetaData entityAssociationMetaData = new EntityAssociationMetaData();
                Class<?> cl = type.getReturnedClass();
                entityAssociationMetaData.setClassName(cl.getName());
                entityAssociationMetaData.setEntityName(cl.getSimpleName());
                entityAssociationMetaData.setName(propertyName);
                entityAssociationMetaData.setIsCollection(type.isCollectionType());
                entityMD.addEntityAssociationMetaData(entityAssociationMetaData);
            }
        }
    }

    /** get */
    protected <T> T get(List<T> list, String queryName) throws DaoNotFoundException {
        switch(list.size()) {
            case 0:
                throw new DaoNotFoundException(queryName + " returns no results");
            case 1:
                return list.get(0);
            default:
                throw new DaoNotFoundException(queryName + " does not return a unique object");
        }
    }

    /** getEntity */
    protected <T> T getEntity(Class<T> cl, DaoFilter filters[]) throws DaoNotFoundException {
        DaoQueryCallback<T> cb = new DaoQueryCallback<T>(cl, filters);
        JpaTemplate template = getJpaTemplate();
        Date start = new Date();
        List<T> list = template.executeFind(cb);
        m_log.info(cl.getSimpleName() + " retrieved in " + (new Date().getTime() - start.getTime()) + " ms");
        return get(list, cb.getQueryString());
    }

    /** getEntityMetaData */
    protected Map<String, EntityMetaData> getEntityMetaData(Map<String, ClassMetadata> map) {
        Map<String, EntityMetaData> result = new HashMap<String, EntityMetaData>();
        Collection<ClassMetadata> collection = map.values();
        for (ClassMetadata md : collection) {
            Class<?> cl = md.getMappedClass(EntityMode.POJO);
            EntityMetaData entityMetaData = new EntityMetaData();
            String propertyNames[] = md.getPropertyNames();
            entityMetaData.setName(cl.getSimpleName());
            entityMetaData.setClassName(cl.getName());
            result.put(cl.getName(), entityMetaData);
            if (md.hasIdentifierProperty()) {
                addEntityPropertyMetaData(md, entityMetaData, md.getIdentifierPropertyName());
            }
            for (int count = 0; count < propertyNames.length; count++) {
                String propertyName = propertyNames[count];
                addEntityPropertyMetaData(md, entityMetaData, propertyName);
            }
        }
        return result;
    }

    /** getParameters */
    protected Map<String, Object> getParameters(DaoParameter params[]) {
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        for (int count = 0; count < params.length; count++) {
            DaoParameter param = params[count];
            paramMap.put(param.getColumn(), param.getValue());
        }
        return paramMap;
    }

    /** getQueryTable */
    protected Map<String, String> getQueryTable(File queryDirectory) throws DaoException {
        HashMap<String, String> queryTable = new HashMap<String, String>();
        File files[] = queryDirectory.listFiles(this);
        if (files == null) {
            return queryTable;
        }
        for (int count = 0; count < files.length; count++) {
            File file = files[count];
            String fileName = file.getName();
            String queryName = fileName.substring(0, fileName.indexOf("."));
            try {
                FileReader fr = new FileReader(file);
                BufferedReader reader = new BufferedReader(fr);
                StringBuffer content = new StringBuffer();
                String text;
                while ((text = reader.readLine()) != null) {
                    content.append(text);
                    content.append("\n");
                }
                m_log.info("Loading query file: " + fileName + " JQL: " + content.toString());
                queryTable.put(queryName, content.toString());
            } catch (FileNotFoundException e) {
                throw new DaoException(e.getMessage());
            } catch (IOException e) {
                throw new DaoException(e.getMessage());
            }
        }
        return queryTable;
    }
}
