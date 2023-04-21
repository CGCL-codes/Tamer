package net.zcarioca.zscrum.domain.dao;

import java.util.List;
import net.zcarioca.zscrum.domain.Project;
import net.zcarioca.zscrum.domain.dao.exceptions.DAOReadException;

/**
 * DAO for the {@link Project} entity.
 * 
 * 
 * @author zcarioca
 */
public interface ProjectDAO extends EntityDAO<Project> {

    /**
    * Gets all of the projects for a given user.
    * 
    * @param userId The user id.
    * @return Returns the projects for a given user.
    */
    public List<Project> loadByUserId(int userId) throws DAOReadException;
}
