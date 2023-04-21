package de.fraunhofer.isst.axbench.operations.simulator;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IFileEditorInput;
import de.fraunhofer.isst.axbench.Session;
import de.fraunhofer.isst.axbench.api.simulation.IFeatureIon;
import de.fraunhofer.isst.axbench.api.simulation.ISelection;
import de.fraunhofer.isst.axbench.axlang.api.IAXLangElement;
import de.fraunhofer.isst.axbench.axlang.api.IGlobalInstance;
import de.fraunhofer.isst.axbench.axlang.elements.Model;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.ApplicationModel;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.Component;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.DataElement;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.Function;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.Operation;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.Parameter;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.Port;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.PortRWAccess;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.RWAccess;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.Storage;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.StorageRWAccess;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.SubComponent;
import de.fraunhofer.isst.axbench.axlang.elements.featuremodel.Feature;
import de.fraunhofer.isst.axbench.axlang.elements.featuremodel.FeatureModel;
import de.fraunhofer.isst.axbench.axlang.elements.globalinstances.AbstractConnectionInstance;
import de.fraunhofer.isst.axbench.axlang.elements.globalinstances.AbstractGlobalInstance;
import de.fraunhofer.isst.axbench.axlang.elements.globalinstances.ComponentInstance;
import de.fraunhofer.isst.axbench.axlang.elements.globalinstances.FunctionInstance;
import de.fraunhofer.isst.axbench.axlang.elements.globalinstances.PortInstance;
import de.fraunhofer.isst.axbench.axlang.elements.globalinstances.StorageInstance;
import de.fraunhofer.isst.axbench.axlang.elements.globalinstances.SubComponentInstance;
import de.fraunhofer.isst.axbench.axlang.elements.mappings.F2AMapping;
import de.fraunhofer.isst.axbench.axlang.elements.mappings.Feature2ArchitectureLink;
import de.fraunhofer.isst.axbench.axlang.utilities.GlobalInstanceUtilities;
import de.fraunhofer.isst.axbench.axlang.utilities.attributes.Synchronicity;
import de.fraunhofer.isst.axbench.operations.simulator.elements.SimulationClockPort;
import de.fraunhofer.isst.axbench.operations.simulator.elements.SimulationConnection;
import de.fraunhofer.isst.axbench.operations.simulator.elements.SimulationFunction;
import de.fraunhofer.isst.axbench.operations.simulator.elements.SimulationPort;
import de.fraunhofer.isst.axbench.operations.simulator.elements.SimulationStorage;
import de.fraunhofer.isst.axbench.operations.simulator.eventlistener.ISimulationEventListener;
import de.fraunhofer.isst.axbench.operations.simulator.valuestorage.Selection;

/**
 * @brief builds a simulation model of an aXLang-model
 * 
 * @author mgrosse
 * @author ekleinod
 * @version 0.9.0
 * @since 0.7.2
 */
public class SimulationModelBuilder {

    private ApplicationModel theApplicationModel;

    private static F2AMapping theF2AMapping;

    private Collection<FunctionInstance> clFunctionInstances = null;

    private Collection<PortInstance> clPortInstances = null;

    private Collection<AbstractConnectionInstance> clConnectionInstances = null;

    private Collection<StorageInstance> clStorageInstances = null;

    private Collection<SimulationFunction> clSimulationFunctions;

    private Collection<SimulationFunction> clInitialFunctions;

    private Map<String, SimulationPort> simulationPortMap;

    private Collection<SimulationClockPort> clClockPorts;

    private Map<String, SimulationStorage> simulationStorageMap;

    private Collection<ISimulationEventListener> clSimulationEventListeners;

    private File rootDirectory;

    /**
	 * @brief Constructor setting the operation parameters.
	 * 
	 * Operation parameters:
	 * - one aXLang element
	 * - number of simulation steps
	 */
    public SimulationModelBuilder(Model model, ApplicationModel applicationModel, F2AMapping f2aMapping) {
        super();
        theApplicationModel = applicationModel;
        theF2AMapping = f2aMapping;
        clSimulationEventListeners = new HashSet<ISimulationEventListener>();
    }

