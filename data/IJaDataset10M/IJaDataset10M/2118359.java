package de.fraunhofer.isst.axbench.operations.transformer;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import de.fraunhofer.isst.axbench.api.AXLMessage.MessageType;
import de.fraunhofer.isst.axbench.api.operations.AbstractAXLOperation;
import de.fraunhofer.isst.axbench.api.operations.OperationInterface;
import de.fraunhofer.isst.axbench.api.operations.OperationParameter;
import de.fraunhofer.isst.axbench.api.operations.ParameterTypes;
import de.fraunhofer.isst.axbench.axlang.api.IAXLangElement;
import de.fraunhofer.isst.axbench.axlang.elements.Model;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.ApplicationModel;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.Component;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.DataElement;
import de.fraunhofer.isst.axbench.axlang.elements.architecturemodel.SubComponent;
import de.fraunhofer.isst.axbench.axlang.elements.featuremodel.FeatureModel;
import de.fraunhofer.isst.axbench.axlang.elements.mappings.AbstractF2ArchitectureMapping;
import de.fraunhofer.isst.axbench.axlang.utilities.AXLException;
import de.fraunhofer.isst.axbench.axlang.utilities.Role;
import de.fraunhofer.isst.axbench.axlang.utilities.attributes.Attributes;
import de.fraunhofer.isst.axbench.operations.transformer.utilities.ReferenceReconstructor;

/**
 * 
 * @error code commented out because of compile error
 * @see #computeNormalizedConfiguration(Component)
 * 
 * @author mgrosse
 * @author ekleinod
 * @version 0.9.0
 * @since 0.8.0
 */
public class ComponentHierarchyFolder extends AbstractAXLOperation {

    private static final String ID_IN_ELEMENT_TOFOLD = "axltofold";

    private static final String ID_IN_ELEMENT_COMPARE = "axlcompare";

    private static final String ID_OUT_ELEMENT = "axlfolded";

    /**
	 * @brief Constructor.
	 * 
	 * Element input parameters:
	 * - one aXLang model to fold
	 * - one aXLang model for comparison compare
	 * Element output parameters:
	 * - folded aXLang model
	 * 
	 */
    public ComponentHierarchyFolder() {
        super();
        OperationInterface ifInput = new OperationInterface();
        OperationParameter prmElement = new OperationParameter(ParameterTypes.ELEMENT, ID_IN_ELEMENT_TOFOLD, "aXLang model to fold");
        prmElement.setElementType(Model.class);
        ifInput.addParameter(prmElement);
        prmElement = new OperationParameter(ParameterTypes.ELEMENT, ID_IN_ELEMENT_COMPARE, "compare model", "aXLang model to compare the fold model to");
        prmElement.setElementType(Model.class);
        ifInput.addParameter(prmElement);
        setOperationInterface(ifInput, ParameterDirection.INPUT);
        OperationInterface ifOutput = new OperationInterface();
        prmElement = new OperationParameter(ParameterTypes.ELEMENT, ID_OUT_ELEMENT, "folded aXLang model");
        prmElement.setElementType(Model.class);
        ifOutput.addParameter(prmElement);
        setOperationInterface(ifOutput, ParameterDirection.OUTPUT);
    }

    /**
	 * @brief Folds the fold model according to the comparison model.
	 * 
	 * @todo prüfen, ob 
	 * IAXLangElement normalizedModel = constructNormalComponents(theSource, theThing);
	 * reduceToNormalComponentInstances(theThing);
	 * richtig aufgerufen werden (mit theThing statt theSource)
	 * 
	 * -# construct the normal components
	 * -# define the component types of the subcomponents in the folded model
	 * -# reconstruct the references in the folded model that cross component borders
	 * 
	 * @param theInputParameters map of the input parameters (empty map or null allowed)
	 * @return map of the output parameters (empty map allowed)
	 */
    @Override
    protected Map<String, Object> executeConcreteOperation(Map<String, Object> theInputParameters) {
        Map<String, Object> mapReturn = super.executeConcreteOperation(theInputParameters);
        Model theSource = (Model) theInputParameters.get(ID_IN_ELEMENT_COMPARE);
        Model theUnfoldedSource = (Model) theInputParameters.get(ID_IN_ELEMENT_TOFOLD);
        addAXLMessage(MessageFormat.format("Folding model ''{0}'' according to model ''{1}''.", theUnfoldedSource.getIdentifier(), theSource.getIdentifier()), MessageType.MESSAGE);
        Model foldedModel = constructNormalComponents(theSource, theUnfoldedSource);
        if (foldedModel != null) {
            reduceToNormalComponentInstances(theUnfoldedSource);
            ReferenceReconstructor.reconstructReferences(foldedModel);
            mapReturn.put(ID_OUT_ELEMENT, foldedModel);
        }
        return mapReturn;
    }

