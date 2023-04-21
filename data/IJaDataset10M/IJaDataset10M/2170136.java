package com.volantis.xml.pipeline.sax.drivers.picasa;

import com.volantis.xml.pipeline.Namespace;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcessConfiguration;
import com.volantis.xml.pipeline.sax.dynamic.DynamicRuleConfigurator;
import com.volantis.xml.pipeline.sax.dynamic.NamespaceRuleSet;

/**
 * Default implementation of the {@link PicasaDriverFactory} class
 */
public class DefaultPicasaDriverFactory extends PicasaDriverFactory {

    /**
     *  DynamicRuleConfigurator for this factory
     */
    protected DynamicRuleConfigurator ruleConfigurator;

    /**
     * Create a new <code>DefaultPicasaDriverFactory</code> instance
     */
    public DefaultPicasaDriverFactory() {
        ruleConfigurator = createRuleConfigurator();
    }

    public DynamicRuleConfigurator getRuleConfigurator() {
        return ruleConfigurator;
    }

    /**
     * Factor the DynamicRuleConfigurator that the
     * {@link #getRuleConfigurator} will return
     * @return a DynamicRuleConfigurator instance
     */
    protected DynamicRuleConfigurator createRuleConfigurator() {
        return new DynamicRuleConfigurator() {

            public void configure(DynamicProcessConfiguration configuration) {
                NamespaceRuleSet ruleSet = configuration.getNamespaceRules(Namespace.PICASA.getURI(), true);
                ruleSet.addRule("list-photos", ListPhotosRule.getDefaultInstance());
            }
        };
    }
}
