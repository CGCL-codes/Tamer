package net.sourceforge.hivemind.editor.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.hivemind.ErrorHandler;
import org.apache.hivemind.Location;
import org.apache.hivemind.Occurances;
import org.apache.hivemind.ShutdownCoordinator;
import org.apache.hivemind.conditional.EvaluationContextImpl;
import org.apache.hivemind.conditional.Node;
import org.apache.hivemind.conditional.Parser;
import org.apache.hivemind.impl.ConfigurationPointImpl;
import org.apache.hivemind.impl.ContributionImpl;
import org.apache.hivemind.impl.ModuleImpl;
import org.apache.hivemind.impl.RegistryAssembly;
import org.apache.hivemind.impl.ServiceInterceptorContributionImpl;
import org.apache.hivemind.impl.ServicePointImpl;
import org.apache.hivemind.impl.ShutdownCoordinatorImpl;
import org.apache.hivemind.internal.ConfigurationPoint;
import org.apache.hivemind.internal.Module;
import org.apache.hivemind.internal.ServicePoint;
import org.apache.hivemind.parse.ConfigurationPointDescriptor;
import org.apache.hivemind.parse.ContributionDescriptor;
import org.apache.hivemind.parse.DependencyDescriptor;
import org.apache.hivemind.parse.ImplementationDescriptor;
import org.apache.hivemind.parse.InstanceBuilder;
import org.apache.hivemind.parse.InterceptorDescriptor;
import org.apache.hivemind.parse.ModuleDescriptor;
import org.apache.hivemind.parse.ServicePointDescriptor;
import org.apache.hivemind.schema.Schema;
import org.apache.hivemind.schema.impl.SchemaImpl;
import org.apache.hivemind.util.IdUtils;

/**
 * DOCME cdo: docu is missing
 * 
 * @history 27.02.2006 cdo created
 * 
 * @author <a href=mailto:cdo@top-logic.com>cdo</a>
 * @version $Revision: 6 $ $Author: $ $Date: 2006-02-28 17:16:21 -0500 (Tue, 28 Feb 2006) $
 */
public class ExtendedRegistryInfrastructureConstructor {

    private ErrorHandler errorHandler;

    private Log log;

    private RegistryAssembly assembly;

    private Parser conditionalExpressionParser;

    /**
     * Map of {@link ModuleDescriptor} keyed on module id.
     */
    private Map<String, ModuleDescriptor> moduleDescriptors = new HashMap<String, ModuleDescriptor>();

    /**
     * Map of {@link ModuleImpl} keyed on module id.
     */
    private Map<String, ModuleImpl> modules = new HashMap<String, ModuleImpl>();

    /**
     * Map of {@link Schema} keyed on fully qualified module id.
     */
    private Map<String, Schema> schemas = new HashMap<String, Schema>();

    /**
     * Map of {@link ServicePointImpl} keyed on fully qualified id.
     */
    private Map<String, ServicePointImpl> servicePoints = new HashMap<String, ServicePointImpl>();

    /**
     * Map of {@link ConfigurationPointImpl} keyed on fully qualified id.
     */
    private Map<String, ConfigurationPointImpl> configurationPoints = new HashMap<String, ConfigurationPointImpl>();

    /**
     * Shutdown coordinator shared by all objects.
     */
    private ShutdownCoordinator shutdownCoordinator = new ShutdownCoordinatorImpl();

    public ExtendedRegistryInfrastructureConstructor(ErrorHandler errorHandler, Log log, RegistryAssembly assembly) {
        this.errorHandler = errorHandler;
        this.log = log;
        this.assembly = assembly;
    }

    /**
     * Constructs the registry infrastructure, based on data collected during
     * the prior calls to {@link #addModuleDescriptor(ModuleDescriptor)}.
     * Expects that all post-processing of the {@link RegistryAssembly} has
     * already occured.
     */
    public ExtendedRegistryInfrastructure constructRegistryInfrastructure(Locale locale) {
        ExtendedRegistryInfrastructureImpl result = new ExtendedRegistryInfrastructureImpl(errorHandler, locale);
        addServiceAndConfigurationPoints(result);
        addImplementationsAndContributions();
        checkForMissingServices();
        checkContributionCounts();
        result.setShutdownCoordinator(shutdownCoordinator);
        addModulesToRegistry(result);
        return result;
    }

