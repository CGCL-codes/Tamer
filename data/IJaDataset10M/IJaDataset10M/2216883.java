package jhomenet.commons.weather;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import jhomenet.commons.GeneralApplicationContext;
import jhomenet.commons.JHomenetException;
import jhomenet.commons.persistence.WeatherChannelConnectionPersistenceFacade;
import jhomenet.commons.polling.PollingIntervals;
import jhomenet.commons.service.AbstractExecutableQuartzService;
import jhomenet.commons.work.WorkException;
import jhomenet.commons.work.WorkQueue;
import jhomenet.commons.work.unit.AbstractWorkUnit;

/**
 * TODO: Class description.
 *
 * @author Dave Irwin (jhomenet at gmail dot com)
 */
public class WeatherGatewayManager extends AbstractExecutableQuartzService {

    /**
	 * Reference to logging mechanism.
	 */
    private static Logger logger = Logger.getLogger(WeatherGatewayManager.class.getName());

    /**
	 * 
	 */
    private static WeatherGatewayManager instance;

    /**
	 * List of weather gateways.
	 */
    private final Map<String, WeatherGateway> gateways = new HashMap<String, WeatherGateway>();

    /**
	 * 
	 */
    private final WorkQueue workQueue;

    /**
	 * 
	 */
    private WeatherChannelConnectionPersistenceFacade persistenceLayer;

    /**
	 * List of listeners.
	 */
    private final List<WeatherGatewayManagerListener> listeners = new CopyOnWriteArrayList<WeatherGatewayManagerListener>();

    /**
	 * Hardware polling description.
	 */
    public static final String WEATHER_POLLING_GROUP = "Weather polling";

    /**
	 * Package private hardware polling service identifier.
	 */
    static final String WEATHER_SERVICE_ID = "WeatherPollingService";

    /**
	 * The polling startup delay (in milliseconds).
	 */
    private final Long pollingStartupDelay = new Long(30 * 1000);

    /**
	 * 
	 * @param persistenceLayer
	 * @param workQueue
	 * @param serverContext
	 * @return
	 */
    public static final synchronized WeatherGatewayManager buildInstance(WeatherChannelConnectionPersistenceFacade persistenceLayer, WorkQueue workQueue, GeneralApplicationContext serverContext) {
        if (instance == null) instance = new WeatherGatewayManager(persistenceLayer, workQueue, serverContext);
        return instance;
    }

    /**
	 * 
	 * @return
	 */
    public static final WeatherGatewayManager getInstance() {
        return instance;
    }

    /**
	 * 
	 * @param persistenceLayer
	 * @param workQueue
	 */
    private WeatherGatewayManager(WeatherChannelConnectionPersistenceFacade persistenceLayer, WorkQueue workQueue, GeneralApplicationContext serverContext) {
        super(serverContext);
        if (persistenceLayer == null) throw new IllegalArgumentException("Persistence layer cannot be null!");
        if (workQueue == null) throw new IllegalArgumentException("Work queue cannot be null!");
        this.persistenceLayer = persistenceLayer;
        this.workQueue = workQueue;
    }

    /**
	 * @see jhomenet.commons.service.Service#getServiceName()
	 */
    @Override
    public String getServiceName() {
        return "Weather gateway manager service";
    }

    /**
	 * @see jhomenet.commons.service.Service#initializeListeners()
	 */
    @Override
    public void initializeListeners() {
    }

    /** 
	 * @see jhomenet.commons.service.AbstractExecutableQuartzService#initializeServiceStub()
	 */
    @Override
    protected void initializeServiceStub() {
        logger.debug("Initializing the weather gateway service...");
        long pollingStartTime = System.currentTimeMillis() + pollingStartupDelay;
        SimpleTrigger thirtyMinuteTrigger = new SimpleTrigger("Thirty minute trigger", "Weather gateway group", new Date(pollingStartTime), null, SimpleTrigger.REPEAT_INDEFINITELY, 1000 * 60 * 30);
        JobDetail pollingJob = new JobDetail("Weather poller", WeatherGatewayManager.WEATHER_POLLING_GROUP, CurrentConditionPollingJob.class);
        pollingJob.getJobDataMap().put(WEATHER_SERVICE_ID, this);
        addJob(pollingJob, thirtyMinuteTrigger);
    }

