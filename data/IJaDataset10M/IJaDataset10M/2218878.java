package org.jdiagnose.remote;

import org.jdiagnose.remote.VMIDGuidGenerator;
import junit.framework.TestCase;

/**
 * @author jmccrindle
 */
public class VMIDGuidGeneratorTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Constructor for VMIDGuidGeneratorTest.
     * @param name
     */
    public VMIDGuidGeneratorTest(String name) {
        super(name);
    }

    public void testGenerate() throws Exception {
        VMIDGuidGenerator generator = new VMIDGuidGenerator();
        String one = generator.nextGuid();
        assertNotNull(one);
        String two = generator.nextGuid();
        assertNotNull(two);
        assertTrue(!one.equals(two));
        System.out.println(one);
    }
}