    public void addModuleDescriptor(ModuleDescriptor md) {
        String id = md.getModuleId();
        if (log.isDebugEnabled()) {
            log.debug("Processing module " + id);
        }
        if (modules.containsKey(id)) {
            Module existing = modules.get(id);
            errorHandler.error(log, ImplMessages.duplicateModuleId(id, existing.getLocation(), md.getLocation()), null, null);
            return;
        }
        ModuleImpl module = new ModuleImpl();
        module.setLocation(md.getLocation());
        module.setModuleId(id);
        module.setPackageName(md.getPackageName());
        module.setClassResolver(md.getClassResolver());
        if (size(md.getDependencies()) > 0) {
            assembly.addPostProcessor(new ModuleDependencyChecker(md));
        }
        for (Iterator schemaIt = md.getSchemas().iterator(); schemaIt.hasNext(); ) {
            SchemaImpl schema = (SchemaImpl) schemaIt.next();
            schema.setModule(module);
            schemas.put(IdUtils.qualify(id, schema.getId()), schema);
        }
        modules.put(id, module);
        moduleDescriptors.put(id, md);
    }

    private void addServiceAndConfigurationPoints(ExtendedRegistryInfrastructureImpl infrastructure) {
        for (ModuleDescriptor md : moduleDescriptors.values()) {
            String id = md.getModuleId();
            ModuleImpl module = modules.get(id);
            addServicePoints(infrastructure, module, md);
            addConfigurationPoints(infrastructure, module, md);
        }
    }

    private void addServicePoints(ExtendedRegistryInfrastructureImpl infrastructure, Module module, ModuleDescriptor md) {
        String moduleId = md.getModuleId();
        List services = md.getServicePoints();
        int count = size(services);
        for (int i = 0; i < count; i++) {
            ServicePointDescriptor sd = (ServicePointDescriptor) services.get(i);
            String pointId = moduleId + "." + sd.getId();
            ServicePoint existingPoint = servicePoints.get(pointId);
            if (existingPoint != null) {
                errorHandler.error(log, ImplMessages.duplicateExtensionPointId(pointId, existingPoint), sd.getLocation(), null);
                continue;
            }
            if (log.isDebugEnabled()) {
                log.debug("Creating service point " + pointId);
            }
            ServicePointImpl point = new ServicePointImpl();
            point.setExtensionPointId(pointId);
            point.setLocation(sd.getLocation());
            point.setModule(module);
            point.setServiceInterfaceName(sd.getInterfaceClassName());
            point.setParametersSchema(findSchema(sd.getParametersSchema(), module, sd.getParametersSchemaId(), point.getLocation()));
            point.setParametersCount(sd.getParametersCount());
            point.setVisibility(sd.getVisibility());
            point.setShutdownCoordinator(shutdownCoordinator);
            infrastructure.addServicePoint(point);
            servicePoints.put(pointId, point);
            addInternalImplementations(module, pointId, sd);
        }
    }

    private void addConfigurationPoints(ExtendedRegistryInfrastructureImpl registry, Module module, ModuleDescriptor md) {
        String moduleId = md.getModuleId();
        List points = md.getConfigurationPoints();
        int count = size(points);
        for (int i = 0; i < count; i++) {
            ConfigurationPointDescriptor cpd = (ConfigurationPointDescriptor) points.get(i);
            String pointId = moduleId + "." + cpd.getId();
            ConfigurationPoint existingPoint = configurationPoints.get(pointId);
            if (existingPoint != null) {
                errorHandler.error(log, ImplMessages.duplicateExtensionPointId(pointId, existingPoint), cpd.getLocation(), null);
                continue;
            }
            if (log.isDebugEnabled()) {
                log.debug("Creating configuration point " + pointId);
            }
            ConfigurationPointImpl point = new ConfigurationPointImpl();
            point.setExtensionPointId(pointId);
            point.setLocation(cpd.getLocation());
            point.setModule(module);
            point.setExpectedCount(cpd.getCount());
            point.setContributionsSchema(findSchema(cpd.getContributionsSchema(), module, cpd.getContributionsSchemaId(), cpd.getLocation()));
            point.setVisibility(cpd.getVisibility());
            point.setShutdownCoordinator(shutdownCoordinator);
            registry.addConfigurationPoint(point);
            configurationPoints.put(pointId, point);
        }
    }

    private void addContributionElements(Module sourceModule, ConfigurationPointImpl point, List elements) {
        if (size(elements) == 0) {
            return;
        }
        if (log.isDebugEnabled()) {
            log.debug("Adding contributions to configuration point " + point.getExtensionPointId());
        }
        ContributionImpl c = new ContributionImpl();
        c.setContributingModule(sourceModule);
        c.addElements(elements);
        point.addContribution(c);
    }

    private void addModulesToRegistry(ExtendedRegistryInfrastructureImpl registry) {
        for (ModuleImpl module : modules.values()) {
            if (log.isDebugEnabled()) {
                log.debug("Adding module " + module.getModuleId() + " to registry");
            }
            module.setRegistry(registry);
        }
    }

    private void addImplementationsAndContributions() {
        for (ModuleDescriptor md : moduleDescriptors.values()) {
            if (log.isDebugEnabled()) {
                log.debug("Adding contributions from module " + md.getModuleId());
            }
            addImplementations(md);
            addContributions(md);
        }
    }

