package fr.gfi.gfinet.server.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import fr.gfi.gfinet.common.exception.DaoException;
import fr.gfi.gfinet.server.info.Competence;

/**
 * Data manager to manage the Competence entity object.
 * 
 * @author Tiago Fernandez
 * @since 08/03/2007
 */
public class CompetenceDao extends JpaSpringDao {

    /**
	 * Lists all competences.
	 * 
	 * @return all competences
	 * @throws DaoException when the operation doesn't complete
	 */
    public List<Competence> list() throws DaoException {
        List<Competence> result = null;
        try {
            result = initializeCompetence(getJpaTemplate().find("from Competence"));
        } catch (Throwable ex) {
            throw new DaoException(ex);
        }
        return result;
    }

    /**
	 * Finds an competence by ID.
	 * 
	 * @param id for fetching
	 * @return the competence
	 * @throws DaoException when the operation doesn't complete
	 */
    public Competence findById(Long id) throws DaoException {
        Competence result = null;
        try {
            result = initializeCompetence((Competence) findById(Competence.class, id));
        } catch (Throwable ex) {
            throw new DaoException(ex);
        }
        return result;
    }

    /**
	 * Finds an competence by its french name.
	 * 
	 * @param name for fetching
	 * @return the competence
	 * @throws DaoException when the operation doesn't complete
	 */
    public Competence findByFrenchName(String name) throws DaoException {
        Competence result = null;
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("name", name);
            result = initializeCompetence((Competence) getSingleResult("CompetenceByFrenchName", params));
        } catch (Throwable ex) {
            throw new DaoException(ex);
        }
        return result;
    }

    /**
	 * Finds competences by its popularity.
	 * 
	 * @param isFamous true or false
	 * @return a list of competence
	 * @throws DaoException when the operation doesn't complete
	 */
    public List<Competence> findByPopularity(boolean isFamous, Locale localeUsedForSorter) throws DaoException {
        List<Competence> result = null;
        String requete = null;
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("isFamous", isFamous);
            if (localeUsedForSorter == Locale.FRENCH) {
                requete = "FrenchCompetenceByPopularity";
            } else {
                requete = "EnglishCompetenceByPopularity";
            }
            result = initializeCompetence(getResultList(requete, params));
        } catch (Throwable ex) {
            throw new DaoException(ex);
        }
        return result;
    }

    /**
	 * Finds an competence by its english name.
	 * 
	 * @param name for fetching
	 * @return the competence
	 * @throws DaoException when the operation doesn't complete
	 */
    public Competence findByEnglishName(String name) throws DaoException {
        Competence result = null;
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("name", name);
            result = initializeCompetence((Competence) getSingleResult("CompetenceByEnglishName", params));
        } catch (Throwable ex) {
            throw new DaoException(ex);
        }
        return result;
    }

    protected List<Competence> initializeCompetence(List<Competence> list) {
        if (list != null) {
            for (Competence comp : list) {
                initializeCompetence(comp);
            }
        }
        return list;
    }

    protected Competence initializeCompetence(Competence competence) {
        if (competence != null) {
            initialize(competence.getGroup());
        }
        return competence;
    }
}
