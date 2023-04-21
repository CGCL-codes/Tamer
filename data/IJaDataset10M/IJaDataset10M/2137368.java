package com.voxdei.voxcontentSE.DAO.vdRole;

import java.io.Serializable;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Service;

/**
 * Maneja las entradas de la base de datos para la tabla vd_role.
 * @author Michael Salgado
 * @company VoxDei.
 */
@Service
public class VdRoleDAO extends HibernateDaoSupport implements VdRoleI {

    private static Logger logger = Logger.getLogger(VdRoleDAO.class);

    private static VdRoleDAO singleton;

    /**
     * Identifica la columna SEE en el campo.
     */
    public static final int _SEE = 0;

    /**
     * Identifica la columna REMOVE en el campo.
     */
    public static final int _REMOVE = 1;

    /**
     * Identifica la columna EDIT en el campo.
     */
    public static final int _EDIT = 2;

    /**
     * Identifica la columna ADDNEW en el campo.
     */
    public static final int _ADDNEW = 3;

    /**
     * Identifica la columna PARENT_ROLE en el campo.
     */
    public static final int _PARENT_ROLE = 4;

    /**
     * Identifica la columna DESCRIPTION en el campo.
     */
    public static final int _DESCRIPTION = 5;

    /**
     * Identifica la columna NAME en el campo.
     */
    public static final int _NAME = 6;

    /**
     * Identifica la columna ALIAS en el campo.
     */
    public static final int _ALIAS = 7;

    /**
     * Identifica la columna ID en el campo.
     */
    public static final int _ID = 8;

    /**
	* Constructor
	*/
    @Autowired
    public VdRoleDAO(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
        singleton = this;
        logger.info("VdRoleDAO inicializado.");
    }

    /**
     * Obtiene el VdRoleDAO singleton.
     *
     * @return VdRoleDAO
     */
    public static VdRoleDAO getInstance() {
        logger.debug("VdRoleDAO Instanciado");
        return singleton;
    }

    /**
     * Crea una instancia de VdRole.
     *     
     * @return the new VdRole
     */
    public VdRole createVdRole() {
        logger.debug("VdRole Creada en VdRoleDAO");
        return new VdRole();
    }

    /**
     * Inserta un registro en la tabla vd_role.
     *
     * @param  instacia de  VdRole
     * @return La instacia de VdRole con un id generado.
     */
    @Override
    public VdRole insert(VdRole table) throws DataAccessException {
        logger.debug("VdRoleDAO insert");
        super.getHibernateTemplate().save(table);
        return table;
    }

    /**
	 * @see BasicDAO
	 */
    @Override
    public List<?> findAll() throws DataAccessException {
        logger.debug("VdRoleDAO findAll");
        return super.getHibernateTemplate().find("FROM VdRole");
    }

    /**
	 * @see BasicDAO	 
     * @param id Long - PK# 1
	*/
    @Override
    public VdRole findById(Serializable id) throws DataAccessException {
        logger.debug("VdRoleDAO findById");
        return super.getHibernateTemplate().load(VdRole.class, id);
    }

    @Override
    public void remove(VdRole table) throws DataAccessException {
        logger.debug("VdRoleDAO remove");
        super.getHibernateTemplate().delete(table);
    }

    @Override
    public void update(VdRole table) throws DataAccessException {
        logger.debug("VdRoleDAO update");
        super.getHibernateTemplate().update(table);
    }

    /**
	 * Encuenta todos las instancias de vd_role buscandolas por SEE.
	 * 
	 * @see com.voxdei.voxcontentSE.DAO.vdRole.VdRole
	 * 
	 */
    @Override
    public List<?> findAllBySee(Boolean value) throws DataAccessException {
        logger.debug("VdRoleDAO findAllBysee");
        return super.getHibernateTemplate().find("FROM VdRole WHERE SEE = ?", value);
    }

    /**
	 * Encuenta todos las instancias de vd_role buscandolas por REMOVE.
	 * 
	 * @see com.voxdei.voxcontentSE.DAO.vdRole.VdRole
	 * 
	 */
    @Override
    public List<?> findAllByRemove(Boolean value) throws DataAccessException {
        logger.debug("VdRoleDAO findAllByremove");
        return super.getHibernateTemplate().find("FROM VdRole WHERE REMOVE = ?", value);
    }