    private void addImplementations(ModuleDescriptor md) {
        String moduleId = md.getModuleId();
        Module sourceModule = modules.get(moduleId);
        List implementations = md.getImplementations();
        int count = size(implementations);
        for (int i = 0; i < count; i++) {
            ImplementationDescriptor impl = (ImplementationDescriptor) implementations.get(i);
            if (!includeContribution(impl.getConditionalExpression(), sourceModule, impl.getLocation())) {
                continue;
            }
            String pointId = impl.getServiceId();
            String qualifiedId = IdUtils.qualify(moduleId, pointId);
            addImplementations(sourceModule, qualifiedId, impl);
        }
    }

    private void addContributions(ModuleDescriptor md) {
        String moduleId = md.getModuleId();
        Module sourceModule = modules.get(moduleId);
        List contributions = md.getContributions();
        int count = size(contributions);
        for (int i = 0; i < count; i++) {
            ContributionDescriptor cd = (ContributionDescriptor) contributions.get(i);
            if (!includeContribution(cd.getConditionalExpression(), sourceModule, cd.getLocation())) {
                continue;
            }
            String pointId = cd.getConfigurationId();
            String qualifiedId = IdUtils.qualify(moduleId, pointId);
            ConfigurationPointImpl point = configurationPoints.get(qualifiedId);
            if (point == null) {
                errorHandler.error(log, ImplMessages.unknownConfigurationPoint(moduleId, cd), cd.getLocation(), null);
                continue;
            }
            if (!point.visibleToModule(sourceModule)) {
                errorHandler.error(log, ImplMessages.configurationPointNotVisible(point, sourceModule), cd.getLocation(), null);
                continue;
            }
            addContributionElements(sourceModule, point, cd.getElements());
        }
    }

    private Schema findSchema(SchemaImpl schema, Module module, String schemaId, Location location) {
        if (schema != null) {
            schema.setModule(module);
            return schema;
        }
        if (schemaId == null) {
            return null;
        }
        String moduleId = module.getModuleId();
        String qualifiedId = IdUtils.qualify(moduleId, schemaId);
        return getSchema(qualifiedId, moduleId, location);
    }

    private Schema getSchema(String schemaId, String referencingModule, Location reference) {
        Schema schema = schemas.get(schemaId);
        if (schema == null) {
            errorHandler.error(log, ImplMessages.unableToResolveSchema(schemaId), reference, null);
        } else if (!schema.visibleToModule(referencingModule)) {
            errorHandler.error(log, ImplMessages.schemaNotVisible(schemaId, referencingModule), reference, null);
            schema = null;
        }
        return schema;
    }

    /**
     * Adds internal service contributions; the contributions provided inplace
     * with the service definition.
     */
    private void addInternalImplementations(Module sourceModule, String pointId, ServicePointDescriptor spd) {
        InstanceBuilder builder = spd.getInstanceBuilder();
        List interceptors = spd.getInterceptors();
        if (builder == null && interceptors == null) {
            return;
        }
        if (builder != null) {
            addServiceInstanceBuilder(sourceModule, pointId, builder, true);
        }
        if (interceptors == null) {
            return;
        }
        int count = size(interceptors);
        for (int i = 0; i < count; i++) {
            InterceptorDescriptor id = (InterceptorDescriptor) interceptors.get(i);
            addInterceptor(sourceModule, pointId, id);
        }
    }

    /**
     * Adds ordinary service contributions.
     */
    private void addImplementations(Module sourceModule, String pointId, ImplementationDescriptor id) {
        InstanceBuilder builder = id.getInstanceBuilder();
        List interceptors = id.getInterceptors();
        if (builder != null) {
            addServiceInstanceBuilder(sourceModule, pointId, builder, false);
        }
        int count = size(interceptors);
        for (int i = 0; i < count; i++) {
            InterceptorDescriptor ind = (InterceptorDescriptor) interceptors.get(i);
            addInterceptor(sourceModule, pointId, ind);
        }
    }

    /**
     * Adds an {@link InstanceBuilder} to a service extension point.
     */
    private void addServiceInstanceBuilder(Module sourceModule, String pointId, InstanceBuilder builder, boolean isDefault) {
        if (log.isDebugEnabled()) {
            log.debug("Adding " + builder + " to service extension point " + pointId);
        }
        ServicePointImpl point = servicePoints.get(pointId);
        if (point == null) {
            errorHandler.error(log, ImplMessages.unknownServicePoint(sourceModule, pointId), builder.getLocation(), null);
            return;
        }
        if (!point.visibleToModule(sourceModule)) {
            errorHandler.error(log, ImplMessages.servicePointNotVisible(point, sourceModule), builder.getLocation(), null);
            return;
        }
        if (point.getServiceConstructor(isDefault) != null) {
            errorHandler.error(log, ImplMessages.duplicateFactory(sourceModule, pointId, point), builder.getLocation(), null);
            return;
        }
        point.setServiceModel(builder.getServiceModel());
        point.setServiceConstructor(builder.createConstructor(point, sourceModule), isDefault);
    }

