package com.ixora.rms.batch;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import com.ixora.RMIServiceNames;
import com.ixora.common.RMIServices;
import com.ixora.common.exception.AppException;
import com.ixora.common.logging.AppLogger;
import com.ixora.common.logging.AppLoggerFactory;
import com.ixora.rms.EntityDescriptorTree;
import com.ixora.rms.ResourceId;
import com.ixora.rms.agents.AgentId;
import com.ixora.rms.agents.AgentState;
import com.ixora.rms.client.QueryRealizer;
import com.ixora.rms.client.QueryRealizerImpl;
import com.ixora.rms.client.model.SessionModel;
import com.ixora.rms.client.session.MonitoringSessionDescriptor;
import com.ixora.rms.client.session.MonitoringSessionRealizer;
import com.ixora.rms.exception.RMSException;
import com.ixora.rms.providers.ProviderState;
import com.ixora.rms.reactions.ReactionLogRecord;
import com.ixora.rms.services.AgentRepositoryService;
import com.ixora.rms.services.DashboardRepositoryService;
import com.ixora.rms.services.DataEngineService;
import com.ixora.rms.services.DataLogService;
import com.ixora.rms.services.DataViewRepositoryService;
import com.ixora.rms.services.HostMonitorService;
import com.ixora.rms.services.MonitoringSessionService;
import com.ixora.rms.services.ProviderInstanceRepositoryService;
import com.ixora.rms.services.ReactionLogService;
import com.ixora.rms.services.ReactionLogService.ReactionLogEvent;

/**
 * @author Daniel Moraru
 */
public class BatchSessionImpl extends UnicastRemoteObject implements BatchSession {

    private static final long serialVersionUID = -4291710964007150834L;

    /** Logger */
    private static final AppLogger logger = AppLoggerFactory.getLogger(BatchSessionImpl.class);

    /**
	 * This is the model of the monitoring session. It's the object
	 * containing the state of this session, all hosts, agents,
	 * discovered entities and their current configuration.
	 */
    private SessionModel fSessionModel;

    /** Session configuration */
    private BatchSessionConfiguration fSessionConf;

    /** Query realizer */
    private QueryRealizer fQueryRealizer;

    /** Monitoring session descriptor */
    private MonitoringSessionDescriptor fSessionDescriptor;

    private MonitoringSessionService fMonitoringSession;

    private HostMonitorService fHostMonitor;

    private DataViewRepositoryService fDataViewRepository;

    private AgentRepositoryService fAgentRepository;

    private DataEngineService fDataEngine;

    private DataLogService fDataLogger;

    private ReactionLogService fReactionLog;

    private EventHandler fEventHandler;

    /**
	 * Event handler.
	 */
    private class EventHandler implements MonitoringSessionService.Listener, SessionModel.RoughListener, DataLogService.Listener, ReactionLogService.Listener {

        /**
		 * @see com.ixora.rms.client.model.SessionModel.RoughListener#modelChanged(com.ixora.rms.internal.ResourceId[], int, int)
		 */
        public void modelChanged(final ResourceId[] ridChanged, final int change, final int changeType) {
            handleSessionModelChange(ridChanged, change, changeType);
        }

        /**
         * @see com.ixora.rms.services.MonitoringSessionService.Listener#agentNonFatalError(java.lang.String, com.ixora.rms.agents.AgentId, java.lang.Throwable)
         */
        public void agentNonFatalError(final String host, final AgentId agentId, final Throwable t) {
            handleAgentNonFatalError(host, agentId, t);
        }

        public void agentStateChanged(String host, AgentId agentId, AgentState state, Throwable e) {
        }

        public void entitiesChanged(String host, AgentId agentId, EntityDescriptorTree entities) {
        }

        public void providerStateChanged(String host, AgentId agentId, String providerInstanceName, ProviderState state, Throwable e) {
        }

        /**
		 * @see com.ixora.rms.services.DataLogService.Listener#error(java.lang.Throwable)
		 */
        public void error(final Throwable t) {
            handleFatalLoggingError(t);
        }

        /**
		 * @see com.ixora.rms.services.ReactionLogService.Listener#reactionLogEvent(com.ixora.rms.services.ReactionLogService.ReactionLogEvent)
		 */
        public void reactionLogEvent(final ReactionLogEvent event) {
            handleReactionLogEvent(event);
        }
    }

