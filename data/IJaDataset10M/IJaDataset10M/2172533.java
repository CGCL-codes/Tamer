package org.nakedobjects.plugins.headless.embedded;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.nakedobjects.commons.ensure.Ensure.ensureThatArg;
import static org.nakedobjects.commons.ensure.Ensure.ensureThatState;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.nakedobjects.commons.components.ApplicationScopedComponent;
import org.nakedobjects.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.config.internal.PropertiesConfiguration;
import org.nakedobjects.metamodel.facetdecorator.FacetDecorator;
import org.nakedobjects.metamodel.spec.IntrospectableSpecification;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.services.ServicesInjector;
import org.nakedobjects.metamodel.services.container.DomainObjectContainerDefault;
import org.nakedobjects.metamodel.specloader.NakedObjectReflectorAbstract;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;
import org.nakedobjects.metamodel.specloader.classsubstitutor.ClassSubstitutor;
import org.nakedobjects.metamodel.specloader.classsubstitutor.ClassSubstitutorIdentity;
import org.nakedobjects.metamodel.specloader.collectiontyperegistry.CollectionTypeRegistry;
import org.nakedobjects.metamodel.specloader.collectiontyperegistry.CollectionTypeRegistryDefault;
import org.nakedobjects.metamodel.specloader.progmodelfacets.ProgrammingModelFacets;
import org.nakedobjects.metamodel.specloader.progmodelfacets.ProgrammingModelFacetsJava5;
import org.nakedobjects.metamodel.specloader.traverser.SpecificationTraverser;
import org.nakedobjects.metamodel.specloader.traverser.SpecificationTraverserDefault;
import org.nakedobjects.metamodel.specloader.validator.MetaModelValidator;
import org.nakedobjects.metamodel.specloader.validator.MetaModelValidatorNoop;
import org.nakedobjects.plugins.headless.applib.HeadlessViewer;
import org.nakedobjects.plugins.headless.embedded.internal.RuntimeContextForEmbeddedMetaModel;
import org.nakedobjects.plugins.headless.viewer.DomainObjectContainerHeadlessViewer;
import org.nakedobjects.plugins.headless.viewer.internal.HeadlessViewerImpl;

/**
 * Facade for the entire NakedObjects metamodel and supporting components.
 */
public class NakedObjectsMetaModel implements ApplicationScopedComponent {

    private static enum State {

        NOT_INITIALIZED, INITIALIZED, SHUTDOWN
    }

    private final List<Class<?>> serviceTypes = new ArrayList<Class<?>>();

    private State state = State.NOT_INITIALIZED;

    private NakedObjectReflectorAbstract reflector;

    private RuntimeContextForEmbeddedMetaModel runtimeContext;

    private NakedObjectConfiguration configuration;

    private ClassSubstitutor classSubstitutor;

    private CollectionTypeRegistry collectionTypeRegistry;

    private ProgrammingModelFacets programmingModelFacets;

    private SpecificationTraverser specificationTraverser;

    private Set<FacetDecorator> facetDecorators;

    private MetaModelValidator metaModelValidator;

    private HeadlessViewer viewer;

    private EmbeddedContext context;

    private List<Object> services;

    public NakedObjectsMetaModel(final EmbeddedContext context, final Class<?>... serviceTypes) {
        this.serviceTypes.addAll(Arrays.asList(serviceTypes));
        setConfiguration(new PropertiesConfiguration());
        setClassSubstitutor(new ClassSubstitutorIdentity());
        setCollectionTypeRegistry(new CollectionTypeRegistryDefault());
        setSpecificationTraverser(new SpecificationTraverserDefault());
        setFacetDecorators(new TreeSet<FacetDecorator>());
        setProgrammingModelFacets(new ProgrammingModelFacetsJava5());
        setMetaModelValidator(new MetaModelValidatorNoop());
        this.context = context;
    }

    /**
	 * The list of classes representing services, as specified in the {@link #NakedObjectsMetaModel(EmbeddedContext, Class...) constructor}.
	 * 
	 * <p>
	 * To obtain the instantiated services, use the {@link ServicesInjector#getRegisteredServices()} (available from {@link #getServicesInjector()}).
	 */
    public List<Class<?>> getServiceTypes() {
        return Collections.unmodifiableList(serviceTypes);
    }

    public void init() {
        ensureNotInitialized();
        reflector = new NakedObjectReflectorAbstract(configuration, classSubstitutor, collectionTypeRegistry, specificationTraverser, programmingModelFacets, facetDecorators, metaModelValidator) {
        };
        services = createServices(serviceTypes);
        runtimeContext = new RuntimeContextForEmbeddedMetaModel(context, services);
        DomainObjectContainerDefault container = new DomainObjectContainerHeadlessViewer();
        runtimeContext.injectInto(container);
        runtimeContext.setContainer(container);
        runtimeContext.injectInto(reflector);
        reflector.injectInto(runtimeContext);
        reflector.init();
        runtimeContext.init();
        for (Class<?> serviceType : serviceTypes) {
            NakedObjectSpecification serviceNoSpec = reflector.loadSpecification(serviceType);
            if (serviceNoSpec instanceof IntrospectableSpecification) {
                IntrospectableSpecification introspectableSpecification = (IntrospectableSpecification) serviceNoSpec;
                introspectableSpecification.markAsService();
            }
        }
        state = State.INITIALIZED;
        viewer = new HeadlessViewerImpl(runtimeContext);
    }

    public void shutdown() {
        ensureInitialized();
        state = State.SHUTDOWN;
    }