    /**
	 * @brief builds the simulation model elements
	 * @param model the aXLang-model for which the simulation model is built
	 * @param axlInstanceBuilder the instance builder that has been used to build the aXLang-model instances
	 */
    public void buildSimulationModel() {
        GlobalInstanceUtilities.buildAllApplicationModelInstances(theApplicationModel);
        clFunctionInstances = GlobalInstanceUtilities.getFunctionInstances(theApplicationModel.getModel());
        clPortInstances = GlobalInstanceUtilities.getPortInstances(theApplicationModel.getModel());
        clConnectionInstances = GlobalInstanceUtilities.getAbstractConnectionInstances(theApplicationModel.getModel());
        clStorageInstances = GlobalInstanceUtilities.getStorageInstances(theApplicationModel.getModel());
        buildUpSimulationStorages();
        buildUpSimulationPorts();
        initializeSimulationPorts();
        clSimulationFunctions = buildUpSimulationFunctions();
        buildUpSimulationConnections();
    }

    /**
	 * @brief adds a simulation event listener
	 * @param eventListener the simulation event listener to be added
	 */
    public void addSimulationEventListener(ISimulationEventListener eventListener) {
        clSimulationEventListeners.add(eventListener);
    }

    /**
	 * @brief getter method for the collection of simulation functions
	 * @return the collection of simulation functions
	 */
    public Collection<SimulationFunction> getSimulationFunctions() {
        return clSimulationFunctions;
    }

    /**
	 * @brief getter method for the collection of initial simulation functions
	 * @return the collection of initial simulation functions
	 */
    public Collection<SimulationFunction> getInitialFunctions() {
        return clInitialFunctions;
    }

    /**
	 * @brief getter method for the collection of clock ports
	 * @return the collection of clock ports
	 */
    public Collection<SimulationClockPort> getClockPorts() {
        return clClockPorts;
    }

    /**
	 * @brief getter method for the simulationPortMap
	 */
    public Map<String, SimulationPort> getSimulationPortMap() {
        return simulationPortMap;
    }

    /**
	 * @brief getter method for the simulation ports
	 * @return the collection of simulation ports
	 */
    public Collection<SimulationPort> getSimulationPorts() {
        return simulationPortMap.values();
    }

    /**
	 * @brief getter method for the simulationVariableMap
	 */
    public Map<String, SimulationStorage> getSimulationStorageMap() {
        return simulationStorageMap;
    }

    /**
	 * @brief getter method for the simulation variables
	 * @return
	 */
    public Collection<SimulationStorage> getSimulationStorages() {
        return simulationStorageMap.values();
    }

    /**
	 * @brief builds the simulation ports
	 */
    private void buildUpSimulationPorts() {
        simulationPortMap = new HashMap<String, SimulationPort>();
        clClockPorts = new HashSet<SimulationClockPort>();
        for (PortInstance portInstance : clPortInstances) {
            Port port = portInstance.getPort();
            Collection<ISelection> selections = new HashSet<ISelection>();
            Collection<ISelection> exclusions = new HashSet<ISelection>();
            if (portInstance.getParentInstance().getParentInstance() != null && portInstance.getParentInstance().getParentInstance().getParentInstance() != null) {
                ComponentInstance xorComponent = (ComponentInstance) portInstance.getParentInstance().getParentInstance().getParentInstance();
                if (xorComponent.getComponent().isXOR()) {
                    SubComponentInstance selectedAlternativeInstance = (SubComponentInstance) portInstance.getParentInstance().getParentInstance();
                    selections.add(new Selection(selectedAlternativeInstance));
                    for (SubComponent otherAlternative : xorComponent.getComponent().getSubComponents()) {
                        if (!otherAlternative.equals(selectedAlternativeInstance.getSubComponent())) {
                            SubComponentInstance excludedAlternativeInstance = (SubComponentInstance) AbstractGlobalInstance.fromParentInstanceAndElement(xorComponent, otherAlternative);
                            exclusions.add(new Selection(excludedAlternativeInstance));
                        }
                    }
                }
            }
            if (portInstance.getParentInstance().getParentInstance() instanceof SubComponentInstance) {
                SubComponentInstance subComponentInstance = (SubComponentInstance) portInstance.getParentInstance().getParentInstance();
                if (subComponentInstance.getSubComponent().isOptional()) {
                    selections.add(new Selection(subComponentInstance));
                    exclusions.add(new Selection(subComponentInstance, false));
                }
            }
            if (portInstance.getPort().isOptional()) {
                selections.add(new Selection(portInstance));
                exclusions.add(new Selection(portInstance, false));
            }
            boolean considersFeatures = false;
            if (theF2AMapping != null) {
                considersFeatures = true;
            }
            SimulationPort simulationPort = null;
            if (((Component) port.getParent()).isClockElement()) {
                simulationPort = new SimulationClockPort(portInstance);
                clClockPorts.add((SimulationClockPort) simulationPort);
            } else {
                simulationPort = new SimulationPort(portInstance, considersFeatures);
                simulationPort.addElementSelections(selections);
                simulationPort.addElementExclusions(exclusions);
            }
            for (ISimulationEventListener simulationEventListener : clSimulationEventListeners) {
                simulationPort.addSimulationEventListener(simulationEventListener);
            }
            simulationPortMap.put(simulationPort.getIdentifier(), simulationPort);
        }
    }

