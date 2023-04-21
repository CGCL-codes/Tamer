package be.erigo.itimesheets.daos;

import java.util.List;
import be.erigo.itimesheets.tos.SubProjectTO;

public abstract class SubProjectDAO {

    public abstract List<SubProjectTO> getSubProjects(long projectId, boolean activeOnly) throws DAOException;

    public abstract void update(SubProjectTO p) throws DAOException;

    public abstract void delete(SubProjectTO p) throws DAOException;

    public abstract void store(SubProjectTO p, long projectId) throws DAOException;

    public abstract void validateSubProjectEntry(long project_id, long subproject_id) throws DAOException;
}
