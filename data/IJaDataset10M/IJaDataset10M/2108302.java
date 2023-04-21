package com.wideplay.junitobjects.unittests;

import junit.framework.TestCase;
import com.wideplay.junitobjects.ObjectsBackplane;
import com.wideplay.junitobjects.mocks.MockProxy;
import com.wideplay.junitobjects.state.ObjectState;
import com.wideplay.junitobjects.state.ObjectStateDescriptor;
import com.wideplay.junitobjects.state.ObjectStatePrecondition;
import com.wideplay.junitobjects.state.StateChain;
import com.wideplay.junitobjects.testing.StateAssertion;
import com.wideplay.junitobjects.testing.metaphors.InstanceDescriptor;

public class StateChainTest extends TestCase {

    ObjectsBackplane tester;

    MockServiceCopy service;

    String currentMetaphor = "test";

    @Override
    protected void setUp() throws Exception {
        tester = new ObjectsBackplane(null) {

            public boolean isActiveMetaphor(String metaphor) {
                return true;
            }
        };
        super.setUp();
    }

    public void testAddMockObject() throws Exception {
        assertTrue(true);
    }

    public void testTriggerWithMock() throws Exception {
        StateChain chain = new StateChain();
        chain.metaphor = currentMetaphor;
        MockProxy proxy = new MockProxy() {

            public Object newInstance() {
                super.mockInterface = MockServiceCopy.class;
                return new MockServiceCopy() {

                    public Integer number() {
                        return new Integer(3);
                    }

                    public Integer add(Integer i, Integer j) {
                        return new Integer(3);
                    }

                    public void except() {
                        return;
                    }
                };
            }
        };
        proxy.newInstance();
        chain.addMockObject("service", proxy);
        InstanceDescriptor instanceDescriptor = new InstanceDescriptor(this, new Integer(3));
        StateAssertion assertion = new StateAssertion(null, null, null);
        ObjectStateDescriptor descriptor = new ObjectStateDescriptor() {

            public boolean assertState(Object object, boolean isThrowing) {
                return true;
            }
        };
        descriptor.addPropertyAssertion("test", assertion);
        ObjectStatePrecondition precondition = new ObjectStatePrecondition(descriptor) {

            public boolean assertState(Object object, boolean isThrowing) {
                return true;
            }
        };
        ObjectState state = new ObjectState("blah", descriptor, precondition);
        chain.addState("blah", state);
        assertTrue("Trigger with Mock test failed", chain.trigger(instanceDescriptor));
        assertNotNull(this.getService());
    }

    public MockServiceCopy getService() {
        return service;
    }

    public void setService(MockServiceCopy service) {
        this.service = service;
    }
}
