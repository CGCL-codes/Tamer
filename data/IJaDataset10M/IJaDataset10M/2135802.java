package com.volantis.xml.pipeline.sax;

import com.volantis.shared.environment.EnvironmentInteraction;
import com.volantis.shared.servlet.ServletEnvironmentFactory;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.namespace.NamespaceFactory;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.xml.pipeline.sax.impl.dynamic.ContextManagerProcess;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcessConfiguration;
import com.volantis.xml.pipeline.sax.dynamic.DynamicRuleConfigurator;
import com.volantis.xml.pipeline.sax.impl.dynamic.FlowControlProcess;
import com.volantis.xml.pipeline.sax.impl.dynamic.SimpleDynamicProcess;
import com.volantis.xml.pipeline.sax.impl.dynamic.FlowControlProcess;
import com.volantis.xml.pipeline.sax.impl.dynamic.SimpleDynamicProcessConfiguration;
import com.volantis.xml.pipeline.sax.impl.dynamic.SimpleDynamicProcessConfiguration;
import com.volantis.xml.pipeline.sax.impl.XMLPipelineContextImpl;
import org.xml.sax.XMLReader;

/**
 * Test Case for the {@link XMLPipelineFactory} class.
 */
public class DefaultPipelineFactoryTestCase extends XMLPipelineFactoryTestCase {

    /**
     * Instance of the class being tested
     */
    protected XMLPipelineFactory factory;

    /**
     * Creates a new DefaultPipelineFactoryTestCase instance.
     * @param name the name of the test.
     */
    public DefaultPipelineFactoryTestCase(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        factory = createTestable();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        factory = null;
    }

    /**
     * Factory method for creating an instance of the class being tested.
     * @return an XMLPipelineFactory instance.
     */
    protected XMLPipelineFactory createTestable() {
        return XMLPipelineFactory.getDefaultInstance();
    }

    /**
     * Factory method that creates an <code>XMLPipeline</code> instance.
     * @return an XMLPipeline instance.
     */
    protected XMLPipeline createPipeline() {
        XMLPipelineFactory pipelineFactory = createTestable();
        return pipelineFactory.createPipeline(createContext());
    }

    /**
     * Factory method that creates an <code>XMLPipelineContext</code> instance.
     * @return an XMLPipeline instance.
     */
    protected XMLPipelineContext createContext() {
        XMLPipelineFactory pipelineFactory = createTestable();
        XMLPipelineConfiguration config = pipelineFactory.createPipelineConfiguration();
        ExpressionFactory expressionFactory = pipelineFactory.getExpressionFactory();
        ExpressionContext expressionContext = expressionFactory.createExpressionContext(null, null);
        return factory.createPipelineContext(config, expressionContext);
    }

    /**
     * Tests the {@link XMLPipelineFactory#createPipelineContext} method.
     * @throws Exception if an error occurs.
     */
    public void testCreatePipelineContext() throws Exception {
        XMLPipelineConfiguration config = factory.createPipelineConfiguration();
        EnvironmentInteraction interaction = ServletEnvironmentFactory.getDefaultInstance().createEnvironmentInteraction(null, null, null, null, null);
        XMLPipelineContext context = factory.createPipelineContext(config, interaction);
        assertEquals("createPipelineContext should return an instance " + "of XMLPipelineContextImpl", XMLPipelineContextImpl.class, context.getClass());
        assertEquals("createPipelineContext should reference the " + "XMLPipelineConfiguration provided via the " + "createPipelineContext method", config, context.getPipelineConfiguration());
        assertEquals("createPipelineContext should reference the " + "Root environment interaction provided via the " + "createPipelineContext method", interaction, context.getEnvironmentInteractionTracker().getRootEnvironmentInteraction());
    }

    /**
     * Tests the {@link XMLPipelineFactory#createDynamicPipeline} method.
     * @throws Exception if an error occurs.
     */
    public void testSecondCreatePipelineContext() throws Exception {
        XMLPipelineConfiguration config = factory.createPipelineConfiguration();
        ExpressionContext expressionContext = factory.getExpressionFactory().createExpressionContext(null, null);
        XMLPipelineContext context = factory.createPipelineContext(config, expressionContext);
        assertEquals("createPipelineContext should return an instance " + "of XMLPipelineContextImpl", XMLPipelineContextImpl.class, context.getClass());
        assertEquals("createPipelineContext should reference the " + "XMLPipelineConfiguration provided via the " + "createPipelineContext method", config, context.getPipelineConfiguration());
        assertEquals("createPipelineContext should reference the " + "ExpressionContext provided via the " + "createPipelineContext method", expressionContext, context.getExpressionContext());
    }

    /**
     * Tests the {@link XMLPipelineFactory#createContextUpdatingProcess}
     * method.
     * @throws Exception if an error occurs.
     */
    public void testCreateContextUpdatingProcess() throws Exception {
        XMLProcess contextProcess = factory.createContextUpdatingProcess();
        assertEquals("createContextUpdatingProcess should factor a" + " ContextManagerProcess intance", ContextManagerProcess.class, contextProcess.getClass());
    }

    /**
     * Tests the {@link XMLPipelineFactory#createPipelineFilter} method.
     * @throws Exception if an error occurs.
     */
    public void testCreatePipelineFilter() throws Exception {
        XMLPipeline pipeline = createPipeline();
        XMLPipelineFilter filter = factory.createPipelineFilter(pipeline);
        assertTrue("createPipleineFilter should factor an " + "XMLPipelineFilterAdapter instance", filter instanceof XMLPipelineFilterAdapter);
    }

