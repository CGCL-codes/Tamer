package org.slasoi.models.scm.extended.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.slasoi.models.scm.ConfigurationDirective;
import org.slasoi.models.scm.Dependency;
import org.slasoi.models.scm.ServiceBinding;
import org.slasoi.models.scm.ServiceBuilder;
import org.slasoi.models.scm.ServiceConstructionModel;
import org.slasoi.models.scm.ServiceImplementation;
import org.slasoi.models.scm.extended.ServiceBuilderExtended;
import org.slasoi.monitoring.common.features.ComponentMonitoringFeatures;
import org.slasoi.monitoring.common.features.Event;
import org.slasoi.monitoring.common.features.MonitoringFeature;
import org.slasoi.monitoring.common.features.impl.FeaturesFactoryImpl;

/**
 * This class provides test cases for ServiceBuilderExtended class.
 * 
 * @author Alexander Wert
 * 
 */
public class ServiceBuilderTest {

    /**
     * Test target builder object.
     */
    private static ServiceBuilderExtended builder;

    /**
     * Sets up testing environment. Creates test target.
     */
    @Before
    public final void setUp() {
        builder = (ServiceBuilderExtended) ServiceConstructionModel.loadFromXMI("src/test/resources/TestBuilder.scm");
        assertNotNull(builder);
    }

    /**
     * Tests addBinding() method.
     */
    @Test
    public final void testAddBinding() {
        int numOpenDeps = builder.getOpenDependencies().size();
        assertTrue(numOpenDeps > 0);
        Dependency dependency = builder.getOpenDependencies().get(0);
        builder.addBinding(dependency, null);
        assertTrue(builder.getOpenDependencies().size() < numOpenDeps);
        boolean exceptionThrown = false;
        try {
            Dependency unknownDep = ServiceConstructionModel.getFactory().createDependency();
            builder.addBinding(unknownDep, null);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
        exceptionThrown = false;
        try {
            builder.addBinding(dependency, null);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    /**
     * Tests setBindings() method.
     */
    @Test
    public final void testSetBindings() {
        int numOpenDeps = builder.getOpenDependencies().size();
        assertTrue(numOpenDeps > 0);
        Dependency dependency = builder.getOpenDependencies().get(0);
        ServiceBinding[] bindings = new ServiceBinding[1];
        bindings[0] = ServiceConstructionModel.getFactory().createServiceBinding();
        bindings[0].setDependency(dependency);
        builder.setBindings(bindings);
        assertTrue(builder.getOpenDependencies().size() < numOpenDeps);
        boolean exceptionThrown = false;
        try {
            Dependency unknownDep = ServiceConstructionModel.getFactory().createDependency();
            ServiceBinding[] unknownBindings = new ServiceBinding[1];
            unknownBindings[0] = ServiceConstructionModel.getFactory().createServiceBinding();
            unknownBindings[0].setDependency(unknownDep);
            builder.setBindings(unknownBindings);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    /**
     * Tests setBindings() method. Tests getOpenDependencies(), too.
     */
    @Test
    public final void testSetBindingsByIndex() {
        int numOpenDeps = builder.getOpenDependencies().size();
        assertTrue(numOpenDeps > 0);
        Dependency dependency = builder.getOpenDependencies().get(0);
        ServiceBinding binding = ServiceConstructionModel.getFactory().createServiceBinding();
        binding.setDependency(dependency);
        builder.addBinding(dependency, null);
        builder.setBindings(0, binding);
        assertTrue(builder.getOpenDependencies().size() < numOpenDeps);
        boolean exceptionThrown = false;
        try {
            Dependency unknownDep = ServiceConstructionModel.getFactory().createDependency();
            ServiceBinding unknownBinding = ServiceConstructionModel.getFactory().createServiceBinding();
            unknownBinding.setDependency(unknownDep);
            builder.setBindings(0, unknownBinding);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    /**
     * Tests validate() method.
     */
    @Test
    public final void testValidate() {
        int numOpenDeps = builder.getOpenDependencies().size();
        assertTrue(numOpenDeps > 0);
        assertFalse(builder.validate());
        assertTrue(builder.getErrorString().length() > 0);
        for (Dependency dep : builder.getOpenDependencies()) {
            builder.addBinding(dep, null);
        }
        assertTrue(builder.getOpenDependencies().size() == 0);
        assertTrue(builder.validate());
        ConfigurationDirective dir = ServiceConstructionModel.getFactory().createConfigurationDirective();
        dir.setParameterValue("anotherValue");
        dir.setServiceFeature(builder.getImplementation().getConfigurableServiceFeatures().get(0));
        builder.getServiceConfigurationsList().add(dir);
        assertFalse(builder.validate());
    }

    /**
     * 
     */
    @Test
    public final void testEnableMonitoring() {
        ServiceImplementation impl = ServiceConstructionModel.getFactory().createServiceImplementation();
        MonitoringFeature mf = FeaturesFactoryImpl.eINSTANCE.createEvent();
        mf.setName("TestMonitoringFeature");
        ((Event) mf).setType("TestEventType");
        ComponentMonitoringFeatures ft = FeaturesFactoryImpl.eINSTANCE.createComponentMonitoringFeatures();
        ft.getMonitoringFeaturesList().add(mf);
        ft.setType("TestType");
        impl.getComponentMonitoringFeaturesList().add(ft);
        ServiceBuilder b = ServiceConstructionModel.getFactory().createServiceBuilder();
        b.setImplementation(impl);
        ((ServiceBuilderExtended) b).enableMonitoring(mf, "CPU");
        assertTrue(((ServiceBuilderExtended) b).getMonitoringFeatures().contains(mf));
        assertEquals(((ServiceBuilderExtended) b).getMonitoringFeatureByType("TestEventType"), mf);
    }

    /**
     * Tests setServiceConfigurations() methods.
     */
    @Test
    public final void testSetServiceConfigurations() {
        ConfigurationDirective[] directives = new ConfigurationDirective[1];
        directives[0] = ServiceConstructionModel.getFactory().createConfigurationDirective();
        directives[0].setParameterValue("anotherValue");
        directives[0].setServiceFeature(builder.getImplementation().getConfigurableServiceFeatures().get(0));
        builder.setServiceConfigurations(directives);
        assertTrue(builder.getServiceConfigurationsList().contains(directives[0]));
        ConfigurationDirective anotherDirective = ServiceConstructionModel.getFactory().createConfigurationDirective();
        anotherDirective.setParameterValue("anotherValue");
        anotherDirective.setServiceFeature(builder.getImplementation().getConfigurableServiceFeatures().get(0));
        builder.setServiceConfigurations(0, anotherDirective);
        assertTrue(builder.getServiceConfigurationsList().contains(anotherDirective));
    }
}
