package org.uimafit.factory;

import static java.util.Arrays.asList;
import static org.uimafit.descriptor.OperationalProperties.MODIFIES_CAS_DEFAULT;
import static org.uimafit.descriptor.OperationalProperties.MULTIPLE_DEPLOYMENT_ALLOWED_DEFAULT;
import static org.uimafit.descriptor.OperationalProperties.OUTPUTS_NEW_CASES_DEFAULT;
import static org.uimafit.factory.ConfigurationParameterFactory.createConfigurationData;
import static org.uimafit.factory.ConfigurationParameterFactory.ensureParametersComeInPairs;
import static org.uimafit.factory.ExternalResourceFactory.bindExternalResource;
import static org.uimafit.factory.TypeSystemDescriptionFactory.createTypeSystemDescription;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.uima.Constants;
import org.apache.uima.UIMAException;
import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_component.AnalysisComponent;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.impl.AggregateAnalysisEngine_impl;
import org.apache.uima.analysis_engine.impl.AnalysisEngineDescription_impl;
import org.apache.uima.analysis_engine.metadata.AnalysisEngineMetaData;
import org.apache.uima.analysis_engine.metadata.FixedFlow;
import org.apache.uima.analysis_engine.metadata.FlowControllerDeclaration;
import org.apache.uima.analysis_engine.metadata.SofaMapping;
import org.apache.uima.analysis_engine.metadata.impl.FixedFlow_impl;
import org.apache.uima.analysis_engine.metadata.impl.FlowControllerDeclaration_impl;
import org.apache.uima.cas.CAS;
import org.apache.uima.flow.FlowControllerDescription;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ExternalResourceDependency;
import org.apache.uima.resource.ExternalResourceDescription;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.resource.metadata.Capability;
import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.FsIndexCollection;
import org.apache.uima.resource.metadata.OperationalProperties;
import org.apache.uima.resource.metadata.TypePriorities;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.resource.metadata.impl.Import_impl;
import org.apache.uima.util.FileUtils;
import org.uimafit.component.initialize.ExternalResourceInitializer;
import org.uimafit.factory.ConfigurationParameterFactory.ConfigurationData;
import org.uimafit.util.ReflectionUtil;

/**
 * @author Steven Bethard, Philip Ogren, Fabio Mancinelli
 */
public final class AnalysisEngineFactory {

    private AnalysisEngineFactory() {
    }

    /**
	 * Get an AnalysisEngine from the name (Java-style, dotted) of an XML descriptor file, and a set
	 * of configuration parameters.
	 *
	 * @param descriptorName
	 *            The fully qualified, Java-style, dotted name of the XML descriptor file.
	 * @param configurationData
	 *            Any additional configuration parameters to be set. These should be supplied as
	 *            (name, value) pairs, so there should always be an even number of parameters.
	 * @return The AnalysisEngine created from the XML descriptor and the configuration parameters.
	 * @throws IOException
	 * @throws UIMAException
	 */
    public static AnalysisEngine createAnalysisEngine(String descriptorName, Object... configurationData) throws UIMAException, IOException {
        AnalysisEngineDescription aed = createAnalysisEngineDescription(descriptorName, configurationData);
        return UIMAFramework.produceAnalysisEngine(aed);
    }

    /**
	 * Provides a way to create an AnalysisEngineDescription using a descriptor file referenced by
	 * name
	 *
	 * @param descriptorName
	 * @param configurationData
	 *            should consist of name value pairs. Will override configuration parameter settings
	 *            in the descriptor file
	 * @return
	 * @throws UIMAException
	 * @throws IOException
	 */
    public static AnalysisEngineDescription createAnalysisEngineDescription(String descriptorName, Object... configurationData) throws UIMAException, IOException {
        Import_impl imprt = new Import_impl();
        imprt.setName(descriptorName);
        URL url = imprt.findAbsoluteUrl(UIMAFramework.newDefaultResourceManager());
        ResourceSpecifier specifier = ResourceCreationSpecifierFactory.createResourceCreationSpecifier(url, configurationData);
        return (AnalysisEngineDescription) specifier;
    }

