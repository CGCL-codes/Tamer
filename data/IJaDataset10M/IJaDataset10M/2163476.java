package net.stickycode.stile;

import static org.fest.assertions.Assertions.assertThat;
import net.stickycode.mockwire.MockwireConfigured;
import net.stickycode.mockwire.UnderTest;
import net.stickycode.mockwire.junit4.MockwireRunner;
import net.stickycode.resource.Resources;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(MockwireRunner.class)
public class StilerTest {

    @UnderTest({ "sourceDirectory=src", "outputDirectory=target/stile" })
    CommandLineWorkspace workspace;

    @UnderTest
    ResourceListeners listeners;

    @UnderTest
    ProcessesAnnotatedMethodProcessor processor;

    @UnderTest
    Stiler stiler;

    @UnderTest("sphere=mingle")
    JavaCompilerStiler compiler;

    @UnderTest("sphere=mingle")
    JavaSourcesStiler sources;

    @Test
    public void stiler() {
        stiler.register(compiler);
        stiler.register(sources);
        Resources resources = stiler.produce(ResourcesTypes.JavaSource);
        assertThat(resources).hasSize(2);
        Resources classes = stiler.produce(ResourcesTypes.JavaByteCode);
        assertThat(classes).hasSize(2);
    }
}
