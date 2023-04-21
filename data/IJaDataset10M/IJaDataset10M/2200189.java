package jhomenet.server.dao.txt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jhomenet.commons.responsive.exec.DefaultResponsiveStringConverter;
import jhomenet.commons.responsive.exec.ResponsiveConverter;
import jhomenet.commons.responsive.plan.Plan;
import jhomenet.server.JHomeNetServer;
import jhomenet.server.dao.PlanDao;

/**
 * TODO: Class description.
 * <p>
 * Id: $Id: $
 *
 * @author Dave Irwin (jhomenet at gmail dot com)
 */
public class PlanDaoTxt implements PlanDao {

    /**
	 * The sensor responsive object converter.
	 */
    private final ResponsiveConverter<String> converter;

    /**
	 * 
	 */
    private final Map<Long, String> cache = new HashMap<Long, String>();

    /**
	 * 
	 */
    public PlanDaoTxt() {
        super();
        cache.put(ResponsiveTxtDaoUtils.cacheStatusKey, ResponsiveTxtDaoUtils.cacheStatusRequiresBuilding);
        converter = new DefaultResponsiveStringConverter(JHomeNetServer.serverContext.getHardwareManager());
    }

    /**
	 * @see jhomenet.server.dao.GenericDao#clear()
	 */
    public void clear() {
    }

    /**
	 * @see jhomenet.server.dao.GenericDao#findAll()
	 */
    public List<Plan> findAll() {
        return null;
    }

    /**
	 * @see jhomenet.server.dao.GenericDao#findByExample(java.lang.Object, java.lang.String[])
	 */
    public List<Plan> findByExample(Plan exampleInstance, String... excludeProperty) {
        return null;
    }

    /**
	 * @see jhomenet.server.dao.GenericDao#findById(java.io.Serializable, boolean)
	 */
    public Plan findById(Long id, boolean lock) {
        return null;
    }

    /**
	 * @see jhomenet.server.dao.GenericDao#flush()
	 */
    public void flush() {
    }

    /**
	 * @see jhomenet.server.dao.GenericDao#makePersistent(java.lang.Object)
	 */
    public Plan makePersistent(Plan entity) {
        return null;
    }

    /**
	 * @see jhomenet.server.dao.GenericDao#makeTransient(java.lang.Object)
	 */
    public void makeTransient(Plan entity) {
    }
}
