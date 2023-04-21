package org.hibernate.jsr303.tck.tests.constraints.groups;

import org.hibernate.jsr303.tck.tests.constraints.groups.GroupSequenceContainingDefaultValidatorFactory.GroupSequenceContainingDefaultValidator;
import org.hibernate.jsr303.tck.tests.constraints.groups.GroupSequenceWithNoImplicitDefaultGroupValidatorFactory.TestValidator;
import org.hibernate.jsr303.tck.util.TckCompileTestCase;
import javax.validation.GroupDefinitionException;

/**
 * Test wrapper for {@link DefaultGroupRedefinitionTest} tests that are meant to
 * fail to compile.
 */
public class DefaultGroupRedefinitionCompileTest extends TckCompileTestCase {

    public void testGroupSequenceContainingDefault() {
        assertValidatorFailsToCompile(GroupSequenceContainingDefaultValidator.class, GroupDefinitionException.class, "Unable to create a validator for " + "org.hibernate.jsr303.tck.tests.constraints.groups." + "DefaultGroupRedefinitionTest.AddressWithDefaultInGroupSequence " + "because 'Default.class' cannot appear in default group " + "sequence list.");
    }

    public void testGroupSequenceWithNoImplicitDefaultGroup() {
        assertValidatorFailsToCompile(TestValidator.class, GroupDefinitionException.class, "Unable to create a validator for " + "org.hibernate.jsr303.tck.tests.constraints.groups." + "DefaultGroupRedefinitionTest.AddressWithDefaultInGroupSequence " + "because 'Default.class' cannot appear in default group " + "sequence list.");
    }
}
