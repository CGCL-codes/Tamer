package org.emergent.antbite.savant.test;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Arrays;
import junit.framework.TestCase;
import org.emergent.antbite.savant.Artifact;
import org.emergent.antbite.savant.Dependencies;
import org.emergent.antbite.savant.DependencyMediator;
import org.emergent.antbite.savant.SavantException;
import org.emergent.antbite.savant.SavantInternetProcess;
import org.emergent.antbite.savant.Workflow;

/**
 * <p>
 * This class is a test case that tests the dependency mediator
 * and how the artifact dependencies are resolved.
 * </p>
 *
 * @author Brian Pontarelli
 */
public class DependencyMediatorDepsTest extends TestCase {

    public DependencyMediatorDepsTest(String s) {
        super(s);
    }

    /**
     * Tests that the mediator fetches all dependencies using the deps XML file.
     */
    public void testArtifactDeps() throws SavantException, MalformedURLException {
        Artifact a = new Artifact();
        a.setGroup("my_group");
        a.setName("my_complete_artifact");
        a.setProjectname("my_project");
        a.setType("jar");
        a.setVersion("42");
        Dependencies d = new Dependencies();
        d.addArtifact(a);
        File root = new File("test/savant");
        SavantInternetProcess sip = new SavantInternetProcess();
        sip.setDefaultdomain(root.toURL().toString());
        Workflow w = new Workflow();
        w.addProcess(sip);
        File cache = new File("test/cache");
        File toClear = new File(cache, "my_group/my_project");
        File[] files = toClear.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            System.out.println("Deleting: " + file.getAbsolutePath());
            file.delete();
        }
        DependencyMediator dm = new DependencyMediator();
        dm.setLocalCacheDir(cache);
        dm.setDependencies(d);
        dm.setWorkflow(w);
        dm.mediate();
        files = toClear.listFiles();
        assertEquals(4, files.length);
        Arrays.sort(files);
        assertTrue(Arrays.binarySearch(files, new File(toClear, "my_name-2.0.exe")) >= 0);
        assertTrue(Arrays.binarySearch(files, new File(toClear, "my_md5_artifact-2.0.jar")) >= 0);
        assertTrue(Arrays.binarySearch(files, new File(toClear, "my_complete_artifact-42.jar")) >= 0);
    }
}