    /**
	 * @throws RemoteException
	 */
    public BatchSessionImpl(MonitoringSessionService mss, HostMonitorService hms, DataEngineService des, DataLogService dls, ReactionLogService rls, DashboardRepositoryService qgrs, DataViewRepositoryService dvrs, AgentRepositoryService ars, ProviderInstanceRepositoryService pirs) throws RemoteException {
        super(RMIServices.instance().getPort(RMIServiceNames.BATCHSESSION));
        fEventHandler = new EventHandler();
        fMonitoringSession = mss;
        fHostMonitor = hms;
        fDataViewRepository = dvrs;
        fAgentRepository = ars;
        fDataEngine = des;
        fDataEngine.setLogReplayMode(false);
        fDataLogger = dls;
        fReactionLog = rls;
        fSessionModel = new SessionModel(mss, hms, qgrs, dvrs, pirs);
        fQueryRealizer = new QueryRealizerImpl(fSessionModel, fMonitoringSession, fDataEngine, fDataViewRepository);
        fReactionLog.addListener(fEventHandler);
        fMonitoringSession.addListener(fEventHandler);
        fSessionModel.addListener(fEventHandler);
    }

    /**
	 * @param ridChanged
	 * @param change
	 * @param changeType
	 */
    private void handleSessionModelChange(ResourceId[] ridChanged, int change, int changeType) {
        try {
        } catch (Exception e) {
            logger.error(e);
        }
    }

    /**
	 * @param host
	 * @param agentId
	 * @param t
	 */
    private void handleAgentNonFatalError(String host, AgentId agentId, Throwable t) {
        try {
            logger.error("Agent non-fatal error for " + host + "/" + agentId, t);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    /**
	 * @param t
	 */
    private void handleFatalLoggingError(Throwable t) {
        try {
            logger.error("Fatal logging error", t);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    /**
	 * @param event
	 */
    private void handleReactionLogEvent(ReactionLogEvent event) {
        try {
            ReactionLogRecord record = fReactionLog.getRecord(event.getReactionId());
            if (record == null) {
                throw new RMSException("Reaction with id " + event.getReactionId() + " not found");
            }
            logger.info(">>>> Reaction: " + record.getReactionDeliveryInfo().getMessage());
        } catch (Exception e) {
            logger.error(e);
        }
    }

    /**
	 * @throws RMSException
	 * @see com.ixora.rms.batch.BatchSession#configure(com.ixora.rms.batch.BatchSessionConfiguration)
	 */
    public void configure(BatchSessionConfiguration conf) throws RemoteException, RMSException {
        try {
            fSessionConf = conf;
            fSessionDescriptor = MonitoringSessionLoader.loadSession(conf.getMonitoringSessionName());
            MonitoringSessionRealizer realizer = new MonitoringSessionRealizer(fSessionModel);
            realizer.realize(fHostMonitor, fAgentRepository, fMonitoringSession, fSessionDescriptor);
            realizer.realizeQueries(fQueryRealizer, fSessionDescriptor, fDataViewRepository);
            realizer.realizeDashboards(fQueryRealizer, fSessionDescriptor);
        } catch (RMSException e) {
            logger.error(e);
            try {
                deactivateAllAgents();
            } catch (Exception ex) {
                logger.error(ex);
            }
            throw e;
        } catch (Throwable e) {
            logger.error(e);
            try {
                deactivateAllAgents();
            } catch (Exception ex) {
                logger.error(ex);
            }
            throw new RMSException(e);
        }
    }

    /**
	 *
	 */
    private void deactivateAllAgents() {
        fMonitoringSession.deactivateAllAgents();
    }

    /**
	 * @throws RMSException
	 * @see com.ixora.rms.batch.BatchSession#start()
	 */
    public void start() throws RemoteException, RMSException {
        try {
            fMonitoringSession.startAllAgents();
            fDataLogger.startLogging(fSessionConf.getLogRepositoryInfo(), fSessionDescriptor, fEventHandler);
        } catch (RMSException e) {
            logger.error(e);
            throw e;
        } catch (Throwable e) {
            logger.error(e);
            throw new RMSException(e);
        }
    }

    /**
	 * @throws RMSException
	 * @see com.ixora.rms.batch.BatchSession#stop()
	 */
    public void stop() throws RemoteException, RMSException {
        try {
            fDataLogger.stopLogging();
            fMonitoringSession.stopAllAgents();
            fMonitoringSession.deactivateAllAgents();
        } catch (RMSException e) {
            logger.error(e);
            throw e;
        } catch (Throwable e) {
            logger.error(e);
            throw new RMSException(e);
        }
    }

    /**
	 * @see com.ixora.common.remote.Shutdownable#shutdown()
	 */
    public void shutdown() throws AppException, RemoteException {
        try {
            try {
                fDataLogger.stopLogging();
            } catch (Throwable e) {
                logger.error(e);
            }
            try {
                fMonitoringSession.stopAllAgents();
            } catch (Throwable e) {
                logger.error(e);
            }
            try {
                fMonitoringSession.deactivateAllAgents();
            } catch (Throwable e) {
                logger.error(e);
            }
            try {
                fDataLogger.shutdown();
            } catch (Throwable e) {
                logger.error(e);
            }
            try {
                fMonitoringSession.shutdown();
            } catch (Throwable e) {
                logger.error(e);
            }
            UnicastRemoteObject.unexportObject(this, true);
        } finally {
            System.exit(0);
        }
    }
}
