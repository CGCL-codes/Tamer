package shiva.session.persister;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import shiva.cfg.Configuration;
import shiva.domain.metadata.AttributeMapping;
import shiva.domain.metadata.EntityMapping;
import shiva.session.persister.sql.LdapSqlGenerator;
import shiva.session.persister.sql.SqlGenerator;

/**
 * @author Paulo Vitor
 * @author Roberto Su
 * 
 * @description
 *
 */
@SuppressWarnings({ "unchecked" })
public class EntityPersisterImpl implements EntityPersister {

    private Logger logger = Logger.getLogger(this.getClass());

    private SqlGenerator sqlGen = null;

    private Configuration config = null;

    /**
	 * 
	 * @param config
	 */
    public EntityPersisterImpl(Configuration config) {
        this.sqlGen = new LdapSqlGenerator();
        this.config = config;
    }

    @Override
    public void persist(Object ldapEntity) {
        Map<Class, EntityMapping> ems = config.getEntityMappings();
        EntityMapping em = ems.get(ldapEntity.getClass());
        String insertSql = this.sqlGen.generateInsertString(em, ldapEntity);
        logger.info("generated insert string: " + insertSql);
        this.executeUpdateSql(insertSql);
    }

    @Override
    public void delete(Object ldapEntity) {
        Map<Class, EntityMapping> ems = config.getEntityMappings();
        EntityMapping em = ems.get(ldapEntity.getClass());
        String deleteSql = this.sqlGen.generateDeleteString(em, ldapEntity);
        logger.info("generated delete string: " + deleteSql);
        this.executeUpdateSql(deleteSql);
    }

    @Override
    public void update(Object ldapEntity) {
        Map<Class, EntityMapping> ems = config.getEntityMappings();
        EntityMapping em = ems.get(ldapEntity.getClass());
        String updateSql = this.sqlGen.generateUpdateString(em, ldapEntity);
        logger.info("generated update string: " + updateSql);
        this.executeUpdateSql(updateSql);
    }

    @Override
    public boolean exists(Object ldapEntity) {
        Map<Class, EntityMapping> ems = config.getEntityMappings();
        EntityMapping em = ems.get(ldapEntity.getClass());
        String selectByDnSql = this.sqlGen.generateSelectByDnString(em, ldapEntity);
        logger.info("generated select by distinguished name string: " + selectByDnSql);
        boolean dnExists = false;
        ResultSet rs = this.executeQuerySql(selectByDnSql);
        try {
            dnExists = rs.next();
        } catch (Exception e) {
        } finally {
            try {
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                    rs = null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (dnExists) {
            logger.info("entity already exists!");
        } else {
            logger.info("entity does not exists!");
        }
        return dnExists;
    }

    public List getAll(Class clazz) {
        Map<Class, EntityMapping> ems = config.getEntityMappings();
        EntityMapping em = ems.get(clazz);
        String selectAll = this.sqlGen.generateSelectAllString(em, clazz);
        logger.info("generated select all string: " + selectAll);
        List allEntities = new ArrayList();
        ResultSet rs = this.executeQuerySql(selectAll);
        try {
            while (rs.next()) {
                Object classInstance = clazz.newInstance();
                Set<Field> keySet = em.getAttributesMapped().keySet();
                for (Field key : keySet) {
                    Map<Field, AttributeMapping> ams = em.getAttributesMapped();
                    AttributeMapping am = ams.get(key);
                    String attributeNameObj = am.getAttribute().getName();
                    String attributeNameBound = am.getAttributeNameBound();
                    Field field = clazz.getDeclaredField(attributeNameObj);
                    boolean isAccessible = field.isAccessible();
                    if (!isAccessible) {
                        field.setAccessible(true);
                    }
                    field.set(classInstance, rs.getObject(attributeNameBound));
                    if (!isAccessible) {
                        field.setAccessible(false);
                    }
                }
                allEntities.add(classInstance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                    rs = null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return allEntities;
    }

    /**
	 * 
	 * @param sql
	 */
    private void executeUpdateSql(String sql) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = config.getConnection();
            ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            logger.info("executeUpdateSql is ok! :)");
        } catch (SQLException e) {
            logger.error("executeUpdateSql is not ok :(", e);
        } finally {
            try {
                ps.close();
                ps = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
	 * 
	 * @param sql
	 * @return
	 */
    private ResultSet executeQuerySql(String sql) {
        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = config.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            logger.info("executeQuerySql is ok! :)");
        } catch (SQLException e) {
            logger.debug("executeQuerySql is not ok :(", e);
        }
        return rs;
    }
}
