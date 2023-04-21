package org.apache.felix.bundleplugin;

import java.io.File;
import org.apache.maven.plugin.testing.stubs.ArtifactStub;
import org.codehaus.plexus.PlexusTestCase;

/**
 * Common methods for bundle plugin testing
 * 
 * @author <a href="mailto:carlos@apache.org">Carlos Sanchez</a>
 * @version $Id: AbstractBundlePluginTest.java 616180 2008-01-29 06:22:10Z mcculls $
 */
public abstract class AbstractBundlePluginTest extends PlexusTestCase {

    protected ArtifactStub getArtifactStub() {
        ArtifactStub artifact = new ArtifactStub();
        artifact.setGroupId("group");
        artifact.setArtifactId("artifact");
        artifact.setVersion("1.0");
        return artifact;
    }

    protected File getTestBundle() {
        String osgiBundleFileName = "org.apache.maven.maven-model_2.1.0.SNAPSHOT.jar";
        return getTestFile(getBasedir(), "src/test/resources/" + osgiBundleFileName);
    }
}
