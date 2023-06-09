package org.hamcrest.text;

import org.hamcrest.AbstractMatcherTest;
import org.hamcrest.Matcher;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;

public class IsEqualIgnoringCaseTest extends AbstractMatcherTest {

    protected Matcher<?> createMatcher() {
        return equalToIgnoringCase("irrelevant");
    }

    public void testIgnoresCaseOfCharsInString() {
        assertThat("HELLO", equalToIgnoringCase("heLLo"));
        assertThat("hello", equalToIgnoringCase("heLLo"));
        assertThat("HelLo", equalToIgnoringCase("heLLo"));
        assertThat("bye", not(equalToIgnoringCase("heLLo")));
    }

    public void testFailsIfAdditionalWhitespaceIsPresent() {
        assertThat("heLLo ", not(equalToIgnoringCase("heLLo")));
        assertThat(" heLLo", not(equalToIgnoringCase("heLLo")));
    }

    public void testFailsIfMatchingAgainstNull() {
        assertThat(null, not(equalToIgnoringCase("heLLo")));
    }

    public void testRequiresNonNullStringToBeConstructed() {
        try {
            equalToIgnoringCase(null);
            fail("Expected exception");
        } catch (IllegalArgumentException goodException) {
        }
    }

    public void testDescribesItselfAsCaseInsensitive() {
        assertDescription("eqIgnoringCase(\"heLLo\")", equalToIgnoringCase("heLLo"));
    }
}