    /**
	 * @brief builds the simulation variables
	 * @todo check if fall-through at "STRING" is correct 
	 */
    private void buildUpSimulationStorages() {
        simulationStorageMap = new HashMap<String, SimulationStorage>();
        for (StorageInstance storageInstance : clStorageInstances) {
            SimulationStorage simulationVariable = new SimulationStorage(storageInstance);
            for (ISimulationEventListener simulationEventListener : clSimulationEventListeners) {
                simulationVariable.addSimulationEventListener(simulationEventListener);
            }
            simulationStorageMap.put(simulationVariable.getIdentifier(), simulationVariable);
        }
    }

    /**
	 * @brief builds the simulation functions
	 */
    private Collection<SimulationFunction> buildUpSimulationFunctions() {
        Collection<SimulationFunction> simulationFunctions = new HashSet<SimulationFunction>();
        for (FunctionInstance functionInstance : clFunctionInstances) {
            if ((functionInstance.getFunction()).getFunctionParts().isEmpty()) {
                boolean isUsingFeatures = false;
                if (theF2AMapping != null) {
                    isUsingFeatures = true;
                }
                SimulationFunction simulationFunction = new SimulationFunction(functionInstance, this, isUsingFeatures);
                for (ISimulationEventListener simulationEventListener : clSimulationEventListeners) {
                    simulationFunction.addSimulationEventListener(simulationEventListener);
                }
                simulationFunctions.add(simulationFunction);
                for (SimulationPort simulationPort : simulationPortMap.values()) {
                    ComponentInstance portParentInstance = (ComponentInstance) simulationPort.getModelInstance().getParentInstance();
                    ComponentInstance functionParentInstance = (ComponentInstance) functionInstance.getParentInstance();
                    if (portParentInstance.equals(functionParentInstance)) {
                        Port axlPort = simulationPort.getModelInstance().getPort();
                        Function axlFunction = functionInstance.getFunction();
                        for (PortRWAccess axlTrigger : axlFunction.getTriggers()) {
                            if (axlTrigger.getPort().equals(axlPort)) {
                                simulationPort.addTriggeredFunction(axlTrigger.getDataElement(), simulationFunction);
                                simulationFunction.addReadPortDataElement(simulationPort, axlTrigger.getDataElement());
                            }
                        }
                        for (RWAccess axlRWAccess : axlFunction.getRWAccesses()) {
                            if (axlRWAccess instanceof PortRWAccess && ((PortRWAccess) axlRWAccess).getPort().equals(axlPort)) {
                                PortRWAccess portRWAccess = (PortRWAccess) axlRWAccess;
                                if (axlRWAccess.isRead()) {
                                    simulationFunction.addReadPortDataElement(simulationPort, portRWAccess.getDataElement());
                                }
                                if (axlRWAccess.isWrite() && portRWAccess.getDataElement() instanceof Operation) {
                                    simulationPort.addOperationCallingFunction((Operation) portRWAccess.getDataElement(), simulationFunction);
                                }
                            }
                        }
                    }
                }
                for (SimulationStorage simulationStorage : simulationStorageMap.values()) {
                    ComponentInstance variableParentInstance = (ComponentInstance) simulationStorage.getModelInstance().getParentInstance();
                    ComponentInstance functionParentInstance = (ComponentInstance) functionInstance.getParentInstance();
                    if (variableParentInstance.equals(functionParentInstance)) {
                        Storage axlStorage = simulationStorage.getModelInstance().getStorage();
                        Function axlFunction = simulationFunction.getModelInstance().getFunction();
                        for (RWAccess axlRWAccess : axlFunction.getRWAccesses()) {
                            if (axlRWAccess instanceof StorageRWAccess && axlRWAccess.isRead() && ((StorageRWAccess) axlRWAccess).getStorage().equals(axlStorage)) {
                                simulationFunction.addReadVariable(simulationStorage);
                            }
                        }
                    }
                }
                IFile input = ((IFileEditorInput) Session.getCurrentmultipage().getEditorInput()).getFile();
                String rootDirectoryPath = input.getProject().getRawLocation().toPortableString() + System.getProperty("file.separator") + "tmp";
                rootDirectory = new File(rootDirectoryPath);
                rootDirectory.mkdir();
                FunctionScriptGenerator.createFunctionScriptFile(rootDirectoryPath, simulationFunction);
                simulationFunction.initializeScriptEngine(rootDirectoryPath);
            }
        }
        return simulationFunctions;
    }

