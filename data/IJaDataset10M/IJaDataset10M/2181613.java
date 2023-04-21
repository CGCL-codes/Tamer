package com.volantis.xml.pipeline.sax;

import com.volantis.shared.environment.EnvironmentInteraction;
import com.volantis.xml.pipeline.Namespace;
import com.volantis.xml.pipeline.extensions.PipelineExtensionFactory;
import com.volantis.xml.pipeline.sax.adapter.AdapterProcess;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcessConfiguration;
import com.volantis.xml.pipeline.sax.dynamic.DynamicRuleConfigurator;
import com.volantis.xml.pipeline.sax.dynamic.NamespaceRuleSet;
import com.volantis.xml.pipeline.sax.dynamic.rules.AbstractAddAdapterRule;
import com.volantis.xml.pipeline.sax.expression.VariableDeclarationAdapterProcess;
import com.volantis.xml.pipeline.sax.expression.VariableScopeAdapterProcess;
import com.volantis.xml.pipeline.sax.impl.XMLPipelineFactoryImpl;
import com.volantis.xml.pipeline.sax.template.AlternateComplexityRule;
import com.volantis.xml.pipeline.sax.template.Counter;
import com.volantis.xml.pipeline.sax.template.CounterAdapterProcess;
import com.volantis.xml.pipeline.sax.template.ReenterRule;
import com.volantis.xml.pipeline.sax.tryop.FailOnExecuteRule;
import com.volantis.xml.pipeline.sax.tryop.GenerateErrorRule;

/**
 * Implementation of the PipelineFactory interface for use in test cases.
 */
public class TestPipelineFactory extends XMLPipelineFactoryImpl implements Counter {

    /**
     * Constant that identifies the namespace to use for the
     * special integration processes
     */
    public static final Namespace INTEGRATION = new Namespace("integration") {
    };

    /**
     * Counter variable
     */
    private long counter = 0;

    public long get() {
        return counter++;
    }

    /**
     * Factory method that creates a PipelineContext with an empty 
     * <code>XMLPipelineConfiguration</code> and a null 
     * <code>EnvironmentInteraction</code>.
     * @return an XMLPipelineContext instance
     */
    public XMLPipelineContext createPipelineContext() {
        XMLPipelineConfiguration pipelineConfiguration = createPipelineConfiguration();
        EnvironmentInteraction interaction = null;
        return createPipelineContext(pipelineConfiguration, interaction);
    }

    public DynamicRuleConfigurator getRuleConfigurator() {
        final DynamicRuleConfigurator standardRules = super.getRuleConfigurator();
        return new DynamicRuleConfigurator() {

            public void configure(DynamicProcessConfiguration configuration) {
                standardRules.configure(configuration);
                NamespaceRuleSet ruleSet = configuration.getNamespaceRules(INTEGRATION.getURI(), true);
                ruleSet.addRule("variableDeclaration", new AbstractAddAdapterRule() {

                    public AdapterProcess createAdapterProcess(DynamicProcess dynamicProcess) {
                        return new VariableDeclarationAdapterProcess();
                    }
                });
                ruleSet.addRule("variableScope", new AbstractAddAdapterRule() {

                    public AdapterProcess createAdapterProcess(DynamicProcess dynamicProcess) {
                        return new VariableScopeAdapterProcess();
                    }
                });
                ruleSet.addRule("counter", new AbstractAddAdapterRule() {

                    public AdapterProcess createAdapterProcess(DynamicProcess dynamicProcess) {
                        return new CounterAdapterProcess(TestPipelineFactory.this);
                    }
                });
                ruleSet.addRule("reenter", new ReenterRule());
                ruleSet.addRule("alternate-complexity", new AlternateComplexityRule());
                ruleSet.addRule("generateError", new GenerateErrorRule());
                ruleSet.addRule("failOnExecute", new FailOnExecuteRule());
            }
        };
    }
}
