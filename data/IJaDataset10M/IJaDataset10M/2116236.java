package net.sf.sail.test;

import junit.framework.TestCase;
import net.sf.sail.core.beans.Pod;
import net.sf.sail.core.uuid.PodUuid;

/**
 * @author turadg
 */
public class PodTest extends TestCase {

    private Pod pod1a;

    private Pod pod1b;

    private Pod pod1c;

    private Pod pod2;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        pod1a = new Pod();
        pod1a.setPodId(new PodUuid("bbbbbb01-0000-0000-0000-000000000000"));
        pod1b = new Pod();
        pod1b.setPodId(new PodUuid("bbbbbb01-0000-0000-0000-000000000000"));
        pod1c = new Pod();
        pod1c.setPodId(new PodUuid("bbbbbb01-0000-0000-0000-000000000000"));
        pod1c.add(new String("modified"));
        pod2 = new Pod();
        pod2.setPodId(new PodUuid("bbbbbb02-0000-0000-0000-000000000000"));
    }

    public void testEqualsObject() {
        assertFalse(pod1a.equals(pod1c));
        assertFalse(pod1a.equals(pod2));
    }
}