    /**
	 * Encuenta todos las instancias de vd_role buscandolas por EDIT.
	 * 
	 * @see com.voxdei.voxcontentSE.DAO.vdRole.VdRole
	 * 
	 */
    @Override
    public List<?> findAllByEdit(Boolean value) throws DataAccessException {
        logger.debug("VdRoleDAO findAllByedit");
        return super.getHibernateTemplate().find("FROM VdRole WHERE EDIT = ?", value);
    }

    /**
	 * Encuenta todos las instancias de vd_role buscandolas por ADDNEW.
	 * 
	 * @see com.voxdei.voxcontentSE.DAO.vdRole.VdRole
	 * 
	 */
    @Override
    public List<?> findAllByAddnew(Boolean value) throws DataAccessException {
        logger.debug("VdRoleDAO findAllByaddnew");
        return super.getHibernateTemplate().find("FROM VdRole WHERE ADDNEW = ?", value);
    }

    /**
	 * Encuenta todos las instancias de vd_role buscandolas por PARENT_ROLE.
	 * 
	 * @see com.voxdei.voxcontentSE.DAO.vdRole.VdRole
	 * 
	 */
    @Override
    public List<?> findAllByParentRole(Integer value) throws DataAccessException {
        logger.debug("VdRoleDAO findAllByparentRole");
        return super.getHibernateTemplate().find("FROM VdRole WHERE PARENT_ROLE = ?", value);
    }

    /**
	 * Encuenta todos las instancias de vd_role buscandolas por DESCRIPTION.
	 * 
	 * @see com.voxdei.voxcontentSE.DAO.vdRole.VdRole
	 * 
	 */
    @Override
    public List<?> findAllByDescription(String value) throws DataAccessException {
        logger.debug("VdRoleDAO findAllBydescription");
        return super.getHibernateTemplate().find("FROM VdRole WHERE DESCRIPTION = ?", value);
    }

    /**
	 * Encuenta todos las instancias de vd_role buscandolas por NAME.
	 * 
	 * @see com.voxdei.voxcontentSE.DAO.vdRole.VdRole
	 * 
	 */
    @Override
    public List<?> findAllByName(String value) throws DataAccessException {
        logger.debug("VdRoleDAO findAllByname");
        return super.getHibernateTemplate().find("FROM VdRole WHERE NAME = ?", value);
    }

    /**
	 * Encuenta todos las instancias de vd_role buscandolas por ALIAS.
	 * 
	 * @see com.voxdei.voxcontentSE.DAO.vdRole.VdRole
	 * 
	 */
    @Override
    public List<?> findAllByAlias(String value) throws DataAccessException {
        logger.debug("VdRoleDAO findAllByalias");
        return super.getHibernateTemplate().find("FROM VdRole WHERE ALIAS = ?", value);
    }

    /**
	 * Encuenta todos las instancias de vd_role buscandolas por ID.
	 * 
	 * @see com.voxdei.voxcontentSE.DAO.vdRole.VdRole
	 * 
	 */
    @Override
    public List<?> findAllById(Long value) throws DataAccessException {
        logger.debug("VdRoleDAO findAllByid");
        return super.getHibernateTemplate().find("FROM VdRole WHERE ID = ?", value);
    }

    /**
	 * Encuenta todas las instancias de vd_role buscandolas por un WHERE.
	 * 
	 * @param where Parametros de la busqueda por where
	 * @param args	Argumentos para la busqueda del where
	 * @return La lista de VdRole que concuerdan
	 */
    @Override
    public List<?> findAllByWhere(String where, Object... args) throws DataAccessException {
        logger.debug("VdRoleDAO findAllByWhere");
        return super.getHibernateTemplate().find("SELECT * FROM VdRole " + where, args);
    }

    /**
	 * Borra todas las instancias de vd_role buscandolas por un WHERE.
	 * 
	 * @param where Parametros de la busqueda por where
	 * @param args	Argumentos para la busqueda del where
	 * @return El numero de filas afectdas
	 */
    @Override
    public void deleteByWhere(String where, Object... args) throws DataAccessException {
        logger.debug("VdRoleDAO deleteByWhere");
        super.getHibernateTemplate().deleteAll(super.getHibernateTemplate().find("FROM VdRole " + where, args));
    }
}
