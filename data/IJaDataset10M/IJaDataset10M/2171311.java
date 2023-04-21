package com.volantis.mcs.runtime.configuration.xml;

import our.apache.commons.digester.Digester;
import com.volantis.mcs.runtime.configuration.ManagementConfiguration;

/**
 * Rule set used to parse Management informtaion from the mcs-config.xml
 * configuration file.
 */
public class ManagementRuleSet extends PrefixRuleSet {

    /**
     * Standard constructor.
     * @param prefix the path to the element which will act as a parent to
     * this.
     */
    public ManagementRuleSet(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Add the parse rules for management sub elements.
     * @param digester
     */
    public void addRuleInstances(Digester digester) {
        final String pattern = prefix + "/management";
        digester.addObjectCreate(pattern, ManagementConfiguration.class);
        digester.addSetNext(pattern, "setManagementConfiguration");
        final PageTrackingRuleSet pageTrackingRuleSet = new PageTrackingRuleSet(pattern);
        pageTrackingRuleSet.addRuleInstances(digester);
    }
}