    /**
	 * 
	 * @throws JHomenetException
	 */
    public final void buildManager() throws JHomenetException {
        try {
            this.workQueue.addWork(new AbstractWorkUnit<AbstractWorkUnit.Void>() {

                /**
				 * @see jhomenet.commons.work.unit.AbstractWorkUnit#execute()
				 */
                @Override
                public AbstractWorkUnit.Void execute() throws WorkException {
                    List<WeatherGatewayConnectionInfo> connectionList = persistenceLayer.getAllConnectionInfo();
                    logger.debug("Retrieved " + connectionList + " Weather Channel connection info objects");
                    for (WeatherGatewayConnectionInfo connectionInfo : connectionList) {
                        addConnectionInfoInternal(connectionInfo, false);
                    }
                    return AbstractWorkUnit.VOID;
                }

                /**
				 * @see jhomenet.commons.work.unit.WorkUnit#getDescription()
				 */
                public String getDescription() {
                    return "Loading Weather Channel connection info";
                }
            }).get();
        } catch (InterruptedException ie) {
            throw new JHomenetException(ie);
        } catch (ExecutionException ee) {
            throw new JHomenetException(ee);
        }
    }

    /**
	 * @return the gateways
	 */
    public final List<WeatherGateway> getGatewaysAsList() {
        return new ArrayList<WeatherGateway>(gateways.values());
    }

    /**
	 * 
	 * @param zipcode
	 * @return
	 */
    public final WeatherGateway getGateway(String zipcode) {
        return this.gateways.get(zipcode);
    }

    /**
	 * 
	 * @param connectionInfo
	 */
    public final void addConnection(WeatherGatewayConnectionInfo connectionInfo) {
        addConnectionInfoInternal(connectionInfo, true);
    }

    /**
	 * 
	 * @param connectionInfo
	 * @param persistData
	 */
    private void addConnectionInfoInternal(final WeatherGatewayConnectionInfo connectionInfo, Boolean persistData) {
        WeatherGateway gw = new WeatherGateway(connectionInfo);
        this.gateways.put(connectionInfo.getZipcode(), gw);
        if (persistData) {
            this.workQueue.addWork(new AbstractWorkUnit<WeatherGatewayConnectionInfo>() {

                /**
				 * @see jhomenet.commons.work.unit.AbstractWorkUnit#execute()
				 */
                @Override
                public WeatherGatewayConnectionInfo execute() throws WorkException {
                    return persistenceLayer.storeConnectionInfo(connectionInfo);
                }

                /**
				 * @see jhomenet.commons.work.unit.WorkUnit#getDescription()
				 */
                public String getDescription() {
                    return "Persisting Weather Channel connection info";
                }
            });
        }
        fireNewConnectionInfoAdded(gw);
    }

    /**
	 * 
	 * @param l
	 */
    public void addListener(WeatherGatewayManagerListener l) {
        listeners.add(l);
    }

    /**
	 * 
	 * @param l
	 */
    public void remoteListener(WeatherGatewayManagerListener l) {
        listeners.remove(l);
    }

    /**
	 * 
	 * @param connectionInfo
	 */
    private void fireNewConnectionInfoAdded(WeatherGateway connectionInfo) {
        logger.debug("Firing new connection info");
        logger.debug("Number of listeners receiving event: " + listeners.size());
        if (listeners.size() == 0) {
            return;
        }
        for (WeatherGatewayManagerListener listener : listeners) {
            try {
                listener.weatherChannelConnectionInfoAdded(new WeatherGatewayManagerEvent(connectionInfo));
            } catch (RuntimeException rte) {
                logger.error("Unexcepted error while notifying event listeners: " + rte.getMessage());
                listeners.remove(listener);
            }
        }
    }
}
