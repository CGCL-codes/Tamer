package uk.ac.roslin.ensembl.dao.coreaccess;

import uk.ac.roslin.ensembl.exception.DAOException;
import uk.ac.roslin.ensembl.model.core.CoreObject;

/**
 *
 * @author tpaterso
 */
public interface ReInitializationDAO {

    public void reInitialize(CoreObject object) throws DAOException;
}