    /**
	 * This method provides a convenient way to instantiate an AnalysisEngine where the default view
	 * is mapped to the view name passed into the method.
	 *
	 * @param analysisEngineDescription
	 * @param viewName
	 *            the view name to map the default view to
	 * @return an aggregate analysis engine consisting of a single component whose default view is
	 *         mapped to the the view named by viewName.
	 * @throws ResourceInitializationException
	 */
    public static AnalysisEngine createAnalysisEngine(AnalysisEngineDescription analysisEngineDescription, String viewName) throws ResourceInitializationException {
        AggregateBuilder builder = new AggregateBuilder();
        builder.add(analysisEngineDescription, CAS.NAME_DEFAULT_SOFA, viewName);
        return builder.createAggregate();
    }

    /**
	 * Get an AnalysisEngine from an XML descriptor file and a set of configuration parameters.
	 *
	 * @param descriptorPath
	 *            The path to the XML descriptor file.
	 * @param configurationData
	 *            Any additional configuration parameters to be set. These should be supplied as
	 *            (name, value) pairs, so there should always be an even number of parameters.
	 * @return The AnalysisEngine created from the XML descriptor and the configuration parameters.
	 * @throws UIMAException
	 * @throws IOException
	 */
    public static AnalysisEngine createAnalysisEngineFromPath(String descriptorPath, Object... configurationData) throws UIMAException, IOException {
        ResourceSpecifier specifier;
        specifier = ResourceCreationSpecifierFactory.createResourceCreationSpecifier(descriptorPath, configurationData);
        return UIMAFramework.produceAnalysisEngine(specifier);
    }

    /**
	 * Get an AnalysisEngine from an OperationalProperties class, a type system and a set of
	 * configuration parameters. The type system is detected automatically using
	 * {@link TypeSystemDescriptionFactory#createTypeSystemDescription()}.
	 *
	 * @param componentClass
	 *            The class of the OperationalProperties to be created as an AnalysisEngine.
	 * @param configurationData
	 *            Any additional configuration parameters to be set. These should be supplied as
	 *            (name, value) pairs, so there should always be an even number of parameters.
	 * @return The AnalysisEngine created from the OperationalProperties class and initialized with
	 *         the type system and the configuration parameters.
	 * @throws ResourceInitializationException
	 */
    public static AnalysisEngine createPrimitive(Class<? extends AnalysisComponent> componentClass, Object... configurationData) throws ResourceInitializationException {
        TypeSystemDescription tsd = createTypeSystemDescription();
        return createPrimitive(componentClass, tsd, (TypePriorities) null, configurationData);
    }

    /**
	 * Get an AnalysisEngine from an OperationalProperties class, a type system and a set of
	 * configuration parameters.
	 *
	 * @param componentClass
	 *            The class of the OperationalProperties to be created as an AnalysisEngine.
	 * @param typeSystem
	 *            A description of the types used by the OperationalProperties (may be null).
	 * @param configurationData
	 *            Any additional configuration parameters to be set. These should be supplied as
	 *            (name, value) pairs, so there should always be an even number of parameters.
	 * @return The AnalysisEngine created from the OperationalProperties class and initialized with
	 *         the type system and the configuration parameters.
	 * @throws ResourceInitializationException
	 */
    public static AnalysisEngine createPrimitive(Class<? extends AnalysisComponent> componentClass, TypeSystemDescription typeSystem, Object... configurationData) throws ResourceInitializationException {
        return createPrimitive(componentClass, typeSystem, (TypePriorities) null, configurationData);
    }

    /**
	 * @param componentClass
	 * @param typeSystem
	 * @param prioritizedTypeNames
	 * @param configurationData
	 * @return
	 * @throws ResourceInitializationException
	 */
    public static AnalysisEngine createPrimitive(Class<? extends AnalysisComponent> componentClass, TypeSystemDescription typeSystem, String[] prioritizedTypeNames, Object... configurationData) throws ResourceInitializationException {
        TypePriorities typePriorities = TypePrioritiesFactory.createTypePriorities(prioritizedTypeNames);
        return createPrimitive(componentClass, typeSystem, typePriorities, configurationData);
    }

