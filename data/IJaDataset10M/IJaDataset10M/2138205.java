package com.custardsource.cache.jboss2.replacement;

import org.jboss.cache.ConfigureException;
import org.jboss.cache.eviction.EvictionConfiguration;
import org.jboss.cache.xml.XmlHelper;
import org.w3c.dom.Element;
import com.custardsource.cache.jboss2.MultipleQueueConfigurator;
import com.custardsource.cache.policy.replacement.FixedReplacementConfiguration;

public class FixedReplacementConfigurator extends MultipleQueueConfigurator<FixedReplacementConfiguration> {

    public static final String T1_SIZE = "t1Size";

    @Override
    public void parseXMLConfig(Element element) throws ConfigureException {
        super.parseXMLConfig(element);
        String fixedT1Size = XmlHelper.getAttr(element, T1_SIZE, EvictionConfiguration.ATTR, EvictionConfiguration.NAME);
        if (fixedT1Size == null || fixedT1Size.equals("")) {
            throw new ConfigureException("FixedReplacementConfiguration requires t1Size attribute");
        }
        config.setT1Size(Integer.parseInt(fixedT1Size));
    }

    @Override
    protected FixedReplacementConfiguration createConfig() {
        return new FixedReplacementConfiguration();
    }
}
