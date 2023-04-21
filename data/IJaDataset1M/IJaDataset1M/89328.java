package jade.core.sam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import jade.core.Agent;
import jade.core.BaseService;
import jade.core.HorizontalCommand;
import jade.core.IMTPException;
import jade.core.Node;
import jade.core.Profile;
import jade.core.Service;
import jade.core.ServiceException;
import jade.core.ServiceHelper;
import jade.core.Specifier;
import jade.core.VerticalCommand;
import jade.util.Logger;

/**
 * JADE Kernel service supporting System Activity Monitoring (SAM).
 */
public class SAMService extends BaseService {

    public static final String POLLING_PERIOD = "jade_core_sam_SAMService_pollingperiod";

    public static final int POLLING_PERIOD_DEFAULT = 1;

    public static final String SAM_INFO_HANDLERS = "jade_core_sam_SAMService_handlers";

    public static final String SAM_INFO_HANDLERS_DEFAULT = "jade.core.sam.DefaultSAMInfoHandlerImpl";

    private List<EntityInfo> monitoredEntities = new ArrayList<EntityInfo>();

    private List<CounterInfo> monitoredCounters = new ArrayList<CounterInfo>();

    private Poller poller;

    private SAMHelper myHelper = new SAMHelperImpl();

    private ServiceComponent localSlice = new ServiceComponent();

    public String getName() {
        return SAMHelper.SERVICE_NAME;
    }

    @Override
    public void boot(Profile p) throws ServiceException {
        super.boot(p);
        if (p.isMasterMain()) {
            int periodMinutes = POLLING_PERIOD_DEFAULT;
            try {
                periodMinutes = Integer.parseInt(p.getParameter(POLLING_PERIOD, null));
            } catch (Exception e) {
            }
            myLogger.log(Logger.CONFIG, "Polling period = " + periodMinutes + " minutes");
            try {
                String hh = p.getParameter(SAM_INFO_HANDLERS, SAM_INFO_HANDLERS_DEFAULT);
                Vector handlerClasses = Specifier.parseList(hh, ';');
                SAMInfoHandler[] handlers = new SAMInfoHandler[handlerClasses.size()];
                for (int i = 0; i < handlerClasses.size(); ++i) {
                    String className = (String) handlerClasses.get(i);
                    myLogger.log(Logger.CONFIG, "Loading SAMInfoHandler class = " + className + "...");
                    handlers[i] = (SAMInfoHandler) Class.forName(className).newInstance();
                    handlers[i].initialize(p);
                    myLogger.log(Logger.CONFIG, "SAMInfoHandler of class = " + className + " successfully initialized");
                }
                poller = new Poller(this, periodMinutes * 60000, handlers);
                poller.startPolling();
            } catch (Exception e) {
                throw new ServiceException("Error initializing SAMInfoHandler", e);
            }
        }
    }

    @Override
    public void shutdown() {
        if (poller != null) {
            poller.stopPolling();
        }
        super.shutdown();
    }

    @Override
    public ServiceHelper getHelper(Agent a) {
        return myHelper;
    }

    @Override
    public Class getHorizontalInterface() {
        return SAMSlice.class;
    }

    @Override
    public Service.Slice getLocalSlice() {
        return localSlice;
    }

    private Map<String, AverageMeasure> getEntityMeasures() {
        synchronized (myHelper) {
            Map<String, AverageMeasure> entityMeasures = new HashMap<String, AverageMeasure>();
            for (EntityInfo info : monitoredEntities) {
                entityMeasures.put(info.getName(), info.getMeasure());
            }
            return entityMeasures;
        }
    }

    private Map<String, Long> getCounterValues() {
        synchronized (myHelper) {
            Map<String, Long> counterValues = new HashMap<String, Long>();
            for (CounterInfo info : monitoredCounters) {
                counterValues.put(info.getName(), info.getValue());
            }
            return counterValues;
        }
    }

