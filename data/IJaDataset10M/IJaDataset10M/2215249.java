package fr.xebia.demo.objectgrid.ticketing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import com.ibm.websphere.objectgrid.ObjectGridException;
import com.ibm.websphere.objectgrid.ObjectMap;
import com.ibm.websphere.objectgrid.Session;
import com.ibm.websphere.objectgrid.datagrid.EntityAgentMixin;
import com.ibm.websphere.objectgrid.datagrid.EntryErrorValue;
import com.ibm.websphere.objectgrid.datagrid.MapGridAgent;
import com.ibm.websphere.objectgrid.datagrid.ReduceGridAgent;
import com.ibm.websphere.objectgrid.em.EntityManager;
import com.ibm.websphere.objectgrid.em.Query;
import com.ibm.websphere.objectgrid.em.QueryQueue;

/**
 * Demo of a distributed agent that will on each partition process the salary of each employee and return to the grid client the result.
 * 
 * @author <a href="mailto:cyrille.leclerc@pobox.com">Cyrille Le Clerc</a>
 */
public class TrainSearchAgent implements ReduceGridAgent, EntityAgentMixin {

    private static final Logger logger = Logger.getLogger(TrainSearchAgent.class);

    private static final long serialVersionUID = 1L;

    protected String arrivalStationCode;

    protected String departureStationCode;

    protected Date departureTime;

    public TrainSearchAgent(String departureStationCode, Date departureTime, String arrivalStationCode) {
        super();
        this.departureStationCode = departureStationCode;
        this.departureTime = departureTime;
        this.arrivalStationCode = arrivalStationCode;
    }

    @Override
    public Class<?> getClassForEntity() {
        return Train.class;
    }

    @Override
    public Object reduce(Session session, ObjectMap objectmap) {
        List<RouteDetails> result = new ArrayList<RouteDetails>();
        EntityManager entityManager = session.getEntityManager();
        Query query = entityManager.createQuery("select t from Train t");
        logger.debug("Query plan : " + query.getPlan());
        for (Iterator it = query.getResultIterator(); it.hasNext(); ) {
            Train train = (Train) it.next();
            RouteDetails routeDetails = train.hasAvailableSeat(this.departureStationCode, this.departureTime, this.arrivalStationCode);
            if (routeDetails == null) {
                logger.debug("Train " + train.getCode() + " does not have available seat");
            } else {
                result.add(routeDetails);
            }
        }
        if (logger.isInfoEnabled()) {
            logger.info("reduce()" + result);
        }
        return result;
    }

    @Override
    public Object reduce(Session session, ObjectMap objectmap, Collection trains) {
        List<RouteDetails> result = new ArrayList<RouteDetails>();
        for (Iterator<?> it = trains.iterator(); it.hasNext(); ) {
            Train train = (Train) it.next();
            RouteDetails routeDetails = train.hasAvailableSeat(this.departureStationCode, this.departureTime, this.arrivalStationCode);
            if (routeDetails == null) {
                logger.debug("Train " + train.getCode() + " does not have available seat");
            } else {
                result.add(routeDetails);
            }
        }
        return result;
    }

    @Override
    public Object reduceResults(Collection collection) {
        List<List<RouteDetails>> typedCollection = (List<List<RouteDetails>>) collection;
        List<RouteDetails> result = new ArrayList<RouteDetails>();
        for (Iterator it = collection.iterator(); it.hasNext(); ) {
            Object object = it.next();
            if (object instanceof EntryErrorValue) {
                EntryErrorValue entryErrorValue = (EntryErrorValue) object;
                logger.error(entryErrorValue);
            } else if (object instanceof List) {
                List<RouteDetails> routeDetailsList = (List<RouteDetails>) object;
                result.addAll(routeDetailsList);
            } else {
                throw new IllegalStateException();
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("departureStationCode", this.departureStationCode).append("departureTime", this.departureTime).append("arrivalStationCode", this.arrivalStationCode).toString();
    }
}
