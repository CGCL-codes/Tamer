package net.sourceforge.pmd.eclipse.core;

import java.util.Iterator;
import java.util.Set;
import junit.framework.TestCase;
import net.sourceforge.pmd.RuleSet;
import net.sourceforge.pmd.RuleSetFactory;
import net.sourceforge.pmd.RuleSetNotFoundException;
import net.sourceforge.pmd.eclipse.plugin.PMDPlugin;

/**
 * Test the PMD Core plugin
 *
 * @author Philippe Herlin
 *
 */
public class PMDCorePluginTest extends TestCase {

    /**
     * Constructor for PMDPluginTest.
     *
     * @param name
     */
    public PMDCorePluginTest(String name) {
        super(name);
    }

    /**
     * Test that the core plugin has been instantiated
     *
     */
    public void testPMDPluginNotNull() {
        assertNotNull("The Core Plugin has not been instantiated", PMDPlugin.getDefault());
    }

    /**
     * Test that we can get a ruleset manager
     *
     */
    public void testRuleSetManagerNotNull() {
        assertNotNull("Cannot get a ruleset manager", PMDPlugin.getDefault().getRuleSetManager());
    }

    /**
     * Test all the known PMD rulesets has been registered For this test to
     * work, no fragment or only the test plugin fragment should be installed.
     *
     */
    public void testStandardPMDRuleSetsRegistered() throws RuleSetNotFoundException {
        Set<RuleSet> registeredRuleSets = PMDPlugin.getDefault().getRuleSetManager().getRegisteredRuleSets();
        assertFalse("No registered rulesets!", registeredRuleSets.isEmpty());
        RuleSetFactory factory = new RuleSetFactory();
        Iterator<RuleSet> iterator = factory.getRegisteredRuleSets();
        while (iterator.hasNext()) {
            RuleSet ruleSet = iterator.next();
            assertTrue("RuleSet \"" + ruleSet.getName() + "\" has not been registered", ruleSetRegistered(ruleSet, registeredRuleSets));
        }
    }

    /**
     * Test the default rulesets has been registered For this test to work, no
     * Fragment or only the test plugin fragment should be installed.
     *
     */
    public void testDefaultPMDRuleSetsRegistered() throws RuleSetNotFoundException {
        Set<RuleSet> defaultRuleSets = PMDPlugin.getDefault().getRuleSetManager().getRegisteredRuleSets();
        assertFalse("No registered default rulesets!", defaultRuleSets.isEmpty());
        RuleSetFactory factory = new RuleSetFactory();
        Iterator<RuleSet> iterator = factory.getRegisteredRuleSets();
        while (iterator.hasNext()) {
            RuleSet ruleSet = iterator.next();
            assertTrue("RuleSet \"" + ruleSet.getName() + "\" has not been registered", ruleSetRegistered(ruleSet, defaultRuleSets));
        }
    }

    /**
     * test if a ruleset is registered
     *
     * @param ruleSet
     * @param set
     * @return true if OK
     */
    private boolean ruleSetRegistered(RuleSet ruleSet, Set<RuleSet> set) {
        boolean registered = false;
        Iterator<RuleSet> i = set.iterator();
        while (i.hasNext() && !registered) {
            RuleSet registeredRuleSet = i.next();
            registered = registeredRuleSet.getName().equals(ruleSet.getName());
        }
        return registered;
    }
}