    /**
	 * @brief builds the simulation connections
	 */
    private void buildUpSimulationConnections() {
        for (AbstractConnectionInstance connectionInstance : clConnectionInstances) {
            SimulationConnection simulationConnection = new SimulationConnection(connectionInstance);
            for (ISimulationEventListener simulationEventListener : clSimulationEventListeners) {
                simulationConnection.addSimulationEventListener(simulationEventListener);
            }
            for (SimulationPort simulationPort : simulationPortMap.values()) {
                if (connectionInstance.getSourcePortInstance().equals(simulationPort.getModelInstance())) {
                    simulationConnection.setSourcePort(simulationPort);
                    simulationPort.addOutConnection(simulationConnection);
                }
                if (connectionInstance.getTargetPortInstance().equals(simulationPort.getModelInstance())) {
                    simulationConnection.setTargetPort(simulationPort);
                    simulationPort.addInConnection(simulationConnection);
                }
            }
        }
    }

    /**
	 * @brief defines the synchronicity values of the data elements declared at the ports, 
	 * ie. defines whether they are synchronous or asynchronous,
	 * and defines the parameter2index-maps of the operations
	 */
    private void initializeSimulationPorts() {
        for (SimulationPort simPort : simulationPortMap.values()) {
            Port axlPort = simPort.getModelInstance().getPort();
            Collection<DataElement> axlPortsDataElements = axlPort.getDataElements();
            for (DataElement axlDataElement : axlPortsDataElements) {
                boolean bSynchronicity = axlDataElement.getSynchronicity() == Synchronicity.SYNCHRONOUS;
                simPort.setSynchronous(axlDataElement, bSynchronicity);
                if (axlDataElement instanceof Operation) {
                    Operation axlOperation = (Operation) axlDataElement;
                    Map<IAXLangElement, Integer> parameterIndexMap = new HashMap<IAXLangElement, Integer>();
                    Integer index = 0;
                    for (Parameter parameter : axlOperation.getParameters()) {
                        parameterIndexMap.put(parameter, index);
                        index++;
                    }
                    simPort.setParameterIndexMap(axlOperation, parameterIndexMap);
                }
            }
        }
    }

    /**
	 * @brief returns the feature that is mapped to the given global instance; null if no such feature exists
	 * @param globalInstance the global instance the mapping feature is sought for
	 * @return the feature that is mapped to the given global instance; null if no such feature exists
	 */
    public static Feature getMappingFeature(IGlobalInstance globalInstance) {
        Collection<Feature> mappingFeatures = new HashSet<Feature>();
        for (Feature2ArchitectureLink f2aLink : theF2AMapping.getF2ALinks()) {
            if (f2aLink.getTargetInstances().contains(globalInstance)) {
                mappingFeatures.add(f2aLink.getFeature());
            }
        }
        if (mappingFeatures.size() >= 1) {
            Feature[] coveringFeatureArray = mappingFeatures.toArray(new Feature[0]);
            return coveringFeatureArray[0];
        } else {
            return null;
        }
    }

