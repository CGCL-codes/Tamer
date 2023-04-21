package org.mbari.vars.annotation.model.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.exolab.castor.jdo.Database;
import org.mbari.vars.annotation.model.CameraPlatformDeployment;
import org.mbari.vars.annotation.model.VideoArchiveSet;
import org.mbari.vars.dao.DAOException;
import org.mbari.vars.dao.IDataObject;
import org.mbari.vars.dao.ObjectDAO;

/**
 * <p>Data access object for CameraPlatformDeployments.</p>
 *
 * @author <a href="http://www.mbari.org">MBARI</a>
 * @version $Id: CameraPlatformDeploymentDAO.java 265 2006-06-20 05:30:09Z hohonuuli $
 */
public class CameraPlatformDeploymentDAO extends ObjectDAO {

    private static CameraPlatformDeploymentDAO instance = new CameraPlatformDeploymentDAO();

    private static final Logger log = LoggerFactory.getLogger(CameraPlatformDeployment.class);

    /**
     * Constructs ...
     *
     */
    CameraPlatformDeploymentDAO() {
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param obj
     * @param db
     *
     * @throws DAOException
     */
    protected void deleteChildren(IDataObject obj, Database db) throws DAOException {
    }

    /**
     * Retrieves a sorted collection of all the ChiefScientists names in the
     * database.
     *
     * @return A Collection of Strings of all Chief Scientists
     *              in the database.
     *
     * @throws DAOException
     */
    public Collection findAllChiefScientists() throws DAOException {
        Collection c = new ArrayList();
        Database db = fetchDatabase();
        try {
            db.begin();
            Connection con = db.getJdbcConnection();
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("SELECT DISTINCT ChiefScientist FROM CameraPlatformDeployment " + "WHERE ChiefScientist IS NOT NULL " + "ORDER BY ChiefScientist");
            while (rs.next()) {
                c.add(rs.getString(1));
            }
            s.close();
            db.commit();
        } catch (Exception e) {
            throw new DAOException("Failed to open jdbc connection", e);
        } finally {
            try {
                db.close();
            } catch (Exception e) {
                log.error("Attempt to close Castor database failed", e);
            }
        }
        return c;
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param primaryKey
     *
     * @return
     *
     * @throws DAOException
     */
    public IDataObject findByPK(String primaryKey) throws DAOException {
        return findByPK(primaryKey, CameraPlatformDeployment.class);
    }

    /**
     * Singleton access
     *
     * @return The UserAccountDAO object
     */
    public static CameraPlatformDeploymentDAO getInstance() {
        return instance;
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param obj
     * @param db
     *
     * @throws DAOException
     */
    protected void insertChildren(IDataObject obj, Database db) throws DAOException {
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param obj
     * @param db
     *
     * @throws DAOException
     */
    protected void updateChildren(IDataObject obj, Database db) throws DAOException {
    }

    /**
     * This methods should not be called by the developer.
     *
     * @see org.mbari.vars.annotation.model.dao.ObjectDAO#updateParents(org.mbari.vars.annotation.model.IDataObject, org.exolab.castor.jdo.Database)
     *
     * @param obj
     * @param db
     *
     * @throws DAOException
     */
    public void updateParents(IDataObject obj, Database db) throws DAOException {
        CameraPlatformDeployment cpd = (CameraPlatformDeployment) obj;
        VideoArchiveSet vas = (VideoArchiveSet) cpd.getVideoArchiveSet();
        if (vas != null) {
            ObjectDAO dao = VideoArchiveSetDAO.getInstance();
            dao.updateParents(vas, db);
            dao.update(vas, db);
        }
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param obj
     *
     * @throws DAOException
     */
    protected void validate(IDataObject obj) throws DAOException {
        if (!(obj instanceof CameraPlatformDeployment)) {
            throw new DAOException("Attempted to update a " + obj.getClass().getName() + " using CameraPlatformDeploymentDAO. Bad, bad, bad programmer!!");
        }
        CameraPlatformDeployment cpd = (CameraPlatformDeployment) obj;
        VideoArchiveSet vas = (VideoArchiveSet) cpd.getVideoArchiveSet();
        if (vas == null) {
            throw new DAOException("Missing a parent VideoArchiveSet");
        }
    }
}
