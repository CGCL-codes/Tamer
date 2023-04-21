package net.sf.orcc.simulators;

import static net.sf.orcc.OrccLaunchConstants.DEFAULT_FIFO_SIZE;
import static net.sf.orcc.OrccLaunchConstants.ENABLE_TRACES;
import static net.sf.orcc.OrccLaunchConstants.FIFO_SIZE;
import static net.sf.orcc.OrccLaunchConstants.INPUT_STIMULUS;
import static net.sf.orcc.OrccLaunchConstants.PROJECT;
import static net.sf.orcc.OrccLaunchConstants.TRACES_FOLDER;
import static net.sf.orcc.OrccLaunchConstants.XDF_FILE;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.dftools.graph.Vertex;
import net.sf.orcc.OrccException;
import net.sf.orcc.OrccRuntimeException;
import net.sf.orcc.df.Actor;
import net.sf.orcc.df.Connection;
import net.sf.orcc.df.Instance;
import net.sf.orcc.df.Network;
import net.sf.orcc.df.Port;
import net.sf.orcc.df.transformations.Instantiator;
import net.sf.orcc.df.transformations.NetworkFlattener;
import net.sf.orcc.ir.util.ActorInterpreter;
import net.sf.orcc.ir.util.IrUtil;
import net.sf.orcc.runtime.SimulatorFifo;
import net.sf.orcc.runtime.impl.GenericSource;
import net.sf.orcc.util.EcoreHelper;
import net.sf.orcc.util.OrccUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

/**
 * This class implements a simulator using a slow, visitor-based approach.
 * 
 * @author Matthieu Wipliez
 * @author Pierre-Laurent Lagalaye
 * 
 */
public class SlowSimulator extends AbstractSimulator {

    private boolean enableTraces;

    protected List<SimulatorFifo> fifoList;

    private int fifoSize;

    protected Map<Port, SimulatorFifo> inputFifos;

    protected Map<Instance, ActorInterpreter> interpreters;

    protected Map<Port, List<SimulatorFifo>> outputFifos;

    protected IProject project;

    private String stimulusFile;

    private String traceFolder;

    protected List<IFolder> vtlFolders;

    protected String xdfFile;

    protected void connectActors(Instance src, Port srcPort, Instance tgt, Port tgtPort, int fifoSize) {
        SimulatorFifo fifo = null;
        if (enableTraces) {
            String fifoName = src.getName() + "_" + srcPort.getName() + "_" + tgt.getName() + "_" + tgtPort.getName();
            fifo = new SimulatorFifo(srcPort.getType(), fifoSize, traceFolder, fifoName, enableTraces);
        } else {
            fifo = new SimulatorFifo(srcPort.getType(), fifoSize);
        }
        inputFifos.put(tgtPort, fifo);
        List<SimulatorFifo> fifos = outputFifos.get(srcPort);
        if (fifos == null) {
            fifos = new ArrayList<SimulatorFifo>();
            outputFifos.put(srcPort, fifos);
        }
        fifos.add(fifo);
    }

    /**
	 * Visit the network graph for building the required topology. Edges of the
	 * graph correspond to the connections between the actors. These connections
	 * should be implemented as FIFOs of specific size as defined in the CAL
	 * model or a common default size.
	 * 
	 * @param graph
	 */
    public void connectNetwork(Network network) {
        for (Connection connection : network.getConnections()) {
            Vertex srcVertex = connection.getSource();
            Vertex tgtVertex = connection.getTarget();
            if (srcVertex instanceof Instance && tgtVertex instanceof Instance) {
                Integer connectionSize = connection.getSize();
                int size = (connectionSize == null) ? fifoSize : connectionSize;
                Instance src = (Instance) srcVertex;
                Instance tgt = (Instance) tgtVertex;
                Port srcPort = connection.getSourcePort();
                Port tgtPort = connection.getTargetPort();
                if ((srcPort != null) && (tgtPort != null)) {
                    connectActors(src, srcPort, tgt, tgtPort, size);
                }
            }
        }
    }

    protected void initializeNetwork(Network network) {
        GenericSource.setInputStimulus(stimulusFile);
        for (Instance instance : network.getInstances()) {
            ActorInterpreter interpreter = interpreters.get(instance);
            interpreter.initialize();
        }
    }