    private Map<Component, ComponentConfiguration> instanceConfigurationMap = new HashMap<Component, ComponentConfiguration>();

    private Map<Component, Set<ComponentConfiguration>> originConfigurationSetMap = new HashMap<Component, Set<ComponentConfiguration>>();

    private Map<ComponentConfiguration, Component> representativesMap = new HashMap<ComponentConfiguration, Component>();

    private Map<Component, Component> normalComponentMap = new HashMap<Component, Component>();

    /**
	 * @brief defines for each componentInstance in the expanded and configured 
	 * applicationModel a normalComponent
	 * 
	 * constructNormalComponents() takes the "expandedModel" as input and 
	 * constructs the "normalizedModel" as output.
	 * 
	 * The expandedModel contains a new instance of a component of the originModel
	 * for each path that leads from the top component to the originComponent.
	 * constructNormalComponents() constructs new components as normal forms for
	 * the component instances, where two component instances have the same 
	 * normalComponent if the are configured in the same way.
	 * 
	 * The result of the method is the normalizedModel that contains as components
	 * the normalComponents that replace the component instances defined in the 
	 * previous step.
	 */
    private Model constructNormalComponents(Model originModel, Model expandedModel) {
        Model normalizedModel = null;
        instanceConfigurationMap = new HashMap<Component, ComponentConfiguration>();
        originConfigurationSetMap = new HashMap<Component, Set<ComponentConfiguration>>();
        representativesMap = new HashMap<ComponentConfiguration, Component>();
        IAXLangElement originApplicationModel = originModel.getChild(Role.APPLICATIONMODEL);
        IAXLangElement expandedApplicationModel = expandedModel.getChild(Role.APPLICATIONMODEL);
        try {
            normalizedModel = new Model();
            normalizedModel.setIdentifier(originModel.getIdentifier() + "_configured");
            IAXLangElement normalizedFeatureModel = ((FeatureModel) expandedModel.getChild(Role.FEATUREMODEL)).clone();
            normalizedModel.addChild(normalizedFeatureModel, Role.FEATUREMODEL);
            for (IAXLangElement configuration : expandedModel.getChildren(Role.CONFIGURATION)) {
                IAXLangElement normalizedConfiguration = configuration.clone();
                normalizedModel.addChild(normalizedConfiguration, Role.CONFIGURATION);
            }
            ApplicationModel normalizedApplicationModel = new ApplicationModel();
            normalizedApplicationModel.setIdentifier(originApplicationModel.getIdentifier());
            normalizedModel.addChild(normalizedApplicationModel, Role.APPLICATIONMODEL);
            Map<Role, Collection<IAXLangElement>> mapChildren = expandedApplicationModel.getChildren();
            for (Role theRole : mapChildren.keySet()) {
                for (IAXLangElement element : mapChildren.get(theRole)) {
                    if (element instanceof DataElement) {
                        normalizedApplicationModel.addChild(element.clone(), theRole);
                    }
                }
            }
            IAXLangElement normalizedF2AMapping = ((AbstractF2ArchitectureMapping) expandedModel.getChild(Role.F2AMAPPING)).clone();
            normalizedModel.addChild(normalizedF2AMapping, Role.F2AMAPPING);
            Set<Component> normalizedComponentSet = new HashSet<Component>();
            for (IAXLangElement originComponent : originApplicationModel.getChildren(Role.COMPONENT)) {
                if (originComponent.getChildren(Role.SUBCOMPONENT).isEmpty()) {
                    computeRepresentatives(originComponent, expandedModel, normalizedModel);
                    normalizedComponentSet.add((Component) originComponent);
                }
            }
            do {
                for (IAXLangElement originComp : originApplicationModel.getChildren(Role.COMPONENT)) {
                    Component originComponent = (Component) originComp;
                    if (!normalizedComponentSet.contains(originComponent) && hasNormalSubComponents(originComponent, expandedModel)) {
                        computeRepresentatives(originComponent, expandedModel, normalizedModel);
                        normalizedComponentSet.add(originComponent);
                    }
                }
            } while (normalizedComponentSet.size() < originApplicationModel.getChildren(Role.COMPONENT).size());
        } catch (AXLException e) {
            addAXLMessage(e.getMessage(), MessageType.ERROR);
        }
        System.out.println("normal components " + normalComponentMap.keySet().size());
        for (IAXLangElement expandedComponent : normalComponentMap.keySet()) {
            System.out.println();
            System.out.println("expanded " + expandedComponent.getIdentifier());
            System.out.println("normalized " + normalComponentMap.get(expandedComponent).getIdentifier());
        }
        return normalizedModel;
    }

