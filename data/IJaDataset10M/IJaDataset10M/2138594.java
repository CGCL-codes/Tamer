package org.matsim.ptproject.qsim;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.mobsim.framework.listeners.SimulationListener;
import org.matsim.evacuation.shelters.signalsystems.SheltersDoorBlockerController;
import org.matsim.signalsystems.config.AdaptivePlanBasedSignalSystemControlInfo;
import org.matsim.signalsystems.config.AdaptiveSignalSystemControlInfo;
import org.matsim.signalsystems.config.PlanBasedSignalSystemControlInfo;
import org.matsim.signalsystems.config.SignalSystemConfiguration;
import org.matsim.signalsystems.config.SignalSystemConfigurations;
import org.matsim.signalsystems.control.AdaptivePlanBasedSignalSystemControler;
import org.matsim.signalsystems.control.AdaptiveSignalSystemControler;
import org.matsim.signalsystems.control.DefaultPlanBasedSignalSystemController;
import org.matsim.signalsystems.control.PlanBasedSignalSystemController;
import org.matsim.signalsystems.control.SignalSystemController;
import org.matsim.signalsystems.mobsim.SignalEngine;
import org.matsim.signalsystems.systems.SignalGroupDefinition;
import org.matsim.signalsystems.systems.SignalSystemDefinition;
import org.matsim.signalsystems.systems.SignalSystems;

public class QSimSignalEngine implements SignalEngine, SimEngine {

    private static final Logger log = Logger.getLogger(QSimSignalEngine.class);

    /**
	 * The SignalSystemDefinitions accessible by their Id
	 */
    private SortedMap<Id, SignalSystemDefinition> signalSystemDefinitions;

    /**
	 * The SignalGroupDefinitions accessible by the Id of the SignalSystem they belong
	 * to.
	 */
    private SortedMap<Id, List<SignalGroupDefinition>> signalGroupDefinitionsBySystemId;

    /**
	 * Contains the SignalSystemControler instances which can be accessed by the
	 * Id of the SignalSystemDefinition
	 */
    private SortedMap<Id, SignalSystemController> signalSystemControlerBySystemId;

    private SignalSystems signalSystems;

    private SignalSystemConfigurations signalSystemsConfig;

    private QNetwork network;

    private QSim simulation;

    private EventsManager events;

    public QSimSignalEngine(QSim sim) {
        this.simulation = sim;
        this.network = sim.getQNetwork();
        this.events = sim.getEventsManager();
    }

    public void setSignalSystems(final SignalSystems signalSystems, final SignalSystemConfigurations signalSystemConfigurations) {
        this.signalSystems = signalSystems;
        this.signalSystemsConfig = signalSystemConfigurations;
    }

    /**
	 * Initialize the signal systems
	 */
    @Override
    public void onPrepareSim() {
        if (this.signalSystems != null) {
            initSignalSystems(this.signalSystems);
        }
        if (this.signalSystemsConfig != null) {
            initSignalSystemController(this.signalSystemsConfig);
        }
    }

    public void beforeSimStep(double time) {
        for (SignalGroupDefinition signalGroup : this.signalSystems.getSignalGroupDefinitions().values()) {
            Id linkId = signalGroup.getLinkRefId();
            QLinkLanesImpl qlink = (QLinkLanesImpl) this.network.getQLink(linkId);
            for (QLane qlane : qlink.getToNodeQueueLanes()) {
                qlane.updateGreenState(time);
            }
        }
    }

    /**
	 * @see org.matsim.signalsystems.mobsim.SignalEngine#getSignalSystemControlerBySystemId()
	 */
    public SortedMap<Id, SignalSystemController> getSignalSystemControlerBySystemId() {
        return this.signalSystemControlerBySystemId;
    }

    /**
	 * @see org.matsim.signalsystems.mobsim.SignalEngine#getSignalSystemDefinitions()
	 */
    public SortedMap<Id, SignalSystemDefinition> getSignalSystemDefinitions() {
        return this.signalSystemDefinitions;
    }

    private void initSignalSystems(final SignalSystems signalSystems) {
        this.signalSystemDefinitions = new TreeMap<Id, SignalSystemDefinition>();
        this.signalSystemDefinitions.putAll(signalSystems.getSignalSystemDefinitions());
        this.signalGroupDefinitionsBySystemId = new TreeMap<Id, List<SignalGroupDefinition>>();
        for (SignalGroupDefinition signalGroupDefinition : signalSystems.getSignalGroupDefinitions().values()) {
            QLink queueLink = this.network.getQLink(signalGroupDefinition.getLinkRefId());
            if (queueLink == null) {
                throw new IllegalStateException("SignalGroupDefinition Id: " + signalGroupDefinition.getId() + " of SignalSystem Id:  " + signalGroupDefinition.getSignalSystemDefinitionId() + " is set to non existing Link with Id: " + signalGroupDefinition.getLinkRefId());
            }
            if (signalGroupDefinition.getSignalSystemDefinitionId() == null) {
                log.warn("SignalGroupDefinition Id: " + signalGroupDefinition.getId() + " is not attached to a SignalSystem (SignalSystemDefinitionId not set). SignalGroup will not be used!");
                continue;
            }
            if (signalGroupDefinition.getLaneIds() == null) {
                log.warn("SignalGroupDefinition Id: " + signalGroupDefinition.getId() + " of SignalSystem Id:  " + signalGroupDefinition.getSignalSystemDefinitionId() + " is not attached to a lane. SignalGroup will not be used!");
                continue;
            }
            List<SignalGroupDefinition> list = this.signalGroupDefinitionsBySystemId.get(signalGroupDefinition.getSignalSystemDefinitionId());
            if (list == null) {
                list = new ArrayList<SignalGroupDefinition>();
                this.signalGroupDefinitionsBySystemId.put(signalGroupDefinition.getSignalSystemDefinitionId(), list);
            }
            list.add(signalGroupDefinition);
            ((QLinkLanesImpl) queueLink).addSignalGroupDefinition(signalGroupDefinition);
            this.network.getNodes().get(queueLink.getLink().getToNode().getId()).setSignalized(true);
        }
    }

