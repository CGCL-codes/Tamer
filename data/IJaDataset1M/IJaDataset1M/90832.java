package playground.dgrether.signalsystems.tacontrol.model;

import org.matsim.api.core.v01.Id;
import org.matsim.signalsystems.builder.DefaultSignalModelFactory;
import org.matsim.signalsystems.builder.SignalModelFactory;
import org.matsim.signalsystems.data.signalcontrol.v20.SignalPlanData;
import org.matsim.signalsystems.model.SignalController;
import org.matsim.signalsystems.model.SignalPlan;
import org.matsim.signalsystems.model.SignalSystem;
import org.matsim.signalsystems.model.SignalSystemsManager;
import playground.dgrether.signalsystems.DgSensorManager;

/**
 * @author dgrether
 *
 */
public class DgTaSignalModelFactory implements SignalModelFactory {

    public DgTaSignalModelFactory(DefaultSignalModelFactory defaultSignalModelFactory, DgSensorManager sensorManager) {
    }

    @Override
    public SignalSystemsManager createSignalSystemsManager() {
        return null;
    }

    @Override
    public SignalSystem createSignalSystem(Id id) {
        return null;
    }

    @Override
    public SignalController createSignalSystemController(String controllerIdentifier) {
        return null;
    }

    @Override
    public SignalPlan createSignalPlan(SignalPlanData planData) {
        return null;
    }
}
