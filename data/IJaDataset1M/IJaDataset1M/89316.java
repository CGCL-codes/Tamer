package org.matsim.signalsystems.builder;

import java.util.Map;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.signalsystems.data.SignalsData;
import org.matsim.signalsystems.data.signalcontrol.v20.SignalPlanData;
import org.matsim.signalsystems.data.signalcontrol.v20.SignalSystemControllerData;
import org.matsim.signalsystems.data.signalgroups.v20.SignalGroupData;
import org.matsim.signalsystems.data.signalsystems.v20.SignalData;
import org.matsim.signalsystems.data.signalsystems.v20.SignalSystemData;
import org.matsim.signalsystems.model.AmberLogic;
import org.matsim.signalsystems.model.AmberLogicImpl;
import org.matsim.signalsystems.model.DatabasedSignal;
import org.matsim.signalsystems.model.IntergreensLogic;
import org.matsim.signalsystems.model.IntergreensLogicImpl;
import org.matsim.signalsystems.model.Signal;
import org.matsim.signalsystems.model.SignalController;
import org.matsim.signalsystems.model.SignalGroup;
import org.matsim.signalsystems.model.SignalGroupImpl;
import org.matsim.signalsystems.model.SignalPlan;
import org.matsim.signalsystems.model.SignalSystem;
import org.matsim.signalsystems.model.SignalSystemsManager;

/**
 * @author dgrether
 *
 */
public class FromDataBuilder implements SignalSystemsModelBuilder {

    private SignalsData signalsData;

    private SignalModelFactory factory = new DefaultSignalModelFactory();

    private EventsManager events;

    private Scenario scenario;

    public FromDataBuilder(Scenario scenario, SignalModelFactory factory, EventsManager events) {
        this.signalsData = scenario.getScenarioElement(SignalsData.class);
        this.scenario = scenario;
        this.factory = factory;
        this.events = events;
    }

    public FromDataBuilder(Scenario scenario, EventsManager events) {
        this(scenario, new DefaultSignalModelFactory(), events);
    }

    public void createAndAddSignals(SignalSystem system) {
        SignalSystemData ssData = signalsData.getSignalSystemsData().getSignalSystemData().get(system.getId());
        for (SignalData signalData : ssData.getSignalData().values()) {
            Signal signal = new DatabasedSignal(signalData);
            system.addSignal(signal);
        }
    }

    public void createAndAddSignalSystemsFromData(SignalSystemsManager manager) {
        for (SignalSystemData ssData : this.signalsData.getSignalSystemsData().getSignalSystemData().values()) {
            SignalSystem system = this.factory.createSignalSystem(ssData.getId());
            manager.addSignalSystem(system);
            system.setSignalSystemsManager(manager);
        }
    }

    public void createAndAddSignalGroupsFromData(SignalSystem system) {
        Map<Id, SignalGroupData> signalGroupDataMap = this.signalsData.getSignalGroupsData().getSignalGroupDataBySystemId(system.getId());
        for (SignalGroupData signalGroupData : signalGroupDataMap.values()) {
            SignalGroup group = new SignalGroupImpl(signalGroupData.getId());
            for (Id signalId : signalGroupData.getSignalIds()) {
                Signal signal = system.getSignals().get(signalId);
                group.addSignal(signal);
            }
            system.addSignalGroup(group);
        }
    }

    public void createAndAddSignalSystemControllerFromData(SignalSystem system) {
        SignalSystemControllerData systemControlData = signalsData.getSignalControlData().getSignalSystemControllerDataBySystemId().get(system.getId());
        SignalController controller = this.factory.createSignalSystemController(systemControlData.getControllerIdentifier());
        controller.setSignalSystem(system);
        system.setSignalSystemController(controller);
        for (SignalPlanData planData : systemControlData.getSignalPlanData().values()) {
            SignalPlan plan = this.factory.createSignalPlan(planData);
            controller.addPlan(plan);
        }
    }

    public void createAndAddAmberLogic(SignalSystemsManager manager) {
        if (this.scenario.getConfig().signalSystems().isUseAmbertimes()) {
            AmberLogic amberLogic = new AmberLogicImpl(this.signalsData.getAmberTimesData());
            manager.setAmberLogic(amberLogic);
        }
    }

    public SignalSystemsManager createSignalSystemManager() {
        SignalSystemsManager manager = this.factory.createSignalSystemsManager();
        manager.setSignalsData(this.signalsData);
        manager.setEventsManager(events);
        return manager;
    }

    public void createAndAddIntergreenTimesLogic(SignalSystemsManager manager) {
        if (this.scenario.getConfig().signalSystems().isUseIntergreenTimes()) {
            IntergreensLogic intergreensLogic = new IntergreensLogicImpl(this.signalsData.getIntergreenTimesData(), this.scenario.getConfig().signalSystems());
            this.events.addHandler(intergreensLogic);
        }
    }

    @Override
    public SignalSystemsManager createAndInitializeSignalSystemsManager() {
        SignalSystemsManager manager = this.createSignalSystemManager();
        this.createAndAddSignalSystemsFromData(manager);
        for (SignalSystem system : manager.getSignalSystems().values()) {
            this.createAndAddSignals(system);
            this.createAndAddSignalGroupsFromData(system);
            this.createAndAddSignalSystemControllerFromData(system);
        }
        this.createAndAddAmberLogic(manager);
        this.createAndAddIntergreenTimesLogic(manager);
        return manager;
    }

    @Override
    public SignalModelFactory getSignalModelFactory() {
        return this.factory;
    }

    @Override
    public void setSignalModelFactory(SignalModelFactory factory) {
        this.factory = factory;
    }
}
