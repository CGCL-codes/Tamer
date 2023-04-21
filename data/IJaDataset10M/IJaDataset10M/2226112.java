package org.hibernate.jsr303.tck.tests.constraints.groups.groupsequence;

import com.google.gwt.junit.client.GWTTestCase;
import org.hibernate.jsr303.tck.util.client.Failing;
import org.hibernate.jsr303.tck.util.client.NonTckTest;
import javax.validation.GroupDefinitionException;

/**
 * Test wrapper for {@link SequenceResolutionTest}.
 */
public class SequenceResolutionGwtTest extends GWTTestCase {

    private final SequenceResolutionTest delegate = new SequenceResolutionTest();

    @Override
    public String getModuleName() {
        return "org.hibernate.jsr303.tck.tests.constraints.groups.groupsequence.TckTest";
    }

    @NonTckTest
    public void testDummy() {
    }

    @Failing(issue = 6291)
    public void testGroupSequenceContainerOtherGroupSequences() {
        try {
            delegate.testGroupSequenceContainerOtherGroupSequences();
            fail("Expected a " + GroupDefinitionException.class);
        } catch (GroupDefinitionException expected) {
        }
    }

    @Failing(issue = 6291)
    public void testInvalidDefinitionOfDefaultSequenceInEntity() {
        try {
            delegate.testInvalidDefinitionOfDefaultSequenceInEntity();
            fail("Expected a " + GroupDefinitionException.class);
        } catch (GroupDefinitionException expected) {
        }
    }

    @Failing(issue = 6291)
    public void testOnlyFirstGroupInSequenceGetEvaluated() {
        delegate.testOnlyFirstGroupInSequenceGetEvaluated();
    }
}
