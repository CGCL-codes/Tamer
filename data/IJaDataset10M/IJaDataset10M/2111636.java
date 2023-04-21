package fr.gfi.gfinet.server.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import fr.gfi.gfinet.common.exception.DaoException;
import fr.gfi.gfinet.server.info.MissionPeriod;

/**
 * @author Jean DAT
 * @since  Oct. 29, 2007
 */
public class MissionPeriodDao extends JpaSpringDao {

    protected static final Log logger = LogFactory.getLog(MissionPeriodDao.class);

    /**
	 * Lists all MissionPeriod.
	 * @throws DaoException when the operation doesn't complete
	 */
    public List<MissionPeriod> list() throws DaoException {
        List<MissionPeriod> result = null;
        try {
            result = getJpaTemplate().find("from MissionPeriod");
        } catch (Throwable ex) {
            throw new DaoException(ex);
        }
        return result;
    }

    /**
	 * Retrieves an mission by ID.
	 * 
	 * @param id
	 * @return
	 * @throws DaoException when the operation doesn't complete
	 */
    public MissionPeriod findById(long id) throws DaoException {
        MissionPeriod result = null;
        try {
            result = (MissionPeriod) findById(MissionPeriod.class, id);
        } catch (Throwable ex) {
            throw new DaoException(ex);
        }
        return result;
    }

    /**
	 * Multi-criteria research.
	 * @param criterions
	 * @return
	 * @throws DaoException
	 */
    public List<MissionPeriod> findByCriteria(HashMap<String, Object> params) throws DaoException {
        List<MissionPeriod> searchResults = null;
        List<Criterion> criterions = new ArrayList<Criterion>();
        Criterion criterion = null;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (entry.getKey() != null && entry.getKey().trim().length() > 0 && entry.getValue() != null) {
                if (entry.getValue() instanceof String) {
                    criterion = Expression.like(entry.getKey(), "%" + entry.getValue() + "%");
                } else {
                    criterion = Expression.eq(entry.getKey(), entry.getValue());
                }
                criterions.add(criterion);
            }
        }
        searchResults = findByCriteria(MissionPeriod.class, criterions);
        return searchResults;
    }
}
