package org.translationcomponent.service.document.xml.attributes;

import org.translationcomponent.service.document.xml.StaxParserConfiguration;

public abstract class AttributeModifierAbstract implements AttributeModifier {

    private StaxParserConfiguration config;

    protected AttributeModifier chainedModifier;

    public void init(final StaxParserConfiguration config) {
        this.config = config;
        if (chainedModifier != null) {
            chainedModifier.init(config);
        }
    }

    protected StaxParserConfiguration getConfig() {
        return config;
    }

    public void setChainedModifier(final AttributeModifier chainedModifier) {
        this.chainedModifier = chainedModifier;
    }
}