    /**
	 * @brief checks whether the new selection is consistent with the old features
	 * 1. if the selection is positive it must not be associated to a feature that is excluded by some old feature
	 * 2. if the selection is negative, no old feature must point to it; 
	 * 		and if an alternative feature points to it, there must be another alternative among the old features
	 * 
	 * @param newSelection
	 * @param oldFeatures
	 * @return true if the new selection is consistent with the old features, else false
	 */
    public static boolean isConsistentFeatureExtension(ISelection newSelection, Collection<IFeatureIon> oldFeatureIons) {
        boolean isConsistent = true;
        Feature newFeature = getMappingFeature(newSelection.getSelectedInstance());
        Collection<Feature> positiveOldFeatures = new HashSet<Feature>();
        Collection<Feature> negativeOldFeatures = new HashSet<Feature>();
        for (IFeatureIon oldFeatureIon : oldFeatureIons) {
            if (oldFeatureIon.isPositive()) {
                positiveOldFeatures.add(oldFeatureIon.getFeature());
            } else {
                negativeOldFeatures.add(oldFeatureIon.getFeature());
            }
        }
        if (newSelection.isPositive()) {
            if (isInConflictWith(newFeature, positiveOldFeatures) || negativeOldFeatures.contains(newFeature)) {
                isConsistent = false;
            }
        }
        if (!newSelection.isPositive()) {
            if (positiveOldFeatures.contains(newFeature)) {
                isConsistent = false;
            }
        }
        return isConsistent;
    }

    /**
	 * @brief indicates whether the new feature contradicts the old features
	 * 
	 * The only possible conflict (yet) is that the new feature belongs to a branch below an xor-feature 'xor-f'
	 * and at least one of the old features belongs to a branch below another alternative of 'xor-f' than the new feature.
	 * (That means: The new feature and one old feature have a common ancestor that is an xor-feature.)
	 * 
	 * @param newFeature the new feature
	 * @param oldFeatures the old features
	 * @return true if the new feature contradicts the old ones
	 */
    private static boolean isInConflictWith(Feature newFeature, Collection<Feature> oldFeatures) {
        boolean isInConflict = false;
        Collection<Feature> oldXorAncestors = new HashSet<Feature>();
        for (Feature oldFeature : oldFeatures) {
            oldXorAncestors.addAll(getXorAncestors(oldFeature));
        }
        if (!oldFeatures.contains(newFeature)) {
            for (Feature newXorAncestor : getXorAncestors(newFeature)) {
                if (oldXorAncestors.contains(newXorAncestor)) {
                    isInConflict = true;
                }
            }
        }
        return isInConflict;
    }

    /**
	 * @brief returns all xor-features among the ancestors of the given feature
	 * @param feature the feature whose xor-ancestors are sought
	 * @return all xor-features among the ancestors of the given feature
	 */
    private static Collection<Feature> getXorAncestors(Feature feature) {
        Collection<Feature> xorAncestors = new HashSet<Feature>();
        if (feature.getParent() instanceof FeatureModel) {
            return xorAncestors;
        } else {
            Feature parentFeature = (Feature) feature.getParent();
            xorAncestors.addAll(getXorAncestors(parentFeature));
            if (parentFeature.isXOR()) {
                xorAncestors.add(parentFeature);
            }
        }
        return xorAncestors;
    }

    /**
	 * @brief removes the function scripts that have been created together with the simulation functions
	 * @return true if the removal was successful, else false
	 */
    public boolean removeFunctionScripts() {
        return deleteDirectory(rootDirectory);
    }

    /**
	 * @brief auxiliary method needed for the recursive deletion of the directory that contains the function scripts
	 * @param directory the current directory
	 * @return true if the deletion was successful, else false
	 */
    private boolean deleteDirectory(File directory) {
        if (directory.isDirectory()) {
            String[] children = directory.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDirectory(new File(directory, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return directory.delete();
    }
}
