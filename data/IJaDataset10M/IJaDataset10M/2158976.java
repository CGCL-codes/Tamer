package configurator.task.deployment;

import junit.framework.TestCase;
import org.apache.tools.ant.BuildException;

/**
 * @author Justin Tomich
 */
public class WebDeploymentTaskTest extends TestCase {

    public void testAddWithDups() {
        WebDeploymentTask dep = new WebDeploymentTask(null);
        dep.add(new VersionedDescriptor("name", new String[] { "1" }, "/file"));
        try {
            dep.add(new VersionedDescriptor("name", new String[] { "1" }, "/file"));
            fail("shoulda hucked on duplicate descriptor!");
        } catch (BuildException e) {
        }
    }

    public void testAddNoDups() {
        WebDeploymentTask dep = new WebDeploymentTask(null);
        dep.add(new VersionedDescriptor("name", new String[] { "1" }, "/file"));
        dep.add(new VersionedDescriptor("name2", new String[] { "1" }, "/file"));
    }
}