    /**
	 * A simple factory method for creating a primitive AnalysisEngineDescription for a given class,
	 * type system, and configuration parameter data
	 *
	 * @param componentClass
	 *            a class that extends AnalysisComponent e.g.
	 *            org.uimafit.component.JCasAnnotator_ImplBase
	 * @param typeSystem
	 * @param configurationData
	 * @return
	 * @throws ResourceInitializationException
	 */
    public static AnalysisEngineDescription createPrimitiveDescription(Class<? extends AnalysisComponent> componentClass, TypeSystemDescription typeSystem, Object... configurationData) throws ResourceInitializationException {
        return createPrimitiveDescription(componentClass, typeSystem, (TypePriorities) null, configurationData);
    }

    /**
	 * A simple factory method for creating a primitive AnalysisEngineDescription for a given class,
	 * type system, and configuration parameter data The type system is detected automatically using
	 * {@link TypeSystemDescriptionFactory#createTypeSystemDescription()}.
	 *
	 * @param componentClass
	 *            a class that extends AnalysisComponent e.g.
	 *            org.uimafit.component.JCasAnnotator_ImplBase
	 * @param configurationData
	 * @return
	 * @throws ResourceInitializationException
	 */
    public static AnalysisEngineDescription createPrimitiveDescription(Class<? extends AnalysisComponent> componentClass, Object... configurationData) throws ResourceInitializationException {
        TypeSystemDescription tsd = createTypeSystemDescription();
        return createPrimitiveDescription(componentClass, tsd, (TypePriorities) null, configurationData);
    }

    /**
	 * @param componentClass
	 * @param typeSystem
	 * @param typePriorities
	 * @param configurationData
	 * @return
	 * @throws ResourceInitializationException
	 */
    public static AnalysisEngineDescription createPrimitiveDescription(Class<? extends AnalysisComponent> componentClass, TypeSystemDescription typeSystem, TypePriorities typePriorities, Object... configurationData) throws ResourceInitializationException {
        return createPrimitiveDescription(componentClass, typeSystem, typePriorities, (FsIndexCollection) null, (Capability[]) null, configurationData);
    }

    /**
	 * The factory methods for creating an AnalysisEngineDescription
	 *
	 * @param componentClass
	 * @param typeSystem
	 * @param typePriorities
	 * @param capabilities
	 * @param configurationData
	 * @return
	 * @throws ResourceInitializationException
	 */
    public static AnalysisEngineDescription createPrimitiveDescription(Class<? extends AnalysisComponent> componentClass, TypeSystemDescription typeSystem, TypePriorities typePriorities, FsIndexCollection indexes, Capability[] capabilities, Object... configurationData) throws ResourceInitializationException {
        ensureParametersComeInPairs(configurationData);
        Map<String, ExternalResourceDescription> externalResources = ExternalResourceFactory.extractExternalResourceParameters(configurationData);
        ConfigurationData cdata = createConfigurationData(configurationData);
        return createPrimitiveDescription(componentClass, typeSystem, typePriorities, indexes, capabilities, cdata.configurationParameters, cdata.configurationValues, externalResources);
    }

    public static AnalysisEngineDescription createPrimitiveDescription(Class<? extends AnalysisComponent> componentClass, TypeSystemDescription typeSystem, TypePriorities typePriorities, FsIndexCollection indexes, Capability[] capabilities, ConfigurationParameter[] configurationParameters, Object[] configurationValues) throws ResourceInitializationException {
        return createPrimitiveDescription(componentClass, typeSystem, typePriorities, indexes, capabilities, configurationParameters, configurationValues, null);
    }

