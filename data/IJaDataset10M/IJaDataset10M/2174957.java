package at.racemgr.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import at.racemgr.be.Team;

/**
 * DAO for teams
 */
public class TeamDao extends AbstractDao<Team> {

    public TeamDao() {
        super();
    }

    /**
     * constructor for unit-tests
     * 
     * @param em
     */
    public TeamDao(EntityManager em) {
        super(em, Team.class);
    }

    @SuppressWarnings("unchecked")
    public List<Team> findByName(String teamName, String engineName) {
        Query query = getEntityManager().createQuery("select d from Team as d where " + "d.teamName = :teamName and d.engineName = :engineName");
        query.setParameter("teamName", teamName);
        query.setParameter("engineName", engineName);
        return query.getResultList();
    }
}
