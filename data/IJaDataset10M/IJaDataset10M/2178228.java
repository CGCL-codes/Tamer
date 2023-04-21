package com.voxdei.voxcontentSE.DAO.vdComment;

import java.io.Serializable;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Service;

/**
 * Maneja las entradas de la base de datos para la tabla vd_comment.
 * @author Michael Salgado
 * @company VoxDei.
 */
@Service
public class VdCommentDAO extends HibernateDaoSupport implements VdCommentI {

    private static Logger logger = Logger.getLogger(VdCommentDAO.class);

    private static VdCommentDAO singleton;

    /**
     * Identifica la columna TYPE en el campo.
     */
    public static final int _TYPE = 0;

    /**
     * Identifica la columna ENABLED en el campo.
     */
    public static final int _ENABLED = 1;

    /**
     * Identifica la columna CREATED_DATE en el campo.
     */
    public static final int _CREATED_DATE = 2;

    /**
     * Identifica la columna FULL_TEXT en el campo.
     */
    public static final int _FULL_TEXT = 3;

    /**
     * Identifica la columna EMAIL en el campo.
     */
    public static final int _EMAIL = 4;

    /**
     * Identifica la columna NICK_COMMENT en el campo.
     */
    public static final int _NICK_COMMENT = 5;

    /**
     * Identifica la columna TITLE en el campo.
     */
    public static final int _TITLE = 6;

    /**
     * Identifica la columna ID_USER en el campo.
     */
    public static final int _ID_USER = 7;

    /**
     * Identifica la columna ID_PARENT en el campo.
     */
    public static final int _ID_PARENT = 8;

    /**
     * Identifica la columna ID en el campo.
     */
    public static final int _ID = 9;

    /**
	* Constructor
	*/
    @Autowired
    public VdCommentDAO(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
        singleton = this;
        logger.info("VdCommentDAO inicializado.");
    }

    /**
     * Obtiene el VdCommentDAO singleton.
     *
     * @return VdCommentDAO
     */
    public static VdCommentDAO getInstance() {
        logger.debug("VdCommentDAO Instanciado");
        return singleton;
    }

    /**
     * Crea una instancia de VdComment.
     *     
     * @return the new VdComment
     */
    public VdComment createVdComment() {
        logger.debug("VdComment Creada en VdCommentDAO");
        return new VdComment();
    }

    /**
     * Inserta un registro en la tabla vd_comment.
     *
     * @param  instacia de  VdComment
     * @return La instacia de VdComment con un id generado.
     */
    @Override
    public VdComment insert(VdComment table) throws DataAccessException {
        logger.debug("VdCommentDAO insert");
        super.getHibernateTemplate().save(table);
        return table;
    }

    /**
	 * @see BasicDAO
	 */
    @Override
    public List<?> findAll() throws DataAccessException {
        logger.debug("VdCommentDAO findAll");
        return super.getHibernateTemplate().find("FROM VdComment");
    }

    /**
	 * @see BasicDAO	 
     * @param id Long - PK# 1
	*/
    @Override
    public VdComment findById(Serializable id) throws DataAccessException {
        logger.debug("VdCommentDAO findById");
        return super.getHibernateTemplate().load(VdComment.class, id);
    }

    @Override
    public void remove(VdComment table) throws DataAccessException {
        logger.debug("VdCommentDAO remove");
        super.getHibernateTemplate().delete(table);
    }

    @Override
    public void update(VdComment table) throws DataAccessException {
        logger.debug("VdCommentDAO update");
        super.getHibernateTemplate().update(table);
    }

    /**
	 * Encuenta todos las instancias de vd_comment buscandolas por TYPE.
	 * 
	 * @see com.voxdei.voxcontentSE.DAO.vdComment.VdComment
	 * 
	 */
    @Override
    public List<?> findAllByType(String value) throws DataAccessException {
        logger.debug("VdCommentDAO findAllBytype");
        return super.getHibernateTemplate().find("FROM VdComment WHERE TYPE = ?", value);
    }

    /**
	 * Encuenta todos las instancias de vd_comment buscandolas por ENABLED.
	 * 
	 * @see com.voxdei.voxcontentSE.DAO.vdComment.VdComment
	 * 
	 */
    @Override
    public List<?> findAllByEnabled(Boolean value) throws DataAccessException {
        logger.debug("VdCommentDAO findAllByenabled");
        return super.getHibernateTemplate().find("FROM VdComment WHERE ENABLED = ?", value);
    }