    /**
	 * @param componentClass
	 * @param typeSystem
	 * @param typePriorities
	 * @param capabilities
	 * @param configurationParameters
	 * @param configurationValues
	 * @return
	 * @throws ResourceInitializationException
	 */
    public static AnalysisEngineDescription createPrimitiveDescription(Class<? extends AnalysisComponent> componentClass, TypeSystemDescription typeSystem, TypePriorities typePriorities, FsIndexCollection indexes, Capability[] capabilities, ConfigurationParameter[] configurationParameters, Object[] configurationValues, Map<String, ExternalResourceDescription> externalResources) throws ResourceInitializationException {
        AnalysisEngineDescription desc = UIMAFramework.getResourceSpecifierFactory().createAnalysisEngineDescription();
        desc.setFrameworkImplementation(Constants.JAVA_FRAMEWORK_NAME);
        desc.setPrimitive(true);
        desc.setAnnotatorImplementationName(componentClass.getName());
        org.uimafit.descriptor.OperationalProperties componentAnno = ReflectionUtil.getInheritableAnnotation(org.uimafit.descriptor.OperationalProperties.class, componentClass);
        if (componentAnno != null) {
            OperationalProperties op = desc.getAnalysisEngineMetaData().getOperationalProperties();
            op.setMultipleDeploymentAllowed(componentAnno.multipleDeploymentAllowed());
            op.setModifiesCas(componentAnno.modifiesCas());
            op.setOutputsNewCASes(componentAnno.outputsNewCases());
        } else {
            OperationalProperties op = desc.getAnalysisEngineMetaData().getOperationalProperties();
            op.setMultipleDeploymentAllowed(MULTIPLE_DEPLOYMENT_ALLOWED_DEFAULT);
            op.setModifiesCas(MODIFIES_CAS_DEFAULT);
            op.setOutputsNewCASes(OUTPUTS_NEW_CASES_DEFAULT);
        }
        AnalysisEngineMetaData meta = desc.getAnalysisEngineMetaData();
        meta.setName(componentClass.getName());
        if (componentClass.getPackage() != null) {
            meta.setVendor(componentClass.getPackage().getName());
        }
        meta.setDescription("Descriptor automatically generated by uimaFIT");
        meta.setVersion("unknown");
        ConfigurationData reflectedConfigurationData = createConfigurationData(componentClass);
        ResourceCreationSpecifierFactory.setConfigurationParameters(desc, reflectedConfigurationData.configurationParameters, reflectedConfigurationData.configurationValues);
        if (configurationParameters != null) {
            ResourceCreationSpecifierFactory.setConfigurationParameters(desc, configurationParameters, configurationValues);
        }
        if (typeSystem != null) {
            desc.getAnalysisEngineMetaData().setTypeSystem(typeSystem);
        }
        if (typePriorities != null) {
            desc.getAnalysisEngineMetaData().setTypePriorities(typePriorities);
        }
        if (indexes != null) {
            desc.getAnalysisEngineMetaData().setFsIndexCollection(indexes);
        } else {
            desc.getAnalysisEngineMetaData().setFsIndexCollection(FsIndexFactory.createFsIndexCollection(componentClass));
        }
        if (capabilities != null) {
            desc.getAnalysisEngineMetaData().setCapabilities(capabilities);
        } else {
            Capability capability = CapabilityFactory.createCapability(componentClass);
            if (capability != null) {
                desc.getAnalysisEngineMetaData().setCapabilities(new Capability[] { capability });
            }
        }
        Collection<ExternalResourceDependency> deps = ExternalResourceInitializer.getResourceDeclarations(componentClass).values();
        desc.setExternalResourceDependencies(deps.toArray(new ExternalResourceDependency[deps.size()]));
        if (externalResources != null) {
            for (Entry<String, ExternalResourceDescription> e : externalResources.entrySet()) {
                bindExternalResource(desc, e.getKey(), e.getValue());
            }
        }
        return desc;
    }

    /**
	 * Provides a way to override configuration parameter settings with new values in an
	 * AnalysisEngineDescription
	 *
	 * @param analysisEngineDescription
	 * @param configurationData
	 * @throws ResourceInitializationException
	 * @Deprecated use {@link ResourceCreationSpecifierFactory#setConfigurationParameters}
	 */
    @Deprecated
    public static void setConfigurationParameters(AnalysisEngineDescription analysisEngineDescription, Object... configurationData) throws ResourceInitializationException {
        ResourceCreationSpecifierFactory.setConfigurationParameters(analysisEngineDescription, configurationData);
    }

    /**
	 * @param componentClass
	 * @param typeSystem
	 * @param typePriorities
	 * @param configurationParameters
	 * @return
	 * @throws ResourceInitializationException
	 */
    public static AnalysisEngine createPrimitive(Class<? extends AnalysisComponent> componentClass, TypeSystemDescription typeSystem, TypePriorities typePriorities, Object... configurationParameters) throws ResourceInitializationException {
        AnalysisEngineDescription desc = createPrimitiveDescription(componentClass, typeSystem, typePriorities, configurationParameters);
        return createPrimitive(desc);
    }

