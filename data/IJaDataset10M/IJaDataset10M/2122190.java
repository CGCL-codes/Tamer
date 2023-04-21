package org.apache.zookeeper;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import org.apache.zookeeper.test.ClientBase;
import org.apache.zookeeper.version.util.VerGen;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Test VerGen, used during the build.
 *
 */
@RunWith(Parameterized.class)
public class VerGenTest extends ZKTestCase {

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] { { "1.2.3", new Object[] { 1, 2, 3, null } }, { "1.2.3-dev", new Object[] { 1, 2, 3, "dev" } }, { "1.2.3-SNAPSHOT", new Object[] { 1, 2, 3, "SNAPSHOT" } }, { "1.2.3-foo-bar+123", new Object[] { 1, 2, 3, "foo-bar+123" } } });
    }

    private String input;

    private Object[] expected;

    public VerGenTest(String input, Object[] expected) {
        this.input = input;
        this.expected = expected;
    }

    @Test
    public void testParser() {
        VerGen.Version v = VerGen.parseVersionString(input);
        Assert.assertEquals(expected[0], v.maj);
        Assert.assertEquals(expected[1], v.min);
        Assert.assertEquals(expected[2], v.micro);
        Assert.assertEquals(expected[3], v.qualifier);
    }

    @Test
    public void testGenFile() throws Exception {
        VerGen.Version v = VerGen.parseVersionString(input);
        File outputDir = ClientBase.createTmpDir();
        VerGen.generateFile(outputDir, v, 1, "Nov1");
        ClientBase.recursiveDelete(outputDir);
    }
}
