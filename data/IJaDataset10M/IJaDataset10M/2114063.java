package test.net.sourceforge.pmd.core;

import java.util.Iterator;
import java.util.Set;
import net.sourceforge.pmd.RuleSet;
import net.sourceforge.pmd.RuleSetFactory;
import net.sourceforge.pmd.RuleSetNotFoundException;
import net.sourceforge.pmd.core.PMDCorePlugin;
import net.sourceforge.pmd.core.PluginConstants;
import junit.framework.TestCase;

/**
 * Test the PMD Core plugin
 * 
 * @author Philippe Herlin
 * @version $Revision: 3646 $
 * 
 * $Log$
 * Revision 1.2  2005/07/02 14:32:01  phherlin
 * Implement the RuleSets extension points new tests
 *
 * Revision 1.1  2005/06/15 21:14:56  phherlin
 * Create the project for the Eclipse plugin unit tests
 *
 *
 */
public class PMDCorePluginTest extends TestCase {

    /**
     * Constructor for PMDCorePluginTest.
     * @param name
     */
    public PMDCorePluginTest(String name) {
        super(name);
    }

    /**
     * Test that the core plugin has been instantiated
     *
     */
    public void testPMDCorePluginNotNull() {
        assertNotNull("The Core Plugin has not been instantiated", PMDCorePlugin.getDefault());
    }

    /**
     * Test that we can get a ruleset manager
     *
     */
    public void testRuleSetManagerNotNull() {
        assertNotNull("Cannot get a ruleset manager", PMDCorePlugin.getDefault().getRuleSetManager());
    }

    /**
     * Test all the known PMD rulesets has been registered
     * For this test to work, no fragement or only the test plugin fragment should be installed.
     *
     */
    public void testStandardPMDRuleSetsRegistered() throws RuleSetNotFoundException {
        Set registeredRuleSets = PMDCorePlugin.getDefault().getRuleSetManager().getRegisteredRuleSets();
        assertFalse("No registered rulesets!", registeredRuleSets.isEmpty());
        RuleSetFactory factory = new RuleSetFactory();
        for (int i = 0; i < PluginConstants.PMD_RULESETS.length; i++) {
            RuleSet ruleSet = factory.createRuleSet(PluginConstants.PMD_RULESETS[i]);
            assertTrue("RuleSet \"" + PluginConstants.PMD_RULESETS[i] + "\" has not been registered", ruleSetRegistered(ruleSet, registeredRuleSets));
        }
    }

    /**
     * Test the default rulesets has been registered
     * For this test to work, no fragement or only the test plugin fragment should be installed.
     *
     */
    public void testDefaultPMDRuleSetsRegistered() throws RuleSetNotFoundException {
        Set defaultRuleSets = PMDCorePlugin.getDefault().getRuleSetManager().getRegisteredRuleSets();
        assertFalse("No registered default rulesets!", defaultRuleSets.isEmpty());
        RuleSetFactory factory = new RuleSetFactory();
        for (int i = 0; i < PluginConstants.PMD_RULESETS.length; i++) {
            RuleSet ruleSet = factory.createRuleSet(PluginConstants.PMD_RULESETS[i]);
            assertTrue("RuleSet \"" + PluginConstants.PMD_RULESETS[i] + "\" has not been registered", ruleSetRegistered(ruleSet, defaultRuleSets));
        }
    }

    /**
     * test if a ruleset is registered
     * @param ruleSet
     * @param set
     * @return true if ok
     */
    private boolean ruleSetRegistered(RuleSet ruleSet, Set set) {
        boolean registered = false;
        Iterator i = set.iterator();
        while (i.hasNext() && !registered) {
            RuleSet registeredRuleSet = (RuleSet) i.next();
            registered = registeredRuleSet.getName().equals(ruleSet.getName());
        }
        return registered;
    }
}