    private void addInterceptor(Module sourceModule, String pointId, InterceptorDescriptor id) {
        if (log.isDebugEnabled()) log.debug("Adding " + id + " to service extension point " + pointId);
        ServicePointImpl point = servicePoints.get(pointId);
        String sourceModuleId = sourceModule.getModuleId();
        if (point == null) {
            errorHandler.error(log, ImplMessages.unknownServicePoint(sourceModule, pointId), id.getLocation(), null);
            return;
        }
        if (!point.visibleToModule(sourceModule)) {
            errorHandler.error(log, ImplMessages.servicePointNotVisible(point, sourceModule), id.getLocation(), null);
            return;
        }
        ServiceInterceptorContributionImpl sic = new ServiceInterceptorContributionImpl();
        sic.setFactoryServiceId(IdUtils.qualify(sourceModuleId, id.getFactoryServiceId()));
        sic.setLocation(id.getLocation());
        sic.setFollowingInterceptorIds(IdUtils.qualifyList(sourceModuleId, id.getBefore()));
        sic.setPrecedingInterceptorIds(IdUtils.qualifyList(sourceModuleId, id.getAfter()));
        sic.setName(id.getName() != null ? IdUtils.qualify(sourceModuleId, id.getName()) : null);
        sic.setContributingModule(sourceModule);
        sic.setParameters(id.getParameters());
        point.addInterceptorContribution(sic);
    }

    /**
     * Checks that each service has at service constructor.
     */
    private void checkForMissingServices() {
        for (ServicePointImpl point : servicePoints.values()) {
            if (point.getServiceConstructor() != null) {
                continue;
            }
            errorHandler.error(log, ImplMessages.missingService(point), null, null);
        }
    }

    /**
     * Checks that each configuration extension point has the right number of
     * contributions.
     */
    private void checkContributionCounts() {
        for (ConfigurationPointImpl point : configurationPoints.values()) {
            Occurances expected = point.getExpectedCount();
            int actual = point.getContributionCount();
            if (expected.inRange(actual)) {
                continue;
            }
            errorHandler.error(log, ImplMessages.wrongNumberOfContributions(point, actual, expected), point.getLocation(), null);
        }
    }

    /**
     * Filters a contribution based on an expression. Returns true if the
     * expression is null, or evaluates to true. Returns false if the expression
     * if non-null and evaluates to false, or an exception occurs evaluating the
     * expression.
     * 
     * @param expression
     *            to parse and evaluate
     * @param location
     *            of the expression (used if an error is reported)
     * @since 1.1
     */
    private boolean includeContribution(String expression, Module module, Location location) {
        if (expression == null) {
            return true;
        }
        if (conditionalExpressionParser == null) {
            conditionalExpressionParser = new Parser();
        }
        try {
            Node node = conditionalExpressionParser.parse(expression);
            return node.evaluate(new EvaluationContextImpl(module.getClassResolver()));
        } catch (RuntimeException ex) {
            errorHandler.error(log, ex.getMessage(), location, ex);
            return false;
        }
    }

    private static int size(Collection c) {
        return c == null ? 0 : c.size();
    }

    /**
     * This class is used to check the dependencies of a ModuleDescriptor. As
     * the checker is run it will log errors to the ErrorHandler if dependencies
     * don't resolve or the versions dont match.
     */
    private class ModuleDependencyChecker implements Runnable {

        private ModuleDescriptor _source;

        public ModuleDependencyChecker(ModuleDescriptor source) {
            _source = source;
        }

        public void run() {
            List dependencies = _source.getDependencies();
            int count = size(dependencies);
            for (int i = 0; i < count; i++) {
                DependencyDescriptor dependency = (DependencyDescriptor) dependencies.get(i);
                checkDependency(dependency);
            }
        }

        private void checkDependency(DependencyDescriptor dependency) {
            ModuleDescriptor requiredModule = moduleDescriptors.get(dependency.getModuleId());
            if (requiredModule == null) {
                errorHandler.error(log, ImplMessages.dependencyOnUnknownModule(dependency), dependency.getLocation(), null);
                return;
            }
            if (dependency.getVersion() != null && !dependency.getVersion().equals(requiredModule.getVersion())) {
                errorHandler.error(log, ImplMessages.dependencyVersionMismatch(dependency), dependency.getLocation(), null);
                return;
            }
        }
    }
}
