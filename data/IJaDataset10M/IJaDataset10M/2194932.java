package org.rakiura.cpn.sample;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.rakiura.cpn.*;

/**
 * Test utility for TwoTasksNet.
 * 
 *<br>
 * TestTwoTasksExample.java<br>
 * <br>
 * Created: Fri Oct 29 17:42:14 1999<br>
 *
 * @author Mariusz Nowostawski
 * @version $Revision: 1.6 $
 */
public class TestTwoTasksExample extends TestCase {

    private TwoTasksNet net;

    private Simulator sim;

    public TestTwoTasksExample(String name) {
        super(name);
    }

    /** Setup. */
    protected void setUp() {
    }

    /**
   */
    public void testPassingToken() {
        Multiset m = new Multiset();
        m.add(new Integer(0));
        this.net = new TwoTasksNet(m);
        this.sim = new BasicSimulator(this.net);
        this.sim.run();
        final Multiset res = this.net.outputPlace().getTokens();
        assertEquals("OutputPlace marking size", 1, res.size());
        assertEquals("OutputPlace marking value", 2, ((Number) res.getAny()).intValue());
    }

    /**
   */
    public void testPassingTokens2() {
        Multiset m = new Multiset();
        for (int i = 0; i < 2; i++) {
            m.add(new Integer(i));
        }
        this.net = new TwoTasksNet(m);
        this.sim = new BasicSimulator(this.net);
        this.sim.run();
        final Multiset res = this.net.outputPlace().getTokens();
        assertEquals("OutputPlace marking size", 2, res.size());
    }

    /**
   * Test suite. */
    public static Test suite() {
        return new TestSuite(TestTwoTasksExample.class);
    }
}
