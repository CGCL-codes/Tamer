package com.novell.ldapchai.tests;

import com.novell.ldapchai.ChaiPasswordRule;
import junit.framework.TestCase;
import org.junit.Assert;
import java.util.HashSet;
import java.util.Set;

public class PasswordPolicyTester extends TestCase {

    protected void setUp() throws Exception {
        TestHelper.setUp();
    }

    public void testUniquePasswordRules() throws Exception {
        final int ruleCount = ChaiPasswordRule.values().length;
        final Set<String> rulePropNames = new HashSet<String>();
        for (final ChaiPasswordRule rule : ChaiPasswordRule.values()) {
            rulePropNames.add(rule.getKey());
        }
        Assert.assertEquals(ruleCount, rulePropNames.size());
    }
}
