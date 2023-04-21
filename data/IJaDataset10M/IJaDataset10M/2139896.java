package de.fraunhofer.isst.axbench.api.simulation;

import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.Parameter;

public interface ISimulationParameterEvent extends ISimulationEvent {

    public Parameter getParameter();
}
