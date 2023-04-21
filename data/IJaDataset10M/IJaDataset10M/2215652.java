package com.volantis.xml.pipeline.sax.impl.dependency;

import com.volantis.xml.pipeline.sax.dynamic.NamespaceRuleSet;
import com.volantis.xml.pipeline.sax.dynamic.DynamicRuleConfigurator;
import com.volantis.xml.pipeline.sax.impl.dynamic.SingleNamespaceConfigurator;

public class DependencyTestRuleConfigurator extends SingleNamespaceConfigurator {

    /**
     * The default instance.
     */
    private static final DynamicRuleConfigurator DEFAULT_INSTANCE = new DependencyTestRuleConfigurator();

    public static final String NAMESPACE = "http://www.volantis.com/test/dependency";

    /**
     * Get the default instance.
     *
     * @return The default instance.
     */
    public static DynamicRuleConfigurator getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    public static final AggregatorRule AGGREGATOR_RULE = new AggregatorRule();

    public static final DependencyRule DEPENDENCY_RULE = new DependencyRule();

    public DependencyTestRuleConfigurator() {
        super(NAMESPACE);
    }

    protected void configure(NamespaceRuleSet ruleSet) {
        ruleSet.addRule("aggregator", AGGREGATOR_RULE);
        ruleSet.addRule("dependency", DEPENDENCY_RULE);
    }
}
