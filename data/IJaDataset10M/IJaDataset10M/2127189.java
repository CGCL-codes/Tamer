package de.fraunhofer.isst.axbench.operations.simulator.filter;

import java.util.Collection;
import java.util.HashSet;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Shell;
import de.fraunhofer.isst.axbench.api.simulation.ISimulationElement;
import de.fraunhofer.isst.axbench.api.simulation.ISimulationEvent;
import de.fraunhofer.isst.axbench.api.simulation.ISimulationEventStructure;
import de.fraunhofer.isst.axbench.axlang.elements.Model;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.Component;
import de.fraunhofer.isst.axbench.operations.simulator.elements.SimulationPort;

/**
 * @brief filter for top level ports
 * 
 * @author mgrosse
 * @version 0.9.0
 * @since 0.9.0
 *
 */
public class TopLevelPortsFilter extends ElementsFilterHandler {

    /**
	 * @brief returns the top level ports, ie the ports of the top component of the simulated application model
	 * @return the top level ports, ie the ports of the top component of the simulated application model
	 */
    @Override
    public Collection<ISimulationElement> getVisibleElements(ISimulationEventStructure originEventStructure) {
        Collection<ISimulationElement> visibleElements = new HashSet<ISimulationElement>();
        for (ISimulationEvent originSimulationEvent : originEventStructure.getSimulationEvents()) {
            if ((originSimulationEvent.getSimulationElement() instanceof SimulationPort) && ((Component) originSimulationEvent.getSimulationElement().getModelInstance().getInstantiatedElement().getParent()).isTop()) {
                visibleElements.add(originSimulationEvent.getSimulationElement());
            }
        }
        return visibleElements;
    }

    protected int openDialog(Shell parentShell, Model model) {
        return Dialog.OK;
    }
}
