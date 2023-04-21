package org.tripcom.security.policies;

import org.junit.After;
import org.junit.Before;
import org.tripcom.security.bus.Bus;
import org.tripcom.security.bus.JavaSpaceBus;
import org.tripcom.security.bus.LocalJavaSpace;
import org.tripcom.security.stubs.TSAdapterStub;

public class TSAdapterPolicyStrategyTest extends AbstractPolicyStrategyTest<TSAdapterPolicyStrategy> {

    private Bus bus;

    private TSAdapterStub tsAdapter;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        bus = new JavaSpaceBus(new LocalJavaSpace());
        strategy = new TSAdapterPolicyStrategy(bus);
        tsAdapter = new TSAdapterStub(bus, new SimplePolicyStrategy());
        tsAdapter.start();
    }

    @After
    @Override
    public void tearDown() throws Exception {
        tsAdapter.stop();
        tsAdapter = null;
        strategy = null;
        bus = null;
        super.tearDown();
    }
}
