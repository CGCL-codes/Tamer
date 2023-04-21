package net.stickycode.deploy.cli;

import org.junit.Test;
import net.stickycode.configured.MissingConfigurationException;
import net.stickycode.stereotype.Configured;
import static org.fest.assertions.Assertions.assertThat;

public class StickyCommandLineTest {

    private static class Sample {

        @Configured
        String value;

        @Configured
        Boolean doSomething = false;
    }

    @Test(expected = MissingConfigurationException.class)
    public void noConfiguration() {
        sample();
    }

    @Test
    public void booleanConfiguration() {
        Sample target = sample("--value");
        assertThat(target.value).isEqualTo("true");
        assertThat(target.doSomething).isFalse();
    }

    @Test
    public void configurationValue() {
        Sample target = sample("--value=blah", "--doSomething");
        assertThat(target.value).isEqualTo("blah");
        assertThat(target.doSomething).isTrue();
    }

    @Test
    public void reversedBoolean() {
        Sample target = sample("--value=blah", "--no-doSomething");
        assertThat(target.value).isEqualTo("blah");
        assertThat(target.doSomething).isFalse();
    }

    private Sample sample(String... args) {
        Sample target = new Sample();
        new StickyCommandLine(args).configure(target);
        return target;
    }
}
