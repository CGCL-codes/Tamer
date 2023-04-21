package com.hp.hpl.mars.portal.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.Arrays;
import java.util.Collection;
import org.apache.commons.logging.Log;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Suite;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.mars.portal.PortalException;
import com.hp.hpl.mars.portal.testUtil.LoggingInterceptor;
import com.hp.hpl.mars.portal.vocabulary.EXAMPLE;
import com.hp.hpl.mars.portal.widget.SimpleResourceWidget;
import com.hp.hpl.mars.portal.widget.Widget;

@RunWith(Suite.class)
@Suite.SuiteClasses(value = { ResourceToComponentByTypeTest.SpecialCases.class, ResourceToComponentByTypeTest.ComponentFactoryTest.class, ResourceToComponentByTypeTest.ParamTest.class })
public class ResourceToComponentByTypeTest {

    private static final String fullClassName = ResourceToComponentByTypeTest.class.getName();

    private static MockDescriptionFactory descriptionFactory = new MockDescriptionFactory();

    /**
	 * Parametrized tests - they all check, if the given
	 * ResourceToComponentByType mapping for the given mappable resource creates
	 * the widget with the expected ViewName.
	 */
    @RunWith(Parameterized.class)
    public static class ParamTest {

        private ResourceToComponentByTypeFactory factory = new ResourceToComponentByTypeFactory();

        private ComponentManager componentManager = new ComponentManager(null, new MockComponentFactoryCatalog().getCatalog());

        private String rMapLocalName;

        private String rCompLocalName;

        private String resultViewName;

        @Before
        public void setUp() {
            componentManager.setServletRequest(new MockHttpServletRequest());
        }

        @Parameters
        public static Collection data() {
            return Arrays.asList(new Object[][] { { "mapWithoutDefault", "resWithoutType", SimpleResourceWidget.DEFAULT_VIEWNAME }, { "mapWithDefault", "resWithoutType", "simpleWidget2_viewName" }, { "mapWithoutDefault", "resWithUnmappedType", SimpleResourceWidget.DEFAULT_VIEWNAME }, { "mapWithDefault", "resWithUnmappedType", "simpleWidget2_viewName" }, { "mapWithoutDefault", "resMappedToRuntimeError", SimpleResourceWidget.DEFAULT_VIEWNAME }, { "mapWithDefault", "resMappedToRuntimeError", "simpleWidget2_viewName" }, { "mapWithoutDefault", "resWithMisconfiguredType", SimpleResourceWidget.DEFAULT_VIEWNAME }, { "mapWithDefault", "resWithMisconfiguredType", "simpleWidget2_viewName" }, { "mapWithoutDefault", "resWithSinglyMappedType", "singlyMappedResource_2_viewName" }, { "mapWithVariousPrecedences", "resWithAmbiguouslyMappedType", "multipleHighestPriorityResource_viewName3or4" }, { "mapWithVariousPrecedences", "resWithOneHighestPriority", "multipleHighestPriorityResource_viewName5" }, { "fixedMap", "resWithoutType", "fixedViewName" }, { "fixedMap", "resWithAmbiguouslyMappedType", "fixedViewName" } });
        }

        public ParamTest(String rMapLocalName, String rCompLocalName, String resultViewName) {
            this.rMapLocalName = rMapLocalName;
            this.rCompLocalName = rCompLocalName;
            this.resultViewName = resultViewName;
        }

        /**
		 * This parametrized test method reads a ResourceToComponentByType from
		 * the RDF description, reads a component RDF description, performs the
		 * mapping by "getComponent()" and checks, if the resulting widget has
		 * the expected viewname type.
		 */
        @Test
        public void mapResource() {
            Model model = descriptionFactory.getPortalDescription(fullClassName + "/ParamTest");
            Resource rMap = model.getResource(EXAMPLE.NS + rMapLocalName);
            ResourceToComponentByType map = (ResourceToComponentByType) componentManager.getStaticComponent(rMap);
            Resource rComp = model.getResource(EXAMPLE.NS + rCompLocalName);
            Component c = map.getComponent(rComp, "seed");
            assertEquals(resultViewName, ((Widget) c).getViewName());
        }
    }

    /**
	 * This inherited test class checks, if the cache behavior for
	 * mutable/immutable resources matches the general contract of
	 * AbstractComponentFactory.
	 */
    public static class ComponentFactoryTest extends AbstractComponentFactoryTest {

        @Override
        protected AbstractComponentFactory getTarget() {
            return new ResourceToComponentByTypeFactory();
        }

        @Override
        protected Resource getImmutableResource() {
            Model model = descriptionFactory.getPortalDescription(fullClassName + "/ComponentFactoryTest");
            return model.getResource(EXAMPLE.NS + "immutableResource");
        }

        @Override
        protected Resource getMutableResource() {
            Model model = descriptionFactory.getPortalDescription(fullClassName + "/ComponentFactoryTest");
            return model.getResource(EXAMPLE.NS + "mutableResource");
        }
    }

    /**
	 * Some special behaviors, which do not need to be parametrized
	 */
    public static class SpecialCases {

        private ResourceToComponentByTypeFactory factory = new ResourceToComponentByTypeFactory();

        private ComponentManager componentManager = new ComponentManager(null, new MockComponentFactoryCatalog().getCatalog());

        @Before
        public void setUp() {
            componentManager.setServletRequest(new MockHttpServletRequest());
        }

        /**
		 * When a there is no mapping for a component, and the default mapping
		 * for the ResourceToComponentByType is portal:NoWidget, then <br/>
		 * throw PortalException (i.e. all resources should be explicitly mapped
		 * in such a case).
		 * 
		 */
        @Test(expected = PortalException.class)
        public void noWidgetErrorOnUnmappedComponent() {
            Model model = descriptionFactory.getPortalDescription(fullClassName + "/SpecialCases");
            Resource rTargetNoDefaultWidget = model.getResource(EXAMPLE.NS + "mapWithNoWidgetDefault");
            ResourceToComponentByType rtcbt = (ResourceToComponentByType) factory.getComponent(rTargetNoDefaultWidget, componentManager, null);
            Resource r = model.getResource(EXAMPLE.NS + "resWithUnmappedType");
            rtcbt.getComponent(r, "mapWithNoWidgetDefault");
        }

        /**
		 * When there are ambiguous mappings (i.e. resource can map to two
		 * different components, both having the highest precedence), then there
		 * should be log.warn() call with some string message.
		 * 
		 * AOP method call interceptor is used to verify, if log.warn() was ever
		 * called. This might be an overkill - could try to simplify
		 */
        @Test
        public void logWarning() {
            Model model = descriptionFactory.getPortalDescription(fullClassName + "/ParamTest");
            Resource rMap = model.getResource(EXAMPLE.NS + "mapWithVariousPrecedences");
            ResourceToComponentByType map = (ResourceToComponentByType) componentManager.getStaticComponent(rMap);
            ApplicationContext ctx = new FileSystemXmlApplicationContext("src/test/resources/" + fullClassName + "/portalCore-testconfig.xml");
            Log interceptableLog = (Log) ctx.getBean("logServiceBean");
            LoggingInterceptor interceptor = (LoggingInterceptor) ctx.getBean("logInterceptor");
            map.setLog(interceptableLog);
            Resource r = model.getResource(EXAMPLE.NS + "resWithAmbiguouslyMappedType");
            assertTrue(interceptor.getMethodName() == null);
            map.getComponent(r, "mapWithVariousPrecedences");
            assertEquals("warn", interceptor.getMethodName());
            assertTrue(((String) interceptor.getArgument()).length() > 0);
        }
    }
}
