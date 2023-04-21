package com.volantis.xml.pipeline.sax.impl;

import com.volantis.shared.environment.EnvironmentFactory;
import com.volantis.shared.environment.EnvironmentInteraction;
import com.volantis.shared.environment.EnvironmentInteractionTracker;
import com.volantis.shared.throwable.ExtendedRuntimeException;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.namespace.NamespaceFactory;
import com.volantis.xml.namespace.NamespacePrefixTracker;
import com.volantis.xml.pipeline.extensions.PipelineExtensionFactory;
import com.volantis.xml.pipeline.sax.InternalXMLPipelineFactory;
import com.volantis.xml.pipeline.sax.XMLPipeline;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineFilter;
import com.volantis.xml.pipeline.sax.XMLPipelineFilterAdapter;
import com.volantis.xml.pipeline.sax.XMLPipelineProcess;
import com.volantis.xml.pipeline.sax.XMLPipelineProcessImpl;
import com.volantis.xml.pipeline.sax.XMLPipelineReader;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.config.Configuration;
import com.volantis.xml.pipeline.sax.config.SimpleXMLPipelineConfiguration;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.xml.pipeline.sax.drivers.uri.URIDriverFactory;
import com.volantis.xml.pipeline.sax.drivers.web.WebDriverFactory;
import com.volantis.xml.pipeline.sax.drivers.webservice.WebServiceDriverFactory;
import com.volantis.xml.pipeline.sax.drivers.picasa.PicasaDriverFactory;
import com.volantis.xml.pipeline.sax.drivers.flickr.FlickrDriverFactory;
import com.volantis.xml.pipeline.sax.drivers.googledocs.GoogleDocsDriverFactory;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcessConfiguration;
import com.volantis.xml.pipeline.sax.dynamic.DynamicRuleConfigurator;
import com.volantis.xml.pipeline.sax.flow.FlowControlManager;
import com.volantis.xml.pipeline.sax.impl.dynamic.ContextAnnotatingProcess;
import com.volantis.xml.pipeline.sax.impl.dynamic.ContextManagerProcess;
import com.volantis.xml.pipeline.sax.impl.dynamic.FlowControlProcess;
import com.volantis.xml.pipeline.sax.impl.dynamic.SimpleDynamicProcess;
import com.volantis.xml.pipeline.sax.impl.dynamic.SimpleDynamicProcessConfiguration;
import com.volantis.xml.pipeline.sax.impl.flow.SimpleFlowControlManager;
import com.volantis.xml.pipeline.sax.impl.recorder.ContextPipelineRecorder;
import com.volantis.xml.pipeline.sax.operations.PipelineOperationFactory;
import com.volantis.xml.pipeline.sax.performance.MonitoringConfiguration;
import com.volantis.xml.pipeline.sax.recorder.PipelineRecorder;
import com.volantis.xml.pipeline.sax.servlet.ServletOperationFactory;
import com.volantis.xml.pipeline.sax.template.TemplateFactory;
import com.volantis.xml.utilities.sax.XMLReaderFactory;
import java.io.IOException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * A default XML implementation of the XMLPipelineFactory interface
 */
public class XMLPipelineFactoryImpl extends InternalXMLPipelineFactory {

    /**
     * The default configuration for performance monitoring
     */
    private static final Configuration monitoringConfiguration = new MonitoringConfiguration();

    /**
     *  DynamicRuleConfigurator for this factory
     */
    private final DynamicRuleConfigurator ruleConfigurator;

    private final InternalXMLPipelineFactory wrapper;

    /**
     * Creates a new <code>XMLPipelineFactoryImpl</code> instance
     */
    public XMLPipelineFactoryImpl() {
        this(null);
    }

    public XMLPipelineFactoryImpl(InternalXMLPipelineFactory wrapper) {
        this.wrapper = wrapper == null ? this : wrapper;
        ruleConfigurator = createRuleConfigurator();
    }

    public XMLPipelineContext createPipelineContext(XMLPipelineConfiguration configuration, EnvironmentInteraction environmentInteraction) {
        NamespacePrefixTracker namespaceTracker = wrapper.getNamespaceFactory().createPrefixTracker();
        EnvironmentFactory environmentFactory = EnvironmentFactory.getDefaultInstance();
        EnvironmentInteractionTracker envTracker = environmentFactory.createInteractionTracker();
        if (environmentInteraction != null) {
            envTracker.pushEnvironmentInteraction(environmentInteraction);
        }
        ExpressionContext expressionContext = wrapper.getExpressionFactory().createExpressionContext(envTracker, namespaceTracker);
        return createPipelineContext(configuration, expressionContext);
    }

    public XMLPipelineContext createPipelineContext(XMLPipelineConfiguration configuration, ExpressionContext expressionContext) {
        return new XMLPipelineContextImpl(wrapper, configuration, expressionContext);
    }

    public XMLProcess createContextUpdatingProcess() {
        return new ContextManagerProcess();
    }

    public XMLProcess createContextAnnotatingProcess() {
        return new ContextAnnotatingProcess();
    }