    private List<Object> createServices(List<Class<?>> serviceTypes) {
        List<Object> services = new ArrayList<Object>();
        for (Class<?> serviceType : serviceTypes) {
            try {
                services.add(serviceType.newInstance());
            } catch (InstantiationException e) {
                throw new NakedObjectException("Unable to instantiate service", e);
            } catch (IllegalAccessException e) {
                throw new NakedObjectException("Unable to instantiate service", e);
            }
        }
        return services;
    }

    /**
	 * Available once {@link #init() initialized}.
	 */
    public SpecificationLoader getSpecificationLoader() {
        return reflector;
    }

    /**
	 * Available once {@link #init() initialized}.
	 */
    public HeadlessViewer getViewer() {
        ensureInitialized();
        return viewer;
    }

    /**
	 * The {@link ServicesInjector}; can use to obtain the set of registered services.
	 * 
	 * <p>
	 * Available once {@link #init() initialized}.
	 */
    public ServicesInjector getServicesInjector() {
        ensureInitialized();
        return runtimeContext.getServicesInjector();
    }

    /**
	 * The {@link NakedObjectConfiguration} in force, either defaulted or specified
	 * {@link #setConfiguration(NakedObjectConfiguration) explicitly.}
	 */
    public NakedObjectConfiguration getConfiguration() {
        return configuration;
    }

    /**
	 * Optionally specify the {@link NakedObjectConfiguration}.
	 * 
	 * <p>
	 * Call prior to {@link #init()}.
	 */
    public void setConfiguration(NakedObjectConfiguration configuration) {
        ensureNotInitialized();
        ensureThatArg(configuration, is(notNullValue()));
        this.configuration = configuration;
    }

    /**
	 * The {@link ClassSubstitutor} in force, either defaulted or
	 * specified {@link #setClassSubstitutor(ClassSubstitutor) explicitly}.
	 */
    public ClassSubstitutor getClassSubstitutor() {
        return classSubstitutor;
    }

    /**
	 * Optionally specify the {@link ClassSubstitutor}.
	 * 
	 * <p>
	 * Call prior to {@link #init()}.
	 */
    public void setClassSubstitutor(ClassSubstitutor classSubstitutor) {
        ensureNotInitialized();
        ensureThatArg(classSubstitutor, is(notNullValue()));
        this.classSubstitutor = classSubstitutor;
    }

    /**
	 * The {@link CollectionTypeRegistry} in force, either defaulted or
	 * specified {@link #setCollectionTypeRegistry(CollectionTypeRegistry) explicitly.}
	 */
    public CollectionTypeRegistry getCollectionTypeRegistry() {
        return collectionTypeRegistry;
    }

    /**
	 * Optionally specify the {@link CollectionTypeRegistry}.
	 * 
	 * <p>
	 * Call prior to {@link #init()}.
	 */
    public void setCollectionTypeRegistry(CollectionTypeRegistry collectionTypeRegistry) {
        ensureNotInitialized();
        ensureThatArg(collectionTypeRegistry, is(notNullValue()));
        this.collectionTypeRegistry = collectionTypeRegistry;
    }

    /**
	 * The {@link SpecificationTraverser} in force, either defaulted or
	 * specified {@link #setSpecificationTraverser(SpecificationTraverser) explicitly}.
	 */
    public SpecificationTraverser getSpecificationTraverser() {
        return specificationTraverser;
    }

    /**
	 * Optionally specify the {@link SpecificationTraverser}.
	 */
    public void setSpecificationTraverser(SpecificationTraverser specificationTraverser) {
        this.specificationTraverser = specificationTraverser;
    }

    /**
	 * The {@link ProgrammingModelFacets} in force, either defaulted or
	 * specified {@link #setProgrammingModelFacets(ProgrammingModelFacets) explicitly}.
	 */
    public ProgrammingModelFacets getProgrammingModelFacets() {
        return programmingModelFacets;
    }

    /**
	 * Optionally specify the {@link ProgrammingModelFacets}.
	 * 
	 * <p>
	 * Call prior to {@link #init()}.
	 */
    public void setProgrammingModelFacets(ProgrammingModelFacets programmingModelFacets) {
        ensureNotInitialized();
        ensureThatArg(programmingModelFacets, is(notNullValue()));
        this.programmingModelFacets = programmingModelFacets;
    }

    /**
	 * The {@link FacetDecorator}s in force, either defaulted or specified
	 * {@link #setFacetDecorators(Set) explicitly}.
	 */
    public Set<FacetDecorator> getFacetDecorators() {
        return Collections.unmodifiableSet(facetDecorators);
    }

    /**
	 * Optionally specify the {@link FacetDecorator}s.
	 * 
	 * <p>
	 * Call prior to {@link #init()}.
	 */
    public void setFacetDecorators(Set<FacetDecorator> facetDecorators) {
        ensureNotInitialized();
        ensureThatArg(facetDecorators, is(notNullValue()));
        this.facetDecorators = facetDecorators;
    }

    /**
	 * The {@link MetaModelValidator} in force, either defaulted or specified
	 * {@link #setMetaModelValidator(MetaModelValidator) explicitly}.
	 */
    public MetaModelValidator getMetaModelValidator() {
        return metaModelValidator;
    }

    /**
	 * Optionally specify the {@link MetaModelValidator}.
	 */
    public void setMetaModelValidator(MetaModelValidator metaModelValidator) {
        this.metaModelValidator = metaModelValidator;
    }

    private State ensureNotInitialized() {
        return ensureThatState(state, is(State.NOT_INITIALIZED));
    }

    private State ensureInitialized() {
        return ensureThatState(state, is(State.INITIALIZED));
    }
}