    /**
     * Tests the {@link XMLPipelineFactory#createPipelineReader} method.
     * @throws Exception if an error occurs.
     */
    public void testCreatePipelineReader() throws Exception {
        XMLPipeline pipeline = createPipeline();
        XMLPipelineReader reader = factory.createPipelineReader(pipeline);
        assertTrue("createPipleineReader should factor an " + "XMLPipelineFilterAdapter instance", reader instanceof XMLPipelineFilterAdapter);
        assertTrue("The returned XMLPipelineFilterAdapter should have an " + "XMLReader set as its parent.", ((XMLPipelineFilter) reader).getParent() instanceof XMLReader);
    }

    /**
     * Tests the {@link XMLPipelineFactory#getRuleConfigurator} method.
     * @throws Exception if an error occurs.
     */
    public void testGetRuleConfigurator() throws Exception {
        DynamicRuleConfigurator configurator = factory.getRuleConfigurator();
        assertNotNull("getRuleConfigurator returned null", configurator);
    }

    /**
     * Tests the {@link XMLPipelineFactory#createFlowControlProcess}
     * method.
     * @throws Exception if an error occurs.
     */
    public void testCreateFlowControlProcess() throws Exception {
        XMLProcess flowProcess = factory.createFlowControlProcess();
        assertEquals("createFlowControlProcess should return a " + "FlowControlProcess instance", FlowControlProcess.class, flowProcess.getClass());
    }

    /**
     * Tests the 
     * {@link XMLPipelineFactory#createDynamicProcessConfiguration} method
     * @throws Exception if an error occurs
     */
    public void testCreateDynamicProcessConfiguration() throws Exception {
        DynamicProcessConfiguration config = factory.createDynamicProcessConfiguration();
        assertEquals("createDynamicProcessConfiguration should factor a " + "SimpleDynamicProcessConfiguration instance", SimpleDynamicProcessConfiguration.class, config.getClass());
    }

    /**
     * Tests the {@link XMLPipelineFactory#createDynamicProcess} method.
     * @throws Exception if an error occurs.
     */
    public void testCreateDynamicProcess() throws Exception {
        XMLPipelineConfiguration configuration = factory.createPipelineConfiguration();
        configuration.storeConfiguration(DynamicProcessConfiguration.class, factory.createDynamicProcessConfiguration());
        DynamicProcess process = factory.createDynamicProcess(configuration);
        assertEquals("createDynamicProcess should factor a " + "SimpleDynamicProcess instance", SimpleDynamicProcess.class, process.getClass());
    }

    /**
     * Tests the {@link XMLPipelineFactory#createDynamicProcess} method.
     * @throws Exception if an error occurs.
     */
    public void testSecondCreateDynamicProcess() throws Exception {
        DynamicProcess process = factory.createDynamicProcess(factory.createDynamicProcessConfiguration());
        assertEquals("createDynamicProcess should factor a " + "SimpleDynamicProcess instance", SimpleDynamicProcess.class, process.getClass());
    }

    /**
     * Tests the {@link XMLPipelineFactory#createPipelineConfiguration}
     * method.
     * @throws Exception if an error occurs.
     */
    public void testCreatePipelineConfiguration() throws Exception {
        DynamicProcessConfiguration config = factory.createDynamicProcessConfiguration();
        assertEquals("createPipelineConfiguration should factor a " + "SimpleDynamicProcessConfiguration instance", SimpleDynamicProcessConfiguration.class, config.getClass());
    }

    /**
     * Tests the {@link XMLPipelineFactory#createPipeline} method.
     * @throws Exception if an error occurs.
     */
    public void testCreatePipeline() throws Exception {
        XMLPipelineContext context = createContext();
        XMLPipeline pipeline = factory.createPipeline(context);
        assertEquals("createPipeline should factor a XMLPipelineProcessImpl " + "instance", XMLPipelineProcessImpl.class, pipeline.getClass());
    }

    /**
     * Tests the {@link XMLPipelineFactory#createDynamicPipeline} method.
     * @throws Exception if an error occurs.
     */
    public void testCreateDynamicPipeline() throws Exception {
        XMLPipelineContext context = createContext();
        XMLPipelineConfiguration config = context.getPipelineConfiguration();
        config.storeConfiguration(DynamicProcessConfiguration.class, factory.createDynamicProcessConfiguration());
        XMLPipeline pipeline = factory.createDynamicPipeline(context);
        assertEquals("createDynamicPipeline should factor a " + "SimpleDynamicProcess instance", SimpleDynamicProcess.class, pipeline.getClass());
        assertEquals("Pipeline process should be the pipeline", pipeline, pipeline.getPipelineProcess());
        assertNull("Head process must not be set", pipeline.getHeadProcess());
    }

    /**
     * Tests the {@link XMLPipelineFactory#getNamespaceFactory} method.
     * @throws Exception if an error occurs.
     */
    public void testGetNamespaceFactory() throws Exception {
        NamespaceFactory namespaceFactory = factory.getNamespaceFactory();
        assertEquals("getNamespaceFactory should return the default " + "NamespaceFactory instance", NamespaceFactory.getDefaultInstance(), namespaceFactory);
    }

    /**
     * Tests the {@link XMLPipelineFactory#getExpressionFactory} method.
     * @throws Exception if an error occurs.
     */
    public void testGetExpressionFactory() throws Exception {
        ExpressionFactory expressionFactory = factory.getExpressionFactory();
        assertEquals("getExpressionFactory should return the default " + "ExpressionFactory instance", ExpressionFactory.getDefaultInstance(), expressionFactory);
    }
}