    /**
	 * Inner class SAMHelperImpl
	 */
    private class SAMHelperImpl implements SAMHelper {

        public synchronized void addEntityMeasureProvider(String entityName, final MeasureProvider provider) {
            addEntityMeasureProvider(entityName, new AverageMeasureProvider() {

                public AverageMeasure getValue() {
                    Number value = provider.getValue();
                    if (value != null) {
                        return new AverageMeasure(value.doubleValue(), 1);
                    } else {
                        return new AverageMeasure(0, 0);
                    }
                }
            });
        }

        public synchronized void addEntityMeasureProvider(String entityName, AverageMeasureProvider provider) {
            EntityInfo info = getEntityInfo(entityName);
            info.addProvider(provider);
        }

        public synchronized void addCounterValueProvider(String counterName, CounterValueProvider provider) {
            CounterInfo info = getCounterInfo(counterName);
            info.addProvider(provider);
        }

        public void init(Agent a) {
        }
    }

    /**
	 * Inner class ServiceComponent
	 */
    private class ServiceComponent implements Service.Slice {

        public Service getService() {
            return SAMService.this;
        }

        public Node getNode() throws ServiceException {
            try {
                return SAMService.this.getLocalNode();
            } catch (IMTPException imtpe) {
                throw new ServiceException("Problem contacting the IMTP Manager", imtpe);
            }
        }

        public VerticalCommand serve(HorizontalCommand cmd) {
            try {
                String cmdName = cmd.getName();
                if (cmdName.equals(SAMSlice.H_GETSAMINFO)) {
                    SAMInfo info = new SAMInfo(getEntityMeasures(), getCounterValues());
                    cmd.setReturnValue(info);
                }
            } catch (Throwable t) {
                cmd.setReturnValue(t);
            }
            return null;
        }
    }

    /**
	 * Inner class EntityInfo
	 */
    private class EntityInfo {

        private String name;

        private List<AverageMeasureProvider> providers = new ArrayList<AverageMeasureProvider>();

        EntityInfo(String name) {
            this.name = name;
        }

        String getName() {
            return name;
        }

        void addProvider(AverageMeasureProvider provider) {
            providers.add(provider);
        }

        AverageMeasure getMeasure() {
            AverageMeasure result = new AverageMeasure();
            for (AverageMeasureProvider p : providers) {
                AverageMeasure m = p.getValue();
                result.update(m);
            }
            return result;
        }
    }

    private EntityInfo getEntityInfo(String entityName) {
        for (EntityInfo info : monitoredEntities) {
            if (info.getName().equals(entityName)) {
                return info;
            }
        }
        EntityInfo info = new EntityInfo(entityName);
        monitoredEntities.add(info);
        return info;
    }

    /**
	 * Inner class CounterInfo
	 */
    private class CounterInfo {

        private String name;

        private List<CounterValueProvider> providers = new ArrayList<CounterValueProvider>();

        private List<Long> previousTotalValues = new ArrayList<Long>();

        CounterInfo(String name) {
            this.name = name;
        }

        String getName() {
            return name;
        }

        void addProvider(CounterValueProvider provider) {
            providers.add(provider);
            previousTotalValues.add((long) 0);
        }

        long getValue() {
            long result = 0;
            for (int i = 0; i < providers.size(); ++i) {
                CounterValueProvider p = providers.get(i);
                long v = p.getValue();
                if (p.isDifferential()) {
                    result += v;
                } else {
                    result += v - previousTotalValues.get(i);
                    previousTotalValues.set(i, v);
                }
            }
            return result;
        }
    }

    private CounterInfo getCounterInfo(String counterName) {
        for (CounterInfo info : monitoredCounters) {
            if (info.getName().equals(counterName)) {
                return info;
            }
        }
        CounterInfo info = new CounterInfo(counterName);
        monitoredCounters.add(info);
        return info;
    }
}
