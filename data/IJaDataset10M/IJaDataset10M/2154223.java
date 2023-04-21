package net.stickycode.stile.cli;

import static org.fest.assertions.Assertions.assertThat;
import net.stickycode.mockwire.MockwireConfigured;
import net.stickycode.mockwire.UnderTest;
import net.stickycode.mockwire.junit4.MockwireRunner;
import net.stickycode.stile.artifact.Artifact;
import net.stickycode.stile.artifact.xml.v1.XmlArtifactParser;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(MockwireRunner.class)
@MockwireConfigured
public class CliTest {

    @UnderTest
    StileCli cli;

    @UnderTest
    XmlArtifactParser parser;

    @Test(expected = ArtifactResourceCannotBeFoundException.class)
    public void notFound() {
        cli.loadArtifact("notFound.xml");
    }

    @Test
    public void simple() {
        Artifact artifact = cli.loadArtifact("simple.xml");
        assertThat(artifact).isNotNull();
        assertThat(artifact.getId()).isEqualTo("net.stickycode.stile.core");
        assertThat(artifact.getVersion().toString()).isEqualTo("1.9");
    }

    @Test
    public void sample1() {
        Artifact artifact = cli.loadArtifact("sample1.xml");
        assertThat(artifact).isNotNull();
    }
}
