package org.ietr.preesm.mapper;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import net.sf.dftools.algorithm.model.parameters.InvalidExpressionException;
import net.sf.dftools.algorithm.model.sdf.SDFGraph;
import net.sf.dftools.architecture.slam.Design;
import net.sf.dftools.workflow.WorkflowException;
import net.sf.dftools.workflow.elements.Workflow;
import net.sf.dftools.workflow.tools.WorkflowLogger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.ietr.preesm.core.scenario.PreesmScenario;
import org.ietr.preesm.mapper.abc.AbstractAbc;
import org.ietr.preesm.mapper.abc.IAbc;
import org.ietr.preesm.mapper.abc.impl.latency.InfiniteHomogeneousAbc;
import org.ietr.preesm.mapper.abc.taskscheduling.AbstractTaskSched;
import org.ietr.preesm.mapper.abc.taskscheduling.TopologicalTaskSched;
import org.ietr.preesm.mapper.algo.list.InitialLists;
import org.ietr.preesm.mapper.algo.list.KwokListScheduler;
import org.ietr.preesm.mapper.graphtransfo.SdfToDagConverter;
import org.ietr.preesm.mapper.graphtransfo.TagDAG;
import org.ietr.preesm.mapper.model.MapperDAG;
import org.ietr.preesm.mapper.params.AbcParameters;

/**
 * Plug-in class for list scheduling
 * 
 * @author pmenuet
 * @author mpelcat
 */
public class ListSchedulingTransformation extends AbstractMapping {

    /**
	 * 
	 */
    public ListSchedulingTransformation() {
    }

    @Override
    public Map<String, String> getDefaultParameters() {
        Map<String, String> parameters = super.getDefaultParameters();
        return parameters;
    }

    @Override
    public Map<String, Object> execute(Map<String, Object> inputs, Map<String, String> parameters, IProgressMonitor monitor, String nodeName, Workflow workflow) throws WorkflowException {
        Map<String, Object> outputs = new HashMap<String, Object>();
        Design architecture = (Design) inputs.get("architecture");
        SDFGraph algorithm = (SDFGraph) inputs.get("SDF");
        PreesmScenario scenario = (PreesmScenario) inputs.get("scenario");
        super.execute(inputs, parameters, monitor, nodeName, workflow);
        AbcParameters abcParameters = new AbcParameters(parameters);
        MapperDAG dag = SdfToDagConverter.convert(algorithm, architecture, scenario, false);
        calculateSpan(dag, architecture, scenario, abcParameters);
        IAbc simu = new InfiniteHomogeneousAbc(abcParameters, dag, architecture, abcParameters.getSimulatorType().getTaskSchedType(), scenario);
        InitialLists initial = new InitialLists();
        if (!initial.constructInitialLists(dag, simu)) {
            WorkflowLogger.getLogger().log(Level.SEVERE, "Error in scheduling");
            return null;
        }
        WorkflowLogger.getLogger().log(Level.INFO, "Mapping");
        AbstractTaskSched taskSched = new TopologicalTaskSched(simu.getTotalOrder());
        simu.resetDAG();
        IAbc simu2 = AbstractAbc.getInstance(abcParameters, dag, architecture, scenario);
        simu2.setTaskScheduler(taskSched);
        KwokListScheduler scheduler = new KwokListScheduler();
        scheduler.schedule(dag, initial.getCpnDominant(), simu2, null, null);
        WorkflowLogger.getLogger().log(Level.INFO, "Mapping finished");
        TagDAG tagSDF = new TagDAG();
        try {
            tagSDF.tag(dag, architecture, scenario, simu2, abcParameters.getEdgeSchedType());
        } catch (InvalidExpressionException e) {
            e.printStackTrace();
            throw (new WorkflowException(e.getMessage()));
        }
        outputs.put("DAG", dag);
        outputs.put("ABC", simu2);
        super.clean(architecture, scenario);
        return outputs;
    }
}