    /**
	 * @param desc
	 * @param configurationData
	 * @return
	 * @throws ResourceInitializationException
	 */
    public static AnalysisEngine createPrimitive(AnalysisEngineDescription desc, Object... configurationData) throws ResourceInitializationException {
        AnalysisEngineDescription descClone = (AnalysisEngineDescription) desc.clone();
        ResourceCreationSpecifierFactory.setConfigurationParameters(descClone, configurationData);
        return UIMAFramework.produceAnalysisEngine(descClone);
    }

    /**
	 * @param componentClasses
	 * @param typeSystem
	 * @param typePriorities
	 * @param sofaMappings
	 * @param configurationParameters
	 * @return
	 * @throws ResourceInitializationException
	 */
    public static AnalysisEngine createAggregate(List<Class<? extends AnalysisComponent>> componentClasses, TypeSystemDescription typeSystem, TypePriorities typePriorities, SofaMapping[] sofaMappings, Object... configurationParameters) throws ResourceInitializationException {
        AnalysisEngineDescription desc = createAggregateDescription(componentClasses, typeSystem, typePriorities, sofaMappings, configurationParameters);
        AnalysisEngine engine = new AggregateAnalysisEngine_impl();
        engine.initialize(desc, null);
        return engine;
    }

    /**
	 * @param componentClasses
	 * @param typeSystem
	 * @param typePriorities
	 * @param sofaMappings
	 * @param flowControllerDescription
	 * @param configurationParameters
	 * @return
	 * @throws ResourceInitializationException
	 */
    public static AnalysisEngine createAggregate(List<Class<? extends AnalysisComponent>> componentClasses, TypeSystemDescription typeSystem, TypePriorities typePriorities, SofaMapping[] sofaMappings, FlowControllerDescription flowControllerDescription, Object... configurationParameters) throws ResourceInitializationException {
        AnalysisEngineDescription desc = createAggregateDescription(componentClasses, typeSystem, typePriorities, sofaMappings, configurationParameters, flowControllerDescription);
        AnalysisEngine engine = new AggregateAnalysisEngine_impl();
        engine.initialize(desc, null);
        return engine;
    }

    /**
	 * @param desc
	 * @return
	 * @throws ResourceInitializationException
	 */
    public static AnalysisEngine createAggregate(AnalysisEngineDescription desc) throws ResourceInitializationException {
        return UIMAFramework.produceAnalysisEngine(desc, null, null);
    }

    /**
	 * @param componentClasses
	 * @param typeSystem
	 * @param typePriorities
	 * @param sofaMappings
	 * @param configurationParameters
	 * @return
	 * @throws ResourceInitializationException
	 */
    public static AnalysisEngineDescription createAggregateDescription(List<Class<? extends AnalysisComponent>> componentClasses, TypeSystemDescription typeSystem, TypePriorities typePriorities, SofaMapping[] sofaMappings, Object... configurationParameters) throws ResourceInitializationException {
        List<AnalysisEngineDescription> primitiveEngineDescriptions = new ArrayList<AnalysisEngineDescription>();
        List<String> componentNames = new ArrayList<String>();
        for (Class<? extends AnalysisComponent> componentClass : componentClasses) {
            AnalysisEngineDescription primitiveDescription = createPrimitiveDescription(componentClass, typeSystem, typePriorities, configurationParameters);
            primitiveEngineDescriptions.add(primitiveDescription);
            componentNames.add(componentClass.getName());
        }
        return createAggregateDescription(primitiveEngineDescriptions, componentNames, typeSystem, typePriorities, sofaMappings, null);
    }

    /**
	 * @param analysisEngineDescriptions
	 * @param componentNames
	 * @param typeSystem
	 * @param typePriorities
	 * @param sofaMappings
	 * @return
	 * @throws ResourceInitializationException
	 */
    public static AnalysisEngine createAggregate(List<AnalysisEngineDescription> analysisEngineDescriptions, List<String> componentNames, TypeSystemDescription typeSystem, TypePriorities typePriorities, SofaMapping[] sofaMappings) throws ResourceInitializationException {
        AnalysisEngineDescription desc = createAggregateDescription(analysisEngineDescriptions, componentNames, typeSystem, typePriorities, sofaMappings, null);
        AnalysisEngine engine = new AggregateAnalysisEngine_impl();
        engine.initialize(desc, null);
        return engine;
    }

