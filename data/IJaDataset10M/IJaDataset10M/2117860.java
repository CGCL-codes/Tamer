package uk.ac.roslin.ensembl.dao.database.coreaccess;

import uk.ac.roslin.ensembl.datasourceaware.core.DACoordinateSystem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import uk.ac.roslin.ensembl.dao.coreaccess.CoordinateSystemDAO;
import uk.ac.roslin.ensembl.dao.database.factory.DBDAOCollectionCoreFactory;
import uk.ac.roslin.ensembl.dao.database.factory.DBDAOSingleSpeciesCoreFactory;
import uk.ac.roslin.ensembl.dao.factory.DAOCollectionCoreFactory;
import uk.ac.roslin.ensembl.dao.factory.DAOSingleSpeciesCoreFactory;
import uk.ac.roslin.ensembl.mapper.core.SpeciesMapper;
import uk.ac.roslin.ensembl.exception.DAOException;
import uk.ac.roslin.ensembl.model.database.Database;

/**
 *
 * @author paterson
 */
public class DBCoordinateSystemDAO extends DBCoreObjectDAO implements CoordinateSystemDAO {

    public DBCoordinateSystemDAO() {
        super();
    }

    public DBCoordinateSystemDAO(DAOSingleSpeciesCoreFactory factory) {
        super(factory);
    }

    public DBCoordinateSystemDAO(DAOCollectionCoreFactory factory) {
        super(factory);
    }

    @Override
    public List<DACoordinateSystem> getCoordinateSystems() throws DAOException {
        List<DACoordinateSystem> out = new ArrayList<DACoordinateSystem>();
        SqlSession session = null;
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        try {
            if (species != null) {
                parameters.put("speciesID", species.getDBSpeciesID(this.getFactory().getDBVersion()));
            } else {
                parameters.put("speciesID", new Integer(1));
            }
            session = this.getFactory().getNewSqlSession();
            SpeciesMapper mapper = session.getMapper(SpeciesMapper.class);
            out = mapper.getCoordSystems(parameters);
        } catch (Exception e) {
            throw new DAOException("Failed to call getCoordinateSystems", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return out;
    }

    public void setFeatureCS() throws DAOException {
        List<HashMap> tempList = null;
        SqlSession session = null;
        try {
            HashMap<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("dbName", this.getFactory().getDatabase().getdBName());
            if (species != null) {
                parameters.put("speciesID", species.getDBSpeciesID(this.getFactory().getDBVersion()));
            } else {
                parameters.put("speciesID", new Integer(1));
            }
            session = this.getFactory().getNewSqlSession();
            SpeciesMapper mapper = session.getMapper(SpeciesMapper.class);
            tempList = mapper.setFeatureCS(parameters);
        } catch (Exception e) {
            throw new DAOException("Failed to call setFeaturesCS", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        if (tempList != null && !tempList.isEmpty()) {
            for (HashMap m : tempList) {
                try {
                    String feature = (String) m.get("feature_type");
                    Integer max = (Integer) m.get("max_length");
                    Integer csID = (Integer) m.get("cs_id");
                    if (singleSpecies) {
                        ssFactory.getDatabase().addFeatureCS(feature, csID, max);
                    } else {
                        collFactory.getDatabase().addFeatureCS(feature, csID, max, species);
                    }
                } catch (Exception e) {
                }
            }
        }
    }
}