    private void initSignalSystemController(final SignalSystemConfigurations signalSystemConfigurations) {
        this.signalSystemControlerBySystemId = new TreeMap<Id, SignalSystemController>();
        for (SignalSystemConfiguration config : signalSystemConfigurations.getSignalSystemConfigurations().values()) {
            SignalSystemController systemControler = null;
            if (this.signalSystemControlerBySystemId.containsKey(config.getSignalSystemId())) {
                throw new IllegalStateException("SignalSystemControler for SignalSystem with id: " + config.getSignalSystemId() + " already exists. Cannot add second SignalSystemControler for same system. Check your" + " signal system's configuration file.");
            }
            if (config.getControlInfo() instanceof AdaptivePlanBasedSignalSystemControlInfo) {
                AdaptiveSignalSystemControler c = createAdaptiveControler((AdaptiveSignalSystemControlInfo) config.getControlInfo());
                if (!(c instanceof PlanBasedSignalSystemController)) {
                    throw new IllegalArgumentException("Class " + c.getClass().getName() + "is no PlanBasedSignalSystemController instance. Check your configuration of the signal system control!");
                }
                AdaptivePlanBasedSignalSystemControler controler = (AdaptivePlanBasedSignalSystemControler) c;
                systemControler = controler;
            } else if (config.getControlInfo() instanceof AdaptiveSignalSystemControlInfo) {
                AdaptiveSignalSystemControler controler = createAdaptiveControler((AdaptiveSignalSystemControlInfo) config.getControlInfo());
                systemControler = controler;
            } else if (config.getControlInfo() instanceof PlanBasedSignalSystemControlInfo) {
                DefaultPlanBasedSignalSystemController controler = new DefaultPlanBasedSignalSystemController(config);
                systemControler = controler;
            }
            if (systemControler != null) {
                this.initSignalSystemControlerDefaults(systemControler, config);
                this.signalSystemControlerBySystemId.put(config.getSignalSystemId(), systemControler);
                systemControler.setSignalEngine(this);
                if (systemControler instanceof SimulationListener) {
                    this.simulation.addQueueSimulationListeners((SimulationListener) systemControler);
                }
                List<SignalGroupDefinition> groups = this.signalGroupDefinitionsBySystemId.get(config.getSignalSystemId());
                if ((groups == null) || groups.isEmpty()) {
                    String message = "SignalSystemControler for SignalSystem Id: " + config.getSignalSystemId() + " without any SignalGroups defined in SignalSystemConfiguration!";
                    log.warn(message);
                } else {
                    for (SignalGroupDefinition group : groups) {
                        systemControler.getSignalGroups().put(group.getId(), group);
                        group.setResponsibleLSAControler(systemControler);
                    }
                }
            } else {
                log.error("Could not initialize signal system controler for signal system with id: " + config.getSignalSystemId() + " " + "Check stacktrace for details.");
            }
        }
    }

    private AdaptiveSignalSystemControler createAdaptiveControler(final AdaptiveSignalSystemControlInfo config) {
        String controllerName = config.getAdaptiveControlerClass();
        AdaptiveSignalSystemControler controler = null;
        if (controllerName == null) {
            throw new IllegalArgumentException("controler class must be given");
        }
        if (controllerName.startsWith("org.matsim")) {
            throw new IllegalArgumentException("Loading classes by name within the org.matsim packages is not allowed!");
        } else if (controllerName.equalsIgnoreCase("SheltersDoorBlockerController")) {
            controler = new SheltersDoorBlockerController(config);
        } else {
            try {
                Class<? extends AdaptiveSignalSystemControler> klas = (Class<? extends AdaptiveSignalSystemControler>) Class.forName(config.getAdaptiveControlerClass());
                Class[] args = new Class[1];
                args[0] = AdaptiveSignalSystemControlInfo.class;
                Constructor<? extends AdaptiveSignalSystemControler> c = klas.getConstructor(args);
                controler = c.newInstance(config);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        if (controler == null) {
            throw new IllegalStateException("Cannot create AdaptiveSignalSystemControler for class name: " + config.getAdaptiveControlerClass());
        }
        return controler;
    }

    private void initSignalSystemControlerDefaults(final SignalSystemController controler, final SignalSystemConfiguration config) {
        SignalSystemDefinition systemDef = this.signalSystemDefinitions.get(config.getSignalSystemId());
        controler.setDefaultCycleTime(systemDef.getDefaultCycleTime());
        controler.setDefaultInterGreenTime(systemDef.getDefaultInterGreenTime());
        controler.setDefaultSynchronizationOffset(systemDef.getDefaultSynchronizationOffset());
    }

    public EventsManager getEvents() {
        return this.events;
    }

    @Override
    public void afterSim() {
    }

    @Override
    public QSim getQSim() {
        return this.simulation;
    }
}