    /**
	 * @param analysisEngineDescriptions
	 * @return
	 * @throws ResourceInitializationException
	 */
    public static AnalysisEngineDescription createAggregateDescription(AnalysisEngineDescription... analysisEngineDescriptions) throws ResourceInitializationException {
        String[] names = new String[analysisEngineDescriptions.length];
        int i = 0;
        for (AnalysisEngineDescription aed : analysisEngineDescriptions) {
            names[i] = aed.getImplementationName() + "-" + i;
            i++;
        }
        return createAggregateDescription(asList(analysisEngineDescriptions), asList(names), null, null, null, null);
    }

    /**
	 * @param componentClasses
	 * @param typeSystem
	 * @param typePriorities
	 * @param sofaMappings
	 * @param flowControllerDescription
	 * @param configurationParameters
	 * @return
	 * @throws ResourceInitializationException
	 */
    public static AnalysisEngineDescription createAggregateDescription(List<Class<? extends AnalysisComponent>> componentClasses, TypeSystemDescription typeSystem, TypePriorities typePriorities, SofaMapping[] sofaMappings, FlowControllerDescription flowControllerDescription, Object... configurationParameters) throws ResourceInitializationException {
        List<AnalysisEngineDescription> primitiveEngineDescriptions = new ArrayList<AnalysisEngineDescription>();
        List<String> componentNames = new ArrayList<String>();
        for (Class<? extends AnalysisComponent> componentClass : componentClasses) {
            AnalysisEngineDescription primitiveDescription = createPrimitiveDescription(componentClass, typeSystem, typePriorities, configurationParameters);
            primitiveEngineDescriptions.add(primitiveDescription);
            componentNames.add(componentClass.getName());
        }
        return createAggregateDescription(primitiveEngineDescriptions, componentNames, typeSystem, typePriorities, sofaMappings, flowControllerDescription);
    }

    /**
	 * @param analysisEngineDescriptions
	 * @param componentNames
	 * @param typeSystem
	 * @param typePriorities
	 * @param sofaMappings
	 * @param flowControllerDescription
	 * @return
	 * @throws ResourceInitializationException
	 */
    public static AnalysisEngine createAggregate(List<AnalysisEngineDescription> analysisEngineDescriptions, List<String> componentNames, TypeSystemDescription typeSystem, TypePriorities typePriorities, SofaMapping[] sofaMappings, FlowControllerDescription flowControllerDescription) throws ResourceInitializationException {
        AnalysisEngineDescription desc = createAggregateDescription(analysisEngineDescriptions, componentNames, typeSystem, typePriorities, sofaMappings, flowControllerDescription);
        AnalysisEngine engine = new AggregateAnalysisEngine_impl();
        engine.initialize(desc, null);
        return engine;
    }

    /**
	 * A simplified factory method for creating an aggregate description for a given flow controller
	 * and a sequence of analysis engine descriptions
	 *
	 * @param flowControllerDescription
	 * @param analysisEngineDescriptions
	 * @return
	 * @throws ResourceInitializationException
	 */
    public static AnalysisEngineDescription createAggregateDescription(FlowControllerDescription flowControllerDescription, AnalysisEngineDescription... analysisEngineDescriptions) throws ResourceInitializationException {
        String[] names = new String[analysisEngineDescriptions.length];
        int i = 0;
        for (AnalysisEngineDescription aed : analysisEngineDescriptions) {
            names[i] = aed.getImplementationName() + "-" + i;
            i++;
        }
        return createAggregateDescription(asList(analysisEngineDescriptions), asList(names), null, null, null, flowControllerDescription);
    }