    @Override
    protected void initializeOptions() {
        fifoSize = getAttribute(FIFO_SIZE, DEFAULT_FIFO_SIZE);
        stimulusFile = getAttribute(INPUT_STIMULUS, "");
        xdfFile = getAttribute(XDF_FILE, "");
        String name = getAttribute(PROJECT, "");
        enableTraces = getAttribute(ENABLE_TRACES, false);
        traceFolder = getAttribute(TRACES_FOLDER, "");
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        project = root.getProject(name);
        vtlFolders = OrccUtil.getOutputFolders(project);
    }

    /**
	 * Visit the network graph for instantiating the vertexes (actors we want to
	 * simulate). Created actor instances are stored in the simuActorsMap.
	 * 
	 * @param graph
	 * @throws OrccException
	 * @throws FileNotFoundException
	 */
    protected void instantiateNetwork(Network network) throws OrccException, FileNotFoundException {
        for (Instance instance : network.getInstances()) {
            Actor actor = instance.getActor();
            Actor clonedActor = IrUtil.copy(actor);
            instance.setEntity(clonedActor);
            actor.eResource().getContents().add(clonedActor);
            ConnectedActorInterpreter interpreter = new ConnectedActorInterpreter(clonedActor, instance.getArguments(), getWriteListener());
            interpreters.put(instance, interpreter);
            interpreter.setFifos(inputFifos, outputFifos);
        }
    }

    protected void runNetwork(Network network) {
        boolean isAlive = true;
        do {
            boolean hasExecuted = false;
            for (Instance instance : network.getInstances()) {
                int nbFiring = 0;
                ActorInterpreter interpreter = interpreters.get(instance);
                while (interpreter.schedule()) {
                    if (isCanceled()) {
                        return;
                    }
                    nbFiring++;
                }
                hasExecuted |= (nbFiring > 0);
                if (isCanceled()) {
                    return;
                }
            }
            isAlive = hasExecuted;
        } while (isAlive);
    }

    @Override
    public void start(String mode) {
        try {
            SimulatorDescriptor.killDescriptors();
            interpreters = new HashMap<Instance, ActorInterpreter>();
            inputFifos = new HashMap<Port, SimulatorFifo>();
            outputFifos = new HashMap<Port, List<SimulatorFifo>>();
            IFile file = OrccUtil.getFile(project, xdfFile, "xdf");
            ResourceSet set = new ResourceSetImpl();
            Network network = EcoreHelper.getEObject(set, file);
            network = new Instantiator().doSwitch(network);
            new NetworkFlattener().doSwitch(network);
            instantiateNetwork(network);
            updateConnections(network);
            connectNetwork(network);
            initializeNetwork(network);
            runNetwork(network);
            SimulatorDescriptor.killDescriptors();
        } catch (OrccException e) {
            throw new OrccRuntimeException(e.getMessage());
        } catch (FileNotFoundException e) {
            throw new OrccRuntimeException(e.getMessage());
        } finally {
            inputFifos = null;
            outputFifos = null;
            interpreters = null;
        }
    }

    protected void updateConnections(Network network) throws OrccException {
        for (Connection connection : network.getConnections()) {
            Vertex srcVertex = connection.getSource();
            Vertex tgtVertex = connection.getTarget();
            if (srcVertex instanceof Instance) {
                Instance source = (Instance) srcVertex;
                String srcPortName = connection.getSourcePort().getName();
                Port srcPort;
                if (source.isActor()) {
                    srcPort = source.getActor().getOutput(srcPortName);
                } else {
                    srcPort = source.getBroadcast().getOutput(srcPortName);
                }
                connection.setSourcePort(srcPort);
            }
            if (tgtVertex instanceof Instance) {
                Instance target = (Instance) tgtVertex;
                String dstPortName = connection.getTargetPort().getName();
                Port dstPort;
                if (target.isActor()) {
                    dstPort = target.getActor().getInput(dstPortName);
                } else {
                    dstPort = target.getBroadcast().getInput();
                }
                connection.setTargetPort(dstPort);
            }
        }
    }
}