    private void computeRepresentatives(IAXLangElement originComponent, IAXLangElement expandedModel, IAXLangElement normalizedModel) {
        originConfigurationSetMap.put((Component) originComponent, new HashSet<ComponentConfiguration>());
        System.out.println("originComponents " + getComponentInstances(originComponent, expandedModel).size());
        for (Component componentInstance : getComponentInstances(originComponent, expandedModel)) {
            instanceConfigurationMap.put(componentInstance, computeNormalizedConfiguration(componentInstance));
            originConfigurationSetMap.get(originComponent).add(instanceConfigurationMap.get(componentInstance));
        }
        int iNameCount = 0;
        System.out.println("originConfigurationSets " + originConfigurationSetMap.get(originComponent).size());
        for (ComponentConfiguration config : originConfigurationSetMap.get(originComponent)) {
            Component representative = null;
            boolean isRepresented = false;
            for (Component instance : getComponentInstances(originComponent, expandedModel)) {
                if (instanceConfigurationMap.get(instance).equals(config)) {
                    if (!isRepresented) {
                        isRepresented = true;
                        representative = (Component) instance.clone();
                        if (originConfigurationSetMap.get(originComponent).size() == 1) {
                            representative.setIdentifier(originComponent.getIdentifier());
                        } else {
                            representative.setIdentifier(originComponent.getIdentifier() + "_" + iNameCount);
                        }
                        iNameCount++;
                        representativesMap.put(config, representative);
                        try {
                            normalizedModel.getChild(Role.APPLICATIONMODEL).addChild(representative, Role.COMPONENT);
                        } catch (AXLException e) {
                        }
                        instanceConfigurationMap.put(representative, config);
                    }
                    normalComponentMap.put(instance, representative);
                }
            }
        }
    }

    private boolean hasNormalSubComponents(IAXLangElement originComponent, IAXLangElement expandedModel) {
        boolean allNormal = true;
        Collection<Component> componentInstances = getComponentInstances(originComponent, expandedModel);
        for (Component componentInstance : componentInstances) {
            for (IAXLangElement osc : originComponent.getChildren(Role.SUBCOMPONENT)) {
                SubComponent originSubComponent = (SubComponent) osc;
                if (componentInstance.getChild(originSubComponent.getIdentifier()) != null) {
                    Component type = (Component) componentInstance.getChild(originSubComponent.getIdentifier()).getReference(Role.COMPONENTTYPE);
                    if (!normalComponentMap.containsKey(type)) {
                        allNormal = false;
                    }
                }
            }
        }
        return allNormal;
    }

    private Collection<Component> getComponentInstances(IAXLangElement originComponent, IAXLangElement expandedModel) {
        List<Component> componentInstances = new ArrayList<Component>();
        System.out.println();
        System.out.println("originComponent " + originComponent.getIdentifier());
        for (IAXLangElement component : expandedModel.getChild(Role.APPLICATIONMODEL).getChildren(Role.COMPONENT)) {
            System.out.println("compared to " + component.getIdentifier());
            System.out.println("with origin " + ((Component) component).getOrigin().getIdentifier());
            if (((Component) component).getOrigin().equals(originComponent)) {
                System.out.println("instance " + component.getIdentifier());
                componentInstances.add((Component) component);
            }
        }
        return componentInstances;
    }

    private ComponentConfiguration computeNormalizedConfiguration(Component component) {
        ComponentConfiguration config = new ComponentConfiguration();
        for (IAXLangElement subcomponent : component.getChildren(Role.SUBCOMPONENT)) {
            config.addSubComponentEntry(subcomponent.getIdentifier(), normalComponentMap.get(subcomponent.getReference(Role.COMPONENTTYPE)));
        }
        for (IAXLangElement variable : component.getChildren(Role.STORAGE)) {
            config.addVariable(variable.getIdentifier(), variable.getAttributeValue(Attributes.DATATYPE.getID()));
        }
        return config;
    }

    private void reduceToNormalComponentInstances(IAXLangElement expandedModel) {
        for (IAXLangElement instance : expandedModel.getChild(Role.APPLICATIONMODEL).getChildren(Role.COMPONENT)) {
            Component representative = normalComponentMap.get(instance);
            for (IAXLangElement sC : instance.getChildren(Role.SUBCOMPONENT)) {
                SubComponent instanceSubComponent = (SubComponent) sC;
                Component oldType = (Component) instanceSubComponent.getReference(Role.COMPONENTTYPE);
                Component newType = normalComponentMap.get(oldType);
                SubComponent normalSubComponent = (SubComponent) representative.getChild(instanceSubComponent.getIdentifier());
                normalSubComponent.removeReference(normalSubComponent.getReference(Role.COMPONENTTYPE), Role.COMPONENTTYPE);
                try {
                    normalSubComponent.addReference(newType, Role.COMPONENTTYPE);
                } catch (AXLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