    /**
	 * A factory method for creating an aggregate description.
	 *
	 * @param analysisEngineDescriptions
	 * @param componentNames
	 * @param typeSystem
	 * @param typePriorities
	 * @param sofaMappings
	 * @param flowControllerDescription
	 * @return
	 * @throws ResourceInitializationException
	 */
    public static AnalysisEngineDescription createAggregateDescription(List<AnalysisEngineDescription> analysisEngineDescriptions, List<String> componentNames, TypeSystemDescription typeSystem, TypePriorities typePriorities, SofaMapping[] sofaMappings, FlowControllerDescription flowControllerDescription) throws ResourceInitializationException {
        AnalysisEngineDescription desc = new AnalysisEngineDescription_impl();
        desc.setFrameworkImplementation(Constants.JAVA_FRAMEWORK_NAME);
        desc.setPrimitive(false);
        boolean allowMultipleDeploy = true;
        for (AnalysisEngineDescription d : analysisEngineDescriptions) {
            allowMultipleDeploy &= d.getAnalysisEngineMetaData().getOperationalProperties().isMultipleDeploymentAllowed();
        }
        desc.getAnalysisEngineMetaData().getOperationalProperties().setMultipleDeploymentAllowed(allowMultipleDeploy);
        List<String> flowNames = new ArrayList<String>();
        for (int i = 0; i < analysisEngineDescriptions.size(); i++) {
            AnalysisEngineDescription aed = analysisEngineDescriptions.get(i);
            String componentName = componentNames.get(i);
            desc.getDelegateAnalysisEngineSpecifiersWithImports().put(componentName, aed);
            flowNames.add(componentName);
        }
        if (flowControllerDescription != null) {
            FlowControllerDeclaration flowControllerDeclaration = new FlowControllerDeclaration_impl();
            flowControllerDeclaration.setSpecifier(flowControllerDescription);
            desc.setFlowControllerDeclaration(flowControllerDeclaration);
        }
        FixedFlow fixedFlow = new FixedFlow_impl();
        fixedFlow.setFixedFlow(flowNames.toArray(new String[flowNames.size()]));
        desc.getAnalysisEngineMetaData().setFlowConstraints(fixedFlow);
        if (typePriorities != null) {
            desc.getAnalysisEngineMetaData().setTypePriorities(typePriorities);
        }
        if (sofaMappings != null) {
            desc.setSofaMappings(sofaMappings);
        }
        return desc;
    }

    /**
	 * Creates an AnalysisEngine from the given descriptor, and uses the engine to process the file
	 * or text.
	 *
	 * @param descriptorFileName
	 *            The fully qualified, Java-style, dotted name of the XML descriptor file.
	 * @param fileNameOrText
	 *            Either the path of a file to be loaded, or a string to use as the text. If the
	 *            string given is not a valid path in the file system, it will be assumed to be
	 *            text.
	 * @return A JCas object containing the processed document.
	 * @throws IOException
	 * @throws UIMAException
	 */
    public static JCas process(String descriptorFileName, String fileNameOrText) throws IOException, UIMAException {
        AnalysisEngine engine = createAnalysisEngine(descriptorFileName);
        JCas jCas = process(engine, fileNameOrText);
        engine.collectionProcessComplete();
        return jCas;
    }

    /**
	 * Processes the file or text with the given AnalysisEngine.
	 *
	 * @param analysisEngine
	 *            The AnalysisEngine object to process the text.
	 * @param fileNameOrText
	 *            Either the path of a file to be loaded, or a string to use as the text. If the
	 *            string given is not a valid path in the file system, it will be assumed to be
	 *            text.
	 * @return A JCas object containing the processed document.
	 * @throws IOException
	 * @throws UIMAException
	 */
    public static JCas process(AnalysisEngine analysisEngine, String fileNameOrText) throws IOException, UIMAException {
        JCas jCas = analysisEngine.newJCas();
        process(jCas, analysisEngine, fileNameOrText);
        return jCas;
    }

    /**
	 * Provides a convenience method for running an AnalysisEngine over some text with a given JCas.
	 *
	 * @param jCas
	 * @param analysisEngine
	 * @param fileNameOrText
	 * @throws IOException
	 * @throws UIMAException
	 */
    public static void process(JCas jCas, AnalysisEngine analysisEngine, String fileNameOrText) throws IOException, UIMAException {
        File textFile = new File(fileNameOrText);
        String text;
        if (textFile.exists()) {
            text = FileUtils.file2String(textFile);
        } else {
            text = fileNameOrText;
        }
        jCas.setDocumentText(text);
        analysisEngine.process(jCas);
    }
}
