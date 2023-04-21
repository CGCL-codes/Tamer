package com.google.code.jetm.maven.data;

import static org.fest.assertions.Assertions.assertThat;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import com.google.code.jetm.maven.TimingReportMojo;

/**
 * Unit tests for {@link TimeUnit}.
 * 
 * @author jrh3k5
 * 
 */
public class TimeUnitTest {

    /**
     * A {@link Rule} used to test for thrown exceptions.
     */
    @Rule
    public ExpectedException expected = ExpectedException.none();

    /**
     * Test the retrieval of a time unit by the {@link TimingReportMojo} abbreviations for time units.
     */
    @Test
    public void testFromMojoAbbreviation() {
        assertThat(TimeUnit.fromMojoAbbreviation("millis")).isEqualTo(TimeUnit.MILLISECONDS);
        assertThat(TimeUnit.fromMojoAbbreviation("secs")).isEqualTo(TimeUnit.SECONDS);
    }

    /**
     * Looking up an unknown abbreviation should fail.
     */
    @Test
    public void testFromMojoAbbreviationUnknownAbbreviation() {
        final String abbreviation = "what is this i don't even";
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage("Unrecognized time unit abbreviation: " + abbreviation);
        TimeUnit.fromMojoAbbreviation(abbreviation);
    }

    /**
     * Test the retrieval of the display name.
     */
    @Test
    public void testGetDisplayName() {
        assertThat(TimeUnit.MILLISECONDS.getDisplayName()).isEqualTo("ms");
        assertThat(TimeUnit.SECONDS.getDisplayName()).isEqualTo("sec");
    }

    /**
     * Test the conversion of microseconds to seconds.
     */
    @Test
    public void testMillisecondsToSeconds() {
        assertThat(TimeUnit.SECONDS.fromMilliseconds(1500)).isEqualTo(1.5);
    }

    /**
     * Test the conversion between two microsecond values.
     */
    @Test
    public void testMillisecondsToMilliseconds() {
        assertThat(TimeUnit.MILLISECONDS.fromMilliseconds(1200)).isEqualTo(1200);
    }
}