    /**
	 * Encuenta todos las instancias de vd_comment buscandolas por CREATED_DATE.
	 * 
	 * @see com.voxdei.voxcontentSE.DAO.vdComment.VdComment
	 * 
	 */
    @Override
    public List<?> findAllByCreatedDate(java.util.Date value) throws DataAccessException {
        logger.debug("VdCommentDAO findAllBycreatedDate");
        return super.getHibernateTemplate().find("FROM VdComment WHERE CREATED_DATE = ?", value);
    }

    /**
	 * Encuenta todos las instancias de vd_comment buscandolas por FULL_TEXT.
	 * 
	 * @see com.voxdei.voxcontentSE.DAO.vdComment.VdComment
	 * 
	 */
    @Override
    public List<?> findAllByFullText(String value) throws DataAccessException {
        logger.debug("VdCommentDAO findAllByfullText");
        return super.getHibernateTemplate().find("FROM VdComment WHERE FULL_TEXT = ?", value);
    }

    /**
	 * Encuenta todos las instancias de vd_comment buscandolas por EMAIL.
	 * 
	 * @see com.voxdei.voxcontentSE.DAO.vdComment.VdComment
	 * 
	 */
    @Override
    public List<?> findAllByEmail(String value) throws DataAccessException {
        logger.debug("VdCommentDAO findAllByemail");
        return super.getHibernateTemplate().find("FROM VdComment WHERE EMAIL = ?", value);
    }

    /**
	 * Encuenta todos las instancias de vd_comment buscandolas por NICK_COMMENT.
	 * 
	 * @see com.voxdei.voxcontentSE.DAO.vdComment.VdComment
	 * 
	 */
    @Override
    public List<?> findAllByNickComment(String value) throws DataAccessException {
        logger.debug("VdCommentDAO findAllBynickComment");
        return super.getHibernateTemplate().find("FROM VdComment WHERE NICK_COMMENT = ?", value);
    }

    /**
	 * Encuenta todos las instancias de vd_comment buscandolas por TITLE.
	 * 
	 * @see com.voxdei.voxcontentSE.DAO.vdComment.VdComment
	 * 
	 */
    @Override
    public List<?> findAllByTitle(String value) throws DataAccessException {
        logger.debug("VdCommentDAO findAllBytitle");
        return super.getHibernateTemplate().find("FROM VdComment WHERE TITLE = ?", value);
    }

    /**
	 * Encuenta todos las instancias de vd_comment buscandolas por ID_USER.
	 * 
	 * @see com.voxdei.voxcontentSE.DAO.vdComment.VdComment
	 * 
	 */
    @Override
    public List<?> findAllByIdUser(Long value) throws DataAccessException {
        logger.debug("VdCommentDAO findAllByidUser");
        return super.getHibernateTemplate().find("FROM VdComment WHERE ID_USER = ?", value);
    }

    /**
	 * Encuenta todos las instancias de vd_comment buscandolas por ID_PARENT.
	 * 
	 * @see com.voxdei.voxcontentSE.DAO.vdComment.VdComment
	 * 
	 */
    @Override
    public List<?> findAllByIdParent(Long value) throws DataAccessException {
        logger.debug("VdCommentDAO findAllByidParent");
        return super.getHibernateTemplate().find("FROM VdComment WHERE ID_PARENT = ?", value);
    }

    /**
	 * Encuenta todos las instancias de vd_comment buscandolas por ID.
	 * 
	 * @see com.voxdei.voxcontentSE.DAO.vdComment.VdComment
	 * 
	 */
    @Override
    public List<?> findAllById(Long value) throws DataAccessException {
        logger.debug("VdCommentDAO findAllByid");
        return super.getHibernateTemplate().find("FROM VdComment WHERE ID = ?", value);
    }

    /**
	 * Encuenta todas las instancias de vd_comment buscandolas por un WHERE.
	 * 
	 * @param where Parametros de la busqueda por where
	 * @param args	Argumentos para la busqueda del where
	 * @return La lista de VdComment que concuerdan
	 */
    @Override
    public List<?> findAllByWhere(String where, Object... args) throws DataAccessException {
        logger.debug("VdCommentDAO findAllByWhere");
        return super.getHibernateTemplate().find("SELECT * FROM VdComment " + where, args);
    }

    /**
	 * Borra todas las instancias de vd_comment buscandolas por un WHERE.
	 * 
	 * @param where Parametros de la busqueda por where
	 * @param args	Argumentos para la busqueda del where
	 * @return El numero de filas afectdas
	 */
    @Override
    public void deleteByWhere(String where, Object... args) throws DataAccessException {
        logger.debug("VdCommentDAO deleteByWhere");
        super.getHibernateTemplate().deleteAll(super.getHibernateTemplate().find("FROM VdComment " + where, args));
    }
}
