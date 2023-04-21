package org.crap4j.eclipse.updatesite;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import org.crap4j.external.AntRunner;
import org.junit.Assert;
import org.junit.Test;

public class BuildUpdateBehaviourTest {

    @Test
    public void shouldHaveCleanUpdateFolder() throws URISyntaxException {
        File buildOutputDir = new File("/Users/bobevans/Documents/projects/crap4j/org.crap4j.eclipse.updatesite/build/update");
        runBuild();
        Assert.assertTrue(buildOutputDir.exists() && buildOutputDir.isDirectory() && buildOutputDir.list().length == 0);
    }

    private void runBuild() {
        AntRunner ar = new AntRunner("/Users/bobevans/Documents/projects/crap4j/org.crap4j.eclipse.updatesite/build.xml", "/Users/bobevans/Applications/ant", "/Users/bobevans/Applications/eclipse-3.3.1/plugins/org.junit4_4.3.1/junit.jar", "/Users/bobevans/Applications/ant", true);
        ar.run();
    }
}
