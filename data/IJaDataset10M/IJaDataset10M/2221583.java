package com.googlecode.bdoc.diff.domain;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import com.googlecode.bdoc.Ref;
import com.googlecode.bdoc.Story;
import com.googlecode.bdoc.diff.domain.ClassBehaviourDiff;
import com.googlecode.bdoc.doc.domain.ClassBehaviour;
import com.googlecode.bdoc.doc.domain.Scenario;
import com.googlecode.bdoc.doc.domain.Specification;
import com.googlecode.bdoc.doc.domain.Statement;
import com.googlecode.bdoc.doc.domain.TestMethod;
import com.googlecode.bdoc.doc.testdata.TestWithGeneralBehaviour;

/**
 * @author Per Otto Bergum Christensen
 */
@Ref(Story.DIFF_OF_BDOCS)
public class TestClassBehaviourDiff {

    private static final String GIVE_A_STATEMENT = "giveAStatement";

    private static final String SHOULD_BEHAVE_LIKE_THAT = "shouldBehaveLikeThat";

    private static final String GIVEN_WHEN_THEN = "givenWhenThen";

    private final ClassBehaviour emptyClassBehaviour = new ClassBehaviour(TestAsTestdata.class);

    private final ClassBehaviour nonEmptyClassBehaviour = new ClassBehaviour(TestAsTestdata.class);

    {
        nonEmptyClassBehaviour.addBehaviour(new TestMethod(TestWithGeneralBehaviour.class, GIVEN_WHEN_THEN));
        nonEmptyClassBehaviour.addBehaviour(new TestMethod(TestWithGeneralBehaviour.class, SHOULD_BEHAVE_LIKE_THAT));
        nonEmptyClassBehaviour.addBehaviour(new TestMethod(TestWithGeneralBehaviour.class, GIVE_A_STATEMENT));
    }

    @Test
    public void shouldReturnNewScenarios() {
        ClassBehaviourDiff diff = new ClassBehaviourDiff(emptyClassBehaviour, nonEmptyClassBehaviour);
        assertTrue(diff.getNewScenarios().contains(new Scenario(GIVEN_WHEN_THEN)));
    }

    @Test
    public void shouldReturnDeletedScenarios() {
        ClassBehaviourDiff diff = new ClassBehaviourDiff(nonEmptyClassBehaviour, emptyClassBehaviour);
        assertTrue(diff.getDeletedScenarios().contains(new Scenario(GIVEN_WHEN_THEN)));
    }

    @Test
    public void shouldReturnNewSpecifications() {
        ClassBehaviourDiff diff = new ClassBehaviourDiff(emptyClassBehaviour, nonEmptyClassBehaviour);
        assertTrue(diff.getNewSpecifications().contains(new Specification(SHOULD_BEHAVE_LIKE_THAT)));
    }

    @Test
    public void shouldReturnDeletedSpecifications() {
        ClassBehaviourDiff diff = new ClassBehaviourDiff(nonEmptyClassBehaviour, emptyClassBehaviour);
        assertTrue(diff.getDeletedSpecifications().contains(new Specification(SHOULD_BEHAVE_LIKE_THAT)));
    }

    @Test
    public void shouldReturnNewStatements() {
        ClassBehaviourDiff diff = new ClassBehaviourDiff(emptyClassBehaviour, nonEmptyClassBehaviour);
        assertTrue(diff.getNewStatements().contains(new Statement(GIVE_A_STATEMENT)));
    }

    @Test
    public void shouldReturnDeletedStatements() {
        ClassBehaviourDiff diff = new ClassBehaviourDiff(nonEmptyClassBehaviour, emptyClassBehaviour);
        assertTrue(diff.getDeletedStatements().contains(new Statement(GIVE_A_STATEMENT)));
    }

    public class TestAsTestdata {
    }
}
