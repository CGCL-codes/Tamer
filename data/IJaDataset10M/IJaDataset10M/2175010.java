package fr.xebia.demo.objectgrid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import com.ibm.websphere.objectgrid.ObjectGrid;
import com.ibm.websphere.objectgrid.Session;
import com.ibm.websphere.objectgrid.UndefinedMapException;
import com.ibm.websphere.objectgrid.datagrid.AgentManager;
import com.ibm.websphere.objectgrid.datagrid.MapGridAgent;
import com.ibm.websphere.objectgrid.em.EntityManager;

/**
 * <p>
 * Helper class that simplifies ObjectGrid data access code.
 * </p>
 * 
 * <p>
 * Inspired by Spring's <a
 * href="http://www.springframework.org/docs/api/org/springframework/orm/hibernate/HibernateTemplate.html">HibernateTemplate</a>
 * </p>
 * 
 * @author <a href="mailto:cyrille.leclerc@pobox.com">Cyrille Le Clerc</a>
 */
public class ObjectGridTemplate<Entity> {

    private static final Logger logger = Logger.getLogger(ObjectGridTemplate.class);

    /**
     * backing object grid
     */
    protected ObjectGrid objectGrid;

    public ObjectGridTemplate(ObjectGrid objectGrid) {
        super();
        this.objectGrid = objectGrid;
    }

    public List<Entity> findByNamedParam(String queryString, String paramName, Object value) {
        return findByNamedParam(queryString, new String[] { paramName }, new Object[] { value });
    }

    /**
     * <p>
     * see <a href="http://www.ibm.com/developerworks/forums/dw_thread.jsp?thread=175971&forum=778&cat=9&ca=drs-fo"> Best practice for
     * EntityManager queries on a partitioned grid</a>
     * </p>
     * 
     * @param queryString
     * @param paramNames
     * @param values
     * @return list of the matching entities found on the partitions of the grid
     */
    @SuppressWarnings("unchecked")
    public List<Entity> findByNamedParam(final String queryString, final String[] paramNames, final Object[] values) {
        Validate.notNull(paramNames, "paramNames");
        Validate.notNull(values, "values");
        Validate.isTrue(paramNames.length == values.length, "Length of paramNames array must match length of values array");
        if (logger.isDebugEnabled()) {
            logger.debug("> distributedFind(query=" + queryString + ", paramNames=" + Arrays.asList(paramNames) + ", values='" + Arrays.asList(values) + ")");
        }
        try {
            AgentManager agentManager = ObjectGridUtils.getCurrentSession(this.objectGrid).getMap(QueryAgent.AGENT_RUNNER_MAP_NAME).getAgentManager();
            MapGridAgent agent = new QueryAgent<Entity>(queryString, paramNames, values);
            Map<Integer, List<Entity>> employeesByPartitionId = agentManager.callMapAgent(agent);
            List<Entity> result = new ArrayList<Entity>();
            for (Entry<Integer, List<Entity>> entry : employeesByPartitionId.entrySet()) {
                Integer partitionId = entry.getKey();
                List<Entity> partitionResults = entry.getValue();
                if (logger.isDebugEnabled()) {
                    logger.debug("Found " + partitionResults.size() + " entries on partition " + partitionId);
                }
                result.addAll(partitionResults);
            }
            return result;
        } catch (RuntimeException e) {
            throw new RuntimeException("Exception querying '" + queryString + "' with paramNames=" + Arrays.asList(paramNames) + ", values=" + Arrays.asList(values) + " via the QueryAgent", e);
        } catch (UndefinedMapException e) {
            throw new RuntimeException("Map '" + QueryAgent.AGENT_RUNNER_MAP_NAME + "' used by QueryAgent to route queries was not found", e);
        }
    }

    /**
     * Obtains a {@link ThreadLocal} based {@link EntityManager}. If none exist, creates one.
     * 
     * @return the current EntityManager
     * @see org.hibernate.SessionFactory#getCurrentSession()
     */
    public EntityManager getCurrentEntityManager() {
        return ObjectGridUtils.getCurrentSession(this.objectGrid).getEntityManager();
    }

    /**
     * Obtains a {@link ThreadLocal} based {@link Session}. If none exist, creates one.
     * 
     * @return the current Session
     * @see org.hibernate.SessionFactory#getCurrentSession()
     */
    public Session getCurrentSession() {
        return ObjectGridUtils.getCurrentSession(objectGrid);
    }
}
