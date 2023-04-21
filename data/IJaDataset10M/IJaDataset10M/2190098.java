package net.googlecode.demenkov.dao.impl;

import net.googlecode.demenkov.dao.UniversityDAO;
import net.googlecode.demenkov.domains.University;
import org.springframework.stereotype.Repository;
import javax.persistence.Query;
import java.util.List;

/**
 * DAO class for actions with universities
 *
 * @author Demenkov Yura
 */
@Repository
public class UniversityDAOImpl extends GenericDAOImpl<University, Integer> implements UniversityDAO {

    public University findUniversityByUniversityName(String universityName) {
        Query query = entityManager.createQuery("from University where universityName=:universityname");
        query.setParameter("universityname", universityName);
        if (query.getResultList().size() > 0) {
            return (University) query.getResultList().get(0);
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public List<University> getAllUniversities() {
        return entityManager.createQuery("from University ").getResultList();
    }
}
