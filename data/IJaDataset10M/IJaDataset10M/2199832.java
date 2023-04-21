package de.ibis.permoto.solver.sim.tech.simEngine1.queueNet;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import de.ibis.permoto.solver.sim.tech.simEngine1.coreEngine.Simulation;
import de.ibis.permoto.solver.sim.tech.simEngine1.dataAnalysis.Measure;
import de.ibis.permoto.solver.sim.tech.simEngine1.dataAnalysis.simulationOutput.SimulationMeasureBean;

/**
 * A SimulationOutput object is used to print the results of all the measures.
 */
public abstract class SimulationOutput {

    protected Simulation sim;

    protected QueueNetwork network;

    protected Measure[] measureList;

    /**
	 * Creates a new instance of MeasureOutput class and obtains the references
	 * to all Measure object.
	 * @param simulation Reference to the simulation to be "outputted".
	 */
    public SimulationOutput(Simulation simulation) {
        this.sim = simulation;
        network = sim.getNetwork();
        LinkedList<Measure> list = network.getMeasures();
        measureList = new Measure[list.size()];
        for (int i = 0; i < measureList.length; i++) {
            measureList[i] = (Measure) list.get(i);
        }
    }

    /**
	 * Returns the Results that were written to some data storage by a Simulation.
	 * @return a List of serializable {@link SimulationMeasureBean} objects
	 */
    public abstract List<SimulationMeasureBean> getSavedResults();

    /**
	 * Writes the output of the measures.
	 * This method must be overridden.
	 * @return created output file
	 */
    public abstract File writeAllMeasures();

    /**
	 * Writes the measure results to an aim: derbyDB , Beans...
	 */
    public abstract void writeSimulationResults(Boolean whatIf);
}