    public XMLProcess createContextAnnotatingProcess(boolean setBaseURIOnRoot) {
        return new ContextAnnotatingProcess(setBaseURIOnRoot);
    }

    public XMLPipelineFilter createPipelineFilter(XMLPipeline pipeline) throws SAXException {
        return createPipelineFilter(pipeline, false);
    }

    public XMLPipelineFilter createPipelineFilter(XMLPipeline pipeline, boolean setBaseURIOnRoot) throws SAXException {
        DefaultXMLPipelineProcess outerPipeline = new DefaultXMLPipelineProcess(pipeline.getPipelineContext());
        XMLPipelineProcess xmlProcess = (XMLPipelineProcess) pipeline.getPipelineProcess();
        XMLProcess next = xmlProcess.getNextProcess();
        outerPipeline.setNextProcess(next);
        outerPipeline.addTailProcess(wrapper.createContextUpdatingProcess());
        outerPipeline.addTailXMLPipelineProcess(xmlProcess);
        final XMLProcess cap = wrapper.createContextAnnotatingProcess(setBaseURIOnRoot);
        outerPipeline.addTailProcess(cap);
        return new XMLPipelineFilterAdapter(outerPipeline) {

            public void parse(InputSource input) throws IOException, SAXException {
                super.parse(input);
                cap.stopProcess();
            }
        };
    }

    public XMLPipelineReader createPipelineReader(XMLPipeline pipeline) throws SAXException {
        XMLReader reader = XMLReaderFactory.createXMLReader(false);
        XMLPipelineFilter filter = wrapper.createPipelineFilter(pipeline);
        filter.setParent(reader);
        return filter;
    }

    public DynamicRuleConfigurator getRuleConfigurator() {
        return ruleConfigurator;
    }

    public XMLProcess createFlowControlProcess() {
        return new FlowControlProcess();
    }

    public DynamicProcessConfiguration createDynamicProcessConfiguration() {
        return new SimpleDynamicProcessConfiguration();
    }

    public DynamicProcess createDynamicProcess(XMLPipelineConfiguration configuration) {
        DynamicProcessConfiguration dynamicConfig = SimpleDynamicProcess.getDynamicConfiguration(configuration);
        return wrapper.createDynamicProcess(dynamicConfig);
    }

    public DynamicProcess createDynamicProcess(DynamicProcessConfiguration configuration) {
        return new SimpleDynamicProcess(configuration);
    }

    public XMLPipelineConfiguration createPipelineConfiguration() {
        SimpleXMLPipelineConfiguration simpleXMLPipelineConfiguration = new SimpleXMLPipelineConfiguration();
        simpleXMLPipelineConfiguration.storeConfiguration(MonitoringConfiguration.class, monitoringConfiguration);
        return simpleXMLPipelineConfiguration;
    }

    public XMLPipeline createPipeline(XMLPipelineContext context) {
        return new XMLPipelineProcessImpl(context);
    }

    public XMLPipeline createDynamicPipeline(XMLPipelineContext context) {
        try {
            return new SimpleDynamicProcess(context);
        } catch (SAXException e) {
            throw new ExtendedRuntimeException("Could not create a pipeline", e);
        }
    }

    public NamespaceFactory getNamespaceFactory() {
        return NamespaceFactory.getDefaultInstance();
    }

    public ExpressionFactory getExpressionFactory() {
        return ExpressionFactory.getDefaultInstance();
    }

    public PipelineRecorder createPipelineRecorder() {
        return new ContextPipelineRecorder();
    }

    public InternalXMLPipelineFactory createWrappableFactory(InternalXMLPipelineFactory wrapper) {
        return new XMLPipelineFactoryImpl(wrapper);
    }

    public FlowControlManager createFlowControlManager() {
        return new SimpleFlowControlManager();
    }

    /**
     * Factor the DynamicRuleConfigurator that the
     *
     * {@link #getRuleConfigurator} will return
     * @return a DynamicRuleConfigurator instance
     */
    private DynamicRuleConfigurator createRuleConfigurator() {
        return new DynamicRuleConfigurator() {

            public void configure(DynamicProcessConfiguration configuration) {
                PipelineOperationFactory.getDefaultInstance().getRuleConfigurator().configure(configuration);
                URIDriverFactory.getDefaultInstance().getRuleConfigurator().configure(configuration);
                PicasaDriverFactory.getDefaultInstance().getRuleConfigurator().configure(configuration);
                FlickrDriverFactory.getDefaultInstance().getRuleConfigurator().configure(configuration);
                GoogleDocsDriverFactory.getDefaultInstance().getRuleConfigurator().configure(configuration);
                WebDriverFactory.getDefaultInstance().getRuleConfigurator().configure(configuration);
                TemplateFactory.getDefaultInstance().getRuleConfigurator().configure(configuration);
                WebServiceDriverFactory.getDefaultInstance().getRuleConfigurator().configure(configuration);
                ServletOperationFactory.getDefaultInstance().getRuleConfigurator().configure(configuration);
                PipelineExtensionFactory.getDefaultInstance().extendRules(configuration);
            }
        };
    }
}
